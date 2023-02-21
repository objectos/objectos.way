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

import java.io.IOException;
import org.testng.annotations.Test;

public class ThrowStatementTest {

  @Test(description = """
  The throw statement TC01

  - throw + new
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName IO_EXCEPTION = classType(IOException.class);

        @Override
        protected final void definition() {
          _class("Throw");
          body(
            method(
              p(THROW, NEW, IO_EXCEPTION)
            )
          );
        }
      }.toString(),

      """
      class Throw {
        void unnamed() {
          throw new java.io.IOException();
        }
      }
      """
    );
  }

}