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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import objectos.lang.object.Check;
import objectos.notes.Note1;
import objectos.notes.NoteSink;

/**
 * The <strong>Objectos Web</strong> main class.
 */
public final class Web {

  // types

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
  public interface Paginator extends Sql.PageProvider {

    /**
     * The current page.
     *
     * @return the current page
     */
    @Override
    Sql.Page page();

    int firstItem();

    int lastItem();

    int totalCount();

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
    public sealed interface Option {}

    /**
     * A debug note indicating that a file has been created.
     */
    Note1<Path> CREATED = Note1.debug(Resources.class, "File created");

    /**
     * An error note indicating that a request has been denied because an
     * attempted filesystem traversal has been detected.
     */
    Note1<Path> TRAVERSAL = Note1.error(Resources.class, "Traversal detected");

  }

  private non-sealed interface WebResourcesOption extends Resources.Option {

    void accept(WebResources.Builder builder);

  }

  /**
   * An web session uniquely identifies the user of an application.
   */
  public sealed interface Session permits WebSession {

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
     * A store configuration option.
     */
    public sealed interface Option {}

    void cleanUp();

    /**
     * Creates and immediately stores a new session instance.
     *
     * @return a newly created session instance.
     */
    Session createNext();

    void filter(Http.Exchange http);

    Session get(String id);

    /**
     * Returns a Set-Cookie header value for the specified session ID.
     *
     * @param id
     *        the id of the session
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

  non-sealed static abstract class WebStoreOption implements Store.Option {

    abstract void accept(WebStore.Builder builder);

  }

  private Web() {}

  /**
   * Creates a new paginator instance.
   *
   * @param target the request target
   * @param pageAttrName the name of the page query parameter
   * @param pageSize the maximum number of elements in each page
   * @param totalCount the number of elements in all of the pages
   *
   * @return a new paginator instance
   */
  public static Paginator createPaginator(Http.Request.Target target, String pageAttrName, int pageSize, int totalCount) {
    Check.notNull(target, "target == null");
    Check.notNull(pageAttrName, "pageAttrName == null");
    Check.argument(pageSize > 0, "pageSize must be positive");
    Check.argument(totalCount >= 0, "totalCount must be equal or greater than zero");

    return WebPaginator.of(target, pageAttrName, pageSize, totalCount);
  }

  /**
   * Creates a new resources instance configured with the specified options.
   *
   * @param options
   *        configures the creation of the new resources instance
   *
   * @return a newly created resources instance
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  public static Resources createResources(Resources.Option... options) throws IOException {
    WebResources.Builder builder;
    builder = new WebResources.Builder();

    for (int i = 0; i < options.length; i++) {
      Resources.Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      WebResourcesOption option;
      option = (WebResourcesOption) o;

      option.accept(builder);
    }

    return builder.build();
  }

  public static Session createSession(String id) {
    return new WebSession(id);
  }

  /**
   * Creates a new session store with the specified configuration options.
   */
  public static Store createStore(Store.Option... options) {
    WebStore.Builder builder;
    builder = new WebStore.Builder();

    for (int i = 0, len = options.length; i < len; i++) {
      Store.Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      WebStoreOption option;
      option = (WebStoreOption) o;

      option.accept(builder);
    }

    return builder.build();
  }

  /**
   * Store option: use the specified {@code clock} when setting session
   * instances time related values.
   *
   * @param value
   *        the clock instance to use
   *
   * @return a new store configuration option
   */
  public static Store.Option clock(Clock value) {
    Check.notNull(value, "value == null");

    return new WebStoreOption() {
      @Override
      final void accept(WebStore.Builder builder) {
        builder.clock = value;
      }
    };
  }

  /**
   * Store option: use the specified {@code name} when setting the client
   * session cookie.
   *
   * @param name
   *        the cookie name to use
   *
   * @return a new store configuration option
   */
  public static Store.Option cookieName(String name) {
    Check.notNull(name, "name == null");

    return new WebStoreOption() {
      @Override
      final void accept(WebStore.Builder builder) {
        builder.cookieName = name;
      }
    };
  }

