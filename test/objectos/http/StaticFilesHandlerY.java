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
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import objectos.way.Note;
import objectos.way.Y;

final class StaticFilesHandlerY {

  String defaultType;

  Note.Sink noteSink = Y.noteSink();

  Path root;

  Map<String, String> types;

  public static Handler create(Consumer<? super StaticFilesHandlerY> opts) {
    final StaticFilesHandlerY y;
    y = new StaticFilesHandlerY();

    opts.accept(y);

    return y.build();
  }

  private StaticFilesHandler build() {
    return new StaticFilesHandler(
        noteSink,

        new StaticFilesAttributes(this::attributes),

        new StaticFilesETag(0L),

        new StaticFilesMethod(),

        new StaticFilesResponses(
            new StaticFilesExtension("*"),

            new StaticFilesTypes(defaultType, types)
        ),

        new StaticFilesRoot(root)
    );
  }

  private BasicFileAttributes attributes(Path file) throws IOException {
    final BasicFileAttributes original;
    original = Files.readAttributes(file, BasicFileAttributes.class);

    return new ForwardingBasicFileAttributes(original) {
      @Override
      public FileTime lastModifiedTime() {
        final Clock clock;
        clock = Y.clockFixed();

        final Instant instant;
        instant = clock.instant();

        return FileTime.from(instant);
      }
    };
  }

  private static class ForwardingBasicFileAttributes implements BasicFileAttributes {
    private final BasicFileAttributes original;

    ForwardingBasicFileAttributes(BasicFileAttributes original) { this.original = original; }

    @Override
    public FileTime lastModifiedTime() { return original.lastModifiedTime(); }

    @Override
    public FileTime lastAccessTime() { return original.lastAccessTime(); }

    @Override
    public FileTime creationTime() { return original.creationTime(); }

    @Override
    public boolean isRegularFile() { return original.isRegularFile(); }

    @Override
    public boolean isDirectory() { return original.isDirectory(); }

    @Override
    public boolean isSymbolicLink() { return original.isSymbolicLink(); }

    @Override
    public boolean isOther() { return original.isOther(); }

    @Override
    public long size() { return original.size(); }

    @Override
    public Object fileKey() { return original.fileKey(); }
  }

}
