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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ResponseParserTest {

  private ResponseParser parser;

  @BeforeClass
  public void _setUp() {
    parser = newParser(256);
  }

  @Test(description = ""
      + "1.0 302 Found"
      + "- well formed"
      + "- status line only")
  public void testCase01() {
    assertEquals(
      parse(
        "HTTP/1.0 302 Found",
        "", ""
      ),
      String.join(
        System.lineSeparator(),

        "V1_0 FOUND",
        "BodyIgnoredImpl",
        ""
      )
    );
  }

  @Test(description = ""
      + "1.1 302 Found"
      + "- well formed"
      + "- with headers")
  public void testCase02() {
    assertEquals(
      parse(
        "HTTP/1.1 302 Found",
        "Server: objectos-http/1.0.0-SNAPSHOT",
        "Location: /test/login",
        "", ""
      ),
      String.join(
        System.lineSeparator(),

        "V1_1 FOUND",
        "HeaderServerImpl: objectos-http/1.0.0-SNAPSHOT",
        "HeaderLocationImpl: /test/login",
        "BodyIgnoredImpl",
        ""
      )
    );
  }

  @Test(description = ""
      + "1.1 200 OK"
      + "- well formed"
      + "- content-type html, charset utf8"
      + "- content-length exact")
  public void testCase03() {
    assertEquals(
      parse(
        "HTTP/1.1 200 OK",
        "Server: objectos-http/1.0.0-SNAPSHOT",
        "Content-Type: text/html; charset=utf8",
        "Content-Length: 4",
        "Set-Cookie: foo=bar",
        "",
        "1234"
      ),
      String.join(
        System.lineSeparator(),

        "V1_1 OK",
        "HeaderServerImpl: objectos-http/1.0.0-SNAPSHOT",
        "HeaderContentTypeImpl: text/html; charset=utf-8",
        "HeaderContentLengthImpl: 4",
        "HeaderSetCookieImpl: foo=bar",
        "BodyTextImpl",
        "1234"
      )
    );
  }

  @Test(description = ""
      + "1.1 200 OK"
      + "- well formed"
      + "- content-type html, charset utf8"
      + "- content-length shorter (ie, payload is greater than content-length)")
  public void testCase04() {
    assertEquals(
      parse(
        "HTTP/1.1 200 OK",
        "Server: objectos-http/1.0.0-SNAPSHOT",
        "Content-Type: text/html; charset=utf8",
        "Content-Length: 4",
        "Set-Cookie: foo=bar",
        "",
        "12345678"
      ),
      String.join(
        System.lineSeparator(),

        "V1_1 OK",
        "HeaderServerImpl: objectos-http/1.0.0-SNAPSHOT",
        "HeaderContentTypeImpl: text/html; charset=utf-8",
        "HeaderContentLengthImpl: 4",
        "HeaderSetCookieImpl: foo=bar",
        "BodyTextImpl",
        "1234"
      )
    );
  }

  private ResponseParser newParser(int bufferSize) {
    ByteBuffer byteBuffer;
    byteBuffer = ByteBuffer.allocate(bufferSize);

    CharBuffer charBuffer;
    charBuffer = CharBuffer.allocate(bufferSize);

    return new ResponseParser(byteBuffer, charBuffer);
  }

  private String parse(String... lines) {
    ReadableByteChannel channel;
    channel = ByteArrayReadableByteChannel.ofLines(lines);

    parser.setInput(channel);

    exhaust(parser);

    Response result;

    try {
      result = parser.getResult();
    } catch (HttpException e) {
      throw new AssertionError(e);
    }

    ThisVisitor visitor;
    visitor = new ThisVisitor();

    result.acceptResponseVisitor(visitor);

    return visitor.toString();
  }

  private void exhaust(ResponseParser parser) {
    while (parser.isActive()) {
      parser.executeOne();
    }
  }

  private class ThisVisitor extends SimpleResponseVisitor {

    private final StringBuilder out = new StringBuilder();

    @Override
    public final String toString() {
      return out.toString();
    }

    @Override
    public final void visitResponseStatusLine(Version version, Status status, String reason) {
      out.append(version.toString());

      out.append(' ');

      out.append(status.toString());
    }

    @Override
    protected final void defaultResponseBodyAction(Body body) {
      out.append('\n');

      Class<? extends Body> bodyClass;
      bodyClass = body.getClass();

      out.append(bodyClass.getSimpleName());

      out.append('\n');

      out.append(body);
    }

    @Override
    protected final void defaultResponseHeaderAction(ResponseHeader header) {
      out.append('\n');

      Class<? extends ResponseHeader> impl;
      impl = header.getClass();

      out.append(impl.getSimpleName());

      out.append(':');

      out.append(' ');

      out.append(header.getHeaderValue());
    }

  }

}
