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
package objectos.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import objectos.util.UnmodifiableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpExchangeTest extends AbstractHttpTest implements HttpProcessor {

  private HttpEngine engine;

  private boolean executed;

  private HttpResponseHandle handle;

  private Map<String, String> headers;

  private Method method;

  private TestableSocket socket;

  private RequestTarget target;

  private Version version;

  @BeforeClass
  public void _beforeClass() {
    socket = new TestableSocket();

    headers = new HashMap<String, String>();

    engine = new HttpEngine(
      64,

      noteSink,

      this,

      stringDeduplicator
    );
  }

  @BeforeMethod
  public void _beforeMethod() {
    socket.clear();

    executed = false;

    handle = null;

    headers.clear();

    method = null;

    target = null;

    version = null;
  }

  @Override
  public final void requestHeader(String name, String value) {
    headers.put(name, value);
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
    Socket socket;
    socket = TestCase0001.testableSocket();

    assertEquals(socket.isClosed(), false);

    HttpExchange exchange;
    exchange = new HttpExchange(64, noteSink, this, socket);

    assertEquals(exchange.state, HttpExchange._START);

    exchange.stepOne();

    assertEquals(exchange.state, HttpExchange._SOCKET_READ);

    assertEquals(exchange.socketReadAction, HttpExchange._REQUEST_METHOD);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);

    assertEquals(exchange.bufferLimit, 64);

    assertEquals(exchange.state, HttpExchange._REQUEST_METHOD);

    exchange.stepOne();

    // 'G' 'E' 'T' SP = 4
    assertEquals(exchange.bufferIndex, 4);

    assertEquals(exchange.method, Method.GET);

    assertEquals(exchange.state, HttpExchange._REQUEST_TARGET);

    exchange.stepOne();

    // '/' SP = 2
    assertEquals(exchange.bufferIndex, 6);

    assertEquals(exchange.requestTarget.pathEquals("/"), true);

    assertEquals(exchange.state, HttpExchange._REQUEST_VERSION);

    exchange.stepOne();

    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' CR LF = 10
    assertEquals(exchange.bufferIndex, 16);

    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER_NAME);

    assertEquals(exchange.version, Version.V1_1);
  }

  @Test(description = TestCase0001.DESCRIPTION)
  public void testCase01ToRemove() throws Throwable {
    socket.setRequest(TestCase0001.REQUEST);

    engine.setInput(socket);

    assertEquals(engine.state, HttpEngine._START);

    executeOne();

    assertEquals(engine.channelEof, false);

    assertEquals(engine.channelReadTotal, 64);

    assertEquals(engine.decodeAction, HttpEngine._PARSE);

    assertEquals(engine.ioReady, HttpEngine._DECODE);

    assertEquals(engine.ioTask, HttpEngine.IO_READ);

    assertEquals(engine.state, HttpEngine._WAIT_IO);

    executeOne();

    assertEquals(engine.state, HttpEngine._DECODE);

    executeOne();

    assertEquals(engine.byteBuffer.hasRemaining(), false);

    assertEquals(engine.charBuffer.hasRemaining(), false);

    assertEquals(engine.state, HttpEngine._PARSE);

    assertEquals(
      engine.stringValue(),

      crlf(
        "GET / HTTP/1.1",
        "Host: localhost:7001",
        "Connection: keep-alive",
        "se"
      )
    );

    executeOne();

    assertEquals(engine.channelEof, false);

    assertEquals(engine.channelReadTotal, 64 + 64);

    assertEquals(engine.decodeAction, HttpEngine._PARSE_HEADER_NAME);

    assertEquals(headers.size(), 2);
    assertEquals(headers.get("host"), "localhost:7001");
    assertEquals(headers.get("connection"), "keep-alive");
    headers.clear();

    assertEquals(engine.ioReady, HttpEngine._DECODE);

    assertEquals(engine.ioTask, HttpEngine.IO_READ);

    assertEquals(method, Method.GET);

    assertEquals(engine.state, HttpEngine._WAIT_IO);

    assertEquals(target.pathEquals("/"), true);

    assertEquals(version, Version.V1_1);

    executeOne();

    assertEquals(engine.state, HttpEngine._DECODE);

    executeOne();

    assertEquals(engine.byteBuffer.hasRemaining(), true);

    assertEquals(engine.charBuffer.hasRemaining(), false);

    assertEquals(engine.state, HttpEngine._PARSE_HEADER_NAME);

    assertEquals(
      engine.stringValue(),

      "sec-ch-ua: \" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chrom"
    );

    executeOne();

    assertEquals(engine.channelEof, false);

    assertEquals(engine.channelReadTotal, 64 + 64 + 62);

    assertEquals(engine.decodeAction, HttpEngine._PARSE_HEADER_VALUE);

    assertEquals(headers.size(), 0);

    assertEquals(engine.ioReady, HttpEngine._DECODE);

    assertEquals(engine.ioTask, HttpEngine.IO_READ);

    assertEquals(engine.state, HttpEngine._WAIT_IO);

    executeOne();

    assertEquals(engine.state, HttpEngine._DECODE);

    executeOne();

    assertEquals(engine.byteBuffer.hasRemaining(), false);

    assertEquals(engine.charBuffer.hasRemaining(), false);

    assertEquals(engine.state, HttpEngine._PARSE_HEADER_VALUE);

    assertEquals(
      engine.stringValue(),

      crlf(
        "ium\";v=\"97\"",
        "sec-ch-ua-mobile: ?0",
        "sec-ch-ua-platform: \"Linux\"",
        ""
      )
    );

    executeOne();

    assertEquals(engine.channelEof, false);

    assertEquals(engine.channelReadTotal, 64 + 64 + 62 + 64);

    assertEquals(engine.decodeAction, HttpEngine._PARSE_HEADER);

    assertEquals(headers.size(), 3);
    assertEquals(headers.get("sec-ch-ua"),
      "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
    assertEquals(headers.get("sec-ch-ua-mobile"), "?0");
    assertEquals(headers.get("sec-ch-ua-platform"), "\"Linux\"");
    headers.clear();

    assertEquals(engine.ioReady, HttpEngine._DECODE);

    assertEquals(engine.ioTask, HttpEngine.IO_READ);

    assertEquals(engine.state, HttpEngine._WAIT_IO);

    exhaust(engine);

    if (engine.error != null) {
      throw engine.error;
    }

    assertNotNull(handle);

    assertTrue(executed);
  }

  private void exhaust(HttpEngine engine) {
    engine.run();
  }

  private String crlf(String... strings) {
    UnmodifiableList<String> list;
    list = UnmodifiableList.copyOf(strings);

    return list.join(Http.CRLF);
  }

  private void executeOne() throws Throwable {
    engine.executeOne();

    if (engine.error != null) {
      throw engine.error;
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