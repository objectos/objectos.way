/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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
package objectox.html.stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

public class ScanOpTest {

  private final ScanOp op = new ScanOp() {};

  @Test(description = """
  documentNode:
  - default method throws
  """)
  public void documentNode() {
    try {
      op.documentNode();

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Not a document node op");
    }
  }

  @Test(description = """
  elementAttribute:
  - default method throws
  """)
  public void elementAttribute() {
    try {
      op.elementAttribute();

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Not an element attribute op");
    }
  }

  @Test(description = """
  elementNode:
  - default method throws
  """)
  public void elementNode() {
    try {
      op.elementNode();

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Not an element node op");
    }
  }

  @Test(description = """
  isAttribute:
  - defaults to false
  """)
  public void isAttribute() {
    assertEquals(op.isAttribute(), false);
  }

}
