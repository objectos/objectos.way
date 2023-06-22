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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RequestParserTest {

  private RequestParser parser;

  @BeforeClass
  public void _setUp() {
    int bufferSize;
    bufferSize = 256;

    ByteBuffer byteBuffer;
    byteBuffer = ByteBuffer.allocate(bufferSize);

    CharBuffer charBuffer;
    charBuffer = CharBuffer.allocate(bufferSize);

    parser = RequestParser.create(byteBuffer, charBuffer);
  }

  @Test(description = ""
      + "GET 1.0"
      + "- well formed"
      + "- request line only")
  public void testCase01() {
    assertEquals(
      parse(
        "GET /test/ HTTP/1.0",
        "",
        ""
      ),
      String.join(
        System.lineSeparator(),

        "GET /test/ V1_0",
        "BodyIgnoredImpl",
        ""
      )
    );
  }

  @Test(description = ""
      + "GET 1.0"
      + "- well formed"
      + "- headers")
  public void testCase02() {
    assertEquals(
      parse(
        "GET /test/ HTTP/1.0",
        "Accept: text/html",
        "Host: www.example.com",
        "",
        ""
      ),
      String.join(
        System.lineSeparator(),

        "GET /test/ V1_0",
        "HeaderAcceptImpl: text/html",
        "HeaderHostImpl: www.example.com",
        "BodyIgnoredImpl",
        ""
      )
    );
  }

  @Test(description = ""
      + "POST 1.0"
      + "- well formed"
      + "- headers"
      + "- payload")
  public void testCase03() {
    assertEquals(
      parse(
        "POST /test/api/login HTTP/1.0",
        "Accept: application/json",
        "Cookie: foo=bar",
        "Content-Length: 35",
        "Content-Type: application/json; charset=\"UTF-8\"",
        "Host: www.example.com",
        "",
        "{\"prop1\":\"val001\",\"prop2\":\"val002\"}"
      ),
      String.join(
        System.lineSeparator(),

        "POST /test/api/login V1_0",
        "HeaderAcceptImpl: application/json",
        "HeaderCookieImpl: foo=bar",
        "HeaderContentLengthImpl: 35",
        "HeaderContentTypeImpl: application/json; charset=utf-8",
        "HeaderHostImpl: www.example.com",
        "BodyTextImpl",
        "{\"prop1\":\"val001\",\"prop2\":\"val002\"}"
      )
    );
  }

  private String parse(String... lines) {
    ReadableByteChannel channel;
    channel = ByteArrayReadableByteChannel.ofLines(lines);

    parser.setInput(channel);

    exhaust(parser);

    Request result;

    try {
      result = parser.getResult();
    } catch (HttpException e) {
      throw new AssertionError(e);
    }

    ThisVisitor visitor;
    visitor = new ThisVisitor();

    result.acceptRequestVisitor(visitor);

    return visitor.toString();
  }

  private void exhaust(RequestParser parser) {
    while (parser.isActive()) {
      parser.executeOne();
    }
  }

  private class ThisVisitor extends SimpleRequestVisitor {

    private final StringBuilder out = new StringBuilder();

    @Override
    public final String toString() {
      return out.toString();
    }

    @Override
    public final void visitRequestLine(Method method, String target, Version version) {
      out.append(method.name());

      out.append(' ');

      out.append(target);

      out.append(' ');

      out.append(version.toString());
    }

    @Override
    protected final void defaultRequestBodyAction(Body body) {
      out.append('\n');

      Class<? extends Body> bodyClass;
      bodyClass = body.getClass();

      out.append(bodyClass.getSimpleName());

      out.append('\n');

      out.append(body);
    }

    @Override
    protected final void defaultRequestHeaderAction(RequestHeader header) {
      out.append('\n');

      Class<? extends RequestHeader> impl;
      impl = header.getClass();

      out.append(impl.getSimpleName());

      out.append(':');

      out.append(' ');

      out.append(header.getHeaderValue());
    }

  }

}
