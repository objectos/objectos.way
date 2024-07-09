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

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The <strong>Objectos Carbonated UI</strong> main class.
 */
public final class Carbonated implements Ui.Binder {

  public sealed interface Option {

  }

  // non-public types

  static non-sealed abstract class CarbonatedOption implements Option {}

  private Carbonated() {}

  public static Carbonated create(Option... options) {
    return new Carbonated();
  }

  public final Http.Module createHttpModule() {
    final byte[] script;

    try {
      script = Script.getBytes();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return new Web.Module() {
      @Override
      protected final void configure() {
        route("/carbonated/script.js", GET(this::script));
        route("/carbonated/styles.css", GET(new CarbonatedStyles()));
      }

      private void script(Http.Exchange http) {
        http.status(Http.OK);

        http.dateNow();

        http.header(Http.CONTENT_TYPE, "text/javascript; charset=utf-8");

        http.header(Http.CONTENT_LENGTH, script.length);

        http.send(script);
      }
    };
  }

  @Override
  public final Ui ui(Html.Template parent) {
    return new CarbonatedUi(parent);
  }

}