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
package objectos.notes.console;

import java.io.PrintStream;
import objectos.notes.Level;
import objectos.notes.base.AbstractNoteSink;
import objectos.notes.base.Log0;
import objectos.notes.base.Log1;
import objectos.notes.base.Log2;
import objectos.notes.base.Log3;
import objectos.notes.base.LongLog;

final class StandardConsoleNoteSink extends AbstractNoteSink implements ConsoleNoteSink {

	private PrintStream stream = System.out;

	public StandardConsoleNoteSink(Level level) {
		super(level);
	}

	public final void target(PrintStream stream) {
		this.stream = stream;
	}

	@Override
	protected final void addLog(Log0 log) {
		String s;
		s = layout.formatLog0(log);

		stream.print(s);
	}

	@Override
	protected final void addLog(Log1 log) {
		String s;
		s = layout.formatLog1(log);

		stream.print(s);
	}

	@Override
	protected final void addLog(Log2 log) {
		String s;
		s = layout.formatLog2(log);

		stream.print(s);
	}

	@Override
	protected final void addLog(Log3 log) {
		String s;
		s = layout.formatLog3(log);

		stream.print(s);
	}

	@Override
	protected final void addLog(LongLog log) {
		String s;
		s = layout.formatLongLog(log);

		stream.print(s);
	}

}