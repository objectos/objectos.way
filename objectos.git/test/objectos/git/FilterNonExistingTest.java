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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.fs.Directory;
import objectos.fs.ResolvedPath;
import objectos.util.list.UnmodifiableList;
import objectos.util.set.UnmodifiableSet;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FilterNonExistingTest extends AbstractGitTest {

  public Iterable<ObjectId> input;

  private Throwable error;

  private ThisHandle handle;

  private FilterNonExisting job;

  private ObjectReaderMode mode;

  private UnmodifiableSet<ObjectId> result;

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    job.executeFinally();
  }

  @BeforeClass
  public void beforeClass() {
    job = new FilterNonExisting(engine);

    handle = new ThisHandle();
  }

  @BeforeMethod
  public void beforeMethod() {
    input = null;

    error = null;

    mode = null;

    result = null;
  }

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase05() throws IOException, ExecutionException {
    Directory srcDir;
    srcDir = TestCase05.getDirectory();

    Repository src;
    src = openRepository(srcDir);

    UnmodifiableSet<ObjectId> set;
    set = TestCase05.getCopyObjectSet();

    job.set(src, set);

    job.executeStart(handle);

    assertSame(mode, ObjectReaderMode.EXISTS);

    assertSame(input, set);

    UnmodifiableList<ObjectId> list;
    list = UnmodifiableList.copyOf(set);

    job.executeObjectStart(list.get(0));

    job.executeObjectNotFound(list.get(1));
    job.executeObjectNotFound(list.get(2));
    job.executeObjectNotFound(list.get(3));

    job.executeResult();

    assertNull(error);

    assertNotNull(result);

    assertEquals(result.size(), 3);

    assertFalse(result.contains(list.get(0)));

    assertTrue(result.contains(list.get(1)));
    assertTrue(result.contains(list.get(2)));
    assertTrue(result.contains(list.get(3)));
  }

  private class ThisHandle extends GitRepository implements ObjectReaderHandle {

    private final FilterNonExistingTest outer = FilterNonExistingTest.this;

    @Override
    public final void catchThrowable(Throwable t) {
      outer.error = t;
    }

    @Override
    public final void setInput(ObjectReaderMode mode, ObjectId single) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final void setInputMany(ObjectReaderMode mode, Iterable<ObjectId> many) {
      outer.mode = mode;

      outer.input = many;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void setResult(Object r) {
      outer.result = (UnmodifiableSet<ObjectId>) r;
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