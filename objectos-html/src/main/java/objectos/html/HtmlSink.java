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
import objectos.html.HtmlTemplate.Visitor;
import objectos.html.internal.HtmlPlayer;
import objectos.html.internal.MinifiedWriter;
import objectos.html.io.HtmlEscape;
import objectos.lang.Check;

/**
 * TODO
 *
 * @since 0.5.0
 */
public final class HtmlSink extends HtmlPlayer {

  /**
   * Base {@link Visitor} implementation suitable for writing HTML files.
   *
   * @since 0.5.1
   */
  public abstract static class Writer implements Visitor {

    private IOException ioException;

    private Appendable out;

    protected final void escaped(String value) {
      if (ioException != null) {
        return;
      }

      try {
        HtmlEscape.to(value, out);
      } catch (IOException e) {
        ioException = e;
      }
    }

    protected final void write(char c) {
      if (ioException != null) {
        return;
      }

      try {
        out.append(c);
      } catch (IOException e) {
        ioException = e;
      }
    }

    protected final void write(String s) {
      if (ioException != null) {
        return;
      }

      try {
        out.append(s);
      } catch (IOException e) {
        ioException = e;
      }
    }

    final void throwIfNecessary() throws IOException {
      if (ioException == null) {
        return;
      }

      var toThrow = ioException;

      ioException = null;

      throw toThrow;
    }

  }

  private MinifiedWriter minifiedWriter;

  private Writer writer;

  public final void appendTo(HtmlTemplate template, Appendable out) throws IOException {
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(out, "out == null");

    record(template);

    writeImpl(out);
  }

  /**
   * Instructs this sink to use the minified writer.
   *
   * @return this {@code HtmlSink} instance
   *
   * @since 0.5.1
   */
  public final HtmlSink minified() {
    writer = minifiedWriter();

    return this;
  }

  /**
   * Writes the specified template to the specified directory.
   *
   * @param template
   *        the template to be written
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
    Check.argument(Files.isDirectory(directory), directory, " is not a directory");

    record(template);

    var pathName = $pathName();

    if (pathName == null) {
      throw new IllegalArgumentException("""
      Cannot write template: no pathname was defined for this template.

      Please use the `pathName` instruction to set a template's pathname.
      """
      );
    }

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
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(visitor, "visitor == null");

    record(template);

    play(visitor);
  }

  private MinifiedWriter minifiedWriter() {
    if (minifiedWriter == null) {
      minifiedWriter = new MinifiedWriter();
    }

    return minifiedWriter;
  }

  private Writer thisWriter() {
    if (writer == null) {
      writer = minifiedWriter();
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