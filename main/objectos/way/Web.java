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
import java.util.Map;
import objectos.lang.object.Check;
import objectos.notes.Note1;
import objectos.notes.NoteSink;

/**
 * The <strong>Objectos Web</strong> main class.
 */
public final class Web {

  // types

  /**
   * An action to be executed as a response to an web request.
   */
  public interface Action {

    /**
     * Executes this action.
     */
    void execute();

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

  /**
   * Option: map file extension names to content type (media type) values as
   * defined by the specified properties string.
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
   * Option: set the note sink to the specified instance.
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
   * Option: serve the contents of the specified directory as if they were at
   * the root of the web server.
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
   * Option: serve at the specified path name a file with the specified
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