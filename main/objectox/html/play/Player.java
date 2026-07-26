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
import objectos.html.play.HtmlEvent;

public final class Player implements Iterator<HtmlEvent> {

  private final Ctx ctx;

  private State state = State.START;

  Player(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final boolean hasNext() {
    while (ctx.shouldCompute()) {
      final Transition t;
      t = transition();

      state = t.compute();
    }

    return ctx.hasEvent();
  }

  @Override
  public final HtmlEvent next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    } else {
      return ctx.nextEvent();
    }
  }

  private Transition transition() {
    return switch (state) {
      case START -> new StartState(ctx);

      case BEGIN_DOCUMENT -> throw new UnsupportedOperationException("Unimplemented case: " + state);
    };
  }

}
