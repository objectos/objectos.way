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
import java.util.Objects;
import objectos.notes.Level;
import objectos.notes.NoteSink;

/**
 * A {@link NoteSink} object that writes out notes to the system console.
 */
public final class ConsoleNoteSink extends AbstractNoteSink {

  private PrintStream stream = System.out;

  public ConsoleNoteSink(Level level) {
    super(Objects.requireNonNull(level, "level == null"));
  }

  public final void target(PrintStream stream) {
    this.stream = stream;
  }

  @Override
  protected void writeBytes(byte[] bytes) {
    stream.writeBytes(bytes);
  }

}