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
          _enum(id("Subject"),
            enumConstant(id("ONE"))
          );
        }
      }.toString(),

      """
      enum Subject {
        ONE;
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Subject"),
            enumConstant(id("ONE")),
            enumConstant(id("TWO"))
          );
        }
      }.toString(),

      """
      enum Subject {
        ONE,

        TWO;
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Subject"),
            enumConstant(id("ONE")),
            enumConstant(id("TWO")),
            enumConstant(id("THREE"))
          );
        }
      }.toString(),

      """
      enum Subject {
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
          _enum(id("Test"),
            enumConstant(id("ONE"), s("abc"))
          );
        }
      }.toString(),

      """
      enum Test {
        ONE("abc");
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Test"),
            enumConstant(id("ONE"), s("abc"), n("field"))
          );
        }
      }.toString(),

      """
      enum Test {
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
          _enum(
            _public(), id("Test"),
            enumConstant(id("ONE"))
          );
        }
      }.toString(),

      """
      public enum Test {
        ONE;
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(
            annotation(Deprecated.class),
            _public(), id("Test"),
            enumConstant(id("ONE"))
          );
        }
      }.toString(),

      """
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
        @Override
        protected final void definition() {
          var iface = ClassName.of(Serializable.class);

          _enum(
            id("Test"), _implements(iface),
            enumConstant(id("ONE"))
          );
        }
      }.toString(),

      """
      enum Test implements java.io.Serializable {
        ONE;
      }
      """
    );
  }

}