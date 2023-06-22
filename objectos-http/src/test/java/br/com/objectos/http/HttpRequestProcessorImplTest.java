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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Date;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpRequestProcessorImplTest {

  private ThisHttpResponseHandle handle;

  private HttpRequestProcessorImpl impl;

  private Path siteDirectory;

  @BeforeClass
  public void _beforeClass() throws IOException, URISyntaxException {
    handle = new ThisHttpResponseHandle(64);

    Path testInf;
    testInf = TestInf.get();

    siteDirectory = testInf.resolve("site");

    impl = new HttpRequestProcessorImpl(siteDirectory);
  }

  @Test(description = TestCase0001.DESCRIPTION)
  public void testCase01() throws IOException {
    impl.requestStart(handle);

    impl.requestLine(
      Method.GET,

      RequestTargetImpl.ofString("/"),

      Version.V1_1
    );

    ResponseTask rt;
    rt = impl.responseTask();

    assertEquals(rt.isActive(), true);

    assertEquals(impl.ioReady, HttpRequestProcessorImpl._RESPONSE_HEADERS);

    assertEquals(impl.state, HttpRequestProcessorImpl._WAIT_IO);

    rt.executeOne();

    Path idx;
    idx = siteDirectory.resolve("index.html");

    long idxSize;
    idxSize = Files.size(idx);

    assertEquals(impl.contentLength, idxSize);

    assertEquals(impl.file, idx);

    assertEquals(impl.state, HttpRequestProcessorImpl._RESPONSE_HEADERS);

    rt.executeOne();

    String s = impl.stringBuilder.toString();

    String[] parts;
    parts = s.split(Http.CRLF);

    assertEquals(parts.length, 6);
    assertEquals(parts[0], "HTTP/1.1 200 OK");
    assertEquals(parts[1], "Server: Objectos HTTP");
    assertEquals(parts[2].startsWith("Date: "), true);
    assertEquals(parts[3], "Content-Type: text/html; charset=utf8");
    assertEquals(parts[4], "Content-Length: " + Long.toString(idxSize));
    assertEquals(parts[5].startsWith("Last-Modified: "), true);

    assertEquals(impl.state, HttpRequestProcessorImpl._WAIT_IO);

    assertEquals(impl.stringBuilderOffset, 64);

    assertEquals(impl.writtenCount, 64);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._ENCODE);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._WAIT_IO);

    assertEquals(impl.stringBuilderOffset, 64 + 64);

    assertEquals(impl.writtenCount, 64 + 64);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._ENCODE);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._WAIT_IO);

    assertEquals(impl.stringBuilderOffset, 184);

    assertEquals(impl.writtenCount, 184);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._ENCODE);

    rt.executeOne();

    assertEquals(impl.fileChannel.isOpen(), false);

    assertEquals(impl.fileChannelPosition, impl.contentLength);

    assertEquals(impl.state, HttpRequestProcessorImpl._WAIT_IO);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._WRITE_FILE);

    rt.executeOne();

    assertEquals(impl.state, HttpRequestProcessorImpl._STOP);

    String r;
    r = handle.toString();

    parts = r.split(Http.CRLF);

    assertEquals(parts.length, 8);
    assertEquals(parts[0], "HTTP/1.1 200 OK");
    assertEquals(parts[1], "Server: Objectos HTTP");
    assertEquals(parts[2].startsWith("Date: "), true);
    assertEquals(parts[3], "Content-Type: text/html; charset=utf8");
    assertEquals(parts[4], "Content-Length: " + Long.toString(idxSize));
    assertEquals(parts[5].startsWith("Last-Modified: "), true);
    assertEquals(parts[6], "");
    assertEquals(parts[7], "<!doctype html><title>/index.html</title>");
  }

  private class ThisHttpResponseHandle implements HttpResponseHandle {

    private final ByteBuffer byteBuffer;

    private final WritableByteChannel channel;

    private final CharBuffer charBuffer;

    private final DateFormat dateFormat;

    private final ByteArrayOutputStream outputStream;

    private final StringBuilder stringBuilder;

    ThisHttpResponseHandle(int bufferSize) {
      byteBuffer = ByteBuffer.allocate(bufferSize);

      charBuffer = CharBuffer.allocate(bufferSize);

      dateFormat = Http.createDateFormat();

      outputStream = new ByteArrayOutputStream();

      channel = Channels.newChannel(outputStream);

      stringBuilder = new StringBuilder();
    }

    @Override
    public final String formatDate(Date date) {
      return dateFormat.format(date);
    }

    @Override
    public final String formatDate(long millis) {
      return formatDate(new Date(millis));
    }

    @Override
    public final ByteBuffer getByteBuffer() {
      byteBuffer.clear();

      return byteBuffer;
    }

    @Override
    public final WritableByteChannel getChannel() {
      return channel;
    }

    @Override
    public final CharBuffer getCharBuffer() {
      charBuffer.clear();

      return charBuffer;
    }

    @Override
    public final CharsetEncoder getEncoder(Charset charset) {
      return charset.newEncoder();
    }

    @Override
    public final StringBuilder getStringBuilder() {
      stringBuilder.setLength(0);

      return stringBuilder;
    }

    @Override
    public final String toString() {
      byte[] byteArray;
      byteArray = outputStream.toByteArray();

      return new String(byteArray, StandardCharsets.UTF_8);
    }

  }

}