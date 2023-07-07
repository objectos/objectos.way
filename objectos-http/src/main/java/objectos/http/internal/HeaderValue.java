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
package objectos.http.internal;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import objectos.util.ByteArrays;

record HeaderValue(byte[] buffer, int start, int end) {

  public static final HeaderValue EMPTY = new HeaderValue(ByteArrays.empty(), 0, 0);

  public final boolean contentEquals(byte[] that) {
    int thisLength;
    thisLength = end - start;

    if (thisLength != that.length) {
      return false;
    }

    for (int offset = 0; offset < thisLength; offset++) {
      byte ch;
      ch = buffer[start + offset];

      byte thisLow;
      thisLow = Bytes.toLowerCase(ch);

      byte thatLow;
      thatLow = that[offset];

      if (thisLow != thatLow) {
        return false;
      }
    }

    return true;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this ||
        obj instanceof HeaderValue that
            && Arrays.equals(buffer, start, end, that.buffer, that.start, that.end);
  }

  @Override
  public final String toString() {
    return new String(buffer, start, end - start, StandardCharsets.UTF_8);
  }

}