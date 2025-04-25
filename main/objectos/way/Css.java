/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * The <strong>Objectos CSS</strong> main class.
 */
public final class Css {

  //
  // public API
  //

  /**
   * The configuration of a CSS generation process. It is also capable of
   * generating CSS on demand by invoking the {@link #writeTo(Appendable)}.
   */
  public sealed interface Configuration extends Media.Text permits CssConfiguration {

    /**
     * Options for creating a {@code Configuration} instance.
     */
    sealed interface Options permits CssConfigurationBuilder {

      /**
       * Use the specified note sink.
       *
       * @param value
       *        the note sink to use
       */
      void noteSink(Note.Sink value);

      void scanClass(Class<?> value);

      /**
       * Recursively scan the specified directory for Java class files.
       *
       * @param value
       *        the directory containing Java class files
       */
      void scanDirectory(Path value);

      void scanJarFileOf(Class<?> value);

      void theme(String value);

      void theme(String query, String value);

    }

    /**
     * Creates a new {@code Configuration} instance with the specified options.
     *
     * @param options
     *        a handle for an {@code Options} instance
     *
     * @return a new {@code Configuration} instance
     */
    static Configuration create(Consumer<? super Options> options) {
      final CssConfigurationBuilder builder;
      builder = new CssConfigurationBuilder();

      options.accept(builder);

      return builder.build();
    }

    /**
     * Returns {@code text/css; charset=utf-8}.
     *
     * @return {@code text/css; charset=utf-8}
     */
    @Override
    String contentType();

    /**
     * Returns {@code StandardCharsets.UTF_8}.
     *
     * @return {@code StandardCharsets.UTF_8}
     */
    @Override
    Charset charset();

    /**
     * Scans the class file of {@code Source} annotated classes for CSS
     * utilities and writes the resulting CSS to the specified
     * {@code Appendable}.
     *
     * @param out
     *        the generated CSS will be appended to this object
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    @Override
    void writeTo(Appendable out) throws IOException;

  }

  /**
   * Indicates that the annotated type should be scanned for CSS utilities
   * during a CSS generation process.
   */
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface Source {}

  //
  // non-public types
  //

  enum Key {

    ALIGN_CONTENT,
    ALIGN_ITEMS,
    ALIGN_SELF,
    APPEARANCE,
    ASPECT_RATIO,

    BACKGROUND_COLOR,
    BORDER,
    BORDER_BOTTOM,
    BORDER_BOTTOM_COLOR,
    BORDER_BOTTOM_STYLE,
    BORDER_BOTTOM_WIDTH,
    BORDER_COLLAPSE,
    BORDER_COLOR,
    BORDER_LEFT,
    BORDER_LEFT_COLOR,
    BORDER_LEFT_STYLE,
    BORDER_LEFT_WIDTH,
    BORDER_RADIUS,
    BORDER_RIGHT,
    BORDER_RIGHT_COLOR,
    BORDER_RIGHT_STYLE,
    BORDER_RIGHT_WIDTH,
    BORDER_SPACING,
    BORDER_STYLE,
    BORDER_TOP,
    BORDER_TOP_COLOR,
    BORDER_TOP_STYLE,
    BORDER_TOP_WIDTH,
    BORDER_WIDTH,
    BOTTOM,
    BOX_SHADOW,

    CLEAR,
    COLOR,
    COLUMN_GAP,
    CONTENT,
    CURSOR,

    DISPLAY,

    FLEX,
    FLEX_BASIS,
    FLEX_DIRECTION,
    FLEX_GROW,
    FLEX_SHRINK,
    FLEX_WRAP,
    FLOAT,
    FILL,
    FILTER,
    FONT_FAMILY,
    FONT_SIZE,
    FONT_STYLE,
    FONT_WEIGHT,

    GAP,
    GRID,
    GRID_AREA,
    GRID_COLUMN,
    GRID_COLUMN_END,
    GRID_COLUMN_START,
    GRID_TEMPLATE,
    GRID_TEMPLATE_COLUMNS,
    GRID_TEMPLATE_ROWS,

    HEIGHT,

    INSET,

    JUSTIFY_CONTENT,
    JUSTIFY_ITEMS,
    JUSTIFY_SELF,

    LEFT,
    LETTER_SPACING,
    LINE_HEIGHT,
    LIST_STYLE_TYPE,

