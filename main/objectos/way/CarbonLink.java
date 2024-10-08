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

import objectos.way.Carbon.CarbonLinkStyle;
import objectos.way.Carbon.LinkStyle;

final class CarbonLink {

  private static final Html.ClassName __LINK_BASE = Html.classText("""
      inline-flex
      text-link-primary outline-none
      transition-colors duration-100
      active:underline active:outline active:outline-1 active:outline-focus active:outline-offset-0
      focus:outline focus:outline-1 focus:outline-focus focus:outline-offset-0
      hover:text-link-primary-hover hover:underline
      """);

  private static final Html.ClassName __LINK_STANDARD = Html.classText("""
      no-underline
      """);

  private static final Html.ClassName __LINK_INLINE = Html.classText("""
      underline
      """);

  private static final Html.ClassName __LINK_VISITED = Html.classText("""
      visited:text-link-visited
      visited:hover:text-link-primary-hover
      """);

  private static final Html.ClassName LINK = Html.className(
      __LINK_BASE, __LINK_STANDARD
  );

  private static final Html.ClassName LINK_VISITED = Html.className(
      __LINK_BASE, __LINK_VISITED
  );

  private static final Html.ClassName LINK_INLINE = Html.className(
      __LINK_BASE, __LINK_INLINE
  );

  private static final Html.ClassName LINK_INLINE_VISITED = Html.className(
      __LINK_BASE, __LINK_INLINE, __LINK_VISITED
  );

  private CarbonLink() {}

  public static Html.ElementInstruction render(Html.TemplateBase tmpl, LinkStyle style, Html.Instruction... contents) {
    return tmpl.a(
        switch (style) {
          case CarbonLinkStyle.STANDARD -> LINK;

          case CarbonLinkStyle.VISITED -> LINK_VISITED;

          case CarbonLinkStyle.INLINE -> LINK_INLINE;

          case CarbonLinkStyle.INLINE_VISITED -> LINK_INLINE_VISITED;

          default -> tmpl.noop();
        },

        tmpl.flatten(contents)
    );
  }

}