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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Pass1Test extends ObjectosCodeTest {

  private Pass1 pass1;

  @BeforeClass
  @Override
  public void _beforeClass() {
    if (pass1 == null) {
      pass1 = new Pass1();
    }
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Override
  final void test(
      JavaTemplate template,
      int[] p0,
      int[] p1,
      String expectedSource) {
    if (p1 == null) {
      return;
    }

    var source = new Pass1.Source() {
      @Override
      public final int codeAt(int index) {
        return p0[index];
      }
    };

    pass1.execute(source);

    int[] result = pass1.toArray();

    testArrays(result, p1, "Pass (1) assertion failed");
  }

}