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
import java.util.Objects;
import java.util.function.Consumer;

final class HtmlMarkup implements Html.Markup {

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

  @Override
  public final Html.Instruction.OfFragment renderFragment(Html.Fragment.Of0 fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke();
    } catch (Error | RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of1<T1> fragment, T1 arg1) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1);
    } catch (Error | RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2);
    } catch (Error | RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2, T3> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2, arg3);
    } catch (Error | RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2, T3, T4> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2, arg3, arg4);
    } catch (Error | RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final Html.Instruction.OfFragment renderPlugin(Consumer<Html.Markup> plugin) {
    Check.notNull(plugin, "plugin == null");

    int index;
    index = fragmentBegin();

    plugin.accept(this);

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  @Override
  public final Html.Instruction.OfFragment renderTemplate(Html.Template template) {
    Check.notNull(template, "template == null");

    try {
      int index;
      index = fragmentBegin();

      template.html = this;

      template.tryToRender();

      fragmentEnd(index);
    } finally {
      template.html = null;
    }

    return Html.FRAGMENT;
  }

  final String testableText() {
    StringBuilder sb;
    sb = new StringBuilder();

    HtmlDom document;
    document = compile();

    for (var node : document.nodes()) {
      switch (node) {
        case Html.Dom.Element element -> testableElement(sb, element);

        default -> {}
      }
    }

    return sb.toString();
  }

  private void testableElement(StringBuilder sb, Html.Dom.Element element) {
    for (var node : element.nodes()) {
      switch (node) {
        case Html.Dom.Element child -> testableElement(sb, child);

        case Html.Dom.Text text -> {
          String testable;
          testable = text.testable();

          if (testable == null) {
            continue;
          }

          sb.append(testable);
          sb.append(':');

          String value;
          value = text.value();

          value = value.trim();

          if (!value.isEmpty()) {
            sb.append(' ');
            sb.append(value);
          }

          sb.append(System.lineSeparator());
        }

        default -> {}
      }
    }
  }

  public final String toJsonString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      HtmlDom document;
      document = compile();

      HtmlFormatter.JSON.formatTo(document, sb);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      HtmlDom document;
      document = compile();

      HtmlFormatter.STANDARD.formatTo(document, sb);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  public final Html.Instruction.OfAttribute attribute(Html.AttributeName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    return attribute0(name, value);
  }

  @Override
  public final Html.Instruction.OfDataOn dataOnClick(Script.Action action) {
    Check.notNull(action, "action == null");

    return dataOn0(HtmlAttributeName.DATA_ON_CLICK, action);
  }

  @Override
  public final Html.Instruction.OfDataOn dataOnClick(Script.Action... actions) {
    return dataOn1(HtmlAttributeName.DATA_ON_CLICK, actions);
  }

  @Override
  public final Html.Instruction.OfDataOn dataOnInput(Script.Action action) {
    Check.notNull(action, "action == null");

    return dataOn0(HtmlAttributeName.DATA_ON_INPUT, action);
  }

  @Override
  public final Html.Instruction.OfDataOn dataOnInput(Script.Action... actions) {
    return dataOn1(HtmlAttributeName.DATA_ON_INPUT, actions);
  }

  private Html.Instruction.OfDataOn dataOn0(Html.AttributeName name, Script.Action value) {
    if (value == Script.noop()) {
      return Html.NOOP;
    } else {
      return attribute0(name, value);
    }
  }

  private Html.Instruction.OfDataOn dataOn1(Html.AttributeName name, Script.Action... actions) {
    Check.notNull(actions, "actions == null");

    Script.Action value;
    value = Script.join(actions);

    return dataOn0(name, value);
  }

  /**
   * Flattens the specified instructions so that each of the specified
   * instructions is individually added, in order, to a receiving element.
   *
   * <p>
   * This is useful, for example, when creating {@link HtmlComponent}
   * instances. The following Objectos HTML code:
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
  @Override
  public final Html.Instruction.OfElement flatten(Html.Instruction... contents) {
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
  public final Html.Instruction.NoOp noop() {
    return Html.NOOP;
  }

  @Override
  public final Html.Instruction.OfElement raw(String text) {
    Check.notNull(text, "text == null");

    rawImpl(text);

    return Html.ELEMENT;
  }

  @Override
  public final Html.Instruction.OfElement testable(String name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    testableImpl(name, value);

    return Html.ELEMENT;
  }

  /**
   * Generates a text node with the specified {@code text} value. The text
   * value is escaped before being emitted to the output.
   *
   * <p>
   * The following Objectos HTML template:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
   * "text"}
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
  @Override
  public final Html.Instruction.OfElement text(String text) {
    Check.notNull(text, "text == null");

    textImpl(text);

    return Html.ELEMENT;
  }

  //
  // Section: DOM related methods
  //

  final HtmlDom compile() {
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
      objectArray = Util.growIfNecessary(objectArray, objectIndex + OFFSET_MAX);
    }

    objectArray[objectIndex + OFFSET_ELEMENT] = new HtmlDomElement(this);

    objectArray[objectIndex + OFFSET_ATTRIBUTE] = new HtmlDomAttribute(this);

    objectArray[objectIndex + OFFSET_TEXT] = new HtmlDomText();

    objectArray[objectIndex + OFFSET_RAW] = new HtmlDomRaw();

    documentCtx();

    return new HtmlDom(this);
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

      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED, _ELEMENT_NODES_EXHAUSTED -> {
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

        case HtmlByteProto.MARKED6 -> index += 6;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    stateSet(nextState);

    documentCtxMainIndexStore(index);

    return nextState == _DOCUMENT_NODES_HAS_NEXT;
  }

  final Html.Dom.Node documentNext() {
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

        yield HtmlDomDocumentType.INSTANCE;
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

  final HtmlDomElement element(int startIndex, int parentIndex) {
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

    HtmlDomElement element;
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

  final boolean elementAttributesHasNext(Html.ElementName parent) {
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
             HtmlByteProto.ATTRIBUTE_EXT1 -> {
          index = rollbackIndex;

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT,
             HtmlByteProto.TESTABLE -> index = skipVarInt(index);

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

  final HtmlDomAttribute elementAttributesNext() {
    stateCAS(_ELEMENT_ATTRS_HAS_NEXT, _ELEMENT_ATTRS_NEXT);

    // restore main index
    int index;
    index = elementCtxAttrsIndexLoad();

    // our return value
    final HtmlDomAttribute attribute;
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

      case HtmlByteProto.ATTRIBUTE_EXT1 -> {
        attr = main[index++];

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

  private HtmlDomAttribute htmlAttribute() {
    return (HtmlDomAttribute) objectArray[objectIndex + OFFSET_ATTRIBUTE];
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

    HtmlDomAttribute attribute;
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
          HtmlDomElement element;
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

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          byte attr;
          attr = main[index++];

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TESTABLE,
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

      case HtmlByteProto.ATTRIBUTE_EXT1 -> {
        // skip ordinal
        index++;

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
        HtmlDomElement element;
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
          HtmlDomElement element;
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

        case HtmlByteProto.ATTRIBUTE_EXT1 -> index += 3;

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT,
             HtmlByteProto.TESTABLE -> {
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

  final Html.Dom.Node elementNodesNext() {
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

        main = Util.growIfNecessary(main, mainIndex + 13);

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
        HtmlDomRaw raw;
        raw = (HtmlDomRaw) objectArray[objectIndex + OFFSET_RAW];

        // text value
        raw.value = toObjectString(v0, v1);

        yield raw;
      }

      case HtmlByteProto.TESTABLE -> {
        index = jmp2(index);

        byte t0;
        t0 = main[auxStart++];

        byte t1;
        t1 = main[auxStart++];

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        elementCtxNodesIndexStore(index);

        yield htmlText(t0, t1, v0, v1);
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

  private HtmlDomText htmlText(byte v0, byte v1) {
    HtmlDomText text;
    text = (HtmlDomText) objectArray[objectIndex + OFFSET_TEXT];

    // testable
    text.testable = null;

    // text value
    text.value = toObjectString(v0, v1);

    return text;
  }

  private HtmlDomText htmlText(byte t0, byte t1, byte v0, byte v1) {
    HtmlDomText text;
    text = (HtmlDomText) objectArray[objectIndex + OFFSET_TEXT];

    // testable
    text.testable = toObjectString(t0, t1);

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
    aux = Util.growIfNecessary(aux, auxIndex + 13);

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

  private HtmlDomElement htmlElement() {
    return (HtmlDomElement) objectArray[objectIndex + OFFSET_ELEMENT];
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

  //
  // Section: recording methods
  //

  byte[] aux = new byte[128];

  int auxIndex;

  int auxStart;

  byte[] main = new byte[256];

  int mainContents;

  int mainIndex;

  int mainStart;

  Object[] objectArray;

  int objectIndex;

  final void compilationBegin() {
    auxIndex = auxStart = 0;

    mainContents = mainIndex = mainStart = 0;

    objectIndex = 0;
  }

  final void compilationEnd() {
    // TODO remove...
  }

  final void ambiguous(HtmlAmbiguous name, String value) {
    int ordinal;
    ordinal = name.ordinal();

    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.AMBIGUOUS1,

        // name
        HtmlBytes.encodeInt0(ordinal),

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL5
    );
  }

  final Html.Instruction.OfAttribute attribute0(Html.AttributeName name) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    mainAdd(
        HtmlByteProto.ATTRIBUTE0,

        // name
        HtmlBytes.encodeInt0(index),

        HtmlByteProto.INTERNAL3
    );

    return Html.ATTRIBUTE;
  }

  final Html.AttributeOrNoOp attribute0(Html.AttributeName name, Object value) {
    return attribute1(name, value, HtmlByteProto.ATTRIBUTE1);
  }

  private Html.AttributeOrNoOp attribute1(Html.AttributeName name, Object value, byte proto) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    int object;
    object = objectAdd(value);

    mainAdd(
        proto,

        // name
        HtmlBytes.encodeInt0(index),

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL5
    );

    return Html.ATTRIBUTE;
  }

  @Override
  public final Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents) {
    Check.notNull(name, "name == null");

    elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();

    return Html.ELEMENT;
  }

  public final Html.Instruction.OfElement element(Html.ElementName name, String text) {
    Check.notNull(name, "name == null");
    Check.notNull(text, "text == null");

    textImpl(text);

    elementBegin(name);
    elementValue(Html.ELEMENT);
    elementEnd();

    return Html.ELEMENT;
  }

  final void elementBegin(Html.ElementName name) {
    commonBegin();

    mainAdd(
        HtmlByteProto.ELEMENT,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,

        HtmlByteProto.STANDARD_NAME,

        HtmlBytes.encodeName(name)
    );
  }

  final void elementValue(Html.Instruction value) {
    if (value == Html.ATTRIBUTE ||
        value == Html.ELEMENT ||
        value == Html.FRAGMENT) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case HtmlByteProto.INTERNAL -> {
          int endIndex;
          endIndex = mainContents;

          byte maybeNeg;

          do {
            maybeNeg = main[mainContents--];
          } while (maybeNeg < 0);

          int length;
          length = HtmlBytes.decodeCommonEnd(main, mainContents, endIndex);

          mainContents -= length;
        }

        case HtmlByteProto.INTERNAL3 -> mainContents -= 3 - 2;

        case HtmlByteProto.INTERNAL4 -> mainContents -= 4 - 2;

        case HtmlByteProto.INTERNAL5 -> mainContents -= 5 - 2;

        case HtmlByteProto.INTERNAL6 -> mainContents -= 6 - 2;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }

      auxAdd(HtmlByteProto.INTERNAL);
    }

    else if (value instanceof Html.AttributeObject ext) {
      Html.AttributeName name;
      name = ext.name();

      int nameIndex;
      nameIndex = name.index();

      if (nameIndex < 0) {
        throw new UnsupportedOperationException("Custom attribute name");
      }

      int valueIndex;
      valueIndex = externalValue(ext.value());

      auxAdd(
          HtmlByteProto.ATTRIBUTE_EXT1,

          // name
          HtmlBytes.encodeInt0(nameIndex),

          // value
          HtmlBytes.encodeInt0(valueIndex),
          HtmlBytes.encodeInt1(valueIndex)
      );
    }

    else if (value == Html.NOOP) {
      // no-op
    }

    else {
      throw new UnsupportedOperationException(
          "Implement me :: type=" + value.getClass()
      );
    }
  }

  final void elementEnd() {
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
        case HtmlByteProto.TEXT -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          mainAdd(mark, aux[index++], aux[index++], aux[index++]);
        }

        case HtmlByteProto.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case HtmlByteProto.ATTRIBUTE0 -> {
                contents = encodeInternal3(contents, proto);

                continue loop;
              }

              case HtmlByteProto.AMBIGUOUS1,
                   HtmlByteProto.ATTRIBUTE1 -> {
                contents = encodeInternal5(contents, proto);

                continue loop;
              }

              case HtmlByteProto.ELEMENT -> {
                contents = encodeElement(contents, proto);

                continue loop;
              }

              case HtmlByteProto.FLATTEN -> {
                contents = encodeFlatten(contents);

                continue loop;
              }

              case HtmlByteProto.FRAGMENT -> {
                contents = encodeFragment(contents);

                continue loop;
              }

              case HtmlByteProto.LENGTH2 -> contents = encodeLength2(contents);

              case HtmlByteProto.LENGTH3 -> contents = encodeLength3(contents);

              case HtmlByteProto.MARKED3 -> contents += 3;

              case HtmlByteProto.MARKED4 -> contents += 4;

              case HtmlByteProto.MARKED5 -> contents += 5;

              case HtmlByteProto.MARKED6 -> contents += 6;

              case HtmlByteProto.RAW,
                   HtmlByteProto.TEXT -> {
                contents = encodeInternal4(contents, proto);

                continue loop;
              }

              case HtmlByteProto.TESTABLE -> {
                contents = encodeInternal6(contents, proto);

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

    commonEnd(mainContents, mainStart);

    // we clear the aux list
    auxIndex = auxStart;
  }

  final void flattenBegin() {
    commonBegin();

    mainAdd(
        HtmlByteProto.FLATTEN,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );
  }

  final void rawImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.RAW,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  final void testableImpl(String name, String value) {
    int nameIdx;
    nameIdx = objectAdd(name);

    int valueIdx;
    valueIdx = objectAdd(value);

    mainAdd(
        HtmlByteProto.TESTABLE,

        // name
        HtmlBytes.encodeInt0(nameIdx),
        HtmlBytes.encodeInt1(nameIdx),

        // value
        HtmlBytes.encodeInt0(valueIdx),
        HtmlBytes.encodeInt1(valueIdx),

        HtmlByteProto.INTERNAL6
    );
  }

  final void textImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.TEXT,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  private void auxAdd(byte b0) {
    aux = Util.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  private void auxAdd(byte b0, byte b1, byte b2, byte b3) {
    aux = Util.growIfNecessary(aux, auxIndex + 3);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
  }

  private void commonBegin() {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;
  }

  private void commonEnd(int contentsIndex, int startIndex) {
    // ensure main can hold 5 more elements
    // - ByteProto.END
    // - length
    // - length
    // - length
    // - ByteProto.INTERNAL
    main = Util.growIfNecessary(main, mainIndex + 4);

    // mark the end
    main[mainIndex++] = HtmlByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - contentsIndex - 1;

    mainIndex = HtmlBytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = HtmlByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[startIndex + 1] = HtmlBytes.encodeInt0(length);
    main[startIndex + 2] = HtmlBytes.encodeInt1(length);
  }

  private int encodeElement(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents++] = HtmlByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeFlatten(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = HtmlByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          byte idx0;
          idx0 = main[index++];

          byte idx1;
          idx1 = main[index++];

          byte idx2;
          idx2 = main[index++];

          mainAdd(proto, idx0, idx1, idx2);
        }

        case HtmlByteProto.AMBIGUOUS1,
             HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ELEMENT,
             HtmlByteProto.TEXT,
             HtmlByteProto.RAW -> {
          int elementIndex;
          elementIndex = index;

          do {
            len0 = main[index++];
          } while (len0 < 0);

          int len;
          len = HtmlBytes.decodeOffset(main, elementIndex, index);

          elementIndex -= len;

          // ensure main can hold least 4 elements
          // 0   - ByteProto
          // 1-3 - variable length
          main = Util.growIfNecessary(main, mainIndex + 3);

          main[mainIndex++] = proto;

          int length;
          length = mainIndex - elementIndex;

          mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);
        }

        case HtmlByteProto.END -> {
          break loop;
        }

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
          );
        }
      }
    }

    return maxIndex;
  }

  private int encodeFragment(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = HtmlByteProto.LENGTH3;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    byte len2;
    len2 = main[index++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeLength3(len0, len1, len2);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> index = encodeInternal5(index, proto);

        case HtmlByteProto.ATTRIBUTE0 -> index = encodeInternal3(index, proto);

        case HtmlByteProto.ATTRIBUTE1 -> index = encodeInternal5(index, proto);

        case HtmlByteProto.ELEMENT -> index = encodeElement(index, proto);

        case HtmlByteProto.END -> {
          break loop;
        }

        case HtmlByteProto.FRAGMENT -> index = encodeFragment(index);

        case HtmlByteProto.LENGTH2 -> index = encodeLength2(index);

        case HtmlByteProto.LENGTH3 -> index = encodeLength3(index);

        case HtmlByteProto.MARKED3 -> index += 3;

        case HtmlByteProto.MARKED4 -> index += 4;

        case HtmlByteProto.MARKED5 -> index += 5;

        case HtmlByteProto.MARKED6 -> index += 6;

        case HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> index = encodeInternal4(index, proto);

        case HtmlByteProto.TESTABLE -> index = encodeInternal6(index, proto);

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
    main[contents] = HtmlByteProto.MARKED3;

    // point to next
    int offset;
    offset = 3;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal4(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED4;

    // point to next
    int offset;
    offset = 4;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal5(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED5;

    // point to next
    int offset;
    offset = 5;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal6(int contents, byte proto) {
    return encodeInternalN(contents, proto, HtmlByteProto.MARKED6, 6);
  }

  private int encodeInternalN(int contents, byte proto, byte marker, int offset) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = marker;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeLength2(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    int length;
    length = HtmlBytes.decodeInt(len0, len1);

    // point to next element
    return contents + length;
  }

  private int encodeLength3(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    byte len2;
    len2 = main[contents++];

    int length;
    length = HtmlBytes.decodeLength3(len0, len1, len2);

    // point to next element
    return contents + length;
  }

  private int externalValue(String value) {
    String result;
    result = value;

    if (value == null) {
      result = "null";
    }

    return objectAdd(result);
  }

  final int fragmentBegin() {
    // we mark:
    // 1) the start of the contents of the current declaration
    int startIndex;
    startIndex = mainIndex;

    mainAdd(
        HtmlByteProto.FRAGMENT,

        // length takes 3 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );

    return startIndex;
  }

  final void fragmentEnd(int startIndex) {
    // ensure main can hold 5 more elements
    // - ByteProto.END
    // - length
    // - length
    // - length
    // - ByteProto.INTERNAL
    main = Util.growIfNecessary(main, mainIndex + 4);

    // mark the end
    main[mainIndex++] = HtmlByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - startIndex - 1;

    mainIndex = HtmlBytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = HtmlByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1 + len2
    length -= 4;

    // we skip the first byte proto
    HtmlBytes.encodeLength3(main, startIndex + 1, length);
  }

  private void mainAdd(byte b0) {
    main = Util.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1, byte b2) {
    main = Util.growIfNecessary(main, mainIndex + 2);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3) {
    main = Util.growIfNecessary(main, mainIndex + 3);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    main = Util.growIfNecessary(main, mainIndex + 4);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    main = Util.growIfNecessary(main, mainIndex + 5);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
  }

  private int objectAdd(Object value) {
    int index;
    index = objectIndex++;

    if (objectArray == null) {
      objectArray = new Object[10];
    }

    objectArray = Util.growIfNecessary(objectArray, objectIndex);

    objectArray[index] = value;

    return index;
  }

  //
  // Section: elements methods
  //

  /**
   * Generates the {@code <!DOCTYPE html>} doctype.
   */
  @Override
  public final void doctype() {
    mainAdd(HtmlByteProto.DOCTYPE);
  }

  /**
   * Renders the {@code a} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return element(HtmlElementName.A, contents);
  }

  /**
   * Renders the {@code a} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement a(String text) {
    return element(HtmlElementName.A, text);
  }

  /**
   * Renders the {@code abbr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return element(HtmlElementName.ABBR, contents);
  }

  /**
   * Renders the {@code abbr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement abbr(String text) {
    return element(HtmlElementName.ABBR, text);
  }

  /**
   * Renders the {@code article} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return element(HtmlElementName.ARTICLE, contents);
  }

  /**
   * Renders the {@code article} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement article(String text) {
    return element(HtmlElementName.ARTICLE, text);
  }

  /**
   * Renders the {@code b} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return element(HtmlElementName.B, contents);
  }

  /**
   * Renders the {@code b} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement b(String text) {
    return element(HtmlElementName.B, text);
  }

  /**
   * Renders the {@code blockquote} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return element(HtmlElementName.BLOCKQUOTE, contents);
  }

  /**
   * Renders the {@code blockquote} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement blockquote(String text) {
    return element(HtmlElementName.BLOCKQUOTE, text);
  }

  /**
   * Renders the {@code body} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return element(HtmlElementName.BODY, contents);
  }

  /**
   * Renders the {@code body} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement body(String text) {
    return element(HtmlElementName.BODY, text);
  }

  /**
   * Renders the {@code br} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.BR, contents);
  }

  /**
   * Renders the {@code button} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return element(HtmlElementName.BUTTON, contents);
  }

  /**
   * Renders the {@code button} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement button(String text) {
    return element(HtmlElementName.BUTTON, text);
  }

  /**
   * Renders the {@code clipPath} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return element(HtmlElementName.CLIPPATH, contents);
  }

  /**
   * Renders the {@code clipPath} attribute or element with the specified
   * text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfElement clipPath(String text) {
    ambiguous(HtmlAmbiguous.CLIPPATH, text);
    return Html.ELEMENT;
  }

  /**
   * Renders the {@code code} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return element(HtmlElementName.CODE, contents);
  }

  /**
   * Renders the {@code code} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement code(String text) {
    return element(HtmlElementName.CODE, text);
  }

  /**
   * Renders the {@code dd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return element(HtmlElementName.DD, contents);
  }

  /**
   * Renders the {@code dd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dd(String text) {
    return element(HtmlElementName.DD, text);
  }

  /**
   * Renders the {@code defs} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return element(HtmlElementName.DEFS, contents);
  }

  /**
   * Renders the {@code defs} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement defs(String text) {
    return element(HtmlElementName.DEFS, text);
  }

  /**
   * Renders the {@code details} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return element(HtmlElementName.DETAILS, contents);
  }

  /**
   * Renders the {@code details} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement details(String text) {
    return element(HtmlElementName.DETAILS, text);
  }

  /**
   * Renders the {@code div} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return element(HtmlElementName.DIV, contents);
  }

  /**
   * Renders the {@code div} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement div(String text) {
    return element(HtmlElementName.DIV, text);
  }

  /**
   * Renders the {@code dl} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return element(HtmlElementName.DL, contents);
  }

  /**
   * Renders the {@code dl} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dl(String text) {
    return element(HtmlElementName.DL, text);
  }

  /**
   * Renders the {@code dt} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return element(HtmlElementName.DT, contents);
  }

  /**
   * Renders the {@code dt} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dt(String text) {
    return element(HtmlElementName.DT, text);
  }

  /**
   * Renders the {@code em} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return element(HtmlElementName.EM, contents);
  }

  /**
   * Renders the {@code em} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement em(String text) {
    return element(HtmlElementName.EM, text);
  }

  /**
   * Renders the {@code fieldset} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return element(HtmlElementName.FIELDSET, contents);
  }

  /**
   * Renders the {@code fieldset} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement fieldset(String text) {
    return element(HtmlElementName.FIELDSET, text);
  }

  /**
   * Renders the {@code figure} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return element(HtmlElementName.FIGURE, contents);
  }

  /**
   * Renders the {@code figure} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement figure(String text) {
    return element(HtmlElementName.FIGURE, text);
  }

  /**
   * Renders the {@code footer} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return element(HtmlElementName.FOOTER, contents);
  }

  /**
   * Renders the {@code footer} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement footer(String text) {
    return element(HtmlElementName.FOOTER, text);
  }

  /**
   * Renders the {@code form} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return element(HtmlElementName.FORM, contents);
  }

  /**
   * Renders the {@code form} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfElement form(String text) {
    ambiguous(HtmlAmbiguous.FORM, text);
    return Html.ELEMENT;
  }

  /**
   * Renders the {@code g} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return element(HtmlElementName.G, contents);
  }

  /**
   * Renders the {@code g} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement g(String text) {
    return element(HtmlElementName.G, text);
  }

  /**
   * Renders the {@code h1} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return element(HtmlElementName.H1, contents);
  }

  /**
   * Renders the {@code h1} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h1(String text) {
    return element(HtmlElementName.H1, text);
  }

  /**
   * Renders the {@code h2} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return element(HtmlElementName.H2, contents);
  }

  /**
   * Renders the {@code h2} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h2(String text) {
    return element(HtmlElementName.H2, text);
  }

  /**
   * Renders the {@code h3} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return element(HtmlElementName.H3, contents);
  }

  /**
   * Renders the {@code h3} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h3(String text) {
    return element(HtmlElementName.H3, text);
  }

  /**
   * Renders the {@code h4} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return element(HtmlElementName.H4, contents);
  }

  /**
   * Renders the {@code h4} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h4(String text) {
    return element(HtmlElementName.H4, text);
  }

  /**
   * Renders the {@code h5} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return element(HtmlElementName.H5, contents);
  }

  /**
   * Renders the {@code h5} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h5(String text) {
    return element(HtmlElementName.H5, text);
  }

  /**
   * Renders the {@code h6} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return element(HtmlElementName.H6, contents);
  }

  /**
   * Renders the {@code h6} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h6(String text) {
    return element(HtmlElementName.H6, text);
  }

  /**
   * Renders the {@code head} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return element(HtmlElementName.HEAD, contents);
  }

  /**
   * Renders the {@code head} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement head(String text) {
    return element(HtmlElementName.HEAD, text);
  }

  /**
   * Renders the {@code header} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return element(HtmlElementName.HEADER, contents);
  }

  /**
   * Renders the {@code header} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement header(String text) {
    return element(HtmlElementName.HEADER, text);
  }

  /**
   * Renders the {@code hgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return element(HtmlElementName.HGROUP, contents);
  }

  /**
   * Renders the {@code hgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement hgroup(String text) {
    return element(HtmlElementName.HGROUP, text);
  }

  /**
   * Renders the {@code hr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.HR, contents);
  }

  /**
   * Renders the {@code html} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return element(HtmlElementName.HTML, contents);
  }

  /**
   * Renders the {@code html} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement html(String text) {
    return element(HtmlElementName.HTML, text);
  }

  /**
   * Renders the {@code img} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.IMG, contents);
  }

  /**
   * Renders the {@code input} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.INPUT, contents);
  }

  /**
   * Renders the {@code kbd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return element(HtmlElementName.KBD, contents);
  }

  /**
   * Renders the {@code kbd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement kbd(String text) {
    return element(HtmlElementName.KBD, text);
  }

  /**
   * Renders the {@code label} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return element(HtmlElementName.LABEL, contents);
  }

  /**
   * Renders the {@code label} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfElement label(String text) {
    ambiguous(HtmlAmbiguous.LABEL, text);
    return Html.ELEMENT;
  }

  /**
   * Renders the {@code legend} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return element(HtmlElementName.LEGEND, contents);
  }

  /**
   * Renders the {@code legend} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement legend(String text) {
    return element(HtmlElementName.LEGEND, text);
  }

  /**
   * Renders the {@code li} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return element(HtmlElementName.LI, contents);
  }

  /**
   * Renders the {@code li} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement li(String text) {
    return element(HtmlElementName.LI, text);
  }

  /**
   * Renders the {@code link} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.LINK, contents);
  }

  /**
   * Renders the {@code main} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return element(HtmlElementName.MAIN, contents);
  }

  /**
   * Renders the {@code main} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement main(String text) {
    return element(HtmlElementName.MAIN, text);
  }

  /**
   * Renders the {@code menu} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return element(HtmlElementName.MENU, contents);
  }

  /**
   * Renders the {@code menu} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement menu(String text) {
    return element(HtmlElementName.MENU, text);
  }

  /**
   * Renders the {@code meta} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.META, contents);
  }

  /**
   * Renders the {@code nav} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return element(HtmlElementName.NAV, contents);
  }

  /**
   * Renders the {@code nav} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement nav(String text) {
    return element(HtmlElementName.NAV, text);
  }

  /**
   * Renders the {@code ol} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return element(HtmlElementName.OL, contents);
  }

  /**
   * Renders the {@code ol} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ol(String text) {
    return element(HtmlElementName.OL, text);
  }

  /**
   * Renders the {@code optgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return element(HtmlElementName.OPTGROUP, contents);
  }

  /**
   * Renders the {@code optgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement optgroup(String text) {
    return element(HtmlElementName.OPTGROUP, text);
  }

  /**
   * Renders the {@code option} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return element(HtmlElementName.OPTION, contents);
  }

  /**
   * Renders the {@code option} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement option(String text) {
    return element(HtmlElementName.OPTION, text);
  }

  /**
   * Renders the {@code p} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return element(HtmlElementName.P, contents);
  }

  /**
   * Renders the {@code p} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement p(String text) {
    return element(HtmlElementName.P, text);
  }

  /**
   * Renders the {@code path} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return element(HtmlElementName.PATH, contents);
  }

  /**
   * Renders the {@code path} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement path(String text) {
    return element(HtmlElementName.PATH, text);
  }

  /**
   * Renders the {@code pre} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return element(HtmlElementName.PRE, contents);
  }

  /**
   * Renders the {@code pre} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement pre(String text) {
    return element(HtmlElementName.PRE, text);
  }

  /**
   * Renders the {@code progress} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return element(HtmlElementName.PROGRESS, contents);
  }

  /**
   * Renders the {@code progress} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement progress(String text) {
    return element(HtmlElementName.PROGRESS, text);
  }

  /**
   * Renders the {@code samp} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return element(HtmlElementName.SAMP, contents);
  }

  /**
   * Renders the {@code samp} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement samp(String text) {
    return element(HtmlElementName.SAMP, text);
  }

  /**
   * Renders the {@code script} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return element(HtmlElementName.SCRIPT, contents);
  }

  /**
   * Renders the {@code script} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement script(String text) {
    return element(HtmlElementName.SCRIPT, text);
  }

  /**
   * Renders the {@code section} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return element(HtmlElementName.SECTION, contents);
  }

  /**
   * Renders the {@code section} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement section(String text) {
    return element(HtmlElementName.SECTION, text);
  }

  /**
   * Renders the {@code select} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return element(HtmlElementName.SELECT, contents);
  }

  /**
   * Renders the {@code select} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement select(String text) {
    return element(HtmlElementName.SELECT, text);
  }

  /**
   * Renders the {@code small} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return element(HtmlElementName.SMALL, contents);
  }

  /**
   * Renders the {@code small} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement small(String text) {
    return element(HtmlElementName.SMALL, text);
  }

  /**
   * Renders the {@code span} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return element(HtmlElementName.SPAN, contents);
  }

  /**
   * Renders the {@code span} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement span(String text) {
    return element(HtmlElementName.SPAN, text);
  }

  /**
   * Renders the {@code strong} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return element(HtmlElementName.STRONG, contents);
  }

  /**
   * Renders the {@code strong} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement strong(String text) {
    return element(HtmlElementName.STRONG, text);
  }

  /**
   * Renders the {@code style} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return element(HtmlElementName.STYLE, contents);
  }

  /**
   * Renders the {@code style} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement style(String text) {
    return element(HtmlElementName.STYLE, text);
  }

  /**
   * Renders the {@code sub} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return element(HtmlElementName.SUB, contents);
  }

  /**
   * Renders the {@code sub} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sub(String text) {
    return element(HtmlElementName.SUB, text);
  }

  /**
   * Renders the {@code summary} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return element(HtmlElementName.SUMMARY, contents);
  }

  /**
   * Renders the {@code summary} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement summary(String text) {
    return element(HtmlElementName.SUMMARY, text);
  }

  /**
   * Renders the {@code sup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return element(HtmlElementName.SUP, contents);
  }

  /**
   * Renders the {@code sup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sup(String text) {
    return element(HtmlElementName.SUP, text);
  }

  /**
   * Renders the {@code svg} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return element(HtmlElementName.SVG, contents);
  }

  /**
   * Renders the {@code svg} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement svg(String text) {
    return element(HtmlElementName.SVG, text);
  }

  /**
   * Renders the {@code table} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return element(HtmlElementName.TABLE, contents);
  }

  /**
   * Renders the {@code table} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement table(String text) {
    return element(HtmlElementName.TABLE, text);
  }

  /**
   * Renders the {@code tbody} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return element(HtmlElementName.TBODY, contents);
  }

  /**
   * Renders the {@code tbody} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tbody(String text) {
    return element(HtmlElementName.TBODY, text);
  }

  /**
   * Renders the {@code td} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return element(HtmlElementName.TD, contents);
  }

  /**
   * Renders the {@code td} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement td(String text) {
    return element(HtmlElementName.TD, text);
  }

  /**
   * Renders the {@code template} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return element(HtmlElementName.TEMPLATE, contents);
  }

  /**
   * Renders the {@code template} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement template(String text) {
    return element(HtmlElementName.TEMPLATE, text);
  }

  /**
   * Renders the {@code textarea} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return element(HtmlElementName.TEXTAREA, contents);
  }

  /**
   * Renders the {@code textarea} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement textarea(String text) {
    return element(HtmlElementName.TEXTAREA, text);
  }

  /**
   * Renders the {@code th} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return element(HtmlElementName.TH, contents);
  }

  /**
   * Renders the {@code th} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement th(String text) {
    return element(HtmlElementName.TH, text);
  }

  /**
   * Renders the {@code thead} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return element(HtmlElementName.THEAD, contents);
  }

  /**
   * Renders the {@code thead} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement thead(String text) {
    return element(HtmlElementName.THEAD, text);
  }

  /**
   * Renders the {@code title} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return element(HtmlElementName.TITLE, contents);
  }

  /**
   * Renders the {@code title} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfElement title(String text) {
    ambiguous(HtmlAmbiguous.TITLE, text);
    return Html.ELEMENT;
  }

  /**
   * Renders the {@code tr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return element(HtmlElementName.TR, contents);
  }

  /**
   * Renders the {@code tr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tr(String text) {
    return element(HtmlElementName.TR, text);
  }

  /**
   * Renders the {@code ul} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return element(HtmlElementName.UL, contents);
  }

  /**
   * Renders the {@code ul} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ul(String text) {
    return element(HtmlElementName.UL, text);
  }

  //
  // Section: attribute methods
  //

  /**
   * Renders the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute accesskey(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACCESSKEY, value);
  }

  /**
   * Renders the {@code action} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute action(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACTION, value);
  }

  /**
   * Renders the {@code align} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute align(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGN, value);
  }

  /**
   * Renders the {@code alignment-baseline} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute alignmentBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGNMENT_BASELINE, value);
  }

  /**
   * Renders the {@code alt} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute alt(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALT, value);
  }

  /**
   * Renders the {@code aria-hidden} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute ariaHidden(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_HIDDEN, value);
  }

  /**
   * Renders the {@code aria-label} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute ariaLabel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_LABEL, value);
  }

  /**
   * Renders the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute async() {
    return attribute0(HtmlAttributeName.ASYNC);
  }

  /**
   * Renders the {@code autocomplete} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute autocomplete(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.AUTOCOMPLETE, value);
  }

  /**
   * Renders the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute autofocus() {
    return attribute0(HtmlAttributeName.AUTOFOCUS);
  }

  /**
   * Renders the {@code baseline-shift} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute baselineShift(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BASELINE_SHIFT, value);
  }

  /**
   * Renders the {@code border} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute border(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BORDER, value);
  }

  /**
   * Renders the {@code cellpadding} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute cellpadding(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLPADDING, value);
  }

  /**
   * Renders the {@code cellspacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute cellspacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLSPACING, value);
  }

  /**
   * Renders the {@code charset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute charset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CHARSET, value);
  }

  /**
   * Renders the {@code cite} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute cite(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CITE, value);
  }

  /**
   * Renders the {@code class} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute className(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLASS, value);
  }

  /**
   * Renders the {@code clip-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute clipRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLIP_RULE, value);
  }

  /**
   * Renders the {@code color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute color(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR, value);
  }

  /**
   * Renders the {@code color-interpolation} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute colorInterpolation(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION, value);
  }

  /**
   * Renders the {@code color-interpolation-filters} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  /**
   * Renders the {@code cols} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute cols(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLS, value);
  }

  /**
   * Renders the {@code content} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute content(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENT, value);
  }

  /**
   * Renders the {@code contenteditable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute contenteditable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENTEDITABLE, value);
  }

  /**
   * Renders the {@code crossorigin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute crossorigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CROSSORIGIN, value);
  }

  /**
   * Renders the {@code cursor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute cursor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CURSOR, value);
  }

  /**
   * Renders the {@code d} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute d(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.D, value);
  }

  /**
   * Renders the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute defer() {
    return attribute0(HtmlAttributeName.DEFER);
  }

  /**
   * Renders the {@code dir} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute dir(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIR, value);
  }

  /**
   * Renders the {@code direction} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute direction(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRECTION, value);
  }

  /**
   * Renders the {@code dirname} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute dirname(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRNAME, value);
  }

  /**
   * Renders the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute disabled() {
    return attribute0(HtmlAttributeName.DISABLED);
  }

  /**
   * Renders the {@code display} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute display(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DISPLAY, value);
  }

  /**
   * Renders the {@code dominant-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute dominantBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DOMINANT_BASELINE, value);
  }

  /**
   * Renders the {@code draggable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute draggable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DRAGGABLE, value);
  }

  /**
   * Renders the {@code enctype} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute enctype(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ENCTYPE, value);
  }

  /**
   * Renders the {@code fill} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute fill(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL, value);
  }

  /**
   * Renders the {@code fill-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute fillOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_OPACITY, value);
  }

  /**
   * Renders the {@code fill-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute fillRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_RULE, value);
  }

  /**
   * Renders the {@code filter} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute filter(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILTER, value);
  }

  /**
   * Renders the {@code flood-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute floodColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_COLOR, value);
  }

  /**
   * Renders the {@code flood-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute floodOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_OPACITY, value);
  }

  /**
   * Renders the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute forAttr(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Renders the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute forElement(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Renders the {@code glyph-orientation-horizontal} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  /**
   * Renders the {@code glyph-orientation-vertical} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /**
   * Renders the {@code height} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute height(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HEIGHT, value);
  }

  /**
   * Renders the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute hidden() {
    return attribute0(HtmlAttributeName.HIDDEN);
  }

  /**
   * Renders the {@code href} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute href(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HREF, value);
  }

  /**
   * Renders the {@code http-equiv} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute httpEquiv(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HTTP_EQUIV, value);
  }

  /**
   * Renders the {@code id} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute id(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ID, value);
  }

  /**
   * Renders the {@code image-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute imageRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.IMAGE_RENDERING, value);
  }

  /**
   * Renders the {@code integrity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute integrity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.INTEGRITY, value);
  }

  /**
   * Renders the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute lang(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LANG, value);
  }

  /**
   * Renders the {@code letter-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute letterSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LETTER_SPACING, value);
  }

  /**
   * Renders the {@code lighting-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute lightingColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LIGHTING_COLOR, value);
  }

  /**
   * Renders the {@code marker-end} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute markerEnd(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_END, value);
  }

  /**
   * Renders the {@code marker-mid} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute markerMid(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_MID, value);
  }

  /**
   * Renders the {@code marker-start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute markerStart(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_START, value);
  }

  /**
   * Renders the {@code mask} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute mask(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK, value);
  }

  /**
   * Renders the {@code mask-type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute maskType(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK_TYPE, value);
  }

  /**
   * Renders the {@code maxlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute maxlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MAXLENGTH, value);
  }

  /**
   * Renders the {@code media} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute media(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MEDIA, value);
  }

  /**
   * Renders the {@code method} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute method(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.METHOD, value);
  }

  /**
   * Renders the {@code minlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute minlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MINLENGTH, value);
  }

  /**
   * Renders the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute multiple() {
    return attribute0(HtmlAttributeName.MULTIPLE);
  }

  /**
   * Renders the {@code name} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute name(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.NAME, value);
  }

  /**
   * Renders the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute nomodule() {
    return attribute0(HtmlAttributeName.NOMODULE);
  }

  /**
   * Renders the {@code onafterprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onafterprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONAFTERPRINT, value);
  }

  /**
   * Renders the {@code onbeforeprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onbeforeprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREPRINT, value);
  }

  /**
   * Renders the {@code onbeforeunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onbeforeunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREUNLOAD, value);
  }

  /**
   * Renders the {@code onclick} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onclick(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONCLICK, value);
  }

  /**
   * Renders the {@code onhashchange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onhashchange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONHASHCHANGE, value);
  }

  /**
   * Renders the {@code onlanguagechange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onlanguagechange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONLANGUAGECHANGE, value);
  }

  /**
   * Renders the {@code onmessage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onmessage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONMESSAGE, value);
  }

  /**
   * Renders the {@code onoffline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onoffline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONOFFLINE, value);
  }

  /**
   * Renders the {@code ononline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute ononline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONONLINE, value);
  }

  /**
   * Renders the {@code onpagehide} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onpagehide(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGEHIDE, value);
  }

  /**
   * Renders the {@code onpageshow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onpageshow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGESHOW, value);
  }

  /**
   * Renders the {@code onpopstate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onpopstate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPOPSTATE, value);
  }

  /**
   * Renders the {@code onrejectionhandled} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onrejectionhandled(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONREJECTIONHANDLED, value);
  }

  /**
   * Renders the {@code onstorage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onstorage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSTORAGE, value);
  }

  /**
   * Renders the {@code onsubmit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onsubmit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSUBMIT, value);
  }

  /**
   * Renders the {@code onunhandledrejection} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onunhandledrejection(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNHANDLEDREJECTION, value);
  }

  /**
   * Renders the {@code onunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute onunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNLOAD, value);
  }

  /**
   * Renders the {@code opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute opacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OPACITY, value);
  }

  /**
   * Renders the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute open() {
    return attribute0(HtmlAttributeName.OPEN);
  }

  /**
   * Renders the {@code overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute overflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OVERFLOW, value);
  }

  /**
   * Renders the {@code paint-order} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute paintOrder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PAINT_ORDER, value);
  }

  /**
   * Renders the {@code placeholder} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute placeholder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PLACEHOLDER, value);
  }

  /**
   * Renders the {@code pointer-events} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute pointerEvents(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.POINTER_EVENTS, value);
  }

  /**
   * Renders the {@code property} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute property(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PROPERTY, value);
  }

  /**
   * Renders the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute readonly() {
    return attribute0(HtmlAttributeName.READONLY);
  }

  /**
   * Renders the {@code referrerpolicy} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute referrerpolicy(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REFERRERPOLICY, value);
  }

  /**
   * Renders the {@code rel} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute rel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REL, value);
  }

  /**
   * Renders the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute required() {
    return attribute0(HtmlAttributeName.REQUIRED);
  }

  /**
   * Renders the {@code rev} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute rev(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REV, value);
  }

  /**
   * Renders the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute reversed() {
    return attribute0(HtmlAttributeName.REVERSED);
  }

  /**
   * Renders the {@code role} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute role(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROLE, value);
  }

  /**
   * Renders the {@code rows} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute rows(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROWS, value);
  }

  /**
   * Renders the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute selected() {
    return attribute0(HtmlAttributeName.SELECTED);
  }

  /**
   * Renders the {@code shape-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute shapeRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SHAPE_RENDERING, value);
  }

  /**
   * Renders the {@code size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute size(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZE, value);
  }

  /**
   * Renders the {@code sizes} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute sizes(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZES, value);
  }

  /**
   * Renders the {@code spellcheck} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute spellcheck(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SPELLCHECK, value);
  }

  /**
   * Renders the {@code src} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute src(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRC, value);
  }

  /**
   * Renders the {@code srcset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute srcset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRCSET, value);
  }

  /**
   * Renders the {@code start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute start(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.START, value);
  }

  /**
   * Renders the {@code stop-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute stopColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_COLOR, value);
  }

  /**
   * Renders the {@code stop-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute stopOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_OPACITY, value);
  }

  /**
   * Renders the {@code stroke} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute stroke(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE, value);
  }

  /**
   * Renders the {@code stroke-dasharray} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeDasharray(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHARRAY, value);
  }

  /**
   * Renders the {@code stroke-dashoffset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeDashoffset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHOFFSET, value);
  }

  /**
   * Renders the {@code stroke-linecap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeLinecap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINECAP, value);
  }

  /**
   * Renders the {@code stroke-linejoin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeLinejoin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINEJOIN, value);
  }

  /**
   * Renders the {@code stroke-miterlimit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_MITERLIMIT, value);
  }

  /**
   * Renders the {@code stroke-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_OPACITY, value);
  }

  /**
   * Renders the {@code stroke-width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute strokeWidth(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_WIDTH, value);
  }

  /**
   * Renders the {@code style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute inlineStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STYLE, value);
  }

  /**
   * Renders the {@code tabindex} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute tabindex(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TABINDEX, value);
  }

  /**
   * Renders the {@code target} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute target(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TARGET, value);
  }

  /**
   * Renders the {@code text-anchor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute textAnchor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_ANCHOR, value);
  }

  /**
   * Renders the {@code text-decoration} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute textDecoration(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_DECORATION, value);
  }

  /**
   * Renders the {@code text-overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute textOverflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_OVERFLOW, value);
  }

  /**
   * Renders the {@code text-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute textRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_RENDERING, value);
  }

  /**
   * Renders the {@code transform} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute transform(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM, value);
  }

  /**
   * Renders the {@code transform-origin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute transformOrigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM_ORIGIN, value);
  }

  /**
   * Renders the {@code translate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute translate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSLATE, value);
  }

  /**
   * Renders the {@code type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute type(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TYPE, value);
  }

  /**
   * Renders the {@code unicode-bidi} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute unicodeBidi(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.UNICODE_BIDI, value);
  }

  /**
   * Renders the {@code value} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute value(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VALUE, value);
  }

  /**
   * Renders the {@code vector-effect} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute vectorEffect(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VECTOR_EFFECT, value);
  }

  /**
   * Renders the {@code viewBox} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute viewBox(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VIEWBOX, value);
  }

  /**
   * Renders the {@code visibility} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute visibility(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VISIBILITY, value);
  }

  /**
   * Renders the {@code white-space} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute whiteSpace(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WHITE_SPACE, value);
  }

  /**
   * Renders the {@code width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute width(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WIDTH, value);
  }

  /**
   * Renders the {@code word-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute wordSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WORD_SPACING, value);
  }

  /**
   * Renders the {@code wrap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute wrap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRAP, value);
  }

  /**
   * Renders the {@code writing-mode} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute writingMode(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRITING_MODE, value);
  }

  /**
   * Renders the {@code xmlns} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  @Override
  public final Html.Instruction.OfAttribute xmlns(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.XMLNS, value);
  }

}