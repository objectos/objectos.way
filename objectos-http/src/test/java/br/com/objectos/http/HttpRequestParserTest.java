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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import objectos.util.UnmodifiableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpRequestParserTest extends AbstractHttpTest implements HttpProcessor {

  private TestableSocketChannel channel;

  private boolean executed;

  private HttpResponseHandle handle;

  private Map<String, String> headerMap;

  private Method method;

  private HttpEngine parser;

  private RequestTarget target;

  private Version version;

  @BeforeClass
  public void _beforeClass() {
    channel = new TestableSocketChannel();

    headerMap = new HashMap<String, String>();

    parser = new HttpEngine(
      64,

      logger,

      this,

      stringDeduplicator
    );
  }

  @BeforeMethod
  public void _beforeMethod() {
    channel.clear();

    executed = false;

    handle = null;

    headerMap.clear();

    method = null;

    target = null;

    version = null;
  }

  @Override
  public final void requestHeader(String name, String value) {
    headerMap.put(name, value);
  }

  @Override
  public final void requestLine(Method method, RequestTarget target, Version version) {
    this.method = method;

    this.target = target;

    this.version = version;
  }

  @Override
  public final void requestStart(HttpResponseHandle handle) {
    this.handle = handle;
  }

  @Override
  public final ResponseTask responseTask() {
    return new ExecuteOneTask();
  }

  @Test(description = TestCase0001.DESCRIPTION)
  public void testCase01() throws Throwable {
    channel.setRequest(TestCase0001.REQUEST);

    parser.setInput(channel);

    assertEquals(parser.state, HttpEngine._START);

    executeOne();

    assertEquals(parser.channelEof, false);

    assertEquals(parser.channelReadTotal, 64);

    assertEquals(parser.decodeAction, HttpEngine._PARSE);

    assertEquals(parser.ioReady, HttpEngine._DECODE);

    assertEquals(parser.ioTask, HttpEngine.IO_READ);

    assertEquals(parser.state, HttpEngine._WAIT_IO);

    executeOne();

    assertEquals(parser.state, HttpEngine._DECODE);

    executeOne();

    assertEquals(parser.byteBuffer.hasRemaining(), false);

    assertEquals(parser.charBuffer.hasRemaining(), false);

    assertEquals(parser.state, HttpEngine._PARSE);

    assertEquals(
      parser.stringValue(),

      crlf(
        "GET / HTTP/1.1",
        "Host: localhost:7001",
        "Connection: keep-alive",
        "se"
      )
    );

    executeOne();

    assertEquals(parser.channelEof, false);

    assertEquals(parser.channelReadTotal, 64 + 64);

    assertEquals(parser.decodeAction, HttpEngine._PARSE_HEADER_NAME);

    assertEquals(headerMap.size(), 2);
    assertEquals(headerMap.get("host"), "localhost:7001");
    assertEquals(headerMap.get("connection"), "keep-alive");
    headerMap.clear();

    assertEquals(parser.ioReady, HttpEngine._DECODE);

    assertEquals(parser.ioTask, HttpEngine.IO_READ);

    assertEquals(method, Method.GET);

    assertEquals(parser.state, HttpEngine._WAIT_IO);

    assertEquals(target.pathEquals("/"), true);

    assertEquals(version, Version.V1_1);

    executeOne();

    assertEquals(parser.state, HttpEngine._DECODE);

    executeOne();

    assertEquals(parser.byteBuffer.hasRemaining(), true);

    assertEquals(parser.charBuffer.hasRemaining(), false);

    assertEquals(parser.state, HttpEngine._PARSE_HEADER_NAME);

    assertEquals(
      parser.stringValue(),

      "sec-ch-ua: \" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chrom"
    );

    executeOne();

    assertEquals(parser.channelEof, false);

    assertEquals(parser.channelReadTotal, 64 + 64 + 62);

    assertEquals(parser.decodeAction, HttpEngine._PARSE_HEADER_VALUE);

    assertEquals(headerMap.size(), 0);

    assertEquals(parser.ioReady, HttpEngine._DECODE);

    assertEquals(parser.ioTask, HttpEngine.IO_READ);

    assertEquals(parser.state, HttpEngine._WAIT_IO);

    executeOne();

    assertEquals(parser.state, HttpEngine._DECODE);

    executeOne();

    assertEquals(parser.byteBuffer.hasRemaining(), false);

    assertEquals(parser.charBuffer.hasRemaining(), false);

    assertEquals(parser.state, HttpEngine._PARSE_HEADER_VALUE);

    assertEquals(
      parser.stringValue(),

      crlf(
        "ium\";v=\"97\"",
        "sec-ch-ua-mobile: ?0",
        "sec-ch-ua-platform: \"Linux\"",
        ""
      )
    );

    executeOne();

    assertEquals(parser.channelEof, false);

    assertEquals(parser.channelReadTotal, 64 + 64 + 62 + 64);

    assertEquals(parser.decodeAction, HttpEngine._PARSE_HEADER);

    assertEquals(headerMap.size(), 3);
    assertEquals(headerMap.get("sec-ch-ua"),
      "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
    assertEquals(headerMap.get("sec-ch-ua-mobile"), "?0");
    assertEquals(headerMap.get("sec-ch-ua-platform"), "\"Linux\"");
    headerMap.clear();

    assertEquals(parser.ioReady, HttpEngine._DECODE);

    assertEquals(parser.ioTask, HttpEngine.IO_READ);

    assertEquals(parser.state, HttpEngine._WAIT_IO);

    exhaust(parser);

    if (parser.error != null) {
      throw parser.error;
    }

    assertNotNull(handle);

    assertTrue(executed);
  }

  private void exhaust(HttpEngine engine) {
    while (engine.isActive()) {
      engine.executeOne();
    }
  }

  private String crlf(String... strings) {
    UnmodifiableList<String> list;
    list = UnmodifiableList.copyOf(strings);

    return list.join(Http.CRLF);
  }

  private void executeOne() throws Throwable {
    parser.executeOne();

    if (parser.error != null) {
      throw parser.error;
    }
  }

  private class ExecuteOneTask implements ResponseTask {

    @Override
    public final void executeOne() {
      executed = true;
    }

    @Override
    public final boolean isActive() {
      return !executed;
    }

  }

}