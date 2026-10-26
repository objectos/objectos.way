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

import java.io.IOException;

public final class Output implements Appendable {

  private char[] buffer;

  private int index;

  private int limit;

  private int level;

  private final StringBuilder sb = new StringBuilder();

  private int written;

  public Output() {}

  public Output(char[] buffer) {
    this.buffer = buffer;

    limit = buffer.length;
  }

  @Override
  public final Output append(char c) throws IOException {
    if (index < limit) {
      buffer[index++] = c;

      written += 1;
    } else {
      sb.append(c);
    }

    return this;
  }

  @Override
  public final Output append(CharSequence csq) throws IOException {
    final int len;
    len = csq.length();

    final int remaining;
    remaining = limit - index;

    if (len <= remaining) {
      csq.getChars(0, len, buffer, index);

      index += len;

      written += len;
    } else {
      csq.getChars(0, remaining, buffer, index);

      index = limit;

      written += remaining;

      sb.append(csq, remaining, len);
    }

    return this;
  }

  @Override
  public final Output append(CharSequence csq, int start, int end) throws IOException {
    final int len;
    len = end - start;

    final int remaining;
    remaining = limit - index;

    if (len <= remaining) {
      csq.getChars(start, end, buffer, index);

      index += len;

      written += len;
    } else {
      final int mid;
      mid = start + remaining;

      csq.getChars(start, mid, buffer, index);

      index = limit;

      written += remaining;

      sb.append(csq, mid, end);
    }

    return this;
  }

  public final void dec() {
    if (level == 0) {
      final String msg;
      msg = "Cannot decrement indentation level below zero";

      throw new IllegalStateException(msg);
    }

    level--;
  }

  public final boolean exhausted() {
    return index == limit;
  }

  public final void inc() {
    level++;
  }

  public final int level() {
    return level;
  }

  public final String overflow() {
    return sb.toString();
  }

  public final void rewrap(char[] cbuf, int off, int len) throws IOException {
    final String overflow;
    overflow = sb.toString();

    sb.setLength(0);

    buffer = cbuf;

    index = off;

    limit = off + len;

    written = 0;

    append(overflow);
  }

  public final int written() {
    return written == 0 ? -1 : written;
  }

  @Override
  public final String toString() {
    final int bufferLen;
    bufferLen = written;

    final int startIndex;
    startIndex = index - bufferLen;

    final String fromBuffer;
    fromBuffer = new String(buffer, startIndex, bufferLen);

    final int overflowLen;
    overflowLen = sb.length();

    if (overflowLen == 0) {
      return fromBuffer;
    }

    else {
      return fromBuffer + overflow();
    }
  }

}