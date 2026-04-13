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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.way.Note;
import objectos.way.Note.Long1Ref1;

final class HttpServerTaskYNoteSink extends Note.NoOpSink {

  long id;

  IOException thrown;

  @Override
  public final <T1> void send(Long1Ref1<T1> note, long value1, T1 value2) {
    assertEquals(note.source(), HttpServerTask.class.getName());

    assertEquals(note.key(), "IOE");

    id = value1;

    thrown = (IOException) value2;
  }

}
