/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.util;

@FunctionalInterface
interface AssertContents {
  void execute(Object... expected);
}