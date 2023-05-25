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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class PrimitiveTypeStepTest extends AbstractCssBootSpecTest {

  @Test
  public void execute() {
    execute(
      new PrimitiveTypeStep(adapter),

      new CssSpec() {
        @Override
        protected final void definition() {
          PrimitiveType length = primitive(Primitive.LENGTH);
          PrimitiveType percentage = primitive(Primitive.PERCENTAGE);

          property(
            "bottom",
            formal("", Source.MANUAL_ENTRY),
            sig(t("BottomValue", length, percentage), "value")
          );
        }
      }
    );

    assertEquals(resultList.size(), 2);

    assertEquals(
      resultList.get(0),

      """
      package br.com.objectos.css.type;

      public interface LengthType extends BottomValue, Value {}
      """
    );

    assertEquals(
      resultList.get(1),

      """
      package br.com.objectos.css.type;

      public interface PercentageType extends BottomValue, Value {}
      """
    );
  }

}
