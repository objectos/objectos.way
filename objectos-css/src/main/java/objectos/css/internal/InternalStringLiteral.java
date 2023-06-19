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

import java.util.Objects;
import java.util.regex.Pattern;
import objectos.css.om.PropertyValue;
import objectos.css.tmpl.StringLiteral;

public final class InternalStringLiteral implements StringLiteral {

  private static final Pattern FONT_FAMILY = Pattern.compile("-?[a-zA-Z_][a-zA-Z0-9_-]*");

  private final String value;

  public InternalStringLiteral(String value) {
    this.value = value;
  }

  public static InternalStringLiteral of(String value) {
    return new InternalStringLiteral(
      Objects.requireNonNull(value, "value == null")
    );
  }

  @Override
  public final PropertyValue asFontFamilyValue() {
    var matcher = FONT_FAMILY.matcher(value);

    if (!matcher.matches()) {
      return this;
    } else {
      return new NamedElement(value);
    }
  }

  @Override
  public final String toString() {
    return "\"" + value + "\"";
  }

}