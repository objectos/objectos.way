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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class MethodTest {

  @Test
  public void is() {
    assertTrue(Method.GET.is(Method.GET));
    assertTrue(Method.GET.is(Method.GET, Method.POST));
    assertTrue(Method.GET.is(Method.POST, Method.GET));

    assertFalse(Method.GET.is(Method.POST));
    assertFalse(Method.GET.is(Method.DELETE, Method.POST));
    assertFalse(Method.GET.is(Method.POST, Method.DELETE));
  }

  @Test
  public void text() {
    assertEquals(Method.GET.text(), "GET");
    assertEquals(Method.HEAD.text(), "HEAD");
    assertEquals(Method.POST.text(), "POST");
    assertEquals(Method.PUT.text(), "PUT");
    assertEquals(Method.DELETE.text(), "DELETE");
  }

}