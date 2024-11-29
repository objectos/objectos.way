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

import java.util.List;
import java.util.Objects;

final class WebFormConfig implements Web.Form.Config {

  String action = "/";

  Web.Relation spec;

  @Override
  public final void action(String value) {
    Check.argument(!value.isBlank(), "Action must not be blank");

    final char first;
    first = value.charAt(0);

    Check.argument(first == '/', "Action must start with the '/' character");

    action = value;
  }

  @Override
  public final void spec(Web.Relation value) {
    spec = Objects.requireNonNull(value, "value == null");
  }

  final WebForm build() {
    Check.state(spec != null, "spec was not set");

    return new WebForm(this);
  }

  final List<? extends WebFormField> fields() {
    WebRelation impl;
    impl = (WebRelation) spec;

    return impl.toFields();
  }

}