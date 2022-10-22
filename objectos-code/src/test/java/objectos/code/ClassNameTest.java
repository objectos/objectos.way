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

import objectos.code.ClassNameTest.Nest1.Nest2;
import objectos.code.ClassNameTest.Nest1.Nest2.Nest3;
import org.testng.annotations.Test;

public class ClassNameTest {

  class Nest1 {
    class Nest2 {
      class Nest3 {}
    }
  }

  @Test
  public void of_Class() {
    assertEquals(
      ClassName.of(ClassNameTest.class).toString(),
      "objectos.code.ClassNameTest"
    );
    assertEquals(
      ClassName.of(Nest1.class).toString(),
      "objectos.code.ClassNameTest.Nest1"
    );
    assertEquals(
      ClassName.of(Nest2.class).toString(),
      "objectos.code.ClassNameTest.Nest1.Nest2"
    );
    assertEquals(
      ClassName.of(Nest3.class).toString(),
      "objectos.code.ClassNameTest.Nest1.Nest2.Nest3"
    );
  }

}
