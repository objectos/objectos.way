/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CssTemplateTest {

  @Test(description = """
  Preflight 01

  - universal selector
  """)
  public void testCase01() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any
          );
        }
      },

      """
      * {}
      """
    );
  }

  @Test(description = """
  Preflight 01

  - selector list
  """)
  public void testCase02() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,
            __after,
            __before
          );
        }
      },

      """
      *, ::after, ::before {}
      """
    );
  }

  @Test(description = """
  Preflight 01

  - box-sizing
  """)
  public void testCase03() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,

            boxSizing(borderBox),
            boxSizing(contentBox),
            boxSizing(inherit),
            boxSizing(initial),
            boxSizing(unset)
          );
        }
      },

      """
      * {
        box-sizing: border-box;
        box-sizing: content-box;
        box-sizing: inherit;
        box-sizing: initial;
        box-sizing: unset;
      }
      """
    );
  }

  @Test(description = """
  Preflight 01

  - border-width
  """)
  public void testCase04() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,

            borderWidth($0),
            borderWidth(medium, thick),
            borderWidth(medium, thick, thin),
            borderWidth(medium, thick, thin, unset)
          );
        }
      },

      """
      * {
        border-width: 0;
        border-width: medium thick;
        border-width: medium thick thin;
        border-width: medium thick thin unset;
      }
      """
    );
  }

  private void test(CssTemplate template, String pretty) {
    var sheet = template.toStyleSheet();
    assertEquals(sheet.toString(), pretty);
  }

}