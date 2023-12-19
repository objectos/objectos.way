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
import java.util.NoSuchElementException;
import java.util.Set;
import objectos.html.pseudom.DocumentProcessor;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlNode;
import objectos.lang.object.Check;
import objectos.util.array.IntArrays;
import objectos.util.array.ObjectArrays;

public final class HtmlCompiler02 extends HtmlCompiler01 {

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
  public final InternalCompiledHtml compile() {
    Object[] objects;
    objects = ObjectArrays.empty();

    if (objectArray != null) {
      objects = Arrays.copyOf(objectArray, objectIndex);
    }

    return new InternalCompiledHtml(
        Arrays.copyOfRange(aux, IDX_AUX, auxIndex), objects
    );
  }

  @Override
  public final void process(DocumentProcessor processor) {
    Check.notNull(processor, "processor == null");

    PseudoHtmlDocument document;
    document = bootstrap();

    processor.process(document);
  }

  private static final byte _START = -1,
      _DOCUMENT = -2,
      _DOCUMENT_NODES = -3,
      _ELEMENT = -4,
      _ELEMENT_ATTRS_REQ = -5,
      _ELEMENT_ATTRS_ITER = -6,
      _ELEMENT_NODES_REQ = -7,
      _ELEMENT_NODES_ITER = -8,
      _ATTRIBUTE = -9,
      _ATTRIBUTE_VALUES_REQ = -10,
      _ATTRIBUTE_VALUES_ITER = -11;

  int[] listArray;

  int listIndex;

  // visible for testing
  final PseudoHtmlDocument bootstrap() {
    listArray = new int[10];

    listIndex = -1;

    // push initial ctx
    ctxPush(_START);

    // holds max main index
    mainContents = mainIndex;

    // reset main index
    mainIndex = 0;

    // we reuse objectArray reference to store our pseudo html objects
    if (objectArray == null) {
      objectArray = new Object[IDX_OBJECT_INDEX];
    }

    return new PseudoHtmlDocument(this);
  }

