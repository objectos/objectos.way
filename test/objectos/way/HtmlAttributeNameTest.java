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
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class HtmlAttributeNameTest {

  @Test
  public void canBeEncoded_WithSingleByte() {
    int size;
    size = HtmlAttributeName.size();

    int max;
    max = 1 << 8;

    assertTrue(size < max);
  }

  @Test
  public void create01() {
    final Html.AttributeName active;
    active = Html.AttributeName.of("data-active");

    assertEquals(active.index(), -1);
    assertEquals(active.name(), "data-active");
    assertEquals(active.booleanAttribute(), false);
    assertEquals(active.singleQuoted(), false);
  }

}