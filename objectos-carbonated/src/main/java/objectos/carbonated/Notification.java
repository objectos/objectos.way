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
import objectos.html.HtmlComponent;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.lang.Check;

public final class Notification extends HtmlComponent {

  public enum Contrast {
    HIGH,

    LOW;
  }

  public enum Status {
    ERROR,

    SUCCESS,

    WARNING,

    INFO;
  }

  private static final ClassSelector NOTIFICATION = U.cs("notification");

  private static final ClassSelector ICON = U.cs("notification-icon");

  private static final ClassSelector LOW_CONTRAST = U.cs("notification-low-contrast");

  private static final ClassSelector ERROR = U.cs("notification-error");

  private static final ClassSelector SUCCESS = U.cs("notification-success");

  private static final ClassSelector INFO = U.cs("notification-info");

  private static final ClassSelector INFO_SQUARE = U.cs("notification-info-square");

  private static final ClassSelector WARNING = U.cs("notification-warning");

  private static final ClassSelector WARNING_ALT = U.cs("notification-warning-alt");

  private static final ClassSelector DETAILS = U.cs("notification-details");

  private static final ClassSelector TEXT_WRAPPER = U.cs("notification-text-wrapper");

  private static final ClassSelector TITLE = U.cs("notification-title");

  private static final ClassSelector SUBTITLE = U.cs("notification-subtitle");

  private Contrast contrast;

  private Status status;

  public Notification(HtmlTemplate parent) {
    super(parent);

    reset();
  }

  public final Notification contrast(Contrast value) {
    contrast = Check.notNull(value, "value == null");

    return this;
  }

  public final Notification status(Status value) {
    status = Check.notNull(value, "value == null");

    return this;
  }

  public final ElementContents render(String title) {
    return render(title, "");
  }

  public final ElementContents render(String title, String subtitle) {
    Check.notNull(title, "title == null");
    Check.notNull(subtitle, "subtitle == null");

    ElementContents div;
    div = div(
      NOTIFICATION,

      switch (status) {
        case ERROR -> Notification.ERROR;
        case SUCCESS -> Notification.SUCCESS;
        case WARNING -> Notification.WARNING;
        case INFO -> Notification.INFO;
      },

      switch (contrast) {
        case LOW -> Notification.LOW_CONTRAST;
        case HIGH -> noop();
      },

      div(
        DETAILS,

        switch (status) {
          case ERROR -> svg(
            ICON,

            xmlns("http://www.w3.org/2000/svg"),
            width("20"),
            height("20"),
            viewBox("0 0 32 32"),
            path(
              fill("none"),
              d("M9,10.56l1.56,-1.56l12.44,12.44l-1.56,1.56Z")
            ),
            path(
              d("M16,2A13.914,13.914,0,0,0,2,16,13.914,13.914,0,0,0,16,30,13.914,13.914,0,0,0,30,16,13.914,13.914,0,0,0,16,2Zm5.4449,21L9,10.5557,10.5557,9,23,21.4448Z")
            )
          );

          case SUCCESS -> svg(
            ICON,

            xmlns("http://www.w3.org/2000/svg"),
            width("20"),
            height("20"),
            viewBox("0 0 32 32"),
            path(
              d("M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2ZM14,21.5908l-5-5L10.5906,15,14,18.4092,21.41,11l1.5957,1.5859Z")
            ),
            path(
              fill("none"),
              d("M14 21.591 9 16.591 10.591 15 14 18.409 21.41 11 23.005 12.585 14 21.591z")
            )
          );

          case WARNING -> svg(
            ICON,

            xmlns("http://www.w3.org/2000/svg"),
            width("20"),
            height("20"),
            viewBox("0 0 32 32"),
            path(
              d("M16,2C8.3,2,2,8.3,2,16s6.3,14,14,14s14-6.3,14-14C30,8.3,23.7,2,16,2z M14.9,8h2.2v11h-2.2V8z M16,25 c-0.8,0-1.5-0.7-1.5-1.5S15.2,22,16,22c0.8,0,1.5,0.7,1.5,1.5S16.8,25,16,25z")
            ),
            path(
              fill("#000"),
              d("M17.5,23.5c0,0.8-0.7,1.5-1.5,1.5c-0.8,0-1.5-0.7-1.5-1.5S15.2,22,16,22 C16.8,22,17.5,22.7,17.5,23.5z M17.1,8h-2.2v11h2.2V8z")
            )
          );

          case INFO -> svg(
            ICON,

            xmlns("http://www.w3.org/2000/svg"),
            width("20"),
            height("20"),
            viewBox("0 0 32 32"),
            path(
              fill("none"),
              d("M16,8a1.5,1.5,0,1,1-1.5,1.5A1.5,1.5,0,0,1,16,8Zm4,13.875H17.125v-8H13v2.25h1.875v5.75H12v2.25h8Z")
            ),
            path(
              d("M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2Zm0,6a1.5,1.5,0,1,1-1.5,1.5A1.5,1.5,0,0,1,16,8Zm4,16.125H12v-2.25h2.875v-5.75H13v-2.25h4.125v8H20Z")
            )
          );
        },

        div(
          TEXT_WRAPPER,

          div(TITLE, t(title)),

          div(SUBTITLE, t(subtitle))
        )
      )
    );

    reset();

    return div;
  }

