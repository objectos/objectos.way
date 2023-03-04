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

import br.com.objectos.html.ex.Sitemap;
import objectos.html.AbstractTemplate;
import objectos.html.Template;
import org.testng.annotations.Test;

public class TemplateTest {

  @Test
  public void index() {
    testMinified(
        new Index(new Sitemap()),
        "<!doctype html>",
        "<html>",
        "<head>",
        // "<meta charset=\"utf-8\">",
        "<meta>",
        // "<link rel=\"stylesheet\"
        // href=\"https://example.com/css/styles.css\">",
        "<link>",
        "</head>",
        "<body>",
        "<header>",
        "<nav>",
        "<a>",
        "<img>",
        "</a>",
        "</nav>",
        "</header>",
        "</body>",
        "</html>"
    );
  }
  
  @Test
  public void t() {
    testMinified(
        new AbstractTemplate() {
          @Override
          protected final void definition() {
            p(t("0"));
            p(t("0", "1"));
            p(t("0", "1", "2"));
            p(t("0", "1", "2", "3"));
            p(t("0", "1", "2", "3", "4"));
            p(t("0", "1", "2", "3", "4", "5"));
            p(t("0", "1", "2", "3", "4", "5", "6"));
            p(t("0", "1", "2", "3", "4", "5", "6", "7"));
            p(t("0", "1", "2", "3", "4", "5", "6", "7", "8"));
            p(t("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
            p(t("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
          }
        },
        "<p>0</p>",
        "<p>0 1</p>",
        "<p>0 1 2</p>",
        "<p>0 1 2 3</p>",
        "<p>0 1 2 3 4</p>",
        "<p>0 1 2 3 4 5</p>",
        "<p>0 1 2 3 4 5 6</p>",
        "<p>0 1 2 3 4 5 6 7</p>",
        "<p>0 1 2 3 4 5 6 7 8</p>",
        "<p>0 1 2 3 4 5 6 7 8 9</p>",
        "<p>0 1 2 3 4 5 6 7 8 9 10</p>"
    );
  }
  
  @Test
  public void tags() {
    testMinified(
        new AbstractTemplate() {
          @Override
          protected final void definition() {
            span("x");
          }
        },
        "<span>x</span>"
    );
  }
  
  private void testMinified(Template o, String... expected) {
    assertEquals(o.printMinified(), String.join("", expected));
  }
  
}
