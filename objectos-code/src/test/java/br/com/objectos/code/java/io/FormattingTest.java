/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.io;

import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.ClassBodyElement;
import br.com.objectos.code.java.declaration.ConstructorCode;
import br.com.objectos.code.java.declaration.ConstructorCodeEx;
import br.com.objectos.code.java.declaration.FieldCode;
import br.com.objectos.code.java.element.NewLine;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class FormattingTest extends AbstractFormattingTest {

  final ConstructorCode c0 = ConstructorCodeEx._default_empty;

  final FieldCode f0 = field(_private(), _final(), _int(), id("value"));

  final FieldCode f1 = field(_private(), _final(), t(String.class), id("name"));

  final NewLine nl = NewLine.nl();

  @Test
  public void fields() {
    withElements(c0, f0, f1);

    UnmodifiableList<ClassBodyElement> res = consume(Formatting.fields());

    assertEquals(res, UnmodifiableList.of(f0, nl, f1, nl, c0));
  }

  @Test
  public void newLine() {
    withElements(f0, f1);

    UnmodifiableList<ClassBodyElement> res = consume(Formatting.newLine());

    assertEquals(res, UnmodifiableList.of(nl, f0, f1));
  }

  @Test
  public void newLineWhenEmpty() {
    withElements();

    UnmodifiableList<ClassBodyElement> res = consume(Formatting.newLine());

    assertTrue(res.isEmpty());
  }

  @Test
  public void newLineWhenNewLines() {
    withElements(nl, nl);

    UnmodifiableList<ClassBodyElement> res = consume(Formatting.newLine());

    assertTrue(res.isEmpty());
  }

  private UnmodifiableList<ClassBodyElement> consume(Formatting formatting) {
    FormattingAction action = action(formatting);

    action.consume(this);

    return action.toUnmodifiableList(ClassBodyElement.class);
  }

}