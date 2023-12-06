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
import objectos.core.io.Charsets;
import objectos.fs.ResolvedPath;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReadBlobTest extends AbstractGitTest {

  private ByteBuffer byteBuffer;

  private ReadBlob job;

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    job.executeFinally();
  }

  @BeforeClass
  public void beforeClass() {
    job = new ReadBlob(engine);

    byteBuffer = ByteBuffer.allocate(engine.getBufferSize());
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws IOException {
    ThisHandle handle;
    handle = new ThisHandle();

    ObjectId oid;
    oid = TestCase12.getBlobDeltified();

    job.set(handle, oid);

    job.executeStart(handle);

    assertEquals(handle.objectId, oid);

    assertNull(handle.error);

    byte[] contents;
    contents = TestCase12.getBlobContents();

    assertEquals(contents.length, 68);

    job.executeObjectHeader(ObjectKind.BLOB, contents.length);

    assertNull(handle.error);

    job.executeObjectBodyFull(contents, contents.length, byteBuffer);

    assertNull(handle.error);

    Blob result;
    result = (Blob) handle.result;

    assertEquals(
      result.toString(Charsets.utf8()),

      String.join(
        System.lineSeparator(),

        "# ObjectosRepo",
        "",
        "This is a git repository meant to be used in tests.",
        ""
      )
    );
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