/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated;

import objectos.css.tmpl.Api;
import objectos.css.util.ClassSelector;
import objectos.css.util.CustomProperty;

/**
 * Utils...
 */
final class U {

  private U() {}

  public static ClassSelector nextClass() {
    return ClassSelector.randomClassSelector(5);
  }

  public static <T extends Api.PropertyValue> CustomProperty<T> nextProp() {
    return CustomProperty.randomName(5);
  }

  public static ClassSelector cs(String name) {
    return ClassSelector.of(name);
  }

  public static <T extends Api.PropertyValue> CustomProperty<T> prop(String name) {
    return CustomProperty.named(name);
  }

}