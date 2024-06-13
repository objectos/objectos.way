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
package testing.zite;

import objectos.notes.NoteSink;
import objectos.way.SessionStore;
import objectos.web.Stage;
import objectos.web.WebResources;

public final class TestingSiteInjector {

  private final NoteSink noteSink;

  private final SessionStore sessionStore;

  private final Stage stage;

  private final WebResources webResources;

  public TestingSiteInjector(NoteSink noteSink,
                             SessionStore sessionStore,
                             Stage stage,
                             WebResources webResources) {
    this.noteSink = noteSink;
    this.sessionStore = sessionStore;
    this.stage = stage;
    this.webResources = webResources;
  }

  public final NoteSink noteSink() { return noteSink; }

  public final SessionStore sessionStore() { return sessionStore; }

  public final Stage stage() { return stage; }

  public final WebResources webResources() { return webResources; }

}