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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import objectos.concurrent.CpuTask;
import objectos.concurrent.IoWorker;
import objectos.fs.Directory;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableMap;
import objectos.util.set.GrowableSet;
import objectos.util.set.UnmodifiableSet;

/**
 * A state machine providing low-level (plumbing) Git operations.
 *
 * <p>
 * Instead of using this class directly, users might prefer using
 * {@link StageGitCommand} and {@link GitService} as they provide means to
 * combine, compose and parallelize git operations.
 *
 * <p>
 * When using tasks provided by this class, please note that they are not
 * <em>thread-safe</em> and must be run in a single thread. Read {@link GitTask}
 * javadoc for more information.
 *
 * @since 1
 */
public final class GitEngine extends GitInjector {

  private static final int MASK_CHAR_BUFFER = 1 << 0;

  private static final int MASK_DEFLATER = 1 << 1;

  private static final int MASK_FILTER_NON_EXISTING = 1 << 2;

  private static final int MASK_RESOLVE_REF = 1 << 3;

  private static final int MASK_STRING_BUILDER = 1 << 4;

  private Deque<Object> arrayDeque;

  private int bitset;

  private final int bufferSize;

  private final Deque<ByteArrayWriter> byteArrayWriters = Git.newArrayDeque(3);

  private final Deque<ByteBuffer> byteBuffers = Git.newArrayDeque(3);

  private CharBuffer charBuffer;

  private CopyObjects copyObjects;

  private CpuTask currentCpuTask;

  private final Map<Charset, DecoderBucket> decoderMap = new GrowableMap<>();

  private Deflater deflater;

  private final Map<Charset, CharsetEncoder> encoderMap = new HashMap<>(2);

  private FilterNonExisting filterNonExisting;

  private IdentificationBuilder identificationBuilder;

  private Inflater inflater;

  private final Deque<IntStack> intStacks = Git.newArrayDeque(3);

  private final IoWorker ioWorker;

  private final NoteSink logger;

  private MaterializeEntry materializeEntry;

  private MessageDigest messageDigest;

  private final Deque<GrowableList<Object>> GrowableLists = Git.newArrayDeque(3);

  private final Deque<GrowableSet<Object>> GrowableSets = Git.newArrayDeque(3);

  private ObjectReader objectReader;

  private OpenPackFile openPackFile;

  private OpenRepository openRepository;

  private ReadBlob readBlob;

  private ReadCommit readCommit;

  private ReadTree readTree;

  private ResolveRef resolveRef;

  private StringBuilder stringBuilder;

  private UpdateRef updateRef;

  private WriteCommit writeCommit;

  private WriteTree writeTree;

  GitEngine(int bufferSize, IoWorker ioWorker, NoteSink logger) {
    this.bufferSize = bufferSize;

    this.ioWorker = ioWorker;

    this.logger = logger;
  }

  /**
   * Returns an option that sets the buffer size to the specified value.
   *
   * @param size
   *        the buffer size
   *
   * @return an option that sets the buffer size to the specified value
   *
   * @throws IllegalArgumentException
   *         if {@code size < 64}
   */
  public static Option bufferSize(final int size) {
    Git.checkBufferSize(size);

    return new Option() {
      @Override
      final void acceptBuilder(GitEngineBuilder builder) {
        builder.setBufferSize(size);
      }
    };
  }

