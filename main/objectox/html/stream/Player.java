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
package objectox.html.stream;

import java.util.Iterator;
import java.util.NoSuchElementException;
import objectos.html.stream.Event;

public final class Player extends Ctx implements Iterator<Event> {

  private State state;

  Player(byte[] code, Object[] objects) {
    this(code, objects, EMPTY_STACK, -1);
  }

  Player(byte[] code, Object[] objects, Frame[] stack, int stackIndex) {
    super(code, objects, stack, stackIndex);

    state = ScanDocumentNodeState.of(this);
  }

  public static Player of(byte[] code, Object[] objects) {
    return new Player(
        code,

        objects
    );
  }

  @Override
  public final boolean hasNext() {
    while (shouldCompute()) {
      state = state.compute();
    }

    return hasEvent();
  }

  @Override
  public final Event next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    } else {
      return nextEvent();
    }
  }

}
