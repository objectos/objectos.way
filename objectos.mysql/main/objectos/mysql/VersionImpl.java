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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import objectos.fs.RegularFile;

final class VersionImpl implements Version {

  private final int major;
  private final int minor;
  private final int patch;
  private final Release release;
  private String toString;

  VersionImpl(Release release, int major, int minor, int patch) {
    this.release = release;
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  static VersionImpl parse(RegularFile executable) throws IOException {
    List<String> command;
    command = new ArrayList<>();

    command.add(executable.getPath());

    command.add("--version");

    ProcessBuilder processBuilder;
    processBuilder = new ProcessBuilder(command);

    Process process;
    process = processBuilder.start();

    InputStream inputStream;
    inputStream = process.getInputStream();

    InputStreamReader inputStreamReader;
    inputStreamReader = new InputStreamReader(inputStream);

    BufferedReader reader;
    reader = new BufferedReader(inputStreamReader);

    String line;

    try {
      line = reader.readLine();

      String shouldBeNull;
      shouldBeNull = reader.readLine();

      if (shouldBeNull != null) {
        throw new UnsupportedOperationException("Implement me");
      }
    } finally {
      reader.close();
    }

    int exitValue;

    try {
      exitValue = process.waitFor();
    } catch (InterruptedException interrupted) {
      Thread.currentThread().interrupt();
      throw new IOException(
          "Interrupted while trying to parse version", interrupted);
    }

    if (exitValue != 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    return parseLine(line);
  }

  static VersionImpl parseLine(String line) {
    char[] charArray;
    charArray = line.toCharArray();

    Parser parser;
    parser = new Parser(charArray);

    return parser.parse();
  }

  @Override
  public final Release getRelease() {
    return release;
  }

  @Override
  public final String toString() {
    if (toString == null) {
      toString = toString0();
    }

    return toString;
  }

  private String toString0() {
    StringBuilder b;
    b = new StringBuilder();

    b.append(major);
    b.append('.');
    b.append(minor);
    b.append('.');
    b.append(patch);

    return b.toString();
  }

  private static class Parser {

    private final char[] charArray;

    private int major;

    private int minor;
    private int patch;
    private State state = State.START;

    public Parser(char[] charArray) {
      this.charArray = charArray;
    }

    final void appendMajor(char c) {
      major = append(major, c);
    }

    final void appendMinor(char c) {
      minor = append(minor, c);
    }

    final void appendPatch(char c) {
      patch = append(patch, c);
    }

    final VersionImpl get() {
      Release release;
      release = getRelease();

      return new VersionImpl(release, major, minor, patch);
    }

    final VersionImpl parse() {
      for (int i = 0; i < charArray.length; i++) {
        if (state.shouldBreak()) {
          break;
        }

        char c;
        c = charArray[i];

        state = state.consume(this, c);
      }

      return state.get(this);
    }

    final void reset() {
      major = 0;
      minor = 0;
      patch = 0;
    }

    private int append(int value, char c) {
      return (value * 10) + (c - 48);
    }

    private Release getRelease() {
      if (major == 8) {
        return Release.MYSQL_8;
      }

      if (major == 5 && minor == 7) {
        return Release.MYSQL_5_7;
      }

      if (major == 5 && minor == 6) {
        return Release.MYSQL_5_6;
      }

      throw new UnsupportedOperationException("Implement me: " + major + '.' + minor);
    }

  }

  private enum State {

    DONE {
      @Override
      final boolean shouldBreak() {
        return true;
      }
    },

    ERROR {
      @Override
      final boolean shouldBreak() {
        return true;
      }
    },

    MAJOR {
      @Override
      final State consume(Parser p, char c) {
        if (Character.isDigit(c)) {
          p.appendMajor(c);
          return this;
        }

        if (c == '.') {
          return MINOR;
        }

        return ERROR;
      }
    },

    MINOR {
      @Override
      final State consume(Parser p, char c) {
        if (Character.isDigit(c)) {
          p.appendMinor(c);
          return this;
        }

        if (c == '.') {
          return PATCH;
        }

        p.reset();

        return SPACE_0;
      }
    },

    PATCH {
      @Override
      final State consume(Parser p, char c) {
        if (Character.isDigit(c)) {
          p.appendPatch(c);
          return this;
        }

        return DONE;
      }
    },

    SPACE_0 {
      @Override
      final State consume(Parser p, char c) {
        if (c == ' ') {
          return this;
        }

        return VER;
      }
    },

    SPACE_1 {
      @Override
      final State consume(Parser p, char c) {
        if (c == ' ') {
          return this;
        }

        if (Character.isDigit(c)) {
          p.appendMajor(c);
          return MAJOR;
        }

        return ERROR;
      }
    },

    START {
      @Override
      final State consume(Parser p, char c) {
        if (c != ' ') {
          return this;
        }

        return SPACE_0;
      }
    },

    VER {
      @Override
      final State consume(Parser p, char c) {
        if (c != ' ') {
          return this;
        }

        return SPACE_1;
      }
    };

    State consume(Parser p, char c) {
      throw new UnsupportedOperationException(name());
    }

    VersionImpl get(Parser p) {
      return p.get();
    }

    boolean shouldBreak() {
      return false;
    }

  }

}
