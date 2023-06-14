/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

final class Preflight extends CssTemplate {
  @Override
  protected final void definition() {
    style(
      any, __before, __after,

      boxSizing(borderBox),
      borderWidth($0),
      borderStyle(solid),
      borderColor(currentcolor)
    );

    //    /*
    //    1. Use a consistent sensible line-height in all browsers.
    //    2. Prevent adjustments of font size after orientation changes in iOS.
    //    3. Use a more readable tab size.
    //    4. Use the user's configured `sans` font-family by default.
    //    5. Use the user's configured `sans` font-feature-settings by default.
    //    6. Use the user's configured `sans` font-variation-settings by default.
    //    */
    //
    //    html {
    //      line-height: 1.5; /* 1 */
    //      -webkit-text-size-adjust: 100%; /* 2 */
    //      -moz-tab-size: 4; /* 3 */
    //      tab-size: 4; /* 3 */
    //      font-family: theme('fontFamily.sans', ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji"); /* 4 */
    //      font-feature-settings: theme('fontFamily.sans[1].fontFeatureSettings', normal); /* 5 */
    //      font-variation-settings: theme('fontFamily.sans[1].fontVariationSettings', normal); /* 6 */
    //    }
  }
}