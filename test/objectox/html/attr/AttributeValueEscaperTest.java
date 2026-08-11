/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.attr;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class AttributeValueEscaperTest {

  private String escape(String value) {
    final AttributeValueEscaper subject;
    subject = new AttributeValueEscaper(value);

    return subject.escape();
  }

  @Test(description = "empty string")
  public void escape01() {
    final String input;
    input = "";

    final String res;
    res = escape(input);

    assertEquals(res, "");
  }

  @Test(description = "plain text without special characters")
  public void escape02() {
    final String input;
    input = "Hello World";

    final String res;
    res = escape(input);

    assertEquals(res, "Hello World");
  }

  @Test(description = "ampersand")
  public void escape03() {
    final String input;
    input = "A&B";

    final String res;
    res = escape(input);

    assertEquals(res, "A&amp;B");
  }

  @Test(description = "less-than")
  public void escape04() {
    final String input;
    input = "A<B";

    final String res;
    res = escape(input);

    assertEquals(res, "A&lt;B");
  }

  @Test(description = "greater-than")
  public void escape05() {
    final String input;
    input = "A>B";

    final String res;
    res = escape(input);

    assertEquals(res, "A&gt;B");
  }

  @Test(description = "all text special characters")
  public void escape06() {
    final String input;
    input = "A&B<C>D";

    final String res;
    res = escape(input);

    assertEquals(res, "A&amp;B&lt;C&gt;D");
  }

  @Test(description = "multiple ampersands")
  public void escape07() {
    final String input;
    input = "&&";

    final String res;
    res = escape(input);

    assertEquals(res, "&amp;&amp;");
  }

  @Test(description = "multiple less-than signs")
  public void escape08() {
    final String input;
    input = "<<";

    final String res;
    res = escape(input);

    assertEquals(res, "&lt;&lt;");
  }

  @Test(description = "multiple greater-than signs")
  public void escape09() {
    final String input;
    input = ">>";

    final String res;
    res = escape(input);

    assertEquals(res, "&gt;&gt;");
  }

  @Test(description = "html entity in input should be double-escaped")
  public void escape10() {
    final String input;
    input = "&amp;";

    final String res;
    res = escape(input);

    assertEquals(res, "&amp;amp;");
  }

  @Test(description = "double quotation mark")
  public void escape11() {
    final String input;
    input = "A\"B";

    final String res;
    res = escape(input);

    assertEquals(res, "A&quot;B");
  }

  @Test(description = "single quotation mark")
  public void escape12() {
    final String input;
    input = "A'B";

    final String res;
    res = escape(input);

    assertEquals(res, "A&#39;B");
  }

  @Test(description = "all special characters")
  public void escape13() {
    final String input;
    input = "A&B<C>D\"F'G";

    final String res;
    res = escape(input);

    assertEquals(res, "A&amp;B&lt;C&gt;D&quot;F&#39;G");
  }

  @Test(description = "multiple double quotation marks")
  public void escape14() {
    final String input;
    input = "\"\"";

    final String res;
    res = escape(input);

    assertEquals(res, "&quot;&quot;");
  }

  @Test(description = "multiple single quotation marks")
  public void escape15() {
    final String input;
    input = "''";

    final String res;
    res = escape(input);

    assertEquals(res, "&#39;&#39;");
  }

}
