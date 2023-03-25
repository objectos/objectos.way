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
package objectos.html;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import objectos.html.internal.HtmlPlayer;
import objectos.html.internal.PrettyPrintWriter;
import objectos.html.internal.Writer;
import objectos.html.pseudom.DocumentProcessor;

/**
 * TODO
 *
 * @since 0.5.0
 */
public final class HtmlSink extends HtmlPlayer {

  private PrettyPrintWriter prettyPrintWriter;

  private Writer writer;

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
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(directory, "directory == null");

    record(template);

    var pathName = $pathName();

    if (pathName == null) {
      throw new IllegalArgumentException("""
      Cannot write template: no pathname was defined for this template.

      Please use the `pathName` instruction to set a template's pathname.
      """
      );
    }

    // remove leading slash
    pathName = pathName.substring(1);

    var resolved = directory.resolve(pathName).normalize();

    if (!resolved.startsWith(directory)) {
      throw new IllegalArgumentException("""
      Cannot write template: pathname resolved to a path outside of the directory.
      """
      );
    }

    var parent = resolved.getParent();

    Files.createDirectories(parent);

    try (var writer = Files.newBufferedWriter(resolved)) {
      writeImpl(writer);
    }
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
   * Process the specified template with the specified processor.
   *
   * @param template
   *        the template to be processed
   *
   * @param processor
   *        the processor that will consume the template
   *
   * @since 0.5.3
   */
  public final void toProcessor(HtmlTemplate template, DocumentProcessor processor) {
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(processor, "processor == null");

    record(template);

    play(processor);
  }

  private PrettyPrintWriter prettyPrintWriter() {
    if (prettyPrintWriter == null) {
      prettyPrintWriter = new PrettyPrintWriter();
    }

    return prettyPrintWriter;
  }

  private Writer thisWriter() {
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