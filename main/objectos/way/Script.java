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
  public sealed interface Action extends Lang.MediaObject permits ScriptAction {

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
    default byte[] mediaBytes() {
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

  public enum Method {

    GET,

    POST;

  }

  public sealed interface StringLike permits ScriptWriter.ElementMethodInvocation {}

  // queries

  public sealed interface Element permits ScriptWriter.ElementQuery {

    StringLike getAttribute(String name);

  }

  Element element();

  // actions

  void delay(int ms, Consumer<Script> callback);

  void html(Html.Template template);

  /**
   * Performs a soft navigation to the URL specified by the {@code href}
   * attribute of this {@code <a>} HTML element.
   */
  void navigate();

  public sealed interface RequestConfig permits ScriptWriter.RequestConfig {

    void method(Script.Method method);

    void url(String value);

    void url(StringLike value);

    void onSuccess(Consumer<Script> config);

  }

  /**
   * Performs a soft navigation to the URL specified by the {@code href}
   * attribute of this {@code <a>} HTML element.
   */
  void request(Consumer<RequestConfig> config);

  /**
   * Sets the specified attribute to the given value on the HTML element
   * identified by the provided ID.
   *
   * @param id
   *        the ID of the target HTML element
   * @param name
   *        the name of the attribute to set
   * @param value
   *        the value to assign to the attribute
   */
  void setAttribute(Html.Id id, String name, String value);

  /**
   * Causes the event handling to stop at the current HTML element.
   */
  void stopPropagation();

  void toggleClass(Html.Id id, String className);

  void toggleClass(Html.Id id, String class1, String class2);

  void submit(Html.Id id);

}