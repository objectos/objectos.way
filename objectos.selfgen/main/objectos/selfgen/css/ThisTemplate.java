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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import objectos.code.ClassName;
import objectos.code.Code;
import objectos.code.Code.ImportList;
import objectos.lang.Check;

abstract class ThisTemplate {

  static final String GENERATED_MSG = "// Generated by selfgen.css.CssSpec. Do not edit!";

  static final String CSS = "objectos.css";

  static final String CSS_INTERNAL = "objectos.css.internal";

  static final String CSS_OM = "objectos.css.om";

  static final String CSS_TMPL = "objectos.css.tmpl";

  static final String CSS_UTIL = "objectos.css.util";

  static final ClassName CHECK = ClassName.of("objectox.lang", "Check");

  static final ClassName OVERRIDE = ClassName.of(Override.class);

  static final ClassName STRING = ClassName.of(String.class);

  //

  static final ClassName API = ClassName.of(CSS_TMPL, "Api");

  static final ClassName COLOR_VALUE = ClassName.of(API, "ColorValue");

  static final ClassName DOUBLE_LITERAL = ClassName.of(API, "DoubleLiteral");

  static final ClassName FILTER_FUNCTION = ClassName.of(API, "FilterFunction");

  static final ClassName FLEX_VALUE = ClassName.of(API, "FlexValue");

  static final ClassName FUNCTION = ClassName.of(CSS_INTERNAL, "Function");

  static final ClassName FUNCTION_INSTRUCTION = ClassName.of(API, "FunctionInstruction");

  static final ClassName KEYWORD_INSTRUCTION = ClassName.of(API, "KeywordInstruction");

  static final ClassName INT_LITERAL = ClassName.of(API, "IntLiteral");

  static final ClassName LENGTH_VALUE = ClassName.of(API, "LengthValue");

  static final ClassName MEDIA_FEATURE = ClassName.of(API, "MediaFeature");

  static final ClassName MEDIA_RULE_ELEM = ClassName.of(API, "MediaRuleElement");

  static final ClassName PERCENTAGE_VALUE = ClassName.of(API, "PercentageValue");

  static final ClassName PROPERTY_VALUE = ClassName.of(API, "PropertyValue");

  static final ClassName SELECTOR = ClassName.of(API, "Selector");

  static final ClassName SELECTOR_INSTRUCTION = ClassName.of(API, "SelectorInstruction");

  static final ClassName STRING_LITERAL = ClassName.of(API, "StringLiteral");

  static final ClassName STYLE_DECLARATION = ClassName.of(API, "StyleDeclaration");

  static final ClassName STYLE_DECL_INST = ClassName.of(API, "StyleDeclarationInstruction");

  static final ClassName STYLE_RULE = ClassName.of(API, "StyleRule");

  static final ClassName STYLE_RULE_ELEM = ClassName.of(API, "StyleRuleElement");

  static final ClassName URL = ClassName.of(API, "Url");

  static final ClassName VALUE_INSTRUCTION = ClassName.of(API, "ValueInstruction");

  static final ClassName ZERO = ClassName.of(API, "Zero");

  //

  static final ClassName COLOR = ClassName.of(CSS_UTIL, "Color");

  static final ClassName INTERNAL_INSTRUCTION
      = ClassName.of(CSS_INTERNAL, "InternalInstruction");

  static final ClassName INTERNAL_ZERO
      = ClassName.of(CSS_INTERNAL, "InternalZero");

  static final ClassName LENGTH = ClassName.of(CSS_UTIL, "Length");

  static final ClassName LENGTH_UNIT = ClassName.of(CSS_INTERNAL, "LengthUnit");

  static final ClassName PERCENTAGE = ClassName.of(CSS_UTIL, "Percentage");

  static final ClassName PROPERTY = ClassName.of(CSS_INTERNAL, "Property");

  static final ClassName PROPERTY_NAME = ClassName.of(CSS_OM, "PropertyName");

  static final ClassName STANDARD_NAME = ClassName.of(CSS_INTERNAL, "StandardName");

  static final ClassName STANDARD_PSEUDO_CLASS_SELECTOR
      = ClassName.of(CSS_INTERNAL, "StandardPseudoClassSelector");

  static final ClassName STANDARD_PSEUDO_ELEMENT_SELECTOR
      = ClassName.of(CSS_INTERNAL, "StandardPseudoElementSelector");

  static final ClassName STANDARD_TYPE_SELECTOR
      = ClassName.of(CSS_INTERNAL, "StandardTypeSelector");

  final CssSelfGen spec;

  final Code code;

  ClassName className;

  ImportList importList;

  String packageName;

  String simpleName;

  public ThisTemplate(CssSelfGen spec) {
    this.spec = spec;

    code = spec.code;
  }

  @Override
  public final String toString() {
    return contents();
  }

  public void writeTo(Path directory) throws IOException {
    String contents;
    contents = contents();

    Path file;
    file = className.toPath(directory);

    Path parent;
    parent = file.getParent();

    Files.createDirectories(parent);

    Files.writeString(
      file, contents, StandardCharsets.UTF_8,
      StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  final void className(ClassName className) {
    this.className = Check.notNull(className, "className == null");

    packageName = this.className.packageName();

    simpleName = this.className.simpleName();

    importList = code.importList(packageName);
  }

  abstract String contents();

}