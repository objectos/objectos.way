/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.annotations.Test;

public class HttpRequestTargetPathTest {

  @Test
  public void is() {
    HttpRequestTargetPath path;
    path = new HttpRequestTargetPath();

    path.set("/");

    assertEquals(path.is("/"), true);
    assertEquals(path.is("/index.html"), false);
  }

  @Test
  public void startsWith() {
    HttpRequestTargetPath path;
    path = new HttpRequestTargetPath();

    path.set("/foo/bar.html");

    assertEquals(path.startsWith("/foo"), true);
    assertEquals(path.startsWith("/foo/"), true);
    assertEquals(path.startsWith("/foo/bar.html"), true);
    assertEquals(path.startsWith("/foo/bar.html/"), false);
    assertEquals(path.startsWith("/goo"), false);
  }

  @Test
  public void segments() {
    HttpRequestTargetPath path;
    path = new HttpRequestTargetPath();

    path.set("/");

    List<Http.Request.Target.Path.Segment> segments;
    segments = path.segments();

    assertEquals(segments.size(), 1);
    assertEquals(segments.get(0).toString(), "");

    path.set("/index.html");

    segments = path.segments();

    assertEquals(segments.size(), 1);
    assertEquals(segments.get(0).toString(), "index.html");

    path.set("/foo/");

    segments = path.segments();

    assertEquals(segments.size(), 2);
    assertEquals(segments.get(0).toString(), "foo");
    assertEquals(segments.get(1).toString(), "");

    path.set("/foo/index.html");

    segments = path.segments();

    assertEquals(segments.size(), 2);
    assertEquals(segments.get(0).toString(), "foo");
    assertEquals(segments.get(1).toString(), "index.html");
  }

}