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

import objectos.code.Types.IfaceA;
import objectos.code.Types.IfaceB;
import objectos.code.Types.IfaceC;
import org.testng.annotations.Test;

public class InterfaceDeclarationTest {

  @Test(description = """
  Interface declaration TC01

  - identifier
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          interfaceDeclaration(
            name("A")
          );
        }
      }.toString(),

      """
      interface A {}
      """
    );
  }

  @Test(description = """
  Interface declaration TC02

  - modifiers
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          interfaceDeclaration(
            PUBLIC, name("A")
          );

          interfaceDeclaration(
            annotation(TypeAnnotation.class),
            name("B")
          );

          interfaceDeclaration(
            annotation(TypeAnnotation.class),
            PROTECTED, name("C")
          );

          interfaceDeclaration(
            annotation(TypeAnnotation.class),
            PRIVATE, STATIC, name("D")
          );

          interfaceDeclaration(
            SEALED, name("E")
          );

          interfaceDeclaration(
            NON_SEALED, name("F")
          );
        }
      }.toString(),

      """
      public interface A {}

      @objectos.code.TypeAnnotation
      interface B {}

      @objectos.code.TypeAnnotation
      protected interface C {}

      @objectos.code.TypeAnnotation
      private static interface D {}

      sealed interface E {}

      non-sealed interface F {}
      """
    );
  }

  @Test(description = """
  Interface declaration TC03

  - extends clause
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName IFACE_A = ClassTypeName.of(IfaceA.class);
        static final ClassTypeName IFACE_B = ClassTypeName.of(IfaceB.class);
        static final ClassTypeName IFACE_C = ClassTypeName.of(IfaceC.class);

        @Override
        protected final void definition() {
          interfaceDeclaration(
            name("A"), extendsClause(IFACE_A)
          );

          interfaceDeclaration(
            name("B"), extendsClause(IFACE_A, IFACE_B)
          );

          interfaceDeclaration(
            name("C"), extendsClause(IFACE_A, IFACE_B, IFACE_C)
          );
        }
      }.toString(),

      """
      interface A extends objectos.code.Types.IfaceA {}

      interface B extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB {}

      interface C extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB, objectos.code.Types.IfaceC {}
      """
    );
  }

  @Test(description = """
  Interface declarations TC04

  - permits clause
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A = ClassTypeName.of("com.example", "A");
        static final ClassTypeName B = ClassTypeName.of("com.example", "B");
        static final ClassTypeName C = ClassTypeName.of("com.example", "C");

        @Override
        protected final void definition() {
          autoImports();

          interfaceDeclaration(
            name("Test1"),
            permitsClause(A)
          );

          interfaceDeclaration(
            name("Test2"),
            permitsClause(A, B, C)
          );

          interfaceDeclaration(
            name("Test3"),
            extendsClause(A),
            permitsClause(B, C)
          );

          interfaceDeclaration(
            name("Test4"),
            extendsClause(A, B),
            permitsClause(C)
          );
        }
      }.toString(),

      """
      import com.example.A;
      import com.example.B;
      import com.example.C;

      interface Test1 permits A {}

      interface Test2 permits A, B, C {}

      interface Test3 extends A permits B, C {}

      interface Test4 extends A, B permits C {}
      """
    );
  }

  @Test(description = """
  Interface declarations TC05

  - type parameters
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName CHARSEQ = ClassTypeName.of(CharSequence.class);

        @Override
        protected final void definition() {
          interfaceDeclaration(
            name("A"),
            typeParameter("T")
          );

          interfaceDeclaration(
            name("B"),
            typeParameter("T1"),
            typeParameter("T2")
          );

          interfaceDeclaration(
            name("C"),
            typeParameter("X", CHARSEQ),
            typeParameter("Y")
          );
        }
      }.toString(),

      """
      interface A<T> {}

      interface B<T1, T2> {}

      interface C<X extends java.lang.CharSequence, Y> {}
      """
    );
  }

  @Test(description = """
  Interface declarations TC06

  - method declarations
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          interfaceDeclaration(
            name("A"),
            method(VOID, name("a"))
          );
        }
      }.toString(),

      """
      interface A {
        void a();
      }
      """
    );
  }

}