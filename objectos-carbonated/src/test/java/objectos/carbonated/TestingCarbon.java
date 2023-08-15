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
package objectos.carbonated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import objectos.css.util.Next;
import objectos.html.HtmlTemplate;

final class TestingCarbon {

  interface PageFactory {
    HtmlTemplate create(Carbon carbon);
  }

  public static final Carbon carbon;

  static {
    Next.Builder nb;
    nb = Next.builder();

    Random r;
    r = new Random(123456789L);

    nb.random(r);

    nb.nameLength(3);

    Next next;
    next = nb.build();

    Carbon.Builder cb;
    cb = Carbon.builder();

    cb.next(next);

    carbon = cb.build();
  }

  public static void write(String fileName, PageFactory factory) throws IOException {
    HtmlTemplate page;
    page = factory.create(carbon);

    String tmpdir;
    tmpdir = System.getProperty("java.io.tmpdir");

    Path file;
    file = Path.of(tmpdir, "carbonated", fileName);

    Files.writeString(file, page.toString());
  }

}