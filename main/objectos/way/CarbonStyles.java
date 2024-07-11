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

final class CarbonStyles implements Http.Handler {

  private Css.StyleSheet generateStyleSheet() {
    return Css.generateStyleSheet(
        Css.classes(Carbon.Ui.class),

        Css.rule(".cds--white", """
        --cds-background: #ffffff;
        --cds-border-subtle-00: #e0e0e0;
        --cds-border-subtle-01: #c6c6c6;
        --cds-border-subtle-02: #e0e0e0;
        --cds-border-subtle-03: #c6c6c6;
        --cds-focus: #0f62fe;
        --cds-focus-inset: #ffffff;
        --cds-focus-inverse: #ffffff;
        """),

        Css.rule(".cds--g10", """
        --cds-background: #f4f4f4;
        --cds-border-subtle-00: #c6c6c6;
        --cds-border-subtle-01: #e0e0e0;
        --cds-border-subtle-02: #c6c6c6;
        --cds-border-subtle-03: #e0e0e0;
        --cds-focus: #0f62fe;
        --cds-focus-inset: #ffffff;
        --cds-focus-inverse: #ffffff;
        """),

        Css.rule(".cds--g90", """
        --cds-background: #262626;
        --cds-border-subtle-00: #525252;
        --cds-border-subtle-01: #6f6f6f;
        --cds-border-subtle-02: #8d8d8d;
        --cds-border-subtle-03: #8d8d8d;
        --cds-focus: #ffffff;
        --cds-focus-inset: #161616;
        --cds-focus-inverse: #0f62fe;
        """),

        Css.rule(".cds--g100", """
        --cds-background: #161616;
        --cds-border-subtle-00: #393939;
        --cds-border-subtle-01: #525252;
        --cds-border-subtle-02: #6f6f6f;
        --cds-border-subtle-03: #6f6f6f;
        --cds-focus: #ffffff;
        --cds-focus-inset: #161616;
        --cds-focus-inverse: #0f62fe;
        """),

        Css.rule(":root", """
        --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);

        --cds-body-compact-01-font-size: 0.875rem;
        --cds-body-compact-01-font-weight: 400;
        --cds-body-compact-01-line-height: 1.28572;
        --cds-body-compact-01-letter-spacing: 0.16px;
        """),

        Css.overrideBackgroundColor("""
        background: var(--cds-background)
        """),

        Css.overrideBorderColor("""
        focus: var(--cds-focus)
        subtle: var(--cds-border-subtle)

        transparent: transparent
        """),

        Css.overrideFontSize("""
        body-compact-01: var(--cds-body-compact-01-font-size, 0.875rem)/var(--cds-body-compact-01-line-height, 1.28572)/var(--cds-body-compact-01-letter-spacing, 0.16px)/var(--cds-body-compact-01-font-weight, 400)

        heading-compact-01: var(--cds-heading-compact-01-font-size, 0.875rem)/var(--cds-heading-compact-01-line-height, 1.28572)/var(--cds-heading-compact-01-letter-spacing, 0.16px)/var(--cds-heading-compact-01-font-weight, 600)
        """),

        Css.overrideSpacing("""
        0px: 0px
        1px: 0.0625rem
        2px: 0.125rem
        4px: 0.25rem
        6px: 0.375rem
        8px: 0.5rem

        10px: 0.625rem
        12px: 0.75rem
        14px: 0.875rem
        16px: 1rem

        20px: 1.25rem
        24px: 1.5rem
        28px: 1.75rem

        32px: 2rem
        36px: 2.25rem

        40px: 2.5rem
        44px: 2.75rem
        48px: 3rem

        208px: 13rem
        224px: 14rem
        240px: 15rem
        256px: 16rem
        288px: 18rem
        """)
    );
  }

  @Override
  public final void handle(Http.Exchange http) {
    Css.StyleSheet s;
    s = generateStyleSheet();

    byte[] bytes;
    bytes = s.toByteArray();

    http.status(Http.OK);

    http.dateNow();

    http.header(Http.CONTENT_TYPE, s.contentType());

    http.header(Http.CONTENT_LENGTH, bytes.length);

    http.send(bytes);
  }

}