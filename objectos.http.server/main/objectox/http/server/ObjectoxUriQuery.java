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

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import objectos.http.server.UriQuery;

public final class ObjectoxUriQuery implements UriQuery {

  private final byte[] buffer;

  final int startIndex;

  private int length;

  private String value;

  public ObjectoxUriQuery(byte[] buffer, int startIndex) {
    this.buffer = buffer;

    this.startIndex = startIndex;
  }

  @Override
  public final String get(String name) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final String value() {
    if (value == null) {
      Charset charset;
      charset = StandardCharsets.UTF_8;

      String raw;
      raw = new String(buffer, startIndex, length, charset);

      value = URLDecoder.decode(raw, charset);
    }

    return value;
  }

  final void end(int endIndex) {
    this.length = endIndex - startIndex;
  }

}