  /**
   * Creates and returns a new git engine instance.
   *
   * <p>
   * Options are provided by {@code static} methods in this class. The available
   * options are:
   *
   * <table class="plain">
   * <caption>Options provided by the {@code GitEngine} class</caption>
   * <thead>
   * <tr>
   * <th scope="col">Method name</th>
   * <th scope="col">Description</th>
   * <th scope="col">Default value</th>
   * </tr>
   * </thead>
   * <tbody>
   * <tr>
   * <th scope="row">{@link GitEngine#bufferSize(int) bufferSize}</th>
   * <td>The size in bytes of the buffers used by this engine</td>
   * <td>{@code 4096}</td>
   * </tr>
   * <tr>
   * <th scope="row">{@link GitEngine#logger(NoteSink) logger}</th>
   * <td>The logger instance the {@code GitEngine} will use</td>
   * <td>{@link NoOpNoteSink#getInstance()}</td>
   * </tr>
   * </tbody>
   * </table>
   *
   * @param ioWorker
   *        the worker for I/O tasks
   * @param options
   *        additional configuration options
   *
   * @return a new git engine instance
   */
  public static GitEngine create(IoWorker ioWorker, Option... options) {
    Check.notNull(ioWorker, "ioWorker == null");
    Check.notNull(options, "options == null");

    GitEngineBuilder builder;
    builder = new GitEngineBuilder(ioWorker);

    for (int i = 0, length = options.length; i < length; i++) {
      Option o;
      o = options[i];

      Check.notNull(o, "options[", i, "] == null");

      o.acceptBuilder(builder);
    }

    return builder.build();
  }

  /**
   * Returns an options that sets the logger instance to the specified value.
   *
   * @param logger
   *        the logger instance
   *
   * @return an option that sets the logger instance to the specified value
   */
  public static Option logger(final NoteSink logger) {
    Check.notNull(logger, "logger == null");

    return new Option() {
      @Override
      final void acceptBuilder(GitEngineBuilder builder) {
        builder.setLogger(logger);
      }
    };
  }

  /**
   * Returns a new task for copying a set of objects from one repository to
   * another.
   *
   * <p>
   * The task first determines which of the specified objects are not present in
   * the destination repository. For each of the objects in the resulting
   * subset, the task tries to read it fully (into memory) from the source
   * repository and then it tries to write it to the destination repository. If
   * both the read and write operations succeed it goes to the next object. If,
   * however, either the read or write operation fails, the task stops.
   *
   * <p>
   * The task, if successful, returns the set of objects that were actually
   * copied, i.e., excluding the ones that were already present in the
   * destination repository before the operation.
   *
   * @param source
   *        the source repository containing the objects. Git objects will be
   *        read from this repository.
   * @param objects
   *        the ids (hash values) of the objects to be copied
   * @param destination
   *        the destination repository. Git objects will (possibly) be written
   *        to this repository
   *
   * @return a new task for copying Git objects
   *
   * @since 3
   */
  public final GitTask<UnmodifiableSet<ObjectId>> copyObjects(Repository source, UnmodifiableSet<ObjectId> objects, Repository destination) {
    Check.notNull(source, "source == null");
    Check.notNull(objects, "objects == null");
    Check.notNull(destination, "destination == null");

    return new CopyObjectsTask(this, source, objects, destination);
  }

  /**
   * Returns a new task for materializing a Git tree entry at the specified
   * target directory.
   *
   * <p>
   * If the entry denotes a tree object, the operation will attempt to
   * create a child directory (relative to the specified target directory)
   * having the same name as the entry name. If the directory already exists,
   * the operation will simply return the existing directory (wrapped in a
   * {@code MaterializedEntry} instance).
   *
   * <p>
   * If the entry denotes a blob object, the operation will attempt to read the
   * blob contents from the specified repository. If it succeeds reading the
   * contents, it will write all bytes to a regular file having the same name as
   * entry name and located at the specified target directory. If the file does
   * not exist it will be created, if the file already exists it will be
   * overwritten.
   *
   * @param repository
   *        repository to read blob contents from (if necessary)
   * @param entry
   *        the entry to be materialized
   * @param target
   *        the target directory
   *
   * @return a new task for materializing a Git tree entry
   *
   * @since 3
   */
  public final GitTask<MaterializedEntry> materializeEntry(Repository repository, Entry entry, Directory target) {
    Check.notNull(repository, "repository == null");
    Check.notNull(entry, "entry == null");
    Check.notNull(target, "target == null");

    return new MaterializeEntryTask(this, repository, entry, target);
  }

