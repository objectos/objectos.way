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

import java.io.PrintStream;
import objectos.lang.object.Check;
import objectos.notes.Level;

/**
 * A {@link NoteSink} object that writes out notes to the system console.
 */
public final class ConsoleNoteSink extends AbstractNoteSink {

  private PrintStream stream = System.out;

  public ConsoleNoteSink(Level level) {
    super(Check.notNull(level, "level == null"));
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