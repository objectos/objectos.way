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
package objectos.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import objectos.util.UnmodifiableList;

final class TestableSocket extends Socket {

  private byte[] requestBytes;

  private int requestIndex;

  public final void clear() {
    requestBytes = null;

    requestIndex = -1;
  }

  @Override
  public final InputStream getInputStream() throws IOException {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        if (requestIndex < requestBytes.length) {
          return requestBytes[requestIndex++];
        } else {
          return -1;
        }
      }
    };
  }

  public final void setRequest(UnmodifiableList<String> request) {
    String text;
    text = request.join(Http.CRLF);

    requestBytes = text.getBytes(StandardCharsets.UTF_8);

    requestIndex = 0;
  }

}