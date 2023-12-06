/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import static org.testng.Assert.assertEquals;

import objectos.core.io.Charsets;
import org.testng.annotations.Test;

public class Base64Test {

  @Test
  public void decode() {
    testDecode("Zg==", "f");
    testDecode("Zm8=", "fo");
    testDecode("Zm9v", "foo");
    testDecode("Zm9v", "foo");
    testDecode("Zm9vYg==", "foob");
    testDecode("Zm9vYmE=", "fooba");
    testDecode("Zm9vYmFy", "foobar");

    testDecode("TWFueSBoYW5kcyBtYWtlIGxpZ2h0IHdvcmsu", "Many hands make light work.");

    assertEquals(
        TestingGit.decode64(
            "TWFueSBoYW5kcyBtY",
            "WtlIGxpZ2h0IHdvcmsu"
        ),

        "Many hands make light work.".getBytes(Charsets.usAscii())
    );
  }

  private void testDecode(String src, String res) {
    assertEquals(
        TestingGit.decode64(src),

        res.getBytes(Charsets.usAscii())
    );
  }

}