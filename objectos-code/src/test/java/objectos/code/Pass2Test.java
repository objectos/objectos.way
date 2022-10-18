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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Pass2Test extends ObjectosCodeTest {

  private Pass2 pass2;

  private JavaWriter writer;

  @BeforeClass
  @Override
  public void _beforeClass() {
    if (pass2 == null) {
      pass2 = new Pass2();

      writer = new JavaWriter();
    }
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Override
  final void test(
      JavaTemplate template,
      int[] p0,
      Object[] objs,
      int[] p1,
      String expectedSource) {
    if (p1 == null) {
      return;
    }

    pass2.execute(p1, objs, writer);

    assertEquals(writer.toString(), expectedSource);
  }

}