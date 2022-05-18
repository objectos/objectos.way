/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class Event3Test {

  final Event3<Void, Void, Void> TRACE = Event3.trace();

  final Event3<Void, Void, Void> DEBUG = Event3.debug();

  final Event3<Void, Void, Void> INFO = Event3.info();

  final Event3<Void, Void, Void> WARN = Event3.warn();

  final Event3<Void, Void, Void> ERROR = Event3.error();

  @Test
  public void key() {
    assertEquals(TRACE.key(), "Event3Test.java:24");
    assertEquals(DEBUG.key(), "Event3Test.java:26");
    assertEquals(INFO.key(), "Event3Test.java:28");
    assertEquals(WARN.key(), "Event3Test.java:30");
    assertEquals(ERROR.key(), "Event3Test.java:32");
  }

  @Test
  public void level() {
    assertEquals(TRACE.level(), Level.TRACE);
    assertEquals(DEBUG.level(), Level.DEBUG);
    assertEquals(INFO.level(), Level.INFO);
    assertEquals(WARN.level(), Level.WARN);
    assertEquals(ERROR.level(), Level.ERROR);
  }

  @Test
  public void source() {
    assertEquals(TRACE.source(), "objectos.lang.Event3Test");
    assertEquals(DEBUG.source(), "objectos.lang.Event3Test");
    assertEquals(INFO.source(), "objectos.lang.Event3Test");
    assertEquals(WARN.source(), "objectos.lang.Event3Test");
    assertEquals(ERROR.source(), "objectos.lang.Event3Test");
  }

}