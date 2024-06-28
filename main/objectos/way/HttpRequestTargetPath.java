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
import java.util.List;
import java.util.Map;
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableMap;

final class HttpRequestTargetPath implements Http.Request.Target.Path {

  private String value;

  private List<Segment> segments;

  Map<String, String> variables;

  private int matcherIndex;

  public final void reset() {
    value = null;

    if (segments != null) {
      segments.clear();
    }

    if (variables != null) {
      variables.clear();
    }
  }

  public final void set(String rawValue) {
    value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);

    if (segments != null) {
      segments.clear();
    }
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
  public final String get(String name) {
    String result;
    result = null;

    if (variables != null) {
      result = variables.get(name);
    }

    return result;
  }

  @Override
  public final List<Segment> segments() {
    if (segments == null) {
      segments = new GrowableList<>();
    }

    if (segments.isEmpty()) {
      createSegments();
    }

    return segments;
  }

  private void createSegments() {
    // path is guaranteed to be, at a minimum, the '/' path
    int startIndex;
    startIndex = 1;

    while (true) {
      int endIndex;
      endIndex = value.indexOf('/', startIndex);

      if (endIndex < 0) {
        ThisSegment segment;
        segment = new ThisSegment(startIndex, value.length());

        segments.add(segment);

        break;
      }

      ThisSegment segment;
      segment = new ThisSegment(startIndex, endIndex);

      segments.add(segment);

      startIndex = endIndex + 1;
    }
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

  private class ThisSegment implements Segment {

    private final int start;

    private final int end;

    private String value;

    public ThisSegment(int start, int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public final boolean is(String other) {
      return value().equals(other);
    }

    @Override
    public final String toString() {
      return value();
    }

    @Override
    public final String value() {
      if (value == null) {
        value = HttpRequestTargetPath.this.value.substring(start, end);
      }

      return value;
    }

  }

}