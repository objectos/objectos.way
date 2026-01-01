/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public final class ObjectosHttp {

  /*
   HTTP 001: Minimal GET request
  
   -- request
   GET / HTTP/1.1\r
   Host: www.example.com\r
   Connection: close\r
   \r
   ---
  
   --- response
   HTTP/1.1 200 OK\r
   Content-Type: text/plain; charset=utf-8\r
   Content-Length: 13\r
   Date: Wed, 28 Jun 2023 12:08:43 GMT\r
   \r
   Hello World!
   ---
   */

  private ObjectosHttp() {}

  public static Path createTempDir() {
    try {
      return Files.createTempDirectory("objectox-http-server-");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static final FileVisitor<Path> DELETE_RECURSIVELY = new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      Files.delete(dir);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }
  };

  public static void deleteRecursively(Path dir) {
    if (dir != null) {
      try {
        Files.walkFileTree(dir, DELETE_RECURSIVELY);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  public static byte[] readAllBytes(Http.RequestBody body) throws IOException {
    try (InputStream in = body.bodyInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      return out.toByteArray();
    }
  }

  public static String readString(Http.RequestBody body) throws IOException {
    byte[] bytes;
    bytes = readAllBytes(body);

    return new String(bytes, StandardCharsets.UTF_8);
  }

}