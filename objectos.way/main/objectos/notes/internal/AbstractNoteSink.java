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
package objectos.notes.internal;

import java.time.Clock;
import objectos.lang.Level;
import objectos.lang.LongNote;
import objectos.lang.Note;
import objectos.lang.Note0;
import objectos.lang.Note1;
import objectos.lang.Note2;
import objectos.lang.Note3;
import objectos.lang.NoteSink;

abstract class AbstractNoteSink implements NoteSink {

	final Level level;

	Clock clock = Clock.systemDefaultZone();

	final Layout layout = new StandardLayout();

	public AbstractNoteSink(Level level) {
		this.level = level;
	}

	public final void clock(Clock clock) {
		this.clock = clock;
	}

	@Override
	public final boolean isEnabled(Note note) {
		if (note == null) {
			return false;
		}

		return note.isEnabled(level);
	}

	@Override
	public final NoteSink replace(NoteSink sink) {
		return this;
	}

	@Override
	public final void send(Note0 note) {
		if (note == null) {
			return;
		}

		if (!note.isEnabled(level)) {
			return;
		}

		Log0 log;
		log = new Log0(clock, note);

		addLog(log);
	}

	@Override
	public final void send(LongNote note, long value) {
		if (note == null) {
			return;
		}

		if (!note.isEnabled(level)) {
			return;
		}

		LongLog log;
		log = new LongLog(clock, note, value);

		addLog(log);
	}

	@Override
	public final <T1> void send(Note1<T1> note, T1 v1) {
		if (note == null) {
			return;
		}

		if (!note.isEnabled(level)) {
			return;
		}

		Log1 log;
		log = new Log1(clock, note, v1);

		addLog(log);
	}

	@Override
	public final <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
		if (note == null) {
			return;
		}

		if (!note.isEnabled(level)) {
			return;
		}

		Log2 log;
		log = new Log2(clock, note, v1, v2);

		addLog(log);
	}

	@Override
	public final <T1, T2, T3> void send(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3) {
		if (note == null) {
			return;
		}

		if (!note.isEnabled(level)) {
			return;
		}

		Log3 log;
		log = new Log3(clock, note, v1, v2, v3);

		addLog(log);
	}

	abstract void addLog(Log0 log);

	abstract void addLog(Log1 log);

	abstract void addLog(Log2 log);

	abstract void addLog(Log3 log);

	abstract void addLog(LongLog log);

}