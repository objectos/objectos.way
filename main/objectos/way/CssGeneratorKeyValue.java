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

record CssGeneratorKeyValue(String key, String value) implements Css.Generator.KeyValue {

  @Override
  public final String getKey() {
    return key;
  }

  @Override
  public final String getValue() {
    return value;
  }

  @Override
  public final String setValue(String value) {
    throw new UnsupportedOperationException();
  }

}