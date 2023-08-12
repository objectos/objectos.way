/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.ByteArrays;
import objectos.util.ObjectArrays;

class HtmlCompiler01 extends HtmlTemplateApi2 {

  byte[] aux;

  int auxIndex;

  int auxStart;

  byte[] main;

  int mainContents;

  int mainIndex;

  int mainStart;

  Object[] objectArray;

  int objectIndex;

  @Override
  public final void attribute(StandardAttributeName name, String value) {
    int ordinal;
    ordinal = name.getCode();

    int object;
    object = objectAdd(value);

    mainAdd(
      ByteProto2.ATTRIBUTE1,

      // name
      Bytes.encodeInt0(ordinal),

      // value
      Bytes.encodeInt0(object),
      Bytes.encodeInt1(object),

      ByteProto2.INTERNAL5
    );
  }

  @Override
  public final void compilationBegin() {
    aux = new byte[128];

    auxIndex = 0;

    main = new byte[256];

    mainIndex = 0;
  }

  @Override
  public final void compilationEnd() {
    // noop
  }

  @Override
  public final void doctype() {
    mainAdd(ByteProto2.DOCTYPE);
  }

  @Override
  public final void elementBegin(StandardElementName name) {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;

    mainAdd(
      ByteProto2.ELEMENT,

      // length takes 2 bytes
      ByteProto2.NULL,
      ByteProto2.NULL,

      ByteProto2.STANDARD_NAME,

      Bytes.encodeName(name)
    );
  }

  @Override
  public final void elementEnd() {
    // we iterate over each value added via elementValue(Instruction)
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        case ByteProto2.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case ByteProto2.ATTRIBUTE1 -> {
                // keep the start index handy
                int startIndex;
                startIndex = contents;

                // mark this element
                main[contents] = ByteProto2.MARKED5;

                // point to next
                contents += 5;

                // ensure main can hold least 3 elements
                // 0   - ByteProto
                // 1-2 - variable length
                main = ByteArrays.growIfNecessary(main, mainIndex + 2);

                main[mainIndex++] = proto;

                int length;
                length = mainIndex - startIndex;

                mainIndex = Bytes.encodeVarInt(main, mainIndex, length);

                continue loop;
              }

              case ByteProto2.ELEMENT -> {
                // keep the start index handy
                int startIndex;
                startIndex = contents;

                // mark this element
                main[contents++] = ByteProto2.MARKED;

                // decode the length
                byte len0;
                len0 = main[contents++];

                byte len1;
                len1 = main[contents++];

                int length;
                length = Bytes.decodeInt(len0, len1);

                // point to next element
                contents += length;

                // ensure main can hold least 3 elements
                // 0   - ByteProto
                // 1-2 - variable length
                main = ByteArrays.growIfNecessary(main, mainIndex + 2);

                main[mainIndex++] = proto;

                length = mainIndex - startIndex;

                mainIndex = Bytes.encodeVarInt(main, mainIndex, length);

                continue loop;
              }

              case ByteProto2.MARKED -> {
                contents++;

                // decode the length
                byte len0;
                len0 = main[contents++];

                byte len1;
                len1 = main[contents++];

                int length;
                length = Bytes.decodeInt(len0, len1);

                // point to next element
                contents += length;
              }

              case ByteProto2.MARKED5 -> contents += 5;

              default -> {
                throw new UnsupportedOperationException(
                  "Implement me :: proto=" + proto
                );
              }
            }
          }
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: mark=" + mark
        );
      }
    }

    // ensure main can hold 4 more elements
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    // mark the end
    main[mainIndex++] = ByteProto2.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - mainContents - 1;

    mainIndex = Bytes.encodeVarIntR(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = ByteProto2.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - mainStart;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[mainStart + 1] = Bytes.encodeInt0(length);
    main[mainStart + 2] = Bytes.encodeInt1(length);

    // we clear the aux list
    auxIndex = auxStart;
  }

  @Override
  public final void elementValue(Instruction value) {
    if (value == InternalInstruction.INSTANCE) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case ByteProto2.INTERNAL -> {
          byte len0;
          len0 = main[mainContents--];

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = main[mainContents--];

            length = Bytes.decodeVarInt(len0, len1);
          }

          mainContents -= length;
        }

        case ByteProto2.INTERNAL5 -> mainContents -= 5 - 2;

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }

      auxAdd(ByteProto2.INTERNAL);
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
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

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 4);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
  }

  private void mainAdd(byte b0) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 4);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
  }

  private int objectAdd(Object value) {
    if (objectArray == null) {
      objectArray = new Object[10];
    }

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    int index;
    index = objectIndex++;

    objectArray[index] = value;

    return index;
  }

}