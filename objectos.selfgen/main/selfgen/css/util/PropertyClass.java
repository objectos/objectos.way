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
package selfgen.css.util;

import objectos.code.ClassName;
import objectos.code.Code;
import objectos.lang.Check;

public sealed abstract class PropertyClass permits StandardProperty {

  static final ClassName STYLE_CLASS = ThisTemplate.STYLE_CLASS;

  static final ClassName CLASS_SEQ_ID = ThisTemplate.CLASS_SEQ_ID;

  final String simpleName;

  PropertyClass(String simpleName) {
    this.simpleName = Check.notNull(simpleName, "simpleName == null");
  }

  abstract String generate(Code code);

  abstract String javadoc(Code code);

}