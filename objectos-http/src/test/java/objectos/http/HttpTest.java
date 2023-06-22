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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class HttpTest extends AbstractHttpTest {

  @Test(description = TestCase0001.DESCRIPTION)
  public void testCase0001() throws IOException {
    URLConnection c;
    c = GET("/");

    assertEquals(readString(c), "<!doctype html><title>/index.html</title>");

    Map<String, List<String>> hf;
    hf = c.getHeaderFields();

    assertEquals(hf.size(), 6);

    assertEquals(c.getContentLength(), 41);
    assertEquals(c.getContentType(), "text/html; charset=utf8");
    assertEquals(c.getHeaderField("Server"), "Objectos HTTP");
    assertTrue(hf.containsKey("Date"));
    assertTrue(hf.containsKey("Last-Modified"));
  }

}
