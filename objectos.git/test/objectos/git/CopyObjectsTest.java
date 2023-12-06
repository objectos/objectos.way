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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.concurrent.Concurrent;
import objectos.fs.Directory;
import objectos.util.collection.UnmodifiableIterator;
import objectos.util.set.UnmodifiableSet;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CopyObjectsTest extends AbstractGitTest {

  private CopyObjects job;

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    Concurrent.exhaust(job);
  }

  @BeforeClass
  public void beforeClass() {
    job = new CopyObjects(engine);
  }

  @Test(description = TestCase05.DESCRIPTION)
  public void testCase05() throws IOException, ExecutionException {
    Directory srcDir;
    srcDir = TestCase05.getDirectory();

    Repository src;
    src = openRepository(srcDir);

    UnmodifiableSet<ObjectId> set;
    set = TestCase05.getCopyObjectSet();

    Repository dest;
    dest = createEmptyRepository();

    assertFalse(job.isActive());

    job.setInput(src, set, dest);

    assertTrue(job.isActive());

    assertEquals(set.size(), 4);

    UnmodifiableIterator<ObjectId> it;
    it = set.iterator();

    assertLooseNotFound(dest, it.next());
    assertLooseNotFound(dest, it.next());
    assertLooseNotFound(dest, it.next());
    assertLooseNotFound(dest, it.next());

    Concurrent.exhaust(job);

    job.acceptResultConsumer(this);

    it = set.iterator();

    assertLooseExists(dest, it.next());
    assertLooseExists(dest, it.next());
    assertLooseExists(dest, it.next());
    assertLooseExists(dest, it.next());
  }

  @Test(description = TestCase13.DESCRIPTION)
  public void testCase13() throws Throwable {
    Directory srcDir;
    srcDir = TestCase13.getDirectory();

    Repository src;
    src = openRepository(srcDir);

    UnmodifiableSet<ObjectId> set;
    set = TestCase13.getCopyObjectSet();

    Repository dest;
    dest = createEmptyRepository();

    assertFalse(job.isActive());

    job.setInput(src, set, dest);

    assertTrue(job.isActive());

    assertEquals(set.size(), 1);

    UnmodifiableIterator<ObjectId> it;
    it = set.iterator();

    assertLooseNotFound(dest, it.next());

    Concurrent.exhaust(job);

    job.acceptResultConsumer(this);

    it = set.iterator();

    assertLooseExists(dest, it.next());
  }

  @Test(description = TestCase14.DESCRIPTION)
  public void testCase14() throws Throwable {
    Directory srcDir;
    srcDir = TestCase14.getDirectory();

    Repository src;
    src = openRepository(srcDir);

    UnmodifiableSet<ObjectId> set;
    set = TestCase14.getCopyObjectSet();

    Repository dest;
    dest = createEmptyRepository();

    assertFalse(job.isActive());

    job.setInput(src, set, dest);

    assertTrue(job.isActive());

    assertEquals(set.size(), 1);

    UnmodifiableIterator<ObjectId> it;
    it = set.iterator();

    assertLooseNotFound(dest, it.next());

    Concurrent.exhaust(job);

    job.acceptResultConsumer(this);

    if (error != null) {
      throw error;
    }

    it = set.iterator();

    assertLooseExists(dest, it.next());
  }

}