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
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * The <strong>Objectos Web</strong> main class.
 */
public final class Web {

  public sealed interface Form permits WebForm {

    sealed interface Error permits WebFormError {

      String message();

    }

    sealed interface Field permits WebFormField, TextInput {

      sealed interface Config {

        void label(String value);

        void id(String value);

        void name(String value);

        void required();

      }

      boolean isValid();

      String label();

      String id();

      String name();

      boolean required();

      List<? extends Error> errors();

      void setValue(Sql.Transaction trx);

    }

    sealed interface TextInput extends Field permits WebFormTextInput {

      sealed interface Config extends Field.Config permits WebFormTextInputConfig {

        void maxLength(int value);

      }

      @FunctionalInterface
      interface MaxLengthFormatter {

        String format(int maxLength, int actualLength);

      }

      String type();

      String value();

    }

    static Form of(FormSpec spec) {
      return (Form) spec;
    }

    boolean isValid();

    String action();

    List<? extends Field> fields();

  }

  public sealed interface FormSpec permits WebForm {

    sealed interface Config permits WebFormConfig {

      void action(String value);

      void textInput(Consumer<Web.Form.TextInput.Config> config);

    }

    static FormSpec create(Consumer<Config> config) {
      WebFormConfig builder;
      builder = new WebFormConfig();

      config.accept(builder);

      return builder.build();
    }

    Form parse(Http.Exchange http);

  }

  /**
   * The parsed and decoded body of a {@code application/x-www-form-urlencoded}
   * HTTP message.
   */
  public sealed interface FormData permits WebFormData {

    /**
     * Parse the body of the specified HTTP request as if it is the body of a
     * {@code application/x-www-form-urlencoded} HTTP message.
     *
     * @param http
     *        the HTTP exchange to parse
     *
     * @throws UncheckedIOException
     *         if an I/O error occurs while reading the body
     */
    static FormData parse(Http.Exchange http) throws UncheckedIOException, Http.UnsupportedMediaTypeException {
      return WebFormData.parse(http);
    }

    /**
     * Parse the specified request body as if it is the body of a
     * {@code application/x-www-form-urlencoded} HTTP message.
     *
     * @param body
     *        the request body to parse
     *
     * @throws UncheckedIOException
     *         if an I/O error occurs while reading the body
     */
    static FormData parseRequestBody(Http.RequestBody body) throws UncheckedIOException {
      return WebFormData.parse(body);
    }

    /**
     * Returns the names of all of the fields contained in this form data
     *
     * @return the names of all of the fields contained in this form data
     */
    Set<String> names();

    /**
     * Returns the first decoded value associated to the specified field name or
     * {@code null} if the field is not present.
     *
     * @param name
     *        the field name
     *
     * @return the first decoded value or {@code null}
     */
    String get(String name);

    /**
     * Returns a list containing all of the decoded values associated to the
     * specified field name. This method returns an empty list if the field name
     * is not present. In other words, this method never returns {@code null}.
     *
     * @param name
     *        the field name
     *
     * @return a list containing all of the decoded values in encounter order.
     */
    List<String> getAll(String name);

    /**
     * Returns an {@code IntStream} of all of the values, converted to
     * {@code int}, associated to the specified field name. Any value that
     * cannot be converted to an {@code int} is mapped to the specified
     * {@code defaultValue} instead.
     *
     * @param name
     *        the field name
     * @param defaultValue
     *        the default value to use if it cannot be converted to an
     *        {@code int}
     *
     * @return an {@code IntStream} of the values associated to the field name
     */
    default IntStream getAllAsInt(String name, int defaultValue) {
      return getAll(name).stream().mapToInt(s -> {
        try {
          return Integer.parseInt(s);
        } catch (NumberFormatException expected) {
          return defaultValue;
        }
      });
    }

