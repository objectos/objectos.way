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
   * Returns the source code of the Objectos Way JS library.
   *
   * @return the source code
   */
  public static String getSource() {
    return ScriptSource.get();
  }

  // types

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

    void submit();

    void toggleClass(String className);

  }

  Element element();

  Element elementById(Html.Id id);

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