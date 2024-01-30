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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import objectos.lang.object.Check;

class SocketInput {

  private static final int HARD_MAX_BUFFER_SIZE = 1 << 14;

  byte[] buffer;

  int bufferIndex;

  // also serve as initialBufferSize
  int bufferLimit = 1024;

  private InputStream inputStream;

  int lineLimit;

  int maxBufferSize = 4096;

  public SocketInput() {
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

    if (n >= HARD_MAX_BUFFER_SIZE) {
      return HARD_MAX_BUFFER_SIZE;
    }

    return n + 1;
  }

  public void bufferSize(int initial, int max) {
    bufferLimit = powerOfTwo(initial);

    this.maxBufferSize = powerOfTwo(max);
  }

  final void initSocketInput(InputStream inputStream) {
    Check.state(buffer == null, "SocketInit already initialized");

    buffer = new byte[bufferLimit];

    this.inputStream = inputStream;

    resetSocketInput();
  }

  final void resetSocketInput() {
    bufferLimit = 0;

    bufferIndex = 0;

    lineLimit = 0;
  }

  final void parseLine() throws IOException {
    int startIndex;
    startIndex = bufferIndex;

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

        if (buffer.length == maxBufferSize) {
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

  final boolean matches(byte[] bytes) {
    int length;
    length = bytes.length;

    int toIndex;
    toIndex = bufferIndex + length;

    if (toIndex >= lineLimit) {
      // outside of line...
      return false;
    }

    boolean matches;
    matches = Arrays.equals(
        buffer, bufferIndex, toIndex,
        bytes, 0, length
    );

    if (matches) {
      bufferIndex += length;

      return true;
    } else {
      return false;
    }
  }

  final int indexOf(byte needle) {
    for (int i = bufferIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needle) {
        return i;
      }
    }

    return -1;
  }

  final int indexOf(byte needleA, byte needleB) {
    for (int i = bufferIndex; i < bufferLimit; i++) {
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

  final boolean consumeIfEndOfLine() {
    if (bufferIndex < lineLimit) {
      byte next;
      next = buffer[bufferIndex++];

      if (next != Bytes.CR) {
        return false;
      }
    }

    if (bufferIndex != lineLimit) {
      return false;
    }

    // index immediately after LF
    bufferIndex++;

    return true;
  }

  final String bufferToString(int start, int end) {
    int length;
    length = end - start;

    return new String(buffer, start, length, StandardCharsets.UTF_8);
  }

  final boolean consumeIfEmptyLine() {
    int length;
    length = lineLimit - bufferIndex;

    if (length == 0) {
      bufferIndex++;

      return true;
    }

    if (length == 1) {
      byte cr;
      cr = buffer[bufferIndex];

      if (cr == Bytes.CR) {
        bufferIndex += 2;

        return true;
      }
    }

    return false;
  }

  final byte get(int index) {
    return buffer[index];
  }

  final boolean canBuffer(long contentLength) {
    int maxAvailable;
    maxAvailable = maxBufferSize - bufferIndex;

    return maxAvailable >= contentLength;
  }

  final int read(long contentLength) throws IOException {
    // unread bytes in buffer
    int unread;
    unread = bufferLimit - bufferIndex;

    if (unread >= contentLength) {
      // everything is in the buffer already -> do not read

      return 0;
    }

    // we assume canBuffer was invoked before this method...
    // i.e. max buffer size can hold everything
    int length;
    length = (int) contentLength;

    int requiredBufferLength;
    requiredBufferLength = bufferIndex + length;

    // must we increase our buffer?

    if (requiredBufferLength > buffer.length) {
      int newLength;
      newLength = powerOfTwo(requiredBufferLength);

      buffer = Arrays.copyOf(buffer, newLength);
    }

    // how many bytes must we read

    int mustReadCount;
    mustReadCount = length - unread;

    while (mustReadCount > 0) {
      int read;
      read = inputStream.read(buffer, bufferLimit, mustReadCount);

      if (read < 0) {
        return -1;
      }

      bufferLimit += read;

      mustReadCount -= read;
    }

    return length;
  }

  final InputStream openStreamImpl() {
    int length;
    length = bufferLimit - bufferIndex;

    return new ByteArrayInputStream(buffer, bufferIndex, length);
  }

}