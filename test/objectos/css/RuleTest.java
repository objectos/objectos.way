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

import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.annotations.Test;

public class RuleTest {

  @Test
  public void writeClassName() {
    testClassName("m-0", ".m-0 {  }\n");
    testClassName("sm:m-1", ".sm\\:m-1 {  }\n");
    testClassName("2xl:m-2", ".\\32xl\\:m-2 {  }\n");
  }

  private void testClassName(String className, String expected) {
    StringBuilder out;
    out = new StringBuilder();

    Rule rule;
    rule = new Rule(0, className, List.of());

    rule.writeTo(out, 1);

    assertEquals(out.toString(), expected);
  }

}
