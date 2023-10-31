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
package objectos.notes;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import objectos.lang.Level;
import objectos.lang.Note0;
import objectos.lang.Note1;
import objectos.lang.Note2;
import objectos.lang.Note3;
import objectos.lang.NoteSink;
import org.testng.annotations.Test;

public class ConsoleNoteSinkTest {

	static final Note0 NOTE0;

	static final Note1<Throwable> THROW1;

	static final Note2<Throwable, Throwable> THROW2;

	static final Note3<Throwable, Throwable, Throwable> THROW3;

	static {
		Class<?> s;
		s = ConsoleNoteSinkTest.class;

		NOTE0 = Note0.info(s, "NOTE0");

		THROW1 = Note1.error(s, "THROW1");

		THROW2 = Note2.error(s, "THROW2");

		THROW3 = Note3.error(s, "THROW3");
	}

	@Test
	public void send() {
		ThisStream stream;
		stream = new ThisStream();

		NoteSink noteSink = ConsoleNoteSink.of(
				Level.TRACE,

				ConsoleNoteSink.Option.target(stream)
		);

		noteSink.send(NOTE0);

		String string;
		string = stream.toString();

		assertTrue(string.endsWith(
				"INFO  --- [main           ] objectos.notes.ConsoleNoteSinkTest       : NOTE0\n"), string);
	}

	@Test
	public void throwable() {
		ThisStream stream;
		stream = new ThisStream();

		NoteSink noteSink = ConsoleNoteSink.of(
				Level.TRACE,

				ConsoleNoteSink.Option.target(stream)
		);

		Throwable ignore = ignore();

		noteSink.send(THROW1, throwable1());
		noteSink.send(THROW2, ignore, throwable2());
		noteSink.send(THROW3, ignore, ignore, throwable3());

		String string;
		string = stream.toString();

		assertFalse(string.contains("objectos.notes.ConsoleNoteSinkTest.ignore(ConsoleNoteSinkTest.java:"), string);
		assertTrue(string.contains("objectos.notes.ConsoleNoteSinkTest.throwable1(ConsoleNoteSinkTest.java:"), string);
		assertTrue(string.contains("objectos.notes.ConsoleNoteSinkTest.throwable2(ConsoleNoteSinkTest.java:"), string);
		assertTrue(string.contains("objectos.notes.ConsoleNoteSinkTest.throwable3(ConsoleNoteSinkTest.java:"), string);
	}

	private Throwable ignore() {
		return new Throwable();
	}

	private Throwable throwable1() {
		return new Throwable();
	}

	private Throwable throwable2() {
		return new Throwable();
	}

	private Throwable throwable3() {
		return new Throwable();
	}

	private static class ThisStream extends PrintStream {

		public ThisStream() {
			super(new ByteArrayOutputStream());
		}

		@Override
		public final String toString() {
			ByteArrayOutputStream byteStream;
			byteStream = (ByteArrayOutputStream) out;

			byte[] bytes;
			bytes = byteStream.toByteArray();

			return new String(bytes);
		}

	}

}