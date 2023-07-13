/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.css.internal;

import java.util.Arrays;
import objectos.css.StyleSheet;
import objectos.css.om.PropertyName;
import objectos.css.om.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;
import objectos.lang.Check;
import objectos.util.ByteArrays;

final class CssCompiler extends CssTemplateApi {

  private static final int MAX_INDEX = 1 << 24 - 1;

  private static final byte MARK_INTERNAL = -1;
  private static final byte MARK_VALUE3 = -4;

  byte[] aux;

  int auxIndex;

  int auxStart;

  byte[] main;

  int mainContents;

  int mainIndex;

  int mainStart;

  @Override
  public final StyleSheet compile() {
    return new CompiledStyleSheet(
      Arrays.copyOf(main, mainIndex)
    );
  }

  @Override
  public final void compilationStart() {
    aux = new byte[128];

    main = new byte[256];
  }

  @Override
  public final void declarationStart(PropertyName propertyName) {
    if (propertyName instanceof StandardName name) {
      // we mark the start of our aux list
      auxStart = auxIndex;

      // we mark:
      // 1) the start of the contents of the current declaration
      // 2) the start of our main list
      mainContents = mainStart = mainIndex;

      mainAdd(
        Bytes.DECLARATION,
        // indices take 3 bytes
        Bytes.NULL, Bytes.NULL, Bytes.NULL,
        name0(name), name1(name)
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + propertyName.getClass()
      );
    }
  }

  @Override
  public final void declarationValue(PropertyValue value) {
    if (value instanceof StandardName name) {
      // value is a keyword like color: currentcolor; or display: block;
      // store the enum ordinal
      auxAdd(
        MARK_VALUE3,
        Bytes.STANDARD_NAME,
        name0(name), name1(name)
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
  }

  @Override
  public final void declarationEnd() {
    // we iterate over each value added via declarationValue(PropertyValue)
    int auxMax = auxIndex;

    int idx = auxStart;

    // int internal = mainContents;

    while (idx < auxMax) {
      byte marker = aux[idx++];

      switch (marker) {
        case MARK_VALUE3 -> {
          mainAdd(aux[idx++], aux[idx++], aux[idx++]);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    mainAdd(
      Bytes.DECLARATION_END,

      idx0(mainContents), idx1(mainContents), idx2(mainContents),
      idx0(mainStart), idx1(mainStart), idx2(mainStart),
      Bytes.DECLARATION
    );

    setEndIndex();

    // we clear the aux list
    auxIndex = auxStart;
  }

  private void setEndIndex() {
    int endIndex;
    endIndex = mainIndex;

    // we skip the first byte proto
    main[mainStart + 1] = idx0(endIndex);
    main[mainStart + 2] = idx1(endIndex);
    main[mainStart + 3] = idx2(endIndex);
  }

  @Override
  public final void styleRuleStart() {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;

    mainAdd(
      Bytes.STYLE_RULE,
      // indices take 3 bytes
      Bytes.NULL, Bytes.NULL, Bytes.NULL
    );
  }

  @Override
  public final void styleRuleElement(StyleRuleElement element) {
    if (element instanceof StandardName name) {
      // element is a selector name
      // store the enum ordinal
      auxAdd(
        MARK_VALUE3,
        Bytes.STANDARD_NAME,
        name0(name), name1(name)
      );
    }

    else if (element == InternalInstruction.DECLARATION) {
      updateContents();

      auxAdd(MARK_INTERNAL);
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me"
      );
    }
  }

  private int mainIndex(int offset) {
    int idx0 = main[offset + 0];
    int idx1 = main[offset + 1] << 8;
    int idx2 = main[offset + 2] << 16;

    return idx2 | idx1 | idx0;
  }

  private void updateContents() {
    // skip ByteProto
    mainContents--;

    // skip element start
    mainContents -= 3;

    // @ contents index
    mainContents -= 2;

    mainContents = mainIndex(mainContents);
  }

  @Override
  public final void styleRuleEnd() {
    int auxMax = auxIndex;

    int idx = auxStart;

    int internal = mainContents;

    loop: while (idx < auxMax) {
      int marker = aux[idx++];

      switch (marker) {
        case MARK_INTERNAL -> {
          byte proto = main[internal];

          while (true) {
            switch (proto) {
              case Bytes.DECLARATION -> {
                main[internal] = Bytes.MARKED;

                mainAdd(
                  proto,
                  idx0(internal), idx1(internal), idx2(internal)
                );

                internal = mainIndex(internal + 1);

                continue loop;
              }

              default -> {
                throw new UnsupportedOperationException(
                  "Implement me :: proto=" + proto
                );
              }
            }
          }
        }

        case MARK_VALUE3 -> {
          mainAdd(aux[idx++], aux[idx++], aux[idx++]);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    mainAdd(
      Bytes.STYLE_RULE_END,

      idx0(mainContents), idx1(mainContents), idx2(mainContents),
      idx0(mainStart), idx1(mainStart), idx2(mainStart),
      Bytes.STYLE_RULE
    );

    setEndIndex();

    // we clear the aux list
    auxIndex = auxStart;
  }

  @Override
  public final void compilationEnd() {
    int rootIndex = mainIndex;

    while (rootIndex > 0) {
      byte proto = main[--rootIndex];

      switch (proto) {
        case Bytes.STYLE_RULE -> {
          // root @ element start index
          byte elemStart2 = main[--rootIndex];
          byte elemStart1 = main[--rootIndex];
          byte elemStart0 = main[--rootIndex];

          // root @ element contents index
          rootIndex -= 3;

          // new root @ this elements' contenst index
          rootIndex = mainIndex(rootIndex);

          // store indices so they can be retrieved by iterating over aux list from end to start
          auxAdd(elemStart2, elemStart1, elemStart0, proto);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int rootStart = mainIndex;

    mainAdd(Bytes.ROOT);

    while (auxIndex > 0) {
      mainAdd(aux[--auxIndex]);
    }

    mainAdd(
      Bytes.ROOT_END,
      idx0(rootStart), idx1(rootStart), idx2(rootStart)
    );
  }

  private void auxAdd(byte b0) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  private void auxAdd(byte b0, byte b1, byte b2, byte b3) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 3);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
  }

  // we use 3 bytes for internal indices
  private byte idx0(int value) {
    Check.argument(value <= MAX_INDEX, "CssTemplate is too large.");

    return (byte) value;
  }

  // we use 3 bytes for internal indices
  private byte idx1(int value) {
    return (byte) (value >>> 8);
  }

  // we use 3 bytes for internal indices
  private byte idx2(int value) {
    return (byte) (value >>> 16);
  }

  private void mainAdd(byte b0) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1, byte b2) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 2);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 5);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 7);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
    main[mainIndex++] = b6;
    main[mainIndex++] = b7;
  }

  // we use 2 bytes for the StandardName enum
  private byte name0(StandardName name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) ordinal;
  }

  // we use 2 bytes for the StandardName enum
  private byte name1(StandardName name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) (ordinal >>> 8);
  }

}