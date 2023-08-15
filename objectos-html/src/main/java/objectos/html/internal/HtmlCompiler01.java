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

import objectos.html.HtmlTemplate2;
import objectos.html.tmpl.FragmentAction;
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
  public final void ambiguous(Ambiguous name, String value) {
    int ordinal;
    ordinal = name.ordinal();

    int object;
    object = objectAdd(value);

    mainAdd(
      ByteProto2.AMBIGUOUS1,

      // name
      Bytes.encodeInt0(ordinal),

      // value
      Bytes.encodeInt0(object),
      Bytes.encodeInt1(object),

      ByteProto2.INTERNAL5
    );
  }

  @Override
  public final void attribute(StandardAttributeName name) {
    int ordinal;
    ordinal = name.getCode();

    mainAdd(
      ByteProto2.ATTRIBUTE0,

      // name
      Bytes.encodeInt0(ordinal),

      ByteProto2.INTERNAL3
    );
  }

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

    int templateIndex;
    templateIndex = ByteProto2.NULL;

    loop: while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        case ByteProto2.ATTRIBUTE_CLASS,
             ByteProto2.ATTRIBUTE_ID,
             ByteProto2.TEXT -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case ByteProto2.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case ByteProto2.ATTRIBUTE0 -> {
                contents = encodeInternal3(contents, proto);

                continue loop;
              }

              case ByteProto2.AMBIGUOUS1,
                   ByteProto2.ATTRIBUTE1 -> {
                contents = encodeInternal5(contents, proto);

                continue loop;
              }

              case ByteProto2.ELEMENT -> {
                contents = encodeElement(contents, proto);

                continue loop;
              }

              case ByteProto2.FRAGMENT -> {
                contents = encodeFragment(contents);

                continue loop;
              }

              case ByteProto2.MARKED -> contents = encodeMarked(contents);

              case ByteProto2.MARKED3 -> contents += 3;

              case ByteProto2.MARKED4 -> contents += 4;

              case ByteProto2.MARKED5 -> contents += 5;

              case ByteProto2.TEXT -> {
                contents = encodeText(contents);

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

        case ByteProto2.TEMPLATE -> {
          if (templateIndex == ByteProto2.NULL) {
            // initialize template index
            templateIndex = mainStart;

            // skip ByteProto.ELEMENT
            templateIndex += 1;

            // skip length to the end
            templateIndex += 2;

            // skip NAME + name index
            templateIndex += 2;
          }

          while (true) {
            byte proto;
            proto = main[templateIndex];

            switch (proto) {
              case ByteProto2.TEMPLATE_DATA -> {
                templateIndex = encodeFragment(templateIndex);

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
    if (value == InternalInstruction.INSTANCE || value == InternalFragment.INSTANCE) {
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

        case ByteProto2.INTERNAL3 -> mainContents -= 3 - 2;

        case ByteProto2.INTERNAL4 -> mainContents -= 4 - 2;

        case ByteProto2.INTERNAL5 -> mainContents -= 5 - 2;

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }

      auxAdd(ByteProto2.INTERNAL);
    }

    else if (value instanceof Instruction.ExternalAttribute.Id ext) {
      int index;
      index = externalValue(ext.value());

      auxAdd(
        ByteProto2.ATTRIBUTE_ID,

        Bytes.encodeInt0(index),
        Bytes.encodeInt1(index)
      );
    }

    else if (value instanceof Instruction.ExternalAttribute.StyleClass ext) {
      int index;
      index = externalValue(ext.value());

      auxAdd(
        ByteProto2.ATTRIBUTE_CLASS,

        Bytes.encodeInt0(index),
        Bytes.encodeInt1(index)
      );
    }

    else if (value instanceof HtmlTemplate2 tmpl) {
      // keep start index handy
      int startIndex;
      startIndex = mainIndex;

      mainAdd(
        ByteProto2.TEMPLATE_DATA,

        // length to the end
        ByteProto2.NULL,
        ByteProto2.NULL
      );

      InternalHtmlTemplate2 internal;
      internal = tmpl;

      // keep rollback values in the stack
      int auxStart;
      auxStart = this.auxStart;

      int mainContents;
      mainContents = this.mainContents;

      int mainStart;
      mainStart = this.mainStart;

      try {
        internal.api = this;

        internal.definition();
      } finally {
        internal.api = null;

        // rollback values
        this.auxStart = auxStart;

        this.mainContents = mainContents;

        this.mainStart = mainStart;
      }

      mainAdd(ByteProto2.END);

      // set the end index of the declaration
      int length;
      length = mainIndex - startIndex;

      // skip ByteProto.FOO + len0 + len1
      length -= 3;

      // we skip the first byte proto
      main[startIndex + 1] = Bytes.encodeInt0(length);
      main[startIndex + 2] = Bytes.encodeInt1(length);

      auxAdd(ByteProto2.TEMPLATE);
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
  }

  @Override
  public final void fragment(FragmentAction action) {
    int startIndex;
    startIndex = fragmentBegin();

    action.execute();

    fragmentEnd(startIndex);
  }

  @Override
  public final void text(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
      ByteProto2.TEXT,

      // value
      Bytes.encodeInt0(object),
      Bytes.encodeInt1(object),

      ByteProto2.INTERNAL4
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

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 4);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 6);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 7);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
    aux[auxIndex++] = b7;
  }

  private int encodeElement(int contents, byte proto) {
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

    // point to next element
    int offset;
    offset = Bytes.decodeInt(len0, len1);

    // ensure main can hold least 3 elements
    // 0   - ByteProto
    // 1-2 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 2);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = Bytes.encodeVarInt(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeFragment(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = ByteProto2.MARKED;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    // point to next element
    int offset;
    offset = Bytes.decodeInt(len0, len1);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case ByteProto2.ELEMENT -> index = encodeElement(index, proto);

        case ByteProto2.END -> {
          break loop;
        }

        case ByteProto2.MARKED -> index = encodeMarked(index);

        case ByteProto2.MARKED4 -> index += 4;

        case ByteProto2.MARKED5 -> index += 5;

        default -> {
          throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
          );
        }
      }
    }

    return maxIndex;
  }

  private int encodeInternal3(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = ByteProto2.MARKED3;

    // point to next
    int offset;
    offset = 3;

    // ensure main can hold least 3 elements
    // 0   - ByteProto
    // 1-2 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 2);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = Bytes.encodeVarInt(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal5(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = ByteProto2.MARKED5;

    // point to next
    int offset;
    offset = 5;

    // ensure main can hold least 3 elements
    // 0   - ByteProto
    // 1-2 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 2);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = Bytes.encodeVarInt(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeMarked(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    int length;
    length = Bytes.decodeInt(len0, len1);

    // point to next element
    return contents + length;
  }

  private int encodeText(int contents) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = ByteProto2.MARKED4;

    // point to next
    int offset;
    offset = 4;

    // ensure main can hold least 3 elements
    // 0   - ByteProto
    // 1-2 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 2);

    main[mainIndex++] = ByteProto2.TEXT;

    int length;
    length = mainIndex - startIndex;

    mainIndex = Bytes.encodeVarInt(main, mainIndex, length);

    return contents + offset;
  }

  private int externalValue(String value) {
    String result;
    result = value;

    if (value == null) {
      result = "null";
    }

    return objectAdd(result);
  }

  private int fragmentBegin() {
    // we mark:
    // 1) the start of the contents of the current declaration
    int startIndex;
    startIndex = mainIndex;

    mainAdd(
      ByteProto2.FRAGMENT,

      // length takes 2 bytes
      ByteProto2.NULL,
      ByteProto2.NULL
    );

    return startIndex;
  }

  private void fragmentEnd(int startIndex) {
    // ensure main can hold 4 more elements
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    // mark the end
    main[mainIndex++] = ByteProto2.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - startIndex - 1;

    mainIndex = Bytes.encodeVarIntR(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = ByteProto2.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[startIndex + 1] = Bytes.encodeInt0(length);
    main[startIndex + 2] = Bytes.encodeInt1(length);
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