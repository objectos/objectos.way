/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class CssEngineTest03Classes {

  private static class Subj01 {}

  private static class Subj02 {}

  @Test
  public void testCase01() {
    test(
        Set.of(Subj01.class),

        name(Subj01.class)
    );
  }

  @Test
  public void testCase02() {
    test(
        Set.of(Subj01.class, Subj02.class),

        name(Subj01.class),
        name(Subj02.class)
    );
  }

  private String name(Class<?> type) {
    return type.getName().replace('.', '/');
  }

  private void test(Set<Class<?>> classes, String... expected) {
    final CssEngineClassFiles tester;
    tester = new CssEngineClassFiles();

    final Set<String> names;
    names = classes.stream().map(Class::getName).collect(Collectors.toSet());

    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final CssEngine.Classes scanner;
    scanner = new CssEngine.Classes(tester, names, noteSink);

    scanner.scan();

    assertEquals(tester.scan, Set.of(expected));

    assertEquals(tester.scanIfAnnotated, Set.of());
  }

}