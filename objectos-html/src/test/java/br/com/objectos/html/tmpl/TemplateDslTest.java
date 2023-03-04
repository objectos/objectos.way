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

import static br.com.objectos.html.attribute.StandardAttributeName.CHARSET;
import static br.com.objectos.html.attribute.StandardAttributeName.CLASS;
import static br.com.objectos.html.attribute.StandardAttributeName.ID;
import static br.com.objectos.html.attribute.StandardAttributeName.LANG;
import static br.com.objectos.html.element.StandardElementName.A;
import static br.com.objectos.html.element.StandardElementName.BODY;
import static br.com.objectos.html.element.StandardElementName.DIV;
import static br.com.objectos.html.element.StandardElementName.HEAD;
import static br.com.objectos.html.element.StandardElementName.HTML;
import static br.com.objectos.html.element.StandardElementName.META;
import static br.com.objectos.html.element.StandardElementName.NAV;
import static br.com.objectos.html.element.StandardElementName.P;
import static br.com.objectos.html.element.StandardElementName.SECTION;
import static org.testng.Assert.assertEquals;

import br.com.objectos.html.element.StandardElementName;
import br.com.objectos.html.ex.TestCase00;
import br.com.objectos.html.ex.TestCase01;
import br.com.objectos.html.ex.TestCase03;
import br.com.objectos.html.ex.TestCase06;
import br.com.objectos.html.ex.TestCase09;
import br.com.objectos.html.ex.TestCase10;
import br.com.objectos.html.ex.TestCase14;
import br.com.objectos.html.ex.TestCase15;
import br.com.objectos.html.ex.TestCase16;
import br.com.objectos.html.ex.TestCase18;
import br.com.objectos.html.ex.TestCase20;
import br.com.objectos.html.ex.TestCase21;
import br.com.objectos.html.ex.TestCase26;
import java.util.Arrays;
import org.testng.annotations.Test;

public class TemplateDslTest {

