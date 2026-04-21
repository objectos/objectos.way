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
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.way.Media;

final class HttpHost4StaticFilesWriter implements HttpStaticFilesWriter {

  private final Path rootDirectory;

  HttpHost4StaticFilesWriter(Path rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  @Override
  public final String writeMedia(String path, Media media) throws HttpTraversalException, IOException {
    final Path dst;
    dst = resolve(path);

    final Path tmp;
    tmp = Files.createTempFile(rootDirectory, null, null);

    switch (media) {
      case Media.Bytes b -> {
        final byte[] bytes;
        bytes = b.toByteArray();

        Files.write(tmp, bytes);
      }

      case Media.Stream s -> {
        try (OutputStream out = Files.newOutputStream(tmp)) {
          s.writeTo(out);
        }
      }

      case Media.Text t -> {
        final Charset cs;
        cs = t.charset();

        try (Writer w = Files.newBufferedWriter(tmp, cs)) {
          t.writeTo(w);
        }
      }
    }

    Files.move(tmp, dst, StandardCopyOption.ATOMIC_MOVE);

    final BasicFileAttributes attributes;
    attributes = Files.readAttributes(dst, BasicFileAttributes.class);

    return HttpHost3StaticFiles.etag(attributes);
  }

  private Path resolve(String path) throws HttpTraversalException, IOException {
    final String relative;
    relative = path.substring(1);

    final Path resolved;
    resolved = rootDirectory.resolve(relative);

    final Path file;
    file = resolved.normalize();

    if (!file.startsWith(rootDirectory)) {
      throw new HttpTraversalException();
    }

    final Path parent;
    parent = file.getParent();

    Files.createDirectories(parent);

    return file;
  }

}
