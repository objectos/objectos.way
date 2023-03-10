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
import objectos.html.HtmlFragment;
import objectos.html.HtmlTemplate;
import objectos.html.Lambda;
import objectos.html.Template;
import objectos.html.TemplateDsl;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementKind;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.Value;
import objectos.lang.Check;
import objectos.util.CharArrays;
import objectos.util.IntArrays;

public final class TemplateDslImpl implements TemplateDsl {

  private final int[][] attr;
  private final int[] attrLength;

  private char[] buffer;
  private int bufferLength;

  private final int elemSize;

  private int lambdaIndex;

  private int level;

  private final int[] markedAttr;
  private final int[] markedObject;

  private final int[][] object;
  private final int[] objectLength;

  private int[] proto;
  private int protoLength;

  public TemplateDslImpl() {
    buffer = new char[8 * 1024];
    proto = new int[1024];

    int levelSize = 16;
    // int levelSize = 4;
    elemSize = 1024;
    // elemSize = 32;

    attr = new int[levelSize][];
    attrLength = new int[levelSize];

    object = new int[levelSize][];
    objectLength = new int[levelSize];

    markedAttr = new int[levelSize];
    markedObject = new int[levelSize];

    level = 0;
    initLevel();
  }

  @Override
  public final void addTemplate(HtmlTemplate template) {
    template.acceptTemplateDsl(this);
  }

  @Override
  public final void addAttribute(AttributeName name) {
    Check.notNull(name, "name == null");

    addAttribute0(name);
  }

  @Override
  public final void addAttribute(AttributeName name, String value) {
    Check.notNull(name, "name == null");

    addAttribute0(name, value);
  }

  public final void addAttribute(String name) {
    Check.notNull(name, "name == null");
    StandardAttributeName maybeStd = StandardAttributeName.getByName(name);
    if (maybeStd != null) {
      addAttribute0(maybeStd);
    } else {
      uncheckedAddAttribute(name);
    }
  }

  @Override
  public final void addAttribute(String name, String value) {
    Check.notNull(name, "name == null");
    StandardAttributeName maybeStd = StandardAttributeName.getByName(name);
    if (maybeStd != null) {
      addAttribute0(maybeStd, value);
    } else {
      uncheckedAddAttribute(name, value);
    }
  }

  @Override
  public final void addAttributeOrElement(AttributeOrElement value, String text) {
    addProto(text.length());
    addProto(bufferLength);
    addProto(ByteProto.ATTR_OR_ELEMENT);

    addString(text);

    addAttr(ByteProto.MARK_MAYBE_ATTR);
    addAttr(value.code());

    addObject(ByteProto.MARK_MAYBE_ELEMENT);
    addObject(value.code());
  }

  @Override
  public final void addDoctype() {
    uncheckedAddAttribute("html");
    markAttribute();
    addSelfClosingTag("!doctype");
  }

  public final void addElement(ElementKind kind, String name, Value[] values) {
    if (!kind.isVoid()) {
      renderAll(values);
      addProto(ByteProto.END_TAG);
      addValues(values);
      addStartTag(name);
    } else {
      renderAll(values);
      addValues(values);
      addSelfClosingTag(name);
    }
  }

  @Override
  public final void addElement(ElementName name, String text) {
    Check.notNull(name, "name == null");
    Check.notNull(text, "text == null");

    addProto(ByteProto.END_ELEMENT);

    addProto(text.length());
    addProto(bufferLength);
    addProto(ByteProto.TEXT);
    addString(text);

    addProto(ByteProto.GT);
    addProto(name.getCode());
    addProto(ByteProto.START_ELEMENT);

    addObject(ByteProto.MARK_ELEMENT);
  }

  @Override
  public final void addElement(ElementName name, Value... values) {
    Check.notNull(name, "name == null");
    Check.notNull(values, "values == null");

    renderAll(values);

    ElementKind kind = name.getKind();
    if (!kind.isVoid()) {
      addProto(ByteProto.END_ELEMENT);
      addValues(values);
      addStartTag(name);
    } else {
      addValues(values);
      addSelfClosingTag(name);
    }
  }

  @Override
  public final void addFragment(HtmlFragment fragment) {
    Check.notNull(fragment, "fragment == null");

    int returnLevel;
    returnLevel = level;

    level++;

    lambdaIndex++;

    if (level != lambdaIndex) {
      level = lambdaIndex;
    }

    initLevel();

    fragment.acceptTemplateDsl(this);

    level = returnLevel;
  }

  @Override
  public final void addLambda(Lambda lambda) {
    Check.notNull(lambda, "lambda == null");

    int returnLevel;
    returnLevel = level;

    level++;

    lambdaIndex++;

    if (level != lambdaIndex) {
      level = lambdaIndex;
    }

    initLevel();

    lambda.apply();

    level = returnLevel;
  }

  @Override
  public final void addRaw(String text) {
    addProto(text.length());
    addProto(bufferLength);
    addProto(ByteProto.RAW_ELEMENT);

    addString(text);

    addObject(ByteProto.MARK_RAW_ELEMENT);
  }

  public final void addTemplate(Template template) {
    Check.notNull(template, "template == null");
    template.acceptTemplateDsl(this);
  }

