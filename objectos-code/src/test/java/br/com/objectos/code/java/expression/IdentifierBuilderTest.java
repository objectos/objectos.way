/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.idBuilder;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class IdentifierBuilderTest {

  @Test
  public void build() {
    assertEquals(
        idBuilder().append("div").build(),
        id("div")
    );
    assertEquals(
        idBuilder().repeat('_', 2).append("div").build(),
        id("__div")
    );
  }
  
}
