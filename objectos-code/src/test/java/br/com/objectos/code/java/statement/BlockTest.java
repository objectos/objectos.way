/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
