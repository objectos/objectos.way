/*
 * Based on the Tabler Icons project.
 *
 * MIT License
 *
 * Copyright (c) 2020-2023 Paweł Kuna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This Java implementation:
 *
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.html.icon;

import objectos.html.Api;
import objectos.html.Api.Element;
import objectos.html.Api.SvgValue;
import objectos.html.HtmlComponent;
import objectos.html.TemplateBase;
import objectos.lang.object.Check;

/**
 * Provides SVG icons from the <a href="https://tabler-icons.io/">tabler
 * icons</a> project.
 */
public class TablerIcons extends HtmlComponent {

  private Api.ExternalAttribute.Id id;

  private String strokeWidth;

  /**
   * Creates a new instance of this class bound to the specified template.
   *
   * @param parent
   *        the template in which icons will be drawn
   */
  public TablerIcons(TemplateBase parent) {
    super(parent);

    reset();
  }

  /**
   * The value of {@code id} attribute of the rendered SVG element.
   *
   * @param id
   *        the id value
   *
   * @return this instance
   */
  public final TablerIcons id(Api.ExternalAttribute.Id id) {
    this.id = Check.notNull(id, "id == null");

    return this;
  }

  /**
   * The value of the {@code stroke-width} attribute of the rendered SVG
   * element.
   *
   * @param value
   *        the width value
   *
   * @return this instance
   */
  public final TablerIcons strokeWidth(double value) {
    strokeWidth = Double.toString(value);

    return this;
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/box">box</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final Element box(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5")),
        path(d("M12 12l8 -4.5")),
        path(d("M12 12l0 9")),
        path(d("M12 12l-8 -4.5"))        
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/briefcase">briefcase</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final Element briefcase(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M3 7m0 2a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v9a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z")),
        path(d("M8 7v-2a2 2 0 0 1 2 -2h4a2 2 0 0 1 2 2v2")),
        path(d("M12 12l0 .01")),
        path(d("M3 13a20 20 0 0 0 18 0"))
    );
  }

  /**
   * Renders the
   * <a href="https://tabler.io/icons/icon/calendar-repeat">calendar-repeat</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final Element calendarRepeat(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M12.5 21h-6.5a2 2 0 0 1 -2 -2v-12a2 2 0 0 1 2 -2h12a2 2 0 0 1 2 2v3")),
        path(d("M16 3v4")),
        path(d("M8 3v4")),
        path(d("M4 11h12")),
        path(d("M20 14l2 2h-3")),
        path(d("M20 18l2 -2")),
        path(d("M19 16a3 3 0 1 0 2 5.236"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/credit-card-pay">credit-card-pay</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final Element creditCardPay(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M12 19h-6a3 3 0 0 1 -3 -3v-8a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v4.5")),
        path(d("M3 10h18")),
        path(d("M16 19h6")),
        path(d("M19 16l3 3l-3 3")),
        path(d("M7.005 15h.005")),
        path(d("M11 15h2"))  
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/file-text">file-text</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  public final Element fileText(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M14 3v4a1 1 0 0 0 1 1h4")),
        path(d("M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z")),
        path(d("M9 9l1 0")),
        path(d("M9 13l6 0")),
        path(d("M9 17l6 0"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/home">home</a> icon.
   *
   * @return the {@code svg} element
   */
  public final Element home(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l-2 0l9 -9l9 9l-2 0")),
        path(d("M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7")),
        path(d("M9 21v-6a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v6"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/home-2">home-2</a> icon.
   *
   * @return the {@code svg} element
   */
  public final Element home2(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l-2 0l9 -9l9 9l-2 0")),
        path(d("M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7")),
        path(d("M10 12h4v4h-4z"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/menu">menu</a> icon.
   *
   * @return the {@code svg} element
   */
  public final Element menu() {
    return icon(
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M4 8l16 0")),
        path(d("M4 16l16 0"))
    );
  }

  /**
   * Renders the <a href="https://tabler-icons.io/i/menu-2">menu-2</a> icon.
   *
   * @return the {@code svg} element
   */
  public final Element menu2() {
    return icon(
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M4 6l16 0")),
        path(d("M4 12l16 0")),
        path(d("M4 18l16 0"))
    );
  }

  /**
   * Renders the <a href="https://tabler-icons.io/i/paw">paw</a> icon.
   *
   * @return the {@code svg} element
   */
  public final Element paw() {
    // @formatter:off
    return icon(
      path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
      path(d("M14.7 13.5c-1.1 -2 -1.441 -2.5 -2.7 -2.5c-1.259 0 -1.736 .755 -2.836 2.747c-.942 1.703 -2.846 1.845 -3.321 3.291c-.097 .265 -.145 .677 -.143 .962c0 1.176 .787 2 1.8 2c1.259 0 3 -1 4.5 -1s3.241 1 4.5 1c1.013 0 1.8 -.823 1.8 -2c0 -.285 -.049 -.697 -.146 -.962c-.475 -1.451 -2.512 -1.835 -3.454 -3.538z")),
      path(d("M20.188 8.082a1.039 1.039 0 0 0 -.406 -.082h-.015c-.735 .012 -1.56 .75 -1.993 1.866c-.519 1.335 -.28 2.7 .538 3.052c.129 .055 .267 .082 .406 .082c.739 0 1.575 -.742 2.011 -1.866c.516 -1.335 .273 -2.7 -.54 -3.052z")),
      path(d("M9.474 9c.055 0 .109 0 .163 -.011c.944 -.128 1.533 -1.346 1.32 -2.722c-.203 -1.297 -1.047 -2.267 -1.932 -2.267c-.055 0 -.109 0 -.163 .011c-.944 .128 -1.533 1.346 -1.32 2.722c.204 1.293 1.048 2.267 1.933 2.267z")),
      path(d("M16.456 6.733c.214 -1.376 -.375 -2.594 -1.32 -2.722a1.164 1.164 0 0 0 -.162 -.011c-.885 0 -1.728 .97 -1.93 2.267c-.214 1.376 .375 2.594 1.32 2.722c.054 .007 .108 .011 .162 .011c.885 0 1.73 -.974 1.93 -2.267z")),
      path(d("M5.69 12.918c.816 -.352 1.054 -1.719 .536 -3.052c-.436 -1.124 -1.271 -1.866 -2.009 -1.866c-.14 0 -.277 .027 -.407 .082c-.816 .352 -1.054 1.719 -.536 3.052c.436 1.124 1.271 1.866 2.009 1.866c.14 0 .277 -.027 .407 -.082z"))
    );
    // @formatter:on
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/x">x</a> icon.
   *
   * @return the {@code svg} element
   */
  public final Element x(SvgValue... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M18 6l-12 12")),
        path(d("M6 6l12 12"))
    );
  }

  private Element icon(SvgValue... contents) {
    Element svg;
    svg = svg(
        id != null ? id : noop(),

        xmlns("http://www.w3.org/2000/svg"),
        width("24"),
        height("24"),
        viewBox("0 0 24 24"),
        strokeWidth(strokeWidth),
        stroke("currentColor"),
        fill("none"),
        strokeLinecap("round"),
        strokeLinejoin("round"),

        flatten(contents)
    );

    reset();

    return svg;
  }

  private void reset() {
    id = null;

    strokeWidth = "2";
  }

}