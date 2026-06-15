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

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import objectos.y.BasicFileAttributesY;
import org.testng.annotations.Test;

public class StaticFilesETagTest {

  @Test
  public void apply01() {
    final StaticFilesETag subject;
    subject = new StaticFilesETag(0L);

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.lastModifiedTime = FileTime.fromMillis(0b1111_1111_1111);

      opts.size = 0b1111L;
    });

    assertEquals(
        subject.apply(attributes),

        "fff-f"
    );
  }

}
