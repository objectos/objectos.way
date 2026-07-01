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

import java.util.Map;
import objectos.http.StaticFilesOptions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StaticFilesTypesBuilderTest {

  @DataProvider
  public Object[][] contentTypesProvider() {
    return new Object[][] {
        {
            StaticFilesOptions.DEFAULT_CONTENT_TYPES,
            "application/octet-stream",
            Map.ofEntries(
                Map.entry(".css", "text/stylesheet; charset=utf-8"),
                Map.entry(".js", "text/javascript; charset=utf-8"),
                Map.entry(".java", "text/plain; charset=utf-8"),
                Map.entry(".html", "text/html; charset=utf-8"),
                Map.entry(".txt", "text/plain; charset=utf-8"),
                Map.entry(".xml", "text/xml; charset=utf-8"),
                Map.entry(".ico", "image/vnd.microsoft.icon"),
                Map.entry(".jpg", "image/jpeg"),
                Map.entry(".png", "image/png"),
                Map.entry(".mp4", "video/mp4"),
                Map.entry(".woff2", "font/woff2")
            )
        }
    };
  }

  @Test(dataProvider = "contentTypesProvider")
  public void contentTypes01(String s, String defaultType, Map<String, String> expected) {
    final StaticFilesTypesBuilder subject;
    subject = new StaticFilesTypesBuilder("foo/bar");

    subject.contentTypes(s);

    final StaticFilesTypes res;
    res = subject.build();

    assertEquals(res.defaultType, defaultType);
    assertEquals(res.types, expected);
  }

  @Test(description = "reject empty file extension")
  public void contentTypes02() {
    final StaticFilesTypesBuilder subject;
    subject = new StaticFilesTypesBuilder("foo/bar");

    try {
      subject.contentTypes("""
      .css: text/stylesheet
      : invalid/value
      """);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "File extension must not be empty");
    }
  }

}
