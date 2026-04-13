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
import java.net.Socket;
import objectos.http.HttpClientException.Kind;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser1UrlDecoderTest {

  private String decode(int initial, int length, Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestParser0Input input;
    input = HttpRequestParser0Input.of(initial, socket);

    assertEquals(input.readByte(), '%');

    final HttpRequestParser1UrlDecoder decoder;
    decoder = new HttpRequestParser1UrlDecoder(input);

    final StringBuilder out;
    out = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      final int cp;
      cp = decoder.decode(Kind.INVALID_REQUEST_LINE);

      out.appendCodePoint(cp);
    }

    return out.toString();
  }

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        // 1-byte cases
        {"%40", "@", "1-byte (ASCII @)"},
        {"%00", "\u0000", "1-byte (min value, null character)"},
        {"%7F", "\u007F", "1-byte (max value, DEL)"},
        {"%2F", "/", "1-byte (reserved character /)"},
        {"%3A", ":", "1-byte (reserved character :)"}
    };
  }

  @Test(dataProvider = "validProvider")
  public void valid(String raw, String path, String description) throws IOException {
    assertEquals(decode(256, path.length(), raw), path);
  }

}
