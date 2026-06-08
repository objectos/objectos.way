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
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import objectos.internal.Util;

final class StaticFilesBuilder implements StaticFilesOptions {

  private Map<String, String> contentTypes = Map.of();

  @SuppressWarnings("unused")
  private String defaultContentType = "application/octet-stream";

  private Set<Path> directories = Set.of();

  public final StaticFiles build() throws IOException {
    final Path directory = null;

    final StaticFilesAttributes staticFilesAttributes;
    staticFilesAttributes = new StaticFilesAttributes(file -> Files.readAttributes(file, BasicFileAttributes.class));

    final long mask;
    mask = ThreadLocalRandom.current().nextLong();

    final StaticFilesETag staticFilesETag;
    staticFilesETag = new StaticFilesETag(mask);

    final StaticFilesExtension staticFilesExtension;
    staticFilesExtension = new StaticFilesExtension("*");

    final StaticFilesRoot staticFilesRoot;
    staticFilesRoot = new StaticFilesRoot(directory);

    final StaticFilesTypes staticFilesTypes;
    staticFilesTypes = new StaticFilesTypes(defaultContentType, contentTypes);

    return new StaticFiles(
        staticFilesAttributes,

        staticFilesETag,

        staticFilesExtension,

        staticFilesRoot,

        staticFilesTypes
    );
  }

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

  private void register(String extension, String contentType) {
    if (extension.isEmpty()) {
      throw new IllegalArgumentException("File extension must not be empty");
    }

    if (contentType.isEmpty()) {
      throw new IllegalArgumentException("Content type must not be empty");
    }

    if (extension.equals("*")) {

      defaultContentType = contentType;

    } else {

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
  }

}
