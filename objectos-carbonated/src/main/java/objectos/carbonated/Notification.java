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
package objectos.carbonated;

import objectos.css.CssTemplate;
import objectos.css.util.ClassSelector;

public final class Notification extends CssTemplate {

  public static final ClassSelector NOTIFICATION = ClassSelector.randomClassSelector(5);

  public static final ClassSelector ICON = ClassSelector.randomClassSelector(5);

  public static final ClassSelector LOW_CONTRAST = ClassSelector.randomClassSelector(5);

  public static final ClassSelector ERROR = ClassSelector.randomClassSelector(5);

  public static final ClassSelector SUCCESS = ClassSelector.randomClassSelector(5);

  public static final ClassSelector INFO = ClassSelector.randomClassSelector(5);

  public static final ClassSelector INFO_SQUARE = ClassSelector.randomClassSelector(5);

  public static final ClassSelector DETAILS = ClassSelector.randomClassSelector(5);

  public static final ClassSelector TEXT_WRAPPER = ClassSelector.randomClassSelector(5);

  public static final ClassSelector TITLE = ClassSelector.randomClassSelector(5);

  public static final ClassSelector SUBTITLE = ClassSelector.randomClassSelector(5);

  @Override
  protected final void definition() {
    style(
      NOTIFICATION,

      color(var(Theme.TEXT_INVERSE)),
      display(flex),
      flexWrap(wrap),
      height(auto),
      maxWidth(rem(18)),
      minHeight(rem(3)),
      minWidth(rem(18)),
      position(relative),
      width(pct(100))
    );

    media(
      minWidth(Breakpoint.MEDIUM),

      style(
        NOTIFICATION,

        flexWrap(nowrap),
        maxWidth(rem(608 / 16))
      )
    );

    media(
      minWidth(Breakpoint.LARGE),

      style(
        NOTIFICATION,

        maxWidth(rem(736 / 16))
      )
    );

    media(
      minWidth(Breakpoint.MAX),

      style(
        NOTIFICATION,

        maxWidth(rem(832 / 16))
      )
    );

    /*
    .cds--inline-notification:not(.cds--inline-notification--low-contrast) a {
      color: var(--cds-link-inverse,#78a9ff)
    }
     */

    style(
      NOTIFICATION, SP, a,

      textDecoration(none)
    );

    style(
      NOTIFICATION, SP, a, _hover,

      textDecoration(underline)
    );

    style(
      NOTIFICATION, SP, a, _focus,

      outline(px(1), solid, var(Theme.LINK_INVERSE))
    );

    style(
      NOTIFICATION, LOW_CONTRAST, SP, a, _focus,

      outline(px(1), solid, var(Theme.FOCUS))
    );

    style(
      LOW_CONTRAST,

      color(var(Theme.TEXT_PRIMARY))
    );

    style(
      LOW_CONTRAST, __before,

      position(absolute),
      top($0),
      left($0),
      width(pct(100)),
      height(pct(100)),
      boxSizing(borderBox),
      borderWidth(px(1), px(1), px(1), $0),
      borderStyle(solid),
      content(""),
      webkitFilter(opacity(.4)),
      filter(opacity(.4)),
      pointerEvents(none)
    );

    //

    style(
      ERROR,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_ERROR_INVERSE))
    );

    style(
      ERROR, SP, ICON, OR,

      fill(var(Theme.SUPPORT_ERROR_INVERSE))
    );

    style(
      LOW_CONTRAST, ERROR,

      background(var(Theme.NOTIFICATION_BACKGROUND_ERROR)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_ERROR))
    );

    style(
      LOW_CONTRAST, ERROR, __before,

      borderColor(var(Theme.SUPPORT_ERROR))
    );

    style(
      LOW_CONTRAST, ERROR, SP, ICON,

      fill(var(Theme.SUPPORT_ERROR))
    );

    //

    style(
      SUCCESS,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_SUCCESS_INVERSE))
    );

    style(
      SUCCESS, SP, ICON,

      fill(var(Theme.SUPPORT_SUCCESS_INVERSE))
    );

    style(
      LOW_CONTRAST, SUCCESS,

      background(var(Theme.NOTIFICATION_BACKGROUND_SUCCESS)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_SUCCESS))
    );

    style(
      LOW_CONTRAST, SUCCESS, __before,

      borderColor(var(Theme.SUPPORT_ERROR))
    );

    style(
      LOW_CONTRAST, SUCCESS, SP, ICON,

      fill(var(Theme.SUPPORT_SUCCESS))
    );

    //

    style(
      INFO, OR,
      INFO_SQUARE,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_INFO_INVERSE))
    );

    style(
      SUCCESS, SP, ICON,

      fill(var(Theme.SUPPORT_SUCCESS_INVERSE))
    );

    style(
      LOW_CONTRAST, SUCCESS,

      background(var(Theme.NOTIFICATION_BACKGROUND_SUCCESS)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_SUCCESS))
    );

    style(
      LOW_CONTRAST, SUCCESS, __before,

      borderColor(var(Theme.SUPPORT_ERROR))
    );

    style(
      LOW_CONTRAST, SUCCESS, SP, ICON,

      fill(var(Theme.SUPPORT_SUCCESS))
    );

    /*

    .cds--inline-notification--info,.cds--inline-notification--info-square {
    border-left: 3px solid var(--cds-support-info-inverse,#4589ff);
    background: var(--cds-background-inverse,#393939)
    }

    .cds--inline-notification--info .cds--inline-notification__icon,.cds--inline-notification--info .cds--toast-notification__icon,.cds--inline-notification--info .cds--actionable-notification__icon,.cds--inline-notification--info-square .cds--inline-notification__icon,.cds--inline-notification--info-square .cds--toast-notification__icon,.cds--inline-notification--info-square .cds--actionable-notification__icon {
    fill: var(--cds-support-info-inverse,#4589ff)
    }

    .cds--inline-notification--low-contrast.cds--inline-notification--info,.cds--inline-notification--low-contrast.cds--inline-notification--info-square {
    border-left: 3px solid var(--cds-support-info,#0043ce);
    background: var(--cds-notification-background-info,#edf5ff)
    }

    .cds--inline-notification--low-contrast.cds--inline-notification--info .cds--inline-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--info .cds--toast-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--info .cds--actionable-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--info-square .cds--inline-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--info-square .cds--toast-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--info-square .cds--actionable-notification__icon {
    fill: var(--cds-support-info,#0043ce)
    }

    .cds--inline-notification--low-contrast.cds--inline-notification--info::before,.cds--inline-notification--low-contrast.cds--inline-notification--info-square::before {
    border-color: var(--cds-support-info,#0043ce)
    }

    .cds--inline-notification--warning,.cds--inline-notification--warning-alt {
    border-left: 3px solid var(--cds-support-warning-inverse,#f1c21b);
    background: var(--cds-background-inverse,#393939)
    }

    .cds--inline-notification--warning .cds--inline-notification__icon,.cds--inline-notification--warning .cds--toast-notification__icon,.cds--inline-notification--warning .cds--actionable-notification__icon,.cds--inline-notification--warning-alt .cds--inline-notification__icon,.cds--inline-notification--warning-alt .cds--toast-notification__icon,.cds--inline-notification--warning-alt .cds--actionable-notification__icon {
    fill: var(--cds-support-warning-inverse,#f1c21b)
    }

    .cds--inline-notification--low-contrast.cds--inline-notification--warning,.cds--inline-notification--low-contrast.cds--inline-notification--warning-alt {
    border-left: 3px solid var(--cds-support-warning,#f1c21b);
    background: var(--cds-notification-background-warning,#fdf6dd)
    }

    .cds--inline-notification--low-contrast.cds--inline-notification--warning .cds--inline-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--warning .cds--toast-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--warning .cds--actionable-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--warning-alt .cds--inline-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--warning-alt .cds--toast-notification__icon,.cds--inline-notification--low-contrast.cds--inline-notification--warning-alt .cds--actionable-notification__icon {
    fill: var(--cds-support-warning,#f1c21b)
    }

    .cds--inline-notification--low-contrast.cds--inline-notification--warning::before,.cds--inline-notification--low-contrast.cds--inline-notification--warning-alt::before {
    border-color: var(--cds-support-warning,#f1c21b)
    }

    .cds--inline-notification--warning .cds--inline-notification__icon path[opacity="0"],.cds--inline-notification--warning-alt .cds--inline-notification__icon path:first-of-type {
    fill: #000;
    opacity: 1
    }

    */

    style(
      DETAILS,

      display(flex),
      flexGrow(1),
      margin($0, rem(3), $0, rem(0.8125))
    );

    media(
      minWidth(Breakpoint.MEDIUM),

      style(
        DETAILS,

        margin($0, rem(0.8125)))
    );

    style(
      ICON,

      flexShrink(0),
      marginTop(rem(0.875)),
      marginRight(rem(1))
    );

    style(
      TEXT_WRAPPER,

      display(flex),
      flexWrap(wrap),
      padding(rem(0.9375), $0)
    );

    style(
      TITLE,

      fontSize(var(Theme.HEADING_COMPACT_01_FONT_SIZE)),
      fontWeight(var(Theme.HEADING_COMPACT_01_FONT_WEIGHT)),
      lineHeight(var(Theme.HEADING_COMPACT_01_LINE_HEIGHT)),
      letterSpacing(var(Theme.HEADING_COMPACT_01_LETTER_SPACING)),
      margin($0, rem(0.25), $0, $0)
    );

    style(
      SUBTITLE,

      fontSize(var(Theme.BODY_COMPACT_01_FONT_SIZE)),
      fontWeight(var(Theme.BODY_COMPACT_01_FONT_WEIGHT)),
      lineHeight(var(Theme.BODY_COMPACT_01_LINE_HEIGHT)),
      letterSpacing(var(Theme.BODY_COMPACT_01_LETTER_SPACING)),
      wordBreak(breakWord)
    );

    /*

    .cds--inline-notification__action-button.cds--btn--ghost {
    height: 2rem;
    margin-bottom: .5rem;
    margin-left: 2.5rem
    }

    @media(min-width: 42rem) {
    .cds--inline-notification__action-button.cds--btn--ghost {
        margin:.5rem 0
    }
    }

    .cds--inline-notification:not(.cds--inline-notification--low-contrast) .cds--inline-notification__action-button.cds--btn--ghost {
    color: var(--cds-link-inverse,#78a9ff)
    }

    .cds--inline-notification__action-button.cds--btn--ghost:active,.cds--inline-notification__action-button.cds--btn--ghost:hover {
    background-color: var(--cds-background-inverse-hover,#474747)
    }

    .cds--inline-notification--low-contrast .cds--inline-notification__action-button.cds--btn--ghost:active,.cds--inline-notification--low-contrast .cds--inline-notification__action-button.cds--btn--ghost:hover {
    background-color: var(--cds-notification-action-hover,#edf5ff)
    }

    .cds--inline-notification__action-button.cds--btn--ghost:focus {
    border-color: transparent;
    box-shadow: none;
    outline: 2px solid var(--cds-focus-inverse,#ffffff);
    outline-offset: -2px
    }

    .cds--inline-notification--low-contrast .cds--inline-notification__action-button.cds--btn--ghost:focus {
    outline-color: var(--cds-focus,#0f62fe)
    }

    .cds--inline-notification--hide-close-button .cds--inline-notification__action-button.cds--btn--ghost {
    margin-right: .5rem
    }

    .cds--inline-notification__close-button {
    outline: 2px solid transparent;
    outline-offset: -2px;
    position: absolute;
    top: 0;
    right: 0;
    display: flex;
    width: 3rem;
    min-width: 3rem;
    max-width: 3rem;
    height: 3rem;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 0;
    border: none;
    background: transparent;
    cursor: pointer;
    transition: outline 110ms cubic-bezier(.2,0,.38,.9),background-color 110ms cubic-bezier(.2,0,.38,.9)
    }

    .cds--inline-notification__close-button:focus {
    outline: 2px solid var(--cds-focus-inverse,#ffffff);
    outline-offset: -2px
    }

    .cds--inline-notification__close-button .cds--inline-notification__close-icon {
    fill: var(--cds-icon-inverse,#ffffff)
    }

    @media(min-width: 42rem) {
    .cds--inline-notification__close-button {
        position:static
    }
    }

    .cds--inline-notification--low-contrast .cds--inline-notification__close-button:focus {
    outline: 2px solid var(--cds-focus,#0f62fe);
    outline-offset: -2px
    }

    @media screen and (prefers-contrast) {
    .cds--inline-notification--low-contrast .cds--inline-notification__close-button:focus {
        outline-style: dotted
    }
    }

    .cds--inline-notification--low-contrast .cds--inline-notification__close-button .cds--inline-notification__close-icon {
    fill: var(--cds-icon-primary,#161616)
    }

    @media screen and (-ms-high-contrast:active),(forced-colors:active) {
    .cds--inline-notification {
        outline: 1px solid transparent
    }
    }

    @media screen and (-ms-high-contrast:active),(forced-colors:active) {
    .cds--inline-notification__close-button:focus,.cds--btn.cds--btn--ghost.cds--inline-notification__action-button:focus {
        color: Highlight;
        outline: 1px solid Highlight
    }
    }

    @media screen and (-ms-high-contrast:active),(forced-colors:active) {
    .cds--inline-notification .cds--inline-notification__icon {
        fill: ButtonText
    }
    }

    @media screen and (-ms-high-contrast:active),(forced-colors:active) {
    .cds--inline-notification .cds--inline-notification__close-icon {
        fill: ButtonText
    }
    }

     */
  }

}