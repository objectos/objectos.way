/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
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
