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
package objectox.html.format;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class IndentationTest {

  @Test(description = "initial level is 0")
  public void toString01() {
    final Indentation subject;
    subject = new Indentation("  ");

    assertEquals(subject.toString(), "");
  }

  @Test(description = "inc() increases level to 1")
  public void inc01() {
    final Indentation subject;
    subject = new Indentation("  ");

    subject.inc();

    assertEquals(subject.toString(), "  ");
  }

  @Test(description = "multiple inc() calls increase level")
  public void inc02() {
    final Indentation subject;
    subject = new Indentation("  ");

    subject.inc();
    subject.inc();

    assertEquals(subject.toString(), "    ");
  }

  @Test(description = "dec() decreases level")
  public void dec01() {
    final Indentation subject;
    subject = new Indentation("  ");

    subject.inc();
    subject.inc();
    subject.dec();

    assertEquals(subject.toString(), "  ");
  }

  @Test(
      description = "dec() throws when level is 0",
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "Cannot decrement indentation level below zero"
  )
  public void dec02() {
    final Indentation subject;
    subject = new Indentation("  ");

    subject.dec();
  }

}