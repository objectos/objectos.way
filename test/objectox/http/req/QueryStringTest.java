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
package objectox.http.req;

import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class QueryStringTest {

  @Test
  public void queryString01() {
    final QueryString q;
    q = new QueryString(Map.of());

    assertEquals(q.toString(), "");
  }

  @Test
  public void queryString02() {
    final QueryString q;
    q = new QueryString(Map.of("param", "value"));

    assertEquals(q.toString(), "param=value");
  }

  @Test
  public void queryString03() {
    final QueryString q;
    q = new QueryString(Map.of("param", List.of("v1", "v2")));

    assertEquals(q.toString(), "param=v1&param=v2");
  }

  @Test
  public void queryString04() {
    final Map<String, Object> map = new LinkedHashMap<>();

    map.put("p1", "v1");
    map.put("p2", List.of("u1", "u2"));
    map.put("p3", "v3");

    final QueryString q;
    q = new QueryString(map);

    assertEquals(q.toString(), "p1=v1&p2=u1&p2=u2&p3=v3");
  }

}
