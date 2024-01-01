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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import objectos.http.server.ServerRequestBody;

public final class ObjectoxHttpServer {

  /*
   HTTP 001: Minimal GET request

   -- request
   GET / HTTP/1.1\r
   Host: www.example.com\r
   Connection: close\r
   \r
   ---

   --- response
   HTTP/1.1 200 OK<CRLF>
   Content-Type: text/plain; charset=utf-8<CRLF>
   Content-Length: 13<CRLF>
   Date: Wed, 28 Jun 2023 12:08:43 GMT<CRLF>
   <CRLF>
   Hello World!
   ---
   */

  private ObjectoxHttpServer() {}

  public static byte[] readAllBytes(ServerRequestBody body) throws IOException {
    try (InputStream in = body.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      return out.toByteArray();
    }
  }

}