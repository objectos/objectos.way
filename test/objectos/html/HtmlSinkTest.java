/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class HtmlSinkTest {

  private final HtmlSink sink = HtmlSink.of();

  @Test
  public void testCase01() {
    final DistinctClassNames classNames = new DistinctClassNames();

    var tmpl = new HtmlTemplate() {
      private final ClassName abc = new ClassName("abc");
      private final ClassName def = new ClassName("def");
      private final ClassName ghi = new ClassName("ghi");

      @Override
      protected final void definition() {
        div(
            abc, ghi, className("c01"), def,
            p(abc, def, t("Test case01"))
        );
      }
    };

    classNames.clear();

    sink.toProcessor(tmpl, classNames);

    assertEquals(classNames.size(), 4);
    assertTrue(classNames.contains("abc"));
    assertTrue(classNames.contains("def"));
    assertTrue(classNames.contains("ghi"));
    assertTrue(classNames.contains("c01"));
  }

}