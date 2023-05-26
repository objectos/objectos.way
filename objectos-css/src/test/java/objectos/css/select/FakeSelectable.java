/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class FakeSelectable implements Selectable {

  private final Map<String, List<String>> attributeMap = new HashMap<>();
  private String name;
  private Optional<Selectable> parent = Optional.empty();
  private Optional<Selectable> previousSibling = Optional.empty();

  FakeSelectable() {
  }

  public static FakeSelectable named(String name) {
    return new FakeSelectable().withName(name);
  }

  @Override
  public final String attributeValue(String name) {
    return attributeList(name)
        .stream()
        .findFirst()
        .orElse(null);
  }

  @Override
  public final boolean hasAttributeValue(String name, String value) {
    return attributeList(name).contains(value);
  }

  @Override
  public final boolean hasName(String name) {
    return Objects.equals(this.name, name);
  }

  @Override
  public final Optional<? extends Selectable> parent() {
    return parent;
  }

  @Override
  public final Optional<? extends Selectable> previousSibling() {
    return previousSibling;
  }

  public final FakeSelectable withAttribute(String name, String value) {
    attributeList(name).add(value);
    return this;
  }

  public final FakeSelectable withClass(String name) {
    return withAttribute("class", name);
  }

  public final FakeSelectable withId(String value) {
    return withAttribute("id", value);
  }

  public final FakeSelectable withName(String aName) {
    name = aName;
    return this;
  }

  public final FakeSelectable withParent(Selectable element) {
    parent = Optional.of(element);
    return this;
  }

  public final FakeSelectable withPreviousSibling(Selectable element) {
    previousSibling  = Optional.of(element);
    return this;
  }

  private List<String> attributeList(String name) {
    return attributeMap.computeIfAbsent(name, k -> new ArrayList<>());
  }


}