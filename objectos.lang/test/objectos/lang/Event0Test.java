/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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

public class Event0Test {

  final Note0 TRACE = Note0.trace();

  final Note0 DEBUG = Note0.debug();

  final Note0 INFO = Note0.info();

  final Note0 WARN = Note0.warn();

  final Note0 ERROR = Note0.error();

  @Test
  public void key() {
    assertEquals(TRACE.key(), "Event0Test.java:24");
    assertEquals(DEBUG.key(), "Event0Test.java:26");
    assertEquals(INFO.key(), "Event0Test.java:28");
    assertEquals(WARN.key(), "Event0Test.java:30");
    assertEquals(ERROR.key(), "Event0Test.java:32");
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
    assertEquals(TRACE.source(), "objectos.lang.Event0Test");
    assertEquals(DEBUG.source(), "objectos.lang.Event0Test");
    assertEquals(INFO.source(), "objectos.lang.Event0Test");
    assertEquals(WARN.source(), "objectos.lang.Event0Test");
    assertEquals(ERROR.source(), "objectos.lang.Event0Test");
  }

}