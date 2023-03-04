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
import objectos.html.AttributeOrElement;
import objectos.html.CompiledTemplate;
import objectos.html.tmpl.StandardElementName;
import objectos.util.IntArrays;

class Compiler {

  // attributes stack
  private final int[] attribute;

  private int attributeIndex = -1;
  private int[] codes;

  private int codesIndex;
  private int currentElement;

  private int currentElementIndex;
  private int currentElementLength;

  private int cursor;
  private final TemplateDslImpl dsl;

  // elements stack
  private int[] element;
  private int elementIndex = -1;

  // template stack
  private final int[] template;

  private int templateIndex = -1;
  // attribute or element stack
  private final int[] title;
  private int titleIndex = -1;

  Compiler(TemplateDslImpl dsl) {
    this.dsl = dsl;

    attribute = new int[64];
    codes = new int[1024];
    element = new int[64];
    template = new int[32];
    title = new int[8];
  }

  public final CompiledTemplate compile(int protoSize) {
    cursor = protoSize;
    return compile0();
  }

  private void addCode(int code) {
    codes = IntArrays.growIfNecessary(codes, codesIndex);

    codes[codesIndex++] = code;
  }

  private CompiledTemplate compile0() {
    while (cursor > 0) {
      int proto = getProto();
      processByteProto(proto);
    }
    return new CompiledTemplate(
      dsl.getBuffer(),
      Arrays.copyOf(codes, codesIndex)
    );
  }

  private void doAttrOrElement(int index, int length) {
    int returnTo = codesIndex;

    codesIndex = popTitle();
    int code = codes[codesIndex++];
    switch (code) {
      case ByteCode.STRING_STD:
        codesIndex++; // TITLE
        addCode(index);
        addCode(length);
        break;
      case ByteCode.START_ELEMENT:
        codesIndex++; // TITLE
        codesIndex++; // GT
        codesIndex++; // TEXT
        addCode(index);
        addCode(length);
        break;
      default:
        throw new UnsupportedOperationException("Implement me: " + code);
    }

    codesIndex = returnTo;
  }

  private void doBooleanAttribute(int index, int length) {
    int returnTo = codesIndex;

    codesIndex = popAttribute();
    addCode(index);
    addCode(length);

    codesIndex = returnTo;
  }

  private void doBooleanStd(int code) {
    int returnTo = codesIndex;

    codesIndex = popAttribute();
    addCode(code);

    codesIndex = returnTo;
  }

  private void doEndElement() {
    addCode(ByteCode.END_ELEMENT);
    addCode(currentElement);
    addCode(ByteCode.RETURN);
  }

  private void doEndTag() {
    addCode(ByteCode.END_TAG);
    addCode(currentElementIndex);
    addCode(currentElementLength);
    addCode(ByteCode.RETURN);
  }

  private void doGt() {
    addCode(ByteCode.GT);
  }

  private void doJmpElement0() {
    int returnTo = codesIndex;

    codesIndex = popElement();
    addCode(returnTo);

    codesIndex = returnTo;
  }

  private void doJmpElementIfNecessary() {
    if (elementIndex >= 0) {
      doJmpElement0();
    }
  }

  private void doMarkBooleanAttr() {
    addCode(ByteCode.BOOLEAN_ATTR);
    pushAttribute();
    addCode(0); // index slot
    addCode(0); // name length slot
  }

  private void doMarkBooleanStd() {
    addCode(ByteCode.BOOLEAN_STD);
    pushAttribute();
    addCode(0); // code slot
  }

  private void doMarkElementOrTag() {
    addCode(ByteCode.JMP_ELEMENT);
    pushElement();
    addCode(0); // jump to slot
  }

  private void doMarkMaybeAttr(AttributeOrElement value) {
    StandardElementName current = getCurrentElement();
    if (value.isAttributeOf(current)) {
      pushTitle();
      addCode(ByteCode.STRING_STD);
      addCode(value.attributeByteCode());
      addCode(0); // value index slot
      addCode(0); // value length slot;
    }
  }

  private void doMarkMaybeElement(AttributeOrElement value) {
    StandardElementName current = getCurrentElement();
    if (!value.isAttributeOf(current)) {
      int elementByteCode = value.elementByteCode();

      pushTitle();
      addCode(ByteCode.START_ELEMENT);
      addCode(elementByteCode);
      addCode(ByteCode.GT);
      addCode(ByteCode.TEXT);
      addCode(0); // index slot;
      addCode(0); // length slot;
      addCode(ByteCode.END_ELEMENT);
      addCode(elementByteCode);
    }
  }

  private void doMarkRawElement() {
    addCode(ByteCode.RAW);
    pushElement();
    addCode(0); // index slot
    addCode(0); // length slot;
  }

  private void doMarkStringAttr() {
    addCode(ByteCode.STRING_ATTR);
    pushAttribute();
    addCode(0); // index slot
    addCode(0); // name length slot
    addCode(0); // value length slot;
  }

  private void doMarkStringStd() {
    addCode(ByteCode.STRING_STD);
    pushAttribute();
    addCode(0); // code slot
    addCode(0); // value index slot
    addCode(0); // value length slot;
  }

  private void doMarkTemplate() {
    addCode(ByteCode.JMP_ELEMENT);
    pushTemplate();
    addCode(0); // jump to slot
  }

  private void doMarkTextElement() {
    addCode(ByteCode.TEXT);
    pushElement();
    addCode(0); // index slot
    addCode(0); // length slot;
  }

