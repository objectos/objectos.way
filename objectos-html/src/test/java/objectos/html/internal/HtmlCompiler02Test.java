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
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.html.tmpl.TestIdSelector;
import org.testng.annotations.Test;

public class HtmlCompiler02Test {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.GT,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.LANG, "pt-BR");

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.LANG.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.ATTR_VALUE_END,
      ByteCode.GT,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  <html class="no-js" lang="pt-BR"></html>
  """)
  public void testCase02() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.CLASS, "no-js");
    compiler.attribute(StandardAttributeName.LANG, "pt-BR");

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.CLASS.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.ATTR_VALUE_END,
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.LANG.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(1),
      Bytes.encodeInt1(1),
      ByteCode.ATTR_VALUE_END,
      ByteCode.GT,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  <html><head></head></html>
  """)
  public void testCase03() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.HEAD.ordinal(),
      ByteCode.GT,
      ByteCode.END_TAG,
      (byte) StandardElementName.HEAD.ordinal(),

      ByteCode.NL_OPTIONAL,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void testCase09() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.doctype();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.DOCTYPE,
      ByteCode.NL_OPTIONAL,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.GT,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  fragment inclusion
  """)
  public void testCase10() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    FragmentAction action;
    action = () -> {
      compiler.attribute(StandardAttributeName.CHARSET, "utf-8");

      compiler.elementBegin(StandardElementName.META);
      compiler.elementValue(InternalInstruction.INSTANCE);
      compiler.elementEnd();
    };

    compiler.fragment(action);

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.HEAD.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 2,

      ByteCode.START_TAG,
      (byte) StandardElementName.META.ordinal(),
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.CHARSET.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.ATTR_VALUE_END,
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.END_TAG,
      (byte) StandardElementName.HEAD.ordinal(),

      ByteCode.NL_OPTIONAL,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  External id attributes
  """)
  public void testCase13() {
    TestIdSelector foo;
    foo = new TestIdSelector("foo");

    TestIdSelector bar;
    bar = new TestIdSelector("bar");

    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(bar);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(foo);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.ID.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(1),
      Bytes.encodeInt1(1),
      ByteCode.ATTR_VALUE_END,
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.BODY.ordinal(),
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.ID.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.ATTR_VALUE_END,
      ByteCode.GT,

      ByteCode.END_TAG,
      (byte) StandardElementName.BODY.ordinal(),

      ByteCode.NL_OPTIONAL,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  Text child element
  """)
  public void testCase14() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.text("o7html");

    compiler.elementBegin(StandardElementName.P);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.BODY.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 2,

      ByteCode.START_TAG,
      (byte) StandardElementName.P.ordinal(),
      ByteCode.GT,
      ByteCode.TEXT,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.END_TAG,
      (byte) StandardElementName.P.ordinal(),

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.END_TAG,
      (byte) StandardElementName.BODY.ordinal(),

      ByteCode.NL_OPTIONAL,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  Ambiguous
  """)
  public void testCase16() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.ambiguous(Ambiguous.TITLE, "element");

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.ambiguous(Ambiguous.TITLE, "attribute");

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.HEAD.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 2,

      ByteCode.START_TAG,
      (byte) StandardElementName.TITLE.ordinal(),
      ByteCode.GT,
      ByteCode.TEXT,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.END_TAG,
      (byte) StandardElementName.TITLE.ordinal(),

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,
      ByteCode.END_TAG,
      (byte) StandardElementName.HEAD.ordinal(),

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.BODY.ordinal(),
      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.TITLE.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(1),
      Bytes.encodeInt1(1),
      ByteCode.ATTR_VALUE_END,
      ByteCode.GT,

      ByteCode.END_TAG,
      (byte) StandardElementName.BODY.ordinal(),

      ByteCode.NL_OPTIONAL,

      ByteCode.END_TAG,
      (byte) StandardElementName.HTML.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  include template
  """)
  public void testCase20() {
    HtmlTemplate nav = new HtmlTemplate() {
      @Override
      protected final void definition() {
        nav();
      }
    };

    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(nav);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.BODY.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB, (byte) 1,

      ByteCode.START_TAG,
      (byte) StandardElementName.NAV.ordinal(),
      ByteCode.GT,
      ByteCode.END_TAG,
      (byte) StandardElementName.NAV.ordinal(),

      ByteCode.NL_OPTIONAL,
      ByteCode.END_TAG,
      (byte) StandardElementName.BODY.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  style/script => raw
  """)
  public void testCase25() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.text("ul > li {}");

    compiler.elementBegin(StandardElementName.STYLE);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.STYLE.ordinal(),
      ByteCode.GT,

      ByteCode.NL_OPTIONAL,
      ByteCode.TAB_BLOCK, (byte) 1,

      ByteCode.TEXT_STYLE,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),

      ByteCode.NL_OPTIONAL,
      ByteCode.END_TAG,
      (byte) StandardElementName.STYLE.ordinal(),
      ByteCode.NL
    );
  }

  @Test(description = """
  HtmlTemplate TC31

  - email input
  """)
  public void testCase31() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.TYPE, "email");
    compiler.attribute(StandardAttributeName.REQUIRED);

    compiler.elementBegin(StandardElementName.INPUT);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledMarkup result;
    result = compiler.compile();

    test(
      result,

      ByteCode.START_TAG,
      (byte) StandardElementName.INPUT.ordinal(),

      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.TYPE.ordinal(),
      ByteCode.ATTR_VALUE_START,
      ByteCode.ATTR_VALUE,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteCode.ATTR_VALUE_END,

      ByteCode.SPACE,
      ByteCode.ATTR_NAME,
      (byte) StandardAttributeName.REQUIRED.ordinal(),

      ByteCode.GT,

      ByteCode.NL
    );
  }

  private void test(CompiledMarkup markup, byte... expected) {
    byte[] result;
    result = markup.main;

    if (result.length != expected.length) {
      throw new AssertionError(
        """
        Arrays don't have the same size.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }

    if (!Arrays.equals(result, expected)) {
      throw new AssertionError(
        """
        Arrays don't have the same content.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }
  }

}