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
import objectos.util.list.GrowableList;

final class HttpRequestTargetPath implements Http.Request.Target.Path {

  private String value;

  private List<Segment> segments;

  public final void reset() {
    value = null;

    if (segments != null) {
      segments.clear();
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