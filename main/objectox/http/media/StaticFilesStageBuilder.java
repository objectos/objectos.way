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
package objectox.http.media;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import objectos.http.StaticFilesOptions;
import objectos.internal.IOFunction;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Note;

public final class StaticFilesStageBuilder implements StaticFilesOptions {

  private static final LinkOption[] LINK = {LinkOption.NOFOLLOW_LINKS};

  private Set<Path> directories = Set.of();

  Function<BasicFileAttributes, String> etag;

  private long etagMask = ThreadLocalRandom.current().nextLong();

  private final Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private final StaticFilesTypesBuilder typesBuilder = new StaticFilesTypesBuilder("application/octet-stream");

  public final StaticFilesStage build() {
    final IOFunction<Path, BasicFileAttributes> reader;
    reader = file -> Files.readAttributes(file, BasicFileAttributes.class, LINK);

    final StaticFilesAttributes staticFilesAttributes;
    staticFilesAttributes = new StaticFilesAttributes(reader);

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

    return new StaticFilesStage(
        directories,

        noteSink,

        staticFilesAttributes,

        staticFilesETag,

        staticFilesMethod,

        staticFilesResponses
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

  public final void etagMask(long value) {
    etagMask = value;
  }

}
