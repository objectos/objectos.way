/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import objectos.internal.Bytes;
import objectos.lang.OutputStreamConsumer;

final class ResponseWriter implements Closeable, ContentSender {

  private final ResponseBuffered buffered;

  private final ResponseDate date;

  private final boolean head;

  private final ResponsePojo response;

  ResponseWriter(ResponseBuffered buffered, ResponseDate date, boolean head, ResponsePojo response) {
    this.buffered = buffered;

    this.date = date;

    this.head = head;

    this.response = response;
  }

  private static final byte[] COLONCRLF = ":\r\n".getBytes(StandardCharsets.US_ASCII);

  @Override
  public final void close() throws IOException {
    buffered.close();
  }

  public final void write() throws IOException {
    statusLine();

    headerFields();

    body();
  }

  private static final Map<HttpStatus0, byte[]> STATUS_LINES = new EnumMap<>(HttpStatus0.class);

  static {
    final HttpStatus0[] values;
    values = HttpStatus0.values();

    for (HttpStatus0 status : values) {
      final String response;
      response = "HTTP/1.1 " + Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      final byte[] bytes;
      bytes = Http.utf8(response);

      STATUS_LINES.put(status, bytes);
    }
  }

  private void statusLine() throws IOException {
    final HttpStatus0 status;
    status = response.status();

    final byte[] statusLine;
    statusLine = STATUS_LINES.get(status);

    buffered.write(statusLine);
  }

  private void headerFields() throws IOException {
    final List<Header> headers;
    headers = response.headers();

    for (Header header : headers) {
      final HttpHeaderName name;

      final String value;

      if (header == Header.DATE) {
        name = HttpHeaderName.DATE;

        value = date.now();
      } else {
        name = header.name();

        value = header.value();
      }

      headerFields0(name, value);
    }
  }

  private void headerFields0(HttpHeaderName name, String value) throws IOException {
    final HttpHeaderName0 nameImpl;
    nameImpl = (HttpHeaderName0) name;

    final byte[] nameBytes;
    nameBytes = nameImpl.headerCaseBytes();

    buffered.write(nameBytes);

    if (value.isEmpty()) {
      buffered.write(COLONCRLF);
    }

    else {
      final byte[] valueBytes;
      valueBytes = value.getBytes(StandardCharsets.US_ASCII);

      buffered.write(Bytes.COLONSP);

      buffered.write(valueBytes);

      buffered.write(Bytes.CRLF);
    }
  }

  private void body() throws IOException {
    final ResponseBody body;
    body = response.body();

    switch (body) {
      case ResponseBody.OfContent c -> {
        final Content content;
        content = c.content();

        content.sendContent(this);
      }

      case ResponseBody.OfEmpty.INSTANCE -> {
        buffered.write(Bytes.CRLF);
      }

      case ResponseBody.OfFile f -> {
        buffered.write(Bytes.CRLF);

        if (!head) {
          buffered.write(f.file());
        }
      }
    }
  }

  @Override
  public final void send(MediaType contentType, byte[] contents) throws IOException {
    contentType(contentType);

    final int length;
    length = contents.length;

    final String value;
    value = Integer.toString(length);

    headerFields0(HttpHeaderName.CONTENT_LENGTH, value);

    buffered.write(Bytes.CRLF);

    if (!head) {
      buffered.write(contents);
    }
  }

  @Override
  public final void send(MediaType contentType, OutputStreamConsumer contents) throws IOException {
    contentType(contentType);

    final OutputStreamConsumer c;
    c = Objects.requireNonNull(contents, "contents == null");

    headerFields0(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    buffered.write(Bytes.CRLF);

    if (!head) {
      buffered.write(c);
    }
  }

  private void contentType(MediaType contentType) throws IOException {
    final String fullType;
    fullType = contentType.fullType();

    final String value;
    value = Objects.requireNonNull(fullType, "fullType == null");

    headerFields0(HttpHeaderName.CONTENT_TYPE, value);
  }

}
