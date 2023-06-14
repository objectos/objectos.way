/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.css;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class PreflightTest {

  @Test(enabled = false)
  public void test() {
    assertEquals(
      new Preflight().toString(),

      """

      """
    );
  }

}