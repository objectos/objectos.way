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
import objectos.way.Web;
import objectos.web.Stage;

public record TestingSiteInjector(NoteSink noteSink,
                                  SessionStore sessionStore,
                                  Stage stage,
                                  Web.Resources webResources) {}