    MARGIN,
    /**/MARGIN_TOP,
    /**/MARGIN_RIGHT,
    /**/MARGIN_BOTTOM,
    /**/MARGIN_LEFT,
    /**/MARGIN_BLOCK,
    /**/MARGIN_INLINE,
    MAX_HEIGHT,
    MAX_WIDTH,
    MIN_HEIGHT,
    MIN_WIDTH,

    OPACITY,
    OUTLINE,
    OUTLINE_COLOR,
    OUTLINE_OFFSET,
    OUTLINE_STYLE,
    OUTLINE_WIDTH,
    OVERFLOW,
    OVERFLOW_X,
    OVERFLOW_Y,

    PADDING,
    /**/PADDING_TOP,
    /**/PADDING_RIGHT,
    /**/PADDING_BOTTOM,
    /**/PADDING_LEFT,
    POINTER_EVENTS,
    POSITION,

    RIGHT,
    ROTATE,
    ROW_GAP,

    SCROLL_BEHAVIOR,
    STROKE,
    STROKE_OPACITY,
    STROKE_WIDTH,

    TAB_SIZE,
    TABLE_LAYOUT,
    TEXT_ALIGN,
    TEXT_COLOR,
    TEXT_DECORATION,
    /**/TEXT_DECORATION_COLOR,
    /**/TEXT_DECORATION_LINE,
    /**/TEXT_DECORATION_STYLE,
    /**/TEXT_DECORATION_THICKNESS,
    TEXT_OVERFLOW,
    TEXT_SHADOW,
    TEXT_TRANSFORM,
    TEXT_WRAP,
    TOP,
    TRANSFORM,
    TRANSFORM_ORIGIN,
    TRANSITION,
    TRANSITION_DELAY,
    TRANSITION_DURATION,
    TRANSITION_PROPERTY,
    TRANSITION_TIMING_FUNCTION,
    TRANSLATE,

    USER_SELECT,

    VERTICAL_ALIGN,
    VISIBILITY,

    WHITE_SPACE,
    WIDTH,
    WORD_SPACING,

    Z_INDEX;

    final String propertyName = name().replace('_', '-').toLowerCase(Locale.US);

  }

  //
  // L
  //

  enum Layer {
    THEME,
    BASE,
    COMPONENTS,
    UTILITIES;
  }

  //
  // T
  //

  record ThemeEntry(int index, String name, String value, String id) implements Comparable<ThemeEntry> {

    @Override
    public final int compareTo(ThemeEntry o) {
      return Integer.compare(index, o.index);
    }

    public final Object key() {
      return name;
    }

    @Override
    public final String toString() {
      StringBuilder out;
      out = new StringBuilder();

      writeTo(out);

      return out.toString();
    }

    public final void writeTo(StringBuilder out) {
      out.append(name);
      out.append(": ");
      out.append(value);
      out.append(';');
    }

    final boolean shouldClear() {
      return "*".equals(id) && "initial".equals(value);
    }

    final ThemeEntry withValue(ThemeEntry newValue) {
      return new ThemeEntry(index, name, newValue.value, id);
    }

  }

  private Css() {}

