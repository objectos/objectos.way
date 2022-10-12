/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.io;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.List;
import org.testng.annotations.Test;

public class CodeWriterTest extends AbstractCodeJavaTest {

  @Test
  public void nextLineShouldWorkAsWriteSpaceOffResetLength() {
    testToString(
        it().write("int a")
            .beginSection(Section.BLOCK)
            .nextLine()
            .writePreIndentation()
            .write("int b"),

        "int a",
        "  int b"
    );
  }

  @Test
  public void preIndentationShouldWriteIndentationIfAtStartOfLine() {
    testToString(
        it().writePreIndentation()
            .write("0")
            .beginSection(Section.BLOCK)
            .nextLine()
            .writePreIndentation()
            .write("1")
            .beginSection(Section.BLOCK)
            .nextLine()
            .writePreIndentation()
            .write("2"),

        "0",
        "  1",
        "    2"
    );
  }

  @Test
  public void typeNameShouldNotAutoSpace() {
    testToString(
        it().writeNamedType(NamedClass.of(String.class))
            .writeNamedType(NamedClass.of(List.class)),

        "java.lang.Stringjava.util.List"
    );
  }

  @Test
  public void pushSimpleName() {
    testToString(
        it().pushSimpleName("Constructor")
            .writeSimpleName()
            .write("()")
            .popSimpleName(),

        "Constructor()"
    );
  }

  @Test
  public void autoIndentation() {
    testToString(
        it().write("class Foo {")
            .beginSection(Section.BLOCK)
            .nextLine()
            .writePreIndentation()
            .write("private final String name").write(';')
            .nextLine()
            .writePreIndentation()
            .write("private final int value").write(';')
            .nextLine()
            .endSection()
            .write('}'),

        "class Foo {",
        "  private final String name;",
        "  private final int value;",
        "}"
    );
  }

  @Test
  public void autoIndentationSplitInImplements() {
    testToString(
        it().beginSection(Section.TYPE)
            .write("class Foo implements").nextLine()
            .writePreIndentation()
            .write("Runnable {}")
            .endSection(),

        "class Foo implements",
        "    Runnable {}"
    );
  }

  @Test
  public void autoIndentationInsideMethod() {
    testToString(
        it().write("boolean foo() {")
            .beginSection(Section.BLOCK)
            .nextLine()
            .beginSection(Section.STATEMENT)
            .writePreIndentation()
            .write("return a == b")
            .nextLine()
            .writePreIndentation()
            .write("&& c == d;")
            .nextLine()
            .endSection()
            .endSection()
            .write('}'),

        "boolean foo() {",
        "  return a == b",
        "      && c == d;",
        "}"
    );
  }

  private CodeWriter it() {
    return CodeWriter.forToString();
  }

}