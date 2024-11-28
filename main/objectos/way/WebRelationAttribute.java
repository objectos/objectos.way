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

abstract class WebRelationAttribute {

  private final String name;

  private final String description;

  private final boolean required;

  public WebRelationAttribute(WebRelationAttributeConfig config) {
    name = config.name;

    description = config.description;

    required = config.required;
  }

  public final String name() {
    return name;
  }

  public final String description() {
    return description;
  }

  public final boolean required() {
    return required;
  }

}