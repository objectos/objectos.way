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
import objectos.way.Html;
import objectox.html.HtmlInstruction;
import objectox.html.attr.AttributeOrNoOp;

/// Represents an instruction that generates part of the output of an
/// HTML template.
public sealed interface Instruction {

  /**
   * Class of instructions that are represented by object instances.
   *
   * <p>
   * Instances of this interface can be safely reused in multiple templates.
   */
  sealed interface AsObject extends Instruction permits AttributeObject {}

  /**
   * Class of instructions that are represented by methods of the
   * {@link Html.Template} class.
   *
   * <p>
   * Instances of this interface MUST NOT be reused in a template.
   */
  sealed interface AsMethod
      extends Instruction
      permits
      ElementMarkup,
      OfAttribute,
      OfDataOn,
      OfFragment,
      NoOp {}

  /**
   * An instruction to generate an ambiguous element in a template.
   */
  sealed interface OfAmbiguous extends OfAttribute, ElementMarkup permits HtmlInstruction {}

  /**
   * An instruction to generate an HTML attribute in template.
   */
  sealed interface OfAttribute extends AsMethod, OfVoid permits OfAmbiguous, AttributeOrNoOp {}

  /**
   * An instruction to generate a {@code data-on-*} HTML attribute in a
   * template.
   */
  sealed interface OfDataOn extends AsMethod, OfVoid permits AttributeOrNoOp {}

  /**
   * An instruction to include an HTML fragment to a template.
   */
  sealed interface OfFragment extends AsMethod, OfVoid permits HtmlInstruction {}

  /**
   * Class of instructions that are allowed as arguments to template
   * methods that represent void elements.
   */
  sealed interface OfVoid extends Instruction
      permits
      AttributeObject,
      OfAttribute,
      OfDataOn,
      OfFragment,
      NoOp {}

  /// The no-op instruction.
  sealed interface NoOp extends AsMethod, OfVoid permits AttributeOrNoOp {}

  /// Returns the no-op instruction.
  ///
  /// @return the no-op instruction
  static NoOp noop() {
    return HtmlInstruction.NOOP;
  }

}