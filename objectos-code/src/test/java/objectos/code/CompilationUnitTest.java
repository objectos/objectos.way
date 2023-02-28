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

import objectos.code.type.ClassTypeName;
import org.testng.annotations.Test;

public class CompilationUnitTest {

  @Test(description = """
  empty
  """)
  public void testCase00() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {}
      }.toString(),

      """

      """
    );
  }

  @Test(description = """
  class Foo {}
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Foo")
          );
        }
      }.toString(),

      """
      class Foo {}
      """
    );
  }

  @Test(description = """
  package test;

  class Foo {}
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          packageDeclaration("test");

          classDeclaration(
            name("Foo")
          );
        }
      }.toString(),

      """
      package test;

      class Foo {}
      """
    );
  }

  @Test(description = """
  import test.Bar;

  class Foo extends Bar {}
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName BAR = ClassTypeName.of("com.example", "Bar");

        @Override
        protected final void definition() {
          autoImports();

          classDeclaration(
            name("Foo"),
            extendsClause(BAR)
          );
        }
      }.toString(),

      """
      import com.example.Bar;

      class Foo extends Bar {}
      """
    );
  }

  @Test(description = """
  autoImports() + java.lang
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName THREAD = ClassTypeName.of(Thread.class);

        @Override
        protected final void definition() {
          packageDeclaration("test");

          autoImports();

          classDeclaration(
            name("Test"),

            extendsClause(THREAD)
          );
        }
      }.toString(),

      """
      package test;

      class Test extends Thread {}
      """
    );
  }

  @Test(description = """
  autoImports() + same package
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName BAR = ClassTypeName.of("test", "Bar");

        @Override
        protected final void definition() {
          packageDeclaration("test");

          autoImports();

          classDeclaration(
            name("Test"),
            extendsClause(BAR)
          );
        }
      }.toString(),

      """
      package test;

      class Test extends Bar {}
      """
    );
  }

  @Test(description = """
  Compilation Unit TC06

  - multiple import declarations
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A = ClassTypeName.of("test", "A");
        static final ClassTypeName B = ClassTypeName.of("test", "B");
        static final ClassTypeName C = ClassTypeName.of("test", "C");

        @Override
        protected final void definition() {
          autoImports();

          classDeclaration(
            name("Test0"),
            extendsClause(A)
          );

          classDeclaration(
            name("Test1"),
            extendsClause(B)
          );

          classDeclaration(
            name("Test2"),
            extendsClause(C)
          );
        }
      }.toString(),

      """
      import test.A;
      import test.B;
      import test.C;

      class Test0 extends A {}

      class Test1 extends B {}

      class Test2 extends C {}
      """
    );
  }

  @Test(description = """
  Compilation Unit TC07

  - package declaration
  - multiple import declarations
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A = ClassTypeName.of("test", "A");
        static final ClassTypeName B = ClassTypeName.of("test", "B");
        static final ClassTypeName C = ClassTypeName.of("test", "C");

        @Override
        protected final void definition() {
          packageDeclaration("foo");

          autoImports();

          classDeclaration(
            name("Test0"),
            extendsClause(A)
          );

          classDeclaration(
            name("Test1"),
            extendsClause(B)
          );

          classDeclaration(
            name("Test2"),
            extendsClause(C)
          );
        }
      }.toString(),

      """
      package foo;

      import test.A;
      import test.B;
      import test.C;

      class Test0 extends A {}

      class Test1 extends B {}

      class Test2 extends C {}
      """
    );
  }

  @Test(description = """
  Compilation Unit TC08

  - import declarations + annotation
  """)
  public void testCase08() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName TYPE_ANNOTATION = ClassTypeName.of("bar", "TypeAnnotation");

        @Override
        protected final void definition() {
          packageDeclaration("foo");

          autoImports();

          classDeclaration(
            annotation(TYPE_ANNOTATION),
            name("Test")
          );
        }
      }.toString(),

      """
      package foo;

      import bar.TypeAnnotation;

      @TypeAnnotation
      class Test {}
      """
    );
  }

  @Test(description = """
  Compilation Unit TC09

  - import declarations + modifier
  """)
  public void testCase09() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          packageDeclaration("foo");

          autoImports();

          classDeclaration(
            PUBLIC, name("Test"),
            extendsClause(ClassTypeName.of("bar", "Super"))
          );
        }
      }.toString(),

      """
      package foo;

      import bar.Super;

      public class Test extends Super {}
      """
    );
  }

}