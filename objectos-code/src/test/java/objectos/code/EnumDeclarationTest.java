/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import java.io.Serializable;
import org.testng.annotations.Test;

public class EnumDeclarationTest {

  @Test(description = """
  constants
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          enumDeclaration(
            name("Test1"),

            enumConstant(name("ONE"))
          );

          enumDeclaration(
            name("Test2"),

            enumConstant(name("ONE")),

            enumConstant(name("TWO"))
          );

          enumDeclaration(
            name("Test3"),

            enumConstant(name("ONE")),

            enumConstant(name("TWO")),

            enumConstant(name("THREE"))
          );
        }
      }.toString(),

      """
      enum Test1 {
        ONE;
      }

      enum Test2 {
        ONE,

        TWO;
      }

      enum Test3 {
        ONE,

        TWO,

        THREE;
      }
      """
    );
  }

  @Test(description = """
  constants + args
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          enumDeclaration(
            name("Test1"),

            enumConstant(
              name("ONE"),
              argument(s("abc"))
            )
          );

          enumDeclaration(
            name("Test2"),

            enumConstant(
              name("ONE"),
              argument(s("abc")),
              argument(n("field"))
            )
          );
        }
      }.toString(),

      """
      enum Test1 {
        ONE("abc");
      }

      enum Test2 {
        ONE("abc", field);
      }
      """
    );
  }

  @Test(description = """
  modifiers (class level)
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          enumDeclaration(
            PUBLIC, name("Test"),

            enumConstant(name("ONE"))
          );

          enumDeclaration(
            annotation(Deprecated.class),
            PUBLIC, name("Test"),

            enumConstant(name("ONE"))
          );
        }
      }.toString(),

      """
      public enum Test {
        ONE;
      }

      @java.lang.Deprecated
      public enum Test {
        ONE;
      }
      """
    );
  }

  @Test(description = """
  implements cause
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName AUTO_CLOSEABLE = ClassTypeName.of(AutoCloseable.class);

        static final ClassTypeName SERIALIZABLE = ClassTypeName.of(Serializable.class);

        @Override
        protected final void definition() {
          enumDeclaration(
            name("Test1"),
            implementsClause(SERIALIZABLE),

            enumConstant(name("ONE"))
          );

          enumDeclaration(
            name("Test2"),
            implementsClause(AUTO_CLOSEABLE, SERIALIZABLE),

            enumConstant(name("ONE"))
          );
        }
      }.toString(),

      """
      enum Test1 implements java.io.Serializable {
        ONE;
      }

      enum Test2 implements java.lang.AutoCloseable, java.io.Serializable {
        ONE;
      }
      """
    );
  }

  @Test(description = """
  Enum class declarations TC05

  - enum
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName IFACE = ClassTypeName.of("com.example", "Iface");

        static final ClassTypeName STRING = ClassTypeName.of(String.class);

        @Override
        protected final void definition() {
          enumDeclaration(
            PUBLIC, name("Test"),
            implementsClause(IFACE),

            enumConstant(
              name("A"),
              argument(s("a"))
            ),

            enumConstant(
              name("B"),
              argument(s("b"))
            ),

            field(
              PRIVATE, FINAL, STRING, name("value")
            ),

            constructor(
              PRIVATE, parameter(STRING, name("value")),

              p(THIS, n("value"), IS, n("value"))
            ),

            method(
              annotation(Override.class),
              PUBLIC, FINAL, STRING, name("toString"),
              p(RETURN, n("value"))
            )
          );
        }
      }.toString(),

      """
      public enum Test implements com.example.Iface {
        A("a"),

        B("b");

        private final java.lang.String value;

        private Test(java.lang.String value) {
          this.value = value;
        }

        @java.lang.Override
        public final java.lang.String toString() {
          return value;
        }
      }
      """
    );
  }

}