/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.core.io;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import org.testng.annotations.Test;

public class WriteTest {

  @Test
  public void byteArray() throws IOException {
    ByteArrayOutputStreamSource source;
    source = new ByteArrayOutputStreamSource();

    byte[] randomBytes;
    randomBytes = ThisRandom.nextBytes(1 << 16);

    Write.byteArray(source, randomBytes);

    assertEquals(source.toByteArray(), randomBytes);
  }

  @Test
  public void string() throws IOException {
    ByteArrayWriterSource source;
    source = new ByteArrayWriterSource();

    String randomString;
    randomString = ThisRandom.nextString(1 << 15);

    Write.string(source, Charsets.utf8(), randomString);

    byte[] result;
    result = source.toByteArray();

    assertEquals(result, randomString.getBytes(Charsets.utf8()));

    if (Charset.isSupported("UTF-16")) {
      Charset utf16;
      utf16 = Charset.forName("UTF-16");

      Write.string(source, utf16, randomString);

      result = source.toByteArray();

      assertEquals(result, randomString.getBytes(utf16));
    }
  }

}