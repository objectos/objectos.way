/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HtmlClassNameTest {

  @Test(description = "of: single class name")
  public void of01() {
    of("foo", "foo");
  }

  @Test(description = "of: space separated")
  public void of02() {
    of("foo   bar", "foo   bar");
  }

  @Test(description = "of: new-line separated")
  public void of03() {
    of("foo\nbar", "foo\nbar");
  }

  private void of(String value, String expected) {
    Html.ClassName cn;
    cn = Html.ClassName.of(value);

    assertEquals(cn.attrValue(), expected);
  }

  @Test(description = "ofText: single class name")
  public void ofText01() {
    ofText("foo", "foo");
  }

  @Test(description = "ofText: space separated")
  public void ofText02() {
    ofText("foo   bar", "foo bar");
  }

  @Test(description = "ofText: new-line separated")
  public void ofText03() {
    ofText("""
    foo
    bar
    """, "foo bar");
  }

  @Test(description = "ofText: new-line separated")
  public void ofText04() {
    ofText("""
    first \tsecond
      third\r

    fourth
    """, "first second third fourth");
  }

  private void ofText(String value, String expected) {
    Html.ClassName cn;
    cn = Html.ClassName.ofText(value);

    assertEquals(cn.attrValue(), expected);
  }

}