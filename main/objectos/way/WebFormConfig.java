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
import java.util.function.Consumer;

final class WebFormConfig implements Web.FormSpec.Config {

  String action = "/";

  boolean useNameForId;

  UtilList<WebFormField> fields = new UtilList<>();

  @Override
  public final void action(String value) {
    Check.argument(!value.isBlank(), "Action must not be blank");

    final char first;
    first = value.charAt(0);

    Check.argument(first == '/', "Action must start with the '/' character");

    action = value;
  }

  @Override
  public final void useNameForId() {
    useNameForId = true;
  }

  @Override
  public final void textInput(Consumer<Web.Form.TextInput.Config> config) {
    WebFormTextInputConfig builder;
    builder = new WebFormTextInputConfig(this);

    config.accept(builder);

    WebFormTextInput textInput;
    textInput = builder.build();

    fields.add(textInput);
  }

  final WebForm build() {
    return new WebForm(this);
  }

  final List<? extends WebFormField> fields() {
    return fields.toUnmodifiableList();
  }

}