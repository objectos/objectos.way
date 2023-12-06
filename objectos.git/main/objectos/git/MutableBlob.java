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

import java.nio.charset.Charset;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.util.array.ByteArrays;

/**
 * A class for creating an arbitrary blob object suitable for writing to a
 * repository. It can be written to a repository directly or it can be written
 * indirectly as an entry in a {@link MutableTree} instance.
 *
 * @since 2
 */
public final class MutableBlob extends MutableTreeEntry {

  byte[] contents = ByteArrays.empty();

  ObjectId objectId;

  private EntryMode mode = EntryMode.REGULAR_FILE;

  private final String name;

  /**
   * Creates a new {@code MutableBlob} having the specified name.
   *
   * @param name
   *        the name of this blob when serving as a tree entry
   *
   * @throws IllegalArgumentException
   *         if name is an empty string
   *
   * @since 3
   */
  public MutableBlob(String name) {
    this.name = Check.notNull(name, "name == null");

    Check.argument(!this.name.isEmpty(), "name must not be empty");
  }

  MutableBlob(byte[] contents, String name) {
    this.contents = Check.notNull(contents, "contents == null");

    this.name = Check.notNull(name, "name == null");
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.formatStart(sb, this);

    formatToStringImpl(null, sb, depth);

    sb.append('\n');

    ToString.formatEnd(sb, depth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final EntryMode getMode() {
    return mode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getName() {
    return name;
  }

  /**
   * Sets the contents of this blob to be the same as the specified blob.
   *
   * @param value
   *        the new contents of this blob
   *
   * @since 3
   */
  public final void setContents(Blob value) {
    Check.notNull(value, "value == null");

    contents = value.getBytes();
  }

  /**
   * Sets the contents of this blob to the specified byte array.
   *
   * @param value
   *        the new contents of this blob
   *
   * @since 3
   */
  public final void setContents(byte[] value) {
    contents = Check.notNull(value, "value == null");
  }

  /**
   * Sets this blob's executable bit.
   */
  public final void setExecutable() {
    mode = EntryMode.EXECUTABLE_FILE;
  }

  @Override
  final ObjectId computeObjectId(GitInjector job, Charset charset) {
    Check.state(objectId != null, "objectId was not set");

    return objectId;
  }

  @Override
  final void executeWriteTree(WriteTree wt) {
    wt.writeMutableBlob(contents);
  }

  @Override
  final void formatToStringImpl(String prefix, StringBuilder sb, int depth) {
    sb.append('\n');

    ToString.appendIndentation(sb, depth);

    sb.append(mode.printMode());

    sb.append(Git.SP);

    sb.append(mode.printType());

    sb.append(Git.SP);

    sb.append("0000000000000000000000000000000000000000");

    sb.append("    ");

    if (prefix != null) {
      sb.append(prefix);

      sb.append('/');
    }

    sb.append(name);
  }

}