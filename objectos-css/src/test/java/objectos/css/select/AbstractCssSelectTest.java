/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.select;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

abstract class AbstractCssSelectTest {

  static final TypeSelector a = TypeSelectors.a;
  static final TypeSelector ul = TypeSelectors.ul;
  static final TypeSelector li = TypeSelectors.li;

  final void assertDoesNotMatch(Selector selector, Selectable element) {
    assertFalse(selector.matches(element));
  }

  final void assertMatches(Selector selector, Selectable element) {
    assertTrue(selector.matches(element));
  }

  final AttributeSelector attribute(String name) {
    return new AttributeSelector(name);
  }

  final AttributeValueSelector attribute(String name, AttributeValueElement element) {
    return new AttributeValueSelector(
        new AttributeSelector(name),
        element
    );
  }

  final ClassSelector cn(String name) {
    return new ClassSelector(name);
  }

  final IdSelector id(String name) {
    return new IdSelector(name);
  }

}