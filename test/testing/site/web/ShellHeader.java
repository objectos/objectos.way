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
package testing.site.web;

import objectos.way.Html;

public class ShellHeader extends Html.Component {

  private enum Section {
    HOME("/"),

    FILMS("/film");

    private final String href;

    private Section(String href) { this.href = href; }
  }

  private static final Section[] SECTIONS = Section.values();

  private Section active = Section.HOME;

  public ShellHeader(Html.Template parent) {
    super(parent);
  }

  public final void home() {
    active = Section.HOME;
  }

  public final void render() {
    m.header(
        m.nav(
            m.ul(
                m.renderFragment(this::items)
            )
        )
    );
  }

  private void items() {
    for (Section section : SECTIONS) {
      if (section == active) {
        m.li(section.name());
      } else {
        m.li(
            m.a(m.href(section.href), m.text(section.name()))
        );
      }
    }
  }

}