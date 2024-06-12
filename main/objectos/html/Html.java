/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.html;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlNode;
import objectos.lang.CharWritable;
import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;
import objectos.util.array.ObjectArrays;

public final class Html extends BaseElements implements CharWritable {

  public interface Extensible {

    Api.GlobalAttribute renderAttribute(Function<Html, Api.GlobalAttribute> attribute);

    void renderFragment(Consumer<Html> fragment);

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
  public Html() {}

  public final Api.GlobalAttribute attribute(AttributeName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    return attribute0(name, value);
  }

  public final Api.GlobalAttribute attribute(AttributeName name, Action value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    return attribute0(name, value);
  }

  /**
   * Flattens the specified instructions so that each of the specified
   * instructions is individually added, in order, to a receiving element.
   *
   * <p>
   * This is useful, for example, when creating {@link HtmlComponent} instances.
   * The following Objectos HTML code:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
   * "flatten"}
   *
   * <p>
   * Generates the following HTML:
   *
   * <pre>{@code
   *    <body>
   *    <div class="my-component">
   *    <h1>Flatten example</h1>
   *    <p>First paragraph</p>
   *    <p>Second paragraph</p>
   *    </div>
   *    </body>
   * }</pre>
   *
   * <p>
   * The {@code div} instruction is rendered as if it was invoked with four
   * distinct instructions:
   *
   * <ul>
   * <li>the {@code class} attribute;
   * <li>the {@code h1} element;
   * <li>the first {@code p} element; and
   * <li>the second {@code p} element.
   * </ul>
   *
   * @param contents
   *        the instructions to be flattened
   *
   * @return an instruction representing this flatten operation
   */
  public final Api.Element flatten(Api.Instruction... contents) {
    Check.notNull(contents, "contents == null");

    flattenBegin();

    for (int i = 0; i < contents.length; i++) {
      Api.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();

    return Api.ELEMENT;
  }

  /**
   * Includes a fragment into this template represented by the specified lambda.
   *
   * <p>
   * The included fragment MUST only invoke methods this template instance. It
   * is common (but not required) for a fragment to be a method reference to
   * a private method of the template instance.
   *
   * <p>
   * The following Objectos HTML template:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
   * "IncludeExample"}
   *
   * <p>
   * Generates the following HTML:
   *
   * <pre>{@code
   *     <!DOCTYPE html>
   *     <html>
   *     <head>
   *     <title>Include fragment example</title>
   *     </head>
   *     <body>
   *     <h1>Objectos HTML</h1>
   *     <p>Using the include instruction</p>
   *     </body>
   *     </html>
   * }</pre>
   *
   * <p>
   * Note that the methods of included method references all return
   * {@code void}.
   *
   * @param fragment
   *        the fragment to include
   *
   * @return an instruction representing this fragment
   */
  public final Api.Fragment include(FragmentLambda fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    fragment.execute();

    fragmentEnd(index);

    return Api.FRAGMENT;
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
  public final Api.NoOp noop() {
    return Api.NOOP;
  }

  public final Api.Element raw(String text) {
    Check.notNull(text, "text == null");

    rawImpl(text);

    return Api.ELEMENT;
  }

  /**
   * Generates a text node with the specified {@code text} value. The text value
   * is escaped before being emitted to the output.
   *
   * <p>
   * The following Objectos HTML template:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region = "text"}
   *
   * <p>
   * Generates the following HTML:
   *
   * <pre>{@code
   *     <p><strong>This is in bold</strong> &amp; this is not</p>
   * }</pre>
   *
   * @param text
   *        the text value to be added
   *
   * @return an instruction representing the text node
   */
  public final Api.Element text(String text) {
    Check.notNull(text, "text == null");

    textImpl(text);

    return Api.ELEMENT;
  }

  public final Api.Fragment render(Consumer<Html> fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    fragment.accept(this);

    fragmentEnd(index);

    return Api.FRAGMENT;
  }

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    WayHtmlFormatter.INSTANCE.formatTo(this, dest);
  }

  //

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

    objectArray[objectIndex + OFFSET_ELEMENT] = new PseudoHtmlElement(this);

    objectArray[objectIndex + OFFSET_ATTRIBUTE] = new PseudoHtmlAttribute(this);

    objectArray[objectIndex + OFFSET_TEXT] = new PseudoHtmlText();

    objectArray[objectIndex + OFFSET_RAW] = new PseudoHtmlRawText();

    documentCtx();

    return new PseudoHtmlDocument(this);
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
        case ByteProto.DOCTYPE, ByteProto.ELEMENT, ByteProto.TEXT -> {
          // next node found
          nextState = _DOCUMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.LENGTH2 -> {
          index++;

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          index += Bytes.decodeInt(b0, b1);
        }

        case ByteProto.LENGTH3 -> {
          index++;

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          byte b2;
          b2 = main[index++];

          index += Bytes.decodeLength3(b0, b1, b2);
        }

        case ByteProto.MARKED3 -> index += 3;

        case ByteProto.MARKED4 -> index += 4;

        case ByteProto.MARKED5 -> index += 5;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    stateSet(nextState);

    documentCtxMainIndexStore(index);

    return nextState == _DOCUMENT_NODES_HAS_NEXT;
  }

  final HtmlNode documentNext() {
    stateCAS(_DOCUMENT_NODES_HAS_NEXT, _DOCUMENT_NODES_NEXT);

    // restore main index from the context
    int index;
    index = documentCtxMainIndexLoad();

    // next
    byte proto;
    proto = main[index++];

    return switch (proto) {
      case ByteProto.DOCTYPE -> {
        documentCtxMainIndexStore(index);

        yield PseudoHtmlDocumentType.INSTANCE;
      }

      case ByteProto.ELEMENT -> {
        byte b0;
        b0 = main[index++];

        byte b1;
        b1 = main[index++];

        int length;
        length = Bytes.decodeInt(b0, b1);

        int elementStartIndex;
        elementStartIndex = index;

        int parentIndex;
        parentIndex = index + length;

        documentCtxMainIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case ByteProto.TEXT -> {
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
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        Bytes.encodeInt2(0)
    );
  }

  private int documentCtxMainIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 1];

    byte b1;
    b1 = aux[mainStart + 2];

    byte b2;
    b2 = aux[mainStart + 3];

    return Bytes.decodeLength3(b0, b1, b2);
  }

  private void documentCtxMainIndexStore(int value) {
    aux[mainStart + 1] = Bytes.encodeInt0(value);

    aux[mainStart + 2] = Bytes.encodeInt1(value);

    aux[mainStart + 3] = Bytes.encodeInt2(value);
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

    elementCtx(startIndex, parentIndex);

    PseudoHtmlElement element;
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

  final boolean elementAttributesHasNext(StandardElementName parent) {
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
        case ByteProto.STANDARD_NAME -> index += 1;

        case ByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          Ambiguous ambiguous;
          ambiguous = Ambiguous.decode(ordinalByte);

          if (ambiguous.isAttributeOf(parent)) {
            index = rollbackIndex;

            nextState = _ELEMENT_ATTRS_HAS_NEXT;

            break loop;
          }
        }

        case ByteProto.ATTRIBUTE0,
             ByteProto.ATTRIBUTE1,
             ByteProto.ATTRIBUTE_CLASS,
             ByteProto.ATTRIBUTE_ID -> {
          index = rollbackIndex;

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case ByteProto.ELEMENT,
             ByteProto.RAW,
             ByteProto.TEXT -> index = skipVarInt(index);

        case ByteProto.END -> {
          index = rollbackIndex;

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
    final PseudoHtmlAttribute attribute;
    attribute = htmlAttribute();

    // values to set
    byte attr, v0 = -1, v1 = -1;

    byte proto;
    proto = main[index++];

    switch (proto) {
      case ByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        byte ordinalByte;
        ordinalByte = main[auxStart++];

        Ambiguous ambiguous;
        ambiguous = Ambiguous.decode(ordinalByte);

        attr = ambiguous.encodeAttribute();

        v0 = main[auxStart++];

        v1 = main[auxStart++];
      }

      case ByteProto.ATTRIBUTE0 -> {
        index = jmp2(index);

        attr = main[auxStart++];
      }

      case ByteProto.ATTRIBUTE1 -> {
        index = jmp2(index);

        attr = main[auxStart++];

        v0 = main[auxStart++];

        v1 = main[auxStart++];
      }

      case ByteProto.ATTRIBUTE_CLASS -> {
        int ordinal;
        ordinal = AttributeName.CLASS.index();

        attr = Bytes.encodeInt0(ordinal);

        v0 = main[index++];

        v1 = main[index++];
      }

      case ByteProto.ATTRIBUTE_ID -> {
        int ordinal;
        ordinal = AttributeName.ID.index();

        attr = Bytes.encodeInt0(ordinal);

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
    ordinal = Bytes.decodeInt(attr);

    attribute.name = WayAttributeName.get(ordinal);

    // attribute value
    String value;
    value = null;

    if (v0 != -1 || v1 != -1) {
      int objectIndex;
      objectIndex = Bytes.decodeInt(v0, v1);

      Object o;
      o = objectArray[objectIndex];

      value = String.valueOf(o);
    }

    attribute.value = value;

    // store new state
    elementCtxAttrsIndexStore(index);

    stateSet(_ELEMENT_ATTRS_NEXT);

    return attribute;
  }

  private PseudoHtmlAttribute htmlAttribute() {
    return (PseudoHtmlAttribute) objectArray[objectIndex + OFFSET_ATTRIBUTE];
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

    PseudoHtmlAttribute attribute;
    attribute = htmlAttribute();

    if (attribute.value != null) {
      stateSet(_ATTRIBUTE_VALUES_HAS_NEXT);

      return true;
    }

    // restore index from context
    int index;
    index = elementCtxAttrsIndexLoad();

    // current attribute
    AttributeName attributeName;
    attributeName = attribute.name;

    int attributeCode;
    attributeCode = attributeName.index();

    byte currentAttr;
    currentAttr = Bytes.encodeInt0(attributeCode);

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
        case ByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          Ambiguous ambiguous;
          ambiguous = Ambiguous.get(ordinal);

          // find out the parent
          PseudoHtmlElement element;
          element = htmlElement();

          StandardElementName elementName;
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

        case ByteProto.ATTRIBUTE0 -> {
          index = rollbackIndex;

          break loop;
        }

        case ByteProto.ATTRIBUTE1 -> {
          index = jmp2(index);

          byte attr;
          attr = main[auxStart++];

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case ByteProto.ATTRIBUTE_CLASS -> {
          int ordinal;
          ordinal = AttributeName.CLASS.index();

          byte attr;
          attr = Bytes.encodeInt0(ordinal);

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case ByteProto.ATTRIBUTE_ID -> {
          int ordinal;
          ordinal = AttributeName.ID.index();

          byte attr;
          attr = Bytes.encodeInt0(ordinal);

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case ByteProto.ELEMENT,
             ByteProto.RAW,
             ByteProto.TEXT -> index = skipVarInt(index);

        case ByteProto.END -> {
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

  final String attributeValuesNext(String maybeNext) {
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
      case ByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        elementCtxAttrsIndexStore(index);

        // skip ordinal
        auxStart++;

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        yield toObjectString(v0, v1);
      }

      case ByteProto.ATTRIBUTE1 -> {
        index = jmp2(index);

        elementCtxAttrsIndexStore(index);

        // skip ordinal
        auxStart++;

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        yield toObjectString(v0, v1);
      }

      case ByteProto.ATTRIBUTE_CLASS -> {
        byte v0;
        v0 = main[index++];

        byte v1;
        v1 = main[index++];

        elementCtxAttrsIndexStore(index);

        yield toObjectString(v0, v1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  private String toObjectString(byte v0, byte v1) {
    int objectIndex;
    objectIndex = Bytes.decodeInt(v0, v1);

    Object o;
    o = objectArray[objectIndex];

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
        PseudoHtmlElement element;
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
        case ByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          Ambiguous ambiguous;
          ambiguous = Ambiguous.get(ordinal);

          // find out parent element
          PseudoHtmlElement element;
          element = htmlElement();

          StandardElementName parent;
          parent = element.name;

          if (ambiguous.isAttributeOf(parent)) {
            continue loop;
          }

          index = rollbackIndex;

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.ATTRIBUTE1 -> index = skipVarInt(index);

        case ByteProto.ATTRIBUTE_CLASS,
             ByteProto.ATTRIBUTE_ID -> index += 2;

        case ByteProto.ELEMENT,
             ByteProto.RAW,
             ByteProto.TEXT -> {
          index = rollbackIndex;

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case ByteProto.END -> {
          index = rollbackIndex;

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

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxNodesIndexStore(index);

    stateSet(nextState);

    return nextState == _ELEMENT_NODES_HAS_NEXT;
  }

  final HtmlNode elementNodesNext() {
    stateCAS(_ELEMENT_NODES_HAS_NEXT, _ELEMENT_NODES_NEXT);

    // restore index from context
    int index;
    index = elementCtxNodesIndexLoad();

    byte proto;
    proto = main[index++];

    return switch (proto) {
      case ByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        // load ambiguous name

        byte ordinalByte;
        ordinalByte = main[auxStart++];

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        int ordinal;
        ordinal = Bytes.decodeInt(ordinalByte);

        Ambiguous ambiguous;
        ambiguous = Ambiguous.get(ordinal);

        StandardElementName element;
        element = ambiguous.element;

        main = ByteArrays.growIfNecessary(main, mainIndex + 13);

        /*00*/main[mainIndex++] = ByteProto.MARKED4;
        /*01*/main[mainIndex++] = v0;
        /*02*/main[mainIndex++] = v1;
        /*03*/main[mainIndex++] = ByteProto.INTERNAL4;

        /*04*/main[mainIndex++] = ByteProto.LENGTH2;
        /*05*/main[mainIndex++] = Bytes.encodeInt0(7);
        /*06*/main[mainIndex++] = Bytes.encodeInt0(7);
        int elementStartIndex = mainIndex;
        /*07*/main[mainIndex++] = ByteProto.STANDARD_NAME;
        /*08*/main[mainIndex++] = (byte) element.ordinal();
        /*09*/main[mainIndex++] = ByteProto.TEXT;
        /*10*/main[mainIndex++] = Bytes.encodeInt0(10);
        /*11*/main[mainIndex++] = ByteProto.END;
        /*12*/main[mainIndex++] = Bytes.encodeInt0(11);
        /*13*/main[mainIndex++] = ByteProto.INTERNAL;

        int parentIndex;
        parentIndex = index;

        elementCtxNodesIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case ByteProto.ELEMENT -> {
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

      case ByteProto.RAW -> {
        index = jmp2(index);

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        elementCtxNodesIndexStore(index);

        // return value
        PseudoHtmlRawText raw;
        raw = (PseudoHtmlRawText) objectArray[objectIndex + OFFSET_RAW];

        // text value
        raw.value = toObjectString(v0, v1);

        yield raw;
      }

      case ByteProto.TEXT -> {
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

  private PseudoHtmlText htmlText(byte v0, byte v1) {
    PseudoHtmlText text;
    text = (PseudoHtmlText) objectArray[objectIndex + OFFSET_TEXT];

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
    aux[auxIndex++] = Bytes.encodeInt0(startIndex);
    aux[auxIndex++] = Bytes.encodeInt1(startIndex);
    aux[auxIndex++] = Bytes.encodeInt2(startIndex);

    // 4-6 nodes iteration index
    aux[auxIndex++] = Bytes.encodeInt0(startIndex);
    aux[auxIndex++] = Bytes.encodeInt1(startIndex);
    aux[auxIndex++] = Bytes.encodeInt2(startIndex);

    // 7-9 start index
    aux[auxIndex++] = Bytes.encodeInt0(startIndex);
    aux[auxIndex++] = Bytes.encodeInt1(startIndex);
    aux[auxIndex++] = Bytes.encodeInt2(startIndex);

    // 10-12 parent index
    aux[auxIndex++] = Bytes.encodeInt0(parentIndex);
    aux[auxIndex++] = Bytes.encodeInt1(parentIndex);
    aux[auxIndex++] = Bytes.encodeInt2(parentIndex);

    // 13 parent context length
    aux[auxIndex++] = Bytes.encodeInt0(length);
  }

  private int elementCtxAttrsIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 1];

    byte b1;
    b1 = aux[mainStart + 2];

    byte b2;
    b2 = aux[mainStart + 3];

    return Bytes.decodeLength3(b0, b1, b2);
  }

  private void elementCtxAttrsIndexStore(int value) {
    aux[mainStart + 1] = Bytes.encodeInt0(value);

    aux[mainStart + 2] = Bytes.encodeInt1(value);

    aux[mainStart + 3] = Bytes.encodeInt2(value);
  }

  private StandardElementName elementCtxNameLoad() {
    // restore start index
    byte b0;
    b0 = aux[mainStart + 7];

    byte b1;
    b1 = aux[mainStart + 8];

    byte b2;
    b2 = aux[mainStart + 9];

    int startIndex;
    startIndex = Bytes.decodeLength3(b0, b1, b2);

    StandardElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[startIndex++];

    switch (proto) {
      case ByteProto.STANDARD_NAME -> {
        byte nameByte;
        nameByte = main[startIndex++];

        int ordinal;
        ordinal = Bytes.decodeInt(nameByte);

        name = StandardElementName.getByCode(ordinal);
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

    return Bytes.decodeLength3(b0, b1, b2);
  }

  private void elementCtxNodesIndexStore(int value) {
    aux[mainStart + 4] = Bytes.encodeInt0(value);

    aux[mainStart + 5] = Bytes.encodeInt1(value);

    aux[mainStart + 6] = Bytes.encodeInt2(value);
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
    parentIndex = Bytes.decodeLength3(b0, b1, b2);

    // restore parent length
    byte len;
    len = aux[mainStart + 13];

    int length;
    length = Bytes.decodeInt(len);

    // remove this context
    auxIndex = mainStart;

    // set parent as the current context
    mainStart = auxIndex - length;

    return parentIndex;
  }

  private PseudoHtmlElement htmlElement() {
    return (PseudoHtmlElement) objectArray[objectIndex + OFFSET_ELEMENT];
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

    auxStart = Bytes.decodeOffset(main, startIndex, index);

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