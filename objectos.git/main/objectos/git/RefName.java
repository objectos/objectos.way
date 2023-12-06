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
package objectos.git;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import objectos.core.io.Charsets;
import objectos.fs.Directory;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;

/**
 * Represents the name of a Git reference.
 *
 * @since 1
 */
public abstract class RefName implements ToString.Formattable {

  /**
   * The refs/heads/master reference.
   */
  public static final RefName MASTER = new RefsHeads("master");

  final String name;

  RefName(String name) {
    this.name = name;
  }

  /**
   * Creates a new {@code RefName} representing a named branch.
   *
   * <p>
   * For example, if {@code RefName.namedBranch("test")} is invoked, then the
   * returned {@code RefName} will represent the Git reference
   * {@code refs/heads/test}.
   *
   * @param name
   *        the name of the branch
   *
   * @return a new {@code RefName} representing a named branch.
   */
  public static RefName namedBranch(String name) {
    Check.notNull(name, "name == null");

    char[] chars;
    chars = name.toCharArray();

    for (char c : chars) {
      if (c == '/') {
        throw new IllegalArgumentException(name + " contains an invalid char " + c);
      }
    }

    return new RefsHeads(name);
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "", print()
    );
  }

  /**
   * Returns the name of this reference.
   *
   * @return the name of this reference
   */
  public abstract String print();

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  abstract boolean matches(String name);

  abstract ResolvedPath resolveLoose(Directory root) throws IOException;

  abstract void update(Directory directory, ObjectId value) throws IOException;

  private static class RefsHeads extends RefName implements PathNameVisitor<FileChannel, Void> {

    RefsHeads(String name) {
      super(name);
    }

    @Override
    public final String print() {
      return "refs/heads/" + name;
    }

    @Override
    public final FileChannel visitDirectory(Directory directory, Void p) throws IOException {
      throw new IOException(directory.getPath() + " exists but is a directory.");
    }

    @Override
    public final FileChannel visitNotFound(ResolvedPath notFound, Void p) throws IOException {
      notFound.createParents();

      return notFound.openWriteChannel();
    }

    @Override
    public final FileChannel visitRegularFile(RegularFile file, Void p) throws IOException {
      return file.openWriteChannel();
    }

    @Override
    final boolean matches(String name) {
      return print().equals(name);
    }

    @Override
    final ResolvedPath resolveLoose(Directory directory) throws IOException {
      return directory.resolve("refs", "heads", name);
    }

    @Override
    final void update(Directory directory, ObjectId value) throws IOException {
      ResolvedPath maybeFile;
      maybeFile = resolveFsObject(directory);

      try (var channel = maybeFile.acceptPathNameVisitor(this, null)) {
        String text;
        text = value.getHexString() + Git.LF;

        byte[] bytes;
        bytes = text.getBytes(Charsets.utf8());

        ByteBuffer buffer;
        buffer = ByteBuffer.wrap(bytes);

        channel.truncate(0);

        channel.write(buffer);
      }
    }

    private ResolvedPath resolveFsObject(Directory directory) throws IOException {
      return directory.resolve("refs", "heads", name);
    }

  }

}