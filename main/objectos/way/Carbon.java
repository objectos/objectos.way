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
import objectos.way.Html.ClassName;

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

  /**
   * A Carbon UI component.
   */
  public sealed interface Component {

    Html.ElementInstruction render();

  }

  public sealed interface Header extends Component permits CarbonHeader {

    public sealed interface CloseButton extends Component permits CarbonHeader.HeaderCloseButton {

      CloseButton ariaLabel(String value);

      CloseButton title(String value);

      CloseButton dataOnClick(Script.Action value);

      Script.Action hideAction();

      Script.Action showAction();

    }

    public sealed interface MenuButton extends Component permits CarbonHeader.HeaderMenuButton {

      MenuButton ariaLabel(String value);

      MenuButton title(String value);

      MenuButton dataOnClick(Script.Action value);

      Script.Action hideAction();

      Script.Action showAction();

    }

    public sealed interface MenuItem extends Component permits CarbonHeaderMenuItem {

      MenuItem active(boolean value);

      MenuItem href(String value);

      MenuItem text(String value);

    }

    public sealed interface Name extends Component permits CarbonHeaderName {

      Name href(String value);

      Name prefix(String value);

      Name text(String value);

      Name dataOnClick(Script.Action value);

    }

    public sealed interface Navigation extends Component permits CarbonHeaderNavigation {

      Navigation ariaLabel(String value);

      Navigation dataFrame(String name, String value);

      MenuItem addItem();

    }

    CloseButton addCloseButton();

    MenuButton addMenuButton();

    Name addName();

    Navigation addNavigation();

    Header ariaLabel(String value);

  }

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

  public sealed interface Overlay extends Component permits CarbonOverlay {

    Overlay offsetHeader();

    Script.Action hideAction();

    Script.Action showAction();

  }

  /**
   * The UI shell is the top-level UI element of an web application.
   */
  public sealed interface Shell permits CarbonShell {

    Shell theme(ClassName value);

    Shell title(String value);

    Header addHeader();

    Overlay addOverlay();

    void render();

  }

  public static abstract class Template extends CarbonTemplate {

    /**
     * Sole constructor.
     *
     * @param http
     *        the HTTP exchange
     */
    protected Template(Http.Exchange http) {
      super(http);
    }

  }

  public static abstract class Template2 extends CarbonTemplate2 {

    /**
     * Sole constructor.
     *
     * @param http
     *        the HTTP exchange
     */
    protected Template2(Http.Exchange http) {
      super(http);
    }

  }

  private Carbon() {}

  public static Carbon create() {
    return new Carbon();
  }

  public final Shell createShell(Html.Template template) {
    Check.notNull(template, "template == null");

    return new CarbonShell(template);
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