  final boolean documentHasNext() {
    documentHasNextCheck();

    boolean result;
    result = false;

    loop: while (protoMore()) {
      int proto;
      proto = protoPeek();

      switch (proto) {
        case ByteProto.DOCTYPE,
             ByteProto.ELEMENT -> {
          result = true;

          break loop;
        }

        case ByteProto.ROOT -> protoNext();

        case ByteProto.ROOT_END -> {
          protoNext();

          break loop;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    proto2ctx();

    return result;
  }

  private void documentHasNextCheck() {
    int peek;
    peek = ctxPeek();

    switch (peek) {
      case _DOCUMENT_NODES -> ctx2proto();

      case _ELEMENT -> {
        elementPop(_DOCUMENT_NODES);

        ctx2proto();
      }

      default -> ctxThrow(peek, _DOCUMENT_NODES);
    }
  }

  final void documentIterable() {
    ctxCheck(_START);

    ctxSet(0, _DOCUMENT);
  }

  final void documentIterator() {
    ctxCheck(_DOCUMENT);

    ctxPush(mainIndex, _DOCUMENT_NODES);
  }

  final HtmlNode documentNext() {
    ctxCheck(_DOCUMENT_NODES);

    ctx2proto();

    int proto = protoNext();

    return switch (proto) {
      case ByteProto.DOCTYPE -> {
        proto2ctx();

        yield PseudoHtmlDocumentType.INSTANCE;
      }

      case ByteProto.ELEMENT -> executeElement();

      case ByteProto.ROOT_END -> throw new NoSuchElementException();

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  final void elementAttributes() {
    ctxCheck(_ELEMENT);

    ctxSet(0, _ELEMENT_ATTRS_REQ);
  }

  final boolean elementAttributesHasNext() {
    elementAttributesHasNextCheck();

    int index = ctxPeek(1);

    int value = listArray[index];

    if (value == ATTRS_END) {
      int start = ctxPeek(2);

      listIndex = start - 1;

      ctxCheck(_ELEMENT_ATTRS_REQ);

      ctxSet(0, _ELEMENT);

      return false;
    } else {
      return true;
    }
  }

  final void elementAttributesIterator(StandardElementName parent) {
    ctxCheck(_ELEMENT_ATTRS_REQ);

    ctx2proto();

    int startIndex = listIndex + 1;

    // end marker
    ctxPush(ATTRS_END);

    // start index to be used in the while-loop
    ctxPush(startIndex);

    loop: while (protoMore()) {
      int proto = protoNext();

      switch (proto) {
        case ByteProto.ATTRIBUTE -> executeAttribute();

        case ByteProto.AMBIGUOUS -> {
          int location = protoNext();

          int returnTo = mainIndex;

          // skip ByteProto.ATTR_OR_ELEM
          // skip tail index
          mainIndex = location + 2;

          int code = protoNext();

          var attributeOrElement = Ambiguous.get(code);

          if (attributeOrElement.isAttributeOf(parent)) {
            code = attributeOrElement.attributeByteCode();

            int value = protoNext();

            attributeImpl(code, value);
          }

          mainIndex = returnTo;
        }

        case ByteProto.ELEMENT,
             ByteProto.TEXT,
             ByteProto.RAW -> protoNext();

        case ByteProto.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    ctxPush(
        startIndex, // cursor for iteration

        _ELEMENT_ATTRS_ITER
    );
  }

  final HtmlAttribute elementAttributesNext() {
    ctxCheck(_ELEMENT_ATTRS_ITER);

    int index = ctxPeek(1);

    int code = listArray[index++];

    if (code == ATTRS_END) {
      throw new NoSuchElementException();
    }

    var attribute = htmlAttribute();

    var name = AttributeName.getByCode(code);

    attribute.name = name;

    var type = listArray[index++];

    var value = listArray[index++];

    ctxSet(1, index);

    ctxPush(index, value, type, _ATTRIBUTE);

    return attribute;
  }

  final void elementNodes() {
    ctxCheck(_ELEMENT);

    ctxSet(0, _ELEMENT_NODES_REQ);
  }

  final boolean elementNodesHasNext() {
    elementNodesHasNextCheck();

    ctx2proto();

    var hasNext = false;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.ATTRIBUTE -> mainIndex += 2;

        case ByteProto.AMBIGUOUS -> {
          int elemCode = ctxPeek(2);

          var parent = StandardElementName.getByCode(elemCode);

          int returnToTrue = mainIndex;

          protoNext(); // ByteProto.ATTR_OR_ELEM

          int location = protoNext();

          int returnToFalse = mainIndex;

          mainIndex = location + 2;

          int code = protoNext();

          var attributeOrElement = Ambiguous.get(code);

          if (!attributeOrElement.isAttributeOf(parent)) {
            hasNext = true;

            mainIndex = returnToTrue;

            break loop;
          } else {
            mainIndex = returnToFalse;
          }
        }

        case ByteProto.ELEMENT,
             ByteProto.TEXT,
             ByteProto.RAW -> {
          hasNext = true;

          break loop;
        }

        case ByteProto.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    proto2ctx();

    if (!hasNext) {
      ctxPop(3);

      ctxCheck(_ELEMENT_NODES_REQ);

      ctxSet(0, _ELEMENT);

      var htmlElement = htmlElement();

      int code = ctxPeek(3);

      htmlElement.name = StandardElementName.getByCode(code);
    }

    return hasNext;
  }

  final void elementNodesIterator() {
    ctxCheck(_ELEMENT_NODES_REQ);

    int index = ctxPeek(1);
    int elemCode = ctxPeek(3);

    ctxPush(elemCode, index, _ELEMENT_NODES_ITER);
  }

  final HtmlNode elementNodesNext() {
    ctxCheck(_ELEMENT_NODES_ITER);

    ctx2proto();

    int proto = protoNext();

    HtmlNode node = switch (proto) {
      case ByteProto.AMBIGUOUS -> executeMaybeElement();

      case ByteProto.ELEMENT -> executeElement();

      case ByteProto.ELEMENT_END -> null;

      case ByteProto.TEXT -> executeText();

      case ByteProto.RAW -> executeRaw();

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };

    if (node == null) {
      throw new NoSuchElementException();
    }

    return node;
  }

  private void ctx2proto() {
    mainIndex = ctxPeek(1);
  }

  private void ctxCheck(int expected) {
    int state = ctxPeek();

    ctxThrow(state, expected);
  }

  private int ctxGet(int index) {
    return listArray[index];
  }

  private int ctxPeek() {
    return listArray[listIndex];
  }

  private int ctxPeek(int offset) {
    return listArray[listIndex - offset];
  }

  private void ctxPop(int count) {
    listIndex -= count;
  }

  private void ctxPush(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 1);
    listArray[++listIndex] = v0;
  }

  private void ctxPush(int v0, int v1) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 2);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
  }

  private void ctxPush(int v0, int v1, int v2) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 3);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
    listArray[++listIndex] = v2;
  }

  private void ctxPush(int v0, int v1, int v2, int v3) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 4);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
    listArray[++listIndex] = v2;
    listArray[++listIndex] = v3;
  }

