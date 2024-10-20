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
package objectos.notes.impl;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import objectos.notes.Level;
import objectos.notes.Note2;

/**
 * A {@link objectos.notes.NoteSink} implementation that writes out notes to a
 * regular file.
 */
public final class FileNoteSink extends AbstractNoteSink implements Closeable {

  /**
   * A note indicating that the instance has started successfully.
   */
  public static final Note2<Path, Level> STARTED = Note2.info(FileNoteSink.class, "Started");

  private final Path file;

  private final Lock lock = new ReentrantLock();

  private WritableByteChannel channel;

  private ByteBuffer buffer;

  private volatile boolean active;

  public FileNoteSink(Path file, Level level) {
    super(Objects.requireNonNull(level, "level == null"));

    this.file = Objects.requireNonNull(file, "file == null");
  }

  public final FileNoteSink start() throws IOException {
    lock.lock();
    try {
      Path parent;
      parent = file.getParent();

      if (!Files.exists(parent)) {
        Files.createDirectories(parent);
      }

      channel = Files.newByteChannel(file, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

      buffer = ByteBuffer.allocateDirect(4096);

      active = true;

      send(FileNoteSink.STARTED, file, level);

      return this;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void close() throws IOException {
    lock.lock();
    try {
      closeImpl();
    } finally {
      lock.unlock();
    }
  }

  private void closeImpl() throws IOException {
    channel.close();

    active = false;
  }

  @Override
  protected final void writeBytes(byte[] bytes) {
    if (!active) {
      return;
    }

    int remaining;
    remaining = bytes.length;

    lock.lock();
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
      lock.unlock();
    }
  }

}
