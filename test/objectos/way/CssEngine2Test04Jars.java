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

import java.util.Set;
import org.slf4j.Logger;
import org.testng.annotations.Test;

public class CssEngine2Test04Jars {

  @Test
  public void scanJarFile01() {
    final CssEngine2ClassFiles tester;
    tester = new CssEngine2ClassFiles();

    final Set<Class<?>> jars;
    jars = Set.of(Logger.class);

    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final CssEngine2.Jars scanner;
    scanner = new CssEngine2.Jars(tester, jars, noteSink);

    scanner.scan();

    assertEquals(tester.scan, Set.of());

    assertEquals(tester.scanIfAnnotated.contains("org/slf4j/Logger"), true);
    assertEquals(tester.scanIfAnnotated.contains("org/slf4j/Marker"), true);
  }

}