  //
  // current version:
  // https://github.com/tailwindlabs/tailwindcss/blob/961e8da8fd0b2d96c49a5907bd9df70efcacccf7/packages/tailwindcss/preflight.css
  //
  static String defaultBase() {
    return """
    /*
      1. Prevent padding and border from affecting element width. (https://github.com/mozdevs/cssremedy/issues/4)
      2. Remove default margins and padding
      3. Reset all borders.
    */

    *,
    ::after,
    ::before,
    ::backdrop,
    ::file-selector-button {
      box-sizing: border-box; /* 1 */
      margin: 0; /* 2 */
      padding: 0; /* 2 */
      border: 0 solid; /* 3 */
    }

    /*
      1. Use a consistent sensible line-height in all browsers.
      2. Prevent adjustments of font size after orientation changes in iOS.
      3. Use a more readable tab size.
      4. Use the user's configured `sans` font-family by default.
      5. Use the user's configured `sans` font-feature-settings by default.
      6. Use the user's configured `sans` font-variation-settings by default.
      7. Disable tap highlights on iOS.
    */

    html,
    :host {
      line-height: 1.5; /* 1 */
      -webkit-text-size-adjust: 100%; /* 2 */
      tab-size: 4; /* 3 */
      font-family: var(
        --default-font-family,
        ui-sans-serif,
        system-ui,
        sans-serif,
        'Apple Color Emoji',
        'Segoe UI Emoji',
        'Segoe UI Symbol',
        'Noto Color Emoji'
      ); /* 4 */
      font-feature-settings: var(--default-font-feature-settings, normal); /* 5 */
      font-variation-settings: var(--default-font-variation-settings, normal); /* 6 */
      -webkit-tap-highlight-color: transparent; /* 7 */
    }

    /*
      Inherit line-height from `html` so users can set them as a class directly on the `html` element.
    */

    body {
      line-height: inherit;
    }

    /*
      1. Add the correct height in Firefox.
      2. Correct the inheritance of border color in Firefox. (https://bugzilla.mozilla.org/show_bug.cgi?id=190655)
      3. Reset the default border style to a 1px solid border.
    */

    hr {
      height: 0; /* 1 */
      color: inherit; /* 2 */
      border-top-width: 1px; /* 3 */
    }

    /*
      Add the correct text decoration in Chrome, Edge, and Safari.
    */

    abbr:where([title]) {
      -webkit-text-decoration: underline dotted;
      text-decoration: underline dotted;
    }

    /*
      Remove the default font size and weight for headings.
    */

    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      font-size: inherit;
      font-weight: inherit;
    }

    /*
      Reset links to optimize for opt-in styling instead of opt-out.
    */

    a {
      color: inherit;
      -webkit-text-decoration: inherit;
      text-decoration: inherit;
    }

    /*
      Add the correct font weight in Edge and Safari.
    */

    b,
    strong {
      font-weight: bolder;
    }

    /*
      1. Use the user's configured `mono` font-family by default.
      2. Use the user's configured `mono` font-feature-settings by default.
      3. Use the user's configured `mono` font-variation-settings by default.
      4. Correct the odd `em` font sizing in all browsers.
    */

    code,
    kbd,
    samp,
    pre {
      font-family: var(
        --default-mono-font-family,
        ui-monospace,
        SFMono-Regular,
        Menlo,
        Monaco,
        Consolas,
        'Liberation Mono',
        'Courier New',
        monospace
      ); /* 4 */
      font-feature-settings: var(--default-mono-font-feature-settings, normal); /* 5 */
      font-variation-settings: var(--default-mono-font-variation-settings, normal); /* 6 */
      font-size: 1em; /* 4 */
    }

    /*
      Add the correct font size in all browsers.
    */

    small {
      font-size: 80%;
    }

    /*
      Prevent `sub` and `sup` elements from affecting the line height in all browsers.
    */

    sub,
    sup {
      font-size: 75%;
      line-height: 0;
      position: relative;
      vertical-align: baseline;
    }

    sub {
      bottom: -0.25em;
    }

    sup {
      top: -0.5em;
    }

    /*
      1. Remove text indentation from table contents in Chrome and Safari. (https://bugs.chromium.org/p/chromium/issues/detail?id=999088, https://bugs.webkit.org/show_bug.cgi?id=201297)
      2. Correct table border color inheritance in all Chrome and Safari. (https://bugs.chromium.org/p/chromium/issues/detail?id=935729, https://bugs.webkit.org/show_bug.cgi?id=195016)
      3. Remove gaps between table borders by default.
    */

    table {
      text-indent: 0; /* 1 */
      border-color: inherit; /* 2 */
      border-collapse: collapse; /* 3 */
    }

    /*
      Use the modern Firefox focus style for all focusable elements.
    */

    :-moz-focusring {
      outline: auto;
    }

    /*
      Add the correct vertical alignment in Chrome and Firefox.
    */

    progress {
      vertical-align: baseline;
    }

    /*
      Add the correct display in Chrome and Safari.
    */

    summary {
      display: list-item;
    }

    /*
      Make lists unstyled by default.
    */

    ol,
    ul,
    menu {
      list-style: none;
    }

    /*
      1. Make replaced elements `display: block` by default. (https://github.com/mozdevs/cssremedy/issues/14)
      2. Add `vertical-align: middle` to align replaced elements more sensibly by default. (https://github.com/jensimmons/cssremedy/issues/14#issuecomment-634934210)
          This can trigger a poorly considered lint error in some tools but is included by design.
    */

    img,
    svg,
    video,
    canvas,
    audio,
    iframe,
    embed,
    object {
      display: block; /* 1 */
      vertical-align: middle; /* 2 */
    }

    /*
      Constrain images and videos to the parent width and preserve their intrinsic aspect ratio. (https://github.com/mozdevs/cssremedy/issues/14)
    */

    img,
    video {
      max-width: 100%;
      height: auto;
    }

    /*
      1. Inherit font styles in all browsers.
      2. Remove border radius in all browsers.
      3. Remove background color in all browsers.
      4. Ensure consistent opacity for disabled states in all browsers.
    */

    button,
    input,
    select,
    optgroup,
    textarea,
    ::file-selector-button {
      font: inherit; /* 1 */
      font-feature-settings: inherit; /* 1 */
      font-variation-settings: inherit; /* 1 */
      letter-spacing: inherit; /* 1 */
      color: inherit; /* 1 */
      border-radius: 0; /* 2 */
      background-color: transparent; /* 3 */
      opacity: 1; /* 4 */
    }

    /*
      Restore default font weight.
    */

    :where(select:is([multiple], [size])) optgroup {
      font-weight: bolder;
    }

    /*
      Restore indentation.
    */

    :where(select:is([multiple], [size])) optgroup option {
      padding-inline-start: 20px;
    }

    /*
      Restore space after button.
    */

    ::file-selector-button {
      margin-inline-end: 4px;
    }

    /*
      1. Reset the default placeholder opacity in Firefox. (https://github.com/tailwindlabs/tailwindcss/issues/3300)
      2. Set the default placeholder color to a semi-transparent version of the current text color.
    */

    ::placeholder {
      opacity: 1; /* 1 */
      color: color-mix(in oklab, currentColor 50%, transparent); /* 2 */
    }

    /*
      Prevent resizing textareas horizontally by default.
    */

    textarea {
      resize: vertical;
    }

    /*
      Remove the inner padding in Chrome and Safari on macOS.
    */

    ::-webkit-search-decoration {
      -webkit-appearance: none;
    }

    /*
      1. Ensure date/time inputs have the same height when empty in iOS Safari.
      2. Ensure text alignment can be changed on date/time inputs in iOS Safari.
    */

    ::-webkit-date-and-time-value {
      min-height: 1lh; /* 1 */
      text-align: inherit; /* 2 */
    }

    /*
      Prevent height from changing on date/time inputs in macOS Safari when the input is set to `display: block`.
    */

    ::-webkit-datetime-edit {
      display: inline-flex;
    }

    /*
      Remove excess padding from pseudo-elements in date/time inputs to ensure consistent height across browsers.
    */

    ::-webkit-datetime-edit-fields-wrapper {
      padding: 0;
    }

    ::-webkit-datetime-edit,
    ::-webkit-datetime-edit-year-field,
    ::-webkit-datetime-edit-month-field,
    ::-webkit-datetime-edit-day-field,
    ::-webkit-datetime-edit-hour-field,
    ::-webkit-datetime-edit-minute-field,
    ::-webkit-datetime-edit-second-field,
    ::-webkit-datetime-edit-millisecond-field,
    ::-webkit-datetime-edit-meridiem-field {
      padding-block: 0;
    }

    /*
      Remove the additional `:invalid` styles in Firefox. (https://github.com/mozilla/gecko-dev/blob/2f9eacd9d3d995c937b4251a5557d95d494c9be1/layout/style/res/forms.css#L728-L737)
    */

    :-moz-ui-invalid {
      box-shadow: none;
    }

    /*
      Correct the inability to style the border radius in iOS Safari.
    */

    button,
    input:where([type='button'], [type='reset'], [type='submit']),
    ::file-selector-button {
      appearance: button;
    }

    /*
      Correct the cursor style of increment and decrement buttons in Safari.
    */

    ::-webkit-inner-spin-button,
    ::-webkit-outer-spin-button {
      height: auto;
    }

    /*
      Make elements with the HTML hidden attribute stay hidden by default.
    */

    [hidden]:where(:not([hidden='until-found'])) {
      display: none !important;
    }
    """;
  }

