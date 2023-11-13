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
package objectos.notes;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Note0Test {

  private Note0 TRACE;

  private Note0 DEBUG;

  private Note0 INFO;

  private Note0 WARN;

  private Note0 ERROR;

  @BeforeClass
  public void beforeClass() {
    Class<?> s;
    s = getClass();

    TRACE = Note0.trace(s, "TRACE");

    DEBUG = Note0.debug(s, "DEBUG");

    INFO = Note0.info(s, "INFO");

    WARN = Note0.warn(s, "WARN");

    ERROR = Note0.error(s, "ERROR");
  }

  @Test
  public void key() {
    assertEquals(TRACE.key(), "TRACE");
    assertEquals(DEBUG.key(), "DEBUG");
    assertEquals(INFO.key(), "INFO");
    assertEquals(WARN.key(), "WARN");
    assertEquals(ERROR.key(), "ERROR");
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
    Class<?> s;
    s = getClass();

    String name;
    name = s.getCanonicalName();

    assertEquals(TRACE.source(), name);
    assertEquals(DEBUG.source(), name);
    assertEquals(INFO.source(), name);
    assertEquals(WARN.source(), name);
    assertEquals(ERROR.source(), name);
  }

}