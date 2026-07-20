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
import objectos.html.play.EndTag;
import objectos.html.play.Piece;

public final class EndTagState implements EndTag, State {

  private final Tape tape;

  private final ElementName elementName;

  private final FrameKind parentKind;

  EndTagState(Tape tape, ElementName elementName, FrameKind parentKind) {
    this.tape = tape;

    this.elementName = elementName;

    this.parentKind = parentKind;
  }

  @Override
  public final State compute() {
    return switch (parentKind) {
      case DOC_ELEMENT -> new NextDocumentNode(tape).compute();

      case ELEMENT_CHILD -> new NextElementNode(tape, elementName).compute();

      default -> throw new UnsupportedOperationException("Implement me :: parentKind=" + parentKind);
    };
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
  public final String name() {
    return elementName.name();
  }

  @Override
  public final String toString() {
    return "EndTag(" + name() + ")";
  }

}
