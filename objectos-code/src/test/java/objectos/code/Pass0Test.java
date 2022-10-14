/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Pass0Test extends ObjectosCodeTest {

  private Pass0 pass0;

  @BeforeClass
  @Override
  public void _beforeClass() {
    if (pass0 == null) {
      pass0 = new Pass0();
    }
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Override
  final void test(
      JavaTemplate template,
      int[] p0,
      String expectedSource) {
    if (p0 == null) {
      return;
    }

    pass0.templateStart();

    template.pass0(pass0);

    pass0.templateEnd();

    int[] result = pass0.toCodes();

    testArrays(result, p0, "Process (0) assertion failed");
  }

}