/*
 * Copyright (C) 2011-2023 Objectos Software LTDA.
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
package objectos.fs.testing;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import objectos.fs.Directory;
import org.testng.annotations.Test;

public class TmpDirTest {

  @Test
  public void create() throws IOException {
    Directory dir;
    dir = TmpDir.create();

    String result;
    result = dir.getPath();

    String tmpdir;
    tmpdir = System.getProperty("java.io.tmpdir");

    assertTrue(result.startsWith(tmpdir));
  }

}
