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

public class GeneratedAbstractTemplateStepTest {

  private final GeneratedAbstractTemplateStep template = new GeneratedAbstractTemplateStep();

  @Test(description = """
  it should generate methods for each element.
  for each element, it should generate attribute overloads.
  """)
  public void execute() {
    template.spec = new HtmlSelfGen() {
      @Override
      protected final void definition() {
        template()
            .maxLevel(1)
            .maxArity(1)
            .skipText("form");

        element("div");
        element("form");
        element("meta").noEndTag();
      }
    }.prepare();

    assertEquals(
      template.toString(),

      """
      package br.com.objectos.html.tmpl;

      import br.com.objectos.html.attribute.StandardAttributeName;
      import br.com.objectos.html.element.ElementName;
      import br.com.objectos.html.element.StandardElementName;
      import br.com.objectos.html.spi.type.DivValue;
      import br.com.objectos.html.spi.type.FormValue;
      import br.com.objectos.html.spi.type.MetaValue;
      import br.com.objectos.html.spi.type.Value;

      abstract class GeneratedAbstractTemplate {
        public final ElementName div(DivValue... values) {
          return addStandardElement(StandardElementName.DIV, values);
        }

        public final ElementName div(String text) {
          return addStandardElement(StandardElementName.DIV, text);
        }

        public final ElementName form(FormValue... values) {
          return addStandardElement(StandardElementName.FORM, values);
        }

        public final ElementName meta(MetaValue... values) {
          return addStandardElement(StandardElementName.META, values);
        }

        abstract <N extends StandardAttributeName> N addStandardAttribute(N name);

        abstract <N extends StandardAttributeName> N addStandardAttribute(N name, String value);

        abstract ElementName addStandardElement(StandardElementName name, String text);

        abstract ElementName addStandardElement(StandardElementName name, Value[] values);
      }
      """
    );
  }

  @Test(description = "it should generate methods for each attribute.")
  public void executeAttributes() {
    template.spec = new HtmlSelfGen() {
      @Override
      protected final void definition() {
        template()
            .maxLevel(1)
            .maxArity(1)
            .skipAttribute("title");

        rootElement()
            .attribute("s")
            .attribute("b").booleanType()
            .attribute("t").as("title")
            .attributeEnd();
      }
    }.prepare();

    assertEquals(
      template.toString(),

      """
      package br.com.objectos.html.tmpl;

      import br.com.objectos.html.attribute.StandardAttributeName;
      import br.com.objectos.html.attribute.StandardAttributeName.B;
      import br.com.objectos.html.attribute.StandardAttributeName.S;
      import br.com.objectos.html.element.ElementName;
      import br.com.objectos.html.element.StandardElementName;
      import br.com.objectos.html.spi.type.Value;

      abstract class GeneratedAbstractTemplate {
        public final B b() {
          return addStandardAttribute(StandardAttributeName.B);
        }

        public final S s(String value) {
          return addStandardAttribute(StandardAttributeName.S, value);
        }

        abstract <N extends StandardAttributeName> N addStandardAttribute(N name);

        abstract <N extends StandardAttributeName> N addStandardAttribute(N name, String value);

        abstract ElementName addStandardElement(StandardElementName name, String text);

        abstract ElementName addStandardElement(StandardElementName name, Value[] values);
      }
      """
    );
  }

}