  //
  // current version:
  // https://github.com/tailwindlabs/tailwindcss/blob/aa15964b28ab9858ac0055082741c2f95f20a920/packages/tailwindcss/theme.css
  //
  static String defaultTheme() {
    return """
    --default-font-sans: ui-sans-serif, system-ui, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
    --default-font-serif: ui-serif, Georgia, Cambria, 'Times New Roman', Times, serif;
    --default-font-mono: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;

    --font-sans: var(--default-font-sans);
    --font-serif: var(--default-font-serif);
    --font-mono: var(--default-font-mono);

    --color-red-50: oklch(0.971 0.013 17.38);
    --color-red-100: oklch(0.936 0.032 17.717);
    --color-red-200: oklch(0.885 0.062 18.334);
    --color-red-300: oklch(0.808 0.114 19.571);
    --color-red-400: oklch(0.704 0.191 22.216);
    --color-red-500: oklch(0.637 0.237 25.331);
    --color-red-600: oklch(0.577 0.245 27.325);
    --color-red-700: oklch(0.505 0.213 27.518);
    --color-red-800: oklch(0.444 0.177 26.899);
    --color-red-900: oklch(0.396 0.141 25.723);
    --color-red-950: oklch(0.258 0.092 26.042);

    --color-orange-50: oklch(0.98 0.016 73.684);
    --color-orange-100: oklch(0.954 0.038 75.164);
    --color-orange-200: oklch(0.901 0.076 70.697);
    --color-orange-300: oklch(0.837 0.128 66.29);
    --color-orange-400: oklch(0.75 0.183 55.934);
    --color-orange-500: oklch(0.705 0.213 47.604);
    --color-orange-600: oklch(0.646 0.222 41.116);
    --color-orange-700: oklch(0.553 0.195 38.402);
    --color-orange-800: oklch(0.47 0.157 37.304);
    --color-orange-900: oklch(0.408 0.123 38.172);
    --color-orange-950: oklch(0.266 0.079 36.259);

    --color-amber-50: oklch(0.987 0.022 95.277);
    --color-amber-100: oklch(0.962 0.059 95.617);
    --color-amber-200: oklch(0.924 0.12 95.746);
    --color-amber-300: oklch(0.879 0.169 91.605);
    --color-amber-400: oklch(0.828 0.189 84.429);
    --color-amber-500: oklch(0.769 0.188 70.08);
    --color-amber-600: oklch(0.666 0.179 58.318);
    --color-amber-700: oklch(0.555 0.163 48.998);
    --color-amber-800: oklch(0.473 0.137 46.201);
    --color-amber-900: oklch(0.414 0.112 45.904);
    --color-amber-950: oklch(0.279 0.077 45.635);

    --color-yellow-50: oklch(0.987 0.026 102.212);
    --color-yellow-100: oklch(0.973 0.071 103.193);
    --color-yellow-200: oklch(0.945 0.129 101.54);
    --color-yellow-300: oklch(0.905 0.182 98.111);
    --color-yellow-400: oklch(0.852 0.199 91.936);
    --color-yellow-500: oklch(0.795 0.184 86.047);
    --color-yellow-600: oklch(0.681 0.162 75.834);
    --color-yellow-700: oklch(0.554 0.135 66.442);
    --color-yellow-800: oklch(0.476 0.114 61.907);
    --color-yellow-900: oklch(0.421 0.095 57.708);
    --color-yellow-950: oklch(0.286 0.066 53.813);

    --color-lime-50: oklch(0.986 0.031 120.757);
    --color-lime-100: oklch(0.967 0.067 122.328);
    --color-lime-200: oklch(0.938 0.127 124.321);
    --color-lime-300: oklch(0.897 0.196 126.665);
    --color-lime-400: oklch(0.841 0.238 128.85);
    --color-lime-500: oklch(0.768 0.233 130.85);
    --color-lime-600: oklch(0.648 0.2 131.684);
    --color-lime-700: oklch(0.532 0.157 131.589);
    --color-lime-800: oklch(0.453 0.124 130.933);
    --color-lime-900: oklch(0.405 0.101 131.063);
    --color-lime-950: oklch(0.274 0.072 132.109);

    --color-green-50: oklch(0.982 0.018 155.826);
    --color-green-100: oklch(0.962 0.044 156.743);
    --color-green-200: oklch(0.925 0.084 155.995);
    --color-green-300: oklch(0.871 0.15 154.449);
    --color-green-400: oklch(0.792 0.209 151.711);
    --color-green-500: oklch(0.723 0.219 149.579);
    --color-green-600: oklch(0.627 0.194 149.214);
    --color-green-700: oklch(0.527 0.154 150.069);
    --color-green-800: oklch(0.448 0.119 151.328);
    --color-green-900: oklch(0.393 0.095 152.535);
    --color-green-950: oklch(0.266 0.065 152.934);

    --color-emerald-50: oklch(0.979 0.021 166.113);
    --color-emerald-100: oklch(0.95 0.052 163.051);
    --color-emerald-200: oklch(0.905 0.093 164.15);
    --color-emerald-300: oklch(0.845 0.143 164.978);
    --color-emerald-400: oklch(0.765 0.177 163.223);
    --color-emerald-500: oklch(0.696 0.17 162.48);
    --color-emerald-600: oklch(0.596 0.145 163.225);
    --color-emerald-700: oklch(0.508 0.118 165.612);
    --color-emerald-800: oklch(0.432 0.095 166.913);
    --color-emerald-900: oklch(0.378 0.077 168.94);
    --color-emerald-950: oklch(0.262 0.051 172.552);

    --color-teal-50: oklch(0.984 0.014 180.72);
    --color-teal-100: oklch(0.953 0.051 180.801);
    --color-teal-200: oklch(0.91 0.096 180.426);
    --color-teal-300: oklch(0.855 0.138 181.071);
    --color-teal-400: oklch(0.777 0.152 181.912);
    --color-teal-500: oklch(0.704 0.14 182.503);
    --color-teal-600: oklch(0.6 0.118 184.704);
    --color-teal-700: oklch(0.511 0.096 186.391);
    --color-teal-800: oklch(0.437 0.078 188.216);
    --color-teal-900: oklch(0.386 0.063 188.416);
    --color-teal-950: oklch(0.277 0.046 192.524);

    --color-cyan-50: oklch(0.984 0.019 200.873);
    --color-cyan-100: oklch(0.956 0.045 203.388);
    --color-cyan-200: oklch(0.917 0.08 205.041);
    --color-cyan-300: oklch(0.865 0.127 207.078);
    --color-cyan-400: oklch(0.789 0.154 211.53);
    --color-cyan-500: oklch(0.715 0.143 215.221);
    --color-cyan-600: oklch(0.609 0.126 221.723);
    --color-cyan-700: oklch(0.52 0.105 223.128);
    --color-cyan-800: oklch(0.45 0.085 224.283);
    --color-cyan-900: oklch(0.398 0.07 227.392);
    --color-cyan-950: oklch(0.302 0.056 229.695);

    --color-sky-50: oklch(0.977 0.013 236.62);
    --color-sky-100: oklch(0.951 0.026 236.824);
    --color-sky-200: oklch(0.901 0.058 230.902);
    --color-sky-300: oklch(0.828 0.111 230.318);
    --color-sky-400: oklch(0.746 0.16 232.661);
    --color-sky-500: oklch(0.685 0.169 237.323);
    --color-sky-600: oklch(0.588 0.158 241.966);
    --color-sky-700: oklch(0.5 0.134 242.749);
    --color-sky-800: oklch(0.443 0.11 240.79);
    --color-sky-900: oklch(0.391 0.09 240.876);
    --color-sky-950: oklch(0.293 0.066 243.157);

    --color-blue-50: oklch(0.97 0.014 254.604);
    --color-blue-100: oklch(0.932 0.032 255.585);
    --color-blue-200: oklch(0.882 0.059 254.128);
    --color-blue-300: oklch(0.809 0.105 251.813);
    --color-blue-400: oklch(0.707 0.165 254.624);
    --color-blue-500: oklch(0.623 0.214 259.815);
    --color-blue-600: oklch(0.546 0.245 262.881);
    --color-blue-700: oklch(0.488 0.243 264.376);
    --color-blue-800: oklch(0.424 0.199 265.638);
    --color-blue-900: oklch(0.379 0.146 265.522);
    --color-blue-950: oklch(0.282 0.091 267.935);

    --color-indigo-50: oklch(0.962 0.018 272.314);
    --color-indigo-100: oklch(0.93 0.034 272.788);
    --color-indigo-200: oklch(0.87 0.065 274.039);
    --color-indigo-300: oklch(0.785 0.115 274.713);
    --color-indigo-400: oklch(0.673 0.182 276.935);
    --color-indigo-500: oklch(0.585 0.233 277.117);
    --color-indigo-600: oklch(0.511 0.262 276.966);
    --color-indigo-700: oklch(0.457 0.24 277.023);
    --color-indigo-800: oklch(0.398 0.195 277.366);
    --color-indigo-900: oklch(0.359 0.144 278.697);
    --color-indigo-950: oklch(0.257 0.09 281.288);

    --color-violet-50: oklch(0.969 0.016 293.756);
    --color-violet-100: oklch(0.943 0.029 294.588);
    --color-violet-200: oklch(0.894 0.057 293.283);
    --color-violet-300: oklch(0.811 0.111 293.571);
    --color-violet-400: oklch(0.702 0.183 293.541);
    --color-violet-500: oklch(0.606 0.25 292.717);
    --color-violet-600: oklch(0.541 0.281 293.009);
    --color-violet-700: oklch(0.491 0.27 292.581);
    --color-violet-800: oklch(0.432 0.232 292.759);
    --color-violet-900: oklch(0.38 0.189 293.745);
    --color-violet-950: oklch(0.283 0.141 291.089);

    --color-purple-50: oklch(0.977 0.014 308.299);
    --color-purple-100: oklch(0.946 0.033 307.174);
    --color-purple-200: oklch(0.902 0.063 306.703);
    --color-purple-300: oklch(0.827 0.119 306.383);
    --color-purple-400: oklch(0.714 0.203 305.504);
    --color-purple-500: oklch(0.627 0.265 303.9);
    --color-purple-600: oklch(0.558 0.288 302.321);
    --color-purple-700: oklch(0.496 0.265 301.924);
    --color-purple-800: oklch(0.438 0.218 303.724);
    --color-purple-900: oklch(0.381 0.176 304.987);
    --color-purple-950: oklch(0.291 0.149 302.717);

    --color-fuchsia-50: oklch(0.977 0.017 320.058);
    --color-fuchsia-100: oklch(0.952 0.037 318.852);
    --color-fuchsia-200: oklch(0.903 0.076 319.62);
    --color-fuchsia-300: oklch(0.833 0.145 321.434);
    --color-fuchsia-400: oklch(0.74 0.238 322.16);
    --color-fuchsia-500: oklch(0.667 0.295 322.15);
    --color-fuchsia-600: oklch(0.591 0.293 322.896);
    --color-fuchsia-700: oklch(0.518 0.253 323.949);
    --color-fuchsia-800: oklch(0.452 0.211 324.591);
    --color-fuchsia-900: oklch(0.401 0.17 325.612);
    --color-fuchsia-950: oklch(0.293 0.136 325.661);

    --color-pink-50: oklch(0.971 0.014 343.198);
    --color-pink-100: oklch(0.948 0.028 342.258);
    --color-pink-200: oklch(0.899 0.061 343.231);
    --color-pink-300: oklch(0.823 0.12 346.018);
    --color-pink-400: oklch(0.718 0.202 349.761);
    --color-pink-500: oklch(0.656 0.241 354.308);
    --color-pink-600: oklch(0.592 0.249 0.584);
    --color-pink-700: oklch(0.525 0.223 3.958);
    --color-pink-800: oklch(0.459 0.187 3.815);
    --color-pink-900: oklch(0.408 0.153 2.432);
    --color-pink-950: oklch(0.284 0.109 3.907);

    --color-rose-50: oklch(0.969 0.015 12.422);
    --color-rose-100: oklch(0.941 0.03 12.58);
    --color-rose-200: oklch(0.892 0.058 10.001);
    --color-rose-300: oklch(0.81 0.117 11.638);
    --color-rose-400: oklch(0.712 0.194 13.428);
    --color-rose-500: oklch(0.645 0.246 16.439);
    --color-rose-600: oklch(0.586 0.253 17.585);
    --color-rose-700: oklch(0.514 0.222 16.935);
    --color-rose-800: oklch(0.455 0.188 13.697);
    --color-rose-900: oklch(0.41 0.159 10.272);
    --color-rose-950: oklch(0.271 0.105 12.094);

    --color-slate-50: oklch(0.984 0.003 247.858);
    --color-slate-100: oklch(0.968 0.007 247.896);
    --color-slate-200: oklch(0.929 0.013 255.508);
    --color-slate-300: oklch(0.869 0.022 252.894);
    --color-slate-400: oklch(0.704 0.04 256.788);
    --color-slate-500: oklch(0.554 0.046 257.417);
    --color-slate-600: oklch(0.446 0.043 257.281);
    --color-slate-700: oklch(0.372 0.044 257.287);
    --color-slate-800: oklch(0.279 0.041 260.031);
    --color-slate-900: oklch(0.208 0.042 265.755);
    --color-slate-950: oklch(0.129 0.042 264.695);

    --color-gray-50: oklch(0.985 0.002 247.839);
    --color-gray-100: oklch(0.967 0.003 264.542);
    --color-gray-200: oklch(0.928 0.006 264.531);
    --color-gray-300: oklch(0.872 0.01 258.338);
    --color-gray-400: oklch(0.707 0.022 261.325);
    --color-gray-500: oklch(0.551 0.027 264.364);
    --color-gray-600: oklch(0.446 0.03 256.802);
    --color-gray-700: oklch(0.373 0.034 259.733);
    --color-gray-800: oklch(0.278 0.033 256.848);
    --color-gray-900: oklch(0.21 0.034 264.665);
    --color-gray-950: oklch(0.13 0.028 261.692);

    --color-zinc-50: oklch(0.985 0 0);
    --color-zinc-100: oklch(0.967 0.001 286.375);
    --color-zinc-200: oklch(0.92 0.004 286.32);
    --color-zinc-300: oklch(0.871 0.006 286.286);
    --color-zinc-400: oklch(0.705 0.015 286.067);
    --color-zinc-500: oklch(0.552 0.016 285.938);
    --color-zinc-600: oklch(0.442 0.017 285.786);
    --color-zinc-700: oklch(0.37 0.013 285.805);
    --color-zinc-800: oklch(0.274 0.006 286.033);
    --color-zinc-900: oklch(0.21 0.006 285.885);
    --color-zinc-950: oklch(0.141 0.005 285.823);

    --color-neutral-50: oklch(0.985 0 0);
    --color-neutral-100: oklch(0.97 0 0);
    --color-neutral-200: oklch(0.922 0 0);
    --color-neutral-300: oklch(0.87 0 0);
    --color-neutral-400: oklch(0.708 0 0);
    --color-neutral-500: oklch(0.556 0 0);
    --color-neutral-600: oklch(0.439 0 0);
    --color-neutral-700: oklch(0.371 0 0);
    --color-neutral-800: oklch(0.269 0 0);
    --color-neutral-900: oklch(0.205 0 0);
    --color-neutral-950: oklch(0.145 0 0);

    --color-stone-50: oklch(0.985 0.001 106.423);
    --color-stone-100: oklch(0.97 0.001 106.424);
    --color-stone-200: oklch(0.923 0.003 48.717);
    --color-stone-300: oklch(0.869 0.005 56.366);
    --color-stone-400: oklch(0.709 0.01 56.259);
    --color-stone-500: oklch(0.553 0.013 58.071);
    --color-stone-600: oklch(0.444 0.011 73.639);
    --color-stone-700: oklch(0.374 0.01 67.558);
    --color-stone-800: oklch(0.268 0.007 34.298);
    --color-stone-900: oklch(0.216 0.006 56.043);
    --color-stone-950: oklch(0.147 0.004 49.25);

    --color-black: #000;
    --color-white: #fff;

    --breakpoint-sm: 40rem;
    --breakpoint-md: 48rem;
    --breakpoint-lg: 64rem;
    --breakpoint-xl: 80rem;
    --breakpoint-2xl: 96rem;

    --default-font-family: var(--font-sans);
    --default-font-feature-settings: var(--font-sans--font-feature-settings);
    --default-font-variation-settings: var(--font-sans--font-variation-settings);
    --default-mono-font-family: var(--font-mono);
    --default-mono-font-feature-settings: var(--font-mono--font-feature-settings);
    --default-mono-font-variation-settings: var(--font-mono--font-variation-settings);

    --rx: 16;
    """;
  }

