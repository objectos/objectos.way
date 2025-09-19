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

public class HtmlMarkupTest07Element {

  @Test
  public void elem01() {
    enum Attr implements Html.AttributeObject {
      INSTANCE;

      @Override
      public final Html.AttributeName attrName() { return Html.AttributeName.CLASS; }

      @Override
      public final String attrValue() { return "background-color:gray-100"; }
    }

    test(
        m -> m.elem(
            Html.ElementName.BUTTON,

            Html.Id.of("foo"),

            Html.ClassName.ofText("""
            display:block
            """),

            Attr.INSTANCE
        ),

        """
        <button id="foo" class="display:block background-color:gray-100"></button>
        """
    );
  }

  private void test(Html.Component component, String expected) {
    assertEquals(component.toHtml(), expected);
  }

}
