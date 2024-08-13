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

import objectos.way.Carbon;
import objectos.way.Http;

final class Index extends CarbonPage {

  public Index(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.HOME;
  }

  @Override
  protected final void renderHead() {
    title("Objectos Carbon");
  }

  @Override
  protected final Carbon.ShellContent renderContent() {
    return carbon.shellContent(
        carbon.dataFrame("main", getClass().getSimpleName())
    );
  }

}