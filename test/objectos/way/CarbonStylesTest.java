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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CarbonStylesTest extends Http.Module {

  private static final String CARBON01 = """
  cafebabe0000004100310700020100156f626a6563746f732f7761792f436172626f6e303107000401001c6f626a6563746f732f7761792f436172626f6e2454656d706c6174650100063c696e69743e010003282956010004436f64650a000300090c0005000601000f4c696e654e756d6265725461626c\
  650100124c6f63616c5661726961626c655461626c65010004746869730100174c6f626a6563746f732f7761792f436172626f6e30313b01000672656e64657207001001001d6f626a6563746f732f7761792f48746d6c24496e737472756374696f6e08001201000b62672d7a696e632d3530300a000100\
  140c00150016010009636c6173734e616d6501003c284c6a6176612f6c616e672f537472696e673b294c6f626a6563746f732f7761792f48746d6c24417474726962757465496e737472756374696f6e3b0a000100180c0019001a010003646976010048285b4c6f626a6563746f732f7761792f48746d6c\
  24496e737472756374696f6e3b294c6f626a6563746f732f7761792f48746d6c24456c656d656e74496e737472756374696f6e3b01000a536f7572636546696c6501000d436172626f6e30312e6a61766101001b52756e74696d65496e76697369626c65416e6e6f746174696f6e730100194c6f626a6563\
  746f732f7761792f43737324536f757263653b01000c496e6e6572436c61737365730700210100136f626a6563746f732f7761792f436172626f6e01000854656d706c6174650700240100176f626a6563746f732f7761792f43737324536f757263650700260100106f626a6563746f732f7761792f4373\
  73010006536f757263650700290100266f626a6563746f732f7761792f48746d6c24417474726962757465496e737472756374696f6e07002b0100116f626a6563746f732f7761792f48746d6c010014417474726962757465496e737472756374696f6e07002e0100246f626a6563746f732f7761792f48\
  746d6c24456c656d656e74496e737472756374696f6e010012456c656d656e74496e737472756374696f6e01000b496e737472756374696f6e003000010003000000000002000000050006000100070000002f00010001000000052ab70008b100000002000a00000006000100000013000b0000000c0001\
  00000005000c000d00000014000e0006000100070000004100060001000000132a04bd000f59032a1211b6001353b6001757b100000002000a0000000a00020000001700120018000b0000000c000100000013000c000d00000003001b00000002001c001d000000060001001e0000001f0000002a000500\
  0300200022040900230025002726090028002a002c0609002d002a002f0609000f002a00300609\
  """;

  private Path directory;

  private Http.Handler stylesHandler;

  private HttpClient client;

  @BeforeClass
  public void beforeClass() throws IOException {
    directory = TestingDir.next();

    TestingDir.hexDump(directory.resolve("objectos/way/Carbon01.class"), CARBON01);

    stylesHandler = Carbon.generateOnGetHandler(TestingNoteSink.INSTANCE, directory);

    TestingHttpServer.bindCarbonStylesTest(this);

    System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");

    client = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() {
    if (client != null) {
      client.close();
    }
  }

  @Override
  protected final void configure() {
    route("/carbon.css", stylesHandler);
  }

  @Test
  public void testCase01() throws IOException, InterruptedException {
    int port = TestingHttpServer.port();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/carbon.css"))
        .header("Host", "carbon.styles.test")
        .timeout(Duration.ofMinutes(1))
        .build();

    HttpResponse<String> response;
    response = client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));

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