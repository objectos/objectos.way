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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import objectos.concurrent.Concurrent;
import objectos.fs.Directory;
import org.testng.annotations.Test;

/**
 * Opens a Git packfile. Resolves the pathname and confirms (or not) that the
 * file exist. Validates the header and (maybe) the checksum. Opens the
 * associated index file (if one exists).
 *
 * <p>
 * Apart from validating the header and (maybe) the checksum, does not do any
 * further reading.
 *
 * @since 3
 */
public class OpenPackFileTest extends AbstractGitTest {

  @Test(description = TestCase12.DESCRIPTION)
  public void testCase12() throws IOException {
    Directory objects;
    objects = TestCase12.getObjectsDirectory();

    ObjectId packName;
    packName = TestCase12.getPackName();

    OpenPackFile task;
    task = engine.getOpenPackFile();

    task.set(objects, packName);

    Concurrent.exhaust(task);

    task.acceptResultConsumer(this);

    PackFile result;
    result = getResult();

    assertTrue(result instanceof IndexedPackFile);

    assertEquals(result.getObjectCount(), 51);

    assertEquals(result.getVersion(), 2);
  }

}
