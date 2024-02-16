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

import java.nio.charset.StandardCharsets;
import java.util.Set;
import objectos.css.WayStyleGen;
import objectos.http.HeaderName;
import objectos.http.ServerExchange;
import objectos.http.Status;
import objectos.notes.NoteSink;
import testing.zite.TestingSiteInjector;

final class Styles {

  private final NoteSink noteSink;

  Styles(TestingSiteInjector injector) {
    noteSink = injector.noteSink();
  }

  public final void handle(ServerExchange http) {
    WayStyleGen styleGen;
    styleGen = new WayStyleGen();

    styleGen.noteSink(noteSink);

    Set<Class<?>> classes;
    classes = Set.of(Home.class, Login.class);

    String s;
    s = styleGen.generate(classes);

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    http.status(Status.OK);

    http.dateNow();

    http.header(HeaderName.CONTENT_TYPE, "text/css; charset=utf-8");

    http.header(HeaderName.CONTENT_LENGTH, bytes.length);

    http.send(bytes);
  }

}