  /**
   * Returns a new task for opening a Git repository located at the specified
   * directory.
   *
   * <p>
   * A {@link Repository} instance is required for most Git operations provided
   * by this class. The <em>open repository</em> operation succeeds if the
   * specified directory is an actual Git repository and it fails otherwise. The
   * operation will also fail if an I/O error occurs.
   *
   * <p>
   * As of the current release the operation will fail if the specified
   * directory is not a bare repository.
   *
   * @param directory
   *        the directory where a Git repository is located
   *
   * @return a new task for opening a repository
   *
   * @since 1
   */
  public final GitTask<Repository> openRepository(Directory directory) {
    Check.notNull(directory, "directory == null");

    return new OpenRepositoryTask(this, directory);
  }

  /**
   * Returns a new task for reading a blob object from a repository.
   *
   * <p>
   * The returned task will fail if an object with the specified hash cannot be
   * found, if an object with the specified hash exists but is not a blob object
   * or if there is a parse error.
   *
   * @param repository
   *        the git repository from which to read the commit
   * @param id
   *        the unique hash value of the blob to read
   *
   * @return a new task for reading a blob object
   *
   * @since 3
   */
  public final GitTask<Blob> readBlob(Repository repository, ObjectId id) {
    Check.notNull(repository, "repository == null");
    Check.notNull(id, "id == null");

    return new ReadBlobTask(this, repository, id);
  }

  /**
   * Returns a new task for reading a commit object from a repository.
   *
   * <p>
   * The returned task will fail if an object with the specified hash cannot be
   * found, if an object with the specified hash exists but is not a commit
   * object or if there is a parse error.
   *
   * @param repository
   *        the git repository from which to read the commit
   * @param id
   *        the unique hash value of the commit to read
   *
   * @return a new task for reading a commit object
   *
   * @since 2
   */
  public final GitTask<Commit> readCommit(Repository repository, ObjectId id) {
    Check.notNull(repository, "repository == null");
    Check.notNull(id, "id == null");

    return new ReadCommitTask(this, repository, id);
  }

  /**
   * Returns a new task for reading a tree object from a repository.
   *
   * <p>
   * The returned task will fail if an object with the specified hash cannot be
   * found, if an object with the specified hash exists but is not a tree
   * object, if there is a parse error or if an I/O error occurs.
   *
   * @param repository
   *        the git repository from which to read the tree
   * @param id
   *        the unique hash value of the tree to read
   *
   * @return a new task for reading a tree object
   *
   * @since 2
   */
  public final GitTask<Tree> readTree(Repository repository, ObjectId id) {
    Check.notNull(repository, "repository == null");
    Check.notNull(id, "id == null");

    return new ReadTreeTask(this, repository, id);
  }

  /**
   * Returns a new task for resolving a Git reference. To resolve a Git
   * reference is to find the object ID associated to it.
   *
   * @param repository
   *        the repository for which the reference will be resolved
   * @param ref
   *        the Git reference to be resolved
   *
   * @return a new task for resolving a Git reference
   *
   * @since 1
   */
  public final GitTask<MaybeObjectId> resolve(Repository repository, RefName ref) {
    Check.notNull(repository, "repository == null");
    Check.notNull(ref, "ref == null");

    return new ResolveRefTask(this, repository, ref);
  }

  /**
   * Returns a new task for updating a Git reference.
   *
   * @param repository
   *        the repository for which the reference will be updated
   * @param ref
   *        the Git reference to be updated
   * @param newValue
   *        the new value for the reference
   *
   * @return a new task for updating a Git reference
   *
   * @since 3
   */
  public final GitTask<MaybeObjectId> updateRef(
                                                Repository repository, RefName ref, ObjectId newValue) {
    Check.notNull(repository, "repository == null");
    Check.notNull(ref, "ref == null");
    Check.notNull(newValue, "newValue == null");

    return new UpdateRefTask(this, repository, ref, newValue);
  }

