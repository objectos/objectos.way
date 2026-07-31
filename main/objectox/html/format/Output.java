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
package objectox.html.format;

public final class Output {

  private final char[] buffer;

  private int index;

  private final int limit;

  private final StringBuilder sb = new StringBuilder();

  private int written;

  private Output(char[] buffer, int index, int limit) {
    this.buffer = buffer;

    this.index = index;

    this.limit = limit;
  }

  public static Output wrap(char[] cbuf, int off, int len) {
    final int limit;
    limit = off + len;

    return new Output(cbuf, off, limit);
  }

  public final void append(char c) {
    written += 1;

    if (index < limit) {
      buffer[index++] = c;
    } else {
      sb.append(c);
    }
  }

  public final void append(CharSequence csq) {
    final CharSequence s;
    s = normalize(csq);

    final int len;
    len = s.length();

    written += len;

    final int remaining;
    remaining = limit - index;

    if (len <= remaining) {
      s.getChars(0, len, buffer, index);

      index += len;
    } else {
      s.getChars(0, remaining, buffer, index);

      index = limit;

      sb.append(s, remaining, len);
    }
  }

  private CharSequence normalize(CharSequence csq) {
    return csq != null ? csq : "null";
  }

  public final boolean exhausted() {
    return index == limit;
  }

  public final boolean startOfLine() {
    if (written == 0) {
      return true;
    }

    final char lastChar;

    if (sb.length() > 0) {
      final int lastIndex;
      lastIndex = sb.length() - 1;

      lastChar = sb.charAt(lastIndex);
    } else {
      final int lastIndex;
      lastIndex = index - 1;

      lastChar = buffer[lastIndex];
    }

    return lastChar == '\n' || lastChar == '\r';
  }

  public final String overflow() {
    return sb.toString();
  }

  public final int written() {
    return written == 0 ? -1 : written;
  }

  @Override
  public final String toString() {
    final int overflowLen;
    overflowLen = sb.length();

    final int bufferLen;
    bufferLen = written - overflowLen;

    final int startIndex;
    startIndex = index - bufferLen;

    final String fromBuffer;
    fromBuffer = new String(buffer, startIndex, bufferLen);

    if (overflowLen == 0) {
      return fromBuffer;
    }

    else {
      return fromBuffer + overflow();
    }
  }

}