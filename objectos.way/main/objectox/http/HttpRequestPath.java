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
package objectox.http;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import objectos.http.Segment;
import objectos.util.IntArrays;
import objectox.lang.Check;

final class HttpRequestPath {

  final byte[] buffer;

  int[] slash = new int[10];

  private int slashIndex;

  private int end;

  public HttpRequestPath(byte[] buffer) {
    this.buffer = buffer;
  }

  public final void slash(int index) {
    slash = IntArrays.growIfNecessary(slash, slashIndex);

    slash[slashIndex++] = index;
  }

  public final void end(int index) {
    end = index;
  }

  public final boolean matches(Segment s0) {
    if (segmentCount() == 1) {
      return match(s0, segment(0));
    }

    return false;
  }

  public final boolean matches(Segment s0, Segment s1) {
    if (segmentCount() == 2) {
      return match(s0, segment(0))
          && match(s1, segment(1));
    }

    return false;
  }

  public final String segment(int index) {
    Check.state(slashIndex > 0, "no slashs were defined");

    if (index < 0 || index >= slashIndex) {
      throw new IndexOutOfBoundsException(
          "Index out of range: " + index + "; valid values: 0 <= index < " + slashIndex
      );
    }

    int segmentStart;
    segmentStart = slash[index] + 1;

    int segmentEnd;

    int nextIndex;
    nextIndex = index + 1;

    if (nextIndex == slashIndex) {
      segmentEnd = end;
    } else {
      segmentEnd = slash[nextIndex];
    }

    return new String(buffer, segmentStart, segmentEnd - segmentStart, StandardCharsets.UTF_8);
  }

  public final int segmentCount() {
    return slashIndex;
  }

  public final Path toPath() {
    Check.state(slashIndex > 0, "no slashs were defined");

    return switch (slashIndex) {
      case 1 -> Path.of(segment(0));

      case 2 -> Path.of(segment(0), segment(1));

      default -> {
        String[] rest;
        rest = new String[slashIndex - 1];

        for (int i = 1; i < slashIndex; i++) {
          rest[i - 1] = segment(i);
        }

        yield Path.of(segment(0), rest);
      }
    };
  }

  @Override
  public final String toString() {
    Check.state(slashIndex > 0, "no slashs were defined");

    int start;
    start = slash[0];

    return new String(buffer, start, end - start, StandardCharsets.UTF_8);
  }

  private boolean match(Segment segment, String value) {
    return switch (segment) {
      case SegmentExact exact -> exact.matches(value);

      case SegmentKind.ALWAYS_TRUE -> true;

      default -> false;
    };
  }

}