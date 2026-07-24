/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html;

import objectos.html.rec.ElementInstruction;
import objectos.html.rec.FragmentInstruction;
import objectos.html.rec.Instruction;
import objectox.html.attr.AttributeOrNoOp;

public final class HtmlInstruction
    implements
    AttributeOrNoOp,
    Instruction.OfAmbiguous,
    ElementInstruction,
    FragmentInstruction {

  public static final AttributeOrNoOp ATTRIBUTE = new HtmlInstruction();

  public static final HtmlInstruction ELEMENT = new HtmlInstruction();

  public static final FragmentInstruction FRAGMENT = new HtmlInstruction();

  public static final AttributeOrNoOp NOOP = new HtmlInstruction();

  private HtmlInstruction() {}

}