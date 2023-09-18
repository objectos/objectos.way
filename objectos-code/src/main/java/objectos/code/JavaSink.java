/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.code.internal.InternalInterpreter;
import objectos.code.internal.JavaSinkOfDirectory;
import objectos.code.internal.JavaSinkOfStringBuilder;
import objectos.code.internal.JavaSinkOption;
import objectos.lang.Check;

/**
 * The {@link JavaSink} class is responsible for evaluating a
 * {@link JavaTemplate} and generating the corresponding Java code.
 *
 * <p>
 * Instances of this class are not thread-safe.
 *
 * @since 0.4
 */
public abstract class JavaSink extends InternalInterpreter {

  /**
   * Represents an option for configuring a {@link JavaSink} instance.
   */
  public abstract static sealed class Option permits JavaSinkOption {

    protected Option() {}

    /**
     * Configures the provided {@code JavaSink} instance.
     *
     * @param sink
     *        the {@code JavaSink} instance to configure
     */
    protected abstract void acceptOfDirectory(JavaSinkOfDirectory sink);

  }

  /**
   * Sole constructor
   */
  protected JavaSink() {}

  /**
   * Returns a {@link JavaSink} instance for generating Java source files at the
   * specified directory.
   *
   * <p>
   * When provided with a {@link JavaTemplate} instance, the returned
   * {@link JavaSink} will:
   *
   * <ul>
   * <li>create the required subdirectories to represent the template's package
   * hierarchy; and
   * <li>fail with an {@link IOException} if the file it is trying to generate
   * already exists. In other words, it will not overwrite any existing file; it
   * will fail with the exception instead.
   * </ul>
   *
   * @param directory
   *        the pathname of the directory where the files are to be generated.
   *
   * @return a {@link JavaSink} instance for generating Java source files.
   *
   * @throws IllegalArgumentException
   *         if the specified pathname represented by {@code directory} does not
   *         exist, exists but it is not a directory or if it cannot be
   *         determined whether it is a directory or not.
   */
  public static JavaSink ofDirectory(Path directory) {
    Check.argument(
      Files.isDirectory(directory),
      directory, " does not exist, exists but is not a directory, or could not be accessed."
    );

    return new JavaSinkOfDirectory(directory);
  }

  /**
   * Returns a {@link JavaSink} instance for generating Java source files at the
   * specified directory with the specified option.
   *
   * <p>
   * The {@code option} parameter can be used to modify this sink behavior when
   * the file it will generate already exists:
   *
   * <ul>
   * <li>the {@link JavaSink#overwriteExisting()} will cause this sink to
   * truncate and overwrite any existing file; and
   * <li>the {@link JavaSink#skipExisting()} will cause to silently skip the
   * existing file. As opposed to the default behavior which is to throw an
   * {@link IOException}.
   * </ul>
   *
   * @param directory
   *        the pathname of the directory where the files are to be generated.
   * @param option
   *        the option to configure the returned sink instance
   *
   * @return a configured {@link JavaSink} instance for generating Java source
   *         files.
   *
   * @throws IllegalArgumentException
   *         if the specified pathname represented by {@code directory} does not
   *         exist, exists but it is not a directory or if it cannot be
   *         determined whether it is a directory or not.
   */
  public static JavaSink ofDirectory(Path directory, Option option) {
    Check.argument(
      Files.isDirectory(directory),
      directory, " does not exist, exists but is not a directory, or could not be accessed."
    );

    var sink = new JavaSinkOfDirectory(directory);

    option.acceptOfDirectory(sink); // implicit null-check

    return sink;
  }

  /**
   * Returns a {@link JavaSink} instance which will append the generated source
   * code to the specified {@link StringBuilder} instance.
   *
   * <p>
   * In other words, the returned instance will begin writing at the current
   * position of the {@code StringBuilder}.
   *
   * @param output
   *        generated Java source code will be appended to the end of this
   *        {@code StringBuilder} instance
   *
   * @return a {@link JavaSink} instance for appending Java source code to the
   *         specified {@code StringBuilder} instance.
   */
  public static JavaSink ofStringBuilder(StringBuilder output) {
    Check.notNull(output, "output == null");

    return new JavaSinkOfStringBuilder(output);
  }

  /**
   * Configures a {@link JavaSink#ofDirectory(Path, Option) directory sink}
   * instance to overwrite any existing Java file.
   *
   * @return the option to overwrite any existing file
   */
  public static Option overwriteExisting() {
    return JavaSinkOption.OVERWRITE_EXISTING;
  }

  /**
   * Configures a {@link JavaSink#ofDirectory(Path, Option) directory sink}
   * instance to silently skip any existing Java file.
   *
   * @return the option to silently skip any existing file
   */
  public static Option skipExisting() {
    return JavaSinkOption.SKIP_EXISTING;
  }

  public void write(JavaTemplate template) throws IOException {
    eval(template);
  }

  protected final void eval(JavaTemplate template) {
    Check.notNull(template, "template == null");

    accept(template);

    compile();

    interpret();
  }

}