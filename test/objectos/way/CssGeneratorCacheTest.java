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
package objectos.way;

import static org.testng.Assert.assertSame;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssGeneratorCacheTest {

  @Test(description = "cache hit")
  public void testCase01() {
    CssRule foo;
    foo = new CssRule(CssKey.$NOOP, "foo", List.of(), CssProperties.NOOP);

    class Impl extends CssGeneratorCache {
      private CssRule hit;

      public Impl() {
        rules.put("foo", foo);
      }

      @Override
      final void onCacheHit(CssRule existing) {
        hit = existing;
      }

      @Override
      final CssRule onCacheMiss(String className) {
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
    CssRule foo;
    foo = new CssRule(CssKey.$NOOP, "foo", List.of(), CssProperties.NOOP);

    class Impl extends CssGeneratorCache {
      @Override
      final CssRule onCacheMiss(String className) {
        return foo;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.onSplit("foo");

    assertSame(impl.rules.get("foo"), foo);
  }

}