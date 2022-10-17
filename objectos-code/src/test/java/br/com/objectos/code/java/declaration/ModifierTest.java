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

import static org.testng.Assert.assertEquals;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ModifierTest extends AbstractCodeJavaTest {

  @Test
  public void fromJavaxModifier() {
    for (javax.lang.model.element.Modifier m : javax.lang.model.element.Modifier.values()) {
      Modifier modifier;
      modifier = Modifiers.of(m);

      assertEquals(modifier.toString(), m.toString());
    }
  }

  @Test
  public void testToString() {
    assertEquals(Modifiers._public().toString(), "public");
    assertEquals(Modifiers._protected().toString(), "protected");
    assertEquals(Modifiers._private().toString(), "private");
    assertEquals(Modifiers._abstract().toString(), "abstract");
    assertEquals(Modifiers._static().toString(), "static");
    assertEquals(Modifiers._sealed().toString(), "sealed");
    assertEquals(Modifiers._nonSealed().toString(), "non-sealed");
    assertEquals(Modifiers._final().toString(), "final");
    assertEquals(Modifiers._transient().toString(), "transient");
    assertEquals(Modifiers._volatile().toString(), "volatile");
    assertEquals(Modifiers._synchronized().toString(), "synchronized");
    assertEquals(Modifiers._native().toString(), "native");
    assertEquals(Modifiers._strictfp().toString(), "strictfp");
  }

}
