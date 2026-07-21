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

import java.util.Objects;
import objectos.html.ElementName;
import objectos.html.rec.ElementMarkup;
import objectos.html.rec.Instruction;

public final class ElementInstruction extends AbstractInstruction implements ElementMarkup {

  @SuppressWarnings("unused")
  private final ElementName name;

  @SuppressWarnings("unused")
  private final Instruction[] contents;

  private ElementInstruction(ElementName name, Instruction[] contents) {
    this.name = name;

    this.contents = contents;
  }

  public static ElementInstruction of(ElementName name, Instruction... contents) {
    final Instruction[] instructions;
    instructions = Objects.requireNonNull(contents, "contents == null");

    final int len;
    len = instructions.length;

    final Instruction[] copy;
    copy = new Instruction[len];

    for (int idx = 0; idx < len; idx++) {
      final Instruction instruction;
      instruction = instructions[idx];

      if (instruction == null) {
        final String msg;
        msg = "contents[" + idx + "] == null";

        throw new NullPointerException(msg);
      }

      instruction.consume();

      copy[idx] = instruction;
    }

    return new ElementInstruction(name, copy);
  }

}
