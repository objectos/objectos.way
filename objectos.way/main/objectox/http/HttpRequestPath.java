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

final class HttpRequestPath {

  final byte[] buffer;

  final int start;

  int end;

  int[] segments = new int[10];

  private int segmentsIndex;

  private int segmentsSize;

  public HttpRequestPath(byte[] buffer, int start, int end) {
    this.buffer = buffer;
    this.start = start;
    this.end = end;
  }

  public HttpRequestPath(byte[] buffer, int start) {
    this.buffer = buffer;
    this.start = start;
  }

  public final void end(int value) {
    end = value;
  }

  public final void segment(int start, int end) {
    int startIndex;
    startIndex = segmentsSize * 2;

    int endIndex;
    endIndex = startIndex + 1;

    segmentsSize++;

    segments = IntArrays.growIfNecessary(segments, endIndex);

    segments[startIndex] = start;

    segments[endIndex] = end;
  }

  public final String nextSegment() {
    if (segmentsIndex == segmentsSize) {
      return null;
    }

    int offset;
    offset = segmentsIndex * 2;

    segmentsIndex++;

    int start;
    start = segments[offset + 0];

    int end;
    end = segments[offset + 1];

    return new String(buffer, start, end - start, StandardCharsets.UTF_8);
  }

  @Override
  public final String toString() {
    return new String(buffer, start, end - start, StandardCharsets.UTF_8);
  }

}