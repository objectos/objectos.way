/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.NewClass._new;
import static br.com.objectos.code.java.statement.ThrowStatement._throw;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ThrowStatementTest extends AbstractCodeJavaTest {

  @Test
  public void throwStatement() {
    test(
        _throw(_new(t(NullPointerException.class))),
        "throw new java.lang.NullPointerException()"
    );
  }

}
