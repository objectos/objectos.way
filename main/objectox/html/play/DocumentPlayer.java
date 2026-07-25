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

public final class DocumentPlayer implements Iterator<HtmlEvent> {

  private boolean computed;

  private int index;

  private HtmlEvent next;

  private final Tape source;

  DocumentPlayer(Tape source) {
    this.source = source;
  }

  @Override
  public final boolean hasNext() {
    if (!computed) {
      next = computeNext();

      computed = true;
    }

    return next != null;
  }

  @Override
  public final HtmlEvent next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    } else {
      final HtmlEvent result;
      result = next;

      computed = false;

      next = null;

      return result;
    }
  }

  final HtmlEvent computeNext() {
    return null;
  }

}
