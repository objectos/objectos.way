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

public class Event1Test {

  final Event1<Void> TRACE = Event1.trace();

  final Event1<Void> DEBUG = Event1.debug();

  final Event1<Void> INFO = Event1.info();

  final Event1<Void> WARN = Event1.warn();

  final Event1<Void> ERROR = Event1.error();

  @Test
  public void key() {
    assertEquals(TRACE.key(), "Event1Test.java:24");
    assertEquals(DEBUG.key(), "Event1Test.java:26");
    assertEquals(INFO.key(), "Event1Test.java:28");
    assertEquals(WARN.key(), "Event1Test.java:30");
    assertEquals(ERROR.key(), "Event1Test.java:32");
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
    assertEquals(TRACE.source(), "objectos.lang.Event1Test");
    assertEquals(DEBUG.source(), "objectos.lang.Event1Test");
    assertEquals(INFO.source(), "objectos.lang.Event1Test");
    assertEquals(WARN.source(), "objectos.lang.Event1Test");
    assertEquals(ERROR.source(), "objectos.lang.Event1Test");
  }

}