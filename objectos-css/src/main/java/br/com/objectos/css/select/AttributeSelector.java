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
package br.com.objectos.css.select;

public class AttributeSelector extends SimpleSelector {

  private final String name;

  AttributeSelector(String name) {
    this.name = name;
  }

  @Override
  public final <R, P> R acceptSimpleSelectorVisitor(SimpleSelectorVisitor<R, P> visitor, P p) {
    return visitor.visitAttributeSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof AttributeSelector)) {
      return false;
    }
    return toString().equals(obj.toString());
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

  @Override
  public final boolean matches(Selectable element) {
    String actualValue = element.attributeValue(name);
    return actualValue != null;
  }

  public final String name() {
    return name;
  }

  @Override
  public final String toString() {
    return new StringBuilder()
        .append('[')
        .append(name)
        .append(']')
        .toString();
  }

}