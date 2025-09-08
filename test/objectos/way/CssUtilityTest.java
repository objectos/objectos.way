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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class CssUtilityTest {

  @Test(description = "order by utility first")
  public void ordering01() {
    List<CssVariant> empty = List.of();
    List<CssVariant> hover = List.of(CssVariant.suffix(":hover"));
    List<CssVariant> active = List.of(CssVariant.suffix(":active"));

    List<CssUtility> utilities = new ArrayList<>();

    CssUtility a1 = utility("appearance", "a-1", empty);
    utilities.add(a1);

    CssUtility a2 = utility("appearance", "a-2", empty);
    utilities.add(a2);

    CssUtility a1Active = utility("appearance", "a-2:active", active);
    utilities.add(a1Active);

    CssUtility a1Hover = utility("appearance", "a-1:hover", hover);
    utilities.add(a1Hover);

    CssUtility b1 = utility("display", "b-1", empty);
    utilities.add(b1);

    CssUtility b2 = utility("display", "b-2", empty);
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
    testClassName("m-0", ".m-0");
    testClassName("sm:m-1", ".sm\\:m-1");
    testClassName("2xl:m-2", ".\\32 xl\\:m-2");
  }

  private CssUtility utility(String key, String className, List<CssVariant> formats) {
    CssModifier modifier;
    modifier = new CssModifier(List.of(), formats);

    return new CssUtility(key, className, modifier, CssProperties.NOOP);
  }

  private void testClassName(String className, String expected) {
    StringBuilder out;
    out = new StringBuilder();

    CssUtility utility;
    utility = utility("color", className, List.of());

    utility.writeClassName(out);

    assertEquals(out.toString(), expected);
  }

}
