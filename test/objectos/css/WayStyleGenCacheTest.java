/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

import static org.testng.Assert.assertSame;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WayStyleGenCacheTest {

  @Test(description = "cache hit")
  public void testCase01() {
    Rule foo;
    foo = new Rule(Utility.DISPLAY, List.of());

    class Impl extends WayStyleGenCache {
      private Rule hit;

      public Impl() {
        rules.put("foo", foo);
      }

      @Override
      final void onCacheHit(Rule existing) {
        hit = existing;
      }

      @Override
      final Rule onCacheMiss(String className) {
        Assert.fail("It should have returned from the cache");

        return null;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.onSplit("foo");

    assertSame(impl.hit, foo);
  }

  @Test(description = "cache miss")
  public void testCase02() {
    Rule foo;
    foo = new Rule(Utility.DISPLAY, List.of());

    class Impl extends WayStyleGenCache {
      @Override
      final Rule onCacheMiss(String className) {
        return foo;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.onSplit("foo");

    assertSame(impl.rules.get("foo"), foo);
  }

}