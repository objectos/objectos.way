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
package objectos.specgen.css;

import java.io.Serializable;
import java.util.Objects;
import objectos.lang.Check;
import objectos.lang.ToString;
import objectos.util.UnmodifiableList;

public class Property implements Comparable<Property>, Serializable, ToString.Formattable {

  private static final long serialVersionUID = -4272842217165803466L;

  private final String formal;
  private final String name;
  private final UnmodifiableList<ValueType> valueTypes;

  public Property(String name, String formal) {
    this(name, formal, UnmodifiableList.of());
  }

  public Property(String name, String formal, UnmodifiableList<ValueType> valueTypes) {
    this.name = Check.notNull(name, "name == null");
    this.formal = Check.notNull(formal, "formal == null");
    this.valueTypes = Check.notNull(valueTypes, "valueTypes == null");
  }

  public final void acceptKeywordSetBuilder(KeywordSet.Builder builder) {
    builder.parse(name, formal);
    for (ValueType valueType : valueTypes) {
      valueType.acceptKeywordSetBuilder(builder);
    }
  }

  @Override
  public final int compareTo(Property o) {
    return name.compareTo(o.name);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof Property)) {
      return false;
    }
    Property that = (Property) obj;
    return name.equals(that.name)
        && formal.equals(that.formal);
  }

  public final String formal() {
    return formal;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
      toString, level, this,
      "name", name,
      "formal", formal,
      "valueTypes", valueTypes
    );
  }

  @Override
  public final int hashCode() {
    return Objects.hash(name, formal);
  }

  public final String name() {
    return name;
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  public final UnmodifiableList<ValueType> valueTypes() {
    return valueTypes;
  }

  public final String write() {
    return name + ':' + formal;
  }

  public static abstract class Builder {

    public final Property build() {
      return new Property(
        name(),
        formal(),
        valueTypes()
      );
    }

    protected abstract String formal();

    protected abstract String name();

    protected abstract UnmodifiableList<ValueType> valueTypes();

  }

}