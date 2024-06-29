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
package objectos.way;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import objectos.util.map.GrowableMap;

final class HttpRequestTargetPath implements Http.Request.Target.Path {

  private int matcherIndex;

  private String value;

  Map<String, String> variables;

  public final void reset() {
    value = null;

    if (variables != null) {
      variables.clear();
    }
  }

  public final void set(String rawValue) {
    value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);
  }

  @Override
  public final String get(String name) {
    String result;
    result = null;

    if (variables != null) {
      result = variables.get(name);
    }

    return result;
  }

  @Override
  public final String toString() {
    return value;
  }

  @Override
  public final String value() {
    return value;
  }

  final void matcherReset() {
    matcherIndex = 0;

    if (variables != null) {
      variables.clear();
    }
  }

  final boolean atEnd() {
    return matcherIndex == value.length();
  }

  final boolean exact(String other) {
    boolean result;
    result = value.equals(other);

    matcherIndex += value.length();

    return result;
  }

  final boolean namedVariable(String name) {
    int solidus;
    solidus = value.indexOf('/', matcherIndex);

    String varValue;

    if (solidus < 0) {
      varValue = value.substring(matcherIndex);
    } else {
      varValue = value.substring(matcherIndex, solidus);
    }

    matcherIndex += varValue.length();

    variable(name, varValue);

    return true;
  }

  final boolean region(String region) {
    boolean result;
    result = value.regionMatches(matcherIndex, region, 0, region.length());

    matcherIndex += region.length();

    return result;
  }

  final boolean startsWithMatcher(String prefix) {
    boolean result;
    result = value.startsWith(prefix);

    matcherIndex += prefix.length();

    return result;
  }

  private void variable(String name, String value) {
    if (variables == null) {
      variables = new GrowableMap<>();
    }

    variables.put(name, value);
  }

}