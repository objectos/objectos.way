/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class NoteSinkTest {

  static final Note0 DEBUG0 = Note0.debug();

  static final Note1<Arg1> DEBUG1 = Note1.debug();

  static final Note2<Arg1, Arg2> DEBUG2 = Note2.debug();

  static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> DEBUG3 = Note3.debug();

  static final Note0 ERROR0 = Note0.error();

  static final Note1<Arg1> ERROR1 = Note1.error();

  static final Note2<Arg1, Arg2> ERROR2 = Note2.error();

  static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> ERROR3 = Note3.error();

  static final Note0 INFO0 = Note0.info();

  static final Note1<Arg1> INFO1 = Note1.info();

  static final Note2<Arg1, Arg2> INFO2 = Note2.info();

  static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> INFO3 = Note3.info();

  static final Note0 TRACE0 = Note0.trace();

  static final Note1<Arg1> TRACE1 = Note1.trace();

  static final Note2<Arg1, Arg2> TRACE2 = Note2.trace();

  static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> TRACE3 = Note3.trace();

  static final Note0 WARN0 = Note0.warn();

  static final Note1<Arg1> WARN1 = Note1.warn();

  static final Note2<Arg1, Arg2> WARN2 = Note2.warn();

  static final Note3<Arg1, Arg2, Duo<Arg1, Arg2>> WARN3 = Note3.warn();

  @Test
  public void isEnabled() {
    assertTrue(INFO0.isEnabled(Level.TRACE));

    assertTrue(INFO0.isEnabled(Level.DEBUG));

    assertTrue(INFO0.isEnabled(Level.INFO));

    assertFalse(INFO0.isEnabled(Level.WARN));

    assertFalse(INFO0.isEnabled(Level.ERROR));
  }

  @Test
  public void log() {
    ThisLogger logger;
    logger = new ThisLogger();

    test0(logger, DEBUG0);

    test0(logger, ERROR0);

    test0(logger, INFO0);

    test0(logger, TRACE0);

    test0(logger, WARN0);

    Arg1 arg1;
    arg1 = new Arg1(111);

    test1(logger, DEBUG1, arg1);

    test1(logger, ERROR1, arg1);

    test1(logger, INFO1, arg1);

    test1(logger, TRACE1, arg1);

    test1(logger, WARN1, arg1);

    Arg2 arg2;
    arg2 = new Arg2(222);

    test2(logger, DEBUG2, arg1, arg2);

    test2(logger, ERROR2, arg1, arg2);

    test2(logger, INFO2, arg1, arg2);

    test2(logger, TRACE2, arg1, arg2);

    test2(logger, WARN2, arg1, arg2);

    Duo<Arg1, Arg2> arg3;
    arg3 = new Duo<Arg1, Arg2>(arg1, arg2);

    test3(logger, DEBUG3, arg1, arg2, arg3);

    test3(logger, ERROR3, arg1, arg2, arg3);

    test3(logger, INFO3, arg1, arg2, arg3);

    test3(logger, TRACE3, arg1, arg2, arg3);

    test3(logger, WARN3, arg1, arg2, arg3);
  }

  private void test0(ThisLogger logger, Note0 event) {
    logger.send(event);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertNull(logger.value1);
    assertNull(logger.value2);
    assertNull(logger.value3);
  }

  private <T1> void test1(ThisLogger logger, Note1<T1> event, T1 arg) {
    logger.send(event, arg);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertSame(logger.value1, arg);
    assertNull(logger.value2);
    assertNull(logger.value3);
  }

  private <T1, T2> void test2(ThisLogger logger, Note2<T1, T2> event, T1 arg1, T2 arg2) {
    logger.send(event, arg1, arg2);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertSame(logger.value1, arg1);
    assertSame(logger.value2, arg2);
    assertNull(logger.value3);
  }

  private <T1, T2, T3> void test3(
      ThisLogger logger, Note3<T1, T2, T3> event, T1 arg1, T2 arg2, T3 arg3) {
    logger.send(event, arg1, arg2, arg3);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertSame(logger.value1, arg1);
    assertSame(logger.value2, arg2);
    assertSame(logger.value3, arg3);
  }

  private static class ThisLogger implements NoteSink {

    Note event;

    Level level;

    Object value1;

    Object value2;

    Object value3;

    @Override
    public final boolean isEnabled(Note event) {
      return true;
    }

    @Override
    public final NoteSink replace(NoteSink logger) {
      return this;
    }

    @Override
    public final void send(Note0 event) {
      set(event);
    }

    @Override
    public final <T1> void send(Note1<T1> event, T1 v1) {
      set(event);

      value1 = v1;
    }

    @Override
    public final <T1, T2> void send(Note2<T1, T2> event, T1 v1, T2 v2) {
      set(event);

      value1 = v1;

      value2 = v2;
    }

    @Override
    public final <T1, T2, T3> void send(Note3<T1, T2, T3> event, T1 v1, T2 v2, T3 v3) {
      set(event);

      value1 = v1;

      value2 = v2;

      value3 = v3;
    }

    private void set(Note event) {
      this.event = event;

      this.level = event.level();

      value1 = null;

      value2 = null;

      value3 = null;
    }

  }

}