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
import java.util.Arrays;

final class SocketInput {

  private final byte[] buffer;

  private final InputStream inputStream;

  private int bufferLimit;

  private int lineIndex;

  private int lineLimit;

  SocketInput(int bufferSize, InputStream inputStream) {
    buffer = new byte[bufferSize];

    this.inputStream = inputStream;
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
        // buffer is full
        throw new OverflowException();
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

  final HttpRequestPath createPath(int targetStart) {
    return new HttpRequestPath(buffer, targetStart);
  }

}