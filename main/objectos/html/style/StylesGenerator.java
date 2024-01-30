/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.html.style;

import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectox.html.style.StylesGeneratorImpl;

public sealed interface StylesGenerator permits StylesGeneratorImpl {

  static StylesGenerator of(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new StylesGeneratorImpl(noteSink);
  }

  void scan(Class<?> clazz);

  String generate();

}