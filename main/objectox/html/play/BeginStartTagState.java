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
import objectos.html.play.BeginStartTag;

public final class BeginStartTagState implements BeginStartTag, State {

  private final Tape tape;

  private final ElementName name;

  BeginStartTagState(Tape tape, ElementName name) {
    this.tape = tape;

    this.name = name;
  }

  @Override
  public final State compute() {
    tape.push();

    final NextElementAttribute nextAttribute;
    nextAttribute = new NextElementAttribute(tape, name);

    return nextAttribute.compute();
  }

  @Override
  public final boolean hasNext() {
    return true;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final Piece next() {
    return this;
  }

  @Override
  public final String toString() {
    return "BeginStartTag(" + name() + ")";
  }

}
