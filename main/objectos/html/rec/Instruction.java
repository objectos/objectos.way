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
package objectos.html.rec;

import objectos.html.AttributeObject;
import objectos.html.Template;
import objectos.html.rec.Instruction.AsMethod;
import objectos.html.rec.Instruction.AsObject;
import objectos.html.rec.Instruction.OfVoid;

/// Represents an instruction to render some markup of an Objectos
/// HTML document.
public sealed interface Instruction
    permits
    AsObject,
    AsMethod,
    OfVoid,
    FlattenInstruction,
    RawInstruction,
    TextInstruction {

  /**
   * Class of instructions that are represented by object instances.
   *
   * <p>
   * Instances of this interface can be safely reused in multiple templates.
   */
  sealed interface AsObject extends Instruction permits AttributeObject {}

  /**
   * Class of instructions that are represented by methods of the
   * {@link Template} class.
   *
   * <p>
   * Instances of this interface MUST NOT be reused in a template.
   */
  sealed interface AsMethod
      extends Instruction
      permits
      ElementInstruction,
      AttributeInstruction,
      FragmentInstruction,
      NoOpInstruction {}

  /**
   * Class of instructions that are allowed as arguments to template
   * methods that represent void elements.
   */
  sealed interface OfVoid extends Instruction
      permits
      AttributeObject,
      AttributeInstruction,
      FragmentInstruction,
      NoOpInstruction {}

  /// Returns the encoded value of this markup.
  ///
  /// @return the encoded value of this markup
  default int value() {
    return -1;
  }

}