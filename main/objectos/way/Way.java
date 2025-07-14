/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;

/// Generates, builds and manages an Objectos Way application.
///
/// This class is not part of the Objectos Way JAR file.
/// It is placed in the main source tree to ease its development.
final class Way {

  private Path basedir;

  private byte[] buffer;

  private final Clock clock = Clock.systemDefaultZone();

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private MessageDigest digest;

  private final HexFormat hexFormat = HexFormat.of();

  private HttpClient httpClient;

  private Duration httpClientConnectTimeout;

  private Duration httpRequestTimeout;

  private int int0;

  private PrintStream logger;

  private Object object0;

  private Object object1;

  private Path repoLocal;

  private String repoRemote;

  private byte state;

  private final String version;

  private final String waySha1;

  private Way(String version, String waySha1) {
    this.version = version;

    this.waySha1 = waySha1;
  }

  public static void main(String[] args) {
    final Way way;
    way = new Way("0.2.5", "e5a9574a4b58c9af8cc4c84e2f3e032786c32fa4");

    way.start(args);
  }

  // ##################################################################
  // # BEGIN: State Machine
  // ##################################################################

  static final byte $PARSE_ARGS = 0;

  static final byte $INIT = 1;

  static final byte $BOOT_DEPS = 2;
  static final byte $BOOT_DEPS_HAS_NEXT = 3;
  static final byte $BOOT_DEPS_EXISTS = 4;
  static final byte $BOOT_DEPS_DOWNLOAD = 5;
  static final byte $BOOT_DEPS_CHECKSUM = 6;

  static final byte $RUNNING = 7;
  static final byte $ERROR = 8;

  private void start(String[] args) {
    object0 = args;

    state = $PARSE_ARGS;

    while (state < $RUNNING) {
      state = execute(state);
    }
  }

