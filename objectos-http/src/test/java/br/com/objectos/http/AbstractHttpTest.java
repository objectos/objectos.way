/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.BeforeSuite;

public abstract class AbstractHttpTest {

  protected static HttpTestingLogger logger;

  protected static HttpService service;

  protected static HashMapStringDeduplicator stringDeduplicator;

  private static AtomicInteger portGenerator = new AtomicInteger(5678);

  @BeforeSuite
  public static void beforeSuiteGitService() throws Exception {
    Path testInf;
    testInf = TestInf.get();

    Path siteDirectory;
    siteDirectory = testInf.resolve("site");

    HttpRequestProcessorProvider provider;
    provider = new HttpRequestProcessorProvider(siteDirectory);

    stringDeduplicator = new HashMapStringDeduplicator();

    logger = new HttpTestingLogger();

    InetSocketAddress address;
    address = nextLoopbackSocketAddress();

    service = HttpService.create(
      address,

      provider,

      HttpService.bufferSize(64),

      HttpService.logger(logger)
    );

    service.startService();
  }

  protected static InetSocketAddress nextLoopbackSocketAddress() {
    var loopback = InetAddress.getLoopbackAddress();

    int port;
    port = portGenerator.getAndIncrement();

    return new InetSocketAddress(loopback, port);
  }

  protected final URLConnection GET(String path) {
    try {
      URL url;
      url = new URL("http", "127.0.0.1", 5678, path);

      return url.openConnection();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected final String readString(URLConnection c) throws IOException {
    try (var in = c.getInputStream(); var out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      byte[] bytes;
      bytes = out.toByteArray();

      return new String(bytes, StandardCharsets.UTF_8);
    }
  }

}