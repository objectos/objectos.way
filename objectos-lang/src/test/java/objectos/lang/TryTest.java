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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TryTest {

  @Test
  public void close() {
    CloseableImpl impl;
    impl = null;

    IOException ioException;
    ioException = null;

    Throwable rethrow;
    rethrow = Try.begin();

    try {
      impl = new CloseableImpl();

      ioException = new IOException();

      impl.throwIoException(ioException);
    } catch (Throwable e) {
      rethrow = e;
    } finally {
      rethrow = Try.close(rethrow, impl);
    }

    try {
      Try.rethrowIfPossible(rethrow, IOException.class);

      Assert.fail();
    } catch (IOException expected) {
      assertSame(expected, ioException);

      Throwable[] suppressedArray;
      suppressedArray = expected.getSuppressed();

      assertEquals(suppressedArray.length, 0);
    }

    IOException suppressed;
    suppressed = null;

    try {
      suppressed = new IOException();

      impl = new CloseableImpl(suppressed);

      impl.throwIoException(ioException);
    } catch (IOException e) {
      rethrow = e;
    } finally {
      rethrow = Try.close(rethrow, impl);
    }

    try {
      Try.rethrowIfPossible(rethrow, IOException.class);

      Assert.fail();
    } catch (IOException expected) {
      assertSame(expected, ioException);

      Throwable[] suppressedArray;
      suppressedArray = expected.getSuppressed();

      assertEquals(suppressedArray.length, 1);

      assertSame(suppressedArray[0], suppressed);
    }
  }

  @Test
  public void rethrowIfPossible() {
    Throwable t;
    t = null;

    try {
      Try.rethrowIfPossible(t, IOException.class);
    } catch (Throwable e) {
      Assert.fail();
    }

    try {
      t = new IOException();

      Try.rethrowIfPossible(t, IOException.class);

      Assert.fail();
    } catch (Throwable expected) {
      assertSame(t, expected);
    }

    try {
      t = new RuntimeException();

      Try.rethrowIfPossible(t, IOException.class);

      Assert.fail();
    } catch (Throwable expected) {
      assertSame(t, expected);
    }

    try {
      t = new AssertionError();

      Try.rethrowIfPossible(t, IOException.class);

      Assert.fail();
    } catch (Throwable expected) {
      assertSame(t, expected);
    }

    try {
      t = new InterruptedException();

      Try.rethrowIfPossible(t, IOException.class);

      Assert.fail();
    } catch (Throwable expected) {
      assertTrue(expected instanceof RethrowException);

      RethrowException rethrow;
      rethrow = (RethrowException) expected;

      assertSame(t, rethrow.getCause());
    }
  }

}