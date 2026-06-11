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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Note;

final class StaticFilesBuilder implements StaticFilesOptions {

  private Set<Path> directories = Set.of();

  private Function<BasicFileAttributes, String> etag;

  private long etagMask = ThreadLocalRandom.current().nextLong();

  private final Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private final StaticFilesTypesBuilder typesBuilder = new StaticFilesTypesBuilder("application/octet-stream");

  public final StaticFiles build() throws IOException {
    final StaticFilesAttributes staticFilesAttributes;
    staticFilesAttributes = new StaticFilesAttributes(file -> Files.readAttributes(file, BasicFileAttributes.class));

    final Function<BasicFileAttributes, String> staticFilesETag;
    staticFilesETag = etag != null ? etag : new StaticFilesETag(etagMask);

    final StaticFilesExtension staticFilesExtension;
    staticFilesExtension = new StaticFilesExtension("*");

    final StaticFilesMethod staticFilesMethod;
    staticFilesMethod = new StaticFilesMethod();

    final StaticFilesTypes staticFilesTypes;
    staticFilesTypes = typesBuilder.build();

    final StaticFilesResponses staticFilesResponses;
    staticFilesResponses = new StaticFilesResponses(staticFilesExtension, staticFilesTypes);

    final StaticFilesRootBuilder staticFilesRootBuilder;
    staticFilesRootBuilder = new StaticFilesRootBuilder(directories);

    final StaticFilesRoot staticFilesRoot;
    staticFilesRoot = staticFilesRootBuilder.build();

    return new StaticFiles(
        new StaticFilesHandler(
            noteSink,

            staticFilesAttributes,

            staticFilesETag,

            staticFilesMethod,

            staticFilesResponses,

            staticFilesRoot
        ),

        new StaticFilesWriter(
            staticFilesAttributes,

            staticFilesETag,

            staticFilesMethod,

            staticFilesResponses,

            staticFilesRoot
        )
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
    typesBuilder.contentTypes(propertiesString);
  }

  public final void etag(Function<BasicFileAttributes, String> value) {
    etag = value;
  }

  public final void etagMask(long value) {
    etagMask = value;
  }

}
