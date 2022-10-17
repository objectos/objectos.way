/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import org.testng.annotations.Test;

public class CalleeTest extends AbstractCodeJavaTest {

  public interface Adapter {

    Callee callee();

  }

  private final CalleeTest.Adapter adapter;

  private CalleeTest(CalleeTest.Adapter adapter) {
    this.adapter = adapter;
  }

  public static CalleeTest with(Adapter adapter) {
    return new CalleeTest(adapter);
  }

  @Test
  public void invoke() {
    test(callee().invoke("m0"),
        callee(
            ".m0()"
        )
    );
    test(callee().invoke("m1", l(1)),
        callee(
            ".m1(1)"
        )
    );
    test(callee().invoke("m2", l(1), l(2)),
        callee(
            ".m2(1, 2)"
        )
    );
    test(callee().invoke("m3", l(1), l(2), l(3)),
        callee(
            ".m3(1, 2, 3)"
        )
    );
    test(callee().invoke("m4", l(1), l(2), l(3), l(4)),
        callee(
            ".m4(1, 2, 3, 4)"
        )
    );
    test(callee().invoke("m5", l(1), l(2), l(3), l(4), l(5)),
        callee(
            ".m5(1, 2, 3, 4, 5)"
        )
    );
    test(callee().invoke("m6", l(1), l(2), l(3), l(4), l(5), l(6)),
        callee(
            ".m6(1, 2, 3, 4, 5, 6)"
        )
    );
    test(callee().invoke("m7", l(1), l(2), l(3), l(4), l(5), l(6), l(7)),
        callee(
            ".m7(1, 2, 3, 4, 5, 6, 7)"
        )
    );
    test(callee().invoke("m8", l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8)),
        callee(
            ".m8(1, 2, 3, 4, 5, 6, 7, 8)"
        )
    );
    test(callee().invoke("m9", l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9)),
        callee(
            ".m9(1, 2, 3, 4, 5, 6, 7, 8, 9)"
        )
    );
    test(callee().invoke("m10", l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9), l(10)),
        callee(
            ".m10(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)"
        )
    );
    test(callee().invoke("mm", Arrays.asList(l(1), l(2), l(3), l(4), l(5))),
        callee(
            ".mm(1, 2, 3, 4, 5)"
        )
    );
  }

  @Test
  public void invokeChain() {
    test(callee().invoke("m0").invoke("m1", l(1)).invoke("m2", l(1), l(2)),
        callee(
            ".m0().m1(1).m2(1, 2)"
        ));
  }

  @Test
  public void invokeWithTypeWitness() {
    test(callee().invoke(hint(tvar("E1")), "m0"),
        callee(
            ".<E1> m0()"
        )
    );
    test(callee().invoke(hint(tvar("E1")), "m1", l(1)),
        callee(
            ".<E1> m1(1)"
        )
    );
    test(callee().invoke(hint(tvar("E1")), "m2", l(1), l(2)),
        callee(
            ".<E1> m2(1, 2)"
        )
    );
    test(callee().invoke(hint(tvar("E1")), "m3", l(1), l(2), l(3)),
        callee(
            ".<E1> m3(1, 2, 3)"
        )
    );
    test(callee().invoke(hint(tvar("E1")), "m4", l(1), l(2), l(3), l(4)),
        callee(
            ".<E1> m4(1, 2, 3, 4)"
        )
    );
    test(callee().invoke(hint(tvar("E1")), "mm", Arrays.asList(l(1), l(2), l(3), l(4), l(5))),
        callee(
            ".<E1> mm(1, 2, 3, 4, 5)"
        )
    );
  }

  private Callee callee() {
    return adapter.callee();
  }

  private String callee(String trailer) {
    return adapter.callee() + trailer;
  }

}
