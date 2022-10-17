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

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class MethodModifierTest extends AbstractCodeJavaTest {

  @Test
  public void _public_static_final() {
    MethodModifierSet res = MethodModifierSet._public()._static()._final().build();
    test(res, "public static final");
  }

  @Test
  public void merge() {
    MethodModifierSet res = MethodModifierSet._public()._static()
        .withModifier(MethodModifierSet._final().build())
        .build();
    test(res, "public static final");
  }

  private void test(MethodModifierSet mod, String... lines) {
    testToString(mod, lines);
  }

}