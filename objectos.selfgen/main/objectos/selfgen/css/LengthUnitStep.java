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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

final class LengthUnitStep extends ThisTemplate {

  private LengthType lengthType;

  public LengthUnitStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  public void writeTo(Path directory) throws IOException {
    lengthType = spec.lengthType;

    if (lengthType != null) {
      super.writeTo(directory);
    }
  }

  @Override
  final String contents() {
    className(LENGTH_UNIT);

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
    public enum \{simpleName} {
    \{lengthUnits()}

      private static final \{simpleName}[] VALUES = values();

      public final String cssName;

      private \{simpleName}(String cssName) {
        this.cssName = cssName;
      }

      public static \{simpleName} byOrdinal(int ordinal) {
        return VALUES[ordinal];
      }

      @Override
      public final String toString() {
        return cssName;
      }
    }
    """;
  }

  private String lengthUnits() {
    List<String> result;
    result = new ArrayList<>();

    Collection<String> unsorted;
    unsorted = lengthType.units;

    ArrayList<String> sorted;
    sorted = new ArrayList<>(unsorted);

    sorted.sort(null);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      String unit;
      unit = sorted.get(i);

      String constantName;
      constantName = unit.toUpperCase(Locale.US);

      result.add(
        code."  \{constantName}(\"\{unit}\")"
      );
    }

    return result.stream().collect(Collectors.joining(",\n\n", "", ";"));
  }

}
