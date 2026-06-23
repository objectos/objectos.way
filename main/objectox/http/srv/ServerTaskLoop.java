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

import java.io.IOException;
import objectos.http.Content;
import objectos.http.HeaderName;
import objectos.http.MediaType;
import objectos.http.RequestMethod;
import objectos.http.Status;
import objectos.way.Note;
import objectox.http.HttpClientException;
import objectox.http.HttpServerException;
import objectox.http.RequestMethodEnum;
import objectox.http.host.Host;
import objectox.http.host.HostMap;
import objectox.http.req.RequestParser;
import objectox.http.req.RequestPojo;
import objectox.http.resp.ResponsePojo;
import objectox.http.resp.ResponseSender;

final class ServerTaskLoop {

  private final HostMap hostMap;

  @SuppressWarnings("unused")
  private final Note.Sink noteSink;

  private final RequestParser requestParser;

  private final ResponseSender responseSender;

  private boolean stop;

  ServerTaskLoop(HostMap hostMap, Note.Sink noteSink, RequestParser requestParser, ResponseSender responseSender) {
    this.hostMap = hostMap;

    this.noteSink = noteSink;

    this.requestParser = requestParser;

    this.responseSender = responseSender;
  }

  public final void executeOne() throws IOException {
    if (requestParser.hasNext()) {
      execute0();
    } else {
      stop = true;
    }
  }

  private void execute0() throws IOException {
    try {
      execute1();
    } catch (HttpClientException | HttpServerException e) {
      noteSink.send(ServerTask.THROW, e);

      final ResponsePojo response;
      response = error(e.status(), e.message());

      responseSender.send(response);

      stop = true;
    }
  }

  private void execute1() throws IOException {
    final RequestPojo request;
    request = requestParser.parse();

    final ResponsePojo response;
    response = handle(request);

    final RequestMethod method;
    method = request.method();

    if (method == RequestMethodEnum.HEAD) {
      responseSender.head(response);
    } else {
      responseSender.send(response);
    }

    stop = request.closeConnection() || response.closeConnection();
  }

  private ResponsePojo handle(RequestPojo request) {
    final String hostValue;
    hostValue = request.header(HeaderName.HOST);

    final Host host;
    host = hostMap.get(hostValue);

    if (host == null) {
      final String msg;
      msg = "Invalid host: %s".formatted(hostValue);

      return error(Status.BAD_REQUEST, msg);
    }

    try {
      return host.handle(request);
    } catch (Throwable e) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private ResponsePojo error(Status status, String message) {
    return ResponsePojo.create0(opts -> {
      opts.status(status);

      opts.date();

      opts.header(HeaderName.CONNECTION, "close");

      opts.send(Content.of(MediaType.TEXT_PLAIN, message));
    });
  }

  public final boolean shouldExecute() {
    return !Thread.currentThread().isInterrupted()
        && !stop;
  }

}