  static void escape(StringBuilder out, char c) {
    out.append("\\");

    out.append(c);
  }

  static void escapeAsCodePoint(StringBuilder out, char c) {
    out.append("\\");

    out.append(Integer.toHexString(c));

    out.append(' ');
  }

  private static final char NULL = '\u0000';

  static void serializeIdentifier(StringBuilder out, String source) {
    final int length;
    length = source.length();

    if (length == 0) {
      return;
    }

    enum State {

      START,

      START_DASH,

      REST;

    }

    State state;
    state = State.START;

    for (int idx = 0; idx < length; idx++) {
      final char c;
      c = source.charAt(idx);

      switch (state) {

        case START -> {
          switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
              state = State.REST;

              escapeAsCodePoint(out, c);
            }

            case '-' -> {
              state = State.START_DASH;
            }

            default -> {
              state = State.REST;

              idx--;
            }
          }
        }

        case START_DASH -> {
          switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
              state = State.REST;

              out.append('-');

              escapeAsCodePoint(out, c);
            }

            default -> {
              state = State.REST;

              out.append('-');

              idx--;
            }
          }
        }

        case REST -> {
          if ('0' <= c && c <= '9') {
            out.append(c);
          }

          else if ('A' <= c && c <= 'Z') {
            out.append(c);
          }

          else if ('a' <= c && c <= 'z') {
            out.append(c);
          }

          else if (c == '-' || c == '_') {
            out.append(c);
          }

          else if (c >= '\u0080') {
            out.append(c);
          }

          else if (c == NULL) {
            out.append('\uFFFD');
          }

          else if ('\u0001' <= c && c <= '\u001F') {
            escapeAsCodePoint(out, c);
          }

          else if (c == '\u007F') {
            escapeAsCodePoint(out, c);
          }

          else {
            escape(out, c);
          }
        }

      }
    }

    if (state == State.START_DASH) {
      escape(out, '-');
    }

  }

}