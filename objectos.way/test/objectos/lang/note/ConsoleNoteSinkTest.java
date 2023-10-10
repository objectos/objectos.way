/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.lang.note;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import objectos.lang.Level;
import objectos.lang.Note0;
import org.testng.annotations.Test;

public class ConsoleNoteSinkTest {

  static final Note0 NOTE0;

  static {
    Class<?> s;
    s = ConsoleNoteSinkTest.class;

    NOTE0 = Note0.info(s, "NOTE0");
  }

  @Test
  public void log() throws Exception {
    PrintStream original;
    original = System.out;

    try {
      ByteArrayOutputStream outputStream;
      outputStream = new ByteArrayOutputStream();

      PrintStream stream;
      stream = new PrintStream(outputStream);

      System.setOut(stream);

      ConsoleNoteSink noteSink;
      noteSink = ConsoleNoteSink.of(Level.INFO);

      noteSink.send(NOTE0);

      TimeUnit.MILLISECONDS.sleep(50);

      noteSink.onShutdownHook();

      byte[] bytes;
      bytes = outputStream.toByteArray();

      String string;
      string = new String(bytes);

      assertTrue(string.endsWith(
        "INFO  --- [main           ] objectos.lang.note.ConsoleNoteSinkTest   : NOTE0\n"), string);
    } finally {
      System.setOut(original);
    }
  }

}