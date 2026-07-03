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
package objectox.dev;

import java.util.Iterator;
import java.util.Objects;
import objectos.dev.Testable;

public final class TestableJoin {

  private final Iterable<? extends Testable> rows;

  public TestableJoin(Iterable<? extends Testable> rows) {
    this.rows = Objects.requireNonNull(rows, "rows == null");
  }

  public final String format() {
    final Iterator<? extends Testable> iter;
    iter = rows.iterator();

    if (iter.hasNext()) {
      final StringBuilder out;
      out = new StringBuilder();

      final String first;
      first = next(iter);

      out.append(first);

      while (iter.hasNext()) {
        out.append('\n');

        final String next;
        next = next(iter);

        out.append(next);
      }

      return out.toString();
    }

    else {
      return "";
    }
  }

  private String next(Iterator<? extends Testable> iter) {
    final Testable next;
    next = iter.next();

    if (next == null) {
      final String msg;
      msg = "rows provided a null instance";

      throw new NullPointerException(msg);
    }

    return next.toTestableText();
  }

}