  final byte execute(byte state) {
    return switch (state) {
      case $PARSE_ARGS -> executeParseArgs();

      case $INIT -> executeInit();

      case $BOOT_DEPS -> executeBootDeps();
      case $BOOT_DEPS_HAS_NEXT -> executeBootDepsHasNext();
      case $BOOT_DEPS_EXISTS -> executeBootDepsExists();
      case $BOOT_DEPS_DOWNLOAD -> executeBootDepsDownload();
      case $BOOT_DEPS_CHECKSUM -> executeBootDepsChecksum();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: State Machine
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse Args
  // ##################################################################

  private byte executeParseArgs() {
    final String basedirPath;
    basedirPath = System.getProperty("user.dir", "");

    basedir = Path.of(basedirPath);

    buffer = new byte[8192];

    try {
      digest = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      return toError("Failed to obtain SHA-1 digest", e);
    }

    httpClientConnectTimeout = Duration.ofSeconds(10);

    httpRequestTimeout = Duration.ofMinutes(1);

    logger = System.out;

    repoLocal = basedir.resolve(".objectos/repository");

    repoRemote = "https://repo.maven.apache.org/maven2";

    // delete args
    object0 = null;

    return $INIT;
  }

  // ##################################################################
  // # END: Parse Args
  // ##################################################################

  // ##################################################################
  // # BEGIN: Init
  // ##################################################################

  private byte executeInit() {
    logInfo("Objectos Way v%s", version);

    logInfo("%10s: %s", "basedir", basedir);

    try {
      logInfo("%10s: %s", "repo-local", repoLocal);

      ensureDirectory(repoLocal);
    } catch (IOException e) {
      return toError("Failed to create directory: " + repoLocal, e);
    }

    logInfo("%10s: %s", "repo-remote", repoRemote);

    return $BOOT_DEPS;
  }

  // ##################################################################
  // # END: Init
  // ##################################################################

  // ##################################################################
  // # BEGIN: Boot Deps
  // ##################################################################

  private byte executeBootDeps() {
    int0 = 0;

    object0 = new Artifact[] {
        new Artifact("br.com.objectos", "objectos.way", version, waySha1)
    };

    return $BOOT_DEPS_HAS_NEXT;
  }

  private byte executeBootDepsHasNext() {
    final Artifact[] deps;
    deps = (Artifact[]) object0;

    if (int0 < deps.length) {
      object1 = deps[int0++];

      return $BOOT_DEPS_EXISTS;
    } else {
      return $RUNNING;
    }
  }

  private byte executeBootDepsExists() {
    final Artifact dep;
    dep = (Artifact) object1;

    if (!dep.exists()) {
      return $BOOT_DEPS_DOWNLOAD;
    } else {
      return $BOOT_DEPS_CHECKSUM;
    }
  }

  private byte executeBootDepsDownload() {
    final Artifact dep;
    dep = (Artifact) object1;

    final URI uri;
    uri = dep.toURI();

    final Path file;
    file = dep.local();

    logInfo("DEP %s -> %s", uri, file);

    final HttpRequest request;
    request = HttpRequest.newBuilder()
        .GET()
        .uri(uri)
        .timeout(httpRequestTimeout)
        .build();

    final BodyHandler<Path> bodyHandler;
    bodyHandler = HttpResponse.BodyHandlers.ofFile(file);

    try {
      final HttpClient client;
      client = httpClient();

      client.send(request, bodyHandler);

      return $BOOT_DEPS_CHECKSUM;
    } catch (IOException | InterruptedException e) {
      return toError("Failed to download: " + uri, e);
    }
  }

  private byte executeBootDepsChecksum() {
    digest.reset();

    final Artifact dep;
    dep = (Artifact) object1;

    final Path file;
    file = dep.local();

    try (InputStream in = Files.newInputStream(file)) {
      while (true) {
        final int read;
        read = in.read(buffer);

        if (read == -1) {
          break;
        }

        digest.update(buffer, 0, read);
      }
    } catch (IOException e) {
      return toError("Failed to compute checksum: " + file, e);
    }

    final byte[] sha1Bytes;
    sha1Bytes = digest.digest();

    final String sha1;
    sha1 = hexFormat.formatHex(sha1Bytes);

    if (!sha1.equals(dep.sha1)) {
      logError("Checksum mismatch for %s: got %s", file, sha1);

      return $ERROR;
    }

    logInfo("CHK %s", file);

    return $BOOT_DEPS_HAS_NEXT;
  }

  // ##################################################################
  // # END: Boot Deps
  // ##################################################################

  // ##################################################################
  // # BEGIN: Artifact
  // ##################################################################

  private class Artifact {

    final String groupId;
    final String artifactId;
    final String version;
    final String sha1;

    private Path local;

    Artifact(String groupId, String artifactId, String version, String sha1) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
      this.sha1 = sha1;
    }

    final boolean exists() {
      return Files.exists(
          local()
      );
    }

    final URI toURI() {
      return URI.create(
          repoRemote + "/maven2/" + groupId.replace('.', '/')
              + "/" + artifactId
              + "/" + version
              + "/" + artifactId + "-" + version + ".jar"
      );
    }

    private Path local() {
      if (local == null) {
        local = repoLocal.resolve(sha1 + ".jar");
      }

      return local;
    }

  }

  // ##################################################################
  // # END: Artifact
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP Client
  // ##################################################################

  private HttpClient httpClient() {
    if (httpClient == null) {
      httpClient = HttpClient.newBuilder()
          .connectTimeout(httpClientConnectTimeout)
          .build();
    }

    return httpClient;
  }

  // ##################################################################
  // # END: HTTP Client
  // ##################################################################

  // ##################################################################
  // # BEGIN: I/O
  // ##################################################################

  private void ensureDirectory(Path directory) throws IOException {
    if (!Files.isDirectory(directory)) {
      final Path parent;
      parent = directory.getParent();

      Files.createDirectories(parent);

      Files.createDirectory(directory);
    }
  }

  // ##################################################################
  // # END: I/O
  // ##################################################################

  // ##################################################################
  // # BEGIN: Logging
  // ##################################################################

  private void log0(System.Logger.Level level, String message) {
    final LocalDateTime now;
    now = LocalDateTime.now(clock);

    final String time;
    time = dateFormat.format(now);

    final String markerName;
    markerName = level.getName();

    logger.format("%s %-5s : %s%n", time, markerName, message);
  }

  private void logInfo(String message) {
    log0(INFO, message);
  }

  private void logInfo(String format, Object... args) {
    logInfo(
        String.format(format, args)
    );
  }

  private void logError(String message) {
    log0(ERROR, message);
  }

  private void logError(String format, Object... args) {
    logError(
        String.format(format, args)
    );
  }

  private byte toError(String message, Throwable t) {
    throw new UnsupportedOperationException(message, t);
  }

  // ##################################################################
  // # END: Logging
  // ##################################################################

}