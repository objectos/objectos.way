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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CommentEscaperTest {

  private String escape(String value) {
    final CommentEscaper subject;
    subject = new CommentEscaper(value);

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

  @Test(description = "single hyphen is not escaped")
  public void escape03() {
    final String input;
    input = "A-B";

    final String res;
    res = escape(input);

    assertEquals(res, "A-B");
  }

  @Test(description = "double hyphen is not escaped")
  public void escape04() {
    final String input;
    input = "A--B";

    final String res;
    res = escape(input);

    assertEquals(res, "A--B");
  }

  @Test(description = "comment close sequence")
  public void escape05() {
    final String input;
    input = "-->";

    final String res;
    res = escape(input);

    assertEquals(res, "--&gt;");
  }

  @Test(description = "triple hyphen is not escaped")
  public void escape06() {
    final String input;
    input = "---";

    final String res;
    res = escape(input);

    assertEquals(res, "---");
  }

  @Test(description = "single hyphen at start is not escaped")
  public void escape07() {
    final String input;
    input = "-Hello";

    final String res;
    res = escape(input);

    assertEquals(res, "-Hello");
  }

  @Test(description = "single hyphen at end is not escaped")
  public void escape08() {
    final String input;
    input = "Hello-";

    final String res;
    res = escape(input);

    assertEquals(res, "Hello-");
  }

  @Test(description = "text with embedded double hyphen is not escaped")
  public void escape09() {
    final String input;
    input = "foo--bar";

    final String res;
    res = escape(input);

    assertEquals(res, "foo--bar");
  }

  @Test(description = "lambda expression is not escaped")
  public void escape10() {
    final String input;
    input = "(x) -> x - 1";

    final String res;
    res = escape(input);

    assertEquals(res, "(x) -> x - 1");
  }

  @Test(description = "comment close sequence in middle of text")
  public void escape11() {
    final String input;
    input = "foo-->bar";

    final String res;
    res = escape(input);

    assertEquals(res, "foo--&gt;bar");
  }

  @Test(description = "multiple comment close sequences")
  public void escape12() {
    final String input;
    input = "-->-->";

    final String res;
    res = escape(input);

    assertEquals(res, "--&gt;--&gt;");
  }

}