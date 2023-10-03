/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the path of a HTTP request target split into segments.
 */
public sealed interface Segments {

  record Segments1(String v1) implements Segments {
    @Override
    public final Segments append(String value) {
      return new Segments2(v1, value);
    }
  }

  record Segments2(String v1, String v2) implements Segments {
    @Override
    public final Segments append(String value) {
      return new Segments3(v1, v2, value);
    }
  }

  record Segments3(String v1, String v2, String v3) implements Segments {
    @Override
    public final Segments append(String value) {
      List<String> values;
      values = List.of(v1, v2, v3, value);

      return new SegmentsN(values);
    }
  }

  record SegmentsN(List<String> values) implements Segments {
    @Override
    public Segments append(String value) {
      List<String> builder;
      builder = new ArrayList<>(values);

      builder.add(value);

      List<String> values;
      values = List.copyOf(builder);

      return new SegmentsN(values);
    }
  }

  Segments append(String value);

}