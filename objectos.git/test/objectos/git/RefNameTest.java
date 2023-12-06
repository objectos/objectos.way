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

import java.io.IOException;
import objectos.core.io.Charsets;
import objectos.core.io.Write;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public class RefNameTest {

  @Test
  public void namedBranch() {
    RefName master;
    master = Git.MASTER;

    assertEquals(master.getClass().getSimpleName(), "RefsHeads");
  }

  @Test
  public void resolveLoose() throws GitStubException, IOException {
    Directory root;
    root = TmpDir.create();

    Directory refs;
    refs = root.createDirectory("refs");

    Directory heads;
    heads = refs.createDirectory("heads");

    RegularFile master;
    master = heads.createRegularFile("master");

    Write.string(
        master, Charsets.utf8(),

        "717271f0f0ee528c0bb094e8b2f84ea6cef7b39d\n"
    );

    RefName ref;
    ref = Git.MASTER;

    ResolvedPath maybe;
    maybe = ref.resolveLoose(root);

    RegularFile result;
    result = maybe.toRegularFile();

    assertEquals(result, master);
  }

}