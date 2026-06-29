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
package objectox.dev;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ReloadingClassLoaderTest {

  @Test(description = "do not reload if it fails binary name filter")
  public void testCase01() {
    assertEquals(
        ReloadingClassLoaderY.sub(opts -> {
          opts.binaryNameFilter = name -> "i.do.not.exist".equals(name);
        }),

        "original"
    );
  }

  @Test(description = "do not reload if it file does not exist")
  public void testCase02() {
    assertEquals(
        ReloadingClassLoaderY.sub(opts -> {
          opts.className = "objectox.dev.ReloadingClassLoaderOther";
        }),

        "original"
    );
  }

  @Test(description = "do not reload if it fails class file filter")
  public void testCase03() {
    assertEquals(
        ReloadingClassLoaderY.sub(opts -> {
          opts.binaryNameFilter = name -> "objectox.dev.ReloadingClassLoaderSub".equals(name);

          opts.classFileFilter = _ -> false;
        }),

        "original"
    );
  }

  @Test(description = "reload otherwise")
  public void testCase04() {
    assertEquals(
        ReloadingClassLoaderY.sub(opts -> {
          opts.binaryNameFilter = name -> "objectox.dev.ReloadingClassLoaderSub".equals(name);

          opts.classFileFilter = _ -> true;
        }),

        "reloaded"
    );
  }

}
