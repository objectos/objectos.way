/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HeaderNameTest {

  @Test(description = """
  It should be possible to create a custom/unknown header name
  """)
  public void testCase01() {
    HeaderName res;
    res = HeaderName.create("Foo");

    assertEquals(res.capitalized(), "Foo");
    assertEquals(res.index(), -1);
  }

  @Test(description = """
  It should return StandardHeaderName for known header names
  """)
  public void testCase02() {
    HeaderName res;
    res = HeaderName.create("Connection");

    assertEquals(res.capitalized(), "Connection");
    assertEquals(res.index() >= 0, true);
  }

}