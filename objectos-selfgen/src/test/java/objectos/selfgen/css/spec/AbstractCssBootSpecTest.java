/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css.spec;

import objectos.code.JavaTemplate;
import objectos.util.GrowableList;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractCssBootSpecTest {

  final StepAdapter adapter = new ThisStepAdapter();

  final GrowableList<String> resultList = new GrowableList<>();

  protected AbstractCssBootSpecTest() {}

  final void execute(Step step, CssSpec spec) {
    var dsl = new CssSpecDsl(step);

    spec.acceptCssSpecDsl(dsl);

    dsl.execute();
  }

  @BeforeMethod
  public final void _clearResultList() {
    resultList.clear();
  }

  private class ThisStepAdapter extends StepAdapter {
    @Override
    public final void write(JavaTemplate template) {
      var result = template.toString();

      resultList.add(result);
    }
  }

}