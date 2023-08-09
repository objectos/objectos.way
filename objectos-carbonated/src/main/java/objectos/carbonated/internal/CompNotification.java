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

import objectos.carbonated.Notification;
import objectos.css.util.ClassSelector;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.lang.Check;

public final class CompNotification implements Notification {

  static final ClassSelector NOTIFICATION = ClassSelector.randomClassSelector(5);

  static final ClassSelector ICON = ClassSelector.randomClassSelector(5);

  static final ClassSelector LOW_CONTRAST = ClassSelector.randomClassSelector(5);

  static final ClassSelector ERROR = ClassSelector.randomClassSelector(5);

  static final ClassSelector SUCCESS = ClassSelector.randomClassSelector(5);

  static final ClassSelector INFO = ClassSelector.randomClassSelector(5);

  static final ClassSelector INFO_SQUARE = ClassSelector.randomClassSelector(5);

  static final ClassSelector WARNING = ClassSelector.randomClassSelector(5);

  static final ClassSelector WARNING_ALT = ClassSelector.randomClassSelector(5);

  static final ClassSelector DETAILS = ClassSelector.randomClassSelector(5);

  static final ClassSelector TEXT_WRAPPER = ClassSelector.randomClassSelector(5);

  static final ClassSelector TITLE = ClassSelector.randomClassSelector(5);

  static final ClassSelector SUBTITLE = ClassSelector.randomClassSelector(5);

  private Contrast contrast = Contrast.HIGH;

  private Status status = Status.ERROR;

  private String title = "";

  private String subtitle = "";

  private final HtmlTemplate html = new HtmlTemplate() {
    @Override
    protected void definition() {
      div(
        NOTIFICATION,

        switch (status) {
          case ERROR -> CompNotification.ERROR;
          case SUCCESS -> CompNotification.SUCCESS;
          case WARNING -> CompNotification.WARNING;
          case INFO -> CompNotification.INFO;
        },

        switch (contrast) {
          case LOW -> CompNotification.LOW_CONTRAST;
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
    }
  };

  @Override
  public final Notification contrast(Contrast value) {
    contrast = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Notification status(Status value) {
    status = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Notification subtitle(String value) {
    subtitle = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Notification title(String value) {
    title = Check.notNull(value, "value == null");

    return null;
  }

  @Override
  public final ElementContents render() {
    return html;
  }

}