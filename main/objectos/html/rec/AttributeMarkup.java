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

import objectos.html.rec.Instruction.AsMethod;
import objectos.html.rec.Instruction.OfAmbiguous;
import objectos.html.rec.Instruction.OfVoid;
import objectox.html.attr.AttributeOrNoOp;
import objectox.html.rec.AttributeInstruction;

/// Represents the markup of an Objectos HTML attribute.
public sealed interface AttributeMarkup
    extends AsMethod, OfVoid
    permits OfAmbiguous, AttributeOrNoOp, AttributeInstruction {}