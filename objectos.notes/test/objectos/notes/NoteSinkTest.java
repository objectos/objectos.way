/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class NoteSinkTest {

	static final Note0 DEBUG0;

	static final Note1<Arg1> DEBUG1;

	static final Note2<Arg1, Arg2> DEBUG2;

	static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> DEBUG3;

	static final Note0 ERROR0;

	static final Note1<Arg1> ERROR1;

	static final Note2<Arg1, Arg2> ERROR2;

	static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> ERROR3;

	static final Note0 INFO0;

	static final Note1<Arg1> INFO1;

	static final Note2<Arg1, Arg2> INFO2;

	static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> INFO3;

	static final Note0 TRACE0;

	static final Note1<Arg1> TRACE1;

	static final Note2<Arg1, Arg2> TRACE2;

	static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> TRACE3;

	static final Note0 WARN0;

	static final Note1<Arg1> WARN1;

	static final Note2<Arg1, Arg2> WARN2;

	static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> WARN3;

	static {
		Class<?> s;
		s = NoteSinkTest.class;

		DEBUG0 = Note0.debug(s, "DEBUG0");
		DEBUG1 = Note1.debug(s, "DEBUG1");
		DEBUG2 = Note2.debug(s, "DEBUG2");
		DEBUG3 = Note3.debug(s, "DEBUG3");

		ERROR0 = Note0.error(s, "ERROR0");
		ERROR1 = Note1.error(s, "ERROR1");
		ERROR2 = Note2.error(s, "ERROR2");
		ERROR3 = Note3.error(s, "ERROR3");

		INFO0 = Note0.info(s, "INFO0");
		INFO1 = Note1.info(s, "INFO1");
		INFO2 = Note2.info(s, "INFO2");
		INFO3 = Note3.info(s, "INFO3");

		TRACE0 = Note0.trace(s, "TRACE0");
		TRACE1 = Note1.trace(s, "TRACE1");
		TRACE2 = Note2.trace(s, "TRACE2");
		TRACE3 = Note3.trace(s, "TRACE3");

		WARN0 = Note0.warn(s, "WARN0");
		WARN1 = Note1.warn(s, "WARN1");
		WARN2 = Note2.warn(s, "WARN2");
		WARN3 = Note3.warn(s, "WARN3");
	}

	@Test
	public void isEnabled() {
		assertTrue(INFO0.isEnabled(Level.TRACE));

		assertTrue(INFO0.isEnabled(Level.DEBUG));

		assertTrue(INFO0.isEnabled(Level.INFO));

		assertFalse(INFO0.isEnabled(Level.WARN));

		assertFalse(INFO0.isEnabled(Level.ERROR));
	}

	@Test
	public void send() {
		ThisNoteSink noteSink;
		noteSink = new ThisNoteSink();

		test0(noteSink, DEBUG0);

		test0(noteSink, ERROR0);

		test0(noteSink, INFO0);

		test0(noteSink, TRACE0);

		test0(noteSink, WARN0);

		Arg1 arg1;
		arg1 = new Arg1(111);

		test1(noteSink, DEBUG1, arg1);

		test1(noteSink, ERROR1, arg1);

		test1(noteSink, INFO1, arg1);

		test1(noteSink, TRACE1, arg1);

		test1(noteSink, WARN1, arg1);

		Arg2 arg2;
		arg2 = new Arg2(222);

		test2(noteSink, DEBUG2, arg1, arg2);

		test2(noteSink, ERROR2, arg1, arg2);

		test2(noteSink, INFO2, arg1, arg2);

		test2(noteSink, TRACE2, arg1, arg2);

		test2(noteSink, WARN2, arg1, arg2);

		Duo<Arg1, Arg2> arg3;
		arg3 = new Duo<Arg1, Arg2>(arg1, arg2);

		test3(noteSink, DEBUG3, arg1, arg2, arg3);

		test3(noteSink, ERROR3, arg1, arg2, arg3);

		test3(noteSink, INFO3, arg1, arg2, arg3);

		test3(noteSink, TRACE3, arg1, arg2, arg3);

		test3(noteSink, WARN3, arg1, arg2, arg3);
	}

	private void test0(ThisNoteSink noteSink, Note0 note) {
		noteSink.send(note);

		assertEquals(noteSink.level, note.level());
		assertEquals(noteSink.note, note);
		assertNull(noteSink.value1);
		assertNull(noteSink.value2);
		assertNull(noteSink.value3);
	}

	private <T1> void test1(ThisNoteSink noteSink, Note1<T1> note, T1 arg) {
		noteSink.send(note, arg);

		assertEquals(noteSink.level, note.level());
		assertEquals(noteSink.note, note);
		assertSame(noteSink.value1, arg);
		assertNull(noteSink.value2);
		assertNull(noteSink.value3);
	}

	private <T1, T2> void test2(ThisNoteSink noteSink, Note2<T1, T2> note, T1 arg1, T2 arg2) {
		noteSink.send(note, arg1, arg2);

		assertEquals(noteSink.level, note.level());
		assertEquals(noteSink.note, note);
		assertSame(noteSink.value1, arg1);
		assertSame(noteSink.value2, arg2);
		assertNull(noteSink.value3);
	}

	private <T1, T2, T3> void test3(
			ThisNoteSink noteSink, Note3<T1, T2, T3> note, T1 arg1, T2 arg2, T3 arg3) {
		noteSink.send(note, arg1, arg2, arg3);

		assertEquals(noteSink.level, note.level());
		assertEquals(noteSink.note, note);
		assertSame(noteSink.value1, arg1);
		assertSame(noteSink.value2, arg2);
		assertSame(noteSink.value3, arg3);
	}

	private static class ThisNoteSink implements NoteSink {

		Note note;

		Level level;

		Object value1;

		Object value2;

		Object value3;

		@Override
		public final boolean isEnabled(Note note) {
			return true;
		}

		@Override
		public final NoteSink replace(NoteSink noteSink) {
			return this;
		}

		@Override
		public final void send(Note0 note) {
			set(note);
		}

		@Override
		public final void send(LongNote note, long value) {
			throw new UnsupportedOperationException("Implement me");
		}

		@Override
		public final <T1> void send(Note1<T1> note, T1 v1) {
			set(note);

			value1 = v1;
		}

		@Override
		public final <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
			set(note);

			value1 = v1;

			value2 = v2;
		}

		@Override
		public final <T1, T2, T3> void send(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3) {
			set(note);

			value1 = v1;

			value2 = v2;

			value3 = v3;
		}

		private void set(Note note) {
			this.note = note;

			this.level = note.level();

			value1 = null;

			value2 = null;

			value3 = null;
		}

	}

}