/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.type.ArrayTypeName;
import objectos.code.type.TypeName;
import objectos.lang.Check;

/**
 * TODO
 *
 * @since 0.4.4
 */
public sealed abstract class ArrayTypeNameImpl extends External implements ArrayTypeName {

  private static final class OfClass extends ArrayTypeNameImpl {
    private final Class<?> value;

    public OfClass(Class<?> value) {
      this.value = value;
    }

    @Override
    public final void execute(InternalApi api) {
      api.arrayTypeName(value);
      api.localToExternal();
    }
  }

  private static final class OfTypeName extends ArrayTypeNameImpl {

    private final TypeName type;

    private final int count;

    OfTypeName(TypeName type, int count) {
      this.type = type;
      this.count = count;
    }

    @Override
    public final void execute(InternalApi api) {
      api.arrayTypeName(type, count);
      api.localToExternal();
    }

  }

  ArrayTypeNameImpl() {}

  public static ArrayTypeNameImpl of(ArrayTypeComponent type, int count) {
    var typeName = (TypeName) type.self();
    Check.argument(count > 0, "count must be greater than 0 (zero)");
    return new OfTypeName(typeName, count);
  }

  public static ArrayTypeNameImpl of(Class<?> type) {
    Check.argument(type.isArray(), """
    A `ArrayTypeName` can only be used to represent array types.
    """);

    return new OfClass(type);
  }

}