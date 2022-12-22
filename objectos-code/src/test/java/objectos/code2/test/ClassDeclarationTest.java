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
package objectos.code2.test;

import static org.testng.Assert.assertEquals;

import objectos.code2.JavaTemplate;
import org.testng.annotations.Test;

public class ClassDeclarationTest {

  @Test(enabled = false, description = """
  final class Subject {}
  """)
  public void testCase01() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _final(); _class("A");

          //_final(); _class("B"); body();
        }
      }.toString(),

      """
      final class A {}

      final class B {}
      """
    );
    // @formatter:on
  }

}