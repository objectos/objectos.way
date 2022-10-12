/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class JavaNamesTest {

  @Test
  public void toValidClassName() {
    assertEquals(JavaNames.toValidClassName("foo-bar"), "FooBar");
  }

  @Test
  public void toValidMethodName() {
    assertEquals(JavaNames.toValidMethodName("foo-bar"), "fooBar");
  }

}