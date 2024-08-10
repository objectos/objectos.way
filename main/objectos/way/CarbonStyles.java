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

import java.util.Set;

final class CarbonStyles implements Http.Handler {

  private final Set<Class<?>> classes;

  CarbonStyles(Set<Class<?>> classes) {
    this.classes = classes;
  }

  private Css.StyleSheet generateStyleSheet() {
    return Css.generateStyleSheet(
        Css.classes(classes),

        Css.classes(
            CarbonContent.class,
            CarbonHeader.class,
            CarbonHeaderCloseButton.class,
            CarbonHeaderMenuButton.class,
            CarbonHeaderMenuItem.class,
            CarbonHeaderName.class,
            CarbonHeaderNavigation.class,
            CarbonProgressIndicator.class,
            CarbonShell.class,
            CarbonShellContent.class,
            CarbonSideNav.class,
            CarbonSideNavMenuItem.class,
            CarbonClasses.class
        ),

        Css.useLogicalProperties(),

        Css.breakpoints("""
        sm: 20rem
        md: 40rem
        lg: 66rem
        xl: 82rem
        max: 99rem
        """),

        Css.baseLayer("""
        .theme-white {
          --cds-background: #ffffff;
          --cds-background-active: rgba(141, 141, 141, 0.5);
          --cds-background-brand: #0f62fe;
          --cds-background-hover: rgba(141, 141, 141, 0.12);
          --cds-background-inverse: #393939;
          --cds-background-inverse-hover: #474747;
          --cds-background-selected: rgba(141, 141, 141, 0.2);
          --cds-background-selected-hover: rgba(141, 141, 141, 0.32);
          --cds-border-disabled: #c6c6c6;
          --cds-border-interactive: #0f62fe;
          --cds-border-inverse: #161616;
          --cds-border-strong-01: #8d8d8d;
          --cds-border-strong-02: #8d8d8d;
          --cds-border-strong-03: #8d8d8d;
          --cds-border-subtle-00: #e0e0e0;
          --cds-border-subtle-01: #c6c6c6;
          --cds-border-subtle-02: #e0e0e0;
          --cds-border-subtle-03: #c6c6c6;
          --cds-border-subtle-selected-01: #c6c6c6;
          --cds-border-subtle-selected-02: #c6c6c6;
          --cds-border-subtle-selected-03: #c6c6c6;
          --cds-border-tile-01: #c6c6c6;
          --cds-border-tile-02: #a8a8a8;
          --cds-border-tile-03: #c6c6c6;
          --cds-button-separator: #e0e0e0;
          --cds-button-primary: #0f62fe;
          --cds-button-secondary: #393939;
          --cds-button-tertiary: #0f62fe;
          --cds-button-danger-primary: #da1e28;
          --cds-button-danger-secondary: #da1e28;
          --cds-button-danger-active: #750e13;
          --cds-button-primary-active: #002d9c;
          --cds-button-secondary-active: #6f6f6f;
          --cds-button-tertiary-active: #002d9c;
          --cds-button-danger-hover: #b81921;
          --cds-button-primary-hover: #0050e6;
          --cds-button-secondary-hover: #474747;
          --cds-button-tertiary-hover: #0050e6;
          --cds-button-disabled: #c6c6c6;
          --cds-focus: #0f62fe;
          --cds-focus-inset: #ffffff;
          --cds-focus-inverse: #ffffff;
          --cds-icon-disabled: rgba(22, 22, 22, 0.25);
          --cds-icon-interactive: #0f62fe;
          --cds-icon-inverse: #ffffff;
          --cds-icon-on-color: #ffffff;
          --cds-icon-on-color-disabled: #8d8d8d;
          --cds-icon-primary: #161616;
          --cds-icon-secondary: #525252;
          --cds-layer-01: #f4f4f4;
          --cds-layer-02: #ffffff;
          --cds-layer-03: #f4f4f4;
          --cds-layer-accent-01: #e0e0e0;
          --cds-layer-accent-02: #e0e0e0;
          --cds-layer-accent-03: #e0e0e0;
          --cds-layer-accent-active-01: #a8a8a8;
          --cds-layer-accent-active-02: #a8a8a8;
          --cds-layer-accent-active-03: #a8a8a8;
          --cds-layer-accent-hover-01: #d1d1d1;
          --cds-layer-accent-hover-02: #d1d1d1;
          --cds-layer-accent-hover-03: #d1d1d1;
          --cds-layer-active-01: #c6c6c6;
          --cds-layer-active-02: #c6c6c6;
          --cds-layer-active-03: #c6c6c6;
          --cds-layer-hover-01: #e8e8e8;
          --cds-layer-hover-02: #e8e8e8;
          --cds-layer-hover-03: #e8e8e8;
          --cds-layer-selected-01: #e0e0e0;
          --cds-layer-selected-02: #e0e0e0;
          --cds-layer-selected-03: #e0e0e0;
          --cds-layer-selected-disabled: #8d8d8d;
          --cds-layer-selected-hover-01: #d1d1d1;
          --cds-layer-selected-hover-02: #d1d1d1;
          --cds-layer-selected-hover-03: #d1d1d1;
          --cds-layer-selected-inverse: #161616;
          --cds-link-inverse: #78a9ff;
          --cds-link-inverse-active: #f4f4f4;
          --cds-link-inverse-hover: #a6c8ff;
          --cds-link-inverse-visited: #be95ff;
          --cds-link-primary: #0f62fe;
          --cds-link-primary-hover: #0043ce;
          --cds-link-secondary: #0043ce;
          --cds-link-visited: #8a3ffc;
          --cds-overlay: rgba(22, 22, 22, 0.5);
          --cds-shadow: rgba(0, 0, 0, 0.3);
          --cds-text-disabled: rgba(22, 22, 22, 0.25);
          --cds-text-error: #da1e28;
          --cds-text-helper: #6f6f6f;
          --cds-text-inverse: #ffffff;
          --cds-text-on-color: #ffffff;
          --cds-text-on-color-disabled: #8d8d8d;
          --cds-text-placeholder: rgba(22, 22, 22, 0.4);
          --cds-text-primary: #161616;
          --cds-text-secondary: #525252;

          background-color: var(--cds-background);
          color: var(--cds-text-primary);
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);
        }
        """),

        Css.baseLayer("""
        .theme-g10 {
          --cds-background: #f4f4f4;
          --cds-background-active: rgba(141, 141, 141, 0.5);
          --cds-background-brand: #0f62fe;
          --cds-background-hover: rgba(141, 141, 141, 0.12);
          --cds-background-inverse: #393939;
          --cds-background-inverse-hover: #474747;
          --cds-background-selected: rgba(141, 141, 141, 0.2);
          --cds-background-selected-hover: rgba(141, 141, 141, 0.32);
          --cds-border-disabled: #c6c6c6;
          --cds-border-interactive: #0f62fe;
          --cds-border-inverse: #161616;
          --cds-border-strong-01: #8d8d8d;
          --cds-border-strong-02: #8d8d8d;
          --cds-border-strong-03: #8d8d8d;
          --cds-border-subtle-00: #c6c6c6;
          --cds-border-subtle-01: #e0e0e0;
          --cds-border-subtle-02: #c6c6c6;
          --cds-border-subtle-03: #e0e0e0;
          --cds-border-subtle-selected-01: #c6c6c6;
          --cds-border-subtle-selected-02: #c6c6c6;
          --cds-border-subtle-selected-03: #c6c6c6;
          --cds-border-tile-01: #a8a8a8;
          --cds-border-tile-02: #c6c6c6;
          --cds-border-tile-03: #a8a8a8;
          --cds-button-separator: #e0e0e0;
          --cds-button-primary: #0f62fe;
          --cds-button-secondary: #393939;
          --cds-button-tertiary: #0f62fe;
          --cds-button-danger-primary: #da1e28;
          --cds-button-danger-secondary: #da1e28;
          --cds-button-danger-active: #750e13;
          --cds-button-primary-active: #002d9c;
          --cds-button-secondary-active: #6f6f6f;
          --cds-button-tertiary-active: #002d9c;
          --cds-button-danger-hover: #b81921;
          --cds-button-primary-hover: #0050e6;
          --cds-button-secondary-hover: #474747;
          --cds-button-tertiary-hover: #0050e6;
          --cds-button-disabled: #c6c6c6;
          --cds-focus: #0f62fe;
          --cds-focus-inset: #ffffff;
          --cds-focus-inverse: #ffffff;
          --cds-icon-disabled: rgba(22, 22, 22, 0.25);
          --cds-icon-interactive: #0f62fe;
          --cds-icon-inverse: #ffffff;
          --cds-icon-on-color: #ffffff;
          --cds-icon-on-color-disabled: #8d8d8d;
          --cds-icon-primary: #161616;
          --cds-icon-secondary: #525252;
          --cds-layer-01: #ffffff;
          --cds-layer-02: #f4f4f4;
          --cds-layer-03: #ffffff;
          --cds-layer-accent-01: #e0e0e0;
          --cds-layer-accent-02: #e0e0e0;
          --cds-layer-accent-03: #e0e0e0;
          --cds-layer-accent-active-01: #a8a8a8;
          --cds-layer-accent-active-02: #a8a8a8;
          --cds-layer-accent-active-03: #a8a8a8;
          --cds-layer-accent-hover-01: #d1d1d1;
          --cds-layer-accent-hover-02: #d1d1d1;
          --cds-layer-accent-hover-03: #d1d1d1;
          --cds-layer-active-01: #c6c6c6;
          --cds-layer-active-02: #c6c6c6;
          --cds-layer-active-03: #c6c6c6;
          --cds-layer-hover-01: #e8e8e8;
          --cds-layer-hover-02: #e8e8e8;
          --cds-layer-hover-03: #e8e8e8;
          --cds-layer-selected-01: #e0e0e0;
          --cds-layer-selected-02: #e0e0e0;
          --cds-layer-selected-03: #e0e0e0;
          --cds-layer-selected-disabled: #8d8d8d;
          --cds-layer-selected-hover-01: #d1d1d1;
          --cds-layer-selected-hover-02: #d1d1d1;
          --cds-layer-selected-hover-03: #d1d1d1;
          --cds-layer-selected-inverse: #161616;
          --cds-link-inverse: #78a9ff;
          --cds-link-inverse-active: #f4f4f4;
          --cds-link-inverse-hover: #a6c8ff;
          --cds-link-inverse-visited: #be95ff;
          --cds-link-primary: #0f62fe;
          --cds-link-primary-hover: #0043ce;
          --cds-link-secondary: #0043ce;
          --cds-link-visited: #8a3ffc;
          --cds-overlay: rgba(22, 22, 22, 0.5);
          --cds-shadow: rgba(0, 0, 0, 0.3);
          --cds-text-disabled: rgba(22, 22, 22, 0.25);
          --cds-text-error: #da1e28;
          --cds-text-helper: #6f6f6f;
          --cds-text-inverse: #ffffff;
          --cds-text-on-color: #ffffff;
          --cds-text-on-color-disabled: #8d8d8d;
          --cds-text-placeholder: rgba(22, 22, 22, 0.4);
          --cds-text-primary: #161616;
          --cds-text-secondary: #525252;

          background-color: var(--cds-background);
          color: var(--cds-text-primary);
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);
        }
        """),

        Css.baseLayer("""
        .theme-g90 {
          --cds-background: #262626;
          --cds-background-active: rgba(141, 141, 141, 0.4);
          --cds-background-brand: #0f62fe;
          --cds-background-hover: rgba(141, 141, 141, 0.16);
          --cds-background-inverse: #f4f4f4;
          --cds-background-inverse-hover: #e8e8e8;
          --cds-background-selected: rgba(141, 141, 141, 0.24);
          --cds-background-selected-hover: rgba(141, 141, 141, 0.32);
          --cds-border-disabled: rgba(141, 141, 141, 0.5);
          --cds-border-interactive: #4589ff;
          --cds-border-inverse: #f4f4f4;
          --cds-border-strong-01: #8d8d8d;
          --cds-border-strong-02: #a8a8a8;
          --cds-border-strong-03: #c6c6c6;
          --cds-border-subtle-00: #525252;
          --cds-border-subtle-01: #6f6f6f;
          --cds-border-subtle-02: #8d8d8d;
          --cds-border-subtle-03: #8d8d8d;
          --cds-border-subtle-selected-01: #8d8d8d;
          --cds-border-subtle-selected-02: #a8a8a8;
          --cds-border-subtle-selected-03: #a8a8a8;
          --cds-border-tile-01: #6f6f6f;
          --cds-border-tile-02: #8d8d8d;
          --cds-border-tile-03: #a8a8a8;
          --cds-button-separator: #161616;
          --cds-button-primary: #0f62fe;
          --cds-button-secondary: #6f6f6f;
          --cds-button-tertiary: #ffffff;
          --cds-button-danger-primary: #da1e28;
          --cds-button-danger-secondary: #ff8389;
          --cds-button-danger-active: #750e13;
          --cds-button-primary-active: #002d9c;
          --cds-button-secondary-active: #393939;
          --cds-button-tertiary-active: #c6c6c6;
          --cds-button-danger-hover: #b81921;
          --cds-button-primary-hover: #0050e6;
          --cds-button-secondary-hover: #5e5e5e;
          --cds-button-tertiary-hover: #f4f4f4;
          --cds-button-disabled: rgba(141, 141, 141, 0.3);
          --cds-focus: #ffffff;
          --cds-focus-inset: #161616;
          --cds-focus-inverse: #0f62fe;
          --cds-icon-disabled: rgba(244, 244, 244, 0.25);
          --cds-icon-interactive: #ffffff;
          --cds-icon-inverse: #161616;
          --cds-icon-on-color: #ffffff;
          --cds-icon-on-color-disabled: rgba(255, 255, 255, 0.25);
          --cds-icon-primary: #f4f4f4;
          --cds-icon-secondary: #c6c6c6;
          --cds-layer-01: #393939;
          --cds-layer-02: #525252;
          --cds-layer-03: #6f6f6f;
          --cds-layer-accent-01: #525252;
          --cds-layer-accent-02: #6f6f6f;
          --cds-layer-accent-03: #8d8d8d;
          --cds-layer-accent-active-01: #8d8d8d;
          --cds-layer-accent-active-02: #393939;
          --cds-layer-accent-active-03: #525252;
          --cds-layer-accent-hover-01: #636363;
          --cds-layer-accent-hover-02: #5e5e5e;
          --cds-layer-accent-hover-03: #7a7a7a;
          --cds-layer-active-01: #6f6f6f;
          --cds-layer-active-02: #8d8d8d;
          --cds-layer-active-03: #393939;
          --cds-layer-hover-01: #474747;
          --cds-layer-hover-02: #636363;
          --cds-layer-hover-03: #5e5e5e;
          --cds-layer-selected-01: #525252;
          --cds-layer-selected-02: #6f6f6f;
          --cds-layer-selected-03: #525252;
          --cds-layer-selected-disabled: #a8a8a8;
          --cds-layer-selected-hover-01: #636363;
          --cds-layer-selected-hover-02: #5e5e5e;
          --cds-layer-selected-hover-03: #636363;
          --cds-layer-selected-inverse: #f4f4f4;
          --cds-link-inverse: #0f62fe;
          --cds-link-inverse-active: #161616;
          --cds-link-inverse-hover: #0043ce;
          --cds-link-inverse-visited: #8a3ffc;
          --cds-link-primary: #78a9ff;
          --cds-link-primary-hover: #a6c8ff;
          --cds-link-secondary: #a6c8ff;
          --cds-link-visited: #be95ff;
          --cds-overlay: rgba(0, 0, 0, 0.65);
          --cds-shadow: rgba(0, 0, 0, 0.8);
          --cds-text-disabled: rgba(244, 244, 244, 0.25);
          --cds-text-error: #ffb3b8;
          --cds-text-helper: #c6c6c6;
          --cds-text-inverse: #161616;
          --cds-text-on-color: #ffffff;
          --cds-text-on-color-disabled: rgba(255, 255, 255, 0.25);
          --cds-text-placeholder: rgba(244, 244, 244, 0.4);
          --cds-text-primary: #f4f4f4;
          --cds-text-secondary: #c6c6c6;

          background-color: var(--cds-background);
          color: var(--cds-text-primary);
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);
        }
        """),

        Css.baseLayer("""
        .theme-g100 {
          --cds-background: #161616;
          --cds-background-active: rgba(141, 141, 141, 0.4);
          --cds-background-brand: #0f62fe;
          --cds-background-hover: rgba(141, 141, 141, 0.16);
          --cds-background-inverse: #f4f4f4;
          --cds-background-inverse-hover: #e8e8e8;
          --cds-background-selected: rgba(141, 141, 141, 0.24);
          --cds-background-selected-hover: rgba(141, 141, 141, 0.32);
          --cds-border-disabled: rgba(141, 141, 141, 0.5);
          --cds-border-interactive: #4589ff;
          --cds-border-inverse: #f4f4f4;
          --cds-border-strong-01: #6f6f6f;
          --cds-border-strong-02: #8d8d8d;
          --cds-border-strong-03: #a8a8a8;
          --cds-border-subtle-00: #393939;
          --cds-border-subtle-01: #525252;
          --cds-border-subtle-02: #6f6f6f;
          --cds-border-subtle-03: #6f6f6f;
          --cds-border-subtle-selected-01: #6f6f6f;
          --cds-border-subtle-selected-02: #8d8d8d;
          --cds-border-subtle-selected-03: #8d8d8d;
          --cds-border-tile-01: #525252;
          --cds-border-tile-02: #6f6f6f;
          --cds-border-tile-03: #8d8d8d;
          --cds-button-separator: #161616;
          --cds-button-primary: #0f62fe;
          --cds-button-secondary: #6f6f6f;
          --cds-button-tertiary: #ffffff;
          --cds-button-danger-primary: #da1e28;
          --cds-button-danger-secondary: #fa4d56;
          --cds-button-danger-active: #750e13;
          --cds-button-primary-active: #002d9c;
          --cds-button-secondary-active: #393939;
          --cds-button-tertiary-active: #c6c6c6;
          --cds-button-danger-hover: #b81921;
          --cds-button-primary-hover: #0050e6;
          --cds-button-secondary-hover: #5e5e5e;
          --cds-button-tertiary-hover: #f4f4f4;
          --cds-button-disabled: rgba(141, 141, 141, 0.3);
          --cds-focus: #ffffff;
          --cds-focus-inset: #161616;
          --cds-focus-inverse: #0f62fe;
          --cds-icon-disabled: rgba(244, 244, 244, 0.25);
          --cds-icon-interactive: #ffffff;
          --cds-icon-inverse: #161616;
          --cds-icon-on-color: #ffffff;
          --cds-icon-on-color-disabled: rgba(255, 255, 255, 0.25);
          --cds-icon-primary: #f4f4f4;
          --cds-icon-secondary: #c6c6c6;
          --cds-layer-01: #262626;
          --cds-layer-02: #393939;
          --cds-layer-03: #525252;
          --cds-layer-accent-01: #393939;
          --cds-layer-accent-02: #525252;
          --cds-layer-accent-03: #6f6f6f;
          --cds-layer-accent-active-01: #6f6f6f;
          --cds-layer-accent-active-02: #8d8d8d;
          --cds-layer-accent-active-03: #393939;
          --cds-layer-accent-hover-01: #474747;
          --cds-layer-accent-hover-02: #636363;
          --cds-layer-accent-hover-03: #5e5e5e;
          --cds-layer-active-01: #525252;
          --cds-layer-active-02: #6f6f6f;
          --cds-layer-active-03: #8d8d8d;
          --cds-layer-hover-01: #333333;
          --cds-layer-hover-02: #474747;
          --cds-layer-hover-03: #636363;
          --cds-layer-selected-01: #393939;
          --cds-layer-selected-02: #525252;
          --cds-layer-selected-03: #6f6f6f;
          --cds-layer-selected-disabled: #a8a8a8;
          --cds-layer-selected-hover-01: #474747;
          --cds-layer-selected-hover-02: #636363;
          --cds-layer-selected-hover-03: #5e5e5e;
          --cds-layer-selected-inverse: #f4f4f4;
          --cds-link-inverse: #0f62fe;
          --cds-link-inverse-active: #161616;
          --cds-link-inverse-hover: #0043ce;
          --cds-link-inverse-visited: #8a3ffc;
          --cds-link-primary: #78a9ff;
          --cds-link-primary-hover: #a6c8ff;
          --cds-link-secondary: #a6c8ff;
          --cds-link-visited: #be95ff;
          --cds-overlay: rgba(0, 0, 0, 0.65);
          --cds-shadow: rgba(0, 0, 0, 0.8);
          --cds-text-disabled: rgba(244, 244, 244, 0.25);
          --cds-text-error: #ff8389;
          --cds-text-helper: #a8a8a8;
          --cds-text-inverse: #161616;
          --cds-text-on-color: #ffffff;
          --cds-text-on-color-disabled: rgba(255, 255, 255, 0.25);
          --cds-text-placeholder: rgba(244, 244, 244, 0.4);
          --cds-text-primary: #f4f4f4;
          --cds-text-secondary: #c6c6c6;

          background-color: var(--cds-background);
          color: var(--cds-text-primary);
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);
        }
        """),

        Css.baseLayer("""
        :root {
          --default-font-family: var(--font-family-sans);
          --default-mono-font-family: var(--font-family-mono);

          --font-family-sans: "IBM Plex Sans", ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
          --font-family-serif: ui-serif, Georgia, Cambria, "Times New Roman", Times, serif;
          --font-family-mono: "IBM Plex Mono", ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;

          --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);
          --cds-button-separator: #e0e0e0;
          --cds-button-primary: #0f62fe;
          --cds-button-secondary: #393939;
          --cds-button-tertiary: #0f62fe;
          --cds-button-danger-primary: #da1e28;
          --cds-button-danger-secondary: #da1e28;
          --cds-button-danger-active: #750e13;
          --cds-button-primary-active: #002d9c;
          --cds-button-secondary-active: #6f6f6f;
          --cds-button-tertiary-active: #002d9c;
          --cds-button-danger-hover: #b81921;
          --cds-button-primary-hover: #0050e6;
          --cds-button-secondary-hover: #474747;
          --cds-button-tertiary-hover: #0050e6;
          --cds-button-disabled: #c6c6c6;
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-layer-active: var(--cds-layer-active-01, #c6c6c6);
          --cds-layer-hover: var(--cds-layer-hover-01, #e8e8e8);
          --cds-layer-selected: var(--cds-layer-selected-01, #e0e0e0);
          --cds-layer-selected-hover: var(--cds-layer-selected-hover-01, #d1d1d1);
          --cds-layer-accent: var(--cds-layer-accent-01, #e0e0e0);
          --cds-layer-accent-hover: var(--cds-layer-accent-hover-01, #d1d1d1);
          --cds-layer-accent-active: var(--cds-layer-accent-active-01, #a8a8a8);
          --cds-link-inverse: #78a9ff;
          --cds-link-inverse-active: #f4f4f4;
          --cds-link-inverse-hover: #a6c8ff;
          --cds-link-inverse-visited: #be95ff;
          --cds-link-primary: #0f62fe;
          --cds-link-primary-hover: #0043ce;
          --cds-link-secondary: #0043ce;
          --cds-link-visited: #8a3ffc;
          --cds-text-disabled: rgba(22, 22, 22, 0.25);
          --cds-text-error: #da1e28;
          --cds-text-helper: #6f6f6f;
          --cds-text-inverse: #ffffff;
          --cds-text-on-color: #ffffff;
          --cds-text-on-color-disabled: #8d8d8d;
          --cds-text-placeholder: rgba(22, 22, 22, 0.4);
          --cds-text-primary: #161616;
          --cds-text-secondary: #525252;

          --spacing-header: 3rem;
          --spacing-side-nav: 16rem;

          --cds-spacing-01: 0.125rem;
          --cds-spacing-02: 0.25rem;
          --cds-spacing-03: 0.5rem;
          --cds-spacing-04: 0.75rem;
          --cds-spacing-05: 1rem;
          --cds-spacing-06: 1.5rem;
          --cds-spacing-07: 2rem;
          --cds-spacing-08: 2.5rem;
          --cds-spacing-09: 3rem;
          --cds-spacing-10: 4rem;
          --cds-spacing-11: 5rem;
          --cds-spacing-12: 6rem;
          --cds-spacing-13: 10rem;
        }
        """),

        Css.baseLayer("""
        .cds--layer-one {
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-layer-active: var(--cds-layer-active-01, #c6c6c6);
          --cds-layer-hover: var(--cds-layer-hover-01, #e8e8e8);
          --cds-layer-selected: var(--cds-layer-selected-01, #e0e0e0);
          --cds-layer-selected-hover: var(--cds-layer-selected-hover-01, #d1d1d1);
          --cds-layer-accent: var(--cds-layer-accent-01, #e0e0e0);
          --cds-layer-accent-hover: var(--cds-layer-accent-hover-01, #d1d1d1);
          --cds-layer-accent-active: var(--cds-layer-accent-active-01, #a8a8a8);
          --cds-field: var(--cds-field-01, #f4f4f4);
          --cds-field-hover: var(--cds-field-hover-01, #e8e8e8);
          --cds-border-subtle: var(--cds-border-subtle-00, #e0e0e0);
          --cds-border-subtle-selected: var(--cds-border-subtle-selected-01, #c6c6c6);
          --cds-border-strong: var(--cds-border-strong-01, #8d8d8d);
          --cds-border-tile: var(--cds-border-tile-01, #c6c6c6)
        }

        .cds--layer-two {
          --cds-layer: var(--cds-layer-02, #ffffff);
          --cds-layer-active: var(--cds-layer-active-02, #c6c6c6);
          --cds-layer-hover: var(--cds-layer-hover-02, #e8e8e8);
          --cds-layer-selected: var(--cds-layer-selected-02, #e0e0e0);
          --cds-layer-selected-hover: var(--cds-layer-selected-hover-02, #d1d1d1);
          --cds-layer-accent: var(--cds-layer-accent-02, #e0e0e0);
          --cds-layer-accent-hover: var(--cds-layer-accent-hover-02, #d1d1d1);
          --cds-layer-accent-active: var(--cds-layer-accent-active-02, #a8a8a8);
          --cds-field: var(--cds-field-02, #ffffff);
          --cds-field-hover: var(--cds-field-hover-02, #e8e8e8);
          --cds-border-subtle: var(--cds-border-subtle-01, #c6c6c6);
          --cds-border-subtle-selected: var(--cds-border-subtle-selected-02, #c6c6c6);
          --cds-border-strong: var(--cds-border-strong-02, #8d8d8d);
          --cds-border-tile: var(--cds-border-tile-02, #a8a8a8)
        }

        .cds--layer-three {
          --cds-layer: var(--cds-layer-03, #f4f4f4);
          --cds-layer-active: var(--cds-layer-active-03, #c6c6c6);
          --cds-layer-hover: var(--cds-layer-hover-03, #e8e8e8);
          --cds-layer-selected: var(--cds-layer-selected-03, #e0e0e0);
          --cds-layer-selected-hover: var(--cds-layer-selected-hover-03, #d1d1d1);
          --cds-layer-accent: var(--cds-layer-accent-03, #e0e0e0);
          --cds-layer-accent-hover: var(--cds-layer-accent-hover-03, #d1d1d1);
          --cds-layer-accent-active: var(--cds-layer-accent-active-03, #a8a8a8);
          --cds-field: var(--cds-field-03, #f4f4f4);
          --cds-field-hover: var(--cds-field-hover-03, #e8e8e8);
          --cds-border-subtle: var(--cds-border-subtle-02, #e0e0e0);
          --cds-border-subtle-selected: var(--cds-border-subtle-selected-03, #c6c6c6);
          --cds-border-strong: var(--cds-border-strong-03, #8d8d8d);
          --cds-border-tile: var(--cds-border-tile-03, #c6c6c6)
        }
        """),

        componentsButton(),

        componentsDataTable(),

        componentsGrid(),

        componentsLink(),

        componentsModal(),

        componentsOverlay(),

        componentsPageHeader(),

        componentsTearsheet(),

        componentsTile(),

        componentsTypography(),

        extendColors(),

        extendSpacing(),

        extendZIndex(),

        Css.overrideBorderWidth("""
        : 1px
        0: 0px
        2: 2px
        3: 3px
        """),

        Css.overrideContent("""
        none: none
        empty: ""
        """),

        Css.overrideFontSize("""
        12px: 0.75rem
        14px: 0.875rem
        16px: 1rem
        20px: 1.25rem
        24px: 1.5rem
        28px: 1.75rem
        32px: 2rem
        36px: 2.25rem
        42px: 2.625rem
        48px: 3rem
        54px: 3.375rem
        60px: 3.75rem
        68px: 4.25rem
        76px: 4.75rem
        84px: 5.25rem
        92px: 5.75rem
        122px: 7.625rem
        156px: 9.75rem
        """),

        Css.overrideLineHeight("""
        16px: 1rem
        18px: 1.125rem
        20px: 1.25rem
        22px: 1.375rem
        28px: 1.75rem
        32px: 2rem
        36px: 2.25rem
        40px: 2.5rem
        44px: 2.75rem
        50px: 3.125rem
        56px: 3.5rem
        64px: 4rem
        70px: 4.375rem
        78px: 4.875rem
        86px: 5.375rem
        94px: 5.875rem
        102px: 6.375rem
        130px: 8.125rem
        164px: 10.25rem
        """),

        Css.overrideFontWeight("""
        300: 300
        400: 400
        600: 600
        """),

        Css.overrideLetterSpacing("""
        0px: 0px
        0.1px: 0.1px
        0.16px: 0.16px
        0.32px: 0.32px
        0.64px: 0.64px
        0.96px: 0.96px
        """),

        Css.variants("""
        h1: & h1
        h2: & h2
        h3: & h3
        h4: & h4
        h5: & h5
        h6: & h6
        more: &:not(:root)
        span: & span
        svg: & > svg
        tbody: & tbody
        td: & td
        th: & th
        thead: & thead
        tr: & tr
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

  private Css.Option componentsButton() {
    return Css.components("""
    # button-reset
    cursor-pointer appearance-none

    # __button-base
    relative m-0px inline-flex shrink-0
    cursor-pointer appearance-none
    text-start align-top body-compact-01
    outline-0
    transition-all duration-100
    focus:border-focus
    focus:shadow-[inset_0_0_0_1px_var(--cds-focus),inset_0_0_0_2px_var(--cds-background)]

    # __button-justify-standard
    justify-between

    # __button-justify-icon-only
    justify-center

    # __button-padding-standard
    pr-[63px] pl-[15px]

    # __button-padding-ghost
    pr-[15px] pl-[15px]

    # __button-size-sm
    w-max max-w-320px min-h-32px py-6px

    # __button-size-md
    w-max max-w-320px min-h-40px py-10px

    # __button-size-lg
    w-max max-w-320px min-h-48px py-14px

    # __button-size-xl
    w-max max-w-320px min-h-64px py-14px

    # __button-size-2xl
    w-max max-w-320px min-h-80px py-14px

    # __button-has-icon-base
    svg:w-16px svg:h-16px svg:shrink-0

    # __button-has-icon-standard
    __button-has-icon-base
    svg:absolute svg:right-16px svg:mt-1px

    # __button-has-icon-ghost
    __button-has-icon-base
    svg:static svg:ml-8px

    # __button-has-icon-position-sm
    svg:top-6px

    # __button-has-icon-position-md
    svg:top-10px

    # __button-has-icon-position-lg
    svg:top-14px

    # __button-has-icon-position-xl
    svg:top-14px

    # __button-has-icon-position-2xl
    svg:top-14px

    # __button-primary
    bg-button-primary
    border border-transparent
    text-text-on-color
    active:bg-button-primary-active
    hover:bg-button-primary-hover

    # __button-secondary
    bg-button-secondary
    border border-transparent
    text-text-on-color
    active:bg-button-secondary-active
    hover:bg-button-secondary-hover

    # __button-tertiary
    bg-transparent
    border border-button-tertiary
    text-button-tertiary
    active:bg-button-tertiary-active
    hover:bg-button-tertiary-hover hover:text-text-inverse

    # __button-ghost
    bg-transparent
    border border-transparent
    text-link-primary
    active:bg-background-active active:text-link-primary-hover
    hover:bg-background-hover hover:text-link-primary-hover

    # __button-danger
    bg-button-danger
    border border-transparent
    text-text-on-color
    active:bg-button-danger-active
    hover:bg-button-danger-hover

    # __button-danger-tertiary
    bg-transparent
    border border-button-danger-secondary
    text-button-danger-secondary
    active:bg-button-danger-active active:border-button-danger-active active:text-text-on-color
    focus:bg-button-danger focus:text-text-on-color
    hover:bg-button-danger-hover hover:text-text-on-color

    # __button-danger-ghost
    bg-transparent
    border border-transparent
    text-button-danger-secondary
    active:bg-button-danger-active active:text-text-on-color
    hover:bg-button-danger-hover hover:text-text-on-color

    # __button-icon-size-sm
    size-32px pt-[7px]

    # __button-icon-size-md
    size-40px pt-[11px]

    # __button-icon-size-lg
    size-48px pt-14px

    # __button-icon-size-xl
    size-64px pt-14px

    # __button-icon-size-2xl
    size-80px pt-14px

    # __button-standard
    __button-base
    __button-justify-standard
    __button-padding-standard
    __button-has-icon-standard

    # __button-primary-base
    __button-standard __button-primary

    # button-primary
    __button-primary-base __button-size-lg  __button-has-icon-offset-lg
    # button-primary-sm
    __button-primary-base __button-size-sm  __button-has-icon-offset-sm
    # button-primary-md
    __button-primary-base __button-size-md  __button-has-icon-offset-md
    # button-primary-lg
    __button-primary-base __button-size-lg  __button-has-icon-offset-lg
    # button-primary-xl
    __button-primary-base __button-size-xl  __button-has-icon-offset-xl
    # button-primary-2xl
    __button-primary-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-secondary-base
    __button-standard __button-secondary

    # button-secondary
    __button-secondary-base __button-size-lg  __button-has-icon-offset-lg
    # button-secondary-sm
    __button-secondary-base __button-size-sm  __button-has-icon-offset-sm
    # button-secondary-md
    __button-secondary-base __button-size-md  __button-has-icon-offset-md
    # button-secondary-lg
    __button-secondary-base __button-size-lg  __button-has-icon-offset-lg
    # button-secondary-xl
    __button-secondary-base __button-size-xl  __button-has-icon-offset-xl
    # button-secondary-2xl
    __button-secondary-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-tertiary-base
    __button-standard __button-tertiary

    # button-tertiary
    __button-tertiary-base __button-size-lg  __button-has-icon-offset-lg
    # button-tertiary-sm
    __button-tertiary-base __button-size-sm  __button-has-icon-offset-sm
    # button-tertiary-md
    __button-tertiary-base __button-size-md  __button-has-icon-offset-md
    # button-tertiary-lg
    __button-tertiary-base __button-size-lg  __button-has-icon-offset-lg
    # button-tertiary-xl
    __button-tertiary-base __button-size-xl  __button-has-icon-offset-xl
    # button-tertiary-2xl
    __button-tertiary-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-ghost-base
    __button-base
    __button-justify-standard
    __button-padding-ghost
    __button-has-icon-ghost
    __button-ghost

    # button-ghost
    __button-ghost-base __button-size-lg  __button-has-icon-offset-lg
    # button-ghost-sm
    __button-ghost-base __button-size-sm  __button-has-icon-offset-sm
    # button-ghost-md
    __button-ghost-base __button-size-md  __button-has-icon-offset-md
    # button-ghost-lg
    __button-ghost-base __button-size-lg  __button-has-icon-offset-lg
    # button-ghost-xl
    __button-ghost-base __button-size-xl  __button-has-icon-offset-xl
    # button-ghost-2xl
    __button-ghost-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-danger-base
    __button-standard __button-danger

    # button-danger
    __button-danger-base __button-size-lg  __button-has-icon-offset-lg
    # button-danger-sm
    __button-danger-base __button-size-sm  __button-has-icon-offset-sm
    # button-danger-md
    __button-danger-base __button-size-md  __button-has-icon-offset-md
    # button-danger-lg
    __button-danger-base __button-size-lg  __button-has-icon-offset-lg
    # button-danger-xl
    __button-danger-base __button-size-xl  __button-has-icon-offset-xl
    # button-danger-2xl
    __button-danger-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-danger-tertiary-base
    __button-standard __button-danger-tertiary

    # button-danger-tertiary
    __button-danger-tertiary-base __button-size-lg  __button-has-icon-offset-lg
    # button-danger-tertiary-sm
    __button-danger-tertiary-base __button-size-sm  __button-has-icon-offset-sm
    # button-danger-tertiary-md
    __button-danger-tertiary-base __button-size-md  __button-has-icon-offset-md
    # button-danger-tertiary-lg
    __button-danger-tertiary-base __button-size-lg  __button-has-icon-offset-lg
    # button-danger-tertiary-xl
    __button-danger-tertiary-base __button-size-xl  __button-has-icon-offset-xl
    # button-danger-tertiary-2xl
    __button-danger-tertiary-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-danger-ghost-base
    __button-base
    __button-justify-standard
    __button-padding-ghost
    __button-has-icon-ghost
    __button-danger-ghost

    # button-danger-ghost
    __button-danger-ghost-base __button-size-lg  __button-has-icon-offset-lg
    # button-danger-ghost-sm
    __button-danger-ghost-base __button-size-sm  __button-has-icon-offset-sm
    # button-danger-ghost-md
    __button-danger-ghost-base __button-size-md  __button-has-icon-offset-md
    # button-danger-ghost-lg
    __button-danger-ghost-base __button-size-lg  __button-has-icon-offset-lg
    # button-danger-ghost-xl
    __button-danger-ghost-base __button-size-xl  __button-has-icon-offset-xl
    # button-danger-ghost-2xl
    __button-danger-ghost-base __button-size-2xl  __button-has-icon-offset-2xl

    # __button-icon-base
    __button-base
    __button-justify-icon-only

    # __button-icon-primary-base
    __button-icon-base
    __button-primary

    # button-icon-primary
    __button-icon-primary-base __button-icon-size-lg
    # button-icon-primary-sm
    __button-icon-primary-base __button-icon-size-sm
    # button-icon-primary-md
    __button-icon-primary-base __button-icon-size-md
    # button-icon-primary-lg
    __button-icon-primary-base __button-icon-size-lg
    # button-icon-primary-xl
    __button-icon-primary-base __button-icon-size-xl
    # button-icon-primary-2xl
    __button-icon-primary-base __button-icon-size-2xl

    # __button-icon-secondary-base
    __button-icon-base
    __button-secondary

    # button-icon-secondary
    __button-icon-secondary-base __button-icon-size-lg
    # button-icon-secondary-sm
    __button-icon-secondary-base __button-icon-size-sm
    # button-icon-secondary-md
    __button-icon-secondary-base __button-icon-size-md
    # button-icon-secondary-lg
    __button-icon-secondary-base __button-icon-size-lg
    # button-icon-secondary-xl
    __button-icon-secondary-base __button-icon-size-xl
    # button-icon-secondary-2xl
    __button-icon-secondary-base __button-icon-size-2xl

    # __button-icon-tertiary-base
    __button-icon-base
    __button-tertiary

    # button-icon-tertiary
    __button-icon-tertiary-base __button-icon-size-lg
    # button-icon-tertiary-sm
    __button-icon-tertiary-base __button-icon-size-sm
    # button-icon-tertiary-md
    __button-icon-tertiary-base __button-icon-size-md
    # button-icon-tertiary-lg
    __button-icon-tertiary-base __button-icon-size-lg
    # button-icon-tertiary-xl
    __button-icon-tertiary-base __button-icon-size-xl
    # button-icon-tertiary-2xl
    __button-icon-tertiary-base __button-icon-size-2xl

    # __button-icon-ghost-base
    __button-icon-base
    __button-ghost
    svg:fill-icon-primary

    # button-icon-ghost
    __button-icon-ghost-base __button-icon-size-lg
    # button-icon-ghost-sm
    __button-icon-ghost-base __button-icon-size-sm
    # button-icon-ghost-md
    __button-icon-ghost-base __button-icon-size-md
    # button-icon-ghost-lg
    __button-icon-ghost-base __button-icon-size-lg
    # button-icon-ghost-xl
    __button-icon-ghost-base __button-icon-size-xl
    # button-icon-ghost-2xl
    __button-icon-ghost-base __button-icon-size-2xl

    # __button-icon-danger-base
    __button-icon-base
    __button-danger

    # button-icon-danger
    __button-icon-danger-base __button-icon-size-lg
    # button-icon-danger-sm
    __button-icon-danger-base __button-icon-size-sm
    # button-icon-danger-md
    __button-icon-danger-base __button-icon-size-md
    # button-icon-danger-lg
    __button-icon-danger-base __button-icon-size-lg
    # button-icon-danger-xl
    __button-icon-danger-base __button-icon-size-xl
    # button-icon-danger-2xl
    __button-icon-danger-base __button-icon-size-2xl

    # __button-icon-danger-tertiary-base
    __button-icon-base
    __button-danger-tertiary

    # button-icon-danger-tertiary
    __button-icon-danger-tertiary-base __button-icon-size-lg
    # button-icon-danger-tertiary-sm
    __button-icon-danger-tertiary-base __button-icon-size-sm
    # button-icon-danger-tertiary-md
    __button-icon-danger-tertiary-base __button-icon-size-md
    # button-icon-danger-tertiary-lg
    __button-icon-danger-tertiary-base __button-icon-size-lg
    # button-icon-danger-tertiary-xl
    __button-icon-danger-tertiary-base __button-icon-size-xl
    # button-icon-danger-tertiary-2xl
    __button-icon-danger-tertiary-base __button-icon-size-2xl

    # __button-icon-danger-ghost-base
    __button-icon-base
    __button-danger-ghost

    # button-icon-danger-ghost
    __button-icon-danger-ghost-base __button-icon-size-lg
    # button-icon-danger-ghost-sm
    __button-icon-danger-ghost-base __button-icon-size-sm
    # button-icon-danger-ghost-md
    __button-icon-danger-ghost-base __button-icon-size-md
    # button-icon-danger-ghost-lg
    __button-icon-danger-ghost-base __button-icon-size-lg
    # button-icon-danger-ghost-xl
    __button-icon-danger-ghost-base __button-icon-size-xl
    # button-icon-danger-ghost-2xl
    __button-icon-danger-ghost-base __button-icon-size-2xl
    """);
  }

  private Css.Option componentsGrid() {
    return Css.components("""
    # __grid
    mx-auto grid w-full max-w-screen-max
    md:px-16px
    max:px-24px

    # grid-wide
    __grid
    *:mx-16px

    # grid-narrow
    __grid
    *:mr-16px

    # grid-condensed
    __grid
    *:mx-0.5px
    """);
  }

  private Css.Option componentsDataTable() {
    return Css.components("""
    # data-table-container
    relative
    pt-spacing-01

    # data-table-header
    bg-layer
    pt-spacing-05 px-spacing-05 pb-spacing-06

    # data-table-header-title
    heading-03
    text-text-primary

    # data-table-header-description
    body-compact-01
    text-text-secondary

    md:max-w-[50ch]

    lg:max-w-[80ch]

    # data-table-content
    block overflow-x-auto
    outline outline-2 -outline-offset-2 outline-transparent
    focus:outline-focus

    # __data-table-base
    w-full border-collapse border-spacing-0

    thead:bg-layer-accent
    thead:text-14px thead:leading-18px thead:font-600 thead:tracking-0.16px

    th:bg-layer-accent
    th:px-16px
    th:text-start th:align-middle th:text-text-primary

    tbody:w-full
    tbody:bg-layer
    tbody:text-14px tbody:leading-18px tbody:font-400 tbody:tracking-0.16px

    tr:w-full tr:h-48px tr:border-none

    tbody:tr:transition-colors tbody:tr:duration-75
    tbody:tr:hover:bg-layer-hover

    td:border-solid
    td:border-t td:border-t-layer
    td:border-b td:border-b-border-subtle
    td:px-16px
    td:text-start td:align-middle td:text-text-secondary

    # data-table
    __data-table-base
    tr:h-48px

    # data-table-xs
    __data-table-base
    tr:h-24px
    th:py-2px
    td:py-2px

    # data-table-sm
    __data-table-base
    tr:h-32px
    th:py-[7px]
    td:py-[7px]

    # data-table-md
    __data-table-base
    tr:h-40px
    th:pt-[6px] th:pb-[7px]
    td:pt-[6px] td:pb-[7px]

    # data-table-lg
    __data-table-base
    tr:h-48px

    # data-table-xl
    __data-table-base
    tr:h-64px
    th:py-spacing-05 th:align-top
    td:py-spacing-05 td:align-top
    """);
  }

  private Css.Option componentsLink() {
    return Css.components("""
    # __link
    inline-flex
    text-link-primary outline-none
    transition-colors duration-100
    active:underline active:outline active:outline-1 active:outline-focus active:outline-offset-0
    focus:outline focus:outline-1 focus:outline-focus focus:outline-offset-0
    hover:text-link-primary-hover hover:underline

    # link
    __link
    no-underline

    # link-inline
    __link
    underline

    # link-visited
    visited:text-link-visited
    visited:hover:text-link-primary-hover
    """);
  }

  private Css.Option componentsModal() {
    return Css.components("""
    # __modal-container-base
    fixed w-full h-full max-h-full
    grid-cols-[100%] grid-rows-[100%]
    bg-layer
    outline outline-3 -outline-offset-3 outline-transparent
    """);
  }

  private Css.Option componentsOverlay() {
    return Css.components("""
    # __overlay
    fixed inset-0px z-overlay
    bg-overlay
    transition-opacity duration-300
    lg:hidden

    # overlay
    __overlay hidden opacity-0

    # overlay-toggle
    __overlay block opacity-100
    """);
  }

  private Css.Option componentsPageHeader() {
    return Css.components("""
    # page-header
    sticky bg-layer
    shadow-[0_1px_0_var(--cds-layer-accent)]

    # page-header-actions
    col-span-full
    md:flex md:col-span-2 md:justify-end

    # page-header-title
    col-span-full
    heading-04
    md:col-span-3

    # page-header-title-only
    py-32px

    # page-header-title-row
    grid-wide grid-cols-5 gap-y-16px
    """);
  }

  private Css.Option componentsTearsheet() {
    return Css.components("""
    # __tearsheet
    fixed inset-0px z-tearsheet
    bg-overlay
    transition-opacity duration-300

    # tearsheet
    __tearsheet
    block
    opacity-0
    *:translate-y-3/4

    # tearsheet-hidden
    __tearsheet
    hidden
    opacity-0
    *:translate-y-3/4

    # tearsheet-transition
    opacity-100
    *:translate-y-0

    # tearsheet-container
    absolute flex flex-col inset-0px
    bg-layer
    outline outline-3 -outline-offset-3 outline-transparent
    transition-transform duration-300

    md:top-48px md:left-64px md:right-64px

    # tearsheet-header
    flex-none
    border-b border-b-border-subtle
    py-spacing-06 px-spacing-07

    # tearsheet-header-title
    heading-04
    text-text-primary

    # tearsheet-header-description
    mt-spacing-05
    overflow-hidden
    body-compact-01

    md:max-w-[60%]

    # tearsheet-body
    flex grow

    # tearsheet-influencer
    flex-[0_0_257px]
    border-r border-r-border-subtle
    overflow-y-auto
    """);
  }

  private Css.Option componentsTile() {
    return Css.components("""
    # tile
    relative block min-w-128px min-h-64px
    bg-layer
    p-16px
    outline outline-2 -outline-offset-2 outline-transparent
    """);
  }

  private Css.Option componentsTypography() {
    return Css.components("""
    # code-01
    font-mono
    text-12px leading-16px font-400 tracking-0.32px

    # code-02
    font-mono
    text-14px leading-20px font-400 tracking-0.32px

    # body-compact-01
    text-14px leading-18px font-400 tracking-0.16px

    # body-compact-02
    text-16px leading-22px font-400 tracking-0px

    # body-01
    text-14px leading-20px font-400 tracking-0.16px

    # body-02
    text-16px leading-24px font-400 tracking-0px

    # heading-compact-01
    text-14px leading-18px font-600 tracking-0.16px

    # heading-compact-02
    text-16px leading-22px font-600 tracking-0px

    # heading-01
    text-14px leading-20px font-600 tracking-0.16px

    # heading-02
    text-16px leading-24px font-600 tracking-0px

    # heading-03
    text-20px leading-28px font-400 tracking-0px

    # heading-04
    text-28px leading-36px font-400 tracking-0px

    # heading-05
    text-32px leading-40px font-400 tracking-0px

    # heading-06
    text-42px leading-50px font-300 tracking-0px

    # heading-07
    text-54px leading-64px font-300 tracking-0px

    # fluid-heading-03
    text-20px leading-28px font-400 tracking-0px
    max:text-24px

    # fluid-heading-04
    text-28px leading-36px font-400 tracking-0px
    xl:text-32px xl:leading-40px

    # fluid-heading-05
    text-32px leading-40px font-400 tracking-0px
    md:text-36px md:leading-44px md:font-300
    lg:text-42px lg:leading-50px
    xl:text-48px xl:leading-56px
    max:text-60px max:leading-70px

    # fluid-heading-06
    text-32px leading-40px font-600 tracking-0px
    md:text-36px md:leading-44px md:font-600
    lg:text-42px lg:leading-50px
    xl:text-48px xl:leading-56px
    max:text-60px max:leading-70px

    # fluid-paragraph-01
    text-24px leading-30px font-300 tracking-0px
    lg:text-28px lg:leading-36px
    max:text-32px max:leading-40px

    # fluid-display-01
    text-42px leading-50px font-300 tracking-0px
    lg:text-54px lg:leading-64px
    xl:text-60px xl:leading-70px
    max:text-76px max:leading-86px

    # fluid-display-02
    text-42px leading-50px font-600 tracking-0px
    lg:text-54px lg:leading-64px
    xl:text-60px xl:leading-70px
    max:text-76px max:leading-86px

    # fluid-display-03
    text-42px leading-50px font-300 tracking-0px
    md:text-54px md:leading-64px
    lg:text-60px lg:leading-70px lg:-leading-0.64px
    xl:text-76px xl:leading-86px
    max:text-84px max:leading-94px max:-leading-0.96px

    # fluid-display-04
    text-42px leading-50px font-300 tracking-0px
    md:text-68px md:leading-78px
    lg:text-92px lg:leading-102px lg:-leading-0.64px
    xl:text-122px xl:leading-130px
    max:text-156px max:leading-164px max:-leading-0.96px
    """);
  }

  private Css.Option extendColors() {
    return Css.extendColors("""
    background: var(--cds-background)
    background-active: var(--cds-background-active)
    background-hover: var(--cds-background-hover)
    background-selected: var(--cds-background-selected)
    border-interactive: var(--cds-border-interactive)
    border-subtle: var(--cds-border-subtle)
    button-danger: var(--cds-button-danger-primary)
    button-danger-active: var(--cds-button-danger-active)
    button-danger-hover: var(--cds-button-danger-hover)
    button-danger-secondary: var(--cds-button-danger-secondary)
    button-primary: var(--cds-button-primary)
    button-primary-active: var(--cds-button-primary-active)
    button-primary-hover: var(--cds-button-primary-hover)
    button-secondary: var(--cds-button-secondary)
    button-secondary-active: var(--cds-button-secondary-active)
    button-secondary-hover: var(--cds-button-secondary-hover)
    button-tertiary: var(--cds-button-tertiary)
    button-tertiary-active: var(--cds-button-tertiary-active)
    button-tertiary-hover: var(--cds-button-tertiary-hover)
    focus: var(--cds-focus)
    icon-disabled: var(--cds-icon-disabled)
    icon-primary: var(--cds-icon-primary)
    icon-secondary: var(--cds-icon-secondary)
    layer: var(--cds-layer)
    layer-accent: var(--cds-layer-accent)
    layer-hover: var(--cds-layer-hover)
    link-primary: var(--cds-link-primary)
    link-primary-hover: var(--cds-link-primary-hover)
    link-visited: var(--cds-link-visited)
    overlay: var(--cds-overlay)
    text-inverse: var(--cds-text-inverse)
    text-on-color: var(--cds-text-on-color)
    text-on-color-disabled: var(--cds-text-on-color-disabled)
    text-primary: var(--cds-text-primary)
    text-secondary: var(--cds-text-secondary)
    """);
  }

  private Css.Option extendSpacing() {
    return Css.extendSpacing("""
    header: var(--spacing-header)
    side-nav: var(--spacing-side-nav)

    spacing-01: var(--cds-spacing-01)
    spacing-02: var(--cds-spacing-02)
    spacing-03: var(--cds-spacing-03)
    spacing-04: var(--cds-spacing-04)
    spacing-05: var(--cds-spacing-05)
    spacing-06: var(--cds-spacing-06)
    spacing-07: var(--cds-spacing-07)
    spacing-08: var(--cds-spacing-08)
    spacing-09: var(--cds-spacing-09)
    spacing-10: var(--cds-spacing-10)
    spacing-11: var(--cds-spacing-11)
    spacing-12: var(--cds-spacing-12)
    spacing-13: var(--cds-spacing-13)

    0px: 0px
    0.5px: 0.5px
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

    56px: 3.5rem

    64px: 4rem

    80px: 5rem

    96px: 6rem

    112px: 7rem
    128px: 8rem
    144px: 9rem
    160px: 10rem
    176px: 11rem
    192px: 12rem

    208px: 13rem
    224px: 14rem
    240px: 15rem
    256px: 16rem
    288px: 18rem
    320px: 20rem
    384px: 24rem
    """);
  }

  private Css.Option extendZIndex() {
    return Css.extendZIndex("""
    auto: auto
    dropdown: 9100
    tearsheet: 9000
    modal: 9000
    header: 8000
    overlay: 6000
    floating: 6000
    footer: 5000
    hidden: -1
    """);
  }

}