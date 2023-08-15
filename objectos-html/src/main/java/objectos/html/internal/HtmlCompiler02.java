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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import objectos.html.tmpl.ElementKind;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.ObjectArrays;

final class HtmlCompiler02 extends HtmlCompiler01 {

  private static final Set<StandardElementName> PHRASING = EnumSet.of(
    StandardElementName.A,
    StandardElementName.ABBR,
    StandardElementName.B,
    StandardElementName.BR,
    StandardElementName.BUTTON,
    StandardElementName.CODE,
    StandardElementName.EM,
    StandardElementName.IMG,
    StandardElementName.INPUT,
    StandardElementName.KBD,
    StandardElementName.LABEL,
    StandardElementName.LINK,
    StandardElementName.META,
    StandardElementName.PROGRESS,
    StandardElementName.SAMP,
    StandardElementName.SCRIPT,
    StandardElementName.SELECT,
    StandardElementName.SMALL,
    StandardElementName.SPAN,
    StandardElementName.STRONG,
    StandardElementName.SUB,
    StandardElementName.SUP,
    StandardElementName.SVG,
    StandardElementName.TEMPLATE,
    StandardElementName.TEXTAREA
  );

  private static final int IDX_NEW_LINE = 0;

  private static final int IDX_ATTR_FIRST = 1;

  private static final int IDX_ATTR_PROTO = 2;

  private static final int IDX_ATTR_VALUE = 3;

  private static final int IDX_AUX = 4;

  private static final byte _FALSE = 0;

  private static final byte _TRUE = -1;

  @Override
  public final CompiledMarkup compile() {
    Object[] objects;
    objects = ObjectArrays.empty();

    if (objectArray != null) {
      objects = Arrays.copyOf(objectArray, objectIndex);
    }

    return new CompiledMarkup(
      Arrays.copyOfRange(aux, IDX_AUX, auxIndex), objects
    );
  }

  @Override
  public final void optimize() {
    // holds new line status
    aux[IDX_NEW_LINE] = _TRUE;

    // we will use the aux list to store our byte code
    auxIndex = IDX_AUX;

    // holds decoded length
    auxStart = 0;

    // jmp auxiliary
    mainContents = 0;

    // holds the indentation level
    mainStart = 0;

    // we will iterate over the main list looking for unmarked elements
    int index;
    index = 0;

    int elemCount;
    elemCount = 0;

    while (index < mainIndex) {
      byte proto;
      proto = main[index++];

      int length;
      length = switch (proto) {
        case ByteProto2.DOCTYPE -> {
          elemCount = newLineIfNecessary(elemCount);

          auxAdd(ByteCode.DOCTYPE);

          yield 0;
        }

        case ByteProto2.ELEMENT -> {
          elemCount = newLineIfNecessary(elemCount);

          int thisLength;
          thisLength = Bytes.decodeInt(main[index++], main[index++]);

          element(index, null);

          yield thisLength;
        }

        case ByteProto2.MARKED -> Bytes.decodeInt(main[index++], main[index++]);

        case ByteProto2.MARKED3 -> 3 - 1;

        case ByteProto2.MARKED4 -> 4 - 1;

        case ByteProto2.MARKED5 -> 5 - 1;

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      };

      index += length;
    }

    if (auxIndex > 1) {
      int lastIndex;
      lastIndex = auxIndex - 1;

      byte last;
      last = aux[lastIndex];

      switch (last) {
        case ByteCode.NL -> {}

        case ByteCode.NL_OPTIONAL -> {
          aux[lastIndex] = ByteCode.NL;
        }

        default -> {
          auxAdd(ByteCode.NL);
        }
      }
    }
  }

  private int decodeLength(int index) {
    byte len0;
    len0 = main[index++];

    auxStart = len0;

    if (auxStart < 0) {
      byte len1;
      len1 = main[index++];

      auxStart = Bytes.decodeVarInt(len0, len1);
    }

    return index;
  }

