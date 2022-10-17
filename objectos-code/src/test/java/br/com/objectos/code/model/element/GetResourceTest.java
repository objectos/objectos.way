/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.model.element;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.testng.annotations.Test;

public class GetResourceTest {

  @Test
  public void getResource() throws IOException {
    Class<? extends GetResourceTest> type = getClass();

    Module module = type.getModule();

    try (
        InputStream in = module.getResourceAsStream("br/com/objectos/code/util/get-resource.txt")
    ) {
      assertNotNull(in);

      String txt = readString(in);

      assertEquals(txt, "got it!");
    }

    try (
        InputStream in = AbstractCodeCoreTest.class.getResourceAsStream("get-resource.txt")
    ) {
      assertNotNull(in);

      String txt = readString(in);

      assertEquals(txt, "got it!");
    }

    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    URL url = loader.getResource("code-testing/get-resource.txt");

    assertNotNull(url);
  }

  private String readString(InputStream in) throws IOException {
    var out = new ByteArrayOutputStream();

    in.transferTo(out);

    return new String(out.toByteArray());
  }

}
