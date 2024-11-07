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

import objectos.way.Css;
import objectos.way.Http;
import objectos.way.Note;
import testing.zite.TestingSiteInjector;

final class Styles implements Http.Handler {

  private final Note.Sink noteSink;

  Styles(TestingSiteInjector injector) {
    noteSink = injector.noteSink();
  }

  @Override
  public final void handle(Http.Exchange http) {
    Css.StyleSheet s = Css.generateStyleSheet(
        Css.classes(
            Login.class, ShellHeader.class
        ),

        Css.noteSink(noteSink)
    );

    byte[] bytes;
    bytes = s.toByteArray();

    http.status(Http.Status.OK);

    http.dateNow();

    http.header(Http.HeaderName.CONTENT_TYPE, s.contentType());

    http.header(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    http.send(bytes);
  }

}