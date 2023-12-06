/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class IntStackTest extends AbstractGitTest {

  @Test
  public void testCase12() {
    IntStack stack;
    stack = engine.getIntStack();

    try {
      assertTrue(stack.isEmpty());
      assertEquals(stack.size(), 0);

      stack.push(123);

      assertFalse(stack.isEmpty());
      assertEquals(stack.size(), 1);
      assertEquals(stack.peek(), 123);

      stack.push(456);

      assertFalse(stack.isEmpty());
      assertEquals(stack.size(), 2);
      assertEquals(stack.peek(), 456);

      assertEquals(stack.pop(), 456);

      assertFalse(stack.isEmpty());
      assertEquals(stack.size(), 1);
      assertEquals(stack.peek(), 123);

      assertEquals(stack.pop(), 123);

      assertTrue(stack.isEmpty());
      assertEquals(stack.size(), 0);
    } finally {
      engine.putIntStack(stack);
    }
  }

}