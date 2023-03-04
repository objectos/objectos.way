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
package br.com.objectos.html.tmpl;

import br.com.objectos.html.attribute.StandardAttributeName;
import br.com.objectos.html.element.StandardElementName;
import objectos.util.IntArrays;
import objectos.util.GrowableList;

class Interpreter {

  private static final int CURRENT_ATTR_INC = 10;

  private int[][] attr = new int[10][];

  private int attrIndex = 0;
  private int[] call = new int[64];

  private int callIndex = -1;
  private int currentAttr;

  private int currentAttrIndex;
  private int cursor;
  private boolean running;

  private final CompiledTemplate template;
  private final CompiledTemplateVisitor visitor;

  Interpreter(CompiledTemplate template, CompiledTemplateVisitor visitor) {
    this.template = template;
    this.visitor = visitor;
  }

  public final void execute() {
    running = true;
    while (running) {
      execute0();
    }
  }

  private void addAttr(int code) {
    currentAttr = attrIndex;

    attr = IntArrays.growIfNecessary(attr, currentAttr);

    if (attr[currentAttr] == null) {
      attr[currentAttr] = new int[CURRENT_ATTR_INC];
    }

    attrIndex++;

    currentAttrIndex = 0;

    addCurrentAttr(code);
  }

  private void addCurrentAttr(int code) {
    attr[currentAttr] = IntArrays.growIfNecessary(attr[currentAttr], currentAttrIndex);

    attr[currentAttr][currentAttrIndex++] = code;
  }

  private void doAttrs() {
    for (int i = 0; i < attrIndex; i++) {
      currentAttr = i;
      currentAttrIndex = 0;
      doAttrs0();
    }
    attrIndex = 0;
  }

  private void doAttrs0() {
    int code = getCurrentAttr();
    switch (code) {
      case ByteCode.BOOLEAN_ATTR:
        visitor.visitAttribute(
            getBuffer(getCurrentAttr(), getCurrentAttr())
        );
        break;
      case ByteCode.BOOLEAN_STD:
        visitor.visitAttribute(
            StandardAttributeName.getByCode(
                getCurrentAttr()
            )
        );
        break;
      case ByteCode.STRING_ATTR:
        int index = getCurrentAttr();
        int nLength = getCurrentAttr();
        int vLength = getCurrentAttr();
        visitor.visitAttribute(
            getBuffer(index, nLength),
            getBuffer(index + nLength, vLength)
        );
        break;
      case ByteCode.STRING_STD:
        doAttrs0StringStd(
            StandardAttributeName.getByCode(getCurrentAttr()),
            getCurrentAttr()
        );
        break;
      default:
        throw new AssertionError("Unexpected code: " + code);
    }
  }

  private void doAttrs0StringStd(StandardAttributeName name, int valueCount) {
    switch (valueCount) {
      case 0:
        throw new AssertionError("Unexpected valueCount = 0");
      case 1:
        visitor.visitAttribute(
            name,
            getBuffer(getCurrentAttr(), getCurrentAttr())
        );
        break;
      default:
        GrowableList<String> values = new GrowableList<>();
        for (int i = 0; i < valueCount; i++) {
          values.add(
              getBuffer(getCurrentAttr(), getCurrentAttr())
          );
        }
        visitor.visitAttribute(
            name,
            values.toUnmodifiableList()
        );
        break;
    }
  }

  private void doBooleanAttr(int byteCode, int index, int length) {
    addAttr(byteCode);
    addCurrentAttr(index);
    addCurrentAttr(length);
  }

  private void doBooleanStd(int byteCode, int stdCode) {
    addAttr(byteCode);
    addCurrentAttr(stdCode);
  }

  private void doStringAttr(int byteCode, int index, int nLength, int vLength) {
    addAttr(byteCode);
    addCurrentAttr(index);
    addCurrentAttr(nLength);
    addCurrentAttr(vLength);
  }

  private void doStringStd(int byteCode, int stdCode, int index, int length) {
    int valueCount = findOrAddAttr(byteCode, stdCode);
    currentAttrIndex = 0;

    addCurrentAttr(byteCode);
    addCurrentAttr(stdCode);
    addCurrentAttr(valueCount + 1);

    currentAttrIndex += valueCount * 2;

    addCurrentAttr(index);
    addCurrentAttr(length);
  }

  private void execute0() {
    int code = getCode();
    switch (code) {
      case ByteCode.BOOLEAN_ATTR:
        doBooleanAttr(code, getCode(), getCode());
        break;
      case ByteCode.BOOLEAN_STD:
        doBooleanStd(code, getCode());
        break;
      case ByteCode.END_ELEMENT:
        visitor.visitEndTag(getStandardElement());
        break;
      case ByteCode.END_TAG:
        visitor.visitEndTag(getBuffer());
        break;
      case ByteCode.GT:
        doAttrs();
        visitor.visitStartTagEnd();
        break;
      case ByteCode.JMP_ELEMENT:
        int jumpTo = getCode();
        pushCall();
        cursor = jumpTo;
        break;
      case ByteCode.RETURN:
        if (callIndex >= 0) {
          cursor = popCall();
        } else {
          running = false;
        }
        break;
      case ByteCode.ROOT:
        // noop
        break;
      case ByteCode.SELF_CLOSING_TAG:
        doAttrs();
        visitor.visitStartTagEndSelfClosing();
        break;
      case ByteCode.START_ELEMENT:
        visitor.visitStartTag(getStandardElement());
        break;
      case ByteCode.START_TAG:
        visitor.visitStartTag(getBuffer());
        break;
      case ByteCode.STRING_ATTR:
        doStringAttr(code, getCode(), getCode(), getCode());
        break;
      case ByteCode.STRING_STD:
        doStringStd(code, getCode(), getCode(), getCode());
        break;
      case ByteCode.TEXT:
        visitor.visitText(getBuffer());
        break;
      case ByteCode.RAW:
        visitor.visitRaw(getBuffer());
        break;
      default:
        throw new UnsupportedOperationException("Implement me: " + code);
    }
  }

  private int findOrAddAttr(int byteCode, int stdCode) {
    for (int i = 0; i < attrIndex; i++) {
      int[] candidate = attr[i];

      if (candidate[0] != byteCode) {
        continue;
      }

      if (candidate[1] != stdCode) {
        continue;
      }

      currentAttr = i;
      return candidate[2];
    }

    addAttr(byteCode);
    return 0;
  }

  private String getBuffer() {
    return getBuffer(getCode(), getCode());
  }

  private String getBuffer(int index, int length) {
    return template.getBuffer(index, length);
  }

  private int getCode() {
    return template.getCode(cursor++);
  }

  private int getCurrentAttr() {
    return attr[currentAttr][currentAttrIndex++];
  }

  private StandardElementName getStandardElement() {
    return StandardElementName.getByCode(getCode());
  }

  private int popCall() {
    return call[callIndex--];
  }

  private void pushCall() {
    callIndex++;

    call = IntArrays.growIfNecessary(call, callIndex);

    call[callIndex] = cursor;
  }

}