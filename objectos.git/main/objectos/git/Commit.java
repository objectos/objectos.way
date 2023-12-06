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

import objectos.lang.object.ToString;
import objectos.util.list.UnmodifiableList;

/**
 * A Git commit object resulting from a <em>read commit</em> operation.
 *
 * @since 1
 */
public final class Commit extends GitObject {

  final Identification author;

  final Identification committer;

  final String message;

  final UnmodifiableList<ObjectId> parents;

  final ObjectId tree;

  Commit(Identification author,
         Identification committer,
         String message,
         ObjectId objectId,
         UnmodifiableList<ObjectId> parents,
         ObjectId tree) {
    super(objectId);

    this.author = author;

    this.committer = committer;

    this.message = message;

    this.parents = parents;

    this.tree = tree;
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "", objectId,
        "author", getAuthor(),
        "committer", getCommitter(),
        "message", getMessage(),
        "parents", getParents(),
        "tree", getTree()
    );
  }

  /**
   * Returns the author of this commit.
   *
   * @return the author of this commit
   */
  public final Identification getAuthor() {
    return author;
  }

  /**
   * Returns the committer of this commit.
   *
   * @return the committer of this commit
   */
  public final Identification getCommitter() {
    return committer;
  }

  /**
   * Returns the message of this commit.
   *
   * @return the message of this commit
   */
  public final String getMessage() {
    return message;
  }

  /**
   * Returns the hash values of the parents of this commit.
   *
   * @return the hash values of the parents of this commit
   */
  public final UnmodifiableList<ObjectId> getParents() {
    return parents;
  }

  /**
   * Returns the hash value of the tree object associated to this commit.
   *
   * @return the hash value of the tree object associated to this commit
   */
  public final ObjectId getTree() {
    return tree;
  }

  /**
   * Returns a string representation of this commit that should be equivalent to
   * the output of the {@code git-cat-file} utility when invoked with the
   * {@code -p} option.
   *
   * @return a string representation of this commit
   *
   * @since 3
   */
  public final String print() {
    StringBuilder out;
    out = new StringBuilder();

    out.append("tree ");

    out.append(tree.getHexString());

    out.append(System.lineSeparator());

    for (ObjectId parent : parents) {
      out.append("parent ");

      out.append(parent.getHexString());

      out.append(System.lineSeparator());
    }

    out.append("author ");

    out.append(author.print());

    out.append(System.lineSeparator());

    out.append("committer ");

    out.append(committer.print());

    out.append(System.lineSeparator());

    out.append(System.lineSeparator());

    out.append(message);

    return out.toString();
  }

}