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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Concurrent;
import objectos.core.io.Charsets;
import objectos.util.set.GrowableSet;
import objectos.util.set.UnmodifiableSet;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ObjectReaderTest extends AbstractGitTest {

  private ObjectReader reader;

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    Concurrent.exhaust(reader);
  }

  @BeforeClass
  public void beforeClass() {
    reader = new ObjectReader(engine);
  }

  @Test(description = TestCase00.DESCRIPTION + " [Commit]")
  public void testCase00Commit() throws IOException, ExecutionException {
    Repository repository;
    repository = TestCase00.getRepository();

    ObjectId oid;
    oid = TestCase00.getCommit();

    ReadObjectAdapter adapter;
    adapter = new ReadObjectAdapter(repository, oid);

    reader.set(adapter);

    assertTrue(reader.isActive());

    assertEquals(reader.state, ObjectReader._START);

    executeAndAssertState(
      reader,

      ObjectReader._NEXT_PACK_FILE,

      ObjectReader._NEXT_LOOSE_OBJECT,

      ObjectReader._WAIT_IO
    );

    //    assertEquals(reader.ioTask, ObjectReader.IO_OPEN_LOOSE);
    //
    //    assertEquals(reader.ioReady, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 139);

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.inflater.getBytesWritten(), 31);

    assertEquals(reader.state, ObjectReader._HEADER);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    int headerLength;
    headerLength = "commit 182".getBytes(Charsets.usAscii()).length + 1;

    assertEquals(headerLength, 11);

    assertEquals(reader.inflaterBuffer.position(), 31);

    assertEquals(
      adapter.byteArray.size(),

      reader.inflaterBuffer.position() - 11
    );

    assertEquals(adapter.objectKind, ObjectKind.COMMIT);

    assertEquals(reader.objectLength, 182);

    reader.executeOne();

    Concurrent.exhaust(reader);

    reader.acceptResultConsumer(this);

    Object result;
    result = getResult();

    assertSame(result, Result.INSTANCE);

    assertEquals(adapter.byteArray.toByteArray(), TestCase00.getCommitContents());
  }

  @Test(description = TestCase05.DESCRIPTION)
  public void testCase05() throws IOException, ExecutionException {
    Repository repository;
    repository = createEmptyRepository();

    UnmodifiableSet<ObjectId> objectSet;
    objectSet = TestCase05.getCopyObjectSet();

    FilterNonExistingAdapter adapter;
    adapter = new FilterNonExistingAdapter(repository, objectSet);

    reader.set(adapter);

    assertTrue(reader.isActive());

    assertEquals(reader.state, ObjectReader._START);

    Concurrent.exhaust(reader);

    reader.acceptResultConsumer(this);

    UnmodifiableSet<ObjectId> result;
    result = getResult();

    assertEquals(result, objectSet);
  }

  @Test(description = TestCase12.DESCRIPTION + " [Commit]")
  public void testCase12Commit() throws IOException, ExecutionException {
    Repository repository;
    repository = TestCase12.getRepository();

    ObjectId oid;
    oid = TestCase12.getCommitNonDeltified();

    ReadObjectAdapter adapter;
    adapter = new ReadObjectAdapter(repository, oid);

    reader.set(adapter);

    assertTrue(reader.isActive());

    assertEquals(reader.state, ObjectReader._START);

    executeAndAssertState(
      reader,

      ObjectReader._NEXT_PACK_FILE,

      ObjectReader._NEXT_PACK_FILE_OBJECT,

      ObjectReader._IO_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO
    );

    assertEquals(oid.getFanOutIndex(), 138);

    assertEquals(reader.channelPosition, 4 + 4 + (137 * 4));

    assertEquals(reader.channelReadLimit, 4 + 4);

    executeAndAssertState(
      reader,

      ObjectReader._PARSE_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO
    );

    assertEquals(reader.objectPosition, 24);

    assertEquals(reader.objectLength, 25);

    executeAndAssertState(
      reader,

      ObjectReader._PARSE_INDEX_OBJECT_NAME,

      ObjectReader._WAIT_IO
    );

    assertEquals(reader.channelPosition, 4 + 4 + (256 * 4) + (51 * 20) + (51 * 4) + (24 * 4));

    assertEquals(reader.channelReadLimit, 4);

    executeAndAssertState(
      reader,

      ObjectReader._PARSE_INDEX_OFFSET,

      ObjectReader._WAIT_IO
    );

    assertEquals(reader.channelPosition, 980);

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 2633);

    executeAndAssertState(
      reader,

      ObjectReader._PARSE_PACK_HEADER,

      ObjectReader._INFLATE,

      ObjectReader._OBJECT_DATA,

      ObjectReader._WAIT_IO
    );

    assertEquals(reader.channelReadCount, 64 + 64);

    assertEquals(reader.inflater.getBytesWritten(), 33);

    assertEquals(adapter.objectKind, ObjectKind.COMMIT);

    assertEquals(reader.objectLength, 238);

    executeAndAssertState(
      reader,

      ObjectReader._INFLATE,

      ObjectReader._OBJECT_DATA
    );

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._OBJECT_DATA);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(reader.channelReadCount, 64 + 64 + 64);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._OBJECT_DATA);

    Concurrent.exhaust(reader);

    reader.acceptResultConsumer(this);

    Object result;
    result = getResult();

    assertSame(result, Result.INSTANCE);

    assertEquals(adapter.byteArray.toByteArray(), TestCase12.getCommitContents());
  }

  @Test
  public void testCase12Tree() throws IOException, ExecutionException {
    Repository repository;
    repository = TestCase12.getRepository();

    ObjectId oid;
    oid = TestCase12.getTreeDeltified();

    ReadObjectAdapter adapter;
    adapter = new ReadObjectAdapter(repository, oid);

    reader.set(adapter);

    assertTrue(reader.isActive());

    assertEquals(reader.state, ObjectReader._START);

    executeAndAssertState(
      reader,

      ObjectReader._NEXT_PACK_FILE,

      ObjectReader._NEXT_PACK_FILE_OBJECT,

      ObjectReader._IO_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO
    );

    assertEquals(oid.getFanOutIndex(), 51);

    assertEquals(reader.channelPosition, 4 + 4 + (50 * 4));

    assertEquals(reader.channelReadLimit, 4 + 4);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._PARSE_INDEX_FAN_OUT);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(reader.objectPosition, 9);

    assertEquals(reader.objectLength, 11);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._PARSE_INDEX_OBJECT_NAME);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(reader.channelPosition, 4 + 4 + (256 * 4) + (51 * 20) + (51 * 4) + (10 * 4));

    assertEquals(reader.channelReadLimit, 4);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._PARSE_INDEX_OFFSET);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(reader.channelPosition, 3530);

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 83);

    executeAndAssertState(
      reader,

      ObjectReader._PARSE_PACK_HEADER,

      ObjectReader._INFLATE,

      ObjectReader._DELTA_ARRAY,

      ObjectReader._WAIT_IO
    );

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(reader.channelPosition, 3343);

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 270);

    assertEquals(reader.deltaArray.size(), 8 + 4);

    assertEquals(reader.deltaStack.size(), 1);

    assertEquals(reader.deltaStack.peek(), 0);

    executeAndAssertState(
      reader,

      ObjectReader._PARSE_PACK_HEADER,

      ObjectReader._INFLATE,

      ObjectReader._BASE_OBJECT,

      ObjectReader._WAIT_IO
    );

    assertEquals(reader.channelPosition, 3343);

    assertEquals(reader.channelReadCount, 64 + 64);

    assertEquals(reader.channelReadLimit, 270);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._BASE_OBJECT);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._RECONSTRUCT_OBJECT);

    reader.executeOne();

    Concurrent.exhaust(reader);

    reader.acceptResultConsumer(this);

    Object result;
    result = getResult();

    assertSame(result, Result.INSTANCE);

    assertEquals(adapter.byteArray.toByteArray(), TestCase12.getTreeContents());
  }

  @Test(description = TestCase13.DESCRIPTION)
  public void testCase13() throws IOException, ExecutionException {
    Repository repository;
    repository = TestCase13.getRepository();

    ObjectId oid;
    oid = TestCase13.getBlob();

    ReadObjectAdapter adapter;
    adapter = new ReadObjectAdapter(repository, oid);

    reader.set(adapter);

    assertTrue(reader.isActive());

    assertEquals(reader.state, ObjectReader._START);

    executeAndAssertState(
      reader,

      ObjectReader._NEXT_PACK_FILE,

      ObjectReader._NEXT_PACK_FILE_OBJECT,

      ObjectReader._IO_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO,

      ObjectReader._PARSE_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO,

      ObjectReader._PARSE_INDEX_OBJECT_NAME,

      ObjectReader._WAIT_IO,

      ObjectReader._PARSE_INDEX_OFFSET
    );

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(oid.getFanOutIndex(), 193);

    assertEquals(reader.channelPosition, 203);

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 6131);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._PARSE_PACK_HEADER);

    reader.executeOne();

    assertEquals(reader.objectPosition, 0);

    assertEquals(reader.objectLength, 68590);

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.channelPosition, 203);

    assertEquals(reader.channelReadCount, 64 + 64);

    assertEquals(reader.channelReadLimit, 6131);

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    Concurrent.exhaust(reader);

    reader.acceptResultConsumer(this);

    Object result;
    result = getResult();

    assertSame(result, Result.INSTANCE);

    assertEquals(adapter.byteArray.toByteArray(), TestCase13.getBlobContents());
  }

  @Test(description = TestCase14.DESCRIPTION)
  public void testCase14() throws Throwable {
    Repository repository;
    repository = TestCase14.getRepository();

    ObjectId oid;
    oid = TestCase14.getBlob();

    ReadObjectAdapter adapter;
    adapter = new ReadObjectAdapter(repository, oid);

    reader.set(adapter);

    assertTrue(reader.isActive());

    assertEquals(reader.state, ObjectReader._START);

    executeAndAssertState(
      reader,

      ObjectReader._NEXT_PACK_FILE,

      ObjectReader._NEXT_PACK_FILE_OBJECT,

      ObjectReader._IO_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO,

      ObjectReader._PARSE_INDEX_FAN_OUT,

      ObjectReader._WAIT_IO,

      ObjectReader._PARSE_INDEX_OBJECT_NAME,

      ObjectReader._WAIT_IO,

      ObjectReader._PARSE_INDEX_OFFSET
    );

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    assertEquals(oid.getFanOutIndex(), 11);

    assertEquals(reader.channelPosition, 7373);

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 14136);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._PARSE_PACK_HEADER);

    reader.executeOne();

    assertEquals(reader.objectPosition, 0);

    assertEquals(reader.objectLength, 24);

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._DELTA_ARRAY);

    reader.executeOne();

    assertEquals(reader.channelPosition, 6902);

    assertEquals(reader.channelReadCount, 64);

    assertEquals(reader.channelReadLimit, 14607);

    assertEquals(reader.deltaArray.size(), 8 + 24);

    assertEquals(reader.deltaStack.size(), 1);

    assertEquals(reader.deltaStack.peek(), 0);

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._PARSE_PACK_HEADER);

    reader.executeOne();

    assertEquals(reader.objectPosition, 0);

    assertEquals(reader.objectLength, 744);

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.channelPosition, 6902);

    assertEquals(reader.channelReadCount, 64 + 64);

    assertEquals(reader.channelReadLimit, 14607);

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._DELTA_ARRAY);

    reader.executeOne();

    assertEquals(reader.deltaArray.size(), 8 + 24 + 72);

    assertEquals(reader.deltaStack.size(), 2);

    assertEquals(reader.deltaStack.peek(), 32);

    assertEquals(reader.state, ObjectReader._INFLATE);

    reader.executeOne();

    assertEquals(reader.state, ObjectReader._DELTA_ARRAY);

    reader.executeOne();

    assertEquals(reader.deltaArray.size(), 8 + 24 + 72 + 14);

    assertEquals(reader.deltaStack.size(), 2);

    assertEquals(reader.deltaStack.peek(), 32);

    assertEquals(reader.channelPosition, 6902);

    assertEquals(reader.channelReadCount, 64 + 64 + 64);

    assertEquals(reader.channelReadLimit, 14607);

    assertEquals(reader.state, ObjectReader._WAIT_IO);

    executeUntil(
      reader,

      ObjectReader._PARSE_PACK_HEADER
    );

    reader.executeOne();

    assertEquals(reader.objectPosition, 0);

    assertEquals(reader.objectLength, 72316);

    assertEquals(reader.state, ObjectReader._INFLATE);

    while (reader.objectPosition < reader.objectLength) {
      reader.executeOne();
    }

    assertEquals(reader.state, ObjectReader._RECONSTRUCT_OBJECT);

    reader.executeOne();

    Concurrent.exhaust(reader);

    reader.acceptResultConsumer(this);

    if (error != null) {
      throw error;
    }

    Object result;
    result = getResult();

    assertSame(result, Result.INSTANCE);

    assertEquals(adapter.byteArray.toByteArray(), TestCase14.getBlobContents());
  }

  private void executeAndAssertState(ObjectReader reader, byte... states) {
    for (byte expected : states) {
      assertTrue(reader.isActive());

      reader.executeOne();

      assertEquals(reader.state, expected);
    }
  }

  private void executeUntil(ObjectReader reader, byte expected) {
    while (reader.state != expected) {
      reader.executeOne();
    }
  }

  private abstract class AbstractAdapter implements ObjectReaderAdapter {

    ObjectReaderHandle handle;

    final GitRepository repository;

    AbstractAdapter(GitRepository repository) {
      this.repository = repository;
    }

    @Override
    public void executeObjectFinish() {}

    @Override
    public void executeObjectNotFound(ObjectId objectId) {}

    @Override
    public void executeObjectStart(ObjectId objectId) {}

    @Override
    public void executeStart(ObjectReaderHandle handle) {
      this.handle = handle;
    }

    @Override
    public final GitRepository getRepository() {
      return repository;
    }

  }

  private class FilterNonExistingAdapter extends AbstractAdapter {

    private final Set<ObjectId> objects;

    private final GrowableSet<ObjectId> result = new GrowableSet<>();

    FilterNonExistingAdapter(GitRepository repository, Set<ObjectId> objects) {
      super(repository);

      this.objects = objects;
    }

    @Override
    public final void executeFinally() {
      handle.setResult(
        result.toUnmodifiableSet()
      );
    }

    @Override
    public final void executeObjectBodyFull(byte[] array, int length, ByteBuffer buffer) {}

    @Override
    public final void executeObjectBodyPart(ByteBuffer buffer) {}

    @Override
    public final void executeObjectHeader(ObjectKind kind, long length) {}

    @Override
    public final void executeObjectNotFound(ObjectId objectId) {
      result.add(objectId);
    }

    @Override
    public final void executeStart(ObjectReaderHandle handle) {
      super.executeStart(handle);

      handle.setInputMany(ObjectReaderMode.EXISTS, objects);
    }

  }

  private class ReadObjectAdapter extends AbstractAdapter {

    final ByteArrayWriter byteArray = new ByteArrayWriter();

    ObjectKind objectKind;

    private final ObjectId objectId;

    ReadObjectAdapter(GitRepository repository, ObjectId objectId) {
      super(repository);
      this.objectId = objectId;
    }

    @Override
    public final void executeFinally() {
      handle.setResult(Result.INSTANCE);
    }

    @Override
    public final void executeObjectBodyFull(byte[] array, int length, ByteBuffer buffer) {
      byteArray.write(array, 0, length);
    }

    @Override
    public final void executeObjectBodyPart(ByteBuffer buffer) {
      byteArray.write(buffer);
    }

    @Override
    public final void executeObjectHeader(ObjectKind kind, long length) {
      objectKind = kind;
    }

    @Override
    public final void executeStart(ObjectReaderHandle handle) {
      super.executeStart(handle);

      handle.setInput(ObjectReaderMode.READ_OBJECT, objectId);
    }

  }

  private enum Result {
    INSTANCE;
  }

}