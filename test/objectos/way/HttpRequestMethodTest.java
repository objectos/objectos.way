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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class HttpRequestMethodTest {

  @Test
  public void is() {
    assertTrue(Http.GET.is(Http.GET));
    assertTrue(Http.GET.is(Http.GET, Http.POST));
    assertTrue(Http.GET.is(Http.POST, Http.GET));

    assertFalse(Http.GET.is(Http.POST));
    assertFalse(Http.GET.is(Http.DELETE, Http.POST));
    assertFalse(Http.GET.is(Http.POST, Http.DELETE));
  }

  @Test
  public void text() {
    assertEquals(Http.GET.text(), "GET");
    assertEquals(Http.HEAD.text(), "HEAD");
    assertEquals(Http.POST.text(), "POST");
    assertEquals(Http.PUT.text(), "PUT");
    assertEquals(Http.DELETE.text(), "DELETE");
  }

}