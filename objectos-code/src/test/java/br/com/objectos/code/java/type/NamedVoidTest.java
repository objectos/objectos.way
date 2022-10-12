/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.type;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class NamedVoidTest {

  @Test
  public void isVoid() {
    assertTrue(NamedVoid._void().isVoid());
  }

}
