/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.io.IOException;
import java.util.function.Consumer;
import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;
import objectos.util.array.ObjectArrays;

final class HtmlCompiler extends HtmlCompilerElements implements Html.Compiler {

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

  static final byte _ATTRIBUTE_VALUES_ITERABLE = -18;
  static final byte _ATTRIBUTE_VALUES_ITERATOR = -19;
  static final byte _ATTRIBUTE_VALUES_HAS_NEXT = -20;
  static final byte _ATTRIBUTE_VALUES_NEXT = -21;
  static final byte _ATTRIBUTE_VALUES_EXHAUSTED = -22;

  private static final int OFFSET_ELEMENT = 0;
  private static final int OFFSET_ATTRIBUTE = 1;
  private static final int OFFSET_TEXT = 2;
  private static final int OFFSET_RAW = 3;

  private static final int OFFSET_MAX = OFFSET_RAW;

  /**
   * Sole constructor.
   */
  public HtmlCompiler() {}

  private int id = 0;

  @Override
  public final Html.Id nextId() {
    return Html.id("html-" + ++id);
  }

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    HtmlDocument document;
    document = compile();

    HtmlFormatter.STANDARD.formatTo(document, dest);
  }

  @Override
  public final Html.AttributeInstruction attribute(Html.AttributeName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    return attribute0(name, value);
  }

  @Override
  public final Html.DataOnInstruction dataOn(Html.AttributeName name, Script.Action value) {
    Check.notNull(name, "name == null");

    Script.Action a;
    a = Check.notNull(value, "value == null");

    if (a == Script.noop()) {
      return Html.NOOP;
    } else {
      return attribute0(name, a);
    }
  }

  @Override
  public final Html.ElementInstruction flatten(Html.Instruction... contents) {
    Check.notNull(contents, "contents == null");

    flattenBegin();

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();

    return Html.ELEMENT;
  }

  @Override
  public final Html.ElementInstruction flattenNonNull(Html.Instruction... contents) {
    Check.notNull(contents, "contents == null");

    flattenBegin();

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = contents[i];

      if (inst != null) {
        elementValue(inst);
      }
    }

    elementEnd();

    return Html.ELEMENT;
  }

  @Override
  public final Html.FragmentInstruction include(Html.FragmentLambda fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke();
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1> Html.FragmentInstruction include(Html.FragmentLambda1<T1> fragment, T1 arg1) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.renderMobileNavHeaderItems(arg1);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2> Html.FragmentInstruction include(Html.FragmentLambda2<T1, T2> fragment, T1 arg1, T2 arg2) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2, T3> Html.FragmentInstruction include(Html.FragmentLambda3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2, arg3);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2, T3, T4> Html.FragmentInstruction include(Html.FragmentLambda4<T1, T2, T3, T4> fragment,
      T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2, arg3, arg4);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  /**
   * The no-op instruction.
   *
   * <p>
   * It can be used to conditionally add an attribute or element. For example,
   * the following Objectos HTML template:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region = "noop"}
   *
   * <p>
   * Generates the following when {@code error == false}:
   *
   * <pre>{@code
   *     <div class="alert">This is an alert!</div>
   * }</pre>
   *
   * <p>
   * And generates the following when {@code error == true}:
   *
   * <pre>{@code
   *     <div class="alert alert-error">This is an alert!</div>
   * }</pre>
   *
   * @return the no-op instruction.
   */
  @Override
  public final Html.NoOpInstruction noop() {
    return Html.NOOP;
  }

  @Override
  public final Html.ElementInstruction raw(String text) {
    Check.notNull(text, "text == null");

    rawImpl(text);

    return Html.ELEMENT;
  }

  @Override
  public final Html.ElementInstruction text(String text) {
    Check.notNull(text, "text == null");

    textImpl(text);

    return Html.ELEMENT;
  }

  public final Html.FragmentInstruction render(Consumer<Html.Compiler> fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    fragment.accept(this);

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  //

  @Override
  public final HtmlDocument compile() {
    // we will use the aux list to store contexts
    auxIndex = 0;

    // holds decoded length
    auxStart = 0;

    // holds maximum main index. DO NOT TOUCH!!!
    // mainIndex

    // holds the current context
    mainStart = 0;

    // we reuse objectArray reference to store our pseudo html objects
    if (objectArray == null) {
      objectArray = new Object[10];
    } else {
      objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex + OFFSET_MAX);
    }

    objectArray[objectIndex + OFFSET_ELEMENT] = new HtmlElement(this);

    objectArray[objectIndex + OFFSET_ATTRIBUTE] = new HtmlAttribute(this);

    objectArray[objectIndex + OFFSET_TEXT] = new HtmlText();

    objectArray[objectIndex + OFFSET_RAW] = new HtmlRawText();

    documentCtx();

    return new HtmlDocument(this);
  }

  final void documentIterable() {
    stateCAS(_DOCUMENT_START, _DOCUMENT_NODES_ITERABLE);
  }

  final void documentIterator() {
    stateCAS(_DOCUMENT_NODES_ITERABLE, _DOCUMENT_NODES_ITERATOR);
  }

  final boolean documentHasNext() {
    // our iteration index
    int index;

    // state check
    switch (statePeek()) {
      case _DOCUMENT_NODES_ITERATOR, _DOCUMENT_NODES_NEXT -> {
        // valid state

        // restore main index from the context
        index = documentCtxMainIndexLoad();
      }

      case _ELEMENT_ATTRS_EXHAUSTED, _ELEMENT_NODES_EXHAUSTED -> {
        int parentIndex;
        parentIndex = elementCtxRemove();

        stateCheck(_DOCUMENT_NODES_NEXT);

        // restore main index from the context
        index = documentCtxMainIndexLoad();

        if (index != parentIndex) {
          throw new IllegalStateException(
              """
              Last consumed element was not a child of this document
              """
          );
        }
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    // has next
    byte nextState;
    nextState = _DOCUMENT_NODES_EXHAUSTED;

    loop: while (index < mainIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case HtmlByteProto.DOCTYPE, HtmlByteProto.ELEMENT, HtmlByteProto.TEXT -> {
          // next node found
          nextState = _DOCUMENT_NODES_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.LENGTH2 -> {
          index++;

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          index += HtmlBytes.decodeInt(b0, b1);
        }

        case HtmlByteProto.LENGTH3 -> {
          index++;

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          byte b2;
          b2 = main[index++];

          index += HtmlBytes.decodeLength3(b0, b1, b2);
        }

        case HtmlByteProto.MARKED3 -> index += 3;

        case HtmlByteProto.MARKED4 -> index += 4;

        case HtmlByteProto.MARKED5 -> index += 5;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    stateSet(nextState);

    documentCtxMainIndexStore(index);

    return nextState == _DOCUMENT_NODES_HAS_NEXT;
  }

  final Html.Node documentNext() {
    stateCAS(_DOCUMENT_NODES_HAS_NEXT, _DOCUMENT_NODES_NEXT);

    // restore main index from the context
    int index;
    index = documentCtxMainIndexLoad();

    // next
    byte proto;
    proto = main[index++];

    return switch (proto) {
      case HtmlByteProto.DOCTYPE -> {
        documentCtxMainIndexStore(index);

        yield HtmlDocumentType.INSTANCE;
      }

      case HtmlByteProto.ELEMENT -> {
        byte b0;
        b0 = main[index++];

        byte b1;
        b1 = main[index++];

        int length;
        length = HtmlBytes.decodeInt(b0, b1);

        int elementStartIndex;
        elementStartIndex = index;

        int parentIndex;
        parentIndex = index + length;

        documentCtxMainIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case HtmlByteProto.TEXT -> {
        byte b0;
        b0 = main[index++];

        byte b1;
        b1 = main[index++];

        // skip ByteProto.INTERNAL4
        documentCtxMainIndexStore(index + 1);

        yield htmlText(b0, b1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  private void documentCtx() {
    // set current context
    mainStart = auxIndex;

    // push document context
    auxAdd(
        _DOCUMENT_START,

        // main index @ start iteration from the start = 0
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlBytes.encodeInt2(0)
    );
  }

  private int documentCtxMainIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 1];

    byte b1;
    b1 = aux[mainStart + 2];

    byte b2;
    b2 = aux[mainStart + 3];

    return HtmlBytes.decodeLength3(b0, b1, b2);
  }

  private void documentCtxMainIndexStore(int value) {
    aux[mainStart + 1] = HtmlBytes.encodeInt0(value);

    aux[mainStart + 2] = HtmlBytes.encodeInt1(value);

    aux[mainStart + 3] = HtmlBytes.encodeInt2(value);
  }

  final HtmlElement element(int startIndex, int parentIndex) {
    // our iteration index
    int elementIndex;
    elementIndex = startIndex;

    HtmlElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[elementIndex++];

    switch (proto) {
      case HtmlByteProto.STANDARD_NAME -> {
        byte nameByte;
        nameByte = main[elementIndex++];

        int ordinal;
        ordinal = HtmlBytes.decodeInt(nameByte);

        name = HtmlElementName.get(ordinal);
      }

      default -> throw new IllegalArgumentException(
          "Malformed element. Expected name but found=" + proto
      );
    }

    elementCtx(startIndex, parentIndex);

    HtmlElement element;
    element = htmlElement();

    element.name = name;

    return element;
  }

  final void elementAttributes() {
    // state check
    switch (statePeek()) {
      case _ELEMENT_START -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    stateSet(_ELEMENT_ATTRS_ITERABLE);
  }

  final void elementAttributesIterator() {
    stateCAS(_ELEMENT_ATTRS_ITERABLE, _ELEMENT_ATTRS_ITERATOR);
  }

  final boolean elementAttributesHasNext(HtmlElementName parent) {
    // state check
    switch (statePeek()) {
      case _ELEMENT_ATTRS_ITERATOR,
           _ELEMENT_ATTRS_NEXT,
           _ATTRIBUTE_VALUES_EXHAUSTED -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    // restore index from context
    int index;
    index = elementCtxAttrsIndexLoad();

    // has next
    byte nextState;
    nextState = _ELEMENT_ATTRS_EXHAUSTED;

    loop: while (index < mainIndex) {
      // assume 'worst case'
      // in the happy path we should rollback the index
      int rollbackIndex;
      rollbackIndex = index;

      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.STANDARD_NAME -> index += 1;

        case HtmlByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          HtmlAmbiguous ambiguous;
          ambiguous = HtmlAmbiguous.decode(ordinalByte);

          if (ambiguous.isAttributeOf(parent)) {
            index = rollbackIndex;

            nextState = _ELEMENT_ATTRS_HAS_NEXT;

            break loop;
          }
        }

        case HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ATTRIBUTE_CLASS,
             HtmlByteProto.ATTRIBUTE_ID -> {
          index = rollbackIndex;

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> index = skipVarInt(index);

        case HtmlByteProto.END -> {
          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.LENGTH2 -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = HtmlBytes.decodeInt(len0, len1);

          index += length;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxAttrsIndexStore(index);

    stateSet(nextState);

    return nextState == _ELEMENT_ATTRS_HAS_NEXT;
  }

  final HtmlAttribute elementAttributesNext() {
    stateCAS(_ELEMENT_ATTRS_HAS_NEXT, _ELEMENT_ATTRS_NEXT);

    // restore main index
    int index;
    index = elementCtxAttrsIndexLoad();

    // our return value
    final HtmlAttribute attribute;
    attribute = htmlAttribute();

    // values to set
    byte attr, v0 = -1, v1 = -1;

    byte proto;
    proto = main[index++];

    switch (proto) {
      case HtmlByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        byte ordinalByte;
        ordinalByte = main[auxStart++];

        HtmlAmbiguous ambiguous;
        ambiguous = HtmlAmbiguous.decode(ordinalByte);

        attr = ambiguous.encodeAttribute();

        v0 = main[auxStart++];

        v1 = main[auxStart++];
      }

      case HtmlByteProto.ATTRIBUTE0 -> {
        index = jmp2(index);

        attr = main[auxStart++];
      }

      case HtmlByteProto.ATTRIBUTE1 -> {
        index = jmp2(index);

        attr = main[auxStart++];

        v0 = main[auxStart++];

        v1 = main[auxStart++];
      }

      case HtmlByteProto.ATTRIBUTE_CLASS -> {
        int ordinal;
        ordinal = HtmlAttributeName.CLASS.index();

        attr = HtmlBytes.encodeInt0(ordinal);

        v0 = main[index++];

        v1 = main[index++];
      }

      case HtmlByteProto.ATTRIBUTE_ID -> {
        int ordinal;
        ordinal = HtmlAttributeName.ID.index();

        attr = HtmlBytes.encodeInt0(ordinal);

        v0 = main[index++];

        v1 = main[index++];
      }

      default -> {
        // the previous hasNext should have set the index in the right position
        // if we got to an invalid proto something bad must have happened
        throw new IllegalStateException();
      }
    }

    // attribute name
    int ordinal;
    ordinal = HtmlBytes.decodeInt(attr);

    attribute.name = HtmlAttributeName.get(ordinal);

    // attribute value
    Object value;
    value = null;

    if (v0 != -1 || v1 != -1) {
      value = toObject(v0, v1);
    }

    attribute.value = value;

    // store new state
    elementCtxAttrsIndexStore(index);

    stateSet(_ELEMENT_ATTRS_NEXT);

    return attribute;
  }

  private HtmlAttribute htmlAttribute() {
    return (HtmlAttribute) objectArray[objectIndex + OFFSET_ATTRIBUTE];
  }

  final void attributeValues() {
    stateCAS(_ELEMENT_ATTRS_NEXT, _ATTRIBUTE_VALUES_ITERABLE);
  }

  final void attributeValuesIterator() {
    stateCAS(_ATTRIBUTE_VALUES_ITERABLE, _ATTRIBUTE_VALUES_ITERATOR);
  }

  final boolean attributeValuesHasNext() {
    // state check
    switch (statePeek()) {
      case _ATTRIBUTE_VALUES_ITERATOR,
           _ATTRIBUTE_VALUES_NEXT -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    HtmlAttribute attribute;
    attribute = htmlAttribute();

    if (attribute.value != null) {
      stateSet(_ATTRIBUTE_VALUES_HAS_NEXT);

      return true;
    }

    // restore index from context
    int index;
    index = elementCtxAttrsIndexLoad();

    // current attribute
    HtmlAttributeName attributeName;
    attributeName = attribute.name;

    int attributeCode;
    attributeCode = attributeName.index();

    byte currentAttr;
    currentAttr = HtmlBytes.encodeInt0(attributeCode);

    // next state
    byte nextState;
    nextState = _ATTRIBUTE_VALUES_EXHAUSTED;

    loop: while (index < mainIndex) {
      // assume 'worst case'
      // in the happy path we should rollback the index
      int rollbackIndex;
      rollbackIndex = index;

      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = HtmlBytes.decodeInt(ordinalByte);

          HtmlAmbiguous ambiguous;
          ambiguous = HtmlAmbiguous.get(ordinal);

          // find out the parent
          HtmlElement element;
          element = htmlElement();

          HtmlElementName elementName;
          elementName = element.name;

          if (!ambiguous.isAttributeOf(elementName)) {
            // this is an element
            continue loop;
          }

          // find out if this is the same attribute
          byte attr;
          attr = ambiguous.encodeAttribute();

          if (currentAttr == attr) {
            // this is a new value of the same attribute
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE0 -> {
          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE1 -> {
          index = jmp2(index);

          byte attr;
          attr = main[auxStart++];

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE_CLASS -> {
          int ordinal;
          ordinal = HtmlAttributeName.CLASS.index();

          byte attr;
          attr = HtmlBytes.encodeInt0(ordinal);

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE_ID -> {
          int ordinal;
          ordinal = HtmlAttributeName.ID.index();

          byte attr;
          attr = HtmlBytes.encodeInt0(ordinal);

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> index = skipVarInt(index);

        case HtmlByteProto.END -> {
          index = rollbackIndex;

          break loop;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxAttrsIndexStore(index);

    stateSet(nextState);

    return nextState == _ATTRIBUTE_VALUES_HAS_NEXT;
  }

  final Object attributeValuesNext(Object maybeNext) {
    stateCAS(_ATTRIBUTE_VALUES_HAS_NEXT, _ATTRIBUTE_VALUES_NEXT);

    if (maybeNext != null) {
      return maybeNext;
    }

    // restore index
    int index;
    index = elementCtxAttrsIndexLoad();

    byte proto;
    proto = main[index++];

    return switch (proto) {
      case HtmlByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        elementCtxAttrsIndexStore(index);

        // skip ordinal
        auxStart++;

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        yield toObject(v0, v1);
      }

      case HtmlByteProto.ATTRIBUTE1 -> {
        index = jmp2(index);

        elementCtxAttrsIndexStore(index);

        // skip ordinal
        auxStart++;

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        yield toObject(v0, v1);
      }

      case HtmlByteProto.ATTRIBUTE_CLASS -> {
        byte v0;
        v0 = main[index++];

        byte v1;
        v1 = main[index++];

        elementCtxAttrsIndexStore(index);

        yield toObject(v0, v1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  private Object toObject(byte v0, byte v1) {
    int objectIndex;
    objectIndex = HtmlBytes.decodeInt(v0, v1);

    return objectArray[objectIndex];
  }

  private String toObjectString(byte v0, byte v1) {
    Object o;
    o = toObject(v0, v1);

    return o.toString();
  }

  final void elementNodes() {
    // state check
    switch (statePeek()) {
      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    stateSet(_ELEMENT_NODES_ITERABLE);
  }

  final void elementNodesIterator() {
    stateCAS(_ELEMENT_NODES_ITERABLE, _ELEMENT_NODES_ITERATOR);
  }

  final boolean elementNodesHasNext() {
    // iteration index
    int index;

    // state check
    switch (statePeek()) {
      case _ELEMENT_NODES_ITERATOR, _ELEMENT_NODES_NEXT -> {
        // valid state

        // restore index from context
        index = elementCtxNodesIndexLoad();
      }

      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED, _ELEMENT_NODES_EXHAUSTED -> {
        // remove previous element context
        int parentIndex;
        parentIndex = elementCtxRemove();

        // restore index from context
        index = elementCtxNodesIndexLoad();

        if (index != parentIndex) {
          throw new IllegalStateException(
              """
              Last consumed element was not a child of this element
              """
          );
        }

        // restore name
        HtmlElement element;
        element = htmlElement();

        element.name = elementCtxNameLoad();
      }

      default -> throw new IllegalStateException(
          """
          %d state not allowed @ HtmlElement#nodes#hasNext
          """.formatted(statePeek())
      );
    }

    // has next
    byte nextState;
    nextState = _ELEMENT_NODES_EXHAUSTED;

    loop: while (index < mainIndex) {
      // assume 'worst case'
      // in the happy path we rollback the index
      int rollbackIndex;
      rollbackIndex = index;

      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = HtmlBytes.decodeInt(ordinalByte);

          HtmlAmbiguous ambiguous;
          ambiguous = HtmlAmbiguous.get(ordinal);

          // find out parent element
          HtmlElement element;
          element = htmlElement();

          HtmlElementName parent;
          parent = element.name;

          if (ambiguous.isAttributeOf(parent)) {
            continue loop;
          }

          index = rollbackIndex;

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1 -> index = skipVarInt(index);

        case HtmlByteProto.ATTRIBUTE_CLASS,
             HtmlByteProto.ATTRIBUTE_ID -> index += 2;

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> {
          index = rollbackIndex;

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.END -> {
          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.LENGTH2 -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = HtmlBytes.decodeInt(len0, len1);

          index += length;
        }

        case HtmlByteProto.STANDARD_NAME -> index += 1;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxNodesIndexStore(index);

    stateSet(nextState);

    return nextState == _ELEMENT_NODES_HAS_NEXT;
  }

  final Html.Node elementNodesNext() {
    stateCAS(_ELEMENT_NODES_HAS_NEXT, _ELEMENT_NODES_NEXT);

    // restore index from context
    int index;
    index = elementCtxNodesIndexLoad();

    byte proto;
    proto = main[index++];

    return switch (proto) {
      case HtmlByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        // load ambiguous name

        byte ordinalByte;
        ordinalByte = main[auxStart++];

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        int ordinal;
        ordinal = HtmlBytes.decodeInt(ordinalByte);

        HtmlAmbiguous ambiguous;
        ambiguous = HtmlAmbiguous.get(ordinal);

        Html.ElementName element;
        element = ambiguous.element;

        main = ByteArrays.growIfNecessary(main, mainIndex + 13);

        /*00*/main[mainIndex++] = HtmlByteProto.MARKED4;
        /*01*/main[mainIndex++] = v0;
        /*02*/main[mainIndex++] = v1;
        /*03*/main[mainIndex++] = HtmlByteProto.INTERNAL4;

        /*04*/main[mainIndex++] = HtmlByteProto.LENGTH2;
        /*05*/main[mainIndex++] = HtmlBytes.encodeInt0(7);
        /*06*/main[mainIndex++] = HtmlBytes.encodeInt0(7);
        int elementStartIndex = mainIndex;
        /*07*/main[mainIndex++] = HtmlByteProto.STANDARD_NAME;
        /*08*/main[mainIndex++] = (byte) element.index();
        /*09*/main[mainIndex++] = HtmlByteProto.TEXT;
        /*10*/main[mainIndex++] = HtmlBytes.encodeInt0(10);
        /*11*/main[mainIndex++] = HtmlByteProto.END;
        /*12*/main[mainIndex++] = HtmlBytes.encodeInt0(11);
        /*13*/main[mainIndex++] = HtmlByteProto.INTERNAL;

        int parentIndex;
        parentIndex = index;

        elementCtxNodesIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case HtmlByteProto.ELEMENT -> {
        index = jmp2(index);

        // skip fixed length
        auxStart += 2;

        int elementStartIndex;
        elementStartIndex = auxStart;

        int parentIndex;
        parentIndex = index;

        elementCtxNodesIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case HtmlByteProto.RAW -> {
        index = jmp2(index);

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        elementCtxNodesIndexStore(index);

        // return value
        HtmlRawText raw;
        raw = (HtmlRawText) objectArray[objectIndex + OFFSET_RAW];

        // text value
        raw.value = toObjectString(v0, v1);

        yield raw;
      }

      case HtmlByteProto.TEXT -> {
        index = jmp2(index);

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        elementCtxNodesIndexStore(index);

        yield htmlText(v0, v1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  private HtmlText htmlText(byte v0, byte v1) {
    HtmlText text;
    text = (HtmlText) objectArray[objectIndex + OFFSET_TEXT];

    // text value
    text.value = toObjectString(v0, v1);

    return text;
  }

  private void elementCtx(int startIndex, int parentIndex) {
    // current context length
    int length;
    length = auxIndex - mainStart;

    // set current context
    mainStart = auxIndex;

    // ensure aux length
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 13);

    // 0
    aux[auxIndex++] = _ELEMENT_START;

    // 1-3 attrs iteration index
    aux[auxIndex++] = HtmlBytes.encodeInt0(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(startIndex);

    // 4-6 nodes iteration index
    aux[auxIndex++] = HtmlBytes.encodeInt0(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(startIndex);

    // 7-9 start index
    aux[auxIndex++] = HtmlBytes.encodeInt0(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(startIndex);

    // 10-12 parent index
    aux[auxIndex++] = HtmlBytes.encodeInt0(parentIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(parentIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(parentIndex);

    // 13 parent context length
    aux[auxIndex++] = HtmlBytes.encodeInt0(length);
  }

  private int elementCtxAttrsIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 1];

    byte b1;
    b1 = aux[mainStart + 2];

    byte b2;
    b2 = aux[mainStart + 3];

    return HtmlBytes.decodeLength3(b0, b1, b2);
  }

  private void elementCtxAttrsIndexStore(int value) {
    aux[mainStart + 1] = HtmlBytes.encodeInt0(value);

    aux[mainStart + 2] = HtmlBytes.encodeInt1(value);

    aux[mainStart + 3] = HtmlBytes.encodeInt2(value);
  }

  private HtmlElementName elementCtxNameLoad() {
    // restore start index
    byte b0;
    b0 = aux[mainStart + 7];

    byte b1;
    b1 = aux[mainStart + 8];

    byte b2;
    b2 = aux[mainStart + 9];

    int startIndex;
    startIndex = HtmlBytes.decodeLength3(b0, b1, b2);

    HtmlElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[startIndex++];

    switch (proto) {
      case HtmlByteProto.STANDARD_NAME -> {
        byte nameByte;
        nameByte = main[startIndex++];

        int ordinal;
        ordinal = HtmlBytes.decodeInt(nameByte);

        name = HtmlElementName.get(ordinal);
      }

      default -> throw new IllegalArgumentException(
          "Malformed element. Expected name but found=" + proto
      );
    }

    return name;
  }

  private int elementCtxNodesIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 4];

    byte b1;
    b1 = aux[mainStart + 5];

    byte b2;
    b2 = aux[mainStart + 6];

    return HtmlBytes.decodeLength3(b0, b1, b2);
  }

  private void elementCtxNodesIndexStore(int value) {
    aux[mainStart + 4] = HtmlBytes.encodeInt0(value);

    aux[mainStart + 5] = HtmlBytes.encodeInt1(value);

    aux[mainStart + 6] = HtmlBytes.encodeInt2(value);
  }

  private int elementCtxRemove() {
    // restore parent index
    byte b0;
    b0 = aux[mainStart + 10];

    byte b1;
    b1 = aux[mainStart + 11];

    byte b2;
    b2 = aux[mainStart + 12];

    int parentIndex;
    parentIndex = HtmlBytes.decodeLength3(b0, b1, b2);

    // restore parent length
    byte len;
    len = aux[mainStart + 13];

    int length;
    length = HtmlBytes.decodeInt(len);

    // remove this context
    auxIndex = mainStart;

    // set parent as the current context
    mainStart = auxIndex - length;

    return parentIndex;
  }

  private HtmlElement htmlElement() {
    return (HtmlElement) objectArray[objectIndex + OFFSET_ELEMENT];
  }

  private void stateCheck(byte expected) {
    byte actual;
    actual = statePeek();

    if (actual != expected) {
      throw new IllegalStateException(
          """
          Found state '%d' but expected state '%d'
          """.formatted(actual, expected)
      );
    }
  }

  private void stateCAS(byte expected, byte next) {
    // not a real CAS
    // but it does compare and swap
    stateCheck(expected);

    aux[mainStart] = next;
  }

  private byte statePeek() {
    return aux[mainStart];
  }

  private void stateSet(byte value) {
    aux[mainStart] = value;
  }

  private int decodeLength(int index) {
    int startIndex;
    startIndex = index;

    byte maybeNeg;

    do {
      maybeNeg = main[index++];
    } while (maybeNeg < 0);

    auxStart = HtmlBytes.decodeOffset(main, startIndex, index);

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

  private int skipVarInt(int index) {
    byte len0;

    do {
      len0 = main[index++];
    } while (len0 < 0);

    return index;
  }

}