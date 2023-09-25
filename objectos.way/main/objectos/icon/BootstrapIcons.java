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
package objectos.icon;

import objectos.css.util.IdSelector;
import objectos.html.BaseTemplateDsl;
import objectos.html.HtmlComponent;
import objectos.html.tmpl.Api.ElementContents;
import objectos.html.tmpl.Api.SvgInstruction;
import objectos.lang.Check;

/**
 * Provides SVG icons from the
 * <a href="https://github.com/twbs/icons">Bootstrap</a> project.
 */
public class BootstrapIcons extends HtmlComponent {

  private IdSelector id;

  /**
   * Creates a new instance of this class bound to the specified template.
   *
   * @param parent
   *        the template in which icons will be drawn
   */
  public BootstrapIcons(BaseTemplateDsl parent) {
    super(parent);
  }

  /**
   * The value of {@code id} attribute of the rendered SVG element.
   *
   * @param id
   *        the id value
   *
   * @return this instance
   */
  public final BootstrapIcons id(IdSelector id) {
    this.id = Check.notNull(id, "id == null");

    return this;
  }

  /**
   * Renders the
   * <a href="https://icons.getbootstrap.com/icons/three-dots/">three-dots</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final ElementContents threeDots() {
    // @formatter:off
    return icon(
      path(d("M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z"))
    );
    // @formatter:on
  }

  /**
   * Renders the <a href="https://icons.getbootstrap.com/icons/x/">x</a> icon.
   *
   * @return the {@code svg} element
   */
  public final ElementContents x() {
    // @formatter:off
    return icon(
      path(d("M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"))
    );
    // @formatter:on
  }

  private ElementContents icon(SvgInstruction... contents) {
    ElementContents svg;
    svg = svg(
      id != null ? id : noop(),

      xmlns("http://www.w3.org/2000/svg"),
      width("16"),
      height("16"),
      viewBox("0 0 16 16"),
      fill("currentColor"),

      flatten(contents)
    );

    reset();

    return svg;
  }

  private void reset() {
    id = null;
  }

}