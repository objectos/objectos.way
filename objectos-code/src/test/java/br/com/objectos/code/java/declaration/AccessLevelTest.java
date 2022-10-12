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

public class AccessLevelTest extends AbstractCodeJavaTest {

  class Subject {
    public void isPublic() {}
    void isDefault() {}
    protected void isProtected() {}
    @SuppressWarnings("unused")
    private void isPrivate() {}
  }

  @Test
  public void _public() {
    AccessLevel res = AccessLevel.of(getMethodElement(Subject.class, "isPublic"));
    assertEquals(res, AccessLevel.PUBLIC);
  }

  @Test
  public void _default() {
    AccessLevel res = AccessLevel.of(getMethodElement(Subject.class, "isDefault"));
    assertEquals(res, AccessLevel.DEFAULT);
  }

  @Test
  public void _protected() {
    AccessLevel res = AccessLevel.of(getMethodElement(Subject.class, "isProtected"));
    assertEquals(res, AccessLevel.PROTECTED);
  }

  @Test
  public void _private() {
    AccessLevel res = AccessLevel.of(getMethodElement(Subject.class, "isPrivate"));
    assertEquals(res, AccessLevel.PRIVATE);
  }

}