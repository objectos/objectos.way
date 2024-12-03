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
import objectos.way.Web.FormData;

sealed abstract class WebFormField implements Web.Form.Field permits WebFormTextInput {

  final String label;

  final String id;

  final String name;

  final String requiredMessage;

  private final List<WebFormError> errors;

  WebFormField(WebFormFieldConfig config) {
    label = config.label;

    id = config.id();

    name = config.name;

    requiredMessage = config.requiredMessage;

    errors = List.of();
  }

  WebFormField(WebFormField source, List<WebFormError> errors) {
    label = source.label;

    id = source.id;

    name = source.name;

    requiredMessage = source.requiredMessage;

    this.errors = errors != null ? errors : List.of();
  }

  @Override
  public final boolean isValid() {
    return errors.isEmpty();
  }

  @Override
  public final String label() {
    return label;
  }

  @Override
  public final String id() {
    return id;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean required() {
    return requiredMessage != null;
  }

  @Override
  public final List<? extends Web.Form.Error> errors() {
    return errors;
  }

  abstract WebFormField parse(FormData data);

}