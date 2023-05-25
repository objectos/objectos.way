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
package objectos.selfgen.css;

import objectos.selfgen.css.spec.KeywordName;
import objectos.selfgen.css.spec.Source;
import objectos.selfgen.css.spec.ValueType;

class DisplayPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    ValueType displayOutside = t(
        "DisplayOutsideValue",
        k("block"),
        k("inline"),
        k("runIn")
    );

    ValueType displayInside = t(
        "DisplayInsideValue",
        k("flow"),
        k("flow-root"),
        k("table"),
        k("flex"),
        k("grid"),
        k("ruby")
    );

    ValueType displayListItem = t(
        "DisplayListItemValue",
        k("list-item")
    );

    ValueType displayInternal = t(
        "DisplayInternalValue",
        k("table-row-group"),
        k("table-header-group"),
        k("table-footer-group"),
        k("table-row"),
        k("table-cell"),
        k("table-column-group"),
        k("table-column"),
        k("table-caption"),
        k("ruby-base"),
        k("ruby-text"),
        k("ruby-base-container"),
        k("ruby-text-container")
    );

    ValueType displayBox = t(
        "DisplayBoxValue",
        k("contents"),
        k("none")
    );

    ValueType displayLegacy = t(
        "DisplayLegacyValue",
        k("inline-block"),
        k("inline-table"),
        k("inline-flex"),
        k("inline-grid")
    );

    ValueType arity1 = t(
        "DisplayArity1Value",
        displayOutside,
        displayInside,
        displayListItem,
        displayInternal,
        displayBox,
        displayLegacy
    );

    ValueType arity2 = t(
        "DisplayArity2Value",
        displayOutside,
        displayInside
    );

    property(
        "display",

        formal(
            Source.MDN,
            "[ <display-outside> || <display-inside> ] | <display-listitem> | <display-internal> | <display-box> | <display-legacy>"
        ),

        globalSig,

        sig(arity1, "value"),
        sig(arity2, "value1", arity2, "value2")
    );
  }

  private KeywordName k(String name) {
    return keyword(name);
  }

}
