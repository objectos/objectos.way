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
package objectox.html.style;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import objectos.html.style.All;
import objectos.html.style.Hover;
import objectos.html.style.Large;
import objectos.notes.Level;
import objectos.notes.NoteSink;
import objectos.notes.console.ConsoleNoteSink;
import objectox.html.style.StylesGeneratorImpl.State;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StylesGeneratorImplTest {

  private StylesGeneratorImpl impl;

  @BeforeClass
  public void beforeClass() {
    NoteSink noteSink;
    noteSink = ConsoleNoteSink.of(Level.TRACE);

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
    assertEquals(impl.constantPoolIndex.length, 39);
    assertEquals(impl.iteratorIndex, 1);
    assertEquals(impl.state, State.NEXT_POOL_INDEX);

    impl.execute();

    assertEquals(impl.iteratorIndex, 2);
    assertEquals(impl.state, State.NEXT_POOL_INDEX);

    impl.execute();

    assertEquals(impl.iteratorIndex, 3);
    assertEquals(impl.state, State.NEXT_POOL_INDEX);

    for (int i = 3; i <= 39; i++) {
      impl.execute();
    }

    assertEquals(impl.iteratorIndex, 1);
    assertEquals(impl.state, State.NEXT_POOL_ENTRY);

    impl.execute();

    assertEquals(impl.iteratorIndex, 2);
    assertEquals(impl.state, State.NEXT_POOL_ENTRY);

    impl.execute();

    assertEquals(impl.iteratorIndex, 3);
    assertEquals(impl.state, State.NEXT_POOL_ENTRY);

    for (int i = 3; i <= 39; i++) {
      impl.execute();
    }

    assertEquals(impl.state, State.STOP);

    Map<String, Map<String, Set<String>>> result;
    result = impl.utilities;

    assertEquals(result.size(), 1);
    assertEquals(result.containsKey("All"), true);

    Map<String, Set<String>> all;
    all = result.get("All");

    assertEquals(all.size(), 1);
    assertEquals(all.get("objectos.html.style.All$Display"), Set.of("BLOCK"));

    assertEquals(
        impl.generate(),

        """
      .%s { display: block }
      """.formatted(All.Display.BLOCK.className())
    );
  }

  @Test
  public void utility02() {
    // parse phase
    impl.scan(Utility02.class);

    assertEquals(impl.state, State.STOP);

    Map<String, Map<String, Set<String>>> result;
    result = impl.utilities;

    assertEquals(result.size(), 1);
    assertEquals(result.containsKey("Large"), true);

    Map<String, Set<String>> large;
    large = result.get("Large");

    assertEquals(large.size(), 1);
    assertEquals(large.get("objectos.html.style.Large$Display"), Set.of("FLEX"));

    assertEquals(
        impl.generate(),

        """
      @media (min-width: 1024px) {
        .%s { display: flex }
      }
      """.formatted(Large.Display.FLEX.className())
    );
  }

  @Test
  public void utility03() {
    // parse phase
    impl.scan(Utility03.class);

    assertEquals(impl.state, State.STOP);

    Map<String, Map<String, Set<String>>> result;
    result = impl.utilities;

    assertEquals(result.size(), 1);
    assertEquals(result.containsKey("Hover"), true);

    Map<String, Set<String>> hover;
    hover = result.get("Hover");

    assertEquals(hover.size(), 1);
    assertEquals(hover.get("objectos.html.style.Hover$BackgroundColor"), Set.of("SLATE_100"));

    assertEquals(
        impl.generate(),

        """
      .%s:hover { background-color: rgb(241 245 249) }
      """.formatted(Hover.BackgroundColor.SLATE_100.className())
    );
  }

  @Test(enabled = false)
  public void utility04() {
    // parse phase
    impl.scan(Utility04.class);

    assertEquals(
        impl.generate(),

        """
      .aaag { color: rgb(241 245 249) }
      @media (min-width: 640px) {
        .aahi { color: rgb(226 232 240) }
      }
      @media (min-width: 768px) {
        .aaok { color: rgb(203 213 225) }
      }
      @media (min-width: 1024px) {
        .aavm { color: rgb(148 163 184) }
      }
      @media (min-width: 1280px) {
        .aa2o { color: rgb(100 116 139) }
      }
      @media (min-width: 1536px) {
        .abdq { color: rgb(71 85 105) }
      }
      .abks:hover { color: rgb(51 65 85) }
      """.formatted(Hover.BackgroundColor.SLATE_100.className())
    );
  }

}