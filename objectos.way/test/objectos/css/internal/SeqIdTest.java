/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.css.internal;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class SeqIdTest {

  @Test
  public void next() {
    SeqId id = new SeqId(0);
    assertEquals(id.next(), "aaaa");
    assertEquals(id.next(), "aaab");
    assertEquals(id.next(), "aaac");
  }

}
