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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReadTest {

  private UnmodifiableList<String> randomLines;

  private String randomLinesTxt;

  @BeforeClass
  public void _beforeClass() {
    StringBuilder randomLinesTxtBuilder;
    randomLinesTxtBuilder = new StringBuilder();

    GrowableList<String> randomLinesBuilder;
    randomLinesBuilder = new GrowableList<>();

    for (int i = 0; i < 8192; i++) {
      String randomLine;
      randomLine = ThisRandom.nextString(120);

      randomLinesBuilder.add(randomLine);

      randomLinesTxtBuilder.append(randomLine);

      int mod2;
      mod2 = i & 1;

      if (mod2 == 0) {
        randomLinesTxtBuilder.append('\r');
      }

      randomLinesTxtBuilder.append('\n');
    }

    randomLines = randomLinesBuilder.toUnmodifiableList();

    randomLinesTxt = randomLinesTxtBuilder.toString();
  }

  @Test
  public void byteArray() throws IOException {
    byte[] randomBytes;
    randomBytes = ThisRandom.nextBytes(1 << 16);

    ByteArrayInputStreamSource source;
    source = new ByteArrayInputStreamSource(randomBytes);

    byte[] result;
    result = Read.byteArray(source);

    assertEquals(result, randomBytes);
  }

  @Test(description = "Reader.lines(Reader) : not buffered")
  public void lines0() throws IOException {
    byte[] bytes;
    bytes = randomLinesTxt.getBytes(Charsets.utf8());

    ByteArrayInputStream in;
    in = new ByteArrayInputStream(bytes);

    InputStreamReader reader;
    reader = new InputStreamReader(in, Charsets.utf8());

    UnmodifiableList<String> result;
    result = Read.lines(reader);

    assertEquals(result, randomLines);

    assertEquals(in.available(), 0);
  }

  @Test(description = "Reader.lines(Reader) : is buffered")
  public void lines1() throws IOException {
    byte[] bytes;
    bytes = randomLinesTxt.getBytes(Charsets.utf8());

    ByteArrayInputStream in;
    in = new ByteArrayInputStream(bytes);

    InputStreamReader r;
    r = new InputStreamReader(in, Charsets.utf8());

    BufferedReader reader;
    reader = new BufferedReader(r);

    UnmodifiableList<String> result;
    result = Read.lines(reader);

    assertEquals(result, randomLines);
  }

  @Test
  public void lines2() throws IOException {
    ReaderSource source;
    source = new ByteArrayReaderSource(randomLinesTxt);

    UnmodifiableList<String> result;
    result = Read.lines(source, Charsets.utf8());

    assertEquals(result, randomLines);
  }

  @Test
  public void string() throws IOException {
    String randomString;
    randomString = ThisRandom.nextString(1 << 15);

    ByteArrayReaderSource source;
    source = new ByteArrayReaderSource(randomString);

    String result;
    result = Read.string(source, Charsets.utf8());

    assertEquals(result, randomString);
  }

}
