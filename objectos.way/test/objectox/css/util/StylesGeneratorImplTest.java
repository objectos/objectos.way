/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.css.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;
import objectos.lang.NoteSink;
import objectos.lang.TestingNoteSink;
import objectox.css.util.StylesGeneratorImpl.State;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StylesGeneratorImplTest {

  private StylesGeneratorImpl impl;

  @BeforeClass
  public void beforeClass() {
    NoteSink noteSink;
    noteSink = TestingNoteSink.INSTANCE;

    impl = new StylesGeneratorImpl(noteSink);
  }

  @Test
  public void utility01() throws IOException {
    // parse phase
    String binaryName;
    binaryName = Utility01.class.getName();

    impl.binaryName = binaryName;
    impl.state = State.START;

    impl.execute();

    assertEquals(impl.binaryName, binaryName);
    assertEquals(impl.bytes, null);
    assertEquals(impl.bytesIndex, 0);
    assertEquals(impl.constantPoolIndex, null);
    assertEquals(impl.constantPoolEntries, null);
    assertEquals(impl.iteratorIndex, 0);
    assertEquals(impl.state, State.LOAD_CLASS);

    impl.execute();

    assertNotNull(impl.bytes);
    assertEquals(impl.bytesIndex, 0);
    assertEquals(impl.state, State.VERIFY_MAGIC);

    impl.execute();

    assertEquals(impl.bytesIndex, 4);
    assertEquals(impl.state, State.CONSTANT_POOL_COUNT);

    impl.execute();

    assertEquals(impl.bytesIndex, 10);
    assertEquals(impl.constantPoolIndex.length, 36);
    assertEquals(impl.iteratorIndex, 1);
    assertEquals(impl.state, State.NEXT_POOL_INDEX);

    impl.execute();

    assertEquals(impl.bytesIndex, 13);
    assertEquals(impl.iteratorIndex, 2);
    assertEquals(impl.state, State.NEXT_POOL_INDEX);

    impl.execute();

    assertEquals(impl.bytesIndex, 43);
    assertEquals(impl.iteratorIndex, 3);
    assertEquals(impl.state, State.NEXT_POOL_INDEX);

    for (int i = 3; i <= 36; i++) {
      impl.execute();
    }

    assertEquals(impl.bytesIndex, 573);
    assertEquals(impl.iteratorIndex, 1);
    assertEquals(impl.state, State.NEXT_POOL_ENTRY);

    impl.execute();

    assertEquals(impl.iteratorIndex, 2);
    assertEquals(impl.state, State.NEXT_POOL_ENTRY);

    impl.execute();

    assertEquals(impl.iteratorIndex, 3);
    assertEquals(impl.state, State.NEXT_POOL_ENTRY);

    for (int i = 3; i <= 36; i++) {
      impl.execute();
    }

    assertEquals(impl.state, State.STOP);

    Map<String, Map<String, String>> result;
    result = impl.result;

    assertEquals(result.size(), 1);
    assertEquals(result.containsKey(""), true);

    Map<String, String> unprefixed;
    unprefixed = result.get("");

    assertEquals(unprefixed.size(), 1);
    assertEquals(unprefixed.get("Display"), "BLOCK");

    /*


    assertEquals(
      impl.generate(),

      """
      .%s { display: block }
      """.formatted(Display.BLOCK.className())
    );
    */
  }

}