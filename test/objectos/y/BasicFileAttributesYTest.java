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
package objectos.y;

import static org.testng.Assert.assertEquals;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.function.Consumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BasicFileAttributesYTest {

  @DataProvider
  public Object[][] createProvider() {
    return new Object[][] {
        {
            "Default options",

            options(_ -> {}),

            assertions(attr -> {
              assertEquals(attr.creationTime(), null);
              assertEquals(attr.fileKey(), null);
              assertEquals(attr.lastAccessTime(), null);
              assertEquals(attr.lastModifiedTime(), null);

              assertEquals(attr.isDirectory(), false);
              assertEquals(attr.isOther(), false);
              assertEquals(attr.isRegularFile(), false);
              assertEquals(attr.isSymbolicLink(), false);

              assertEquals(attr.size(), 0);
            })
        },

        {
            "file key",

            options(opts -> {
              opts.fileKey = Long.MAX_VALUE;
            }),

            assertions(attr -> {
              assertEquals(attr.creationTime(), null);
              assertEquals(attr.fileKey(), Long.MAX_VALUE);
              assertEquals(attr.lastAccessTime(), null);
              assertEquals(attr.lastModifiedTime(), null);

              assertEquals(attr.isDirectory(), false);
              assertEquals(attr.isOther(), false);
              assertEquals(attr.isRegularFile(), false);
              assertEquals(attr.isSymbolicLink(), false);

              assertEquals(attr.size(), 0);
            })
        },

        {
            "last modified time",

            options(opts -> {
              opts.lastModifiedTime = FileTime.fromMillis(123L);
            }),

            assertions(attr -> {
              assertEquals(attr.creationTime(), null);
              assertEquals(attr.fileKey(), null);
              assertEquals(attr.lastAccessTime(), null);
              assertEquals(attr.lastModifiedTime(), FileTime.fromMillis(123L));

              assertEquals(attr.isDirectory(), false);
              assertEquals(attr.isOther(), false);
              assertEquals(attr.isRegularFile(), false);
              assertEquals(attr.isSymbolicLink(), false);

              assertEquals(attr.size(), 0);
            })
        },

        {
            "Regular file",

            options(opts -> {
              opts.regularFile = true;
            }),

            assertions(attr -> {
              assertEquals(attr.creationTime(), null);
              assertEquals(attr.fileKey(), null);
              assertEquals(attr.lastAccessTime(), null);
              assertEquals(attr.lastModifiedTime(), null);

              assertEquals(attr.isDirectory(), false);
              assertEquals(attr.isOther(), false);
              assertEquals(attr.isRegularFile(), true);
              assertEquals(attr.isSymbolicLink(), false);

              assertEquals(attr.size(), 0);
            })
        },

        {
            "size",

            options(opts -> {
              opts.size = 123L;
            }),

            assertions(attr -> {
              assertEquals(attr.creationTime(), null);
              assertEquals(attr.fileKey(), null);
              assertEquals(attr.lastAccessTime(), null);
              assertEquals(attr.lastModifiedTime(), null);

              assertEquals(attr.isDirectory(), false);
              assertEquals(attr.isOther(), false);
              assertEquals(attr.isRegularFile(), false);
              assertEquals(attr.isSymbolicLink(), false);

              assertEquals(attr.size(), 123L);
            })
        }
    };
  }

  private Consumer<? super BasicFileAttributesY> options(Consumer<? super BasicFileAttributesY> options) { return options; }

  private Consumer<BasicFileAttributes> assertions(Consumer<BasicFileAttributes> assertions) { return assertions; }

  @Test(dataProvider = "createProvider")
  public void create(String description, Consumer<? super BasicFileAttributesY> options, Consumer<BasicFileAttributes> assertions) {
    final BasicFileAttributes res;
    res = BasicFileAttributesY.create(options);

    assertions.accept(res);
  }

}
