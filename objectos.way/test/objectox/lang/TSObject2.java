/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectox.lang;

class TSObject2 extends TSObject1 {

  final String name2;

  final Object value2;

  TSObject2(Object typeName, String name1, Object value1, String name2, Object value2) {
    super(typeName, name1, value1);
    this.name2 = name2;
    this.value2 = value2;
  }

  TSObject2(String name1, Object value1, String name2, Object value2) {
    super(name1, value1);
    this.name2 = name2;
    this.value2 = value2;
  }

  @Override
  public void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, typeName,
        name1, value1,
        name2, value2
    );
  }

}