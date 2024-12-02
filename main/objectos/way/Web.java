/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The <strong>Objectos Web</strong> main class.
 */
public final class Web {

  public sealed interface Form permits WebForm {

    sealed interface Field {

      sealed interface Config {

        void label(String value);

        void id(String value);

        void name(String value);

      }

      String label();

      String id();

      String name();

    }

    sealed interface TextInput extends Field permits WebFormTextInput {

      sealed interface Config extends Field.Config permits WebFormTextInputConfig {

      }

      String type();

      String value();

    }

    static Form of(FormSpec spec) {
      return (Form) spec;
    }

    boolean isValid();

    String action();

    List<Field> fields();

  }

  public sealed interface FormSpec permits WebForm {

    sealed interface Config permits WebFormConfig {

      void action(String value);

      void useNameForId();

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
     * @return the first decoded value or {@code null}
     */
    List<String> getAll(String name);

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
     * Returns the number of distinct keys
     *
     * @return the number of distinct keys
     */
    int size();

  }

  /**
   * An abstract HTTP module suited for web applications.
   */
  public static abstract class Module extends WebModule {

    /**
     * Sole constructor.
     */
    protected Module() {}

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
       *
       * @return this object
       */
      Config contentTypes(String propertiesString);

      /**
       * Set the note sink to the specified instance.
       *
       * @param noteSink
       *        the note sink instance
       *
       * @return this object
       */
      Config noteSink(Note.Sink noteSink);

      /**
       * Serve the contents of the specified directory as if they were at the
       * root of the web server.
       *
       * @param directory
       *        the directory whose contents are to be served
       *
       * @return this object
       */
      Config serveDirectory(Path directory);

      /**
       * Serve at the specified path name a file with the specified
       * contents. The behavior of this option is not specified if the
       * contents of the array is changed while this option is configuring the
       * creation of a resources instance.
       *
       * <p>
       * A typical usage is:
       *
       * <pre>
       * config.serveFile("/robots.txt", Files.readAllBytes(robotsPath));</pre>
       *
       * @param pathName
       *        the path of the resource to be served. It must start with a '/'
       *        character.
       *
       * @param contents
       *        the contents of the resource to be served.
       *
       * @return this object
       */
      Config serveFile(String pathName, byte[] contents);

      /**
       * Set the root directory of the web resources to the specified path.
       *
       * @param value
       *        the path of the directory
       *
       * @return this object
       */
      Config rootDirectory(Path value);

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
    static Resources create(ThrowingConsumer<Config, IOException> config) throws IOException {
      WebResourcesConfig builder;
      builder = new WebResourcesConfig();

      config.accept(builder);

      return builder.build();
    }

    /**
     * Deletes the file at the specified path if it exists.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    boolean deleteIfExists(String path) throws IOException;

    /**
     * Creates a new file at the specified path with the specified text content.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void writeMediaObject(String path, Lang.MediaObject contents) throws IOException;

    /**
     * Creates a new file at the specified path with the specified text content.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void writeString(String path, CharSequence contents, Charset charset) throws IOException;

  }

  /**
   * An web session uniquely identifies the user of an application.
   */
  public sealed interface Session permits WebSession {

    /**
     * Creates a new web session with the specified identifier.
     *
     * @param id
     *        the session identifier
     *
     * @return a newly created session object
     */
    static Session create(String id) {
      return new WebSession(id);
    }

    /**
     * The identifier of this session.
     *
     * @return the identifier of this session.
     */
    String id();

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

  /**
   * Creates, stores and manages session instances.
   */
  public sealed interface Store permits WebStore {

    /**
     * Configures the creation of a {@code Store} instance.
     */
    public sealed interface Config permits WebStoreConfig {

      /**
       * Use the specified {@code clock} when setting session instances time
       * related values.
       *
       * @param value
       *        the clock instance to use
       *
       * @return this object
       */
      Config clock(Clock value);

      /**
       * Use the specified {@code name} when setting the client session cookie.
       *
       * @param name
       *        the cookie name to use
       *
       * @return this object
       */
      Config cookieName(String name);

      /**
       * Sets the session cookie Path attribute to the specified value.
       *
       * @param path
       *        the value of the Path attribute
       *
       * @return this object
       */
      Config cookiePath(String path);

      /**
       * Sets the session cookie Max-Age attribute to the specified value.
       *
       * @param duration
       *        the value of the Max-Age attribute
       *
       * @return this object
       */
      Config cookieMaxAge(Duration duration);

      /**
       * Discards empty sessions, during a {@link Store#cleanUp()}
       * operation, whose last access time is greater than the specified
       * duration.
       *
       * @param duration
       *        the duration value
       *
       * @return this object
       */
      Config emptyMaxAge(Duration duration);

      /**
       * Use the specified {@link Random} instance for generating session IDs.
       *
       * @param random
       *        the {@link Random} instance to use
       *
       * @return this object
       */
      Config random(Random random);

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
    static Store create(Consumer<Config> config) {
      WebStoreConfig builder;
      builder = new WebStoreConfig();

      config.accept(builder);

      return builder.build();
    }

    void cleanUp();

    /**
     * Creates and immediately stores a new session instance.
     *
     * @return a newly created session instance.
     */
    Session createNext();

    void filter(Http.Exchange http);

    /**
     * Returns the session with the specified session ID; or returns
     * {@code null} if the session does not exist.
     *
     * @param id
     *        the session ID
     *
     * @return the session with the specified session ID or {@code null}
     */
    Session get(String id);

    /**
     * Returns a Set-Cookie header value for the specified session ID.
     *
     * @param id
     *        the session ID
     *
     * @return the value of a Set-Cookie header for the specified session ID
     */
    String setCookie(String id);

    /**
     * Stores the specified {@code session} in this repository. If a session
     * instance with the same ID is already managed by this repository then the
     * existing session is replaced by the specified one.
     *
     * @param session
     *        the session instance to be stored
     *
     * @return the previously stored session instance or {@code null}
     */
    Session store(Session session);

  }

  @FunctionalInterface
  public interface ThrowingConsumer<T, E extends Exception> {

    void accept(T config) throws E;

  }

  private Web() {}

}