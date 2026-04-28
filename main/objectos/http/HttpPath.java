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
package objectos.http;

import java.util.HashMap;
import java.util.Map;

final class HttpPath {

  private final String path;

  private int index;

  private Map<String, Object> params = Map.of();

  HttpPath(String path) {
    this.path = path;
  }

  public final int indexOf(char terminator) {
    return path.indexOf(terminator, index);
  }

  public final int length() {
    return path.length() - index;
  }

  public final boolean matches(String value) {
    final int length;
    length = value.length();

    final boolean matches;
    matches = path.regionMatches(index, value, 0, length);

    if (matches) {
      index += length;

      return true;
    } else {
      return false;
    }
  }

  public final void param(String name) {
    final String value;
    value = path.substring(index);

    index = path.length();

    if (params.isEmpty()) {
      params = new HashMap<>();
    }

    params.put(name, value);
  }

  public final void param(String name, int terminatorIndex) {
    final String value;
    value = path.substring(index, terminatorIndex);

    // immediately after the terminator
    index = terminatorIndex + 1;

    if (params.isEmpty()) {
      params = new HashMap<>();
    }

    params.put(name, value);
  }

}
