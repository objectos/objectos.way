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
package br.com.objectos.css.type;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ColorTest {

  @Test(description = "toString() should have six chars/digits")
  public void hex() {
    test(Color.rgb(0), "#000000");
  }

  @Test(description = "hex() should not accept < 0x0 or > 0xffffff")
  public void rgbAboveMaxAndBelowMin() {
    try {
      Color.rgb(0 - 1);
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid hex color value #ffffffff");
    }
    try {
      Color.rgb(0xffffff + 1);
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid hex color value #1000000");
    }
  }

  private void test(Color color, String expected) {
    assertEquals(color.toString(), expected);
  }

}