  private void ctxPush(int v0, int v1, int v2, int v3, int v4) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 5);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
    listArray[++listIndex] = v2;
    listArray[++listIndex] = v3;
    listArray[++listIndex] = v4;
  }

  private void ctxSet(int offset, int value) {
    listArray[listIndex - offset] = value;
  }

  private void ctxThrow(int actual, int expected) {
    if (actual != expected) {
      throw new IllegalStateException(
          """
      Found state '%d' but expected state '%d'
      """.formatted(actual, expected)
      );
    }
  }

  private void proto2ctx() {
    ctxSet(1, mainIndex);
  }

  private boolean protoMore() {
    return mainIndex < mainContents;
  }

  private int protoNext() {
    return main[mainIndex++];
  }

  private int protoPeek() {
    return main[mainIndex];
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
        case ByteProto.DOCTYPE -> {
          elemCount = newLineIfNecessary(elemCount);

          auxAdd(ByteCode.DOCTYPE);

          yield 0;
        }

        case ByteProto.ELEMENT -> {
          elemCount = newLineIfNecessary(elemCount);

          int thisLength;
          thisLength = Bytes.decodeInt(main[index++], main[index++]);

          element(index, null);

          yield thisLength;
        }

        case ByteProto.LENGTH2 -> Bytes.decodeInt(main[index++], main[index++]);

        case ByteProto.LENGTH3 -> Bytes.decodeLength3(main[index++], main[index++], main[index++]);

        case ByteProto.MARKED3 -> 3 - 1;

        case ByteProto.MARKED4 -> 4 - 1;

        case ByteProto.MARKED5 -> 5 - 1;

        case ByteProto.TEXT -> {
          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          auxAdd(ByteCode.TEXT, b0, b1);

          // prevent new line after this text
          elemCount = 0;

          // skip ByteProto.INTERNAL4
          yield 1;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto + ";index=" + index
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

  private int decodeIndex(int index) {
    int startIndex;
    startIndex = index;

    byte maybeNeg;

    do {
      maybeNeg = aux[index++];
    } while (maybeNeg < 0);

    auxStart = Bytes.decodeOffset(aux, startIndex, index);

    return index;
  }

  private int decodeLength(int index) {
    int startIndex;
    startIndex = index;

    byte maybeNeg;

    do {
      maybeNeg = main[index++];
    } while (maybeNeg < 0);

    auxStart = Bytes.decodeOffset(main, startIndex, index);

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
      case ByteProto.STANDARD_NAME -> {
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
        case ByteProto.AMBIGUOUS1 -> {
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

        case ByteProto.ATTRIBUTE0 -> {
          index = jmp(index);

          byte attr;
          attr = main[mainContents++];

          handleAttr(attr);
        }

        case ByteProto.ATTRIBUTE1 -> {
          index = jmp(index);

          byte attr;
          attr = main[mainContents++];

          byte v0;
          v0 = main[mainContents++];

          byte v1;
          v1 = main[mainContents++];

          handleAttr(attr, v0, v1);
        }

        case ByteProto.ATTRIBUTE_CLASS -> {
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

        case ByteProto.ATTRIBUTE_ID -> {
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

        case ByteProto.ELEMENT,
             ByteProto.RAW,
             ByteProto.TEXT -> index = skipVarInt(index);

        case ByteProto.END -> {
          if (aux[IDX_ATTR_FIRST] == _FALSE && aux[IDX_ATTR_VALUE] == _TRUE) {
            auxAdd(ByteCode.ATTR_VALUE_END);
          }

          break loop;
        }

        case ByteProto.LENGTH2 -> {
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
        case ByteProto.AMBIGUOUS1 -> {
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

        case ByteProto.ATTRIBUTE1 -> index = skipVarInt(index);

        case ByteProto.ATTRIBUTE_CLASS,
             ByteProto.ATTRIBUTE_ID -> index += 2;

        case ByteProto.ELEMENT -> {
          index = jmp(index);

          // skip fixed length
          mainContents += 2;

          element(mainContents, parent);
        }

        case ByteProto.END -> {
          break loop;
        }

        case ByteProto.LENGTH2 -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = Bytes.decodeInt(len0, len1);

          index += length;
        }

        case ByteProto.STANDARD_NAME -> index += 1;

        case ByteProto.RAW -> {
          index = jmp(index);

          byte b0;
          b0 = main[mainContents++];

          byte b1;
          b1 = main[mainContents++];

          auxAdd(ByteCode.RAW, b0, b1);
        }

        case ByteProto.TEXT -> {
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

    do {
      len0 = main[index++];
    } while (len0 < 0);

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