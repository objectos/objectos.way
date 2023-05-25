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
package objectos.specgen.css;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import objectos.code.JavaTemplate;

public abstract class Specgen extends PropertyModuleTemplate {

  private Spec spec;

  protected final void generate(Spec spec) throws IOException {
    this.spec = spec;

    try {
      generate();
    } finally {
      this.spec = null;
    }
  }

  protected abstract void templateSink(JavaTemplate template) throws IOException;

  private void generate() throws IOException {
    // A
    p("align-content");
    p("align-items");
    p("align-self");
    p("appearance");

    // B
    p("background");
    p("background-attachment");
    p("background-clip");
    p("background-color");
    p("background-image");
    p("background-origin");
    p("background-position");
    p("background-repeat");
    p("background-size");
    p("border",
      "border-top", "border-right", "border-bottom", "border-left");
    p("border-collapse");
    p("border-color",
      "border-top-color", "border-right-color", "border-bottom-color", "border-left-color");
    p("border-radius");
    p("border-style",
      "border-top-style", "border-right-style", "border-bottom-style", "border-left-style");
    p("border-width",
      "border-top-width", "border-right-width", "border-bottom-width", "border-left-width");
    p("bottom");
    p("box-shadow");
    p("box-sizing");

    // C
    p("clear");
    p("color");
    p("content");
    p("cursor");

    // F
    p("flex");
    p("flex-basis");
    p("flex-direction");
    p("flex-flow");
    p("flex-grow");
    p("flex-shrink");
    p("flex-wrap");
    p("float");
    p("font");
    p("font-family");
    p("font-size");
    p("font-style");
    p("font-weight");

    // H
    p("height",
      "max-height", "max-width", "min-height", "min-width", "width");

    // J
    p("justify-content");
    p("justify-items");
    p("justify-self");

    // L
    p("left");
    p("letter-spacing");
    p("line-height");
    p("list-style");
    p("list-style-image");
    p("list-style-position");
    p("list-style-type");

    // M
    p("margin",
      "margin-top", "margin-right", "margin-bottom", "margin-left");

    // O
    p("object-fit");
    p("opacity");
    p("outline");
    p("outline-color");
    p("outline-offset");
    p("outline-style");
    p("outline-width");
    p("overflow",
      "overflow-block", "overflow-inline", "overflow-x", "overflow-y");

    // P
    p("padding",
      "padding-top", "padding-right", "padding-bottom", "padding-left");
    p("position");

    // R
    p("resize");
    p("right");

    // T
    p("tab-size");
    p("text-align");
    p("text-decoration");
    p("text-decoration-color");
    p("text-decoration-line");
    p("text-decoration-style");
    p("text-decoration-thickness");
    p("text-indent");
    p("text-shadow");
    p("text-size-adjust");
    p("text-transform");
    p("top");
    p("transform");

    // V
    p("vertical-align");

    // W
    p("white-space");

    // Z
    p("z-index");
  }

  private void p(String name) throws IOException {
    var p = spec.getProperty(name);

    set(p, List.of());

    templateSink(this);
  }

  private void p(String name, String... more) throws IOException {
    var p = spec.getProperty(name);

    var group = Stream.of(more).map(spec::getProperty).toList();

    set(p, group);

    templateSink(this);
  }

}
