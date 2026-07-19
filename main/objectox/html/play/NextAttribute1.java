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
package objectox.html.play;

import objectos.html.ElementName;
import objectox.html.attr.AttributeNamePojo;

final class NextAttribute1 {

  private final Tape tape;

  private final ElementName elementName;

  NextAttribute1(Tape tape, ElementName name) {
    this.tape = tape;

    this.elementName = name;
  }

  public final State compute() {
    final int offset;
    offset = tape.nextVarIntLE();

    tape.push(); // return 2 element

    tape.skip(-offset);

    final byte nameIndex;
    nameIndex = tape.nextByte();

    final AttributeNamePojo name;
    name = AttributeNamePojo.get(nameIndex);

    final int valueIndex;
    valueIndex = tape.nextInt16();

    final String value;
    value = tape.string(valueIndex);

    return new AttributeState(tape, elementName, name, value);
  }

}
