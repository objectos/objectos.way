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
package objectos.html;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HtmlTemplateTest {

  @Test(description = """
  HtmlTemplate TC00

  - single html element
  """)
  public void testCase00() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html();
        }
      }.minified(),

      """
      <html></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC01

  - single html element with a single attribute.
  """)
  public void testCase01() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(lang("pt-BR"));
        }
      }.minified(),

      """
      <html lang="pt-BR"></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC02

  - Single html element with two attributes
  """)
  public void testCase02() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(_class("no-js"), lang("pt-BR"));
        }
      }.minified(),

      """
      <html class="no-js" lang="pt-BR"></html>"""
    );
  }

}
