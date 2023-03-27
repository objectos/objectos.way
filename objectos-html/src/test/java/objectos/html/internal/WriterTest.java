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

import static org.testng.Assert.assertEquals;

import objectos.html.pseudom.HtmlDocument;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WriterTest {

  private static class ThisWriter extends Writer {
    @Override
    public final void process(HtmlDocument document) {
      // noop
    }
  }

  private final StringBuilder out = new StringBuilder();

  private final Writer writer = new ThisWriter();

  @BeforeClass
  public void _beforeClass() {
    writer.out = out;
  }

  @Test
  public void writeAttributeValue() {
    // normal text
    writeAttributeValue("abc", "abc");

    // html tokens
    writeAttributeValue("if (a < b && c > d) {}", "if (a &lt; b &amp;&amp; c &gt; d) {}");

    // named entities
    writeAttributeValue("foo&nbsp;bar", "foo&nbsp;bar");
    writeAttributeValue("foo&nb#;bar", "foo&amp;nb#;bar");
    writeAttributeValue("foo&nbsp bar", "foo&amp;nbsp bar");

    // decimal entities
    writeAttributeValue("foo&#39;bar", "foo&#39;bar");
    writeAttributeValue("foo &# 39;", "foo &amp;# 39;");

    // hex entities
    writeAttributeValue("foo&#xa9;bar&Xa9;baz", "foo&#xa9;bar&Xa9;baz");
    writeAttributeValue("foo &#xxa9;", "foo &amp;#xxa9;");

    // ampersand edge cases
    writeAttributeValue("&", "&amp;");
    writeAttributeValue("int a = value & MASK;", "int a = value &amp; MASK;");

    // quotes
    writeAttributeValue("\"", "&quot;");
    writeAttributeValue("'", "&#39;");

    // new lines should be left alone
    writeAttributeValue("foo\nbar", "foo\nbar");
  }

  @Test
  public void writeText() {
    // normal text
    writeText("abc", "abc");

    // html tokens
    writeText("if (a < b && c > d) {}", "if (a &lt; b &amp;&amp; c &gt; d) {}");

    // named entities
    writeText("foo&nbsp;bar", "foo&nbsp;bar");
    writeText("foo&nb#;bar", "foo&amp;nb#;bar");
    writeText("foo&nbsp bar", "foo&amp;nbsp bar");

    // decimal entities
    writeText("foo&#39;bar", "foo&#39;bar");
    writeText("foo &# 39;", "foo &amp;# 39;");

    // hex entities
    writeText("foo&#xa9;bar&Xa9;baz", "foo&#xa9;bar&Xa9;baz");
    writeText("foo &#xxa9;", "foo &amp;#xxa9;");

    // ampersand edge cases
    writeText("&", "&amp;");
    writeText("int a = value & MASK;", "int a = value &amp; MASK;");

    // quotes should be left alone
    writeText("\"", "\"");
    writeText("'", "'");

    // new lines should be left alone
    writeText("foo\nbar", "foo\nbar");
  }

  private void writeAttributeValue(String source, String expected) {
    out.setLength(0);

    writer.writeAttributeValue(source);

    assertEquals(out.toString(), expected);
  }

  private void writeText(String source, String expected) {
    out.setLength(0);

    writer.writeText(source);

    assertEquals(out.toString(), expected);
  }

}