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
package objectos.selfgen.css2;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import objectos.code.ClassTypeName;
import objectos.code.tmpl.TypeName;
import objectos.selfgen.util.JavaNames;
import objectos.util.GrowableSet;

public final class KeywordName implements ParameterType, Value {
  static final Comparator<? super KeywordName> ORDER_BY_FIELD_NAME
      = (self, that) -> self.fieldName.compareTo(that.fieldName);

  static final Comparator<? super KeywordName> ORDER_BY_SIMPLE_NAME
      = (self, that) -> self.className.simpleName().compareTo(that.className.simpleName());

  public final String fieldName;

  public final String keywordName;

  private final GrowableSet<ClassTypeName> superTypes = new GrowableSet<>();

  private final GrowableSet<ValueType> valueTypes = new GrowableSet<>();

  private ClassTypeName className;

  private boolean shouldGenerate;

  KeywordName(String fieldName, String keywordName) {
    this.fieldName = fieldName;
    this.keywordName = keywordName;
  }

  public static KeywordName of(String name) {
    Objects.requireNonNull(name, "name == null");

    var fieldName = JavaNames.toValidMethodName(name);

    return new KeywordName(fieldName, name);
  }

  @Override
  public final void addValueType(ValueType valueType) {
    valueType.addPermitted(ThisTemplate.STANDARD_NAME);

    superTypes.add(valueType.className);

    valueTypes.add(valueType);
  }

  public final ClassTypeName className() {
    return className;
  }

  public final boolean shouldGenerate() {
    return shouldGenerate;
  }

  @Override
  public final TypeName typeName() {
    setClassNameToKeyword();

    return className;
  }

  final void addSuperType(ClassTypeName type) {
    superTypes.add(type);
  }

  final void compile() {
    int size = superTypes.size();

    switch (size) {
      case 0 -> {
        if (className == null) {
          throw new IllegalStateException();
        }

        shouldGenerate = true;

        superTypes.add(ThisTemplate.PROPERTY_VALUE);
      }

      case 1 -> {
        if (className == null) {
          var iterator = superTypes.iterator();

          className = iterator.next();
        } else {
          shouldGenerate = true;
        }
      }

      default -> {
        setClassNameToKeyword();

        for (var type : valueTypes) {
          type.addPermitted(className);
        }

        shouldGenerate = true;
      }
    }
  }

  final Collection<ClassTypeName> superTypes() {
    return superTypes;
  }

  private void setClassNameToKeyword() {
    if (className == null) {
      var simpleName = JavaNames.toValidClassName(keywordName) + "Keyword";

      className = ClassTypeName.of(ThisTemplate.CSS_TMPL, simpleName);
    }
  }

}