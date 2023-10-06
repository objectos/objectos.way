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
package objectox.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import objectox.lang.CharWritable;
import org.testng.annotations.Test;

public class HttpChunkedCharsTest {

  @Test
  public void http002() throws IOException {
    HttpExchange outer;
    outer = new HttpExchange();

    TestableSocket socket;
    socket = TestableSocket.empty();

    outer.buffer = new byte[512];
    outer.socket = socket;

    CharWritable entity = Http002.newEntity();

    HttpChunkedChars chunked;
    chunked = new HttpChunkedChars(outer, entity, StandardCharsets.UTF_8);

    chunked.write();

    assertEquals(socket.outputAsString(), Http002.BODY);
  }

}