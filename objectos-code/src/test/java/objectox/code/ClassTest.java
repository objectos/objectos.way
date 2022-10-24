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

import javax.lang.model.element.Modifier;
import objectos.code.ClassName;
import objectos.code.JavaTemplate;
import objectos.code.PackageName;
import org.testng.annotations.Test;

final class ClassTest extends AbstractObjectoxCodeTest {

  ClassTest(ObjectosCodeTest outer) { super(outer); }

  @Test(description = """
  final class Subject {}
  """)
  public void testCase01() {
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(_final(), id("Subject"));
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 10,
        /* 2*/Pass0.MODIFIER, 0,
        /* 4*/Pass0.IDENTIFIER, 1,
        /* 6*/Pass0.CLASS, 2, 2, 4,
        /*10*/Pass0.COMPILATION_UNIT, 1, 6
      ),

      objs(Modifier.FINAL, "Subject"),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        Pass1.NOP, // imports
        16, // body

        Pass1.CLASS,
        Pass1.NOP, // annotations
        13, // mods
        1, // name
        Pass1.NOP, // type params
        Pass1.NOP, // super
        Pass1.NOP, // implements
        Pass1.NOP, // permits
        Pass1.NOP, // body

        Pass1.LIST, 1, 0,

        Pass1.LIST, 1, 4
      ),

      imports(PackageName.of()),

      """
      final class Subject {}
      """
    );
  }

  @Test(description = """
  single annotation on class

  @java.lang.Deprecated
  class Subject {}
  """)
  public void testCase02() {
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(
            annotation(Deprecated.class),
            id("Subject")
          );
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 13,
        /* 2*/Pass0.NAME, 0,
        /* 4*/Pass0.ANNOTATION, 1, 2,
        /* 7*/Pass0.IDENTIFIER, 1,
        /* 9*/Pass0.CLASS, 2, 4, 7,
        /*13*/Pass0.COMPILATION_UNIT, 1, 9
      ),

      objs(ClassName.of(Deprecated.class), "Subject"),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        Pass1.NOP, // imports
        19, // body

        Pass1.CLASS,
        16, // annotations
        Pass1.NOP, // mods
        1, // name
        Pass1.NOP, // type params
        Pass1.NOP, // super
        Pass1.NOP, // implements
        Pass1.NOP, // permits
        Pass1.NOP, // body

        Pass1.ANNOTATION,
        0, // name
        Pass1.NOP, // element value pairs

        Pass1.LIST, 1, 13,

        Pass1.LIST, 1, 4
      ),

      imports(PackageName.of()),

      """
      @java.lang.Deprecated
      class Subject {}
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
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(
            id("Subject"),
            method(id("m0"))
          );
        }
      },

      pass0(
        /* 0*/Pass0.JMP, 13,
        /* 2*/Pass0.IDENTIFIER, 0,
        /* 4*/Pass0.IDENTIFIER, 1,
        /* 6*/Pass0.METHOD, 1, 4,
        /* 9*/Pass0.CLASS, 2, 2, 6,
        /*13*/Pass0.COMPILATION_UNIT, 1, 9
      ),

      objs("Subject", "m0"),

      pass1(
        Pass1.COMPILATION_UNIT,
        Pass1.NOP, // package
        Pass1.NOP, // imports
        26, // body

        Pass1.CLASS,
        Pass1.NOP, // annotations
        Pass1.NOP, // mods
        0, // name
        Pass1.NOP, // type params
        Pass1.NOP, // super
        Pass1.NOP, // implements
        Pass1.NOP, // permits
        23, // body

        Pass1.METHOD,
        Pass1.NOP, // annotations
        Pass1.NOP, // mods
        Pass1.NOP, // type params
        Pass1.NOP, // return
        1, // name
        Pass1.NOP, // receiver param
        Pass1.NOP, // params
        Pass1.NOP, // throws
        Pass1.NOP, // body

        Pass1.LIST, 1, 13,

        Pass1.LIST, 1, 4
      ),

      imports(PackageName.of()),

      """
      class Subject {
        void m0() {}
      }
      """
    );
  }

}