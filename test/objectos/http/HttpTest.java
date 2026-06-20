/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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

import objectox.http.Rfc;
import org.testng.annotations.Test;

public class HttpTest {

  @Test
  public void parseRequestTarget01() {
    HttpExchange target;
    target = HttpExchange.create(cfg -> cfg.path("/"));

    assertEquals(target.path(), "/");
  }

  @Test
  public void requiredHexDigits() {
    assertEquals(Rfc.requiredHexDigits(0b0000), 1);
    assertEquals(Rfc.requiredHexDigits(0b0001), 1);
    assertEquals(Rfc.requiredHexDigits(0b1000), 1);
    assertEquals(Rfc.requiredHexDigits(0b1111), 1);
    assertEquals(Rfc.requiredHexDigits(0b1_0000), 2);
  }

}