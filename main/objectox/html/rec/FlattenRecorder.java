/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.rec;

import java.util.Collection;
import java.util.Objects;
import objectos.html.rec.Instruction;

final class FlattenRecorder {

  private final HtmlSink sink;

  private final ContentsRecorder contentsRecorder;

  FlattenRecorder(HtmlSink sink, ContentsRecorder contentsRecorder) {
    this.sink = sink;

    this.contentsRecorder = contentsRecorder;
  }

  public final FlattenPointer record(Instruction... contents) {
    final Instruction[] instructions;
    instructions = Objects.requireNonNull(contents, "contents == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFLAT);

    contentsRecorder.record(instructions);

    return new FlattenPointer(startIndex);
  }

  public final FlattenPointer record(Collection<? extends Instruction> contents) {
    final Collection<? extends Instruction> values;
    values = Objects.requireNonNull(contents, "contents == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFLAT);

    final Instruction[] array;
    array = values.toArray(Instruction[]::new);

    contentsRecorder.record(array);

    return new FlattenPointer(startIndex);
  }

}
