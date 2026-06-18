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
package objectox.http.resp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import objectos.http.Content;
import objectos.http.HeaderName;
import objectos.http.MediaType;
import objectos.internal.Bytes;
import objectos.lang.BinaryObject;
import objectox.http.Header;
import objectox.http.HeaderNamePojo;
import objectox.http.Rfc;
import objectox.http.media.ContentBinaryObject;
import objectox.http.media.ContentBytes;

public final class ResponseSender {

  private final byte[] buffer;

  private final ResponseDate date;

  private final OutputStream outputStream;

  public ResponseSender(byte[] buffer, ResponseDate date, OutputStream outputStream) {
    this.buffer = buffer;

    this.date = date;

    this.outputStream = outputStream;
  }

  public final void head(ResponsePojo pojo) throws IOException {
    send0(pojo, true);
  }

  public final void send(ResponsePojo pojo) throws IOException {
    send0(pojo, false);
  }

  private void send0(ResponsePojo pojo, boolean head) throws IOException {
    final StatusEnum status;
    status = pojo.status();

    statusLine(status);

    final List<Header> headers;
    headers = pojo.headers();

    for (Header header : headers) {
      final HeaderName name;

      final String value;

      if (header == Header.DATE) {
        name = HeaderName.DATE;

        value = date.now();
      } else {
        name = header.name();

        value = header.value();
      }

      header(name, value);
    }

    final ResponseEntity entity;
    entity = pojo.entity();

    entity(entity, head);
  }

  private static final Map<StatusEnum, byte[]> STATUS_LINES = new EnumMap<>(StatusEnum.class);

  static {
    final StatusEnum[] values;
    values = StatusEnum.values();

    for (StatusEnum status : values) {
      final String response;
      response = "HTTP/1.1 " + Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      final byte[] bytes;
      bytes = Rfc.utf8(response);

      STATUS_LINES.put(status, bytes);
    }
  }

  private void statusLine(StatusEnum status) throws IOException {
    final byte[] statusLine;
    statusLine = STATUS_LINES.get(status);

    outputStream.write(statusLine);
  }

  private static final byte[] COLONCRLF = ":\r\n".getBytes(StandardCharsets.US_ASCII);

  private void header(HeaderName name, String value) throws IOException {
    final HeaderNamePojo nameImpl;
    nameImpl = (HeaderNamePojo) name;

    final byte[] nameBytes;
    nameBytes = nameImpl.headerCaseBytes();

    outputStream.write(nameBytes);

    if (value.isEmpty()) {
      outputStream.write(COLONCRLF);
    }

    else {
      final byte[] valueBytes;
      valueBytes = value.getBytes(StandardCharsets.US_ASCII);

      outputStream.write(Bytes.COLONSP);

      outputStream.write(valueBytes);

      outputStream.write(Bytes.CRLF);
    }
  }

  private void entity(ResponseEntity entity, boolean head) throws IOException {
    switch (entity) {
      case ResponseEntity.OfContent(Content content) -> {
        switch (content) {
          case ContentBinaryObject(MediaType contentType, BinaryObject contents) -> {
            header(HeaderName.CONTENT_TYPE, contentType.fullType());

            header(HeaderName.TRANSFER_ENCODING, "chunked");

            outputStream.write(Bytes.CRLF);

            if (head) {
              return;
            }

            try (OutputStream chunked = ResponseChunked.of(buffer, 0, outputStream)) {
              contents.binaryTo(chunked);
            }
          }

          case ContentBytes(MediaType contentType, byte[] bytes) -> {
            header(HeaderName.CONTENT_TYPE, contentType.fullType());

            final int length;
            length = bytes.length;

            final String contentLength;
            contentLength = Integer.toString(length);

            header(HeaderName.CONTENT_LENGTH, contentLength);

            outputStream.write(Bytes.CRLF);

            if (head) {
              return;
            }

            outputStream.write(bytes);
          }
        }
      }

      case ResponseEntity.OfEmpty.INSTANCE -> {
        outputStream.write(Bytes.CRLF);
      }

      case ResponseEntity.OfFile(Path file) -> {
        final long length;
        length = Files.size(file);

        final String contentLength;
        contentLength = Long.toString(length);

        header(HeaderName.CONTENT_LENGTH, contentLength);

        outputStream.write(Bytes.CRLF);

        if (head) {
          return;
        }

        try (InputStream in = Files.newInputStream(file)) {
          in.transferTo(outputStream);
        }
      }
    }
  }

}
