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

import org.testng.annotations.Test;

public class ByteArrayWriterTest extends AbstractGitTest {

  @Test
  public void testCase12() {
    ByteArrayWriter w;
    w = engine.getByteArrayWriter();

    try {
      w.putLong(Long.MAX_VALUE);

      assertEquals(w.getLong(0), Long.MAX_VALUE);
    } finally {
      engine.putByteArrayWriter(w);
    }
  }

}