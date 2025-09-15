/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.nio.charset.Charset;
import java.util.function.Consumer;

/// The **Objectos Script** main interface, part of Objectos HTML.
public sealed interface Script permits ScriptWriter {

  // ##################################################################
  // # BEGIN: Callback
  // ##################################################################

  /// Represents a callback to be executed by the browser's JS engine.
  @FunctionalInterface
  public interface Callback {

    /// Executes this callback.
    void execute();

  }

  // ##################################################################
  // # END: Callback
  // ##################################################################

  // ##################################################################
  // # BEGIN: Element
  // ##################################################################

  /// Represents an element in the browser's DOM.
  public sealed interface Element permits ScriptWriter.ElementQuery {

    /// Returns the value of the attribute with the specified name.
    /// @param name the attribute name
    /// @return the attribute value
    StringQuery attr(Html.AttributeName name);

    /// Sets the specified attribute to the given value.
    /// @param name the attribute name
    /// @param value the attribute value
    void attr(Html.AttributeName name, String value);

    /// Sets the specified attribute to the given value.
    /// @param name the attribute name
    /// @param value the attribute value
    void attr(Html.AttributeName name, StringQuery value);

    /// Closes this `dialog` element.
    void close();

    /// Sets the focus on this element, if it can be focused.
    void focus();

    /// Scrolls the element to the specified `x` and `y` coordinates.
    /// @param x the pixel along the horizontal axis of the element that you want displayed in the upper left.
    /// @param y the pixel along the vertical axis of the element that you want displayed in the upper left.
    void scroll(int x, int y);

    /// Displays this `dialog` element as a modal.
    void showModal();

    /// Submits this `form` element.
    void submit();

    /// Toggles the specified `class` value on this element.
    /// @param className the class name
    void toggleClass(String className);

  }

  // ##################################################################
  // # END: Element
  // ##################################################################

  // ##################################################################
  // # BEGIN: Library
  // ##################################################################

  /// Represents the source code of the Objectos Way JS library.
  public sealed interface Library extends Media.Text permits ScriptLibrary {

    /// Returns the sole instance of this interface.
    /// @return the sole instance of this interface
    static Library of() {
      return ScriptLibrary.INSTANCE;
    }

    /// Returns `text/javascript; charset=utf-8`.
    /// @return always `text/css; charset=utf-8`
    @Override
    String contentType();

    /// Returns `StandardCharsets.UTF_8`.
    /// @return always `StandardCharsets.UTF_8`
    @Override
    Charset charset();

    /// Writes the Objectos Way JS library source code to the specified `Appendable`.
    /// @param out where to append the source code
    /// @throws IOException if an I/O error occurs
    @Override
    void writeTo(Appendable out) throws IOException;

  }

  // ##################################################################
  // # END: Library
  // ##################################################################

  // ##################################################################
  // # BEGIN: Method
  // ##################################################################

  /// The `GET` method.
  Method GET = Method.GET;

  /// The `POST` method.
  Method POST = Method.POST;

  /// HTTP method of request operations.
  public enum Method {

    /// The `GET` method.
    GET,

    /// The `POST` method.
    POST;

  }

  // ##################################################################
  // # END: Method
  // ##################################################################

  /// Represents a reference to a string value in the browser's JS engine.
  public sealed interface StringQuery permits ScriptWriter.ElementMethodInvocation {}

  /// Returns a reference to the element on which the script is declared.
  /// @return a reference to the element
  Element element();

  /// Returns a reference to the element whose `id` property matches
  /// the value of the specified `Html.Id` instance.
  ///
  /// @param id the id of the element to locate
  /// @return a reference to the element
  Element elementById(Html.Id id);

  /// Returns a reference to the element whose `id` property matches
  /// the value of the specified query.
  ///
  /// @param id the id value
  /// @return a reference to the element
  Element elementById(StringQuery id);

  // actions

  /// Executes the specified action after the specified initial delay.
  /// @param ms the delay in milliseconds.
  /// @param action the action to be executed
  void delay(int ms, Callback action);

  /// Swaps the current document contents with the HTML from the specified template.
  /// @param template the HTML template
  void html(Html.Template template);

  /// Performs a soft navigation to the URL specified by the `href`
  /// attribute of this `<a>` HTML element.
  void navigate();

  /// Causes the specified `url` to be pushed into the browser's location history.
  /// @param url the location history entry
  void pushState(String url);

  /// Causes the specified `url` to replace the current entry on the browser's location history.
  /// @param url the location history entry
  void replaceState(String url);

  /// Configures the [request][#request(Consumer)] action.
  public sealed interface RequestOptions permits ScriptWriter.RequestOptions {

    /// Sets the HTTP method of this request.
    /// @param value the HTTP method
    void method(Script.Method value);

    /// Sets the request URL to the specified value.
    /// @param value the URL of the resource
    void url(String value);

    /// Sets the request URL to the specified value.
    /// @param value the URL to the resource
    void url(StringQuery value);

    /// Sets the action to be executed if the request is successful.
    /// @param value the action to be executed
    void onSuccess(Callback value);

  }

  /// Causes the browser to perform a HTTP request.
  /// @param options allows for configuring the request
  void request(Consumer<? super RequestOptions> options);

  /// Causes the event handling to stop at the current HTML element.
  void stopPropagation();

}