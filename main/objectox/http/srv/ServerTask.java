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
import java.net.Socket;
import objectos.internal.VisibleForTesting;
import objectos.way.Note;
import objectox.http.host.HostMap;
import objectox.http.req.RequestBodyConfig;
import objectox.http.req.RequestBodySupport;
import objectox.http.resp.ResponseDate;

final class ServerTask implements Runnable {

  private static final Note.Ref1<Throwable> THROW = Note.Ref1.create(ServerTask.class, "THR", Note.ERROR);

  private final int bufferSize;

  @VisibleForTesting
  final HostMap hostMap;

  private final Note.Sink noteSink;

  private final RequestBodyConfig requestBodyConfig;

  private final ResponseDate responseDate;

  @VisibleForTesting
  final Socket socket;

  ServerTask(
      int bufferSize,

      HostMap hostMap,

      Note.Sink noteSink,

      RequestBodyConfig requestBodyConfig,

      ResponseDate responseDate,

      Socket socket
  ) {
    this.bufferSize = bufferSize;

    this.hostMap = hostMap;

    this.noteSink = noteSink;

    this.requestBodyConfig = requestBodyConfig;

    this.responseDate = responseDate;

    this.socket = socket;
  }

  @Override
  public final void run() {
    final RequestBodySupport requestBodySupport;
    requestBodySupport = new RequestBodySupport(requestBodyConfig);

    final ServerTaskStage stage;
    stage = new ServerTaskStage(bufferSize, hostMap, noteSink, requestBodySupport, responseDate, socket);

    try (stage) {
      final ServerTaskLoop loop;
      loop = stage.toLoop();

      while (loop.shouldExecute()) {
        loop.executeOne();
      }
    } catch (IOException e) {
      noteSink.send(THROW, e);
    }
  }

}
