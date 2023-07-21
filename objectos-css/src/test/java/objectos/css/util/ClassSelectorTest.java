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
package objectos.css.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ClassSelectorTest {

  @Test
  public void nextClassSelector() {
    ClassSelector selector;
    selector = ClassSelector.randomClassSelector(5);

    assertEquals(selector.className().length(), 5);
  }

  @Test(description = "It should add the dot '.' character")
  public void toStringTest() {
    ClassSelector selector;
    selector = ClassSelector.of("abc");

    assertEquals(selector.toString(), ".abc");
  }

}