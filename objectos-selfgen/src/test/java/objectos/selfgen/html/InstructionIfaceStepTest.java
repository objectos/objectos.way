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

        element("meta");
      }
    }.prepare();

    assertEquals(
      template.toString(),

      """
      package objectos.html.tmpl;

      import objectos.html.internal.InternalInstruction;

      public sealed interface Instruction {
        sealed interface AnchorInstruction extends Instruction {}

        sealed interface DivInstruction extends Instruction {}

        sealed interface MetaInstruction extends Instruction {}

        sealed interface GlobalAttribute extends AnchorInstruction, DivInstruction, MetaInstruction permits InternalInstruction {}
      }
      """
    );
  }

}