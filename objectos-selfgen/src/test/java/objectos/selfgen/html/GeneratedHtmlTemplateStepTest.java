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

public class GeneratedHtmlTemplateStepTest {

  private final GeneratedHtmlTemplateStep template = new GeneratedHtmlTemplateStep();

  @Test
  public void execute() {
    template.spec = new HtmlSelfGen() {
      @Override
      protected final void definition() {
        rootElement()
            .attribute("lang");

        element("a").simpleName("Anchor");

        element("meta").noEndTag();

        element("option")
            .attribute("disabled").booleanType()
            .attribute("label");

        element("select")
            .attribute("disabled").booleanType();
      }
    }.prepare();

    assertEquals(
      template.toString(),

      """
      package objectos.html.internal;

      import objectos.html.tmpl.Instruction;
      import objectos.html.tmpl.Instruction.AnchorInstruction;
      import objectos.html.tmpl.Instruction.DisabledAttribute;
      import objectos.html.tmpl.Instruction.ElementContents;
      import objectos.html.tmpl.Instruction.GlobalAttribute;
      import objectos.html.tmpl.Instruction.MetaInstruction;
      import objectos.html.tmpl.Instruction.OptionInstruction;
      import objectos.html.tmpl.Instruction.SelectInstruction;
      import objectos.html.tmpl.StandardAttributeName;
      import objectos.html.tmpl.StandardElementName;

      abstract class GeneratedHtmlTemplate {
        public final ElementContents a(AnchorInstruction... contents) {
          element(StandardElementName.A, contents);
          return InternalInstruction.INSTANCE;
        }

        public final ElementContents a(String text) {
          element(StandardElementName.A, text);
          return InternalInstruction.INSTANCE;
        }

        public final ElementContents meta(MetaInstruction... contents) {
          element(StandardElementName.META, contents);
          return InternalInstruction.INSTANCE;
        }

        public final ElementContents option(OptionInstruction... contents) {
          element(StandardElementName.OPTION, contents);
          return InternalInstruction.INSTANCE;
        }

        public final ElementContents option(String text) {
          element(StandardElementName.OPTION, text);
          return InternalInstruction.INSTANCE;
        }

        public final ElementContents select(SelectInstruction... contents) {
          element(StandardElementName.SELECT, contents);
          return InternalInstruction.INSTANCE;
        }

        public final ElementContents select(String text) {
          element(StandardElementName.SELECT, text);
          return InternalInstruction.INSTANCE;
        }

        public final DisabledAttribute disabled() {
          attribute(StandardAttributeName.DISABLED);
          return InternalInstruction.INSTANCE;
        }

        public final OptionInstruction label(String value) {
          attribute(StandardAttributeName.LABEL, value);
          return InternalInstruction.INSTANCE;
        }

        public final GlobalAttribute lang(String value) {
          attribute(StandardAttributeName.LANG, value);
          return InternalInstruction.INSTANCE;
        }

        abstract void attribute(StandardAttributeName name);

        abstract void attribute(StandardAttributeName name, String value);

        abstract void element(StandardElementName name, String text);

        abstract void element(StandardElementName name, Instruction[] contents);
      }
      """
    );
  }

}
