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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * The <strong>Objectos Script</strong> main interface, part of Objectos HTML.
 * It represents an action to be executed by the browser in the context of an
 * web application.
 */
public sealed interface Script permits ScriptWriter {

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

  public static byte[] getBytes() throws IOException {
    URL resource;
    resource = Script.class.getResource("script.js");

    if (resource == null) {
      throw new FileNotFoundException();
    }

    try (InputStream in = resource.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      return out.toByteArray();
    }
  }

  void delay(int ms, Consumer<Script> callback);

  void html(Html.Template template);

  /**
   * Performs a soft navigation to the URL specified by the {@code href}
   * attribute of this {@code <a>} HTML element.
   *
   * @return an action that performs a soft navigation.
   */
  void navigate();

  /**
   * Causes the event handling to stop at the current HTML element.
   *
   * @return an action that causes the event handling to stop at the current
   *         HTML element.
   */
  void stopPropagation();

  void toggleClass(Html.Id id, String className);

  void toggleClass(Html.Id id, String class1, String class2);

  void submit(Html.Id id);

}