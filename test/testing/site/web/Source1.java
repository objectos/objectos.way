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
package testing.site.web;

import objectos.way.Css;
import objectos.way.Html;

@Css.Source
final class Source1 extends Html.Template {

  private static final Html.ClassName CONTAINER = Html.ClassName.of("""
  mx-auto w-full px-16px
  """);

  @Override
  protected final void render() {
    html(
        body(
            CONTAINER,

            className("bg-white")
        )
    );
  }

}