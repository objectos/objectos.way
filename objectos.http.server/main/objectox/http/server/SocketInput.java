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
package objectox.http.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

final class SocketInput {

  private static final int MAX_BUFFER_SIZE = 1 << 14;

  byte[] buffer;

  private final InputStream inputStream;

  private int bufferLimit;

  private int lineIndex;

  private int lineLimit;

  private final int max = 4096;

  public SocketInput(int bufferSize, InputStream inputStream) {
    int actualSize;
    actualSize = powerOfTwo(bufferSize);

    this.buffer = new byte[actualSize];

    this.inputStream = inputStream;
  }

  static final int powerOfTwo(int size) {
    // maybe size is already power of 2
    int x;
    x = size - 1;

    int leading;
    leading = Integer.numberOfLeadingZeros(x);

    int n;
    n = -1 >>> leading;

    if (n < 0) {
      // should not happen as minimal buffer size is 128
      throw new IllegalArgumentException("Buffer size is too small");
    }

    if (n >= MAX_BUFFER_SIZE) {
      return MAX_BUFFER_SIZE;
    }

    return n + 1;
  }

  public final void reset() {
    bufferLimit = 0;

    lineIndex = 0;

    lineLimit = 0;
  }

  public final void parseLine() throws IOException {
    int startIndex;
    startIndex = lineIndex;

    byte needle;
    needle = Bytes.LF;

    while (true) {
      for (int i = startIndex; i < bufferLimit; i++) {
        byte maybe;
        maybe = buffer[i];

        if (maybe == needle) {
          lineLimit = i;

          return;
        }
      }

      // not inside buffer
      // let's try to read more data
      startIndex = bufferLimit;

      int writableLength;
      writableLength = buffer.length - bufferLimit;

      if (writableLength == 0) {
        // buffer is full, try to increase

        if (buffer.length == max) {
          // cannot increase...
          throw new OverflowException();
        }

        int newLength;
        newLength = buffer.length << 1;

        buffer = Arrays.copyOf(buffer, newLength);

        writableLength = buffer.length - bufferLimit;
      }

      int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

      if (bytesRead < 0) {
        // EOF
        throw new EOFException();
      }

      bufferLimit += bytesRead;
    }
  }

  public final boolean hasNext() {
    return lineIndex < lineLimit;
  }

  public final boolean hasNext(int count) {
    int requiredIndex;
    requiredIndex = lineIndex + count - 1;

    return requiredIndex < lineLimit;
  }

  public final byte peek() {
    return buffer[lineIndex];
  }

  public final boolean matches(byte[] bytes) {
    int length;
    length = bytes.length;

    int toIndex;
    toIndex = lineIndex + length;

    if (toIndex >= lineLimit) {
      // outside of line...
      return false;
    }

    boolean matches;
    matches = Arrays.equals(
        buffer, lineIndex, toIndex,
        bytes, 0, length
    );

    if (matches) {
      lineIndex += length;

      return true;
    } else {
      return false;
    }
  }

  public final int index() {
    return lineIndex;
  }

  public final int indexOf(byte needle) {
    for (int i = lineIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needle) {
        return i;
      }
    }

    return -1;
  }

  public final int indexOf(byte needleA, byte needleB) {
    for (int i = lineIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needleA) {
        return i;
      }

      if (maybe == needleB) {
        return i;
      }
    }

    return -1;
  }

  public final byte setAndNext(int index) {
    lineIndex = index;

    return next();
  }

  public final byte next() {
    return buffer[lineIndex++];
  }

  public final boolean consumeIfEndOfLine() {
    if (lineIndex < lineLimit) {
      byte next;
      next = next();

      if (next != Bytes.CR) {
        return false;
      }
    }

    if (lineIndex != lineLimit) {
      return false;
    }

    // index immediately after LF
    lineIndex++;

    return true;
  }

  public final boolean consumeIfEmptyLine() {
    int length;
    length = lineLimit - lineIndex;

    if (length == 0) {
      lineIndex++;

      return true;
    }

    if (length == 1) {
      byte cr;
      cr = peek();

      if (cr == Bytes.CR) {
        lineIndex += 2;

        return true;
      }
    }

    return false;
  }

  public final int lineLimit() {
    return lineLimit;
  }

  public final byte get(int index) {
    return buffer[index];
  }

  public final void set(int value) {
    lineIndex = value;
  }

  public final String getString(int start, int end) {
    int length;
    length = end - start;

    return new String(buffer, start, length, StandardCharsets.UTF_8);
  }

  final HttpRequestPath createPath(int targetStart) {
    return new HttpRequestPath(buffer, targetStart);
  }

}