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

public class InstructionIfaceStepTest {

  private final InstructionIfaceStep template = new InstructionIfaceStep();

  @Test
  public void execute() {
    template.spec = new HtmlSelfGen() {
      @Override
      protected final void definition() {
        element("a").simpleName("Anchor");

        element("div");

        element("meta")
            .noEndTag();

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
      package objectos.html.tmpl;

      import java.util.Iterator;
      import objectos.html.HtmlTemplate;
      import objectos.html.HtmlTemplate2;
      import objectos.html.internal.InternalFragment;
      import objectos.html.internal.InternalInstruction;
      import objectos.html.internal.InternalNoOp;

      public sealed interface Instruction {
        sealed interface AnchorInstruction extends Instruction {}

        sealed interface DivInstruction extends Instruction {}

        sealed interface MetaInstruction extends Instruction {}

        sealed interface OptionInstruction extends Instruction {}

        sealed interface SelectInstruction extends Instruction {}

        sealed interface DisabledAttribute extends OptionInstruction, SelectInstruction permits InternalInstruction {}

        sealed interface AmbiguousInstruction extends
            AnchorInstruction,
            DivInstruction,
            MetaInstruction,
            OptionInstruction,
            SelectInstruction permits InternalInstruction {}

        sealed interface GlobalAttribute extends
            AnchorInstruction,
            DivInstruction,
            MetaInstruction,
            OptionInstruction,
            SelectInstruction permits ExternalAttribute, InternalInstruction {}

        sealed interface ExternalAttribute extends GlobalAttribute {
          non-sealed interface Id extends ExternalAttribute {
            String value();
          }

          non-sealed interface StyleClass extends ExternalAttribute {
            String value();
          }

          non-sealed interface StyleClassSet extends ExternalAttribute {
            Iterator<String> value();
          }
        }

        sealed interface ElementContents extends
            AnchorInstruction,
            DivInstruction,
            OptionInstruction,
            SelectInstruction permits HtmlTemplate, HtmlTemplate2, InternalInstruction {}

        sealed interface Fragment extends
            AnchorInstruction,
            DivInstruction,
            MetaInstruction,
            OptionInstruction,
            SelectInstruction permits InternalFragment {}

        sealed interface NoOp extends
            AnchorInstruction,
            DivInstruction,
            MetaInstruction,
            OptionInstruction,
            SelectInstruction permits InternalNoOp {}
      }
      """
    );
  }

}