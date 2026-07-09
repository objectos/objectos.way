/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html;

import java.util.Arrays;
import objectos.internal.Util;

public final class ObjectArray {

  private Object[] values = Util.EMPTY_OBJECT_ARRAY;

  private int index;

  public ObjectArray() {}

  private ObjectArray(Object[] values) {
    this.values = values;

    index = values.length;
  }

  public static ObjectArray of(Object... values) {
    final Object[] copy;
    copy = values.clone();

    return new ObjectArray(copy);
  }

  public final int add(Object value) {
    final int result;
    result = index++;

    if (values == Util.EMPTY_OBJECT_ARRAY) {
      values = new Object[10];
    } else {
      values = Util.growIfNecessary(values, index);
    }

    values[result] = value;

    return result;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ObjectArray that
        && Arrays.equals(values, 0, index, that.values, 0, that.index);
  }

  @Override
  public final String toString() {
    return Arrays.toString(values);
  }

}