  private void reset() {
    contrast = Contrast.HIGH;

    status = Status.ERROR;
  }

  static final class Styles extends CssTemplate {

    private final Breakpoints breakpoints;

    Styles(Breakpoints breakpoints) {
      this.breakpoints = breakpoints;
    }

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
        minWidth(breakpoints.medium()),

        style(
          NOTIFICATION,

          flexWrap(nowrap),
          maxWidth(rem(608 / 16)))
      );

      media(
        minWidth(breakpoints.large()),

        style(
          NOTIFICATION,

          maxWidth(rem(736 / 16)))
      );

      media(
        minWidth(breakpoints.max()),

        style(
          NOTIFICATION,

          maxWidth(rem(832 / 16)))
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
        borderLeft(px(3), solid, var(Theme.SUPPORT_ERROR_INVERSE)));

      style(
        ERROR, SP, ICON,

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

        borderColor(var(Theme.SUPPORT_SUCCESS))
      );

      style(
        LOW_CONTRAST, SUCCESS, SP, ICON,

        fill(var(Theme.SUPPORT_SUCCESS))
      );

      //

      style(
        INFO, OR, INFO_SQUARE,

        background(var(Theme.BACKGROUND_INVERSE)),
        borderLeft(px(3), solid, var(Theme.SUPPORT_INFO_INVERSE))
      );

      style(
        INFO, SP, ICON,

        fill(var(Theme.SUPPORT_INFO_INVERSE))
      );

      style(
        LOW_CONTRAST, INFO,

        background(var(Theme.NOTIFICATION_BACKGROUND_INFO)),
        borderLeft(px(3), solid, var(Theme.SUPPORT_INFO))
      );

      style(
        LOW_CONTRAST, INFO, __before,

        borderColor(var(Theme.SUPPORT_INFO))
      );

      style(
        LOW_CONTRAST, INFO, SP, ICON,

        fill(var(Theme.SUPPORT_INFO))
      );

      //

      style(
        WARNING, OR, WARNING_ALT,

        background(var(Theme.BACKGROUND_INVERSE)),
        borderLeft(px(3), solid, var(Theme.SUPPORT_WARNING_INVERSE))
      );

      style(
        WARNING, SP, ICON,

        fill(var(Theme.SUPPORT_WARNING_INVERSE))
      );

      style(
        LOW_CONTRAST, WARNING,

        background(var(Theme.NOTIFICATION_BACKGROUND_WARNING)),
        borderLeft(px(3), solid, var(Theme.SUPPORT_WARNING))
      );

      style(
        LOW_CONTRAST, WARNING, __before,

        borderColor(var(Theme.SUPPORT_WARNING))
      );

      style(
        LOW_CONTRAST, WARNING, SP, ICON,

        fill(var(Theme.SUPPORT_WARNING))
      );

      /*
      
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
        minWidth(breakpoints.medium()),

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

        fontSize(var(Typography.HEADING_COMPACT_01_FONT_SIZE)),
        fontWeight(var(Typography.HEADING_COMPACT_01_FONT_WEIGHT)),
        letterSpacing(var(Typography.HEADING_COMPACT_01_LETTER_SPACING)),
        lineHeight(var(Typography.HEADING_COMPACT_01_LINE_HEIGHT)),
        margin($0, rem(0.25), $0, $0)
      );

      style(
        SUBTITLE,

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

}