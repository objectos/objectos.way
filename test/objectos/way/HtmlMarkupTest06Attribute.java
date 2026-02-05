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
package objectos.way;

import static org.testng.Assert.assertEquals;

import objectos.script.Js;
import org.testng.annotations.Test;

public class HtmlMarkupTest06Attribute {

  private final Html.AttributeName custom = Html.AttributeName.of("custom");

  @Test
  public void attr01() {
    test(
        m -> m.div(m.attr(custom)),

        """
        <div custom></div>
        """
    );
  }

  @Test
  public void attr02() {
    test(
        m -> m.div(m.attr(custom, "x")),

        """
        <div custom="x"></div>
        """
    );
  }

  @Test
  public void event01() {
    test(
        m -> m.a(m.onclick(Js.target().remove())),

        """
        <a onclick='way.on(event,["W1",["ET"],["IV","Element","remove",[]]])'></a>
        """
    );
  }

  private void test(Html.Component component, String expected) {
    assertEquals(component.toHtml(), expected);
  }

}
