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
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.html.tmpl.TestIdSelector;
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

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(5),
      Bytes.encodeInt1(5),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.END,
      Bytes.encodeInt0(5),
      ByteProto2.INTERNAL
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
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.MARKED5,
      (byte) StandardAttributeName.LANG.ordinal(),
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteProto2.INTERNAL5,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.ATTRIBUTE1,
      Bytes.encodeInt0(11),
      ByteProto2.END,
      Bytes.encodeInt0(12),
      ByteProto2.INTERNAL
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
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.MARKED5,
      (byte) StandardAttributeName.CLASS.ordinal(),
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteProto2.INTERNAL5,

      ByteProto2.MARKED5,
      (byte) StandardAttributeName.LANG.ordinal(),
      Bytes.encodeInt0(1),
      Bytes.encodeInt1(1),
      ByteProto2.INTERNAL5,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(9),
      Bytes.encodeInt1(9),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.ATTRIBUTE1,
      Bytes.encodeInt0(16),
      ByteProto2.ATTRIBUTE1,
      Bytes.encodeInt0(13),
      ByteProto2.END,
      Bytes.encodeInt0(19),
      ByteProto2.INTERNAL
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
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.MARKED,
      Bytes.encodeInt0(5),
      Bytes.encodeInt1(5),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HEAD.ordinal(),
      ByteProto2.END,
      Bytes.encodeInt0(5),
      ByteProto2.INTERNAL,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.ELEMENT,
      Bytes.encodeInt0(14),
      ByteProto2.END,
      Bytes.encodeInt0(15),
      ByteProto2.INTERNAL
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

      ByteProto2.DOCTYPE,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(5),
      Bytes.encodeInt1(5),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.END,
      Bytes.encodeInt0(5),
      ByteProto2.INTERNAL
    );
  }

  @Test(description = """
  fragment inclusion
  """)
  public void testCase10() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

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
    compiler.elementValue(InternalFragment.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.MARKED,
      Bytes.encodeInt0(18),
      Bytes.encodeInt1(18),

      ByteProto2.MARKED5,
      (byte) StandardAttributeName.CHARSET.ordinal(),
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteProto2.INTERNAL5,

      ByteProto2.MARKED,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.META.ordinal(),
      ByteProto2.ATTRIBUTE1,
      Bytes.encodeInt0(11),
      ByteProto2.END,
      Bytes.encodeInt0(12),
      ByteProto2.INTERNAL,

      // fragment end
      ByteProto2.END,
      Bytes.encodeInt0(18),
      ByteProto2.INTERNAL,

      ByteProto2.MARKED,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HEAD.ordinal(),
      ByteProto2.ELEMENT,
      Bytes.encodeInt0(19),
      ByteProto2.END,
      Bytes.encodeInt0(28),
      ByteProto2.INTERNAL,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.ELEMENT,
      Bytes.encodeInt0(16),
      ByteProto2.END,
      Bytes.encodeInt0(38),
      ByteProto2.INTERNAL
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
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.MARKED,
      Bytes.encodeInt0(8),
      Bytes.encodeInt1(8),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.BODY.ordinal(),
      ByteProto2.ATTRIBUTE_ID,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteProto2.END,
      Bytes.encodeInt0(8),
      ByteProto2.INTERNAL,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(10),
      Bytes.encodeInt1(10),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.ATTRIBUTE_ID,
      Bytes.encodeInt0(1),
      Bytes.encodeInt1(1),
      ByteProto2.ELEMENT,
      Bytes.encodeInt0(20),
      ByteProto2.END,
      Bytes.encodeInt0(21),
      ByteProto2.INTERNAL
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
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(InternalInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.MARKED4,
      Bytes.encodeInt0(0),
      Bytes.encodeInt1(0),
      ByteProto2.INTERNAL4,

      ByteProto2.MARKED,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.P.ordinal(),
      ByteProto2.TEXT,
      Bytes.encodeInt0(10),
      ByteProto2.END,
      Bytes.encodeInt0(11),
      ByteProto2.INTERNAL,

      ByteProto2.MARKED,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.BODY.ordinal(),
      ByteProto2.ELEMENT,
      Bytes.encodeInt0(16),
      ByteProto2.END,
      Bytes.encodeInt0(21),
      ByteProto2.INTERNAL,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(7),
      Bytes.encodeInt1(7),
      ByteProto2.STANDARD_NAME,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.ELEMENT,
      Bytes.encodeInt0(16),
      ByteProto2.END,
      Bytes.encodeInt0(31),
      ByteProto2.INTERNAL
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