    /**
     * Returns a {@code LongStream} of all of the values, converted to
     * {@code long}, associated to the specified field name. Any value that
     * cannot be converted to an {@code long} is mapped to the specified
     * {@code defaultValue} instead.
     *
     * @param name
     *        the field name
     * @param defaultValue
     *        the default value to use if it cannot be converted to an
     *        {@code long}
     *
     * @return an {@code LongStream} of the values associated to the field name
     */
    default LongStream getAllAsLong(String name, long defaultValue) {
      return getAll(name).stream().mapToLong(s -> {
        try {
          return Long.parseLong(s);
        } catch (NumberFormatException expected) {
          return defaultValue;
        }
      });
    }

    /**
     * Returns the first value of the form field with the specified
     * {@code name}, converted to an {@code int} primitive. If the field is not
     * present in the form data or if its value cannot be converted to an
     * {@code int}, the specified {@code defaultValue} is returned.
     *
     * @param name
     *        the field name
     *
     * @param defaultValue
     *        the default value to return if the field is not present or cannot
     *        be converted to an {@code int}
     *
     * @return the value of the form field as an {@code int}, or
     *         {@code defaultValue} if the field is absent or cannot be
     *         converted
     */
    default int getAsInt(String name, int defaultValue) {
      String maybe;
      maybe = get(name);

      if (maybe == null) {
        return defaultValue;
      }

      try {
        return Integer.parseInt(maybe);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    }

    /**
     * Returns the first value of the form field with the specified
     * {@code name}, converted to a {@code long} primitive. If the field is not
     * present in the form data or if its value cannot be converted to a
     * {@code long}, the specified {@code defaultValue} is returned.
     *
     * @param name
     *        the field name
     *
     * @param defaultValue
     *        the default value to return if the field is not present or cannot
     *        be converted to a {@code long}
     *
     * @return the value of the form field as an {@code long}, or
     *         {@code defaultValue} if the field is absent or cannot be
     *         converted
     */
    default long getAsLong(String name, long defaultValue) {
      String maybe;
      maybe = get(name);

      if (maybe == null) {
        return defaultValue;
      }

      try {
        return Long.parseLong(maybe);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    }

    /**
     * Returns the first decoded value associated to the specified key or
     * the specified {@code defaultValue} if the key is not present.
     *
     * @param key
     *        the key to search for
     * @param defaultValue
     *        the value to return if the key is not present
     *
     * @return the first decoded value or the {@code defaultValue}
     */
    String getOrDefault(String key, String defaultValue);

    /**
     * Returns the number of distinct keys.
     *
     * @return the number of distinct keys
     */
    int size();

  }

  /**
   * Allows for pagination of data tables in an web application.
   */
  public sealed interface Paginator extends Sql.PageProvider permits WebPaginator {

    /**
     * Configures the creation of a {@code Paginator} instance.
     */
    sealed interface Config permits WebPaginatorConfig {

      /**
       * Sets the page size to the specified value.
       *
       * @param value
       *        the page size
       *
       * @throws IllegalArgumentException
       *         if the value is equal to or lesser than {@code 0}
       */
      void pageSize(int value);

      /**
       * Sets the query parameter name to be used in the pagination.
       *
       * @param value
       *        the parameter name
       *
       * @throws IllegalArgumentException
       *         if {@code value} is empty or blank
       */
      void parameterName(String value);

      /**
       * Sets the request target for pagination.
       *
       * @param value
       *        the {@code Http.RequestTarget} instance
       */
      void requestTarget(Http.RequestTarget value);

      /**
       * Sets the total number of rows to be paginated.
       *
       * @param value
       *        the total row count
       *
       * @throws IllegalArgumentException
       *         if {@code value} is less than {@code 0}
       */
      void rowCount(int value);

    }

    /**
     * Creates a new paginator instance with the specified configuration.
     *
     * @param config
     *        the paginator configuration
     *
     * @return a new paginator instance
     */
    static Paginator create(Consumer<Config> config) {
      WebPaginatorConfig builder;
      builder = new WebPaginatorConfig();

      config.accept(builder);

      return builder.build();
    }

    /**
     * Returns the current page.
     *
     * @return the current page
     */
    @Override
    Sql.Page page();

    /**
     * Returns the index (1-based) of the first row in the current page.
     *
     * @return the index (1-based) of the first row in the current page.
     */
    int firstRow();

    /**
     * Returns the index (1-based) of the last row in the current page.
     *
     * @return the index (1-based) of the last row in the current page.
     */
    int lastRow();

    /**
     * Returns the total number of rows.
     *
     * @return the total number of rows.
     */
    int rowCount();

    boolean hasNext();

    boolean hasPrevious();

    String nextHref();

    String previousHref();

  }

  /**
   * An HTTP handler for serving the static files of an web application.
   */
  public sealed interface Resources extends AutoCloseable, Http.Handler permits WebResources {

    /**
     * Configures the creation of an Web Resources instance.
     */
    public sealed interface Config permits WebResourcesConfig {

      /**
       * Adds the specified directory to this configuration. The contents of the
       * directory will be recursively copied to the root of the web resources
       * instance during its creation.
       *
       * @param directory
       *        the directory whose contents are to be copied
       */
      void addDirectory(Path directory);

      /**
       * Adds the specified binary file to this configuration.
       *
       * @param pathName
       *        the absolute path of the text file to be created. It must start
       *        with a '/' character.
       * @param contents
       *        the file contents
       */
      void addBinaryFile(String pathName, byte[] contents);

      /**
       * Adds the specified text file to this configuration.
       *
       * @param pathName
       *        the absolute path of the text file to be created. It must start
       *        with a '/' character.
       * @param contents
       *        the text to be written.
       * @param charset
       *        the charset to use for encoding
       */
      void addTextFile(String pathName, CharSequence contents, Charset charset);

      /**
       * Map file extension names to content type (media type) values as defined
       * by the specified properties string.
       *
       * <p>
       * A typical usage is:
       *
       * <pre>
       * config.contentTypes("""
       *     .css: text/stylesheet; charset=utf-8
       *     .js: text/javascript; charset=utf-8
       *     .jpg: image/jpeg
       *     .woff: font/woff2
       *     """);</pre>
       *
       * <p>
       * Which causes:
       *
       * <ul>
       * <li>files ending in {@code .css} to be served with
       * {@code Content-Type: text/stylesheet; charset=utf-8};</li>
       * <li>files ending in {@code .js} to be served with
       * {@code Content-Type: text/javascript; charset=utf-8};</li>
       * <li>files ending in {@code .jpg} to be served with
       * {@code Content-Type: image/jpeg};</li>
       * <li>files ending in {@code .woff} to be served with
       * {@code Content-Type: font/woff2};</li>
       * </ul>
       *
       * @param propertiesString
       *        a string with a file extension / content-type mapping in each
       *        line
       */
      void contentTypes(String propertiesString);

      /**
       * Set the note sink to the specified instance.
       *
       * @param noteSink
       *        the note sink instance
       */
      void noteSink(Note.Sink noteSink);

    }

    /**
     * Creates a new {@code Resources} instance with the specified
     * configuration.
     *
     * @param config
     *        the configuration
     *
     * @return a newly created {@code Resources} instance with the specified
     *         configuration
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    static Resources create(Consumer<Config> config) throws IOException {
      final WebResourcesConfig builder;
      builder = new WebResourcesConfig();

      config.accept(builder);

      final WebResourcesKernel kernel;
      kernel = builder.build();

      return new WebResources(kernel);
    }

    default void handlePath(Http.Routing.OfPath path) {
      path.handler(this);
    }

    /**
     * Deletes the file at the specified path if it exists.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    boolean deleteIfExists(String path) throws IOException;

    /**
     * Reconfigures this {@code Resources} instance with the specified
     * configuration.
     *
     * @param config
     *        the configuration
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void reconfigure(Consumer<Config> config) throws IOException;

    /**
     * Creates a new file at the specified server path with the specified
     * binary content.
     *
     * @param pathName
     *        the server path of the file to be created. It must start with a
     *        '/' character.
     * @param contents
     *        the contents of the new file
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void write(String pathName, byte[] contents) throws IOException;

    /**
     * Creates a new file at the specified server path with the contents of the
     * specified media object.
     *
     * @param pathName
     *        the server path of the file to be created. It must start with a
     *        '/' character.
     * @param object
     *        the media object whose contents is to be copied
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void writeMedia(String pathName, Lang.Media object) throws IOException;

    /**
     * Creates a new file at the specified server path with the specified text
     * content.
     *
     * @param pathName
     *        the server path of the file to be created. It must start with a
     *        '/' character.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void writeString(String pathName, CharSequence contents, Charset charset) throws IOException;

  }

  /**
   * Manages security related information of an web application.
   */
  public sealed interface Secure permits WebSecure {

    /**
     * Configures the creation of a {@code Secure} instance.
     */
    public sealed interface Config permits WebSecureConfig {

      /**
       * Use the specified {@code clock} when setting time related values.
       *
       * @param value
       *        the clock instance to use
       */
      void clock(Clock value);

      /**
       * Use the specified {@code name} when setting the client session cookie.
       *
       * @param name
       *        the cookie name to use
       */
      void cookieName(String name);

      /**
       * Sets the session cookie {@code Path} attribute to the specified value.
       *
       * @param path
       *        the session cookie {@code Path} attribute value
       */
      void cookiePath(String path);

      /**
       * Sets the session cookie {@code Max-Age} attribute to the specified
       * value.
       *
       * @param duration
       *        the session cookie {@code Max-Age} attribute value
       */
      void cookieMaxAge(Duration duration);

      /**
       * Sets the session cookie {@code Secure} attribute to the specified
       * value.
       *
       * @param value
       *        the session cookie {@code Secure} attribute value
       */
      void cookieSecure(boolean value);

      /**
       * Discards empty sessions, during a {@link Store#cleanUp()}
       * operation, whose last access time is greater than the specified
       * duration.
       *
       * @param duration
       *        the duration value
       */
      void emptyMaxAge(Duration duration);

      /**
       * Use the specified {@link RandomGenerator} instance for generating token
       * values.
       *
       * @param value
       *        the {@link RandomGenerator} instance to use
       */
      void randomGenerator(RandomGenerator value);

    }

    /**
     * Creates a new session store with the specified configuration.
     *
     * @param config
     *        the session store configuration
     *
     * @return a newly created session store with the specified
     *         configuration
     */
    static Secure create(Consumer<Config> config) {
      WebSecureConfig builder;
      builder = new WebSecureConfig();

      config.accept(builder);

      return builder.build();
    }

    /**
     * Creates and immediately stores a new session instance.
     *
     * @return a newly created session instance.
     */
    Session createSession();

    /**
     * Returns the session associated to the specified request; or returns
     * {@code null} if a session could not be found.
     *
     * @param request
     *        the HTTP request message
     *
     * @return the session associated to the HTTP request or {@code null}
     */
    Session getSession(Http.Request request);

    /**
     * Returns a new {@link Http.SetCookie} instance for the specified session.
     *
     * @param session
     *        the session
     *
     * @return a newly created {@link Http.SetCookie} instance
     */
    Http.SetCookie setCookie(Session session);

  }

  /**
   * An web session uniquely identifies the user of an application.
   */
  public sealed interface Session permits WebSession {

    /**
     * Returns the object associated to the specified class instance, or
     * {@code null} if there's no object associated.
     *
     * @param <T> the type of the object
     *
     * @param type
     *        the class instance to search for
     *
     * @return the object associated or {@code null} if there's no object
     *         associated
     */
    <T> T get(Class<T> type);

    /**
     * Returns the object associated to the specified name, or {@code null} if
     * there's no object associated.
     *
     * @param name
     *        the name to search for
     *
     * @return the object associated or {@code null} if there's no object
     *         associated
     */
    Object get(String name);

    <T> Object put(Class<T> type, T value);

    Object put(String name, Object value);

    Object remove(String name);

    void invalidate();

  }

  public sealed interface Token permits WebToken {}

  private Web() {}

}