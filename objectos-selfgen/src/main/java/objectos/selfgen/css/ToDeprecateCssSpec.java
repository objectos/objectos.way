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

import objectos.selfgen.css.spec.Prefix;
import objectos.selfgen.css.spec.Source;

public abstract class ToDeprecateCssSpec {

  protected ToDeprecateCssSpec() {}

  protected abstract void definition();

  protected final void colors(String... colors) {}

  protected final void elementNamesAt(String resourceName) {}

  protected final void keywordType(String simpleName, String... names) {}

  protected final void lengthUnits(String... units) {}

  protected final void property(String name, String value, Source source, Prefix... prefixes) {}

  protected final void pseudoClasses(String... names) {}

  protected final void pseudoElements(String... names) {}

  protected final void valueType(String name, String value) {}

}
