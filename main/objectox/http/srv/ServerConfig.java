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

import java.time.Clock;
import objectos.way.Note;
import objectox.http.host.HostMap;
import objectox.http.req.RequestBodySupportFactory;
import objectox.http.resp.ResponseDate;

record ServerConfig(
    int bufferSize,

    Clock clock,

    HostMap hostMap,

    Note.Sink noteSink,

    RequestBodySupportFactory requestBodySupportFactory
) {

  public final byte[] buffer() {
    return new byte[bufferSize];
  }

  public final ResponseDate responseDate() {
    return new ResponseDate(clock);
  }

  public final <T1> void send(Note.Ref1<T1> note, T1 value) {
    noteSink.send(note, value);
  }

}
