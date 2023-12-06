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
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReadTreeTest extends AbstractGitTest {

  private ByteBuffer byteBuffer;

  private ReadTree job;

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    job.executeFinally();
  }

  @BeforeClass
  public void beforeClass() {
    job = new ReadTree(engine);

    byteBuffer = ByteBuffer.allocate(engine.getBufferSize());
  }

  @Test(description = TestCase00.DESCRIPTION)
  public void testCase00() throws IOException {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase00.getTree();

    job.set(handle, oid);

    job.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase00.getTreeContents();

    assertEquals(contents.length, 37);

    job.executeObjectHeader(ObjectKind.TREE, contents.length);

    assertNull(handle.error);

    ByteBuffer buffer;
    buffer = ByteBuffer.wrap(contents);

    assertEquals(buffer.remaining(), 37);

    job.executeObjectBodyPart(buffer);

    assertEquals(job.state, ReadTree._RESULT);

    assertEquals(buffer.remaining(), 0);

    assertNull(handle.error);

    Tree result;
    result = (Tree) handle.result;

    assertEquals(result.objectId, oid);

    UnmodifiableList<Entry> entries;
    entries = result.getEntries();

    assertEquals(entries.size(), 1);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));
  }

  @Test(description = TestCase01.DESCRIPTION)
  public void testCase01() throws Exception {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase01.getTree();

    job.set(handle, oid);

    job.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase01.getTreeContents();

    assertEquals(contents.length, 67);

    job.executeObjectHeader(ObjectKind.TREE, contents.length);

    assertNull(handle.error);

    ByteBuffer buffer;
    buffer = ByteBuffer.wrap(contents);

    assertEquals(buffer.remaining(), 67);

    buffer.limit(64);

    job.executeObjectBodyPart(buffer);

    buffer.limit(buffer.capacity());

    assertEquals(buffer.remaining(), 3);

    assertNull(handle.error);

    job.executeObjectBodyPart(buffer);

    assertEquals(job.state, ReadTree._RESULT);

    assertEquals(buffer.remaining(), 0);

    assertNull(handle.error);

    Tree result;
    result = (Tree) handle.result;

    assertEquals(result.objectId, oid);

    UnmodifiableList<Entry> entries;
    entries = result.getEntries();

    assertEquals(entries.size(), 2);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));

    Entry e1;
    e1 = entries.get(1);

    assertEquals(e1.mode, EntryMode.TREE);
    assertEquals(e1.name, "bin");
    assertEquals(e1.object, ObjectId.parse("dcfae91ae42930b3c7aa438404fb96b5c25068e7"));
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws InvalidObjectIdFormatException {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase12.getTreeDeltified();

    job.set(handle, oid);

    job.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase12.getTreeContents();

    assertEquals(contents.length, 67);

    job.executeObjectHeader(ObjectKind.TREE, contents.length);

    assertNull(handle.error);

    job.executeObjectBodyFull(contents, contents.length, byteBuffer);

    assertEquals(job.state, ReadTree._RESULT);

    assertNull(handle.error);

    Tree result;
    result = (Tree) handle.result;

    assertEquals(result.objectId, oid);

    UnmodifiableList<Entry> entries;
    entries = result.getEntries();

    assertEquals(entries.size(), 2);

    Entry e0;
    e0 = entries.get(0);

    assertEquals(e0.mode, EntryMode.REGULAR_FILE);
    assertEquals(e0.name, "README.md");
    assertEquals(e0.object, ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4"));

    Entry e1;
    e1 = entries.get(1);

    assertEquals(e1.mode, EntryMode.TREE);
    assertEquals(e1.name, "bin");
    assertEquals(e1.object, ObjectId.parse("dcfae91ae42930b3c7aa438404fb96b5c25068e7"));
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
