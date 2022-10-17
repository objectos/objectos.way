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
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.AnnotationCode.annotation;
import static br.com.objectos.code.java.declaration.AnnotationCodeValuePair.value;
import static br.com.objectos.code.java.expression.Expressions.a;
import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class AnnotationCodeTest extends AbstractCodeJavaTest {

  private final NamedClass suit = PackageNameFake.TESTING_OTHER.nestedClass("Suit");
  private final NamedClass whatever = PackageNameFake.TESTING_CODE.nestedClass("Whatever");

  @Test(
      description = "Marker annotations should be rendered without the parenthesis"
  )
  public void markerAnnotation() {
    test(
        annotation(Override.class),
        "@java.lang.Override"
    );
    test(
        annotation(whatever),
        "@testing.code.Whatever"
    );
  }

  @Test(
      description = ""
          + "Normal annotations with a single element should always render "
          + "the element name, even if the name is 'value'"
  )
  public void normalAnnotationsWithSingleElement() {
    test(
        annotation(whatever,
            value("a", l(1))
        ),
        "@testing.code.Whatever(a = 1)"
    );
    test(
        annotation(whatever,
            value("suit", suit.id("HEARTS"))
        ),
        "@testing.code.Whatever(suit = testing.other.Suit.HEARTS)"
    );
    test(
        annotation(whatever,
            value("value", l("A"))
        ),
        "@testing.code.Whatever(value = \"A\")"
    );
  }

  @Test(
      description = ""
          + "annotation shorthand with more than one value "
          + "should be comma separated"
  )
  public void shorthandWithMoreThanOneValue() {
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2))
        ),
        "@testing.code.Whatever(a = 1, b = 2)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4)),
            value("e", l(5))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4, e = 5)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4)),
            value("e", l(5)),
            value("f", l(6))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4, e = 5, f = 6)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4)),
            value("e", l(5)),
            value("f", l(6)),
            value("g", l(7))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4)),
            value("e", l(5)),
            value("f", l(6)),
            value("g", l(7)),
            value("h", l(8))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4)),
            value("e", l(5)),
            value("f", l(6)),
            value("g", l(7)),
            value("h", l(8)),
            value("i", l(9))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9)"
    );
    test(
        annotation(whatever,
            value("a", l(1)),
            value("b", l(2)),
            value("c", l(3)),
            value("d", l(4)),
            value("e", l(5)),
            value("f", l(6)),
            value("g", l(7)),
            value("h", l(8)),
            value("i", l(9)),
            value("j", l(10))
        ),
        "@testing.code.Whatever(a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10)"
    );
  }

  @Test(
      description = "Single element annotations should be rendered without the 'value' name"
  )
  public void singleElementAnnotation() {
    test(
        annotation(whatever, l(String.class)),
        "@testing.code.Whatever(java.lang.String.class)"
    );
    test(
        annotation(whatever, suit.id("CLUBS")),
        "@testing.code.Whatever(testing.other.Suit.CLUBS)"
    );
    test(
        annotation(whatever, a(l("a"), l("b"), l("c"))),
        "@testing.code.Whatever({\"a\", \"b\", \"c\"})"
    );
  }

  private void test(AnnotationCode code, String... lines) {
    testToString(code, lines);
  }

}