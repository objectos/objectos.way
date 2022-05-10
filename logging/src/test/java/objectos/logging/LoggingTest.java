/*
 * Copyright (C) 2021-2022 Objectos Software LTDA.
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
package objectos.logging;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class LoggingTest {

  static final Event0 DEBUG0 = Event0.debug();

  static final Event1<Arg1> DEBUG1 = Event1.debug();

  static final Event2<Arg1, Arg2> DEBUG2 = Event2.debug();

  static final Event3<Arg1, Arg2, Duo<Arg1, Arg2>> DEBUG3 = Event3.debug();

  static final Event0 ERROR0 = Event0.error();

  static final Event1<Arg1> ERROR1 = Event1.error();

  static final Event2<Arg1, Arg2> ERROR2 = Event2.error();

  static final Event3<Arg1, Arg2, Duo<Arg1, Arg2>> ERROR3 = Event3.error();

  static final Event0 INFO0 = Event0.info();

  static final Event1<Arg1> INFO1 = Event1.info();

  static final Event2<Arg1, Arg2> INFO2 = Event2.info();

  static final Event3<Arg1, Arg2, Duo<Arg1, Arg2>> INFO3 = Event3.info();

  static final Event0 TRACE0 = Event0.trace();

  static final Event1<Arg1> TRACE1 = Event1.trace();

  static final Event2<Arg1, Arg2> TRACE2 = Event2.trace();

  static final Event3<Arg1, Arg2, Duo<Arg1, Arg2>> TRACE3 = Event3.trace();

  static final Event0 WARN0 = Event0.warn();

  static final Event1<Arg1> WARN1 = Event1.warn();

  static final Event2<Arg1, Arg2> WARN2 = Event2.warn();

  static final Event3<Arg1, Arg2, Duo<Arg1, Arg2>> WARN3 = Event3.warn();

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

  private void test0(ThisLogger logger, Event0 event) {
    logger.log(event);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertNull(logger.value1);
    assertNull(logger.value2);
    assertNull(logger.value3);
  }

  private <T1> void test1(ThisLogger logger, Event1<T1> event, T1 arg) {
    logger.log(event, arg);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertSame(logger.value1, arg);
    assertNull(logger.value2);
    assertNull(logger.value3);
  }

  private <T1, T2> void test2(ThisLogger logger, Event2<T1, T2> event, T1 arg1, T2 arg2) {
    logger.log(event, arg1, arg2);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertSame(logger.value1, arg1);
    assertSame(logger.value2, arg2);
    assertNull(logger.value3);
  }

  private <T1, T2, T3> void test3(
      ThisLogger logger, Event3<T1, T2, T3> event, T1 arg1, T2 arg2, T3 arg3) {
    logger.log(event, arg1, arg2, arg3);

    assertEquals(logger.level, event.level());
    assertEquals(logger.event, event);
    assertSame(logger.value1, arg1);
    assertSame(logger.value2, arg2);
    assertSame(logger.value3, arg3);
  }

  private static class ThisLogger implements Logger {

    Event event;

    Level level;

    Object value1;

    Object value2;

    Object value3;

    @Override
    public final boolean isEnabled(Event event) {
      return true;
    }

    @Override
    public final void log(Event0 event) {
      set(event);
    }

    @Override
    public final <T1> void log(Event1<T1> event, T1 v1) {
      set(event);

      value1 = v1;
    }

    @Override
    public final <T1, T2> void log(Event2<T1, T2> event, T1 v1, T2 v2) {
      set(event);

      value1 = v1;

      value2 = v2;
    }

    @Override
    public final <T1, T2, T3> void log(Event3<T1, T2, T3> event, T1 v1, T2 v2, T3 v3) {
      set(event);

      value1 = v1;

      value2 = v2;

      value3 = v3;
    }

    @Override
    public final Logger replace(Logger logger) {
      return this;
    }

    private void set(Event event) {
      this.event = event;

      this.level = event.level();

      value1 = null;

      value2 = null;

      value3 = null;
    }

  }

}