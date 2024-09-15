/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CarbonStylesTest extends Http.Module {

  private Path directory;

  private Http.Handler stylesHandler;

  @BeforeClass
  public void beforeClass() throws IOException {
    directory = TestingDir.next();

    TestingDir.hexDump(directory.resolve("objectos/way/Carbon01.class"), CarbonTest.CARBON01);

    stylesHandler = Carbon.generateOnGetHandler(TestingNoteSink.INSTANCE, directory);

    TestingHttpServer.bindCarbonStylesTest(this);
  }

  @Override
  protected final void configure() {
    route("/carbon.css", handler(stylesHandler));
  }

  @Test
  public void testCase01() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Testing.httpClient(
        "/carbon.css",
        Testing.headers(
            "Host", "carbon.styles.test",
            "Connection", "close"
        )
    );

    assertEquals(response.statusCode(), 200);

    String body;
    body = response.body();

    assertTrue(body.contains(".bg-zinc-500"));
  }

  @Test
  public void testCase02() throws IOException {
    Path file;
    file = directory.resolve("carbon.css");

    assertFalse(Files.exists(file));

    Carbon.generate(TestingNoteSink.INSTANCE, directory, file);

    String body;
    body = Files.readString(file);

    assertTrue(body.contains(".bg-zinc-500"));
  }

}