/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import objectos.internal.Util;
import objectos.lang.Stage;
import objectos.way.Note;

final class HttpHost0Builder implements HttpHost, HttpStaticFiles {

  private Map<String, String> contentTypes = Map.of();

  private final String defaultContentType = "application/octet-stream";

  private Set<Path> directories = Set.of();

  private HttpHandler handler = HttpHandler.notFound();

  private String name;

  Note.Sink noteSink;

  private Path rootDirectory;

  int serverPort;

  Path serverRoot;

  private HttpSessionLoader sessionLoader = (_, _) -> null;

  Stage stage;

  // ##################################################################
  // # BEGIN: Build
  // ##################################################################

  public HttpHost5Pojo build() throws IOException {
    // name
    final HttpHost1Name nameBuilder;
    nameBuilder = new HttpHost1Name(name, serverPort);

    final String name;
    name = nameBuilder.get();

    // rootDirectory
    if (rootDirectory == null) {
      rootDirectory = Files.createTempDirectory(serverRoot, name);
    }

    for (Path source : directories) {
      final HttpHost2CopyDirectory copy;
      copy = new HttpHost2CopyDirectory(noteSink, source, rootDirectory);

      Files.walkFileTree(source, copy);
    }

    // static files
    final HttpHost3StaticFiles staticFiles;
    staticFiles = new HttpHost3StaticFiles(contentTypes, defaultContentType, rootDirectory);

    // static files writer
    final HttpStaticFilesWriter staticFilesWriter;
    staticFilesWriter = switch (stage) {
      case DEV -> new HttpStaticFilesWriter1Dev();

      case null, default -> new HttpStaticFilesWriter0RootDirectory(rootDirectory);
    };

    // handler
    final HttpHandler hostHandler;
    hostHandler = new HttpHost4Handler(handler, staticFiles);

    return new HttpHost5Pojo(hostHandler, name, sessionLoader, staticFilesWriter);
  }

  // ##################################################################
  // # END: Build
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpHost
  // ##################################################################

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void handler(HttpHandler value) {
    handler = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void sessionStore(HttpSessionStore value) {
    sessionLoader = (HttpSessionLoader) Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void stage(Stage value) {
    stage = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void staticFiles(Consumer<? super HttpStaticFiles> opts) {
    opts.accept(this);
  }

  final String name() {
    return name;
  }

  // ##################################################################
  // # END: HttpHost
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpStaticFiles
  // ##################################################################

  @Override
  public final void addDirectory(Path directory) {
    if (!Files.isDirectory(directory)) {
      final String msg;
      msg = "Path " + directory + " does not represent a directory";

      throw new IllegalArgumentException(msg);
    }

    if (directories.isEmpty()) {
      directories = new LinkedHashSet<>();
    }

    directories.add(directory);
  }

  @Override
  public final void contentTypes(String propertiesString) {
    final Map<String, String> map;
    map = Util.parsePropertiesMap(propertiesString);

    for (Map.Entry<String, String> entry : map.entrySet()) {
      final String extension;
      extension = entry.getKey();

      final String contentType;
      contentType = entry.getValue();

      register(extension, contentType);
    }
  }

  @Override
  public final void rootDirectory(Path value) {
    if (!Files.isDirectory(value)) {
      final String msg;
      msg = "Path " + value + " does not represent a directory";

      throw new IllegalArgumentException(msg);
    }

    rootDirectory = value;
  }

  final void serverStage(Stage value) {
    if (stage == null) {
      stage = value;
    }
  }

  private void register(String extension, String contentType) {
    if (extension.isEmpty()) {
      throw new IllegalArgumentException("File extension must not be empty");
    }

    if (contentType.isEmpty()) {
      throw new IllegalArgumentException("Content type must not be empty");
    }

    if (contentTypes.isEmpty()) {
      contentTypes = new HashMap<>();
    }

    final String previous;
    previous = contentTypes.putIfAbsent(extension, contentType);

    if (previous != null) {
      final String msg;
      msg = "Duplicate mapping for extension %s: %s, %s".formatted(extension, previous, contentType);

      throw new IllegalArgumentException(msg);
    }
  }

  // ##################################################################
  // # END: HttpStaticFiles
  // ##################################################################

}
