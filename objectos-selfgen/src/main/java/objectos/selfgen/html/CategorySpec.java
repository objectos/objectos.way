/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.html;

import java.util.List;
import objectos.util.GrowableList;

public final class CategorySpec {

  private final List<Child> childSet = new GrowableList<>();

  private final String name;

  CategorySpec(String name) {
    this.name = name;
  }

  public final Iterable<Child> childStream() {
    return childSet;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof CategorySpec)) {
      return false;
    }
    CategorySpec that = (CategorySpec) obj;
    return name.equals(that.name);
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

  public final String name() {
    return name;
  }

  final CategorySpec add(Child child) {
    childSet.add(child);
    return this;
  }

}