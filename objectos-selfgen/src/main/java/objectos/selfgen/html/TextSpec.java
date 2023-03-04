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

import java.util.Set;
import java.util.TreeSet;

public final class TextSpec implements Child, Name {

  private final String className;

  private final Set<ElementSpec> parentSet = new TreeSet<>();

  TextSpec() {
    className = Object.class.toString();
  }

  @Override
  public final Name addParent(ElementSpec parent) {
    parentSet.add(parent);
    return this;
  }

  public final TextSpec category(CategorySpec category) {
    category.add(this);
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof TextSpec)) {
      return false;
    }
    TextSpec that = (TextSpec) obj;
    return className.equals(that.className);
  }

  @Override
  public final int hashCode() {
    return className.hashCode();
  }

  @Override
  public final String name() {
    return className.toString();
  }

  public final Iterable<ElementSpec> parentStream() {
    return parentSet;
  }

}