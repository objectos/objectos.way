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
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

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
    public sealed interface Options permits WebResourcesBuilder {

      /**
       * Adds the contents of the specified directory to this configuration.
       * The contents of the directory will be recursively copied to the root of
       * the web resources instance during its creation.
       *
       * @param directory
       *        the directory whose contents are to be copied
       */
      void addDirectory(Path directory);

      /**
       * Adds the specified media to this configuration.
       *
       * @param pathName
       *        the absolute path of the file to be created. It must start
       *        with a '/' character.
       * @param media
       *        the media object whose contents is to be copied
       */
      void addMedia(String pathName, Media media);

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
     * Creates a new {@code Resources} instance with the specified options.
     *
     * @param options
     *        allows for setting the options
     *
     * @return a newly created {@code Resources} instance with the specified
     *         options
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    static Resources create(Consumer<? super Options> options) throws IOException {
      final WebResourcesBuilder builder;
      builder = new WebResourcesBuilder();

      options.accept(builder);

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
     * Reconfigures this {@code Resources} instance with the specified options.
     *
     * @param options
     *        allows for setting the options
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void reconfigure(Consumer<Options> options) throws IOException;

    /**
     * Creates a new file at the specified server path with the contents of the
     * specified media object.
     *
     * @param pathName
     *        the server path of the file to be created. It must start with a
     *        '/' character.
     * @param media
     *        the media object whose contents is to be copied
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void writeMedia(String pathName, Media media) throws IOException;

  }

  /**
   * Manages security related information of an web application.
   */
  public sealed interface Secure permits WebSecure {

    /**
     * Configures the creation of a {@code Web.Secure} instance.
     */
    public sealed interface Options permits WebSecureBuilder {

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
       * Discards empty sessions, during a clean up operation, whose last access
       * time is greater than the specified duration.
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
     * Creates a new {@code Web.Secure} instance with the specified options.
     *
     * @param options
     *        allows for setting the options
     *
     * @return a newly created {@code Web.Secure} instance with the specified
     *         options
     */
    static Secure create(Consumer<? super Options> options) {
      WebSecureBuilder builder;
      builder = new WebSecureBuilder();

      options.accept(builder);

      return builder.build();
    }

    /**
     * Creates and immediately stores a new session instance.
     *
     * @return a newly created session instance.
     */
    Session createSession();

    /**
     * Returns the CSRF token associated to the specified session, or returns a
     * newly created token. In either case the returned token will be associated
     * to the specified session.
     *
     * @param session
     *        the session to which the CSRF token is to be associated
     *
     * @return the CSRF already associated to the specified session, or a newly
     *         created token
     */
    Token ensureCsrfToken(Session session);

    /**
     * Returns the session associated to the specified request if one exists, or
     * returns a newly created session.
     *
     * @param http
     *        the HTTP exchange
     *
     * @return the session associated to the HTTP request or a newly created
     *         session
     */
    Session ensureSession(Http.Exchange http);

    /**
     * Returns the session associated to the specified request, or returns
     * {@code null} if a session could not be found.
     *
     * @param http
     *        the HTTP request message
     *
     * @return the session associated to the HTTP request or {@code null}
     */
    Session getSession(Http.Request http);

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
     * Creates a new {@code Session} object that is not associated to any
     * session store. The main use case for this is in testing.
     *
     * @return a newly created {@code Session} object.
     */
    static Session create() {
      return new WebSession(null);
    }

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

    void put(String name, Object value);

    void remove(String name);

    void invalidate();

  }

  public sealed interface Token permits WebToken {}

  private Web() {}

}