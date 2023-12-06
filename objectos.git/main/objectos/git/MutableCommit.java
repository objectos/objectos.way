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

import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.util.list.GrowableList;

/**
 * A class for creating a commit object suitable for writing to a repository.
 *
 * @since 3
 */
public final class MutableCommit implements ToString.Formattable {

  private Identification author;

  private Identification committer;

  private String message;

  private GrowableList<ObjectId> parents;

  private ObjectId tree;

  /**
   * Creates a new empty mutable commit instance.
   */
  public MutableCommit() {}

  /**
   * Adds the specified object ID as a parent of this commit.
   *
   * @param parent
   *        the additional parent of this commit
   */
  public final void addParent(ObjectId parent) {
    if (parents == null) {
      parents = new GrowableList<>();
    }

    parents.addWithNullMessage(parent, "parent == null");
  }

  /**
   * Sets this commit to the same state as if it was just instantiated.
   */
  public final void clear() {
    author = null;

    committer = null;

    message = null;

    if (parents != null) {
      parents.clear();
    }

    tree = null;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "author", author,
        "committer", committer,
        "message", message,
        "parents", parents,
        "tree", tree
    );
  }

  /**
   * Sets the author of this commit to the specified value.
   *
   * @param author
   *        the value to set as the author of this commit
   */
  public final void setAuthor(Identification author) {
    this.author = Check.notNull(author, "author == null");
  }

  /**
   * Sets the committer of this commit to the specified value.
   *
   * @param committer
   *        the value to set as the committer of this commit
   */
  public final void setCommitter(Identification committer) {
    this.committer = Check.notNull(committer, "committer == null");
  }

  /**
   * Sets the message of this commit to the specified value.
   *
   * @param message
   *        the value to set as the message of this commit
   */
  public final void setMessage(String message) {
    this.message = Check.notNull(message, "message == null");
  }

  /**
   * Sets the tree of this commit to the specified value.
   *
   * @param tree
   *        the value to set as the tree of this commit
   */
  public final void setTree(ObjectId tree) {
    this.tree = Check.notNull(tree, "tree == null");
  }

  /**
   * Returns the string representation of this commit as defined by the
   * {@link ToString.Formattable#formatToString(StringBuilder, int)} method.
   *
   * @return the string representation of this commit
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  final void acceptStringBuilder(StringBuilder sb) {
    sb.append("tree ");

    sb.append(tree.getHexString());

    sb.append(Git.LF);

    if (parents != null) {
      for (ObjectId parent : parents) {
        sb.append("parent ");

        ObjectId parentObjectId;
        parentObjectId = parent.getObjectId();

        sb.append(parentObjectId.getHexString());

        sb.append(Git.LF);
      }
    }

    sb.append("author ");

    sb.append(author.print());

    sb.append(Git.LF);

    sb.append("committer ");

    sb.append(committer.print());

    sb.append(Git.LF);

    sb.append(Git.LF);

    sb.append(message);
  }

  final IllegalArgumentException validate(StringBuilder sb) {
    IllegalArgumentException result;
    result = null;

    if (author == null) {
      validate0(sb, "author");
    }

    if (committer == null) {
      validate0(sb, "committer");
    }

    if (message == null) {
      validate0(sb, "message");
    }

    if (tree == null) {
      validate0(sb, "tree");
    }

    if (sb.length() > 0) {
      String message;
      message = sb.toString();

      sb.setLength(0);

      result = new IllegalArgumentException(message);
    }

    return result;
  }

  private void validate0(StringBuilder sb, String name) {
    if (sb.length() == 0) {
      sb.append("Cannot write commit, missing the following field(s): ");
    } else {
      sb.append(", ");
    }

    sb.append(name);
  }

}