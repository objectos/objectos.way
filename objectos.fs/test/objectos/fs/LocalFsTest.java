/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.fs;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.core.io.Charsets;
import objectos.core.io.Read;
import objectos.core.io.Resource;
import org.testng.annotations.Test;

public class LocalFsTest {

  @Test(description = "getRegularFile(Resource)")
  public void getRegularFile2() throws NotFoundException, NotRegularFileException, IOException {
    Resource resource;
    resource = Resource.getResource("resource.txt");

    RegularFile file;
    file = LocalFs.getRegularFile(resource);

    assertEquals(
        Read.string(file, Charsets.utf8()),
        "from Resource.getResource()"
    );
  }

}