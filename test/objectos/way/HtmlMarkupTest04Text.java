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

public class HtmlMarkupTest04Text {

  @Test
  public void raw() {
    test(
        m -> {
          // root
          m.nbsp();

          // child node
          m.span(m.nbsp());

          // f
          m.p(m.f(() -> m.nbsp()));

          // flatten
          m.p(m.id("flatten"), m.flatten(m.nbsp()));
        },

        """
        &nbsp;<span>&nbsp;</span>
        <p>&nbsp;</p>
        <p id="flatten">&nbsp;</p>
        """
    );
  }

  @Test
  public void text() {
    test(
        m -> {
          // root
          m.text("x");

          // child node
          m.span(m.text("y"));

          // f
          m.p(m.f(() -> m.text("z")));

          // flatten
          m.p(m.id("flatten"), m.flatten(m.text("k")));
        },

        """
        x<span>y</span>
        <p>z</p>
        <p id="flatten">k</p>
        """
    );
  }

  private void test(Html.Component component, String expected) {
    assertEquals(component.toHtml(), expected);
  }

}
