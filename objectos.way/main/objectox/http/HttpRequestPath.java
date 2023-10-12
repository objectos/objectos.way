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

  @Override
  public final String toString() {
    Check.state(slashIndex > 0, "no slashs were defined");

    int start;
    start = slash[0];

    return new String(buffer, start, end - start, StandardCharsets.UTF_8);
  }

}