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

import objectos.code.ClassName;
import objectos.code.JavaTemplate;
import objectos.code.PackageName;
import org.testng.annotations.Test;

final class CompilationUnitTest extends AbstractObjectoxCodeTest {

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
        /* 0*/Pass0.JMP, 7,
        /* 2*/Pass0.IDENTIFIER, 0,
        /* 4*/Pass0.CLASS, 1, 2,
        /* 7*/Pass0.COMPILATION_UNIT, 1, 4
      ),

      objs("Foo"),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        Pass1.NOP, // imports
        6, // class/interface
        Pass1.NOP, // module
        Pass1.EOF,

        Pass1.CLASS,
        Pass1.NOP, // annotations
        Pass1.NOP, // mods
        0, // name
        Pass1.NOP, // type args
        Pass1.NOP, // super
        Pass1.NOP, // implements
        Pass1.NOP, // permits
        Pass1.NOP, // body
        Pass1.EOF // NEXT
      ),

      imports(PackageName.of()),

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
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _package("test");

          _class(id("Foo"));
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 12,
        /* 2*/Pass0.NAME, 0,
        /* 4*/Pass0.PACKAGE, 1, 2,
        /* 7*/Pass0.IDENTIFIER, 1,
        /* 9*/Pass0.CLASS, 1, 7,
        /*12*/Pass0.COMPILATION_UNIT, 2, 4, 9
      ),

      objs("test", "Foo"),

      pass1(
        Pass1.COMPILATION_UNIT,
        6, // package
        Pass1.NOP, // imports
        9, // class/interface
        Pass1.NOP, // module
        Pass1.EOF,

        Pass1.PACKAGE,
        Pass1.NOP, // annotations
        0, // name

        Pass1.CLASS,
        Pass1.NOP, // annotations
        Pass1.NOP, // mods
        1, // name
        Pass1.NOP, // type args
        Pass1.NOP, // super
        Pass1.NOP, // implements
        Pass1.NOP, // permits
        Pass1.NOP, // body
        Pass1.EOF // NEXT
      ),

      imports(TEST),

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
    var bar = ClassName.of(TEST, "Bar");

    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          autoImports();

          _class(id("Foo"), _extends(bar));
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 11,
        /* 2*/Pass0.AUTO_IMPORTS,
        /* 3*/Pass0.IDENTIFIER, 0,
        /* 5*/Pass0.EXTENDS, 1,
        /* 7*/Pass0.CLASS, 2, 3, 5,
        /*11*/Pass0.COMPILATION_UNIT, 2, 2, 7
      ),

      objs("Foo", bar),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        16, // imports
        6, // class/interface
        Pass1.NOP, // module
        Pass1.EOF,

        Pass1.CLASS,
        Pass1.NOP, // annotations
        Pass1.NOP, // mods
        0, // name
        Pass1.NOP, // type args
        1, // super
        Pass1.NOP, // implements
        Pass1.NOP, // permits
        Pass1.NOP, // body
        Pass1.EOF, // NEXT

        Pass1.IMPORT, 0,
        Pass1.EOF
      ),

      imports(PackageName.of(), bar),

      """
      import test.Bar;

      class Foo extends Bar {}
      """
    );
  }

}