  /**
   * Returns a new task for writing a commit object (as a loose object) to a
   * repository.
   *
   * <p>
   * The task will first verify if the specified commit have all of the
   * mandatory properties defined. If not, the task fails. Otherwise it will
   * assemble the Git commit object and will try to write it as a loose object.
   * If the loose object already exists the task assumes it is correct and
   * returns. If the loose object does not exist, the task creates it and the
   * data is written.
   *
   * <p>
   * As of the current release the operation will create a loose object even if
   * the same commit object exists in a pack file.
   *
   * @param repository
   *        the git repository where the commit is to be written
   * @param commit
   *        a {@code MutableCommit} instance containing all the information to
   *        be recorded as a commit object
   *
   * @return a new task for writing a commit object
   *
   * @since 3
   */
  public final GitTask<ObjectId> writeCommit(Repository repository, MutableCommit commit) {
    Check.notNull(repository, "repository == null");
    Check.notNull(commit, "commit == null");

    return new WriteCommitTask(this, repository, commit);
  }

  /**
   * Returns a new task for recursively writing tree objects to a repository.
   * The task is recursive in the sense that, if an entry in the specified tree
   * is itself a {@link MutableTree} instance, then it will also be written.
   *
   * <p>
   * As of the current release the operation will create loose objects even if
   * the same object exists in a pack file.
   *
   * @param repository
   *        the git repository where the tree is to be written
   * @param tree
   *        a {@code MutableTree} to be written
   *
   * @return a new task for writing a tree object
   *
   * @since 3
   */
  public final GitTask<ObjectId> writeTree(Repository repository, MutableTree tree) {
    Check.notNull(repository, "repository == null");
    Check.notNull(tree, "tree == null");

    return new WriteTreeTask(this, repository, tree);
  }

  @Override
  @SuppressWarnings("unchecked")
  final <T> ArrayDeque<T> getArrayDeque() {
    if (arrayDeque == null) {
      arrayDeque = new ArrayDeque<Object>();
    } else {
      arrayDeque.clear();
    }

    return (ArrayDeque<T>) arrayDeque;
  }

  @Override
  final int getBufferSize() {
    return bufferSize;
  }

  @Override
  final ByteArrayWriter getByteArrayWriter() {
    if (byteArrayWriters.isEmpty()) {
      return new ByteArrayWriter();
    }

    ByteArrayWriter writer;
    writer = byteArrayWriters.remove();

    writer.clear();

    return writer;
  }

  @Override
  final ByteBuffer getByteBuffer() {
    if (byteBuffers.isEmpty()) {
      return ByteBuffer.allocate(bufferSize);
    }

    ByteBuffer buffer;
    buffer = byteBuffers.remove();

    buffer.clear();

    return buffer;
  }

  @Override
  final CharBuffer getCharBuffer() {
    checkBitSet(MASK_CHAR_BUFFER, "CharBuffer");

    if (charBuffer == null) {
      charBuffer = CharBuffer.allocate(bufferSize);
    }

    charBuffer.clear();

    setBitSet(MASK_CHAR_BUFFER);

    return charBuffer;
  }

  final CopyObjects getCopyObjects() {
    if (copyObjects == null) {
      copyObjects = new CopyObjects(this);
    }

    return copyObjects;
  }

  @Override
  final CharsetDecoder getDecoder(Charset charset) {
    DecoderBucket decoder;
    decoder = decoderMap.get(charset);

    if (decoder == null) {
      decoder = new DecoderBucket(charset);

      decoderMap.put(charset, decoder);
    }

    return decoder.get();
  }

  @Override
  final Deflater getDeflater() {
    checkBitSet(MASK_DEFLATER, "Deflater");

    if (deflater == null) {
      deflater = new Deflater();
    } else {
      deflater.reset();
    }

    setBitSet(MASK_DEFLATER);

    return deflater;
  }

  @Override
  final CharsetEncoder getEncoder(Charset charset) {
    CharsetEncoder encoder;
    encoder = encoderMap.get(charset);

    if (encoder == null) {
      encoder = charset.newEncoder();

      encoderMap.put(charset, encoder);
    } else {
      encoder.reset();
    }

    return encoder;
  }

  @Override
  final FilterNonExisting getFilterNonExisting() {
    checkBitSet(MASK_FILTER_NON_EXISTING, "FilterNonExisting");

    if (filterNonExisting == null) {
      filterNonExisting = new FilterNonExisting(this);
    }

    setBitSet(MASK_FILTER_NON_EXISTING);

    return filterNonExisting;
  }

