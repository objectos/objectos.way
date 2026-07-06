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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import objectos.dev.TestableRowFormatter;

public final class TestableTable<T> {

  private final Iterable<? extends T> elements;

  private final BiConsumer<? super TestableRowFormatter, T> format;

  public TestableTable(Iterable<? extends T> elements, BiConsumer<? super TestableRowFormatter, T> format) {
    this.elements = Objects.requireNonNull(elements, "elements == null");

    this.format = Objects.requireNonNull(format, "format == null");
  }

  @Override
  public final String toString() {
    final Iterator<? extends T> iter;
    iter = elements.iterator();

    if (iter.hasNext()) {
      final List<String> rows;
      rows = new ArrayList<>();

      do {
        final TestableRowFormatterPojo rf;
        rf = new TestableRowFormatterPojo();

        final T value;
        value = iter.next();

        format.accept(rf, value);

        final String row;
        row = rf.toString();

        rows.add(row);
      } while (iter.hasNext());

      return rows.stream().collect(Collectors.joining("\n"));
    } else {
      return "";
    }
  }

}
