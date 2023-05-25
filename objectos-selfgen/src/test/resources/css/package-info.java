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
@br.com.objectos.css.StyleSheetScaffold(
    simpleName = "GeneratedStyleSheet",
    providers = {
        br.com.objectos.css.select.Selectors.class
    },
    fields = {
        br.com.objectos.css.select.TypeSelectorAlt.class
    },
    methods = {
        br.com.objectos.css.select.PseudoElementSelectorAlt.class
    })
@StyleSheetSpec(
    lengthSet = {},
    productionSet = {
        @Production(name = "margin", value = "<margin-width>{1,4} | inherit", source = Source.W3C),
        @Production(name = "position", value = "static | relative | absolute | fixed | inherit", source = Source.W3C),
    },
    valueTypeSet = {}
)
package br.com.objectos.css;

import br.com.objectos.css.common.selfgen.annotations.Production;
import br.com.objectos.css.common.selfgen.annotations.Source;
import br.com.objectos.css.common.selfgen.annotations.StyleSheetSpec;