  @Override
  final IdentificationBuilder getIdentificationBuilder() {
    if (identificationBuilder == null) {
      identificationBuilder = new IdentificationBuilder();
    } else {
      identificationBuilder.reset();
    }

    return identificationBuilder;
  }

  @Override
  final Inflater getInflater() {
    if (inflater == null) {
      inflater = new Inflater();
    } else {
      inflater.reset();
    }

    return inflater;
  }

  @Override
  final IntStack getIntStack() {
    if (intStacks.isEmpty()) {
      return new IntStack();
    }

    IntStack stack;
    stack = intStacks.remove();

    stack.clear();

    return stack;
  }

  @Override
  final IoWorker getIoWorker() {
    return ioWorker;
  }

  @Override
  final NoteSink getLogger() {
    return logger;
  }

  final MaterializeEntry getMaterializeEntry() {
    if (materializeEntry == null) {
      materializeEntry = new MaterializeEntry(this);
    }

    return materializeEntry;
  }

  @Override
  final MessageDigest getMessageDigest(String algoName) {
    if (messageDigest == null) {
      try {
        messageDigest = MessageDigest.getInstance(Git.SHA1);
      } catch (NoSuchAlgorithmException e) {
        throw new UnsupportedOperationException("Implement me: move to Git.createMachine?");
      }
    }

    return messageDigest;
  }

  @Override
  @SuppressWarnings("unchecked")
  final <E> GrowableList<E> getGrowableList() {
    if (GrowableLists.isEmpty()) {
      return new GrowableList<>();
    }

    GrowableList<Object> list;
    list = GrowableLists.remove();

    list.clear();

    return (GrowableList<E>) list;
  }

  @SuppressWarnings("unchecked")
  @Override
  final <E> GrowableSet<E> getGrowableSet() {
    if (GrowableSets.isEmpty()) {
      return new GrowableSet<>();
    }

    GrowableSet<Object> set;
    set = GrowableSets.remove();

    set.clear();

    return (GrowableSet<E>) set;
  }

  @Override
  final ObjectReader getObjectReader() {
    if (objectReader == null) {
      objectReader = new ObjectReader(this);
    }

    return objectReader;
  }

  @Override
  final OpenPackFile getOpenPackFile() {
    if (openPackFile == null) {
      openPackFile = new OpenPackFile(this);
    }

    return openPackFile;
  }

  final OpenRepository getOpenRepository() {
    if (openRepository == null) {
      openRepository = new OpenRepository(this);
    }

    return openRepository;
  }

  @Override
  final ReadBlob getReadBlob() {
    if (readBlob == null) {
      readBlob = new ReadBlob(this);
    }

    return readBlob;
  }

  final ReadCommit getReadCommit() {
    if (readCommit == null) {
      readCommit = new ReadCommit(this);
    }

    return readCommit;
  }

  final ReadTree getReadTree() {
    if (readTree == null) {
      readTree = new ReadTree(this);
    }

    return readTree;
  }

  @Override
  final ResolveRef getResolveRef() {
    checkBitSet(MASK_RESOLVE_REF, "ResolveRef");

    if (resolveRef == null) {
      resolveRef = new ResolveRef(this);
    }

    setBitSet(MASK_RESOLVE_REF);

    return resolveRef;
  }

  @Override
  final StringBuilder getStringBuilder() {
    checkBitSet(MASK_STRING_BUILDER, "StringBuilder");

    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
    } else {
      stringBuilder.setLength(0);
    }

    setBitSet(MASK_STRING_BUILDER);

