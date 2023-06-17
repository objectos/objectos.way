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
package objectos.selfgen.css2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import objectos.code.ClassTypeName;
import objectos.code.JavaSink;
import objectos.util.UnmodifiableList;

public abstract class CssSelfGen extends CompiledSpec {

  private final Map<String, KeywordName> keywords = new TreeMap<>();

  private final Map<String, SelectorName> selectors = new TreeMap<>();

  private final Map<String, ValueType> valueTypes = new TreeMap<>();

  public final void execute(String[] args) throws IOException {
    var srcDir = args[0];

    var directory = Path.of(srcDir);

    var sink = JavaSink.ofDirectory(
      directory,
      JavaSink.overwriteExisting()
    );

    var spec = compile();

    spec.write(sink, new GeneratedCssTemplateStep());
  }

  protected final CompiledSpec compile() {
    keywords.clear();

    selectors.clear();

    valueTypes.clear();

    definition();

    return this;
  }

  protected final ValueType def(String simpleName, Value... values) {
    Objects.requireNonNull(simpleName, "simpleName == null");

    if (valueTypes.containsKey(simpleName)) {
      throw new IllegalArgumentException(
        """
        Duplicate ValueType name '%s'
        """.formatted(simpleName)
      );
    }

    var className = ClassTypeName.of(ThisTemplate.CSS_TMPL, simpleName);

    for (var value : values) {
      value.addInterface(className);
    }

    return new ValueType(className, UnmodifiableList.copyOf(values));
  }

  protected abstract void definition();

  protected final KeywordName kw(String name) {
    var keyword = KeywordName.of(name);

    var key = keyword.fieldName;

    var existing = keywords.get(key);

    if (existing == null) {
      keywords.put(key, keyword);

      return keyword;
    }

    if (existing.keywordName.equals(name)) {
      return existing;
    }

    throw new IllegalArgumentException(
      """
      Duplicate keyword field name
      keyword=%s
      existing=%s
      """.formatted(name, existing.keywordName)
    );
  }

  protected final void prop(String propertyName, ParameterType value) {
  }

  protected final void selectors(String... names) {
    for (var name : names) {
      selector(name);
    }
  }

  @Override
  final Iterable<KeywordName> keywords() {
    return keywords.values();
  }

  @Override
  final Iterable<SelectorName> selectors() {
    return selectors.values();
  }

  private void selector(String name) {
    var selector = SelectorName.of(name);

    var key = selector.fieldName();

    if (selectors.containsKey(key)) {
      throw new IllegalArgumentException(
        """
        Duplicate selector field name
        fieldName=%s
        selectorName=%s
        """.formatted(key, name)
      );
    }

    selectors.put(key, selector);
  }

}
