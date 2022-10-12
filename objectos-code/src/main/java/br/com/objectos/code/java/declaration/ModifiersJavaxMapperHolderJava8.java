/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import java.util.Map;
import javax.lang.model.element.Modifier;

class ModifiersJavaxMapperHolderJava8 extends Modifiers.JavaxMapperHolder {

  static final Modifiers.JavaxMapperHolder INSTANCE = new ModifiersJavaxMapperHolderJava8();

  ModifiersJavaxMapperHolderJava8() {}

  @Override
  void addMore(Map<Modifier, br.com.objectos.code.java.declaration.Modifier> map) {
    map.put(javax.lang.model.element.Modifier.DEFAULT, Modifiers._default());
  }

}
