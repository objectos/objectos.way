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
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import objectos.dev.TestableFormatter2;
import objectos.dev.TestableRowFormatter;

public final class TestableFormatter2Pojo implements TestableFormatter2 {

  private final List<Object> items = new ArrayList<>();

  @Override
  public final void h1(String value) {
    h(1, value);
  }

  @Override
  public final void h2(String value) {
    h(2, value);
  }

  @Override
  public final <T> void table(Iterable<? extends T> elements, BiConsumer<TestableRowFormatter, T> format) {
    final TestableTable<T> table;
    table = new TestableTable<>(elements, format);

    items.add(table);
  }

  @Override
  public final String toString() {
    return items.stream()
        .map(Object::toString)
        .collect(Collectors.joining("\n\n", "", "\n"));
  }

  private void h(int level, String value) {
    final TestableHeading h;
    h = new TestableHeading(level, value);

    items.add(h);
  }

}
