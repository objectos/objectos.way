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
package objectox.http.handler;

import java.util.HashMap;
import java.util.Map;
import objectos.http.Request;
import objectos.internal.VisibleForTesting;
import objectox.http.req.RequestPojo;

final class RequestPath {

  private final String path;

  private int index;

  private Map<String, String> params = Map.of();

  RequestPath(String path) {
    this.path = path;
  }

  @VisibleForTesting
  RequestPath(String path, int index) {
    this.path = path;

    this.index = index;
  }

  public final void clear() {
    if (!params.isEmpty()) {
      params.clear();
    }
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

  public final String substring() {
    final String value;
    value = path.substring(index);

    index = path.length();

    return value;
  }

  public final String substring(int endIndex) {
    final String value;
    value = path.substring(index, endIndex);

    // immediately after the terminator
    index = endIndex + 1;

    return value;
  }

  public final boolean param(String name, String value) {
    if (params.isEmpty()) {
      params = new HashMap<>();
    }

    params.put(name, value);

    return true;
  }

  public final void paramsIfNecessary(Request request) {
    if (params.isEmpty()) {
      return;
    }

    final Map<String, String> unmodifiable;
    unmodifiable = Map.copyOf(params);

    request.attr(RequestPojo.PATH_PARAMS, unmodifiable);
  }

  final Map<?, ?> params() {
    return params;
  }

}
