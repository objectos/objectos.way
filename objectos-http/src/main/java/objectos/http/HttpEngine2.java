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
package objectos.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Date;
import objectos.lang.NoteSink;

final class HttpEngine2 implements HttpResponseHandle, Runnable {

  private byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private final int bufferSize;

  private boolean eof;

  @SuppressWarnings("unused")
  private Throwable error;

  private Method method;

  @SuppressWarnings("unused")
  private final NoteSink noteSink;

  private final HttpProcessor processor;

  private final Socket socket;

  private RequestTarget requestTarget;

  HttpEngine2(int bufferSize,
              NoteSink noteSink,
              HttpProcessor processor,
              Socket socket) {
    this.bufferSize = bufferSize;

    this.noteSink = noteSink;

    this.processor = processor;

    this.socket = socket;
  }

  // public stuff

  @Override
  public final String formatDate(Date date) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final String formatDate(long millis) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final ByteBuffer getByteBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final WritableByteChannel getChannel() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final CharBuffer getCharBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final CharsetEncoder getEncoder(Charset charset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final StringBuilder getStringBuilder() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void run() {
    processor.requestStart(this);

    Closeable c;
    c = socket;

    try (c) {
      parseRequest();
    } catch (IOException e) {
      error = e;
    }
  }

  // non-public stuff

  private boolean bufferEquals(byte[] target, int start, int end) {
    return Arrays.equals(
      buffer, start, end,
      target, 0, target.length
    );
  }

  private void inputFill() throws IOException {
    InputStream inputStream;
    inputStream = socket.getInputStream();

    while (!eof && bufferLimit < buffer.length) {
      int bufferWritable;
      bufferWritable = buffer.length - bufferLimit;

      int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, bufferWritable);

      if (bytesRead < 0) {
        eof = true;
      } else {
        bufferLimit += bytesRead;
      }
    }
  }

  private int inputIndexOf(byte target) {
    for (; bufferIndex < bufferLimit; bufferIndex++) {
      if (buffer[bufferIndex] == target) {
        return bufferIndex++;
      }
    }

    return -1;
  }

  private byte inputNext() {
    return buffer[bufferIndex++];
  }

  private void parseRequest() throws IOException {
    parseRequestInit();

    parseRequestLine();
  }

  private void parseRequestInit() throws IOException {
    buffer = new byte[bufferSize];

    inputFill();
  }

  /*
   * The buffer must be large enough to hold the full request line.
   */
  private void parseRequestLine() {
    // find indices

    int methodStart, methodEnd;

    methodStart = bufferIndex;

    methodEnd = inputIndexOf(Http.SP_BYTE);

    if (methodEnd < 0) {
      result(Status.BAD_REQUEST);

      return;
    }

    int targetStart, targetEnd;

    targetStart = methodEnd + 1;

    targetEnd = inputIndexOf(Http.SP_BYTE);

    if (targetEnd < 0) {
      result(Status.URI_TOO_LONG);

      return;
    }

    int versionStart, versionEnd;

    versionStart = targetEnd + 1;

    versionEnd = inputIndexOf(Http.CR_BYTE);

    if (versionEnd < 0 || inputNext() != Http.LF_BYTE) {
      result(Status.BAD_REQUEST);

      return;
    }

    // parse method

    byte methodFirst;
    methodFirst = buffer[methodStart];

    Method maybeMethod;
    maybeMethod = switch (methodFirst) {
      case 'G' -> Method.GET;

      default -> null;
    };

    if (maybeMethod == null) {
      result(Status.NOT_IMPLEMENTED);

      return;
    }

    byte[] methodBytes;
    methodBytes = maybeMethod.bytes;

    if (!bufferEquals(methodBytes, methodStart, methodEnd)) {
      result(Status.NOT_IMPLEMENTED);

      return;
    }

    method = maybeMethod;

    // parse target

    byte[] targetBytes;
    targetBytes = Arrays.copyOfRange(buffer, targetStart, targetEnd);

    requestTarget = new ThisRequestTarget(targetBytes);

    // parse version

    if (!bufferEquals(Version.V1_1.bytes, versionStart, versionEnd)) {
      result(Status.HTTP_VERSION_NOT_SUPPORTED);

      return;
    }

    processor.requestLine(method, requestTarget, Version.V1_1);
  }

  private void result(Status status) {
    throw new UnsupportedOperationException("Implement me");
  }

  private static class ThisRequestTarget implements RequestTarget {

    private final byte[] bytes;

    public ThisRequestTarget(byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    public final boolean pathEquals(String string) {
      return new String(bytes).equals(string);
    }

  }

}