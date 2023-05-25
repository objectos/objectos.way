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
package objectos.selfgen.css.spec;

import java.util.Set;
import java.util.TreeSet;
import javax.lang.model.SourceVersion;
import objectos.lang.Check;
import objectos.lang.ToString;
import objectos.selfgen.util.JavaNames;

public final class KeywordName
    implements
    Comparable<KeywordName>,
    ParameterType,
    Value,
    ToString.Formattable {

  final String simpleName;

  final String fieldName;

  final Set<String> interfaceSet = new TreeSet<>();

  final String name;

  private KeywordName(String name, String fieldName) {
    this.name = name;

    this.simpleName = JavaNames.toValidClassName(name) + "Keyword";

    this.fieldName = fieldName;
  }

  public static KeywordName of(String value) {
    Check.notNull(value, "value == null");

    char firstChar = value.charAt(0);
    String fieldNameCandidate = Character.isUpperCase(firstChar)
        ? JavaNames.toValidClassName(value)
        : JavaNames.toValidMethodName(value);
    if (SourceVersion.isKeyword(fieldNameCandidate)) {
      fieldNameCandidate = fieldNameCandidate + "Kw";
    }
    return new KeywordName(value, fieldNameCandidate);
  }

  public static KeywordName withKwSuffix(String value) {
    Check.notNull(value, "value == null");
    return new KeywordName(value, JavaNames.toValidMethodName(value) + "Kw");
  }

  public final void acceptPritiveType(PrimitiveType type) {
    interfaceSet.add(type.typeSimpleName());
  }

  @Override
  public final void acceptValueType(ValueType type) {
    interfaceSet.add(type.simpleName);
  }

  @Override
  public final int compareTo(KeywordName o) {
    return name.compareTo(o.name);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof KeywordName)) {
      return false;
    }
    KeywordName that = (KeywordName) obj;
    return name.equals(that.name);
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
      toString, level, this,
      "", name
    );
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

}
