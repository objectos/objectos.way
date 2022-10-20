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

import objectos.code.JavaTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Pass0Test extends ObjectosCodeTest {

  private Pass0 pass0;

  @BeforeClass
  @Override
  public void _beforeClass() {
    if (pass0 == null) {
      pass0 = new Pass0();
    }
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Override
  final void test(
      JavaTemplate template,
      int[] p0, Object[] objs,
      int[] p1, ImportSet imports,
      String expectedSource) {
    if (p0 == null) {
      return;
    }

    pass0.templateStart();

    template.eval(pass0);

    pass0.templateEnd();

    testArrays(
      pass0.toCodes(), p0, "Process (0) assertion failed"
    );

    testArrays(
      pass0.toObjects(), objs, "Process (0) assertion failed"
    );
  }

}