  @Override
  public final void addText(String text) {
    addProto(text.length());
    addProto(bufferLength);
    addProto(ByteProto.TEXT_ELEMENT);

    addString(text);

    addObject(ByteProto.MARK_TEXT_ELEMENT);
  }

  public final CompiledTemplate compile() {
    Compiler compiler = new Compiler(this);
    return compiler.compile(protoLength);
  }

  @Override
  public final void markAttribute() {
    markedAttr[level]++;
  }

  @Override
  public final void markAttributeOrElement() {
    markAttribute();
    markAttribute();

    markElement();
    markElement();
  }

  @Override
  public final void markElement() {
    markedObject[level]++;
  }

  @Override
  public final void markLambda() {
    int returnLevel;
    returnLevel = level;

    level = lambdaIndex;

    doAll();

    level = returnLevel;

    lambdaIndex--;
  }

  @Override
  public final void markRaw() {
    markedObject[level]++;
  }

  @Override
  public final void markRootElement() {
    addProto(ByteProto.ROOT_END);

    doAll();

    addProto(ByteProto.ROOT_START);
  }

  @Override
  public final void markTemplate() {
    addProto(ByteProto.MARK_TEMPLATE);
  }

  @Override
  public final void markText() {
    markedObject[level]++;
  }

  final String bufferToString() {
    return new String(buffer, 0, bufferLength);
  }

  final char[] getBuffer() {
    return Arrays.copyOf(buffer, bufferLength);
  }

  final int getProto(int index) {
    return proto[index];
  }

  final int[] protos() {
    return Arrays.copyOf(proto, protoLength);
  }

  final void uncheckedAddAttribute(String name) {
    addProto(name.length());
    addProto(bufferLength);
    addProto(ByteProto.BOOLEAN_ATTR);
    addString(name);

    addAttr(ByteProto.MARK_BOOLEAN_ATTR);
  }

  final void uncheckedAddAttribute(String name, String value) {
    Check.notNull(value, "value == null");

    addProto(value.length());
    addProto(name.length());
    addProto(bufferLength);
    addProto(ByteProto.STRING_ATTR);

    // yes, invert...
    addString(name);
    addString(value);

    addAttr(ByteProto.MARK_STRING_ATTR);
  }

  private void addAttr(int code) {
    attr[level][attrLength[level]++] = code;
  }

  private void addAttribute0(AttributeName name) {
    addProto(name.getCode());
    addProto(ByteProto.BOOLEAN_STD);
    addAttr(ByteProto.MARK_BOOLEAN_STD);
  }

  private void addAttribute0(AttributeName name, String value) {
    Check.notNull(value, "value == null");

    addProto(value.length());
    addProto(bufferLength);
    addProto(name.getCode());
    addProto(ByteProto.STRING_STD);

    addString(value);

    addAttr(ByteProto.MARK_STRING_STD);
  }

  private void addObject(int code) {
    object[level][objectLength[level]++] = code;
  }

  private void addProto(int code) {
    proto = IntArrays.growIfNecessary(proto, protoLength);

    proto[protoLength++] = code;
  }

  private void addSelfClosingTag(ElementName element) {
    addProto(ByteProto.SELF_CLOSING_TAG);
    addStartTagCommon(element);
  }

  private void addSelfClosingTag(String name) {
    addProto(ByteProto.SELF_CLOSING_TAG);
    addStartTagCommon(name);
  }

  private void addStartTag(ElementName element) {
    doObject();
    addProto(ByteProto.GT);
    addStartTagCommon(element);
  }

  private void addStartTag(String name) {
    doObject();
    addProto(ByteProto.GT);
    addStartTagCommon(name);
  }

  private void addStartTagCommon(ElementName element) {
    doAttr();

    addProto(element.getCode());
    addProto(ByteProto.START_ELEMENT);

    addObject(ByteProto.MARK_ELEMENT);
  }

  private void addStartTagCommon(String name) {
    doAttr();

    addProto(name.length());
    addProto(bufferLength);
    addProto(ByteProto.START_TAG);
    addString(name);

    addObject(ByteProto.MARK_TAG);
  }

  private void addString(String value) {
    buffer = CharArrays.append(buffer, bufferLength, value);

    bufferLength += value.length();
  }

  private void addValues(Value[] values) {
    markedAttr[level] = 0;
    markedObject[level] = 0;

    for (Value value : values) {
      value.mark(this);
    }
  }

  private void doAll() {
    markedAttr[level] = attrLength[level];
    doAttr();

    markedObject[level] = objectLength[level];
    doObject();
  }

  private void doAttr() {
    int count = markedAttr[level];
    int lengt = attrLength[level];
    int start = lengt - count;
    for (int i = lengt - 1; i >= start; i--) {
      addProto(attr[level][i]);
    }
    attrLength[level] = start;
  }

  private void doObject() {
    int count = markedObject[level];
    int lengt = objectLength[level];
    int start = lengt - count;
    for (int i = lengt - 1; i >= start; i--) {
      addProto(object[level][i]);
    }
    objectLength[level] = start;
  }

  private void initLevel() {
    if (attr[level] == null) {
      attr[level] = new int[elemSize];
      object[level] = new int[elemSize];
    }
  }

  private void renderAll(Value[] values) {
    for (Value value : values) {
      value.render(this);
    }
  }

}