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
package testing.site.carbon;

import objectos.way.Http;

final class ComponentsLink extends CarbonPage {

  ComponentsLink(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Link - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    div(
        className("grid-wide max-w-full py-05 bg-layer"),

        h1(
            className("col-span-full heading-04"),

            t("Link")
        )
    );

    section(
        className("grid-wide grid-cols-2 md:grid-cols-3 gap-03 max:grid-cols-6 *:self-start"),

        f(this::renderSection)
    );
  }

  private void renderSection() {
    h2(
        className("col-span-full heading-03"),

        t("Link")
    );

    div(
        a(
            className("link"),

            href("https://www.objectos.com.br"), t("Link")
        )
    );

    div(
        a(
            className("link link-visited"),

            href("https://www.objectos.com.br"), t("Link visited")
        )
    );

    h2(
        className("col-span-full heading-03"),

        t("Link (inline)")
    );

    div(
        a(
            className("link-inline"),

            href("https://www.objectos.com.br"), t("Objectos")
        )
    );
  }

}