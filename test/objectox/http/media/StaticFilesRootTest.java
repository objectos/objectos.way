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

import java.io.IOException;
import java.nio.file.Path;
import objectos.http.Request;
import objectos.way.Note;
import objectos.way.Y;
import objectos.y.PathY;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class StaticFilesRootTest {

  private final Path directory = PathY.nextDir();

  private final Note.Sink noteSink = Y.noteSink();

  @Test(description = "root is resolvable")
  public void resolve01() throws IOException {
    final StaticFilesRoot root;
    root = new StaticFilesRoot(directory, noteSink);

    final Request req;
    req = Request.create(opts -> {
      opts.path("/");
    });

    final Path res;
    res = root.resolve(req);

    assertEquals(res, directory);
  }

  @Test(description = "file @ root is resolvable")
  public void resolve02() throws IOException {
    final StaticFilesRoot root;
    root = new StaticFilesRoot(directory, noteSink);

    final Request req;
    req = Request.create(opts -> {
      opts.path("/at-root.txt");
    });

    final Path res;
    res = root.resolve(req);

    assertEquals(res, directory.resolve("at-root.txt"));
  }

  @Test(description = "dir @ root is resolvable")
  public void resolve03() throws IOException {
    final StaticFilesRoot root;
    root = new StaticFilesRoot(directory, noteSink);

    final Request req;
    req = Request.create(opts -> {
      opts.path("/subdir");
    });

    final Path res;
    res = root.resolve(req);

    assertEquals(res, directory.resolve("subdir"));
  }

  @Test(description = "root parent is not resolvable")
  public void resolve04() throws IOException {
    final StaticFilesRoot root;
    root = new StaticFilesRoot(directory, noteSink);

    final Request req;
    req = Request.create(opts -> {
      opts.path("/subdir/../../forbidden.txt");
    });

    final Path res;
    res = root.resolve(req);

    assertEquals(res, null);
  }

  @Test(description = "file @ root is resolvable, even if /.. are used")
  public void resolve05() throws IOException {
    final StaticFilesRoot root;
    root = new StaticFilesRoot(directory, noteSink);

    final Request req;
    req = Request.create(opts -> {
      opts.path("/subdir/../accessible.txt");
    });

    final Path res;
    res = root.resolve(req);

    assertEquals(res, directory.resolve("accessible.txt"));
  }

}
