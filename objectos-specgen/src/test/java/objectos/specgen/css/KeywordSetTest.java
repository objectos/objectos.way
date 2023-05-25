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
package objectos.specgen.css;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class KeywordSetTest {

  @Test
  public void bottom() {
    test(
      "<length> | <percentage> | auto",
      "auto"
    );
  }

  @Test
  public void flex() {
    test(
      "none | [ <'flex-grow'> <'flex-shrink'>? || <'flex-basis'> ]",
      "none"
    );
  }

  @Test
  public void height() {
    test(
      "auto | <length> | <percentage> | min-content | max-content | fit-content(<length-percentage>)",
      "auto",
      "min-content",
      "max-content"
    );
  }

  @Test
  public void lineStyle() {
    test(
      "none | hidden | dotted | dashed | solid | double | groove | ridge | inset | outset",
      "none",
      "hidden",
      "dotted",
      "dashed",
      "solid",
      "double",
      "groove",
      "ridge",
      "inset",
      "outset"
    );
  }

  @Test
  public void rgb() {
    test(
      "rgb( <percentage>{3} [ / <alpha-value> ]? ) | rgb( <number>{3} [ / <alpha-value> ]? ) | rgb( <percentage>#{3} , <alpha-value>? ) | rgb( <number>#{3} , <alpha-value>? )"
    );
  }

  private void test(String formal, String... expected) {
    KeywordSet.Builder b = KeywordSet.builder();
    b.parse("x", formal);
    KeywordSet set = b.build();
    assertEquals(set.size(), expected.length);
    assertTrue(set.contains(expected));
  }

}
