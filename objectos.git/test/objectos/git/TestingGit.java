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

import static objectos.git.EntryMode.EXECUTABLE_FILE;
import static objectos.git.EntryMode.REGULAR_FILE;
import static objectos.git.EntryMode.TREE;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Concurrent;
import objectos.concurrent.DirectIoWorker;
import objectos.fs.Directory;
import objectos.fs.testing.TestInf;
import objectos.fs.testing.TmpDir;
import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

final class TestingGit {

  /*

  @startmindmap

  <style>
  mindmapDiagram {
    arrow {
      LineColor black
    }

    node {
      LineColor black
    }
  }
  </style>

  *_ Test cases
  
  **:**Test case 11**
  ----
  Copy use-case. Verify that generated
  trees are sorted. In particular when blobs
  are edited/filtered since these are
  executed last by the CopyJob.
  ----
  # open source
  # open target
  # copy
  # verify;
  *** CopyJob
  *** Blob
  *** Tree
  *** WritableBlob
  *** WritableTree

  **:**Test case 10**
  ----
  Copy use-case. Verify that the
  CopyJob can transform the contents
  of a file (blob). That is, it can:
  - fetch blob
  - pass blob contents
  - copy transforms contents
  - write new blob contents
  - update tree with new objectId
  ----
  # open source
  # open target
  # copy
  # verify;
  *** CopyJob
  *** Blob
  *** Tree
  *** WritableBlob
  *** WritableTree

  **:**Test case 09**
  ----
  Verify that Repository and/or Repository
  correctly parses the packed-refs file. For
  now, support only the the 'heads' use case,
  tags, while they will exist in the file, can
  be ignored by the parser. Also, in this test
  case, the 'loose' ref does not exist.
  ----
  # open repo
  # resolve refs/heads/master
  # verify correct objectid;
  *** CopyJob
  *** Repository
  *** PackedRefs
  *** RefName
  *** Repository

  **:**Test case 08**
  ----
  Verify that the copy command does not
  create a commit if the resulting tree is empty.
  ----
  # open source
  # open target
  # copy
  # verify;
  *** CopyJob

  **:**Test case 07**
  ----
  Verify that the copy command can create the
  first commit of the target repository. That is,
  the repository exists but has no commits.
  In particular, ensure that the following is
  verified:
  * properly handle the fact that
  refs/heads/master will not exist
  * parent of the final commit will not exist
  ----
  # open source
  # open target (init, but empty)
  # copy
  # verify;
  *** CopyJob
  *** RefName
  *** Repository

  **:**Test case 06**
  ----
  Verify that the copy command can update
  the author and the committer information of
  the commit. With the following true:
  * source repo is bare with loose objects only
  * target repo is bare with loose objects only
  ----
  # open source
  # verify source HEAD author is foo@bar
  # copy and set author to oss@bar
  # verify target HEAD author is oss@bar;
  *** CopyJob
  *** Identification
  *** RefName
  *** Repository

  @endmindmap

   */

  public static final ObjectId REPO00_MASTER;

  private static final int[] BASE64_DECODER;

  static {
    try {
      REPO00_MASTER = ObjectId.parse("717271f0f0ee528c0bb094e8b2f84ea6cef7b39d");
    } catch (InvalidObjectIdFormatException e) {
      throw new RuntimeException(e);
    }

    BASE64_DECODER = new int['z' + 1];

    Arrays.fill(BASE64_DECODER, -1);

    String base64;
    base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    for (int i = 0, len = base64.length(); i < len; i++) {
      char c;
      c = base64.charAt(i);

      BASE64_DECODER[c] = i;
    }
  }

  private TestingGit() {}

  public static Repository bareRepository(Directory directory)
                                                               throws IOException, ExecutionException {
    GitEngine engine;
    engine = standardEngineInstance();

    GitTask<Repository> task;
    task = engine.openRepository(directory);

    Concurrent.exhaust(task);

    return task.getResult();
  }

  public static byte[] decode64(String... lines) {
    Check.notNull(lines, "lines == null");

    // 0
    if (lines.length == 0) {
      return ByteArrays.empty();
    }

    // 1
    long inputLength;
    inputLength = 0;

    for (int i = 0; i < lines.length; i++) {
      String line;
      line = lines[i];

      Check.notNull(line, "lines[", i, "] == null");

      if (line.isEmpty()) {
        throw new IllegalArgumentException("lines[" + i + "] is empty");
      }

      inputLength += line.length();
    }

    Check.argument(inputLength >= 2, "input string length must be at least 2 chars");

    // 2
    int resultLength;
    resultLength = 3 * (int) inputLength / 4;

    String lastLine;
    lastLine = lines[lines.length - 1];

    int lastLineLength;
    lastLineLength = lastLine.length();

    char c;
    c = lastLine.charAt(lastLineLength - 1);

    if (c == '=') {
      resultLength--;

      c = lastLine.charAt(lastLineLength - 2);

      if (c == '=') {
        resultLength--;
      }
    }

    // 3
    byte[] result;
    result = new byte[resultLength];

    int resultIndex;
    resultIndex = 0;

    int state;
    state = 3;

    int high;
    high = 0;

    for (String line : lines) {
      int lineIndex;
      lineIndex = 0;

      int lineLength;
      lineLength = line.length();

      outer: while (lineIndex < lineLength && resultIndex < result.length) {
        int low;

        int highShift;

        int lowShift;

        switch (state) {
          case 0:
            low = findBase64Decoder(line, lineIndex++);

            highShift = 2;

            lowShift = 4;

            state = 1;

            break;
          case 1:
            low = findBase64Decoder(line, lineIndex++);

            highShift = 4;

            lowShift = 2;

            state = 2;

            break;
          case 2:
            low = findBase64Decoder(line, lineIndex++);

            highShift = 6;

            lowShift = 0;

            state = 3;

            break;
          case 3:
            high = findBase64Decoder(line, lineIndex++);

            state = 0;

            continue outer;
          default:
            throw new AssertionError("Unexpected state: " + state);
        }

        int highBits;
        highBits = high << highShift;

        int lowBits;
        lowBits = low >> lowShift;

        int allBits;
        allBits = highBits | lowBits;

        result[resultIndex++] = (byte) (allBits & 0xFF);

        high = low;
      }
    }

    return result;
  }

