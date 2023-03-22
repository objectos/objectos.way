/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import objectos.html.HtmlTemplate;
import objectos.html.HtmlTemplate.Visitor;

/**
 * TODO
 *
 * @since 0.5.0
 */
public final class HtmlSink2 extends HtmlPlayer2 {

  private PrettyPrintWriter2 prettyPrintWriter;

  private Writer2 writer;

  /**
   * Writes the specified template to the specified directory.
   *
   * @param template
   *        the template to be written
   *
   * @param directory
   *        the directory to be the parent or ancestor of the generated HTML
   *        file
   *
   * @throws IOException
   *         if an I/O error occurs while writing the template
   *
   * @throws IllegalArgumentException
   *         if the specified template does not define a pathname of if the
   *         template's pathname resolves to a file that is not a descendant of
   *         the specified directory
   *
   * @since 0.5.1
   */
  public final void toDirectory(HtmlTemplate template, Path directory) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  /**
   * Writes the specified template to the end of the specified
   * {@code StringBuilder} instance.
   *
   * @param template
   *        the template to be written
   *
   * @param out
   *        where this template will be appended to
   *
   * @since 0.5.1
   */
  public final void toStringBuilder(HtmlTemplate template, StringBuilder out) {
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(out, "out == null");

    record(template);

    try {
      writeImpl(out);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  /**
   * Visits the specified template using the specified visitor.
   *
   * @param template
   *        the template to be visited
   *
   * @param visitor
   *        the visitor instance
   *
   * @since 0.5.1
   */
  public final void toVisitor(HtmlTemplate template, Visitor visitor) {
    throw new UnsupportedOperationException("Implement me");
  }

  private PrettyPrintWriter2 prettyPrintWriter() {
    if (prettyPrintWriter == null) {
      prettyPrintWriter = new PrettyPrintWriter2();
    }

    return prettyPrintWriter;
  }

  private Writer2 thisWriter() {
    if (writer == null) {
      writer = prettyPrintWriter();
    }

    return writer;
  }

  private void writeImpl(Appendable out) throws IOException {
    var writer = thisWriter();

    writer.out = out;

    play(writer);

    writer.throwIfNecessary();
  }

}