  /**
   * Store option: sets the session cookie Path attribute to the specified
   * value.
   *
   * @param path
   *        the value of the Path attribute
   *
   * @return a new store configuration option
   */
  public static Store.Option cookiePath(String path) {
    Check.notNull(path, "path == null");

    return new WebStoreOption() {
      @Override
      final void accept(WebStore.Builder builder) {
        builder.cookiePath = path;
      }
    };
  }

  /**
   * Store option: sets the session cookie Max-Age attribute to the
   * specified value.
   *
   * @param duration
   *        the value of the Max-Age attribute
   *
   * @return a new store configuration option
   */
  public static Store.Option cookieMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("maxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("maxAge must not be negative");
    }

    return new WebStoreOption() {
      @Override
      final void accept(WebStore.Builder builder) {
        builder.cookieMaxAge = duration;
      }
    };
  }

  /**
   * Store option: discards empty sessions, during a {@link #cleanUp()}
   * operation, whose last access time is greater than the specified duration.
   *
   * @param duration
   *        the duration value
   *
   * @return a new store configuration option
   */
  public static Store.Option emptyMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("emptyMaxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("emptyMaxAge must not be negative");
    }

    return new WebStoreOption() {
      @Override
      final void accept(WebStore.Builder builder) {
        builder.emptyMaxAge = duration;
      }
    };
  }

  /**
   * Store option: use the specified {@link Random} instance for generating
   * session IDs.
   *
   * @param random
   *        the {@link Random} instance to use
   *
   * @return a new store configuration option
   */
  public static Store.Option random(Random random) {
    Check.notNull(random, "random == null");

    return new WebStoreOption() {
      @Override
      final void accept(WebStore.Builder builder) {
        builder.random = random;
      }
    };
  }

  /**
   * Resources option: map file extension names to content type (media type)
   * values as defined by the specified properties string.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Resources.Option o;
   * o = Web.contentTypes("""
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
   *        a string with a file extension / content-type mapping in each line
   *
   * @return a new configuration option
   */
  public static Resources.Option contentTypes(String propertiesString) {
    Map<String, String> map;
    map = Util.parsePropertiesMap(propertiesString);

    return new WebResourcesOption() {
      @Override
      public final void accept(WebResources.Builder b) {
        b.contentTypes = map;
      }
    };
  }

  /**
   * Resources option: set the note sink to the specified instance.
   *
   * @param noteSink
   *        the note sink instance
   *
   * @return a new configuration option
   */
  public static Resources.Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new WebResourcesOption() {
      @Override
      public final void accept(WebResources.Builder b) {
        b.noteSink = noteSink;
      }
    };
  }

  /**
   * Resources option: serve the contents of the specified directory as if they
   * were at the root of the web server.
   *
   * @param directory
   *        the directory whose contents are to be served
   *
   * @return a new configuration option
   */
  public static Resources.Option serveDirectory(Path directory) {
    Check.argument(Files.isDirectory(directory), "Path " + directory + " does not represent a directory");

    return new WebResourcesOption() {
      @Override
      public final void accept(WebResources.Builder b) {
        b.directories.add(directory);
      }
    };
  }

  /**
   * Resources option: serve at the specified path name a file with the
   * specified
   * contents. The behavior of this option is not specified if the
   * contents of the array is changed while this option is configuring the
   * creation of a resources instance.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Resources.Option o;
   * o = Web.serveFile("/robots.txt", Files.readAllBytes(robotsPath));</pre>
   *
   * @param pathName
   *        the path of the resource to be served. It must start with a '/'
   *        character.
   *
   * @param contents
   *        the contents of the resource to be served.
   *
   * @return a new configuration option
   */
  public static Resources.Option serveFile(String pathName, byte[] contents) {
    Http.Request.Target target;
    target = Http.parseRequestTarget(pathName);

    String query;
    query = target.rawQuery();

    if (query != null) {
      throw new IllegalArgumentException("Found query component in path name: " + pathName);
    }

    String path;
    path = target.path();

    Check.notNull(contents, "contents == null");

    return new WebResourcesOption() {
      @Override
      public final void accept(WebResources.Builder b) {
        b.serveFile(path, contents);
      }
    };
  }

  static Resources.Option rootDirectory(Path directory) {
    Check.argument(Files.isDirectory(directory), "Path " + directory + " does not represent a directory");

    return new WebResourcesOption() {
      @Override
      public final void accept(WebResources.Builder b) {
        b.rootDirectory = directory;
      }
    };
  }

}