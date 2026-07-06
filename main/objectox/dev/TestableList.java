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
import java.util.Objects;
import java.util.stream.Collectors;
import objectos.dev.TestableListFormatter;

public final class TestableList implements TestableListFormatter {

  private final List<String> items = new ArrayList<>();

  @Override
  public final void item(boolean value) {
    final String item;
    item = Boolean.toString(value);

    items.add(item);
  }

  @Override
  public final void item(String value) {
    final String item;
    item = Objects.requireNonNull(value, "value == null");

    items.add(item);
  }

  @Override
  public final String toString() {
    return items.stream().map(this::map).collect(Collectors.joining("\n"));
  }

  private String map(String value) {
    return value.isEmpty()
        ? "-"
        : "- " + value;
  }

}
