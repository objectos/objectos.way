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
import objectox.html.HtmlByteProto;

final class NextElementAttribute {

  private final Tape tape;

  private final ElementName name;

  NextElementAttribute(Tape tape, ElementName name) {
    this.tape = tape;

    this.name = name;
  }

  public final State compute() {
    final byte proto;
    proto = tape.nextByte();

    return switch (proto) {
      case HtmlByteProto.END -> new EndStartTagState(tape, name);

      case HtmlByteProto.AMBIGUOUS1 -> throw new UnsupportedOperationException("Implement me");

      case HtmlByteProto.ATTRIBUTE0 -> throw new UnsupportedOperationException("Implement me");

      default -> throw State.implMe(proto);
    };
  }

}