  private void element(int index, StandardElementName parent) {
    // 1) let's write the start tag fully
    // -> i.e. with the attributes (if any)
    StandardElementName name;
    name = element0StartTag(index, parent);

    ElementKind elementKind;
    elementKind = name.getKind();

    if (elementKind.isVoid()) {
      // this element is a void element
      // -> no children
      // -> no end tag

      if (isHead(parent) && !wasNewLine()) {
        auxAdd(ByteCode.NL_OPTIONAL);

        newLine(_TRUE);
      }

      return;
    }

    // we'll iterate over the children (if any)
    element1Children(index, name);

    // finally, write out the end tag
    element2EndTag(parent, name);
  }

  private StandardElementName element0StartTag(int index, StandardElementName parent) {
    // we'll keep the name values handy
    byte nameByte;

    // in particular this one will be required by the end tag (if one should be rendered)
    StandardElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[index++];

    switch (proto) {
      case ByteProto2.STANDARD_NAME -> {
        nameByte = main[index++];

        int ordinal;
        ordinal = Bytes.decodeInt(nameByte);

        name = StandardElementName.getByCode(ordinal);
      }

      default -> throw new IllegalArgumentException(
        "Malformed element. Expected name but found=" + proto
      );
    }

    // 'open' the start tag

    element0StartTag0Open(parent, name);

    auxAdd(ByteCode.START_TAG, nameByte);

    // we'll iterate over the attributes (if any)

    aux[IDX_ATTR_FIRST] = _TRUE;

    aux[IDX_ATTR_PROTO] = 0;

    aux[IDX_ATTR_VALUE] = _FALSE;

    loop: while (index < mainIndex) {
      proto = main[index++];

      switch (proto) {
        case ByteProto2.AMBIGUOUS1 -> {
          index = jmp(index);

          byte ordinalByte;
          ordinalByte = main[mainContents++];

          Ambiguous ambiguous;
          ambiguous = Ambiguous.decode(ordinalByte);

          if (ambiguous.isAttributeOf(name)) {
            byte attr;
            attr = ambiguous.encodeAttribute();

            byte v0;
            v0 = main[mainContents++];

            byte v1;
            v1 = main[mainContents++];

            handleAttr(attr, v0, v1);
          }
        }

        case ByteProto2.ATTRIBUTE0 -> {
          index = jmp(index);

          byte attr;
          attr = main[mainContents++];

          handleAttr(attr);
        }

        case ByteProto2.ATTRIBUTE1 -> {
          index = jmp(index);

          byte attr;
          attr = main[mainContents++];

          byte v0;
          v0 = main[mainContents++];

          byte v1;
          v1 = main[mainContents++];

          handleAttr(attr, v0, v1);
        }

        case ByteProto2.ATTRIBUTE_CLASS -> {
          int ordinal;
          ordinal = StandardAttributeName.CLASS.ordinal();

          byte attr;
          attr = Bytes.encodeInt0(ordinal);

          byte v0;
          v0 = main[index++];

          byte v1;
          v1 = main[index++];

          handleAttr(attr, v0, v1);
        }

        case ByteProto2.ATTRIBUTE_ID -> {
          int ordinal;
          ordinal = StandardAttributeName.ID.ordinal();

          byte attr;
          attr = Bytes.encodeInt0(ordinal);

          byte v0;
          v0 = main[index++];

          byte v1;
          v1 = main[index++];

          handleAttr(attr, v0, v1);
        }

        case ByteProto2.ELEMENT,
             ByteProto2.RAW,
             ByteProto2.TEXT -> index = skipVarInt(index);

        case ByteProto2.END -> {
          if (aux[IDX_ATTR_FIRST] == _FALSE && aux[IDX_ATTR_VALUE] == _TRUE) {
            auxAdd(ByteCode.ATTR_VALUE_END);
          }

          break loop;
        }

        case ByteProto2.MARKED -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = Bytes.decodeInt(len0, len1);

          index += length;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    // let's close the start tag

    auxAdd(ByteCode.GT);

    return name;
  }

  private void element0StartTag0Open(StandardElementName parent, StandardElementName name) {
    if (isHead(parent) || !PHRASING.contains(name)) {
      // 1) head children
      // 2) non-phrasing elements
      //    => should be written in their own lines

      if (!wasNewLine()) {
        // write NL only if one was not written before
        auxAdd(ByteCode.NL_OPTIONAL);
      }

      indentationWrite();
    }
  }

  private void element1Children(int index, StandardElementName parent) {
    // we increase the indentation level before writing out the children
    indentationInc();

    // for the first child (if any)
    newLine(_FALSE);

    loop: while (index < mainIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto2.AMBIGUOUS1 -> {
          index = jmp(index);

          // load ambiguous name

          byte ordinalByte;
          ordinalByte = main[mainContents++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          Ambiguous ambiguous;
          ambiguous = Ambiguous.get(ordinal);

          if (ambiguous.isAttributeOf(parent)) {
            // ambiguous was treated as an attribute, continue
            continue loop;
          }

          StandardElementName element;
          element = ambiguous.element;

          // 'open' the start tag

          element0StartTag0Open(parent, element);

          int nameOrdinal;
          nameOrdinal = element.ordinal();

          byte nameByte;
          nameByte = Bytes.encodeInt0(nameOrdinal);

          auxAdd(ByteCode.START_TAG, nameByte, ByteCode.GT);

          auxAdd(ByteCode.TEXT, main[mainContents++], main[mainContents++]);

          auxAdd(ByteCode.END_TAG, nameByte);

          element2EndTag0NewLine(parent, element);
        }

        case ByteProto2.ATTRIBUTE1 -> index = skipVarInt(index);

        case ByteProto2.ATTRIBUTE_CLASS,
             ByteProto2.ATTRIBUTE_ID -> index += 2;

        case ByteProto2.ELEMENT -> {
          index = jmp(index);

          // skip fixed length
          mainContents += 2;

          element(mainContents, parent);
        }

        case ByteProto2.END -> {
          break loop;
        }

        case ByteProto2.MARKED -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = Bytes.decodeInt(len0, len1);

          index += length;
        }

        case ByteProto2.STANDARD_NAME -> index += 1;

        case ByteProto2.RAW -> {
          index = jmp(index);

          byte b0;
          b0 = main[mainContents++];

          byte b1;
          b1 = main[mainContents++];

          auxAdd(ByteCode.RAW, b0, b1);
        }

        case ByteProto2.TEXT -> {
          index = jmp(index);

          byte b0;
          b0 = main[mainContents++];

          byte b1;
          b1 = main[mainContents++];

          switch (parent) {
            case SCRIPT -> {
              int valueIndex;
              valueIndex = Bytes.decodeInt(b0, b1);

              String value;
              value = (String) objectArray[valueIndex];

              if (!startsWithNewLine(value)) {
                auxAdd(ByteCode.NL_OPTIONAL);
              }

              indentationWriteBlock();

              auxAdd(ByteCode.TEXT_SCRIPT, b0, b1);

              if (!endsWithNewLine(value)) {
                auxAdd(ByteCode.NL_OPTIONAL);

                newLine(_TRUE);
              }
            }

            case STYLE -> {
              int valueIndex;
              valueIndex = Bytes.decodeInt(b0, b1);

              String value;
              value = (String) objectArray[valueIndex];

              if (!startsWithNewLine(value)) {
                auxAdd(ByteCode.NL_OPTIONAL);
              }

              indentationWriteBlock();

              auxAdd(ByteCode.TEXT_STYLE, b0, b1);

              if (!endsWithNewLine(value)) {
                auxAdd(ByteCode.NL_OPTIONAL);

                newLine(_TRUE);
              }
            }

            default -> {
              auxAdd(ByteCode.TEXT, b0, b1);
            }
          }
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    // we've written all of the children, decrease indentation
    indentationDec();
  }

  private void element2EndTag(StandardElementName parent, StandardElementName name) {
    int nameOrdinal;
    nameOrdinal = name.ordinal();

    byte nameByte;
    nameByte = Bytes.encodeInt0(nameOrdinal);

    if (wasNewLine()) {
      indentationWrite();
    }

    auxAdd(ByteCode.END_TAG, nameByte);

    element2EndTag0NewLine(parent, name);
  }

  private void element2EndTag0NewLine(StandardElementName parent, StandardElementName name) {
    byte newLine;
    newLine = _FALSE;

    if (isHead(parent) || !PHRASING.contains(name)) {
      auxAdd(ByteCode.NL_OPTIONAL);

      newLine = _TRUE;
    }

    newLine(newLine);
  }

  private boolean endsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length > 0) {
      char last;
      last = value.charAt(length - 1);

      return isNewLine(last);
    } else {
      return false;
    }
  }

  private void handleAttr(byte attr) {
    if (aux[IDX_ATTR_FIRST] == _TRUE || aux[IDX_ATTR_VALUE] == _FALSE) {
      // this is the first attribute
      auxAdd(
        ByteCode.SPACE,
        ByteCode.ATTR_NAME, attr
      );
    }

    else {
      // this is a new attribute
      auxAdd(
        ByteCode.ATTR_VALUE_END,
        ByteCode.SPACE,
        ByteCode.ATTR_NAME, attr
      );
    }

    aux[IDX_ATTR_FIRST] = _FALSE;

    aux[IDX_ATTR_PROTO] = attr;

    aux[IDX_ATTR_VALUE] = _FALSE;
  }

  private void handleAttr(byte attr, byte value0, byte value1) {
    if (aux[IDX_ATTR_FIRST] == _TRUE) {
      // this is the first attribute
      auxAdd(
        ByteCode.SPACE,
        ByteCode.ATTR_NAME, attr,
        ByteCode.ATTR_VALUE_START,
        ByteCode.ATTR_VALUE, value0, value1
      );
    }

    else if (aux[IDX_ATTR_PROTO] != attr) {
      // this is a new attribute
      auxAdd(
        ByteCode.ATTR_VALUE_END,
        ByteCode.SPACE,
        ByteCode.ATTR_NAME, attr,
        ByteCode.ATTR_VALUE_START,
        ByteCode.ATTR_VALUE, value0, value1
      );
    }

    else {
      // this is a new value of the same attribute
      auxAdd(
        ByteCode.SPACE,
        ByteCode.ATTR_VALUE, value0, value1
      );
    }

    aux[IDX_ATTR_FIRST] = _FALSE;

    aux[IDX_ATTR_PROTO] = attr;

    aux[IDX_ATTR_VALUE] = _TRUE;
  }

  private void indentationDec() {
    mainStart--;
  }

  private void indentationInc() {
    mainStart++;
  }

  private void indentationWrite() {
    if (mainStart == 0) {
      return;
    }

    auxAdd(ByteCode.TAB, (byte) mainStart);
  }

  private void indentationWriteBlock() {
    if (mainStart == 0) {
      return;
    }

    auxAdd(ByteCode.TAB_BLOCK, (byte) mainStart);
  }

  private boolean isHead(StandardElementName parent) {
    // test is null safe
    return parent == StandardElementName.HEAD;
  }

  private boolean isNewLine(char c) {
    return c == '\n' || c == '\r';
  }

  private int jmp(int index) {
    int baseIndex;
    baseIndex = index;

    index = decodeLength(index);

    mainContents = baseIndex - auxStart;

    // skip ByteProto
    mainContents++;

    return index;
  }

  private void newLine(byte value) {
    aux[0] = value;
  }

  private int newLineIfNecessary(int count) {
    if (count != 0) {
      auxAdd(ByteCode.NL_OPTIONAL);
    }

    return count + 1;
  }

  private int skipVarInt(int index) {
    byte len0;
    len0 = main[index++];

    if (len0 < 0) {
      index++;
    }

    return index;
  }

  private boolean startsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length > 0) {
      char first;
      first = value.charAt(0);

      return isNewLine(first);
    } else {
      return false;
    }
  }

  private boolean wasNewLine() {
    return aux[0] == _TRUE;
  }

}