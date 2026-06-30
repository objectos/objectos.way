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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.function.Function;
import objectos.way.Note;

public record StaticFilesStage(
    Set<Path> directories,

    Note.Sink noteSink,

    StaticFilesAttributes staticFilesAttributes,

    Function<BasicFileAttributes, String> staticFilesETag,

    StaticFilesMethod staticFilesMethod,

    StaticFilesResponses staticFilesResponses
) {

  public final StaticFiles toStaticFiles() throws IOException {
    final StaticFilesRootBuilder staticFilesRootBuilder;
    staticFilesRootBuilder = new StaticFilesRootBuilder(directories, noteSink);

    final StaticFilesRoot staticFilesRoot;
    staticFilesRoot = staticFilesRootBuilder.build();

    return new StaticFiles(
        staticFilesAttributes,

        staticFilesETag,

        staticFilesMethod,

        staticFilesResponses,

        staticFilesRoot
    );
  }

}
