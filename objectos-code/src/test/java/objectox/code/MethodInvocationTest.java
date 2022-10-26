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
package objectox.code;

import objectos.code.JavaTemplate;
import objectos.code.PackageName;
import org.testng.annotations.Test;

final class MethodInvocationTest extends AbstractObjectoxCodeTest {

  MethodInvocationTest(ObjectosCodeTest outer) { super(outer); }

  @Test(description = """
  - unqualified
  - no args
  """)
  public void testCase01() {
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke(name("test"));
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 7,
        /* 2*/Pass0.NAME, 0,
        /* 4*/Pass0.METHOD_INVOCATION, 1, 2,
        /* 7*/Pass0.COMPILATION_UNIT, 1, 4
      ),

      objs("test"),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        Pass1.NOP, // imports
        9, // body

        Pass1.METHOD_INVOCATION,
        Pass1.NOP, // callee
        Pass1.NOP, // type args
        0, // name
        Pass1.NOP, // args

        Pass1.LIST, 1, 4
      ),

      imports(PackageName.of()),

      """
      test();
      """
    );
  }

  @Test(description = """
  - unqualified
  - single argument
  """)
  public void testCase02() {
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke(name("test"), s("a"));
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 10,
        /* 2*/Pass0.NAME, 0,
        /* 4*/Pass0.STRING_LITERAL, 1,
        /* 6*/Pass0.METHOD_INVOCATION, 2, 2, 4,
        /*10*/Pass0.COMPILATION_UNIT, 1, 6
      ),

      objs("test", "a"),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        Pass1.NOP, // imports
        14, // body

        Pass1.METHOD_INVOCATION,
        Pass1.NOP, // callee
        Pass1.NOP, // type args
        0, // name
        11, // args

        Pass1.STRING_LITERAL, 1,

        Pass1.LIST, 1, 9,

        Pass1.LIST, 1, 4
      ),

      imports(PackageName.of()),

      """
      test("a");
      """
    );
  }

}