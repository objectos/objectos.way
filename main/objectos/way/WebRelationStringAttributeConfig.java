/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.Objects;
import java.util.regex.Pattern;

final class WebRelationStringAttributeConfig extends WebRelationAttributeConfig implements Web.Relation.StringAttribute.Config {

  int maxLength;

  Pattern pattern;

  String patternMessage;

  @Override
  public final void maxLength(int value) {
    Check.argument(value > 0, "Maximum length must be greater than zero");

    maxLength = value;
  }

  @Override
  public final void pattern(String value, String message) {
    pattern = Pattern.compile(value);

    patternMessage = Objects.requireNonNull(message, "message == null");
  }

  final WebRelationStringAttribute build() {
    return new WebRelationStringAttribute(this);
  }

}