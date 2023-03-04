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
package objectos.selfgen.html;

import java.io.IOException;
import objectos.code.ClassTypeName;
import objectos.code.JavaSink;
import objectos.code.JavaTemplate;
import objectos.util.UnmodifiableMap;

abstract class ThisTemplate extends JavaTemplate {

  static final ClassTypeName OVERRIDE = ClassTypeName.of(Override.class);

  static final ClassTypeName STRING = ClassTypeName.of(String.class);

  static final ClassTypeName UNMODIFIABLE_MAP = ClassTypeName.of(UnmodifiableMap.class);

  static final String HTML_TMPL = "objectos.html.tmpl";

  static final ClassTypeName ATTRIBUTE_KIND = ClassTypeName.of(HTML_TMPL, "AttributeKind");

  static final ClassTypeName ATTRIBUTE_NAME = ClassTypeName.of(HTML_TMPL, "AttributeName");

  static final ClassTypeName NAMES_BUILDER = ClassTypeName.of(HTML_TMPL, "NamesBuilder");

  static final ClassTypeName GLB_ATTR_NAME = ClassTypeName.of(HTML_TMPL, "GlobalAttributeName");

  static final ClassTypeName STD_ATTR_NAME = ClassTypeName.of(HTML_TMPL, "StandardAttributeName");

  static final String elem = "br.com.objectos.html.element";

  static final ClassTypeName ELEMENT_KIND = ClassTypeName.of(elem, "ElementKind");

  static final ClassTypeName ELEMENT_NAME = ClassTypeName.of(elem, "ElementName");

  static final ClassTypeName STD_ELEMENT_NAME = ClassTypeName.of(elem, "StandardElementName");

  private static final String spi_tmpl = "objectos.html.spi";

  static final ClassTypeName MARKER = ClassTypeName.of(spi_tmpl, "Marker");

  static final ClassTypeName RENDERER = ClassTypeName.of(spi_tmpl, "Renderer");

  static final ClassTypeName ANY_ELEMENT_VALUE = ClassTypeName.of(HTML_TMPL, "AnyElementValue");

  static final ClassTypeName NON_VOID = ClassTypeName.of(HTML_TMPL, "NonVoidElementValue");

  static final ClassTypeName VALUE = ClassTypeName.of(HTML_TMPL, "Value");

  HtmlSelfGen spec;

  public void write(JavaSink sink, HtmlSelfGen spec) throws IOException {
    this.spec = spec;

    sink.write(this);
  }

}