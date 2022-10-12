/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.element;

import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes._int;

import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.io.BodyFormatter;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class NewLineTest extends AbstractCodeJavaTest {

  @Test
  public void single() {
    testToString(
        ClassCode.builder()
            .addNewLine(NewLine.nl())
            .addField(field(_private(), _final(), _int(), id("value")))
            .addNewLine(NewLine.nl())
            .buildWith(BodyFormatter.unformatted()),
        "class Unnamed {",
        "",
        "  private final int value;",
        "",
        "}");
  }

}