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
package objectos.code;

import static objectos.code.Pass0.JMP;

import org.testng.annotations.Test;

final class CompilationUnitTest extends AbstractObjectosCodeTest {

  CompilationUnitTest(ObjectosCodeTest outer) { super(outer); }

  @Test(description = """
  class Foo {}
  """)
  public void testCase01() {
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(id("Foo"));
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 12,
        /* 2*/Pass0.IDENTIFIER, 0, JMP, 10,
        /* 6*/Pass0.CLASS, 1, JMP, 2, JMP, 16,
        /*12*/Pass0.COMPILATION_UNIT, 1, JMP, 6, JMP, 18,
        /*18*/Pass0.EOF
      ),

      objs("Foo"),

      pass1(
        Pass1.KEYWORD, Keyword.CLASS.ordinal(),
        Pass1.IDENTIFIER, 0,
        Pass1.BLOCK_START, Pass1.BLOCK_END,

        Pass1.EOF
      ),

      """
      class Foo {}
      """
    );
  }

}