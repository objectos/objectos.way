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
package objectos.notes.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import objectos.notes.Level;
import objectos.notes.internal.AbstractNoteSink;
import objectos.notes.internal.Log0;
import objectos.notes.internal.Log1;
import objectos.notes.internal.Log2;
import objectos.notes.internal.Log3;
import objectos.notes.internal.LongLog;

final class StandardFileNoteSink extends AbstractNoteSink implements FileNoteSink {

	private final Path file;

	private final Lock lock = new ReentrantLock();

	private WritableByteChannel channel;

	private ByteBuffer buffer;

	private volatile boolean active;

	public StandardFileNoteSink(Path file, Level level) {
		super(level);

		this.file = file;
	}

	public final void start() throws IOException {
		lock.lock();
		try {
			channel = Files.newByteChannel(file, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

			buffer = ByteBuffer.allocateDirect(4096);

			active = true;

			send(FileNoteSink.STARTED, file, level);
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
	protected final void addLog(Log0 log) {
		String s;
		s = layout.formatLog0(log);

		print(s);
	}

	@Override
	protected final void addLog(Log1 log) {
		String s;
		s = layout.formatLog1(log);

		print(s);
	}

	@Override
	protected final void addLog(Log2 log) {
		String s;
		s = layout.formatLog2(log);

		print(s);
	}

	@Override
	protected final void addLog(Log3 log) {
		String s;
		s = layout.formatLog3(log);

		print(s);
	}

	@Override
	protected final void addLog(LongLog log) {
		String s;
		s = layout.formatLongLog(log);

		print(s);
	}

	private void print(String s) {
		if (!active) {
			return;
		}

		byte[] bytes;
		bytes = s.getBytes(StandardCharsets.UTF_8);

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