  @Test
  public void testCase00() {
    var dsl = dsl(new TestCase00());
    testBuffer(
      dsl,
      ""
    );
    testProtos(
      dsl,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase01() {
    var dsl = dsl(new TestCase01());
    testBuffer(
      dsl,
      "pt-BR"
    );
    testProtos(
      dsl,
      5, 0, LANG.getCode(), ByteProto.STRING_STD,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      ByteProto.MARK_STRING_STD,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
    testCodes(
      dsl,
      ByteCode.ROOT,
      ByteCode.JMP_ELEMENT, 4,
      ByteCode.RETURN,
      ByteCode.START_ELEMENT, HTML.getCode(),
      ByteCode.STRING_STD, LANG.getCode(), 0, 5,
      ByteCode.GT,
      ByteCode.END_ELEMENT, HTML.getCode(),
      ByteCode.RETURN
    );
  }

  @Test
  public void testCase03() {
    var dsl = dsl(new TestCase03());
    testBuffer(
      dsl,
      ""
    );
    testProtos(
      dsl,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      HEAD.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
    testCodes(
      dsl,
      ByteCode.ROOT,
      ByteCode.JMP_ELEMENT, 4,
      ByteCode.RETURN,
      ByteCode.START_ELEMENT, HTML.getCode(),
      ByteCode.GT,
      ByteCode.JMP_ELEMENT, 12,
      ByteCode.END_ELEMENT, HTML.getCode(),
      ByteCode.RETURN,
      ByteCode.START_ELEMENT, HEAD.getCode(),
      ByteCode.GT,
      ByteCode.END_ELEMENT, HEAD.getCode(),
      ByteCode.RETURN
    );
  }

  @Test
  public void testCase06() {
    var dsl = dsl(new TestCase06());
    testBuffer(
      dsl,
      "pt-BRutf-8"
    );
    testProtos(
      dsl,
      5, 0, LANG.getCode(), ByteProto.STRING_STD,
      5, 5, CHARSET.getCode(), ByteProto.STRING_STD,
      ByteProto.SELF_CLOSING_TAG,
      ByteProto.MARK_STRING_STD,
      META.getCode(), ByteProto.START_ELEMENT, // meta
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      HEAD.getCode(), ByteProto.START_ELEMENT, // head
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      ByteProto.MARK_STRING_STD,
      HTML.getCode(), ByteProto.START_ELEMENT, // html
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase09() {
    var dsl = dsl(new TestCase09());
    testBuffer(
      dsl,
      "html!doctype"
    );
    testProtos(
      dsl,
      4, 0, ByteProto.BOOLEAN_ATTR,
      ByteProto.SELF_CLOSING_TAG,
      ByteProto.MARK_BOOLEAN_ATTR,
      8, 4, ByteProto.START_TAG,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.MARK_TAG,
      ByteProto.ROOT_START
    );
    testCodes(
      dsl,
      ByteCode.ROOT,
      ByteCode.JMP_ELEMENT, 12,
      ByteCode.JMP_ELEMENT, 6,
      ByteCode.RETURN,
      ByteCode.START_ELEMENT, HTML.getCode(),
      ByteCode.GT,
      ByteCode.END_ELEMENT, HTML.getCode(),
      ByteCode.RETURN,
      ByteCode.START_TAG, 4, 8,
      ByteCode.BOOLEAN_ATTR, 0, 4,
      ByteCode.SELF_CLOSING_TAG,
      ByteCode.RETURN
    );
  }

  @Test
  public void testCase10() {
    var dsl = dsl(new TestCase10());
    testBuffer(
      dsl,
      "utf-8"
    );
    testProtos(
      dsl,
      5, 0, CHARSET.getCode(), ByteProto.STRING_STD,
      ByteProto.SELF_CLOSING_TAG,
      ByteProto.MARK_STRING_STD,
      META.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      HEAD.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase14() {
    var dsl = dsl(new TestCase14());
    testBuffer(
      dsl,
      "o7html"
    );
    testProtos(
      dsl,
      ByteProto.END_ELEMENT,
      6, 0, ByteProto.TEXT,
      ByteProto.GT,
      P.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      BODY.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase15() {
    var dsl = dsl(new TestCase15());
    testBuffer(
      dsl,
      "abc"
    );
    testProtos(
      dsl,
      1, 0, ByteProto.TEXT_ELEMENT,
      ByteProto.END_ELEMENT,
      1, 1, ByteProto.TEXT,
      ByteProto.GT,
      P.getCode(), ByteProto.START_ELEMENT,
      1, 2, ByteProto.TEXT_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_TEXT_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.MARK_TEXT_ELEMENT,
      ByteProto.GT,
      DIV.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase16() {
    var dsl = dsl(new TestCase16());
    testBuffer(
      dsl,
      "elementattribute"
    );
    testProtos(
      dsl,
      7, 0, ByteProto.ATTR_OR_ELEMENT,
      ByteProto.END_ELEMENT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ELEMENT,
      ByteProto.GT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ATTR,
      HEAD.getCode(), ByteProto.START_ELEMENT,
      9, 7, ByteProto.ATTR_OR_ELEMENT,
      ByteProto.END_ELEMENT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ELEMENT,
      ByteProto.GT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ATTR,
      BODY.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      HTML.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase18() {
    var dsl = dsl(new TestCase18());
    testBuffer(
      dsl,
      "idt1t2tc18"
    );
    testProtos(
      dsl,
      2, 0, ID.getCode(), ByteProto.STRING_STD,
      2, 2, ByteProto.ATTR_OR_ELEMENT,
      2, 4, ByteProto.ATTR_OR_ELEMENT,
      ByteProto.END_ELEMENT,
      4, 6, ByteProto.TEXT,
      ByteProto.GT,
      P.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ELEMENT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ELEMENT,
      ByteProto.GT,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ATTR,
      AttributeOrElement.TITLE.code(),
      ByteProto.MARK_MAYBE_ATTR,
      ByteProto.MARK_STRING_STD,
      BODY.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase20() {
    var dsl = dsl(new TestCase20());
    testBuffer(
      dsl,
      "o7htmlis cool"
    );
    testProtos(
      dsl,
      ByteProto.END_ELEMENT,
      6, 0, ByteProto.TEXT,
      ByteProto.GT,
      A.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      NAV.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START,

      ByteProto.END_ELEMENT,
      7, 6, ByteProto.TEXT,
      ByteProto.GT,
      P.getCode(), ByteProto.START_ELEMENT,
      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      SECTION.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START,

      ByteProto.END_ELEMENT,
      ByteProto.MARK_TEMPLATE,
      ByteProto.MARK_TEMPLATE,
      ByteProto.GT,
      BODY.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
  }

  @Test
  public void testCase21() {
    var dsl = dsl(new TestCase21());
    testBuffer(
      dsl,
      "firstsecond"
    );
    testProtos(
      dsl,
      5, 0, CLASS.getCode(), ByteProto.STRING_STD,
      6, 5, CLASS.getCode(), ByteProto.STRING_STD,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      ByteProto.MARK_STRING_STD,
      ByteProto.MARK_STRING_STD,
      DIV.getCode(), ByteProto.START_ELEMENT,
      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT,
      ByteProto.ROOT_START
    );
    testCodes(
      dsl,
      ByteCode.ROOT,
      ByteCode.JMP_ELEMENT, 4,
      ByteCode.RETURN,
      ByteCode.START_ELEMENT, DIV.getCode(),
      ByteCode.STRING_STD, CLASS.getCode(), 0, 5,
      ByteCode.STRING_STD, CLASS.getCode(), 5, 6,
      ByteCode.GT,
      ByteCode.END_ELEMENT, DIV.getCode(),
      ByteCode.RETURN
    );
  }

  @Test
  public void testCase26() {
    var dsl = dsl(new TestCase26());
    testBuffer(
      dsl,
      "f0f1f2"
    );
    testProtos(
      dsl,
      2, 0, ID.getCode(), // id=f0
      ByteProto.STRING_STD,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      ByteProto.MARK_STRING_STD, // id="f0"
      DIV.getCode(),
      ByteProto.START_ELEMENT, // <div

      2, 2, ID.getCode(), // id=f1
      ByteProto.STRING_STD,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      ByteProto.MARK_STRING_STD, // id="f1"
      DIV.getCode(),
      ByteProto.START_ELEMENT, // <div

      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT, // ?
      ByteProto.GT,
      StandardElementName.ARTICLE.getCode(),
      ByteProto.START_ELEMENT, // <article

      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT,
      ByteProto.GT,
      StandardElementName.MAIN.getCode(),
      ByteProto.START_ELEMENT, // <main

      2, 4, ID.getCode(),
      ByteProto.STRING_STD,
      ByteProto.END_ELEMENT,
      ByteProto.GT,
      ByteProto.MARK_STRING_STD, // id="f2"
      DIV.getCode(),
      ByteProto.START_ELEMENT,

      ByteProto.END_ELEMENT,
      ByteProto.MARK_ELEMENT, // f(div)
      ByteProto.MARK_ELEMENT, // main
      ByteProto.MARK_ELEMENT, // f(div)
      ByteProto.GT,
      DIV.getCode(),
      ByteProto.START_ELEMENT,

      ByteProto.ROOT_END,
      ByteProto.MARK_ELEMENT, // <div>
      ByteProto.ROOT_START
    );
  }

  private TemplateDslImpl dsl(AbstractTemplate tmpl) {
    var dsl = new TemplateDslImpl();

    tmpl.acceptTemplateDsl(dsl);

    return dsl;
  }

  private void testBuffer(TemplateDslImpl dsl, String expectedBuffer) {
    assertEquals(dsl.bufferToString(), expectedBuffer);
  }

  private void testCodes(TemplateDslImpl dsl, int... expectedCodes) {
    CompiledTemplate compiled = dsl.compile();
    int[] codes = compiled.codes();
    try {
      assertEquals(codes, expectedCodes);
    } catch (AssertionError e) {
      System.err.println(Arrays.toString(codes));
      System.err.println(Arrays.toString(expectedCodes));
      throw e;
    }
  }

  private void testProtos(TemplateDslImpl dsl, int... expectedProtos) {
    int[] protos = dsl.protos();
    try {
      assertEquals(protos, expectedProtos);
    } catch (AssertionError e) {
      System.err.println(Arrays.toString(protos));
      System.err.println(Arrays.toString(expectedProtos));
      throw e;
    }
  }

}
