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
package objectox.http.srv;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import objectos.way.Note;
import objectox.http.host.HostMap;
import objectox.http.req.RequestBodySupport;
import objectox.http.req.RequestInputStream;
import objectox.http.req.RequestParser;
import objectox.http.resp.ResponseDate;
import objectox.http.resp.ResponseSender;

final class ServerTaskStage implements Closeable {

  private final int bufferSize;

  private final HostMap hostMap;

  private final Note.Sink noteSink;

  private final RequestBodySupport requestBodySupport;

  private final ResponseDate responseDate;

  private final Socket socket;

  ServerTaskStage(
      int bufferSize,

      HostMap hostMap,

      Note.Sink noteSink,

      RequestBodySupport requestBodySupport,

      ResponseDate responseDate,

      Socket socket
  ) {
    this.bufferSize = bufferSize;

    this.hostMap = hostMap;

    this.noteSink = noteSink;

    this.requestBodySupport = requestBodySupport;

    this.responseDate = responseDate;

    this.socket = socket;
  }

  @Override
  public final void close() throws IOException {
    try {
      socket.close();
    } finally {
      requestBodySupport.close();
    }
  }

  public final ServerTaskLoop toLoop() throws IOException {
    final byte[] buffer;
    buffer = new byte[bufferSize];

    final InputStream inputStream;
    inputStream = socket.getInputStream();

    final RequestInputStream requestInputStream;
    requestInputStream = new RequestInputStream(buffer, inputStream);

    final RequestParser requestParser;
    requestParser = new RequestParser(requestBodySupport, requestInputStream);

    final OutputStream outputStream;
    outputStream = socket.getOutputStream();

    final ResponseSender responseSender;
    responseSender = new ResponseSender(buffer, responseDate, outputStream);

    return new ServerTaskLoop(
        hostMap,

        noteSink,

        requestParser,

        responseSender
    );
  }

}
