/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Utf8Test {

  private final byte[] utf8Digits = new byte[] {
      Utf8.DIGIT_0,
      Utf8.DIGIT_1,
      Utf8.DIGIT_2,
      Utf8.DIGIT_3,
      Utf8.DIGIT_4,
      Utf8.DIGIT_5,
      Utf8.DIGIT_6,
      Utf8.DIGIT_7,
      Utf8.DIGIT_8,
      Utf8.DIGIT_9,
  };

  @Test
  public void parseInt() {
    try {
      Utf8.parseInt((byte) '\0');
      Assert.fail();
    } catch (NumberFormatException expected) {

    }

    for (int i = 0; i < utf8Digits.length; i++) {
      byte d;
      d = utf8Digits[i];

      assertEquals(Utf8.parseInt(d), i);
    }

    try {
      Utf8.parseInt((byte) 0x20);
      Assert.fail();
    } catch (NumberFormatException expected) {

    }
  }

}