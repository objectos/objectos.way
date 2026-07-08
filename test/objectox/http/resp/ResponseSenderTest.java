/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.resp;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import objectos.http.Status;
import objectos.y.OutputStreamY;
import org.testng.annotations.Test;

public class ResponseSenderTest {

  @Test
  public void send01() throws IOException {
    assertEquals(
        ResponseSenderY.send(
            opts -> {
              opts.bufferSize = 64;

              opts.outputStream = OutputStreamY.create();
            },

            opts -> {
              opts.status(Status.OK);
            }
        ),

        """
        HTTP/1.1 200 OK\r
        \r
        """
    );
  }

  @Test(description = "send(bytes)")
  public void send02() throws IOException {
    final byte[] bytes;
    bytes = "send(bytes)".getBytes(StandardCharsets.US_ASCII);

    assertEquals(
        ResponseSenderY.send(
            opts -> {
              opts.bufferSize = 64;

              opts.outputStream = OutputStreamY.create();
            },

            opts -> {
              opts.status(Status.OK);

              opts.send(bytes);
            }
        ),

        """
        HTTP/1.1 200 OK\r
        Content-Length: 11\r
        \r
        send(bytes)\
        """
    );
  }

}
