/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.css;

import objectos.css.tmpl.Api;

public enum InternalInstruction
    implements
    Api.ColorValue,
    Api.DoubleLiteral,
    Api.FlexValue,
    Api.FunctionInstruction,
    Api.IntLiteral,
    Api.LengthValue,
    Api.MediaFeatureOrStyleDeclaration,
    Api.PercentageValue,
    Api.SelectorInstruction,
    Api.StringLiteral,
    Api.StyleDeclarationInstruction,
    Api.StyleRule,
    Api.Url {

  INSTANCE;

}