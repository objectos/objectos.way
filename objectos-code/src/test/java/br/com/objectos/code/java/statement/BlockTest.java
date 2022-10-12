/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.Block.block;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.Statements._var;
import static br.com.objectos.code.java.type.NamedPrimitive._int;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Collections;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class BlockTest extends AbstractCodeJavaTest {

  @Test(
      description = "An empty block should always render in a compact way"
  )
  public void empty() {
    test(Block.block(), "{}");
    test(Block.block(new BlockStatement[] {}), "{}");
    test(Block.block(UnmodifiableList.<BlockElement> of()), "{}");
  }

  @Test
  public void forArray() {
    test(
        block(
            _var(_int(), "a", l(10)),
            _return("a")
        ),
        "{",
        "  int a = 10;",
        "  return a;",
        "}");
  }

  @Test(
      description = ""
          + "isEmpty should return true only if "
          + "there are no statements inside the block"
  )
  public void isEmpty() {
    assertTrue(Block.empty().isEmpty());
    assertTrue(Block.block().isEmpty());
    assertTrue(Block.block(Collections.<BlockElement> emptyList()).isEmpty());
    assertFalse(Block.block(id("i").postInc()).isEmpty());
  }

  private void test(Block block, String... expected) {
    testToString(block, expected);
  }

}
