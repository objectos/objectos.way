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
import objectos.html.play.Piece;

public final class AttributeState implements State {

  private final Tape tape;

  private final ElementName elementName;

  private final AttributePiece attribute;

  AttributeState(Tape tape, ElementName elementName, AttributePiece attribute) {
    this.tape = tape;

    this.elementName = elementName;

    this.attribute = attribute;
  }

  @Override
  public final State compute() {
    final NextElementAttribute next;
    next = new NextElementAttribute(tape, elementName);

    return next.compute();
  }

  @Override
  public final boolean hasNext() {
    return true;
  }

  @Override
  public final Piece next() {
    return attribute;
  }

  @Override
  public final String toString() {
    return attribute.toString();
  }

}
