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
package objectos.css.type;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ColorHexTest {

  @Test
  public void getMinifiedHexString() {
    assertEquals(
        ColorHex.of("#71f2C9").getMinifiedHexString(),
        "#71f2C9"
    );

    assertEquals(
        ColorHex.of("#f00").getMinifiedHexString(),
        "#f00"
    );

    assertEquals(
        ColorHex.of("#ff0099").getMinifiedHexString(),
        "#f09"
    );

    assertEquals(
        ColorHex.of("#33Aa33Ff").getMinifiedHexString(),
        "#3a3f"
    );
  }

  @Test
  public void of() {
    assertEquals(
        ColorHex.of("#71f2C9").toString(),
        "#71f2C9"
    );

    assertEquals(
        ColorHex.of("#f00").toString(),
        "#f00"
    );

    assertEquals(
        ColorHex.of("#33AA3380").toString(),
        "#33AA3380"
    );

    try {
      ColorHex.of("foo");
      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid CSS <color> hex notation: foo");
    }

    try {
      ColorHex.of("c0c0c0");
      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid CSS <color> hex notation: c0c0c0");
    }
  }

}
