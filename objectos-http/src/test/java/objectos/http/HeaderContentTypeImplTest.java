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

import java.nio.charset.StandardCharsets;
import objectos.http.media.TextType;
import org.testng.annotations.Test;

public class HeaderContentTypeImplTest {

  @Test(description = "media type only")
  public void testCase01() {
    HeaderContentTypeImpl result;
    result = parse("text/html\r\n");

    assertEquals(result.getMediaType(), TextType.HTML);

    assertEquals(result.getCharset(), null);
  }

  @Test(description = "media type + charset")
  public void testCase02() {
    HeaderContentTypeImpl result;
    result = parse("text/html; charset=utf8\r\n");

    assertEquals(result.getMediaType(), TextType.HTML);

    assertEquals(result.getCharset(), StandardCharsets.UTF_8);
  }

  @Test(description = "media type + quoted charset")
  public void testCase03() {
    HeaderContentTypeImpl result;
    result = parse("text/html; charset=\"utf8\"\r\n");

    assertEquals(result.getMediaType(), TextType.HTML);

    assertEquals(result.getCharset(), StandardCharsets.UTF_8);
  }

  private HeaderContentTypeImpl parse(String source) {
    HeaderContentTypeImpl contentType;
    contentType = new HeaderContentTypeImpl();

    return HeaderTesting.parse(contentType, source);
  }

}