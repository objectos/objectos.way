/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import objectos.css.internal.NamedElement;
import objectos.css.om.Selector;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
abstract class GeneratedCssTemplate {
  protected static final Selector __after = named("::after");

  protected static final Selector __before = named("::before");

  protected static final Selector a = named("a");

  protected static final Selector b = named("b");

  protected static final Selector body = named("body");

  protected static final Selector code = named("code");

  protected static final Selector h1 = named("h1");

  protected static final Selector h2 = named("h2");

  protected static final Selector h3 = named("h3");

  protected static final Selector h4 = named("h4");

  protected static final Selector h5 = named("h5");

  protected static final Selector h6 = named("h6");

  protected static final Selector hr = named("hr");

  protected static final Selector html = named("html");

  protected static final Selector kbd = named("kbd");

  protected static final Selector li = named("li");

  protected static final Selector pre = named("pre");

  protected static final Selector samp = named("samp");

  protected static final Selector small = named("small");

  protected static final Selector strong = named("strong");

  protected static final Selector sub = named("sub");

  protected static final Selector sup = named("sup");

  protected static final Selector ul = named("ul");

  protected static final Selector any = named("*");

  private static NamedElement named(String name) {
    return new NamedElement(name);
  }
}