  public static Entry executable(String name, String sha1) {
    try {
      return EXECUTABLE_FILE.createEntry(name, sha1);
    } catch (InvalidObjectIdFormatException e) {
      AssertionError error;
      error = new AssertionError();

      error.initCause(e);

      throw error;
    }
  }

  public static UnmodifiableList<String> fetchLogSource(
                                                        Repository repository) throws GitStubException, IOException, ExecutionException {
    MaybeObjectId maybe;
    maybe = resolve(repository, Git.MASTER);

    if (!maybe.isObjectId()) {
      throw new IOException("master branch was not found");
    }

    ObjectId id;
    id = maybe.getObjectId();

    GitEngine engine;
    engine = standardEngineInstance();

    GrowableList<String> list;
    list = new GrowableList<>();

    while (id != null) {
      GitTask<Commit> computable;
      computable = engine.readCommit(repository, id);

      id = null;

      Concurrent.exhaust(computable);

      Commit commit;
      commit = computable.getResult();

      String message;
      message = commit.getMessage();

      int indexOf;
      indexOf = message.indexOf("source=");

      String source;
      source = message.substring(indexOf + 7, indexOf + 40 + 7);

      list.add(source);

      UnmodifiableList<ObjectId> parents;
      parents = commit.getParents();

      if (!parents.isEmpty()) {
        id = parents.get(0);
      }
    }

    return list.toUnmodifiableList();
  }

  public static Directory getDirectory(String directoryName) throws IOException {
    Directory testInf;
    testInf = TestInf.get();

    return testInf.getDirectory(directoryName);
  }

  public static Repository openRepo00() throws IOException, ExecutionException {
    return openRepo("repo00.git");
  }

  public static Repository openRepo01() throws IOException, ExecutionException {
    return openRepo("repo01.git");
  }

  public static Entry regular(String name, String sha1) {
    try {
      return REGULAR_FILE.createEntry(name, sha1);
    } catch (InvalidObjectIdFormatException e) {
      AssertionError error;
      error = new AssertionError();

      error.initCause(e);

      throw error;
    }
  }

  public static Directory repo00() throws IOException {
    return getDirectory("repo00.git");
  }

  // the empty repo
  public static Directory repo01() throws IOException {
    return getDirectory("repo01.git");
  }

  // pack-file repo
  public static Directory repo02() throws IOException {
    return getDirectory("repo02.git");
  }

  public static MaybeObjectId resolve(
                                      Repository repository, RefName ref)
                                                                          throws GitStubException, IOException, ExecutionException {
    GitEngine engine;
    engine = standardEngineInstance();

    GitTask<MaybeObjectId> task = engine.resolve(repository, ref);

    Concurrent.exhaust(task);

    return task.getResult();
  }

  public static GitEngine standardEngineInstance() {
    return StandardGitMachine.INSTANCE;
  }

  public static MutableTree tree(String name, MutableTreeEntry... entries) {
    MutableTree tree;
    tree = new MutableTree(name);

    for (MutableTreeEntry e : entries) {
      tree.addEntry(e);
    }

    return tree;
  }

  public static Entry tree(String name, String sha1) {
    try {
      return TREE.createEntry(name, sha1);
    } catch (InvalidObjectIdFormatException e) {
      AssertionError error;
      error = new AssertionError();

      error.initCause(e);

      throw error;
    }
  }

  private static int findBase64Decoder(String line, int index) {
    char c;
    c = line.charAt(index);

    if (c == '=') {
      return 0;
    }

    if (c > 'z') {
      throw new IllegalArgumentException("Line contains invalid character: " + c);
    }

    int maybe;
    maybe = BASE64_DECODER[c];

    if (maybe < 0) {
      throw new IllegalArgumentException("Line contains invalid character: " + c);
    }

    return maybe;
  }

  private static Repository openRepo(String name) throws IOException, ExecutionException {
    Directory source;
    source = getDirectory(name);

    Directory root;
    root = TmpDir.create();

    Directory target;
    target = root.createDirectory(name);

    source.copyTo(target);

    return openRepository(target);
  }

  private static Repository openRepository(Directory sourceDirectory)
                                                                      throws IOException, ExecutionException {
    GitEngine engine;
    engine = standardEngineInstance();

    GitTask<Repository> computable;
    computable = engine.openRepository(sourceDirectory);

    Concurrent.exhaust(computable);

    return computable.getResult();
  }

  private static class StandardGitMachine {

    static GitEngine INSTANCE = create();

    private static GitEngine create() {
      return GitEngine.create(
          DirectIoWorker.get(),

          GitEngine.bufferSize(64)
      );
    }

  }

}