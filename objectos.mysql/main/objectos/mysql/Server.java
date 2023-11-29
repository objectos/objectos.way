/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import objectos.core.service.Service;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;

public final class Server extends Executable implements Service {

  private final Charset charset;

  private final ConfigurationFile configurationFile;

  private Process process;

  Server(RegularFile file, Version version, ConfigurationFile configurationFile) {
    super(file, version);

    this.configurationFile = configurationFile;

    this.charset = configurationFile.charset;
  }

  public static Server createServer(Directory directory, ConfigurationFile configurationFile) throws IOException {
    Check.notNull(directory, "directory == null");
    Check.notNull(configurationFile, "configurationFile == null");

    try {
      RegularFile file;
      file = directory.getRegularFile("mysqld");

      Version version;
      version = VersionImpl.parse(file);

      return new Server(file, version, configurationFile);
    } catch (IOException e) {
      throw new IOException("Failed to access mysqld executable", e);
    }
  }

  public final Process initialize(ServerOption option) throws IOException {
    return initialize0("--initialize", option);
  }

  public final Process initializeInsecure(ServerOption option) throws IOException {
    return initialize0("--initialize-insecure", option);
  }

  public final boolean isReadyForConnections(RegularFile file, long offset) throws IOException {
    long size;
    size = file.size();

    if (offset >= size) {
      return false;
    }

    boolean ready;
    ready = false;

    try (FileChannel channel = file.openReadChannel()) {
      channel.position(offset);

      int diff;
      diff = (int) (channel.size() - offset);

      int bufferSize;
      bufferSize = Math.min(diff, 8192);

      ByteBuffer byteBuffer;
      byteBuffer = ByteBuffer.allocate(bufferSize);

      int bytesRead;
      bytesRead = channel.read(byteBuffer);

      byteBuffer.flip();

      CharBuffer charBuffer;
      charBuffer = CharBuffer.allocate(bytesRead);

      CharsetDecoder decoder;
      decoder = charset.newDecoder();

      decoder.decode(byteBuffer, charBuffer, false);

      charBuffer.flip();

      StringBuilder line;
      line = new StringBuilder();

      while (charBuffer.hasRemaining()) {
        char c;
        c = charBuffer.get();

        if (c != '\n') {
          line.append(c);

          continue;
        }

        String s;
        s = line.toString();

        line.setLength(0);

        if (s.contains("ready for connections")) {
          ready = true;

          break;
        }
      }
    }

    return ready;
  }

  public final boolean isReadyForConnections(ResolvedPath path, long offset) throws IOException {
    if (!path.exists()) {
      return false;
    } else {
      RegularFile file;
      file = path.toRegularFile();

      return isReadyForConnections(file, offset);
    }
  }

  @Override
  public final synchronized void startService() throws IOException {
    if (!isAlive()) {
      ProcessBuilder builder;
      builder = new ProcessBuilder();

      setExecutable(builder);

      configurationFile.acceptProcessBuilder(builder);

      process = builder.start();
    }
  }

  @Override
  public final synchronized void stopService() throws Exception {
    if (isAlive()) {
      process.destroy();

      process.waitFor();

      process = null;
    }
  }

  private Process initialize0(String init, ServerOption additionalOption) throws IOException {
    ProcessBuilder builder;
    builder = new ProcessBuilder();

    setExecutable(builder);

    configurationFile.acceptProcessBuilder(builder);

    List<String> command;
    command = builder.command();

    command.add(init);

    additionalOption.acceptProcessBuilder(builder);

    return builder.start();
  }

  private boolean isAlive() {
    if (process == null) {
      return false;
    }

    try {
      process.exitValue();

      return false;
    } catch (IllegalThreadStateException e) {
      return true;
    }
  }

}
