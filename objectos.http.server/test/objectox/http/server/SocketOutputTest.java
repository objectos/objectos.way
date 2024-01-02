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
package objectox.http.server;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.Socket;
import objectox.http.HttpStatus;
import org.testng.annotations.Test;

public class SocketOutputTest {

  @Test
  public void testCase001() throws IOException {
    TestableSocket socket;
    socket = TestableSocket.empty();

    SocketOutput output;
    output = create(socket);

    output.writeBytes(Version.HTTP_1_1.responseBytes);
    output.writeBytes(ObjectoxServerResponse.HTTP_STATUS_RESPONSE_BYTES.get(HttpStatus.OK));

    output.writeUtf8("Content-Type");
    output.writeBytes(Bytes.COLONSP);
    output.writeUtf8("text/plain; charset=utf-8");
    output.writeBytes(Bytes.CRLF);

    output.writeUtf8("Content-Length");
    output.writeBytes(Bytes.COLONSP);
    output.writeUtf8("14");
    output.writeBytes(Bytes.CRLF);

    output.writeUtf8("Date");
    output.writeBytes(Bytes.COLONSP);
    output.writeUtf8("Wed, 28 Jun 2023 12:08:43 GMT");
    output.writeBytes(Bytes.CRLF);

    output.writeBytes(Bytes.CRLF);

    output.writeUtf8("Hello World!\n");

    output.flush();

    assertEquals(
        socket.outputAsString(),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 14\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        Hello World!
        """
    );
  }

  private SocketOutput create(Socket socket) throws IOException {
    SocketInput input;
    input = new SocketInput(socket).init(32);

    return input.toOutput();
  }

}