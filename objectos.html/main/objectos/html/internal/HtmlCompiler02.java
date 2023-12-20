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
import objectos.html.pseudom.DocumentProcessor;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlNode;
import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;
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

  static final byte _DOCUMENT_START = -1;
  static final byte _DOCUMENT_NODES_ITERABLE = -2;
  static final byte _DOCUMENT_NODES_ITERATOR = -3;
  static final byte _DOCUMENT_NODES_HAS_NEXT = -4;
  static final byte _DOCUMENT_NODES_NEXT = -5;
  static final byte _DOCUMENT_NODES_EXHAUSTED = -6;

  static final byte _ELEMENT_START = -7;
  static final byte _ELEMENT_ATTRS_ITERABLE = -8;
  static final byte _ELEMENT_ATTRS_ITERATOR = -9;
  static final byte _ELEMENT_ATTRS_HAS_NEXT = -10;
  static final byte _ELEMENT_ATTRS_NEXT = -11;
  static final byte _ELEMENT_ATTRS_EXHAUSTED = -12;
  static final byte _ELEMENT_NODES_ITERABLE = -13;
  static final byte _ELEMENT_NODES_ITERATOR = -14;
  static final byte _ELEMENT_NODES_HAS_NEXT = -15;
  static final byte _ELEMENT_NODES_NEXT = -16;
  static final byte _ELEMENT_NODES_EXHAUSTED = -17;

  static final byte _ATTRIBUTE_START = -18;
  static final byte _ATTRIBUTE_VALUES_ITERABLE = -19;
  static final byte _ATTRIBUTE_VALUES_ITERATOR = -20;
  static final byte _ATTRIBUTE_VALUES_HAS_NEXT = -21;
  static final byte _ATTRIBUTE_VALUES_NEXT = -22;
  static final byte _ATTRIBUTE_VALUES_EXHAUSTED = -23;

  private static final int OFFSET_ELEMENT = 0;
  private static final int OFFSET_ATTRIBUTE = 1;

  private static final int OFFSET_MAX = OFFSET_ATTRIBUTE;

  // visible for testing
  final PseudoHtmlDocument bootstrap() {
    // we will use the aux list as the context stack
    auxIndex = -1;

    // holds decoded length
    auxStart = 0;

    // holds max main index
    mainContents = mainIndex;

    // reset main index
    mainIndex = 0;

    // we reuse objectArray reference to store our pseudo html objects
    if (objectArray == null) {
      objectArray = new Object[10];
    } else {
      objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex + OFFSET_MAX);
    }

    objectArray[objectIndex + OFFSET_ELEMENT] = new PseudoHtmlElement(this);

    objectArray[objectIndex + OFFSET_ATTRIBUTE] = new PseudoHtmlAttribute(this);

    // push initial state
    auxPush(
        // state
        _DOCUMENT_START,

        // main index
        Bytes.encodeInt0(mainIndex),
        Bytes.encodeInt1(mainIndex),
        Bytes.encodeInt2(mainIndex)
    );

    return new PseudoHtmlDocument(this);
  }

  final void documentIterable() {
    stateCAS(_DOCUMENT_START, _DOCUMENT_NODES_ITERABLE);
  }

  final void documentIterator() {
    stateCAS(_DOCUMENT_NODES_ITERABLE, _DOCUMENT_NODES_ITERATOR);
  }

  final boolean documentHasNext() {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _DOCUMENT_NODES_ITERATOR -> mainIndexLoad();

      case _ELEMENT_START -> {
        int parentIndex;
        parentIndex = parentIndexLoad();

        // (1) ELEMENT_START
        // (3) startIndex
        // (3) parentIndex
        auxIndex -= 7;

        stateCheck(_DOCUMENT_NODES_NEXT);

        mainIndexLoad();

        if (mainIndex != parentIndex) {
          throw new IllegalStateException(
              """
              Last consumed element was not a child of this document or element
              """
          );
        }
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // has next
    byte nextState;
    nextState = _DOCUMENT_NODES_EXHAUSTED;

    loop: while (mainIndex < mainContents) {
      byte proto;
      proto = main[mainIndex];

      switch (proto) {
        case ByteProto.DOCTYPE, ByteProto.ELEMENT, ByteProto.TEXT -> {
          nextState = _DOCUMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.LENGTH2 -> {
          mainIndex++;

          byte b0;
          b0 = main[mainIndex++];

          byte b1;
          b1 = main[mainIndex++];

          mainIndex += Bytes.decodeInt(b0, b1);
        }

        case ByteProto.LENGTH3 -> {
          mainIndex++;

          byte b0;
          b0 = main[mainIndex++];

          byte b1;
          b1 = main[mainIndex++];

          byte b2;
          b2 = main[mainIndex++];

          mainIndex += Bytes.decodeLength3(b0, b1, b2);
        }

        case ByteProto.MARKED3 -> mainIndex += 3;

        case ByteProto.MARKED4 -> mainIndex += 4;

        case ByteProto.MARKED5 -> mainIndex += 5;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    mainIndexStore();

    stateSet(nextState);

    return nextState == _DOCUMENT_NODES_HAS_NEXT;
  }

  final HtmlNode documentNext() {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _DOCUMENT_NODES_HAS_NEXT -> mainIndexLoad();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // next
    byte proto;
    proto = main[mainIndex++];

    return switch (proto) {
      case ByteProto.ELEMENT -> {
        byte b0;
        b0 = main[mainIndex++];

        byte b1;
        b1 = main[mainIndex++];

        int thisLength;
        thisLength = Bytes.decodeInt(b0, b1);

        int elementStart;
        elementStart = mainIndex;

        int parentIndex;
        parentIndex = mainIndex + thisLength;

        mainIndexStore(parentIndex);

        stateSet(_DOCUMENT_NODES_NEXT);

        yield element(elementStart, parentIndex);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  final PseudoHtmlElement element(int startIndex, int parentIndex) {
    // our iteration index
    int elementIndex;
    elementIndex = startIndex;

    StandardElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[elementIndex++];

    switch (proto) {
      case ByteProto.STANDARD_NAME -> {
        byte nameByte;
        nameByte = main[elementIndex++];

        int ordinal;
        ordinal = Bytes.decodeInt(nameByte);

        name = StandardElementName.getByCode(ordinal);
      }

      default -> throw new IllegalArgumentException(
          "Malformed element. Expected name but found=" + proto
      );
    }

    auxPush(
        _ELEMENT_START,

        Bytes.encodeInt0(startIndex),
        Bytes.encodeInt1(startIndex),
        Bytes.encodeInt2(startIndex),

        Bytes.encodeInt0(parentIndex),
        Bytes.encodeInt1(parentIndex),
        Bytes.encodeInt2(parentIndex)
    );

    PseudoHtmlElement element;
    element = htmlElement();

    element.name = name;

    return element;
  }

  final void elementAttributes() {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _ELEMENT_START -> mainIndexLoad();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // push new context
    int index;
    index = mainIndex;

    int parentIndex;
    parentIndex = mainIndex;

    byte hasNext0 = 0, hasNext1 = 0, hasNext2 = 0;

    auxPush(
        _ELEMENT_ATTRS_ITERABLE,

        Bytes.encodeInt0(index),
        Bytes.encodeInt1(index),
        Bytes.encodeInt2(index),

        Bytes.encodeInt0(parentIndex),
        Bytes.encodeInt1(parentIndex),
        Bytes.encodeInt2(parentIndex),

        hasNext0,
        hasNext1,
        hasNext2
    );
  }

  final void elementAttributesIterator() {
    stateCAS(_ELEMENT_ATTRS_ITERABLE, _ELEMENT_ATTRS_ITERATOR);
  }

  final boolean elementAttributesHasNext(StandardElementName parent) {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _ELEMENT_ATTRS_ITERATOR -> mainIndexLoad();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // has next
    byte nextState;
    nextState = _ELEMENT_ATTRS_EXHAUSTED;

    loop: while (mainIndex < mainContents) {
      byte proto;
      proto = main[mainIndex++];

      switch (proto) {
        case ByteProto.STANDARD_NAME -> mainIndex++;

        case ByteProto.AMBIGUOUS1 -> {
          mainIndex = jmp2(mainIndex);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          Ambiguous ambiguous;
          ambiguous = Ambiguous.decode(ordinalByte);

          if (ambiguous.isAttributeOf(parent)) {
            byte attr;
            attr = ambiguous.encodeAttribute();

            byte v0;
            v0 = main[auxStart++];

            byte v1;
            v1 = main[auxStart++];

            attrStore(attr, v0, v1);

            nextState = _ELEMENT_ATTRS_HAS_NEXT;

            break loop;
          }
        }

        case ByteProto.ATTRIBUTE0 -> {
          mainIndex = jmp2(mainIndex);

          byte attr;
          attr = main[auxStart++];

          attrStore(attr, (byte) -1, (byte) -1);

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case ByteProto.ATTRIBUTE1 -> {
          mainIndex = jmp2(mainIndex);

          byte attr;
          attr = main[auxStart++];

          byte v0;
          v0 = main[auxStart++];

          byte v1;
          v1 = main[auxStart++];

          attrStore(attr, v0, v1);

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case ByteProto.ATTRIBUTE_CLASS -> {
          int ordinal;
          ordinal = StandardAttributeName.CLASS.ordinal();

          byte attr;
          attr = Bytes.encodeInt0(ordinal);

          byte v0;
          v0 = main[mainIndex++];

          byte v1;
          v1 = main[mainIndex++];

          attrStore(attr, v0, v1);

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case ByteProto.ATTRIBUTE_ID -> {
          int ordinal;
          ordinal = StandardAttributeName.ID.ordinal();

          byte attr;
          attr = Bytes.encodeInt0(ordinal);

          byte v0;
          v0 = main[mainIndex++];

          byte v1;
          v1 = main[mainIndex++];

          attrStore(attr, v0, v1);

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case ByteProto.ELEMENT,
             ByteProto.RAW,
             ByteProto.TEXT -> mainIndex = skipVarInt(mainIndex);

        case ByteProto.END -> {
          if (aux[IDX_ATTR_FIRST] == _FALSE && aux[IDX_ATTR_VALUE] == _TRUE) {
            auxAdd(ByteCode.ATTR_VALUE_END);
          }

          break loop;
        }

        case ByteProto.LENGTH2 -> {
          byte len0;
          len0 = main[mainIndex++];

          byte len1;
          len1 = main[mainIndex++];

          int length;
          length = Bytes.decodeInt(len0, len1);

          mainIndex += length;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    if (nextState == _ELEMENT_ATTRS_HAS_NEXT) {
      mainIndexStore();

      stateSet(nextState);

      return true;
    }

    // we must pop the _ELEMENT_ATTRS_ITERABLE/ITERATOR context
    // - (1) _ELEMENT_ATTRS_ITERATOR
    // - (3) mainIndex
    // - (3) parentIndex
    // - (3) hasNext
    auxIndex -= 10;

    return false;
  }

  final HtmlAttribute elementAttributesNext() {
    stateCAS(_ELEMENT_ATTRS_HAS_NEXT, _ELEMENT_ATTRS_NEXT);

    // restore main index
    mainIndexLoad();

    // restore hasNext
    final byte attr;
    attr = aux[auxIndex - 7];

    final byte v0;
    v0 = aux[auxIndex - 8];

    final byte v1;
    v1 = aux[auxIndex - 9];

    // our return value
    final PseudoHtmlAttribute attribute;
    attribute = (PseudoHtmlAttribute) objectArray[objectIndex + OFFSET_ATTRIBUTE];

    // attribute name
    final int ordinal;
    ordinal = Bytes.decodeInt(attr);

    final StandardAttributeName name;
    name = StandardAttributeName.getByCode(ordinal);

    attribute.name = name;

    // push new context
    final int index;
    index = mainIndex;

    final int parentIndex;
    parentIndex = mainIndex;

    auxPush(
        _ATTRIBUTE_START,

        Bytes.encodeInt0(index),
        Bytes.encodeInt1(index),
        Bytes.encodeInt2(index),

        Bytes.encodeInt0(parentIndex),
        Bytes.encodeInt1(parentIndex),
        Bytes.encodeInt2(parentIndex),

        attr,
        v0,
        v1
    );

    return attribute;
  }

  final void attributeValues() {
    stateCAS(_ATTRIBUTE_START, _ATTRIBUTE_VALUES_ITERABLE);
  }

  final void attributeValuesIterator() {
    stateCAS(_ATTRIBUTE_VALUES_ITERABLE, _ATTRIBUTE_VALUES_ITERATOR);
  }

  final boolean attributeValuesHasNext() {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _ATTRIBUTE_VALUES_ITERATOR -> mainIndexLoad();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // has next

    return false;
  }

  final void elementNodes() {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _ELEMENT_START -> mainIndexLoad();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // push new context
    int index;
    index = mainIndex;

    int parentIndex;
    parentIndex = mainIndex;

    auxPush(
        _ELEMENT_NODES_ITERABLE,

        Bytes.encodeInt0(index),
        Bytes.encodeInt1(index),
        Bytes.encodeInt2(index),

        Bytes.encodeInt0(parentIndex),
        Bytes.encodeInt1(parentIndex),
        Bytes.encodeInt2(parentIndex)
    );
  }

  final void elementNodesIterator() {
    stateCAS(_ELEMENT_NODES_ITERABLE, _ELEMENT_NODES_ITERATOR);
  }

  final boolean elementNodesHasNext(StandardElementName parent) {
    // state check
    byte state;
    state = aux[auxIndex];

    switch (state) {
      case _ELEMENT_NODES_ITERATOR -> mainIndexLoad();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    }

    // has next
    byte nextState;
    nextState = _ELEMENT_NODES_EXHAUSTED;

    loop: while (mainIndex < mainContents) {
      byte proto;
      proto = main[mainIndex];

      switch (proto) {
        case ByteProto.AMBIGUOUS1 -> {
          int index;
          index = mainIndex + 1;

          index = jmp2(index);

          // load ambiguous name

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          Ambiguous ambiguous;
          ambiguous = Ambiguous.get(ordinal);

          if (ambiguous.isAttributeOf(parent)) {
            mainIndex = index;

            continue loop;
          }

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.ATTRIBUTE1 -> {
          mainIndex++;

          mainIndex = skipVarInt(mainIndex);
        }

        case ByteProto.ATTRIBUTE_CLASS,
             ByteProto.ATTRIBUTE_ID -> mainIndex += 2 + 1;

        case ByteProto.ELEMENT -> {
          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.END -> {
          break loop;
        }

        case ByteProto.LENGTH2 -> {
          mainIndex++;

          byte len0;
          len0 = main[mainIndex++];

          byte len1;
          len1 = main[mainIndex++];

          int length;
          length = Bytes.decodeInt(len0, len1);

          mainIndex += length;
        }

        case ByteProto.STANDARD_NAME -> mainIndex += 1 + 1;

        case ByteProto.RAW -> {
          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.TEXT -> {
          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    if (nextState == _ELEMENT_NODES_HAS_NEXT) {
      mainIndexStore();

      stateSet(nextState);

      return true;
    }

    // we should pop ELEMENT_NODES_ITERATOR
    // - (1) ELEMENT_NODES_ITERATOR
    // - (3) mainIndex
    // - (3) parentIndex
    auxIndex -= 7;

    return false;
  }

  private void attrStore(byte attr, byte v0, byte v1) {
    aux[auxIndex - 7] = attr;

    aux[auxIndex - 8] = v0;

    aux[auxIndex - 9] = v1;
  }

  private PseudoHtmlElement htmlElement() {
    return (PseudoHtmlElement) objectArray[objectIndex + OFFSET_ELEMENT];
  }

  private void auxPush(byte v0, byte v1, byte v2, byte v3) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 4);

    aux[++auxIndex] = v3;
    aux[++auxIndex] = v2;
    aux[++auxIndex] = v1;
    aux[++auxIndex] = v0;
  }

  private void auxPush(byte v0, byte v1, byte v2, byte v3, byte v4, byte v5, byte v6) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 7);

    aux[++auxIndex] = v6;
    aux[++auxIndex] = v5;
    aux[++auxIndex] = v4;
    aux[++auxIndex] = v3;
    aux[++auxIndex] = v2;
    aux[++auxIndex] = v1;
    aux[++auxIndex] = v0;
  }

  private void auxPush(byte v0, byte v1, byte v2, byte v3, byte v4, byte v5, byte v6, byte v7, byte v8, byte v9) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 10);

    aux[++auxIndex] = v9;
    aux[++auxIndex] = v8;
    aux[++auxIndex] = v7;
    aux[++auxIndex] = v6;
    aux[++auxIndex] = v5;
    aux[++auxIndex] = v4;
    aux[++auxIndex] = v3;
    aux[++auxIndex] = v2;
    aux[++auxIndex] = v1;
    aux[++auxIndex] = v0;
  }

  private void mainIndexLoad() {
    byte b0;
    b0 = aux[auxIndex - 1];

    byte b1;
    b1 = aux[auxIndex - 2];

    byte b2;
    b2 = aux[auxIndex - 3];

    mainIndex = Bytes.decodeLength3(b0, b1, b2);
  }

  private void mainIndexStore() {
    mainIndexStore(mainIndex);
  }

  private void mainIndexStore(int value) {
    aux[auxIndex - 1] = Bytes.encodeInt0(value);

    aux[auxIndex - 2] = Bytes.encodeInt1(value);

    aux[auxIndex - 3] = Bytes.encodeInt2(value);
  }

  private int parentIndexLoad() {
    byte b0;
    b0 = aux[auxIndex - 4];

    byte b1;
    b1 = aux[auxIndex - 5];

    byte b2;
    b2 = aux[auxIndex - 6];

    return Bytes.decodeLength3(b0, b1, b2);
  }

  private void stateCheck(byte expected) {
    byte actual;
    actual = aux[auxIndex];

    if (actual != expected) {
      throw new IllegalStateException(
          """
          Found state '%d' but expected state '%d'
          """.formatted(actual, expected)
      );
    }
  }

  private void stateCAS(byte expected, byte next) {
    stateCheck(expected);

    aux[auxIndex] = next;
  }

  private void stateSet(byte value) {
    aux[auxIndex] = value;
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

  private int jmp2(int index) {
    int baseIndex;
    baseIndex = index;

    index = decodeLength(index);

    auxStart = baseIndex - auxStart;

    // skip ByteProto
    auxStart++;

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