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
package objectos.carbonated.internal;

import objectos.carbonated.Carbon.Breakpoints;
import objectos.carbonated.Theme;
import objectos.carbonated.Typography;
import objectos.css.CssTemplate;

final class CompNotificationStyles extends CssTemplate {

  private final Breakpoints breakpoints;

  CompNotificationStyles(Breakpoints breakpoints) {
    this.breakpoints = breakpoints;
  }

  @Override
  protected final void definition() {
    style(
      CompNotification.NOTIFICATION,

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
      minWidth(breakpoints.medium()),

      style(
        CompNotification.NOTIFICATION,

        flexWrap(nowrap),
        maxWidth(rem(608 / 16)))
    );

    media(
      minWidth(breakpoints.large()),

      style(
        CompNotification.NOTIFICATION,

        maxWidth(rem(736 / 16)))
    );

    media(
      minWidth(breakpoints.max()),

      style(
        CompNotification.NOTIFICATION,

        maxWidth(rem(832 / 16)))
    );

    /*
    .cds--inline-notification:not(.cds--inline-notification--low-contrast) a {
      color: var(--cds-link-inverse,#78a9ff)
    }
     */

    style(
      CompNotification.NOTIFICATION, SP, a,

      textDecoration(none)
    );

    style(
      CompNotification.NOTIFICATION, SP, a, _hover,

      textDecoration(underline)
    );

    style(
      CompNotification.NOTIFICATION, SP, a, _focus,

      outline(px(1), solid, var(Theme.LINK_INVERSE))
    );

    style(
      CompNotification.NOTIFICATION, CompNotification.LOW_CONTRAST, SP, a, _focus,

      outline(px(1), solid, var(Theme.FOCUS))
    );

    style(
      CompNotification.LOW_CONTRAST,

      color(var(Theme.TEXT_PRIMARY))
    );

    style(
      CompNotification.LOW_CONTRAST, __before,

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
      CompNotification.ERROR,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_ERROR_INVERSE)));

    style(
      CompNotification.ERROR, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_ERROR_INVERSE))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.ERROR,

      background(var(Theme.NOTIFICATION_BACKGROUND_ERROR)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_ERROR))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.ERROR, __before,

      borderColor(var(Theme.SUPPORT_ERROR))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.ERROR, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_ERROR))
    );

    //

    style(
      CompNotification.SUCCESS,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_SUCCESS_INVERSE))
    );

    style(
      CompNotification.SUCCESS, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_SUCCESS_INVERSE))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.SUCCESS,

      background(var(Theme.NOTIFICATION_BACKGROUND_SUCCESS)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_SUCCESS))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.SUCCESS, __before,

      borderColor(var(Theme.SUPPORT_SUCCESS))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.SUCCESS, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_SUCCESS))
    );

    //

    style(
      CompNotification.INFO, OR, CompNotification.INFO_SQUARE,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_INFO_INVERSE))
    );

    style(
      CompNotification.INFO, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_INFO_INVERSE))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.INFO,

      background(var(Theme.NOTIFICATION_BACKGROUND_INFO)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_INFO))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.INFO, __before,

      borderColor(var(Theme.SUPPORT_INFO))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.INFO, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_INFO))
    );

    //

    style(
      CompNotification.WARNING, OR, CompNotification.WARNING_ALT,

      background(var(Theme.BACKGROUND_INVERSE)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_WARNING_INVERSE))
    );

    style(
      CompNotification.WARNING, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_WARNING_INVERSE))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.WARNING,

      background(var(Theme.NOTIFICATION_BACKGROUND_WARNING)),
      borderLeft(px(3), solid, var(Theme.SUPPORT_WARNING))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.WARNING, __before,

      borderColor(var(Theme.SUPPORT_WARNING))
    );

    style(
      CompNotification.LOW_CONTRAST, CompNotification.WARNING, SP, CompNotification.ICON,

      fill(var(Theme.SUPPORT_WARNING))
    );

    /*

    .cds--inline-notification--warning .cds--inline-notification__icon path[opacity="0"],.cds--inline-notification--warning-alt .cds--inline-notification__icon path:first-of-type {
    fill: #000;
    opacity: 1
    }

    */

    style(
      CompNotification.DETAILS,

      display(flex),
      flexGrow(1),
      margin($0, rem(3), $0, rem(0.8125))
    );

    media(
      minWidth(breakpoints.medium()),

      style(
        CompNotification.DETAILS,

        margin($0, rem(0.8125)))
    );

    style(
      CompNotification.ICON,

      flexShrink(0),
      marginTop(rem(0.875)),
      marginRight(rem(1))
    );

    style(
      CompNotification.TEXT_WRAPPER,

      display(flex),
      flexWrap(wrap),
      padding(rem(0.9375), $0)
    );

    style(
      CompNotification.TITLE,

      fontSize(var(Typography.HEADING_COMPACT_01_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_COMPACT_01_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_COMPACT_01_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_COMPACT_01_LINE_HEIGHT)),
      margin($0, rem(0.25), $0, $0)
    );

    style(
      CompNotification.SUBTITLE,

      fontSize(var(Typography.BODY_COMPACT_01_FONT_SIZE)),
      fontWeight(var(Typography.BODY_COMPACT_01_FONT_WEIGHT)),
      letterSpacing(var(Typography.BODY_COMPACT_01_LETTER_SPACING)),
      lineHeight(var(Typography.BODY_COMPACT_01_LINE_HEIGHT)),
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