  private void doRawElement(int index, int length) {
    int returnTo = codesIndex;

    codesIndex = popElement();
    addCode(index);
    addCode(length);

    codesIndex = returnTo;
  }

  private void doRootEnd() {
    addCode(ByteCode.RETURN);
  }

  private void doRootStart() {
    if (templateIsEmpty()) {
      addCode(ByteCode.ROOT);
    } else {
      codes[popTemplate()] = codesIndex;
    }
  }

  private void doSelfClosingTag() {
    addCode(ByteCode.SELF_CLOSING_TAG);
    addCode(ByteCode.RETURN);
  }

  private void doStartElement(int code) {
    addCode(ByteCode.START_ELEMENT);
    addCode(code);
    currentElement = code;
  }

  private void doStartTag(int index, int length) {
    addCode(ByteCode.START_TAG);
    addCode(index);
    addCode(length);
    currentElementIndex = index;
    currentElementLength = length;
  }

  private void doStringAttr(int index, int nLength, int vLength) {
    int returnTo = codesIndex;

    codesIndex = popAttribute();
    addCode(index);
    addCode(nLength);
    addCode(vLength);

    codesIndex = returnTo;
  }

  //

  private void doStringStd(int code, int vIndex, int vLength) {
    int returnTo = codesIndex;

    codesIndex = popAttribute();
    addCode(code);
    addCode(vIndex);
    addCode(vLength);

    codesIndex = returnTo;
  }

  private void doText(int index, int length) {
    addCode(ByteCode.TEXT);
    addCode(index);
    addCode(length);
  }

  private void doTextElement(int index, int length) {
    int returnTo = codesIndex;

    codesIndex = popElement();
    addCode(index);
    addCode(length);

    codesIndex = returnTo;
  }

  private StandardElementName getCurrentElement() {
    return StandardElementName.getByCode(currentElement);
  }

  private int getProto() {
    return dsl.getProto(--cursor);
  }

  private int popAttribute() {
    return attribute[attributeIndex--];
  }

  private int popElement() {
    return element[elementIndex--];
  }

  private int popTemplate() {
    return template[templateIndex--];
  }

  private int popTitle() {
    return title[titleIndex--];
  }

  private void processByteProto(int proto) {
    switch (proto) {
      case ByteProto.ATTR_OR_ELEMENT:
        doAttrOrElement(getProto(), getProto());
        break;
      case ByteProto.BOOLEAN_ATTR:
        doBooleanAttribute(getProto(), getProto());
        break;
      case ByteProto.BOOLEAN_STD:
        doBooleanStd(getProto());
        break;
      case ByteProto.END_ELEMENT:
        doEndElement();
        break;
      case ByteProto.END_TAG:
        doEndTag();
        break;
      case ByteProto.GT:
        doGt();
        break;
      case ByteProto.MARK_BOOLEAN_ATTR:
        doMarkBooleanAttr();
        break;
      case ByteProto.MARK_BOOLEAN_STD:
        doMarkBooleanStd();
        break;
      case ByteProto.MARK_ELEMENT:
      case ByteProto.MARK_TAG:
        doMarkElementOrTag();
        break;
      case ByteProto.MARK_MAYBE_ATTR:
        doMarkMaybeAttr(AttributeOrElement.get(getProto()));
        break;
      case ByteProto.MARK_MAYBE_ELEMENT:
        doMarkMaybeElement(AttributeOrElement.get(getProto()));
        break;
      case ByteProto.MARK_STRING_ATTR:
        doMarkStringAttr();
        break;
      case ByteProto.MARK_STRING_STD:
        doMarkStringStd();
        break;
      case ByteProto.MARK_TEMPLATE:
        doMarkTemplate();
        break;
      case ByteProto.MARK_TEXT_ELEMENT:
        doMarkTextElement();
        break;
      case ByteProto.ROOT_START:
        doRootStart();
        break;
      case ByteProto.ROOT_END:
        doRootEnd();
        break;
      case ByteProto.SELF_CLOSING_TAG:
        doSelfClosingTag();
        break;
      case ByteProto.START_ELEMENT:
        doJmpElementIfNecessary();
        doStartElement(getProto());
        break;
      case ByteProto.START_TAG:
        doJmpElementIfNecessary();
        doStartTag(getProto(), getProto());
        break;
      case ByteProto.STRING_ATTR:
        doStringAttr(getProto(), getProto(), getProto());
        break;
      case ByteProto.STRING_STD:
        doStringStd(getProto(), getProto(), getProto());
        break;
      case ByteProto.TEXT:
        doText(getProto(), getProto());
        break;
      case ByteProto.TEXT_ELEMENT:
        doTextElement(getProto(), getProto());
        break;
      case ByteProto.RAW_ELEMENT:
        doRawElement(getProto(), getProto());
        break;
      case ByteProto.MARK_RAW_ELEMENT:
        doMarkRawElement();
        break;
      default:
        throw new UnsupportedOperationException("Implement me: " + proto);
    }
  }

  private void pushAttribute() {
    attribute[++attributeIndex] = codesIndex;
  }

  private void pushElement() {
    elementIndex++;

    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex] = codesIndex;
  }

  private void pushTemplate() {
    template[++templateIndex] = codesIndex;
  }

  private void pushTitle() {
    title[++titleIndex] = codesIndex;
  }

  private boolean templateIsEmpty() {
    return templateIndex < 0;
  }

}
