/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.testng.annotations.Test;

public class ByteSourceTest {

  @Test
  public void hasMore() throws IOException {
    byte[] bytes;
    bytes = bytes(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    InputStream inputStream;
    inputStream = new ByteArrayInputStream(bytes);

    ByteSource source;
    source = ByteSource.ofInputStream(inputStream, 5);

    assertEquals(source.hasMore(1), true);
    assertEquals(source.get(), 0);
    assertEquals(source.hasMore(4), true);
    assertEquals(source.matches(bytes(1, 2, 3, 4)), true);
    assertEquals(source.hasMore(5), true);
    assertEquals(source.matches(bytes(5, 6, 7, 8, 9)), true);
  }

  private byte[] bytes(int... values) {
    byte[] res;
    res = new byte[values.length];

    for (int i = 0; i < values.length; i++) {
      res[i] = (byte) values[i];
    }

    return res;
  }

}