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
import objectos.html.FragmentLambda;
import objectos.html.TestClassSelector;
import objectos.html.TestIdSelector;
import objectos.html.tmpl.Api;
import org.testng.annotations.Test;

public class HtmlCompiler01Test {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.END,
        Bytes.encodeInt0(5),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.LANG, "pt-BR");

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ATTRIBUTE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED5,
        (byte) StandardAttributeName.LANG.ordinal(),
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL5,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ATTRIBUTE1,
        Bytes.encodeInt0(11),
        ByteProto.END,
        Bytes.encodeInt0(12),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html class="no-js" lang="pt-BR"></html>
  """)
  public void testCase02() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.CLASS, "no-js");
    compiler.attribute(StandardAttributeName.LANG, "pt-BR");

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ATTRIBUTE);
    compiler.elementValue(Api.ATTRIBUTE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED5,
        (byte) StandardAttributeName.CLASS.ordinal(),
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL5,

        ByteProto.MARKED5,
        (byte) StandardAttributeName.LANG.ordinal(),
        Bytes.encodeInt0(1),
        Bytes.encodeInt1(1),
        ByteProto.INTERNAL5,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(9),
        Bytes.encodeInt1(9),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ATTRIBUTE1,
        Bytes.encodeInt0(16),
        ByteProto.ATTRIBUTE1,
        Bytes.encodeInt0(13),
        ByteProto.END,
        Bytes.encodeInt0(19),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html><head></head></html>
  """)
  public void testCase03() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HEAD.ordinal(),
        ByteProto.END,
        Bytes.encodeInt0(5),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(14),
        ByteProto.END,
        Bytes.encodeInt0(15),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void testCase09() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.doctype();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.DOCTYPE,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.END,
        Bytes.encodeInt0(5),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  fragment inclusion
  """)
  public void testCase10() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    FragmentLambda action;
    action = () -> {
      compiler.attribute(StandardAttributeName.CHARSET, "utf-8");

      compiler.elementBegin(StandardElementName.META);
      compiler.elementValue(Api.ATTRIBUTE);
      compiler.elementEnd();
    };

    int index = compiler.fragmentBegin();

    action.execute();

    compiler.fragmentEnd(index);

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementValue(Api.FRAGMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.LENGTH3,
        Bytes.encodeInt0(18),
        Bytes.encodeInt1(18),
        Bytes.encodeInt2(18),

        ByteProto.MARKED5,
        (byte) StandardAttributeName.CHARSET.ordinal(),
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL5,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.META.ordinal(),
        ByteProto.ATTRIBUTE1,
        Bytes.encodeInt0(11),
        ByteProto.END,
        Bytes.encodeInt0(12),
        ByteProto.INTERNAL,

        // fragment end
        ByteProto.END,
        Bytes.encodeInt0(19),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HEAD.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(19),
        ByteProto.END,
        Bytes.encodeInt0(29),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(16),
        ByteProto.END,
        Bytes.encodeInt0(39),
        ByteProto.INTERNAL
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

    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(bar);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(foo);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(8),
        Bytes.encodeInt1(8),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.BODY.ordinal(),
        ByteProto.ATTRIBUTE_ID,
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.END,
        Bytes.encodeInt0(8),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(10),
        Bytes.encodeInt1(10),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ATTRIBUTE_ID,
        Bytes.encodeInt0(1),
        Bytes.encodeInt1(1),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(20),
        ByteProto.END,
        Bytes.encodeInt0(21),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  Text child element
  """)
  public void testCase14() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.text("o7html");

    compiler.elementBegin(StandardElementName.P);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED4,
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL4,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.P.ordinal(),
        ByteProto.TEXT,
        Bytes.encodeInt0(10),
        ByteProto.END,
        Bytes.encodeInt0(11),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.BODY.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(16),
        ByteProto.END,
        Bytes.encodeInt0(21),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(16),
        ByteProto.END,
        Bytes.encodeInt0(31),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  Ambiguous
  """)
  public void testCase16() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.ambiguous(Ambiguous.TITLE, "element");

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.ambiguous(Ambiguous.TITLE, "attribute");

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED5,
        (byte) Ambiguous.TITLE.ordinal(),
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL5,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HEAD.ordinal(),
        ByteProto.AMBIGUOUS1,
        Bytes.encodeInt0(11),
        ByteProto.END,
        Bytes.encodeInt0(12),
        ByteProto.INTERNAL,

        ByteProto.MARKED5,
        (byte) Ambiguous.TITLE.ordinal(),
        Bytes.encodeInt0(1),
        Bytes.encodeInt1(1),
        ByteProto.INTERNAL5,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.BODY.ordinal(),
        ByteProto.AMBIGUOUS1,
        Bytes.encodeInt0(11),
        ByteProto.END,
        Bytes.encodeInt0(12),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(9),
        Bytes.encodeInt1(9),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.HTML.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(31),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(18),
        ByteProto.END,
        Bytes.encodeInt0(39),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  include template
  """)
  public void testCase20() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    // template begin
    compiler.elementBegin(StandardElementName.NAV);
    compiler.elementEnd();
    // template end

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(Api.FRAGMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.NAV.ordinal(),
        ByteProto.END,
        Bytes.encodeInt0(5),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.BODY.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(14),
        ByteProto.END,
        Bytes.encodeInt0(15),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  style/script => raw
  """)
  public void testCase25() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.text("ul > li {}");

    compiler.elementBegin(StandardElementName.STYLE);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED4,
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL4,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.STYLE.ordinal(),
        ByteProto.TEXT,
        Bytes.encodeInt0(10),
        ByteProto.END,
        Bytes.encodeInt0(11),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  HtmlTemplate TC31

  - email input
  """)
  public void testCase31() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.TYPE, "email");
    compiler.attribute(StandardAttributeName.REQUIRED);

    compiler.elementBegin(StandardElementName.INPUT);
    compiler.elementValue(Api.ATTRIBUTE);
    compiler.elementValue(Api.ATTRIBUTE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED5,
        (byte) StandardAttributeName.TYPE.ordinal(),
        Bytes.encodeInt0(0),
        Bytes.encodeInt0(0),
        ByteProto.INTERNAL5,

        ByteProto.MARKED3,
        (byte) StandardAttributeName.REQUIRED.ordinal(),
        ByteProto.INTERNAL3,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(9),
        Bytes.encodeInt1(9),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.INPUT.ordinal(),
        ByteProto.ATTRIBUTE1,
        Bytes.encodeInt0(14),
        ByteProto.ATTRIBUTE0,
        Bytes.encodeInt0(11),
        ByteProto.END,
        Bytes.encodeInt0(17),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  HtmlTemplate TC46

  - flatten instruction
  """)
  public void testCase46() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.LABEL);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.INPUT);
    compiler.elementEnd();

    compiler.flattenBegin();
    compiler.elementValue(Api.ELEMENT);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.FORM);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.LABEL.ordinal(),
        ByteProto.END,
        Bytes.encodeInt0(5),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.INPUT.ordinal(),
        ByteProto.END,
        Bytes.encodeInt0(5),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(20),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(14),
        ByteProto.END,
        Bytes.encodeInt0(23),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(9),
        Bytes.encodeInt1(9),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.FORM.ordinal(),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(32),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(26),
        ByteProto.END,
        Bytes.encodeInt0(35),
        ByteProto.INTERNAL
    );
  }

  @Test(description = """
  HtmlTemplate TC47

  - grid component
  """)
  public void testCase47() {
    TestClassSelector grd;
    grd = new TestClassSelector("grd");

    TestClassSelector col;
    col = new TestClassSelector("col");

    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.text("A");

    compiler.flattenBegin();
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.DIV);
    compiler.elementValue(col);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.text("B");

    compiler.flattenBegin();
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.DIV);
    compiler.elementValue(col);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.flattenBegin();
    compiler.elementValue(Api.ELEMENT);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.DIV);
    compiler.elementValue(grd);
    compiler.elementValue(Api.ELEMENT);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
        compiler,

        ByteProto.MARKED4,
        Bytes.encodeInt0(0),
        Bytes.encodeInt1(0),
        ByteProto.INTERNAL4,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.TEXT,
        Bytes.encodeInt0(8),
        ByteProto.END,
        Bytes.encodeInt0(9),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(10),
        Bytes.encodeInt1(10),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.DIV.ordinal(),
        ByteProto.ATTRIBUTE_CLASS,
        Bytes.encodeInt0(1),
        Bytes.encodeInt1(1),
        ByteProto.TEXT,
        Bytes.encodeInt0(21),
        ByteProto.END,
        Bytes.encodeInt0(22),
        ByteProto.INTERNAL,

        ByteProto.MARKED4,
        Bytes.encodeInt0(2),
        Bytes.encodeInt1(2),
        ByteProto.INTERNAL4,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(5),
        Bytes.encodeInt1(5),
        ByteProto.TEXT,
        Bytes.encodeInt0(8),
        ByteProto.END,
        Bytes.encodeInt0(9),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(10),
        Bytes.encodeInt1(10),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.DIV.ordinal(),
        ByteProto.ATTRIBUTE_CLASS,
        Bytes.encodeInt0(3),
        Bytes.encodeInt1(3),
        ByteProto.TEXT,
        Bytes.encodeInt0(21),
        ByteProto.END,
        Bytes.encodeInt0(22),
        ByteProto.INTERNAL,

        ByteProto.LENGTH2,
        Bytes.encodeInt0(7),
        Bytes.encodeInt1(7),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(42),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(19),
        ByteProto.END,
        Bytes.encodeInt0(57),
        ByteProto.INTERNAL,

        ByteProto.ELEMENT,
        Bytes.encodeInt0(12),
        Bytes.encodeInt1(12),
        ByteProto.STANDARD_NAME,
        (byte) StandardElementName.DIV.ordinal(),
        ByteProto.ATTRIBUTE_CLASS,
        Bytes.encodeInt0(4),
        Bytes.encodeInt1(4),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(57),
        ByteProto.ELEMENT,
        Bytes.encodeInt0(34),
        ByteProto.END,
        Bytes.encodeInt0(72),
        ByteProto.INTERNAL
    );
  }

  private void test(HtmlCompiler01 compiler, byte... expected) {
    byte[] result;
    result = Arrays.copyOf(compiler.main, compiler.mainIndex);

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