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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.time.Clock;
import java.util.function.Predicate;

final class AppNoteSinkOfFile extends AppNoteSink implements App.NoteSink.OfFile {

  private final ByteBuffer buffer;

  private final SeekableByteChannel channel;

  private boolean active;

  AppNoteSinkOfFile(Clock clock, Predicate<Note> filter, ByteBuffer buffer, SeekableByteChannel channel) {
    super(clock, filter);

    this.buffer = buffer;

    this.channel = channel;

    active = true;
  }

  @Override
  public final void close() throws IOException {
    w.lock();
    try {
      channel.close();
    } finally {
      active = false;

      w.unlock();
    }
  }

  @Override
  public final void rotate() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected final void writeBytes(byte[] bytes) {
    if (!active) {
      return;
    }

    int remaining;
    remaining = bytes.length;

    w.lock();
    try {
      while (remaining > 0) {
        buffer.clear();

        int length;
        length = Math.min(remaining, buffer.remaining());

        int offset;
        offset = bytes.length - remaining;

        buffer.put(bytes, offset, length);

        buffer.flip();

        while (buffer.hasRemaining()) {
          channel.write(buffer);
        }

        remaining -= length;
      }
    } catch (IOException e) {

      // not much we can do here

      e.printStackTrace();

      active = false;

    } finally {
      w.unlock();
    }
  }

}