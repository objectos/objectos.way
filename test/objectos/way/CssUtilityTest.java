/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class CssUtilityTest {

  @Test(description = "order by utility first")
  public void ordering01() {
    List<CssVariant.OfClassName> empty = List.of();
    List<CssVariant.OfClassName> hover = List.of(new CssVariant.Suffix(":hover"));
    List<CssVariant.OfClassName> active = List.of(new CssVariant.Suffix(":active"));

    List<CssUtility> utilities = new ArrayList<>();

    CssUtility a1 = utility(Css.Key.APPEARANCE, "a-1", empty);
    utilities.add(a1);

    CssUtility a2 = utility(Css.Key.APPEARANCE, "a-2", empty);
    utilities.add(a2);

    CssUtility a1Active = utility(Css.Key.APPEARANCE, "a-2:active", active);
    utilities.add(a1Active);

    CssUtility a1Hover = utility(Css.Key.APPEARANCE, "a-1:hover", hover);
    utilities.add(a1Hover);

    CssUtility b1 = utility(Css.Key.DISPLAY, "b-1", empty);
    utilities.add(b1);

    CssUtility b2 = utility(Css.Key.DISPLAY, "b-2", empty);
    utilities.add(b2);

    Collections.sort(utilities);

    assertSame(utilities.get(0), a1);
    assertSame(utilities.get(1), a2);
    assertSame(utilities.get(2), a1Hover);
    assertSame(utilities.get(3), a1Active);
    assertSame(utilities.get(4), b1);
    assertSame(utilities.get(5), b2);
  }

  @Test
  public void writeClassName() {
    testClassName("m-0", ".m-0 {}\n");
    testClassName("sm:m-1", ".sm\\:m-1 {}\n");
    testClassName("2xl:m-2", ".\\32 xl\\:m-2 {}\n");
  }

  private CssUtility utility(Css.Key key, String className, List<CssVariant.OfClassName> formats) {
    CssModifier modifier;
    modifier = new CssModifier(List.of(), formats);

    return new CssUtility(key, className, modifier, CssProperties.NOOP);
  }

  private void testClassName(String className, String expected) {
    try {
      StringBuilder out;
      out = new StringBuilder();

      CssWriter w;
      w = new CssWriter(out);

      CssUtility utility;
      utility = utility(Css.Key.COLOR, className, List.of());

      utility.writeTo(w, 0);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

}
