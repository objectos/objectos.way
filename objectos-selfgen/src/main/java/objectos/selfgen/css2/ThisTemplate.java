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
import objectos.lang.Generated;
import objectos.selfgen.CssSpec;

abstract class ThisTemplate extends JavaTemplate {

  static final String CSS = "objectos.css";

  static final String CSS_INTERNAL = "objectos.css.internal";

  static final String CSS_OM = "objectos.css.om";

  static final String CSS_TMPL = "objectos.css.tmpl";

  static final String GENERATOR = CssSpec.class.getCanonicalName();

  static final ClassTypeName GENERATED = ClassTypeName.of(Generated.class);

  static final ClassTypeName INTERNAL_LENGTH = ClassTypeName.of(CSS_INTERNAL, "InternalLength");

  static final ClassTypeName INTERNAL_PERCENTAGE
      = ClassTypeName.of(CSS_INTERNAL, "InternalPercentage");

  static final ClassTypeName INTERNAL_STRING_LITERAL
      = ClassTypeName.of(CSS_INTERNAL, "InternalStringLiteral");

  static final ClassTypeName INTERNAL_URL = ClassTypeName.of(CSS_INTERNAL, "InternalUrl");

  static final ClassTypeName INTERNAL_ZERO = ClassTypeName.of(CSS_INTERNAL, "InternalZero");

  static final ClassTypeName LENGTH = ClassTypeName.of(CSS_TMPL, "Length");

  static final ClassTypeName NAMED_ELEMENT = ClassTypeName.of(CSS_INTERNAL, "NamedElement");

  static final ClassTypeName OVERRIDE = ClassTypeName.of(Override.class);

  static final ClassTypeName PERCENTAGE = ClassTypeName.of(CSS_TMPL, "Percentage");

  static final ClassTypeName PROPERTY = ClassTypeName.of(CSS_INTERNAL, "Property");

  static final ClassTypeName PROPERTY_NAME = ClassTypeName.of(CSS_OM, "PropertyName");

  static final ClassTypeName PROPERTY_VALUE = ClassTypeName.of(CSS_OM, "PropertyValue");

  static final ClassTypeName SELECTOR = ClassTypeName.of(CSS_OM, "Selector");

  static final ClassTypeName STRING = ClassTypeName.of(String.class);

  static final ClassTypeName STRING_LITERAL = ClassTypeName.of(CSS_TMPL, "StringLiteral");

  static final ClassTypeName STYLE_DECLARATION = ClassTypeName.of(CSS_OM, "StyleDeclaration");

  static final ClassTypeName STYLE_DECLARATION1
      = ClassTypeName.of(CSS_INTERNAL, "StyleDeclaration1");

  static final ClassTypeName STYLE_DECLARATION2
      = ClassTypeName.of(CSS_INTERNAL, "StyleDeclaration2");

  static final ClassTypeName STYLE_DECLARATION3
      = ClassTypeName.of(CSS_INTERNAL, "StyleDeclaration3");

  static final ClassTypeName STYLE_DECLARATION4
      = ClassTypeName.of(CSS_INTERNAL, "StyleDeclaration4");

  static final ClassTypeName STYLE_DECLARATION_DOUBLE
      = ClassTypeName.of(CSS_INTERNAL, "StyleDeclarationDouble");

  static final ClassTypeName STYLE_DECLARATION_INT
      = ClassTypeName.of(CSS_INTERNAL, "StyleDeclarationInt");

  static final ClassTypeName URL = ClassTypeName.of(CSS_TMPL, "Url");

  static final ClassTypeName ZERO = ClassTypeName.of(CSS_TMPL, "Zero");

  CompiledSpec spec;

  public final void write(JavaSink sink, CompiledSpec spec) throws IOException {
    this.spec = spec;

    writeHook(sink);
  }

  void writeHook(JavaSink sink) throws IOException {
    sink.write(this);
  }

}