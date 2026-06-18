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
package objectox.http.media;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.annotations.Test;

public class ContentBytesTest {

  @Test
  public void binaryTo() throws IOException {
    final byte[] bytes;
    bytes = new byte[8192];

    final ThreadLocalRandom random;
    random = ThreadLocalRandom.current();

    random.nextBytes(bytes);

    final ContentBytes subject;
    subject = new ContentBytes(null, bytes);

    final ByteArrayOutputStream output;
    output = new ByteArrayOutputStream();

    subject.binaryTo(output);

    final byte[] res;
    res = output.toByteArray();

    assertEquals(res, bytes);
  }

}
