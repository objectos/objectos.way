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

import java.io.PrintStream;
import objectos.lang.Level;
import objectos.notes.ConsoleNoteSink;

public final class StandardConsoleNoteSink extends AbstractNoteSink implements ConsoleNoteSink {

	public non-sealed static abstract class OptionValue implements ConsoleNoteSink.Option {

		public abstract void accept(StandardConsoleNoteSink builder);

	}

	private PrintStream stream = System.out;

	public StandardConsoleNoteSink(Level level) {
		super(level);
	}

	public final void target(PrintStream stream) {
		this.stream = stream;
	}

	@Override
	final void addLog(Log0 log) {
		String s;
		s = layout.formatLog0(log);

		stream.print(s);
	}

	@Override
	final void addLog(Log1 log) {
		String s;
		s = layout.formatLog1(log);

		stream.print(s);
	}

	@Override
	final void addLog(Log2 log) {
		String s;
		s = layout.formatLog2(log);

		stream.print(s);
	}

	@Override
	final void addLog(Log3 log) {
		String s;
		s = layout.formatLog3(log);

		stream.print(s);
	}

	@Override
	final void addLog(LongLog log) {
		String s;
		s = layout.formatLongLog(log);

		stream.print(s);
	}

}