/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

class TSObject1 extends TSObject0 {

  final String name1;

  final Object value1;

  TSObject1(Object typeName, String name1, Object value1) {
    super(typeName);
    this.name1 = name1;
    this.value1 = value1;
  }

  TSObject1(String name1, Object value1) {
    this.name1 = name1;
    this.value1 = value1;
  }

  @Override
  public void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, typeName,
        name1, value1
    );
  }

}