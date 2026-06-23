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
import objectox.http.req.RequestBodySupport;
import objectox.http.req.RequestBodySupportFactory;
import objectox.http.req.RequestParser;
import objectox.http.resp.ResponseDate;
import objectox.http.resp.ResponseSender;

final class ServerTaskStage implements Closeable {

  private final ServerConfig serverConfig;

  private final Socket socket;

  private RequestBodySupport requestBodySupport;

  ServerTaskStage(ServerConfig serverCfg, Socket socket) {
    this.serverConfig = serverCfg;

    this.socket = socket;
  }

  @Override
  public final void close() throws IOException {
    try {
      socket.close();
    } finally {
      if (requestBodySupport != null) {
        requestBodySupport.close();
      }
    }
  }

  public final ServerTaskLoop toLoop() throws IOException {
    final byte[] buffer;
    buffer = serverConfig.buffer();

    final InputStream inputStream;
    inputStream = socket.getInputStream();

    final RequestBodySupportFactory requestBodySupportFactory;
    requestBodySupportFactory = serverConfig.requestBodySupportFactory();

    requestBodySupport = requestBodySupportFactory.create(0);

    final RequestParser requestParser;
    requestParser = new RequestParser(buffer, inputStream, requestBodySupport);

    final ResponseDate responseDate;
    responseDate = serverConfig.responseDate();

    final OutputStream outputStream;
    outputStream = socket.getOutputStream();

    final ResponseSender responseSender;
    responseSender = new ResponseSender(buffer, responseDate, outputStream);

    return new ServerTaskLoop(
        serverConfig.hostMap(),

        serverConfig.noteSink(),

        requestParser,

        responseSender
    );
  }

}
