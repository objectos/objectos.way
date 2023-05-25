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

import java.util.Objects;
import objectos.lang.Check;
import objectos.lang.ToString;

public class ValueType implements ToString.Formattable {

  public final String formal;
  public final String name;

  public ValueType(String name, String formal) {
    this.name = Check.notNull(name, "name == null");
    this.formal = Check.notNull(formal, "formal == null");
  }

  public final void acceptKeywordSetBuilder(KeywordSet.Builder builder) {
    builder.parse(name, formal);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof ValueType)) {
      return false;
    }
    ValueType that = (ValueType) obj;
    return name.equals(that.name)
        && formal.equals(that.formal);
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
      sb, depth, this,
      "name", name,
      "formal", formal
    );
  }

  @Override
  public final int hashCode() {
    return Objects.hash(name, formal);
  }

  public final String join() {
    return name + " = " + formal;
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  public final String write() {
    return name + ':' + formal;
  }

}
