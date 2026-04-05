/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest0Socket {

  private static class ThisSocket extends Socket {

    @Override
    public InputStream getInputStream() throws IOException {
      return InputStream.nullInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
      return OutputStream.nullOutputStream();
    }

    @Override
    public void close() throws IOException {
      // noop
    }

  }

  @DataProvider
  public Object[][] socketErrorProvider() {
    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    return new Object[][] {
        {
            new ThisSocket() {
              @Override
              public InputStream getInputStream() throws IOException {
                throw ioe;
              }
            },
            1001L,
            "getInputStream",
            ioe
        },
        {
            new ThisSocket() {
              @Override
              public OutputStream getOutputStream() throws IOException {
                throw ioe;
              }
            },
            1002L,
            "getOutputStream",
            ioe
        },
        {
            new ThisSocket() {
              @Override
              public void close() throws IOException {
                throw ioe;
              }
            },
            1003L,
            "close",
            ioe
        }
    };
  };

  @Test(dataProvider = "socketErrorProvider")
  public void socketError(Socket socket, long id, String event, IOException thrown) {
    var noteSink = new HttpServerTaskYNoteSink();

    HttpServerTaskY.run(opts -> {
      opts.id = id;

      opts.noteSink = noteSink;

      opts.socket = socket;
    });

    assertEquals(noteSink.id, id);
    assertEquals(noteSink.event, event);
    assertEquals(noteSink.thrown, thrown);
  }

}