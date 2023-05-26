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

public class TypeSelector extends SimpleSelector {

  private final int code;
  private final String name;

  TypeSelector(int code, String name) {
    this.code = code;
    this.name = name;
  }

  @Override
  public final <R, P> R acceptSimpleSelectorVisitor(SimpleSelectorVisitor<R, P> visitor, P p) {
    return visitor.visitTypeSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof TypeSelector)) {
      return false;
    }
    TypeSelector that = (TypeSelector) obj;
    return code == that.code;
  }

  public final int getCode() {
    return code;
  }

  public final String getName() {
    return name;
  }

  @Override
  public final int hashCode() {
    return code;
  }

  @Override
  public final boolean matches(Selectable element) {
    return element.hasName(name);
  }

  @Override
  public final String toString() {
    return name;
  }

  @Override
  final void uncheckedAddTypeSelector(TypeSelector selector) {
    throw InvalidSelectorException.get(
      "Cannot append type selector '%s' to another type selector '%s'",
      selector, this
    );
  }

}