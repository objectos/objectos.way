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

import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;
import objectos.http.server.ServerResponse;
import objectox.http.HttpStatus;

public class ObjectoxServerResponse implements ServerResponse {

  static final Map<HttpStatus, byte[]> HTTP_STATUS_RESPONSE_BYTES;

  static {
    Map<HttpStatus, byte[]> map;
    map = new EnumMap<>(HttpStatus.class);

    for (var status : HttpStatus.values()) {
      String response;
      response = Integer.toString(status.code()) + " " + status.description() + "\r\n";

      byte[] responseBytes;
      responseBytes = Bytes.utf8(response);

      map.put(status, responseBytes);
    }

    HTTP_STATUS_RESPONSE_BYTES = map;
  }

  @SuppressWarnings("unused")
  private final byte[] buffer;

  @SuppressWarnings("unused")
  private final OutputStream outputStream;

  public ObjectoxServerResponse(byte[] buffer, OutputStream outputStream) {
    this.buffer = buffer;

    this.outputStream = outputStream;
  }

  @Override
  public final ServerResponse ok() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public ServerResponse contentLength(long value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public ServerResponse contentType(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public ServerResponse dateNow() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void send(byte[] body) {
    throw new UnsupportedOperationException("Implement me");
  }

}