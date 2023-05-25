/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css;

import objectos.selfgen.css.spec.KeywordName;
import objectos.selfgen.css.spec.ValueType;

abstract class AbstractAlignOrJustifyPropertyModule extends AbstractPropertyModule {

  KeywordName auto;
  KeywordName baseline;
  KeywordName center;
  KeywordName end;
  KeywordName flexEnd;
  KeywordName flexStart;
  KeywordName left;
  KeywordName normal;
  KeywordName right;
  KeywordName spaceAround;
  KeywordName spaceBetween;
  KeywordName spaceEvenly;
  KeywordName start;
  KeywordName stretch;

  ValueType baselinePosition;
  ValueType contentDistribution;
  ValueType contentPosition;
  ValueType overflowPosition;
  ValueType selfPosition;
  ValueType selfPositionOrLeftOrRight;

  @Override
  final void propertyDefinition() {
    auto = keyword("auto");
    baseline = keyword("baseline");
    center = keyword("center");
    end = keyword("end");
    KeywordName first = keyword("first");
    flexEnd = keyword("flex-end");
    flexStart = keyword("flex-start");
    KeywordName last = keyword("last");
    left = keyword("left");
    normal = keyword("normal");
    KeywordName safe = keyword("safe");
    KeywordName selfEnd = keyword("self-end");
    KeywordName selfStart = keyword("self-start");
    right = keyword("right");
    spaceAround = keyword("space-around");
    spaceBetween = keyword("space-between");
    spaceEvenly = keyword("space-evenly");
    start = keyword("start");
    stretch = keyword("stretch");
    KeywordName unsafe = keyword("unsafe");

    baselinePosition = t(
        "BaselinePosition",
        first, last
    );

    contentDistribution = t(
        "ContentDistribution",
        spaceBetween, spaceAround, spaceEvenly, stretch
    );
    
    contentPosition = t(
        "ContentPosition",
        center, start, end, flexStart, flexEnd
    );

    overflowPosition = t(
        "OverflowPosition",
        unsafe, safe
    );

    selfPosition = t(
        "SelfPosition",
        center, start, end, selfStart, selfEnd, flexStart, flexEnd
    );

    selfPositionOrLeftOrRight = t(
        "SelfPositionOrLeftOrRight",
        selfPosition, left, right
    );

    propertyDefinitionImpl();
  }

  abstract void propertyDefinitionImpl();

}
