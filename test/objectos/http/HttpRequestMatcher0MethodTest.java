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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.stream.Stream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestMatcher0MethodTest {

  @DataProvider
  public Iterator<HttpMethod> methodProvider() {
    return Stream.of(HttpMethod.VALUES).iterator();
  }

  @Test(dataProvider = "methodProvider")
  public void match(HttpMethod method) {
    final HttpRequestMatcher0Method matcher;
    matcher = new HttpRequestMatcher0Method(method);

    final HttpExchange0 http;
    http = (HttpExchange0) HttpExchange.create(opts -> opts.method(method));

    assertTrue(matcher.match(http));
  }

  @Test
  public void matchHead() {
    final HttpRequestMatcher0Method matcher;
    matcher = new HttpRequestMatcher0Method(HttpMethod.GET);

    final HttpExchange0 http;
    http = (HttpExchange0) HttpExchange.create(opts -> opts.method(HttpMethod.HEAD));

    assertTrue(matcher.match(http));
  }

  @Test(dataProvider = "methodProvider")
  public void matchNot(HttpMethod method) {
    final HttpRequestMatcher0Method matcher;
    matcher = new HttpRequestMatcher0Method(method);

    final EnumSet<HttpMethod> single;
    single = EnumSet.of(method);

    final EnumSet<HttpMethod> others;
    others = EnumSet.complementOf(single);

    final HttpMethod other;
    other = others.iterator().next();

    final HttpExchange0 http;
    http = (HttpExchange0) HttpExchange.create(opts -> opts.method(other));

    assertFalse(matcher.match(http));
  }

}
