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

import java.util.Comparator;
import java.util.Objects;
import java.util.TreeMap;
import objectos.code.ClassTypeName;

public final class KeywordName implements Value {
  static final Comparator<? super KeywordName> ORDER_BY_FIELD_NAME
      = (self, that) -> self.fieldName.compareTo(that.fieldName);

  public final String fieldName;

  public final String keywordName;

  private final TreeMap<String, ClassTypeName> interfaces = new TreeMap<>();

  KeywordName(String fieldName, String keywordName) {
    this.fieldName = fieldName;
    this.keywordName = keywordName;
  }

  public static KeywordName of(String name) {
    Objects.requireNonNull(name, "name == null");

    var fieldName = name;

    return new KeywordName(fieldName, name);
  }

  @Override
  public final void addInterface(ClassTypeName className) {
    var key = className.simpleName();

    interfaces.put(key, className);
  }

  public final ClassTypeName fieldType() {
    return switch (interfaces.size()) {
      case 0 -> throw new IllegalStateException(
        """

        """
      );

      case 1 -> interfaces.firstEntry().getValue();

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

}