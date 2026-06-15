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
import java.net.ServerSocket;
import objectos.http.Server;
import objectox.http.host.HostMap;

public record ServerPojo(HostMap hosts, ServerSocket serverSocket) implements Server {

  @Override
  public final void close() throws IOException {
    serverSocket.close();
  }

  public final int port() {
    return serverSocket.getLocalPort();
  }

}
