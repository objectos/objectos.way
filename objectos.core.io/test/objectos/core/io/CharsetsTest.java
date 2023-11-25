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

import java.nio.charset.Charset;
import org.testng.annotations.Test;

public class CharsetsTest {

  @Test
  public void isoLatin1() {
    Charset result;
    result = Charsets.isoLatin1();

    assertEquals(result.name(), "ISO-8859-1");
  }

  @Test
  public void usAscii() {
    Charset result;
    result = Charsets.usAscii();

    assertEquals(result.name(), "US-ASCII");
  }

  @Test
  public void utf8() {
    Charset result;
    result = Charsets.utf8();

    assertEquals(result.name(), "UTF-8");
  }

}