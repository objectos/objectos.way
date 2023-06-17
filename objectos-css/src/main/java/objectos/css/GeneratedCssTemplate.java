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
import objectos.css.internal.Property;
import objectos.css.internal.StyleDeclaration1;
import objectos.css.internal.StyleDeclaration2;
import objectos.css.internal.StyleDeclaration3;
import objectos.css.internal.StyleDeclaration4;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.tmpl.Color;
import objectos.css.tmpl.GlobalKeyword;
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

  protected static final Color aqua = named("aqua");

  protected static final Color black = named("black");

  protected static final Color blue = named("blue");

  protected static final Color currentcolor = named("currentcolor");

  protected static final Color fuchsia = named("fuchsia");

  protected static final Color gray = named("gray");

  protected static final Color green = named("green");

  protected static final GlobalKeyword inherit = named("inherit");

  protected static final GlobalKeyword initial = named("initial");

  protected static final Color lime = named("lime");

  protected static final Color maroon = named("maroon");

  protected static final Color navy = named("navy");

  protected static final Color olive = named("olive");

  protected static final Color purple = named("purple");

  protected static final Color red = named("red");

  protected static final Color silver = named("silver");

  protected static final Color teal = named("teal");

  protected static final Color transparent = named("transparent");

  protected static final GlobalKeyword unset = named("unset");

  protected static final Color white = named("white");

  protected static final Color yellow = named("yellow");

  private static NamedElement named(String name) {
    return new NamedElement(name);
  }

  protected final StyleDeclaration borderColor(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_COLOR, value.self());
  }

  protected final StyleDeclaration borderColor(Color all) {
    return new StyleDeclaration1(Property.BORDER_COLOR, all.self());
  }

  protected final StyleDeclaration borderColor(Color vertical, Color horizontal) {
    return new StyleDeclaration2(Property.BORDER_COLOR, vertical.self(), horizontal.self());
  }

  protected final StyleDeclaration borderColor(Color top, Color horizontal, Color bottom) {
    return new StyleDeclaration3(Property.BORDER_COLOR, top.self(), horizontal.self(), bottom.self());
  }

  protected final StyleDeclaration borderColor(Color top, Color right, Color bottom, Color left) {
    return new StyleDeclaration4(Property.BORDER_COLOR, top.self(), right.self(), bottom.self(), left.self());
  }
}
