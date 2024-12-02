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

import objectos.way.Web.FormData;

abstract class WebFormField {

  private final String label;

  private final String id;

  private final String name;

  WebFormField(WebFormFieldConfig config) {
    label = config.label;

    id = config.id();

    name = config.name;
  }

  WebFormField(WebFormField source) {
    label = source.label;

    id = source.id;

    name = source.name;
  }

  public final String label() {
    return label;
  }

  public final String id() {
    return id;
  }

  public final String name() {
    return name;
  }

  abstract WebFormField parse(FormData data);

}