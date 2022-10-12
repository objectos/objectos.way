/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.io;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class IndentationTest {

  @Test
  public void start() {
    Indentation it = it();
    test(it, ".");
    it = it.nextLine();
    test(it, ".");
  }

  @Test
  public void block() {
    Indentation it = it().push(Section.BLOCK);
    test(it, ".");
    it = it.nextLine();
    test(it, "  .");
    it = it.nextLine();
    test(it, "  .");
    it = it.push(Section.BLOCK).nextLine();
    test(it, "    .");
    it = it.nextLine();
    test(it, "    .");
    it = it.pop();
    test(it, "  .");
  }

  @Test
  public void statement() {
    Indentation it = it().push(Section.STATEMENT);
    test(it, ".");
    it = it.nextLine();
    test(it, "    .");
    it = it.nextLine();
    test(it, "    .");
    it = it.pop().push(Section.STATEMENT).nextLine();
    test(it, "    .");
    it = it.pop();
    test(it, ".");
  }

  @Test
  public void simulate_whole_file() {
    Indentation it = it();
    test(it, "."); // package
    it = it.nextLine();
    test(it, "."); // import
    it = it.nextLine();
    test(it, "."); // class
    it = it.push(Section.BLOCK).nextLine();
    test(it, "  ."); // field
    it = it.nextLine();
    test(it, "  ."); // method
    it = it.push(Section.BLOCK).nextLine();
    it = it.push(Section.STATEMENT);
    test(it, "    ."); // method body
    it = it.pop().nextLine();
    it = it.push(Section.STATEMENT);
    test(it, "    ."); // if start
    it = it.push(Section.BLOCK).nextLine();
    it = it.push(Section.STATEMENT);
    test(it, "      ."); // if body
  }

  private Indentation it() {
    return Indentation.start();
  }

  private void test(Indentation stack, String expected) {
    CodeWriter w = CodeWriter.forToString();
    stack.acceptCodeWriter(w);
    assertEquals(w.toString() + ".", expected);
  }

}