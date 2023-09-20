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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;

final class GeneratedColorStep extends ThisTemplate {

  public GeneratedColorStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(
      ClassName.of(CSS_UTIL, "GeneratedColor")
    );

    return code."""
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
    package \{packageName};
    \{importList}
    \{GENERATED_MSG}
    abstract class \{simpleName} {
    \{colors()}
    }
    """;
  }

  private String colors() {
    List<String> result;
    result = new ArrayList<>();

    ColorValue colorValue;
    colorValue = spec.colorValue;

    Collection<ColorName> unsorted;
    unsorted = colorValue.names;

    ArrayList<ColorName> sorted;
    sorted = new ArrayList<>(unsorted);

    Comparator<ColorName> byConstantName;
    byConstantName = (self, that) -> (self.constantName().compareTo(that.constantName()));

    sorted.sort(byConstantName);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      ColorName color;
      color = sorted.get(i);

      String constantName;
      constantName = color.constantName();

      String colorName;
      colorName = color.colorName();

      result.add(
        code."  public static final \{COLOR} \{constantName} = \{COLOR}.named(\"\{colorName}\");"
      );
    }

    return result.stream().collect(Collectors.joining("\n\n"));
  }

}
