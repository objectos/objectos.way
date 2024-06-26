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
 * The Objectos Icons main class.
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
    Html.ElementInstruction envelope(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/envelope-fill/">envelope
     * fill</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction envelopeFill(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/github/">GitHub</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction github(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/linkedin/">Linkedin</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction linkedin(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/list/">List</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction list(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/three-dots/">three-dots</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction threeDots(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/twitter/">Twitter</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction twitter(Html.Instruction... contents);

    /**
     * Renders the
     * <a href="https://icons.getbootstrap.com/icons/twitter-x/">Twitter X</a>
     * icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction twitterX(Html.Instruction... contents);

    /**
     * Renders the <a href="https://icons.getbootstrap.com/icons/x/">x</a> icon.
     *
     * @return the {@code svg} element
     */
    Html.ElementInstruction x(Html.Instruction... contents);

  }

  private Icons() {}

  public static Bootstrap bootstrap(Html.TemplateBase parent) {
    return new IconsBootstrap(parent);
  }

}
