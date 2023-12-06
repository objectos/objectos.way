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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReadCommitTest extends AbstractGitTest {

  private ByteBuffer byteBuffer;

  private ReadCommit adapter;

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    adapter.executeFinally();
  }

  @BeforeClass
  public void beforeClass() {
    adapter = new ReadCommit(engine);

    byteBuffer = ByteBuffer.allocate(engine.getBufferSize());
  }

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws IOException {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase00.getCommit();

    adapter.set(handle, oid);

    adapter.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase00.getCommitContents();

    assertEquals(contents.length, 182);

    adapter.executeObjectHeader(ObjectKind.COMMIT, contents.length);

    assertNull(handle.error);

    ByteBuffer buffer;
    buffer = ByteBuffer.wrap(contents);

    assertEquals(buffer.remaining(), 182);

    adapter.executeObjectBodyPart(buffer);

    assertNull(handle.error);

    assertEquals(buffer.remaining(), 182 - 64);

    adapter.executeObjectBodyPart(buffer);

    assertNull(handle.error);

    assertEquals(buffer.remaining(), 182 - 64 - 64);

    adapter.executeObjectBodyPart(buffer);

    assertEquals(adapter.state, ReadCommit._RESULT);

    assertEquals(buffer.remaining(), 0);

    assertNull(handle.error);

    Commit result;
    result = (Commit) handle.result;

    assertNotNull(result);

    assertEquals(
      result.getMessage(),

      String.join(
        System.lineSeparator(),

        "add README.md",
        ""
      )
    );

    UnmodifiableList<ObjectId> parents;
    parents = result.getParents();

    assertEquals(parents.size(), 0);

    assertEquals(result.getTree(), ObjectId.parse("1cd042294d3933032f5fbb9735034dcbce689dc9"));
  }

  @Test(description = TestCase01.DESCRIPTION)
  public void testCase01() throws InvalidObjectIdFormatException {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase01.getCommit();

    adapter.set(handle, oid);

    adapter.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase01.getCommitContents();

    assertEquals(contents.length, 238);

    adapter.executeObjectHeader(ObjectKind.COMMIT, contents.length);

    assertNull(handle.error);

    ByteBuffer buffer;
    buffer = ByteBuffer.wrap(contents);

    assertEquals(buffer.remaining(), 238);

    adapter.executeObjectBodyPart(buffer);

    assertEquals(buffer.remaining(), 238 - 64);

    assertNull(handle.error);

    adapter.executeObjectBodyPart(buffer);

    assertEquals(buffer.remaining(), 238 - 64 - 64);

    assertNull(handle.error);

    adapter.executeObjectBodyPart(buffer);

    assertEquals(buffer.remaining(), 238 - 64 - 64 - 64);

    assertNull(handle.error);

    adapter.executeObjectBodyPart(buffer);

    assertEquals(buffer.remaining(), 0);

    assertNull(handle.error);

    assertEquals(adapter.state, ReadCommit._RESULT);

    Commit result;
    result = (Commit) handle.result;

    assertNotNull(result);

    assertEquals(
      result.getMessage(),

      String.join(
        System.lineSeparator(),

        "[bin] add a ci script",
        ""
      )
    );

    UnmodifiableList<ObjectId> parents;
    parents = result.getParents();

    assertEquals(parents.size(), 1);

    ObjectId parent0;
    parent0 = parents.get(0);

    assertEquals(parent0, ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93"));

    assertEquals(result.getTree(), ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1"));
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws InvalidObjectIdFormatException {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase12.getCommitNonDeltified();

    adapter.set(handle, oid);

    adapter.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase12.getCommitContents();

    assertEquals(contents.length, 238);

    adapter.executeObjectHeader(ObjectKind.COMMIT, contents.length);

    assertNull(handle.error);

    adapter.executeObjectBodyFull(contents, contents.length, byteBuffer);

    assertNull(handle.error);

    assertEquals(adapter.state, ReadCommit._RESULT);

    Commit result;
    result = (Commit) handle.result;

    assertNotNull(result);

    assertEquals(
      result.getMessage(),

      String.join(
        System.lineSeparator(),

        "[bin] add a ci script",
        ""
      )
    );

    UnmodifiableList<ObjectId> parents;
    parents = result.getParents();

    assertEquals(parents.size(), 1);

    ObjectId parent0;
    parent0 = parents.get(0);

    assertEquals(parent0, ObjectId.parse("b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93"));

    assertEquals(result.getTree(), ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1"));
  }

  private class ThisHandle extends GitRepository implements ObjectReaderHandle {

    private Throwable error;

    private ObjectId objectId;

    private Object result;

    @Override
    public final void catchThrowable(Throwable t) {
      error = t;
    }

    @Override
    public final void setInput(ObjectReaderMode mode, ObjectId single) {
      objectId = single;
    }

    @Override
    public final void setInputMany(ObjectReaderMode mode, Iterable<ObjectId> many) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final void setResult(Object r) {
      result = r;
    }

    @Override
    final PackFile getPackFile(int index) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    final int getPackFileCount() {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    final ResolvedPath resolveLooseObject(ObjectId objectId) throws IOException {
      throw new UnsupportedOperationException("Implement me");
    }

  }

}