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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser9BodyType0FormTest {

  private Map<String, Object> parse(String payload) throws IOException {
    final byte[] bytes;
    bytes = payload.getBytes(StandardCharsets.US_ASCII);

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
      final HttpRequestParser9BodyType0Form parser;
      parser = new HttpRequestParser9BodyType0Form(inputStream);

      return parser.parse();
    }
  }

  @DataProvider
  public Object[][] validProvider() {
    return HttpY.queryValidProvider();
  }

  @Test(dataProvider = "validProvider")
  public void valid(String payload, Map<String, Object> expected, String description) throws IOException {
    assertEquals(
        parse(payload),

        expected
    );
  }

}
