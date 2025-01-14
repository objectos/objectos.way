/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.function.Consumer;
import objectos.way.Http.Exchange;
import objectos.way.HttpExchange.ParseStatus;

public final class WayTestingServerExchange implements TestingServerExchange {

  private int bufferSizeInitial = 1024;

  private int bufferSizeMax = 4096;

  private Clock clock = Clock.systemUTC();

  private Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  public WayTestingServerExchange() {}

  public final void bufferSize(int initial, int max) {
    Check.argument(initial >= 128, "initial size must be >= 128");
    Check.argument(max >= 128, "max size must be >= 128");
    Check.argument(max >= initial, "max size must be >= initial size");

    this.bufferSizeInitial = initial;
    this.bufferSizeMax = max;
  }

  public final void clock(Clock clock) {
    this.clock = Check.notNull(clock, "clock == null");
  }

  public final void noteSink(Note.Sink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");
  }

  @Override
  public final String handle(String request, Consumer<Exchange> handler) {
    byte[] bytes;
    bytes = request.getBytes(StandardCharsets.UTF_8);

    try (
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TestingSocket socket = new TestingSocket(inputStream);
        HttpExchange loop = new HttpExchange(socket, bufferSizeInitial, bufferSizeMax, clock, noteSink)
    ) {
      ParseStatus parse;
      parse = loop.parse();

      if (parse.isError()) {
        throw new UnsupportedOperationException("Bad request");
      }

      try {
        handler.accept(loop);
      } catch (Throwable t) {
        loop.internalServerError(t);
      }

      return socket.toString();
    } catch (IOException e) {
      throw new UncheckedIOException("Unexpected IOException: testing server exchange executed in-memory", e);
    }
  }

  private static class TestingSocket extends Socket {

    private final InputStream inputStream;

    private ByteArrayOutputStream outputStream;

    public TestingSocket(InputStream inputStream) {
      this.inputStream = inputStream;
    }

    @Override
    public final InputStream getInputStream() throws IOException {
      return inputStream;
    }

    @Override
    public final OutputStream getOutputStream() throws IOException {
      if (outputStream == null) {
        outputStream = new ByteArrayOutputStream();
      }

      return outputStream;
    }

    @Override
    public final String toString() {
      byte[] bytes;
      bytes = outputStream.toByteArray();

      return new String(bytes, StandardCharsets.UTF_8);
    }

  }

}