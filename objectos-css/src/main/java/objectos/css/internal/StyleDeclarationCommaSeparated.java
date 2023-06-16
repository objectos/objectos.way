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
package objectos.css.internal;

import objectos.css.om.PropertyName;
import objectos.css.om.PropertyValue;
import objectos.css.om.StyleDeclaration;

public record StyleDeclarationCommaSeparated(PropertyName name,
                                             PropertyValue[] values)
    implements StyleDeclaration {
  @Override
  public final String toString() {
    var sb = new StringBuilder();

    sb.append(name);

    sb.append(": ");

    sb.append(values[0]);

    for (int i = 1; i < values.length; i++) {
      sb.append(", ");

      sb.append(values[i]);
    }

    return sb.toString();
  }
}