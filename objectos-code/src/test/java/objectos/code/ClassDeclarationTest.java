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

public class ClassDeclarationTest {

  @Test(description = """
  final class Subject {}
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            FINAL, name("Subject")
          );
        }
      }.toString(),

      """
      final class Subject {}
      """
    );
  }

  @Test(description = """
  annotations on class

  @java.lang.Deprecated
  class Subject {}
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A
            = ClassTypeName.of("com.example", "A");
        static final ClassTypeName B
            = ClassTypeName.of("com.example", "B");

        @Override
        protected final void definition() {
          classDeclaration(
            annotation(Deprecated.class),
            name("Subject")
          );

          classDeclaration(
            annotation(A),
            annotation(B),
            name("Other")
          );
        }
      }.toString(),

      """
      @java.lang.Deprecated
      class Subject {}

      @com.example.A
      @com.example.B
      class Other {}
      """
    );
  }

  @Test(description = """
  single method

  class Subject {
    void m0() {}
  }
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Subject"),

            method(
              name("m0")
            )
          );
        }
      }.toString(),

      """
      class Subject {
        void m0() {}
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC04

  - allow includes
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test"),

            include(this::includeTest)
          );
        }

        private void includeTest() {
          field(INT, name("a"));
        }
      }.toString(),

      """
      class Test {
        int a;
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC05

  - nested class declaration
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Level1"),

            classDeclaration(
              name("A")
            )
          );

          classDeclaration(
            name("Level2"),

            classDeclaration(
              name("A"),

              classDeclaration(
                name("B")
              )
            )
          );

          classDeclaration(
            name("Level3"),

            classDeclaration(
              name("A"),

              classDeclaration(
                name("B"),

                classDeclaration(
                  name("C")
                )
              )
            )
          );
        }
      }.toString(),

      """
      class Level1 {
        class A {}
      }

      class Level2 {
        class A {
          class B {}
        }
      }

      class Level3 {
        class A {
          class B {
            class C {}
          }
        }
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC06

  - class modifiers
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            PUBLIC, name("A")
          );

          classDeclaration(
            PROTECTED, name("B")
          );

          classDeclaration(
            PRIVATE, name("C")
          );

          classDeclaration(
            STATIC, name("D")
          );

          classDeclaration(
            ABSTRACT, name("E")
          );

          classDeclaration(
            FINAL, name("F")
          );

          classDeclaration(
            SEALED, name("G")
          );

          classDeclaration(
            NON_SEALED, name("H")
          );
        }
      }.toString(),

      """
      public class A {}

      protected class B {}

      private class C {}

      static class D {}

      abstract class E {}

      final class F {}

      sealed class G {}

      non-sealed class H {}
      """
    );
  }

  @Test(description = """
  Class declarations TC07

  - implements clause
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName AUTO_CLOSEABLE = ClassTypeName.of(AutoCloseable.class);

        static final ClassTypeName FOO = ClassTypeName.of("com.example", "Foo");

        static final ClassTypeName SERIALIZABLE = ClassTypeName.of(Serializable.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("A"),
            implementsClause(AUTO_CLOSEABLE)
          );

          classDeclaration(
            name("B"),
            implementsClause(AUTO_CLOSEABLE, SERIALIZABLE)
          );

          classDeclaration(
            name("C"),
            extendsClause(FOO),
            implementsClause(AUTO_CLOSEABLE, SERIALIZABLE)
          );
        }
      }.toString(),

      """
      class A implements java.lang.AutoCloseable {}

      class B implements java.lang.AutoCloseable, java.io.Serializable {}

      class C extends com.example.Foo implements java.lang.AutoCloseable, java.io.Serializable {}
      """
    );
  }

  @Test(description = """
  Class declarations TC08

  - annotated method
  """)
  public void testCase08() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName SERIALIZABLE = ClassTypeName.of(Serializable.class);

        static final ClassTypeName THREAD = ClassTypeName.of(Thread.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("A"),
            extendsClause(THREAD),
            method(
              annotation(Override.class),
              VOID, name("foo")
            )
          );

          classDeclaration(
            name("B"),
            implementsClause(SERIALIZABLE),
            method(
              annotation(Override.class),
              VOID, name("foo")
            )
          );
        }
      }.toString(),

      """
      class A extends java.lang.Thread {
        @java.lang.Override
        void foo() {}
      }

      class B implements java.io.Serializable {
        @java.lang.Override
        void foo() {}
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC09

  - more than one member
  """)
  public void testCase09() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("A"),

            field(INT, name("value")),

            method(
              INT, name("value"),
              p(RETURN, n("value"))
            )
          );
        }
      }.toString(),

      """
      class A {
        int value;

        int value() {
          return value;
        }
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC10

  - nested interface declaration
  """)
  public void testCase10() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("A"),

            interfaceDeclaration(
              name("X")
            ),

            enumDeclaration(
              name("Y"),

              enumConstant(
                name("INSTANCE")
              )
            )
          );
        }
      }.toString(),

      """
      class A {
        interface X {}

        enum Y {
          INSTANCE;
        }
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC11

  - class type parameters
  """)
  public void testCase11() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName CHARSEQ = ClassTypeName.of(CharSequence.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("A"),
            typeParameter("T")
          );

          classDeclaration(
            name("B"),
            typeParameter("T1"),
            typeParameter("T2")
          );

          classDeclaration(
            name("C"),
            typeParameter("X", CHARSEQ),
            typeParameter("Y")
          );
        }
      }.toString(),

      """
      class A<T> {}

      class B<T1, T2> {}

      class C<X extends java.lang.CharSequence, Y> {}
      """
    );
  }

  @Test(description = """
  Class declarations TC12

  - extendsClause: last one wins
  """)
  public void testCase12() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A = ClassTypeName.of("com.example", "A");
        static final ClassTypeName B = ClassTypeName.of("com.example", "B");
        static final ClassTypeName C = ClassTypeName.of("com.example", "C");

        @Override
        protected final void definition() {
          classDeclaration(
            name("Foo"),
            extendsClause(A),
            extendsClause(B),
            extendsClause(C)
          );
        }
      }.toString(),

      """
      class Foo extends com.example.C {}
      """
    );
  }

  @Test(description = """
  Class declarations TC13

  - permits clause
  """)
  public void testCase13() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A = ClassTypeName.of("com.example", "A");
        static final ClassTypeName B = ClassTypeName.of("com.example", "B");
        static final ClassTypeName C = ClassTypeName.of("com.example", "C");

        @Override
        protected final void definition() {
          autoImports();

          classDeclaration(
            name("Test1"),
            permitsClause(A)
          );

          classDeclaration(
            name("Test2"),
            permitsClause(A, B, C)
          );

          classDeclaration(
            name("Test3"),
            implementsClause(A),
            permitsClause(B, C)
          );

          classDeclaration(
            name("Test4"),
            extendsClause(A),
            implementsClause(B),
            permitsClause(C)
          );
        }
      }.toString(),

      """
      import com.example.A;
      import com.example.B;
      import com.example.C;

      class Test1 permits A {}

      class Test2 permits A, B, C {}

      class Test3 implements A permits B, C {}

      class Test4 extends A implements B permits C {}
      """
    );
  }

}