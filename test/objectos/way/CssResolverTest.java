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

import static org.testng.Assert.assertEquals;

import java.util.Map;
import org.testng.annotations.Test;

public class CssResolverTest {

  @Test
  public void ofTransitionProperty01() {
    Map<String, String> props;
    props = Map.of("none", "none");

    CssResolverOfTransitionProperty resolver;
    resolver = new CssResolverOfTransitionProperty(props);

    Css.Rule rule;
    rule = resolver.create("transition-none", Css.EMPTY_MODIFIER, false, "none");

    assertEquals(
        rule.toString(),

        """
        .transition-none { transition-property: none }
        """
    );
  }

  @Test
  public void ofTransitionProperty02() {
    Map<String, String> props;
    props = Map.of("all", "all/cubic-bezier(0.4, 0, 0.2, 1)");

    CssResolverOfTransitionProperty resolver;
    resolver = new CssResolverOfTransitionProperty(props);

    Css.Rule rule;
    rule = resolver.create("transition-all", Css.EMPTY_MODIFIER, false, "all/cubic-bezier(0.4, 0, 0.2, 1)");

    assertEquals(
        rule.toString(),

        """
        .transition-all { transition-property: all; transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1) }
        """
    );
  }

  @Test
  public void ofTransitionProperty03() {
    Map<String, String> props;
    props = Map.of("all", "all/cubic-bezier(0.4, 0, 0.2, 1)/150ms");

    CssResolverOfTransitionProperty resolver;
    resolver = new CssResolverOfTransitionProperty(props);

    Css.Rule rule;
    rule = resolver.create("transition-all", Css.EMPTY_MODIFIER, false, "all/cubic-bezier(0.4, 0, 0.2, 1)/150ms");

    assertEquals(
        rule.toString(),

        """
        .transition-all {
          transition-property: all;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        """
    );
  }

}