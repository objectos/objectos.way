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

public class Event2Test {

  final Note2<Void, Void> TRACE = Note2.trace();

  final Note2<Void, Void> DEBUG = Note2.debug();

  final Note2<Void, Void> INFO = Note2.info();

  final Note2<Void, Void> WARN = Note2.warn();

  final Note2<Void, Void> ERROR = Note2.error();

  @Test
  public void key() {
    assertEquals(TRACE.key(), "Event2Test.java:24");
    assertEquals(DEBUG.key(), "Event2Test.java:26");
    assertEquals(INFO.key(), "Event2Test.java:28");
    assertEquals(WARN.key(), "Event2Test.java:30");
    assertEquals(ERROR.key(), "Event2Test.java:32");
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
    assertEquals(TRACE.source(), "objectos.lang.Event2Test");
    assertEquals(DEBUG.source(), "objectos.lang.Event2Test");
    assertEquals(INFO.source(), "objectos.lang.Event2Test");
    assertEquals(WARN.source(), "objectos.lang.Event2Test");
    assertEquals(ERROR.source(), "objectos.lang.Event2Test");
  }

}