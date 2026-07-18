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

import java.util.Iterator;
import java.util.NoSuchElementException;
import objectos.html.play.Piece;
import objectox.html.rec.ByteArray;
import objectox.html.rec.ObjectArray;

public final class Player implements Iterator<Piece> {

  private boolean computed;

  private State state;

  public Player(ByteArray main, ObjectArray objects) {
    final Tape tape;
    tape = new Tape(main, objects);

    state = new StartState(tape);
  }

  @Override
  public final boolean hasNext() {
    if (!computed) {
      state = state.compute();

      computed = true;
    }

    return state.hasNext();
  }

  @Override
  public final Piece next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    } else {
      final Piece result;
      result = state.next();

      computed = false;

      return result;
    }
  }

}
