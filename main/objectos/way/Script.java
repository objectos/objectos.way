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
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * The <strong>Objectos Script</strong> main interface, part of Objectos HTML.
 */
public sealed interface Script permits ScriptWriter {

  Method GET = Method.GET;

  Method POST = Method.POST;

  /**
   * Represents an action to be executed by the browser in the context of an web
   * application.
   */
  public sealed interface Action extends Media.Bytes permits ScriptAction {

    static Action create(Consumer<Script> script) {
      final ScriptWriter writer;
      writer = new ScriptWriter();

      script.accept(writer);

      return writer.build();
    }

    /**
     * Return {@code application/json} always.
     *
     * @return always {@code application/json}
     */
    @Override
    default String contentType() {
      return "application/json";
    }

    /**
     * Returns the JSON data representing this action as a byte array.
     *
     * @return the JSON data representing this action as a byte array
     */
    @Override
    default byte[] toByteArray() {
      String s;
      s = toString();

      return s.getBytes(StandardCharsets.UTF_8);
    }

  }

  /**
   * Represents the source code of the Objectos Way JS library.
   */
  public sealed interface Library extends Media.Text permits ScriptLibrary {

    /**
     * Returns the sole instance of this interface.
     *
     * @return the sole instance of this interface
     */
    static Library of() {
      return ScriptLibrary.INSTANCE;
    }

    /**
     * Returns {@code text/javascript; charset=utf-8}.
     *
     * @return always {@code text/css; charset=utf-8}
     */
    @Override
    String contentType();

    /**
     * Returns {@code StandardCharsets.UTF_8}.
     *
     * @return always {@code StandardCharsets.UTF_8}
     */
    @Override
    Charset charset();

    /**
     * Writes the Objectos Way JS library source code to the specified
     * {@code Appendable}.
     *
     * @param out
     *        where to append the source code
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    @Override
    void writeTo(Appendable out) throws IOException;

  }

  @FunctionalInterface
  public interface Callback {

    void execute();

  }

  public enum Method {

    GET,

    POST;

  }

  public sealed interface StringQuery permits ScriptWriter.ElementMethodInvocation {}

  // queries

  /// Represents an element in the browser's DOM.
  public sealed interface Element permits ScriptWriter.ElementQuery {

    /**
     * Returns the value of the attribute with the specified name.
     *
     * @param name
     *        the attribute name
     */
    StringQuery attr(Html.AttributeName name);

    /**
     * Sets the specified attribute to the given value.
     *
     * @param name
     *        the attribute name
     * @param value
     *        the attribute value
     */
    void attr(Html.AttributeName name, String value);

    /**
     * Sets the specified attribute to the given value.
     *
     * @param name
     *        the attribute name
     * @param value
     *        the attribute value
     */
    void attr(Html.AttributeName name, StringQuery value);

    /// Closes this `dialog` element.
    void close();

    /// Sets the focus on this element, if it can be focused.
    void focus();

    /**
     * Scrolls the element to the specified {@code x} and {@code y} coordinates.
     *
     * @param x
     *        the pixel along the horizontal axis of the element that you want
     *        displayed in the upper left.
     * @param y
     *        the pixel along the vertical axis of the element that you want
     *        displayed in the upper left.
     */
    void scroll(int x, int y);

    /// Displays this `dialog` element as a modal.
    void showModal();

    /// Submits this `form` element.
    void submit();

    /// Toggles the specified `class` value on this element.
    void toggleClass(String className);

  }

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
  /// @param id the query to produce the id of the element to locate
  /// @return a reference to the element
  Element elementById(StringQuery id);

  // actions

  void delay(int ms, Callback callback);

  void html(Html.Template template);

  /**
   * Performs a soft navigation to the URL specified by the {@code href}
   * attribute of this {@code <a>} HTML element.
   */
  void navigate();

  /**
   * Causes the specified {@code url} to be pushed into the browser's location
   * history.
   */
  void pushState(String url);

  /**
   * Causes the specified {@code url} to replace the current entry on the
   * browser's location history.
   */
  void replaceState(String url);

  /**
   * Configures the {@link Script#request(Consumer)} action.
   */
  public sealed interface RequestConfig permits ScriptWriter.RequestConfig {

    /**
     * Sets the HTTP method to the specified value.
     *
     * @param value
     *        the HTTP method
     */
    void method(Script.Method value);

    void url(String value);

    void url(StringQuery value);

    void onSuccess(Callback callback);

  }

  /**
   * Causes the browser to perform a HTTP request.
   */
  void request(Consumer<RequestConfig> config);

  /**
   * Causes the event handling to stop at the current HTML element.
   */
  void stopPropagation();

}