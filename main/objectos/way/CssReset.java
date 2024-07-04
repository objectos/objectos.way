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

final class CssReset {

  private CssReset() {}
  
  /*
   * current version: https://github.com/tailwindlabs/tailwindcss/commit/4aefd26f44cbb5ede9bc02c00085e97550d0953e 
   */
  public static String preflight() {
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
      1. Inherit the font styles in all browsers.
      2. Remove the default background color.
    */
    
    button,
    input,
    optgroup,
    select,
    textarea,
    ::file-selector-button {
      font: inherit; /* 1 */
      font-feature-settings: inherit; /* 1 */
      font-variation-settings: inherit; /* 1 */
      letter-spacing: inherit; /* 1 */
      color: inherit; /* 1 */
      background: transparent; /* 2 */
    }
    
    /*
      Reset the default inset border style for form controls to solid.
    */
    
    input:where(:not([type='button'], [type='reset'], [type='submit'])),
    select,
    textarea {
      border: 1px solid;
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
      Use the modern Firefox focus style for all focusable elements.
    */
    
    :-moz-focusring {
      outline: auto;
    }
    
    /*
      Remove the additional `:invalid` styles in Firefox. (https://github.com/mozilla/gecko-dev/blob/2f9eacd9d3d995c937b4251a5557d95d494c9be1/layout/style/res/forms.css#L728-L737)
    */
    
    :-moz-ui-invalid {
      box-shadow: none;
    }
    
    /*
      Add the correct vertical alignment in Chrome and Firefox.
    */
    
    progress {
      vertical-align: baseline;
    }
    
    /*
      Correct the cursor style of increment and decrement buttons in Safari.
    */
    
    ::-webkit-inner-spin-button,
    ::-webkit-outer-spin-button {
      height: auto;
    }
    
    /*
      Remove the inner padding in Chrome and Safari on macOS.
    */
    
    ::-webkit-search-decoration {
      -webkit-appearance: none;
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
      Prevent resizing textareas horizontally by default.
    */
    
    textarea {
      resize: vertical;
    }
    
    /*
      1. Reset the default placeholder opacity in Firefox. (https://github.com/tailwindlabs/tailwindcss/issues/3300)
      2. Set the default placeholder color to a semi-transparent version of the current text color.
    */
    
    ::placeholder {
      opacity: 1; /* 1 */
      color: color-mix(in srgb, currentColor 50%, transparent); /* 2 */
    }
    
    /*
      Make sure disabled buttons don't get the pointer cursor.
    */
    
    :disabled {
      cursor: default;
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
      Make elements with the HTML hidden attribute stay hidden by default.
    */
    
    [hidden] {
      display: none !important;
    }
    """;
  }

}