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
package objectos.http.internal;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HeaderValueTest {

  @Test
  public void unsignedLongValue() {
    HeaderValue hv;
    hv = TestingInput.hv("9223372036854775807");

    assertEquals(hv.unsignedLongValue(), 9_223_372_036_854_775_807L);

    hv = TestingInput.hv("0");

    assertEquals(hv.unsignedLongValue(), 0L);

    hv = TestingInput.hv("123456789");

    assertEquals(hv.unsignedLongValue(), 123456789L);

    hv = TestingInput.hv("-123");

    assertEquals(hv.unsignedLongValue(), Long.MIN_VALUE);

    hv = TestingInput.hv("9999999999999999999");

    assertEquals(hv.unsignedLongValue(), Long.MIN_VALUE);

    hv = TestingInput.hv("92233720368547758079223372036854775807");

    assertEquals(hv.unsignedLongValue(), Long.MIN_VALUE);
  }

}