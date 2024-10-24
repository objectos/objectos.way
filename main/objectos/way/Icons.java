/*
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
 * The <strong>Objectos Icons</strong> main class.
 */
public final class Icons {

  /**
   * Provides SVG icons from the
   * <a href="https://github.com/twbs/icons">Bootstrap</a> project.
   */
  public sealed interface Bootstrap permits IconsBootstrap {

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/envelope/">envelope</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement envelope(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/envelope-fill/">envelope
     * fill</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement envelopeFill(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/github/">GitHub</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement github(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/linkedin/">Linkedin</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement linkedin(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/list/">List</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement list(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/person-vcard/">Person
     * vcard</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement personVcard(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/three-dots/">three-dots</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement threeDots(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/twitter/">Twitter</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement twitter(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/twitter-x/">Twitter X</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement twitterX(Html.Instruction... contents);

    /**
     * Renders the <a href="https://icons.getbootstrap.com/icons/x/">x</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement x(Html.Instruction... contents);

  }

  /**
   * Provides SVG icons from the <a href="https://tabler-icons.io/">tabler
   * icons</a> project.
   */
  public sealed interface Tabler permits IconsTabler {

    /**
     * The value of {@code id} attribute of the rendered SVG element.
     *
     * @param id
     *        the id value
     *
     * @return this instance
     */
    Tabler id(Html.Id id);

    /**
     * The value of the {@code stroke-width} attribute of the rendered SVG
     * element.
     *
     * @param value
     *        the width value
     *
     * @return this instance
     */
    Tabler strokeWidth(double value);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/arrow-left">arrow-left</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement arrowLeft(Html.Instruction... contents);

    /**
     * Renders the <a href=
     * "https://tabler.io/icons/icon/arrow-narrow-left">arrow-narrow-left</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement arrowNarrowLeft(Html.Instruction... contents);

    /**
     * Renders the <a href=
     * "https://tabler.io/icons/icon/arrow-narrow-right">arrow-narrow-right</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement arrowNarrowRight(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/arrow-right">arrow-right</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement arrowRight(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/box">box</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement box(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/briefcase">briefcase</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement briefcase(Html.Instruction... contents);

    /**
     * Renders the
     * <a href=
     * "https://tabler.io/icons/icon/calendar-repeat">calendar-repeat</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement calendarRepeat(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/caret-left">caret-left
     * (filled)</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement caretLeftFilled(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/caret-right">caret-right
     * (filled)</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement caretRightFilled(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/chevron-left">chevron-left</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement chevronLeft(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/chevron-right">chevron-right</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement chevronRight(Html.Instruction... contents);

    /**
     * Renders the
     * <a href=
     * "https://tabler.io/icons/icon/credit-card-pay">credit-card-pay</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement creditCardPay(Html.Instruction... contents);

    /**
     * Renders the <a href=
     * "https://tabler.io/icons/icon/exclamation-circle">exclamation-circle</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement exclamationCircle(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://tabler.io/icons/icon/file-text">file-text</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement fileText(Html.Instruction... contents);

    /**
     * Renders the <a href=
     * "https://tabler.io/icons/icon/health-recognition">health-recognition</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement healthRecognition(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/home">home</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement home(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/home-2">home-2</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement home2(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/map-pin">map-pin</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement mapPin(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/menu">menu</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement menu1(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler-icons.io/i/menu-2">menu-2</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement menu2(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler-icons.io/i/paw">paw</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement paw(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/user">phone</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement phone(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/user">user</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement user(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/users">users</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement users(Html.Instruction... contents);

    /**
     * Renders the <a href="https://tabler.io/icons/icon/x">x</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.Instruction.OfElement x(Html.Instruction... contents);

  }

  private Icons() {}

  /**
   * Creates a new Bootstrap icon provider bound to the specified parent
   * template.
   *
   * @param parent
   *        the parent template or component
   *
   * @return a new Bootstrap icon provider
   */
  public static Bootstrap bootstrap(Html.TemplateBase parent) {
    return new IconsBootstrap(parent);
  }

  /**
   * Creates a new Tabler icon provider bound to the specified parent
   * template.
   *
   * @param parent
   *        the parent template or component
   *
   * @return a new Tabler icon provider
   */
  public static Tabler tabler(Html.TemplateBase parent) {
    return new IconsTabler(parent);
  }

}
