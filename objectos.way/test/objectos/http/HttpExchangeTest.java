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

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import objectos.http.internal.Http001;
import objectos.http.internal.TestableSocket;
import objectos.http.internal.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test
  public void http001() throws IOException {
    RegularInput input;
    input = Http001.INPUT;

    TestableSocket socket;
    socket = TestableSocket.of(input);

    try (HttpExchange exchange = HttpExchange.of(socket)) {
      assertTrue(exchange.keepAlive());
    }
  }

}