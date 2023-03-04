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
package objectos.selfgen.html;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ElementValueIfaceStepTest {

  @Test(description = "it should generate an interface for each element defined")
  public void execute() {
    var spec = new AbstractHtmlSpec() {
      @Override
      protected final void definition() {
        element("div");
        element("span");
      }
    }.toSpecDsl();

    var elements = spec.elements().iterator();

    var template = new ElementValueIfaceStep();

    template.element = elements.next();

    assertEquals(
      template.toString(),

      """
      package br.com.objectos.html.spi.type;

      public interface DivValue extends Value {}
      """
    );

    template.element = elements.next();

    assertEquals(
      template.toString(),

      """
      package br.com.objectos.html.spi.type;

      public interface SpanValue extends Value {}
      """
    );
  }

}