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
package objectos.way;

/**
 * Provides SVG icons from the <a href="https://tabler-icons.io/">tabler
 * icons</a> project.
 */
final class IconsTabler extends Html.Component implements Icons.Tabler {

  private Html.Id id;

  private String strokeWidth;

  /**
   * Creates a new instance of this class bound to the specified template.
   *
   * @param parent
   *        the template in which icons will be drawn
   */
  public IconsTabler(Html.TemplateBase parent) {
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
  @Override
  public final IconsTabler id(Html.Id id) {
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
  @Override
  public final IconsTabler strokeWidth(double value) {
    strokeWidth = Double.toString(value);

    return this;
  }

  /**
   * Renders the
   * <a href="https://tabler.io/icons/icon/arrow-left">arrow-left</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction arrowLeft(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l14 0")),
        path(d("M5 12l6 6")),
        path(d("M5 12l6 -6"))
    );
  }

  /**
   * Renders the <a href=
   * "https://tabler.io/icons/icon/arrow-narrow-left">arrow-narrow-left</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction arrowNarrowLeft(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l14 0")),
        path(d("M5 12l4 4")),
        path(d("M5 12l4 -4"))
    );
  }

  /**
   * Renders the <a href=
   * "https://tabler.io/icons/icon/arrow-narrow-right">arrow-narrow-right</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction arrowNarrowRight(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l14 0")),
        path(d("M15 16l4 -4")),
        path(d("M15 8l4 4"))
    );
  }

  /**
   * Renders the
   * <a href="https://tabler.io/icons/icon/arrow-right">arrow-right</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction arrowRight(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l14 0")),
        path(d("M13 18l6 -6")),
        path(d("M13 6l6 6"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/box">box</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction box(Html.Instruction... contents) {
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
  @Override
  public final Html.ElementInstruction briefcase(Html.Instruction... contents) {
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
  @Override
  public final Html.ElementInstruction calendarRepeat(Html.Instruction... contents) {
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
   * Renders the <a href="https://tabler.io/icons/icon/caret-left">caret-left
   * (filled)</a> icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction caretLeftFilled(Html.Instruction... contents) {
    // @formatter:off
    return filled(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M13.883 5.007l.058 -.005h.118l.058 .005l.06 .009l.052 .01l.108 .032l.067 .027l.132 .07l.09 .065l.081 .073l.083 .094l.054 .077l.054 .096l.017 .036l.027 .067l.032 .108l.01 .053l.01 .06l.004 .057l.002 .059v12c0 .852 -.986 1.297 -1.623 .783l-.084 -.076l-6 -6a1 1 0 0 1 -.083 -1.32l.083 -.094l6 -6l.094 -.083l.077 -.054l.096 -.054l.036 -.017l.067 -.027l.108 -.032l.053 -.01l.06 -.01z"))
    );
    // @formatter:on
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/caret-right">caret-right
   * (filled)</a> icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction caretRightFilled(Html.Instruction... contents) {
    // @formatter:off
    return filled(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M9 6c0 -.852 .986 -1.297 1.623 -.783l.084 .076l6 6a1 1 0 0 1 .083 1.32l-.083 .094l-6 6l-.094 .083l-.077 .054l-.096 .054l-.036 .017l-.067 .027l-.108 .032l-.053 .01l-.06 .01l-.057 .004l-.059 .002l-.059 -.002l-.058 -.005l-.06 -.009l-.052 -.01l-.108 -.032l-.067 -.027l-.132 -.07l-.09 -.065l-.081 -.073l-.083 -.094l-.054 -.077l-.054 -.096l-.017 -.036l-.027 -.067l-.032 -.108l-.01 -.053l-.01 -.06l-.004 -.057l-.002 -12.059z"))
    );
    // @formatter:on
  }

  /**
   * Renders the
   * <a href="https://tabler.io/icons/icon/chevron-left">chevron-left</a> icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction chevronLeft(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M15 6l-6 6l6 6"))
    );
  }

  /**
   * Renders the
   * <a href="https://tabler.io/icons/icon/chevron-right">chevron-right</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction chevronRight(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M9 6l6 6l-6 6"))
    );
  }

  /**
   * Renders the
   * <a href="https://tabler.io/icons/icon/credit-card-pay">credit-card-pay</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction creditCardPay(Html.Instruction... contents) {
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
   * Renders the <a href=
   * "https://tabler.io/icons/icon/exclamation-circle">exclamation-circle</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction exclamationCircle(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0")),
        path(d("M12 9v4")),
        path(d("M12 16v.01"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/file-text">file-text</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction fileText(Html.Instruction... contents) {
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
   * Renders the <a href=
   * "https://tabler.io/icons/icon/health-recognition">health-recognition</a>
   * icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction healthRecognition(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M4 8v-2a2 2 0 0 1 2 -2h2")),
        path(d("M4 16v2a2 2 0 0 0 2 2h2")),
        path(d("M16 4h2a2 2 0 0 1 2 2v2")),
        path(d("M16 20h2a2 2 0 0 0 2 -2v-2")),
        path(d("M8.603 9.61a2.04 2.04 0 0 1 2.912 0l.485 .39l.5 -.396a2.035 2.035 0 0 1 2.897 .007a2.104 2.104 0 0 1 0 2.949l-3.397 3.44l-3.397 -3.44a2.104 2.104 0 0 1 0 -2.95z"))
    );
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/home">home</a> icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction home(Html.Instruction... contents) {
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
  @Override
  public final Html.ElementInstruction home2(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 12l-2 0l9 -9l9 9l-2 0")),
        path(d("M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7")),
        path(d("M10 12h4v4h-4z"))
    );
  }

  @Override
  public final Html.ElementInstruction mapPin(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M9 11a3 3 0 1 0 6 0a3 3 0 0 0 -6 0")),
        path(d("M17.657 16.657l-4.243 4.243a2 2 0 0 1 -2.827 0l-4.244 -4.243a8 8 0 1 1 11.314 0z"))
    );
  }

  @Override
  public final Html.ElementInstruction menu1(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M4 8l16 0")),
        path(d("M4 16l16 0"))
    );
  }

  @Override
  public final Html.ElementInstruction menu2(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M4 6l16 0")),
        path(d("M4 12l16 0")),
        path(d("M4 18l16 0"))
    );
  }

  @Override
  public final Html.ElementInstruction paw(Html.Instruction... contents) {
    // @formatter:off
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M14.7 13.5c-1.1 -2 -1.441 -2.5 -2.7 -2.5c-1.259 0 -1.736 .755 -2.836 2.747c-.942 1.703 -2.846 1.845 -3.321 3.291c-.097 .265 -.145 .677 -.143 .962c0 1.176 .787 2 1.8 2c1.259 0 3 -1 4.5 -1s3.241 1 4.5 1c1.013 0 1.8 -.823 1.8 -2c0 -.285 -.049 -.697 -.146 -.962c-.475 -1.451 -2.512 -1.835 -3.454 -3.538z")),
        path(d("M20.188 8.082a1.039 1.039 0 0 0 -.406 -.082h-.015c-.735 .012 -1.56 .75 -1.993 1.866c-.519 1.335 -.28 2.7 .538 3.052c.129 .055 .267 .082 .406 .082c.739 0 1.575 -.742 2.011 -1.866c.516 -1.335 .273 -2.7 -.54 -3.052z")),
        path(d("M9.474 9c.055 0 .109 0 .163 -.011c.944 -.128 1.533 -1.346 1.32 -2.722c-.203 -1.297 -1.047 -2.267 -1.932 -2.267c-.055 0 -.109 0 -.163 .011c-.944 .128 -1.533 1.346 -1.32 2.722c.204 1.293 1.048 2.267 1.933 2.267z")),
        path(d("M16.456 6.733c.214 -1.376 -.375 -2.594 -1.32 -2.722a1.164 1.164 0 0 0 -.162 -.011c-.885 0 -1.728 .97 -1.93 2.267c-.214 1.376 .375 2.594 1.32 2.722c.054 .007 .108 .011 .162 .011c.885 0 1.73 -.974 1.93 -2.267z")),
        path(d("M5.69 12.918c.816 -.352 1.054 -1.719 .536 -3.052c-.436 -1.124 -1.271 -1.866 -2.009 -1.866c-.14 0 -.277 .027 -.407 .082c-.816 .352 -1.054 1.719 -.536 3.052c.436 1.124 1.271 1.866 2.009 1.866c.14 0 .277 -.027 .407 -.082z"))
    );
    // @formatter:on
  }

  @Override
  public final Html.ElementInstruction phone(Html.Instruction... contents) {
    // @formatter:off
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M5 4h4l2 5l-2.5 1.5a11 11 0 0 0 5 5l1.5 -2.5l5 2v4a2 2 0 0 1 -2 2a16 16 0 0 1 -15 -15a2 2 0 0 1 2 -2"))
    );
    // @formatter:on
  }

  @Override
  public final Html.ElementInstruction user(Html.Instruction... contents) {
    // @formatter:off
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M8 7a4 4 0 1 0 8 0a4 4 0 0 0 -8 0")),
        path(d("M6 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2"))
    );
    // @formatter:on
  }

  @Override
  public final Html.ElementInstruction users(Html.Instruction... contents) {
    // @formatter:off
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M9 7m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0")),
        path(d("M3 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2")),
        path(d("M16 3.13a4 4 0 0 1 0 7.75")),
        path(d("M21 21v-2a4 4 0 0 0 -3 -3.85"))
    );
    // @formatter:on
  }

  /**
   * Renders the <a href="https://tabler.io/icons/icon/x">x</a> icon.
   *
   * @return the {@code svg} element
   */
  @Override
  public final Html.ElementInstruction x(Html.Instruction... contents) {
    return icon(
        flatten(contents),
        path(stroke("none"), d("M0 0h24v24H0z"), fill("none")),
        path(d("M18 6l-12 12")),
        path(d("M6 6l12 12"))
    );
  }

  private Html.ElementInstruction icon(Html.Instruction... contents) {
    Html.ElementInstruction svg;
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

  private Html.ElementInstruction filled(Html.Instruction... contents) {
    Html.ElementInstruction svg;
    svg = svg(
        id != null ? id : noop(),

        xmlns("http://www.w3.org/2000/svg"),
        width("24"),
        height("24"),
        viewBox("0 0 24 24"),
        fill("currentColor"),

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