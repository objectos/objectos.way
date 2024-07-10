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

import objectos.way.Html.FragmentLambda;
import objectos.way.Html.Instruction;

/**
 * The <strong>Objectos UI</strong> main class.
 */
public abstract class Ui {

  /**
   * An UI binder provides component factories that are bound to an specific
   * HTML template.
   */
  public interface Binder {

    Http.Module createHttpModule();

    /**
     * Creates a new UI component factory bound to the specified template.
     *
     * @param template
     *        the template responsible for the actual rendering
     *
     * @return a newly created UI component factory
     */
    Ui ui(Html.Template template);

  }

  /**
   * The UI shell is the top level UI component of an web application.
   */
  public static abstract class Shell extends Html.Template implements Web.Action {

    private final Http.Exchange http;

    protected final Ui ui;

    protected Shell(Http.Exchange http) {
      this.http = http;

      Binder binder;
      binder = http.get(Binder.class);

      ui = binder.ui(this);
    }

    @Override
    public final void execute() {
      http.ok(this);
    }

    @Override
    protected final void render() {
      doctype();
      html(
          head(
              meta(charset("utf-8")),
              meta(httpEquiv("content-type"), content("text/html; charset=utf-8")),
              meta(name("viewport"), content("width=device-width, initial-scale=1")),
              script(src("/ui/script.js")),
              link(rel("shortcut icon"), type("image/x-icon"), href("/favicon.png")),
              link(rel("stylesheet"), type("text/css"), href("/ui/carbon.css")),
              title("Objectos Carbon")
          ),

          body(
              f(this::renderUi)
          )
      );
    }

    protected abstract void renderUi();

  }

  public sealed interface ShellHeader {
  }

  // components

  interface Component {
    ComponentKey key();

    Object value();

    default Html.Instruction elementValue() {
      return (Instruction) value();
    }

    default Html.FragmentLambda fragmentValue() {
      return (FragmentLambda) value();
    }

    default String stringValue() {
      return (String) value();
    }
  }

  enum ComponentKey {
    ELEMENT,

    FRAGMENT,

    TITLE;
  }

  /**
   * An UI component.
   */
  record UiComponent(ComponentKey key, Object value) implements ShellHeader {}

  /**
   * Sole constructor.
   */
  protected Ui() {}

  public static Binder createCarbon() {
    return new UiBinderCarbon();
  }

  public abstract ShellHeader shellHeader();

}