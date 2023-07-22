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
package objectos.selfgen.css2.util;

import java.io.IOException;
import objectos.code.ClassTypeName;
import objectos.code.JavaSink;
import objectos.code.JavaTemplate;
import objectos.lang.Generated;
import objectos.selfgen.CssUtilSpec;

abstract class ThisTemplate extends JavaTemplate {

  static final String CSS = "objectos.css";

  static final String CSS_UTIL = "objectos.css.util";

  static final String GENERATOR = CssUtilSpec.class.getCanonicalName();

  static final ClassTypeName CLASS_SELECTOR = ClassTypeName.of(CSS_UTIL, "ClassSelector");

  static final ClassTypeName CSS_TEMPLATE = ClassTypeName.of(CSS, "CssTemplate");

  static final ClassTypeName GENERATED = ClassTypeName.of(Generated.class);

  static final ClassTypeName OVERRIDE = ClassTypeName.of(Override.class);

  CssUtilSelfGen spec;

  public final void write(JavaSink sink, CssUtilSelfGen spec) throws IOException {
    this.spec = spec;

    writeHook(sink);
  }

  void writeHook(JavaSink sink) throws IOException {
    sink.write(this);
  }

}