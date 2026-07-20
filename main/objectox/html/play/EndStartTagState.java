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
import objectos.html.play.EndStartTag;
import objectos.html.play.Piece;

public final class EndStartTagState implements EndStartTag, State {

  private final Tape tape;

  private final ElementName elementName;

  EndStartTagState(Tape tape, ElementName elementName) {
    this.tape = tape;

    this.elementName = elementName;
  }

  @Override
  public final State compute() {
    final NextElementNode next;
    next = new NextElementNode(tape, elementName);

    return next.compute();
  }

  @Override
  public final boolean hasNext() {
    return true;
  }

  @Override
  public final Piece next() {
    return this;
  }

  @Override
  public final String toString() {
    return "EndStartTag";
  }

}
