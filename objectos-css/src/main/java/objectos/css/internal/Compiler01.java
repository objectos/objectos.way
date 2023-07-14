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

import objectos.css.om.PropertyName;
import objectos.css.om.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;
import objectos.util.ByteArrays;

class Compiler01 extends CssTemplateApi {

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
        ByteProto.DECLARATION,
        // indices take 3 bytes
        ByteProto.NULL, ByteProto.NULL, ByteProto.NULL,
        Bytes.name0(name),
        Bytes.name1(name)
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
        ByteProto.STANDARD_NAME,
        Bytes.name0(name),
        Bytes.name1(name)
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
  }

  @Override
  public final void unitValue(int value, StandardName unit) {

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
      ByteProto.DECLARATION_END,

      Bytes.idx0(mainContents),
      Bytes.idx1(mainContents),
      Bytes.idx2(mainContents),

      Bytes.idx0(mainStart),
      Bytes.idx1(mainStart),
      Bytes.idx2(mainStart),

      ByteProto.DECLARATION
    );

    setEndIndex();

    // we clear the aux list
    auxIndex = auxStart;
  }

  private void setEndIndex() {
    int endIndex;
    endIndex = mainIndex;

    // we skip the first byte proto
    main[mainStart + 1] = Bytes.idx0(endIndex);
    main[mainStart + 2] = Bytes.idx1(endIndex);
    main[mainStart + 3] = Bytes.idx2(endIndex);
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
      ByteProto.STYLE_RULE,
      // indices take 3 bytes
      ByteProto.NULL, ByteProto.NULL, ByteProto.NULL
    );
  }

  @Override
  public final void styleRuleElement(StyleRuleElement element) {
    if (element instanceof StandardName name) {
      // element is a selector name
      // store the enum ordinal
      auxAdd(
        MARK_VALUE3,
        ByteProto.STANDARD_NAME,
        Bytes.name0(name),
        Bytes.name1(name)
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

  final int mainIndex(int offset) {
    int idx0 = Bytes.toInt(main[offset + 0], 0);
    int idx1 = Bytes.toInt(main[offset + 1], 8);
    int idx2 = Bytes.toInt(main[offset + 2], 16);

    return idx2 | idx1 | idx0;
  }

  private void updateContents() {
    // skip ByteProto
    mainContents--;

    // skip element start
    mainContents -= 3;

    // @ contents index
    mainContents -= 3;

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
              case ByteProto.DECLARATION -> {
                main[internal] = ByteProto.MARKED;

                mainAdd(
                  proto,
                  Bytes.idx0(internal),
                  Bytes.idx1(internal),
                  Bytes.idx2(internal)
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
      ByteProto.STYLE_RULE_END,

      Bytes.idx0(mainContents),
      Bytes.idx1(mainContents),
      Bytes.idx2(mainContents),

      Bytes.idx0(mainStart),
      Bytes.idx1(mainStart),
      Bytes.idx2(mainStart),

      ByteProto.STYLE_RULE
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
        case ByteProto.STYLE_RULE -> {
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

    mainAdd(ByteProto.ROOT);

    while (auxIndex > 0) {
      mainAdd(aux[--auxIndex]);
    }

    mainAdd(
      ByteProto.ROOT_END,
      Bytes.idx0(rootStart),
      Bytes.idx1(rootStart),
      Bytes.idx2(rootStart)
    );
  }

  final void auxAdd(byte b0) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  final void auxAdd(byte b0, byte b1) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 1);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
  }

  final void auxAdd(byte b0, byte b1, byte b2) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 2);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 3);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
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

}