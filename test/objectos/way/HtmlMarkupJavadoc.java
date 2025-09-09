/*
 * Copyright (C) 2015-2025 Objectos Software LTDA.
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

import java.util.List;
import org.testng.annotations.Test;

public class HtmlMarkupJavadoc {

  @Test
  public void f0() {
    // @start region="f0"
    List<String> days = List.of("Mon", "Wed", "Fri");
    Html.Component c = m -> m.ul(
        m.f(() -> {
          for (String day : days) {
            m.li(day);
          }
        })
    );
    // @end

    assertEquals(
        c.toHtml(),

        """
        <ul>
        <li>Mon</li>
        <li>Wed</li>
        <li>Fri</li>
        </ul>
        """
    );
  }

  @Test
  public void f1() {
    // @start region="f1"
    class MyComponent implements Html.Component {
      final List<String> days = List.of("Mon", "Wed", "Fri");

      @Override
      public void renderHtml(Html.Markup m) {
        m.ul(
            m.f(this::contents, m)
        );
      }

      private void contents(Html.Markup m) {
        for (String day : days) {
          m.li(day);
        }
      }
    }
    // @end

    assertEquals(
        new MyComponent().toHtml(),

        """
        <ul>
        <li>Mon</li>
        <li>Wed</li>
        <li>Fri</li>
        </ul>
        """
    );
  }

  @Test
  public void f2() {
    // @start region="f2"
    class MyComponent implements Html.Component {
      @Override
      public void renderHtml(Html.Markup m) {
        m.div(
            m.f(this::button, m, "OK"),
            m.f(this::button, m, "Cancel")
        );
      }

      private void button(Html.Markup m, String text) {
        m.button(text);
      }
    }
    // @end

    assertEquals(
        new MyComponent().toHtml(),

        """
        <div><button>OK</button><button>Cancel</button></div>
        """
    );
  }

  @Test
  public void f3() {
    // @start region="f3"
    class MyComponent implements Html.Component {
      @Override
      public void renderHtml(Html.Markup m) {
        m.div(
            m.f(this::item, m, "City", "Tokyo"),
            m.f(this::item, m, "Country", "Japan")
        );
      }

      private void item(Html.Markup m, String name, String value) {
        m.p(m.text(name), m.span(value));
      }
    }
    // @end

    assertEquals(
        new MyComponent().toHtml(),

        """
        <div>
        <p>City<span>Tokyo</span></p>
        <p>Country<span>Japan</span></p>
        </div>
        """
    );
  }

}