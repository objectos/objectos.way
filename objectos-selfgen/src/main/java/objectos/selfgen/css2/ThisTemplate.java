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
package objectos.selfgen.css2;

import java.io.IOException;
import objectos.code.ClassTypeName;
import objectos.code.JavaSink;
import objectos.code.JavaTemplate;
import objectos.lang.Check;
import objectos.lang.Generated;
import objectos.selfgen.CssSpec;

abstract class ThisTemplate extends JavaTemplate {

  static final String CSS = "objectos.css";

  static final String CSS_INTERNAL = "objectos.css.internal";

  static final String CSS_OM = "objectos.css.om";

  static final String CSS_TMPL = "objectos.css.tmpl";

  static final String CSS_UTIL = "objectos.css.util";

  static final String GENERATOR = CssSpec.class.getCanonicalName();

  static final ClassTypeName CHECK = ClassTypeName.of(Check.class);

  static final ClassTypeName OVERRIDE = ClassTypeName.of(Override.class);

  static final ClassTypeName STRING = ClassTypeName.of(String.class);

  //

  static final ClassTypeName API = ClassTypeName.of(CSS_TMPL, "Api");

  static final ClassTypeName COLOR_VALUE = ClassTypeName.of(API, "ColorValue");

  static final ClassTypeName FILTER_FUNCTION = ClassTypeName.of(API, "FilterFunction");

  static final ClassTypeName KEYWORD_INSTRUCTION = ClassTypeName.of(API, "KeywordInstruction");

  static final ClassTypeName LENGTH_VALUE = ClassTypeName.of(API, "LengthValue");

  static final ClassTypeName MEDIA_FEATURE = ClassTypeName.of(API, "MediaFeature");

  static final ClassTypeName MEDIA_RULE_ELEM = ClassTypeName.of(API, "MediaRuleElement");

  static final ClassTypeName PERCENTAGE_VALUE = ClassTypeName.of(API, "PercentageValue");

  static final ClassTypeName PROPERTY_VALUE = ClassTypeName.of(API, "PropertyValue");

  static final ClassTypeName SELECTOR = ClassTypeName.of(API, "Selector");

  static final ClassTypeName SELECTOR_INSTRUCTION = ClassTypeName.of(API, "SelectorInstruction");

  static final ClassTypeName STRING_LITERAL = ClassTypeName.of(API, "StringLiteral");

  static final ClassTypeName STYLE_DECLARATION = ClassTypeName.of(API, "StyleDeclaration");

  static final ClassTypeName STYLE_DECL_INST = ClassTypeName.of(API, "StyleDeclarationInstruction");

  static final ClassTypeName STYLE_RULE = ClassTypeName.of(API, "StyleRule");

  static final ClassTypeName STYLE_RULE_ELEM = ClassTypeName.of(API, "StyleRuleElement");

  static final ClassTypeName URL = ClassTypeName.of(API, "Url");

  static final ClassTypeName VALUE_INSTRUCTION = ClassTypeName.of(API, "ValueInstruction");

  static final ClassTypeName ZERO = ClassTypeName.of(API, "Zero");

  //

  static final ClassTypeName COLOR = ClassTypeName.of(CSS_UTIL, "Color");

  static final ClassTypeName GENERATED = ClassTypeName.of(Generated.class);

  static final ClassTypeName INTERNAL_INSTRUCTION
      = ClassTypeName.of(CSS_INTERNAL, "InternalInstruction");

  static final ClassTypeName INTERNAL_ZERO
      = ClassTypeName.of(CSS_INTERNAL, "InternalZero");

  static final ClassTypeName LENGTH = ClassTypeName.of(CSS_UTIL, "Length");

  static final ClassTypeName LENGTH_UNIT = ClassTypeName.of(CSS_INTERNAL, "LengthUnit");

  static final ClassTypeName PERCENTAGE = ClassTypeName.of(CSS_UTIL, "Percentage");

  static final ClassTypeName PROPERTY = ClassTypeName.of(CSS_INTERNAL, "Property");

  static final ClassTypeName PROPERTY_NAME = ClassTypeName.of(CSS_OM, "PropertyName");

  static final ClassTypeName STANDARD_NAME = ClassTypeName.of(CSS_INTERNAL, "StandardName");

  static final ClassTypeName STANDARD_PSEUDO_CLASS_SELECTOR
      = ClassTypeName.of(CSS_INTERNAL, "StandardPseudoClassSelector");

  static final ClassTypeName STANDARD_PSEUDO_ELEMENT_SELECTOR
      = ClassTypeName.of(CSS_INTERNAL, "StandardPseudoElementSelector");

  static final ClassTypeName STANDARD_TYPE_SELECTOR
      = ClassTypeName.of(CSS_INTERNAL, "StandardTypeSelector");

  CompiledSpec spec;

  public final void write(JavaSink sink, CompiledSpec spec) throws IOException {
    this.spec = spec;

    writeHook(sink);
  }

  void writeHook(JavaSink sink) throws IOException {
    sink.write(this);
  }

}