    return stringBuilder;
  }

  @Override
  final UpdateRef getUpdateRef() {
    if (updateRef == null) {
      updateRef = new UpdateRef(this);
    }

    return updateRef;
  }

  final WriteCommit getWriteCommit() {
    if (writeCommit == null) {
      writeCommit = new WriteCommit(this);
    }

    return writeCommit;
  }

  final WriteTree getWriteTree() {
    if (writeTree == null) {
      writeTree = new WriteTree(this);
    }

    return writeTree;
  }

  @Override
  final void putArrayDeque(ArrayDeque<?> deque) {
    // noop
  }

  @Override
  final ByteArrayWriter putByteArrayWriter(ByteArrayWriter writer) {
    if (writer != null) {
      byteArrayWriters.add(writer);
    }

    return null;
  }

  @Override
  final ByteBuffer putByteBuffer(ByteBuffer buffer) {
    if (buffer != null) {
      byteBuffers.add(buffer);
    }

    return null;
  }

  @Override
  final CharBuffer putCharBuffer(CharBuffer buffer) {
    if (buffer != null) {
      unsetBitSet(MASK_CHAR_BUFFER);
    }

    return null;
  }

  @Override
  final CharsetDecoder putDecoder(CharsetDecoder decoder) {
    if (decoder != null) {
      Charset charset;
      charset = decoder.charset();

      DecoderBucket bucket;
      bucket = decoderMap.get(charset);

      Check.state(bucket != null, "Bucket was null for ", charset);

      bucket.checkedOut = false;
    }

    return null;
  }

  @Override
  final Deflater putDeflater(Deflater deflater) {
    if (deflater != null) {
      unsetBitSet(MASK_DEFLATER);
    }

    return null;
  }

  @Override
  final CharsetEncoder putEncoder(CharsetEncoder encoder) {
    // noop

    return null;
  }

  @Override
  final FilterNonExisting putFilterNonExisting(FilterNonExisting task) {
    if (task != null) {
      unsetBitSet(MASK_FILTER_NON_EXISTING);
    }

    return null;
  }

  @Override
  final Inflater putInflater(Inflater inflater) {
    // noop

    return null;
  }

  @Override
  final IntStack putIntStack(IntStack stack) {
    if (stack != null) {
      intStacks.add(stack);
    }

    return null;
  }

  @Override
  final MessageDigest putMessageDigest(MessageDigest digest) {

    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  final <E> GrowableList<E> putGrowableList(GrowableList<E> list) {
    if (list != null) {
      GrowableLists.add((GrowableList<Object>) list);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  final <E> GrowableSet<E> putGrowableSet(GrowableSet<E> set) {
    if (set != null) {
      GrowableSets.add((GrowableSet<Object>) set);
    }

    return null;
  }

  @Override
  final ObjectReader putObjectReader(ObjectReader reader) {
    // noop

    return null;
  }

  @Override
  final ResolveRef putResolveRef(ResolveRef task) {
    if (task != null) {
      unsetBitSet(MASK_RESOLVE_REF);
    }

    return null;
  }

  @Override
  final StringBuilder putStringBuilder(StringBuilder sb) {
    if (sb != null) {
      unsetBitSet(MASK_STRING_BUILDER);
    }

    return null;
  }

  @Override
  final boolean tryLock(CpuTask task) {
    if (currentCpuTask != null) {
      return false;
    } else {
      currentCpuTask = task;

      return true;
    }
  }

  @Override
  final void unlock(CpuTask task) {
    if (currentCpuTask == task) {
      currentCpuTask = null;
    } else {
      // TODO: re-enable
      // throw new IllegalStateException("running a different task: " + currentCpuTask);
    }
  }

  private void checkBitSet(int mask, String message) {
    Check.state((bitset & mask) == 0, message, " already checked out");
  }

  private void setBitSet(int mask) {
    bitset |= mask;
  }

  private void unsetBitSet(int mask) {
    bitset &= ~mask;
  }

  /**
   * A {@code GitEngine} configuration option.
   *
   * @since 2
   */
  public abstract static class Option {

    Option() {}

    abstract void acceptBuilder(GitEngineBuilder builder);

  }

  private static class DecoderBucket {

    private boolean checkedOut;

    private final CharsetDecoder decoder;

    public DecoderBucket(Charset charset) {
      decoder = charset.newDecoder();
    }

    final CharsetDecoder get() {
      Check.state(!checkedOut, decoder.charset(), " decoder already checked out");

      checkedOut = true;

      return decoder.reset();
    }

  }

}