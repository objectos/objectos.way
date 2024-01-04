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
import java.nio.charset.StandardCharsets;
import objectos.http.server.UriPath;

public final class ObjectoxUriPath implements UriPath {

  private String value;

  public final void reset() {
    value = null;
  }

  public final void set(String rawValue) {
    value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);
  }

  @Override
  public final boolean is(String path) {
    return value.equals(path);
  }

  @Override
  public final boolean startsWith(String prefix) {
    return value.startsWith(prefix);
  }

  @Override
  public final String toString() {
    return value;
  }

}