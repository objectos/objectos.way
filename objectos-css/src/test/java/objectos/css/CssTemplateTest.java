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

  private final CssSink sink = new CssSink();

  private final StringBuilder stringBuilder = new StringBuilder();

  @Test(enabled = false)
  public void testCase00() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(BODY);
        }
      },

      """
      body {}
      """,

      """
      body{}
      """
    );
  }

  private void test(CssTemplate template, String pretty, String minified) {
    stringBuilder.setLength(0);

    sink.toStringBuilder(template, stringBuilder);

    assertEquals(stringBuilder.toString(), pretty);
  }

}