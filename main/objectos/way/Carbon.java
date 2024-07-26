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
import objectos.lang.object.Check;
import objectos.way.Css.Generator.Classes;

/**
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon {

  public enum Icon {

    CLOSE("""
    <polygon points="17.4141 16 24 9.4141 22.5859 8 16 14.5859 9.4143 8 8 9.4141 14.5859 16 8 22.5859 9.4143 24 16 17.4141 22.5859 24 24 22.5859 17.4141 16"/>"""),

    MENU("""
    <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>""");

    final String raw;

    private Icon(String raw) {
      this.raw = raw;
    }

  }

  /**
   * A Carbon configuration option.
   */
  public sealed interface Option {}

  /**
   * The UI shell is the top-level UI component of an web application.
   */
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

  //
  // non-public types
  //

  private static final class Builder {

    Css.Generator.Classes classes;

    public final Carbon build() {
      return new Carbon(
          classes != null ? classes : Css.classes()
      );
    }

    final void classes(Classes value) {
      Check.state(classes == null, "classes was already set");

      classes = value;
    }

  }

  private non-sealed static abstract class CarbonOption implements Option {

    abstract void acceptBuilder(Builder builder);

  }

  private final Css.Generator.Classes classes;

  private Carbon(Css.Generator.Classes classes) {
    this.classes = classes;
  }

  /**
   * Creates a new carbon instance with the specified configuration options.
   */
  public static Carbon create(Option... options) {
    Builder builder;
    builder = new Builder();

    for (int i = 0; i < options.length; i++) {
      Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      CarbonOption option;
      option = (CarbonOption) o;

      option.acceptBuilder(builder);
    }

    return builder.build();
  }

  public static Option classes(Class<?>... classesToScan) {
    Classes value;
    value = Css.classes(classesToScan);

    return new CarbonOption() {
      @Override
      final void acceptBuilder(Builder builder) {
        builder.classes(value);
      }
    };
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
        route("/ui/carbon.css", GET(new CarbonStyles(classes)));

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

  static Script.Action joinIf(Script.Action existing, Script.Action value) {
    Script.Action a;
    a = Check.notNull(value, "value == null");

    if (existing == null) {
      return a;
    } else {
      return Script.join(existing, a);
    }
  }

}