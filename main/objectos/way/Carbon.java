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
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon extends CarbonClasses {

  /**
   * The White theme.
   */
  public static final Html.ClassName WHITE = Html.className("cds--white");

  /**
   * The Gray 10 theme.
   */
  public static final Html.ClassName G10 = Html.className("cds--g10");

  /**
   * The Gray 90 theme.
   */
  public static final Html.ClassName G90 = Html.className("cds--g90");

  /**
   * The Gray 100 theme.
   */
  public static final Html.ClassName G100 = Html.className("cds--g100");

  public enum Icon {

    CLOSE,

    MENU;

  }

  public static abstract class Shell extends CarbonShell {

    /**
     * Sole constructor.
     *
     * @param http
     *        the HTTP exchange
     */
    protected Shell(Http.Exchange http) {
      super(http);
    }

  }

  private Carbon() {}

  public static Carbon create() {
    return new Carbon();
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
        route("/ui/script.js", GET(this::script));
        route("/ui/carbon.css", GET(new CarbonStyles()));

        filter(this::injectCarbon);
      }

      private void script(Http.Exchange http) {
        http.status(Http.OK);

        http.dateNow();

        http.header(Http.CONTENT_TYPE, "text/javascript; charset=utf-8");

        http.header(Http.CONTENT_LENGTH, script.length);

        http.send(script);
      }

      private void injectCarbon(Http.Exchange http) {
        http.set(Carbon.class, Carbon.this);
      }
    };
  }

}