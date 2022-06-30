/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.util;

import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

final class SetAssert {

  private SetAssert() {}

  public static void iterator(Set<?> it, Object... expected) {
    var jdk = new HashSet<>();

    for (var o : expected) {
      if (o instanceof Thing t) {
        jdk.add(t);
      } else if (o instanceof Iterable<?> iter) {
        for (var t : iter) {
          jdk.add(t);
        }
      } else if (o instanceof Thing[] a) {
        for (var t : a) {
          jdk.add(t);
        }
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    for (Object e : it) {
      assertTrue(jdk.remove(e));
    }

    assertTrue(jdk.isEmpty());
  }

}