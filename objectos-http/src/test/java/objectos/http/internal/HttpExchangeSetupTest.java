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

public class HttpExchangeSetupTest {

  @Test(description = """
  [#426] HTTP 001: SETUP --> INPUT

  - buffer must be reset
  """)
  public void setup01() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.bufferIndex = -1;
    exchange.bufferLimit = -1;
    exchange.state = HttpExchange._SETUP;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.state, HttpExchange._INPUT);
  }

}