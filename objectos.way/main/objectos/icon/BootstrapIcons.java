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

import objectos.html.BaseTemplateDsl;
import objectos.html.HtmlComponent;
import objectos.html.tmpl.Api.ElementContents;
import objectos.html.tmpl.Api.SvgInstruction;

/**
 * Provides SVG icons from the
 * <a href="https://github.com/twbs/icons">Bootstrap</a> project.
 */
public class BootstrapIcons extends HtmlComponent {

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
   * Renders the
   * <a href="https://icons.getbootstrap.com/icons/github/">GitHub</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final ElementContents github(SvgInstruction... contents) {
    // @formatter:off
    return icon(
      flatten(contents),
      path(d("M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.012 8.012 0 0 0 16 8c0-4.42-3.58-8-8-8z"))
    );
    // @formatter:on
  }

  /**
   * Renders the
   * <a href="https://icons.getbootstrap.com/icons/three-dots/">three-dots</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final ElementContents threeDots(SvgInstruction... contents) {
    // @formatter:off
    return icon(
      flatten(contents),
      path(d("M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z"))
    );
    // @formatter:on
  }

  /**
   * Renders the <a href="https://icons.getbootstrap.com/icons/x/">x</a> icon.
   *
   * @return the {@code svg} element
   */
  public final ElementContents x(SvgInstruction... contents) {
    // @formatter:off
    return icon(
      flatten(contents),
      path(d("M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"))
    );
    // @formatter:on
  }

  private ElementContents icon(SvgInstruction... contents) {
    return svg(
      xmlns("http://www.w3.org/2000/svg"),
      width("16"),
      height("16"),
      viewBox("0 0 16 16"),
      fill("currentColor"),

      flatten(contents)
    );
  }

}