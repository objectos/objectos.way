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

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import objectos.code.JavaTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;

public class ObjectosCodeTest {

  @BeforeClass
  public void _beforeClass() {
  }

  @Factory
  public Object[] _factory() {
    return new Object[] {
        new ClassTest(this),
        new CompilationUnitTest(this),
        new LocalVariableTest(this),
        new MethodInvocationTest(this)
    };
  }

  void test(
      JavaTemplate template,
      int[] p0, Object[] objs,
      int[] p1, ImportSet imports,
      String expectedSource) {

  }

  final void testArrays(int[] result, int[] expected, String header) {
    var msg = """

    %s
    actual  =%s
    expected=%s

    """.formatted(header, Arrays.toString(result), Arrays.toString(expected));

    assertEquals(result, expected, msg);
  }

  final void testArrays(Object[] result, Object[] expected, String header) {
    var msg = """

    %s
    actual  =%s
    expected=%s

    """.formatted(header, Arrays.toString(result), Arrays.toString(expected));

    assertEquals(result, expected, msg);
  }

}