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

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import objectox.http.media.ContentBytes;
import org.testng.annotations.Test;

public class ContentTest {

  @Test
  public void ofBytes() {
    final MediaType type;
    type = MediaType.APPLICATION_OCTET_STREAM;

    final byte[] bytes;
    bytes = new byte[8192];

    final ThreadLocalRandom random;
    random = ThreadLocalRandom.current();

    random.nextBytes(bytes);

    final Content res;
    res = Content.of(type, bytes);

    final ContentBytes pojo;
    pojo = (ContentBytes) res;

    assertEquals(pojo.contentType(), type);

    assertEquals(pojo.bytes(), bytes);
  }

  @Test
  public void ofString01() {
    final MediaType type;
    type = MediaType.TEXT_PLAIN;

    final String s;
    s = "This is a test \0 \u1f600";

    final Content res;
    res = Content.of(type, s);

    final ContentBytes pojo;
    pojo = (ContentBytes) res;

    assertEquals(pojo.contentType(), type);

    assertEquals(pojo.bytes(), s.getBytes(StandardCharsets.UTF_8));
  }

}
