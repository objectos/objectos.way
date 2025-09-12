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

import java.util.List;
import org.testng.annotations.Test;

public class HtmlMarkupTest06Way {

  private final Html.Component li1 = m -> m.li("1");
  private final Html.Component li2 = m -> m.li("2");
  private final Html.Component li3 = m -> m.li("3");

  @Test
  public void c1() {
    test(
        m -> m.elem(
            Html.ElementName.UL,
            m.c(
                li1,
                li2,
                li3
            )
        ),

        """
        <ul>
        <li>1</li>
        <li>2</li>
        <li>3</li>
        </ul>
        """
    );
  }

  @Test
  public void c2() {
    test(
        m -> m.elem(
            Html.ElementName.UL,
            m.c(List.of(
                li1,
                li2,
                li3
            ))
        ),

        """
        <ul>
        <li>1</li>
        <li>2</li>
        <li>3</li>
        </ul>
        """
    );
  }

  @Test
  public void c3() {
    test(
        m -> m.elem(
            Html.ElementName.UL,
            m.c()
        ),

        """
        <ul></ul>
        """
    );

    test(
        m -> m.elem(
            Html.ElementName.UL,
            m.c(List.of())
        ),

        """
        <ul></ul>
        """
    );
  }

  private void test(Html.Component component, String expected) {
    assertEquals(component.toHtml(), expected);
  }

}
