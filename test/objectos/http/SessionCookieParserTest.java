/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
import java.util.function.Function;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SessionCookieParserTest {

  @DataProvider
  public Object[][] parseProvider() {
    return new Object[][] {
        // valid
        {true, "1 value",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid first",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; other=foo"},
        {true, "2 values, valid second",
            "other=foo; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid second, first value empty",
            "other=; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid second, first empty",
            "=; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, same name, valid first",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; WAY=foo"},
        {true, "2 values, same name, valid second",
            "WAY=foo; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid second, no space after semicolon",
            "other=foo;WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "valid cookie with trailing semicolon",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=;"},
        {true, "valid cookie with leading semicolon",
            ";WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "3 values, valid one in the middle",
            "foo=bar; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; baz=qux"},

        // invalid
        {false, "empty header value",
            ""},
        {false, "1 value, empty cookie value",
            "WAY="},
        {false, "1 value, length = length - 1",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ"},
        {false, "1 value, valid length, invalid value",
            "WAY=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX="},
        {false, "header is null",
            null},
        {false, "1 value, wrong cookie name",
            "SESSIONID=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "1 value, name correct but mixed case",
            "way=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "1 value, name correct but extra whitespace",
            "WAY = AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "1 value, name correct but tab character between name and equals",
            "WAY\t=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "cookie name is substring of correct name",
            "WA=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "cookie name starts with correct name",
            "WAY_TOO_LONG=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "valid value surrounded by spaces",
            "  WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=  "}
    };
  }

  @Test(dataProvider = "parseProvider")
  public void parse(boolean valid, String description, String headerValue) {
    final SessionCookieParser parser;
    parser = new SessionCookieParser("WAY");

    final Function<String, String> processor;
    processor = s -> s.equals("AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=") ? s : null;

    final String result;
    result = parser.parse(headerValue, processor);

    if (valid) {
      assertEquals(result, "AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=");
    } else {
      assertEquals(result, null);
    }
  }

}