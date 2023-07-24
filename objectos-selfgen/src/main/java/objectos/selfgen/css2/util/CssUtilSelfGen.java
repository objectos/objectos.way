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
package objectos.selfgen.css2.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import objectos.code.JavaSink;
import objectos.lang.Check;
import objectos.selfgen.css2.util.Prefix.Breakpoint;
import objectos.util.GrowableList;

public abstract class CssUtilSelfGen {

  final Map<Prefix, List<Property>> properties = new LinkedHashMap<>();

  protected CssUtilSelfGen() {}

  public final void execute(String[] args) throws IOException {
    String srcDir;
    srcDir = args[0];

    Path directory;
    directory = Path.of(srcDir);

    JavaSink sink;
    sink = JavaSink.ofDirectory(
      directory,
      JavaSink.overwriteExisting()
    );

    compile();

    write(sink, new FrameworkClassStep());

    write(sink, new PrefixClassStep());
  }

  protected final Breakpoint breakpoint(String name, int length) {
    return Prefix.ofBreakpoint(name, length);
  }

  protected final Value ch(int value) {
    return new Value.MethodInt("ch", value);
  }

  protected abstract void definition();

  protected final void generate(
      Prefix prefix, SimpleName simpleName, Methods methods, Names names) {
    List<Property> list;
    list = properties.computeIfAbsent(prefix, k -> new GrowableList<>());

    Property property;
    property = Property.of(prefix, simpleName, methods, names);

    list.add(property);
  }

  protected final Value k(String fieldName) {
    Check.notNull(fieldName, "fieldName == null");

    return new Value.ExpressionName(fieldName);
  }

  protected final Methods methods(String... names) {
    GrowableList<String> list;
    list = new GrowableList<String>();

    for (int i = 0; i < names.length; i++) {
      list.addWithNullMessage(names[i], "names[", i, "] == null");
    }

    return new Methods(list.toUnmodifiableList());
  }

  protected final NamedArguments name(String constantName, Value... values) {
    Check.notNull(constantName, "constantName == null");

    GrowableList<Value> list;
    list = new GrowableList<Value>();

    for (int i = 0; i < values.length; i++) {
      list.addWithNullMessage(values[i], "values[", i, "] == null");
    }

    return new NamedArguments(constantName, list.toUnmodifiableList());
  }

  protected final Names names(NamesValue... elements) {
    GrowableList<NamedArguments> list;
    list = new GrowableList<NamedArguments>();

    for (int i = 0; i < elements.length; i++) {
      NamesValue element;
      element = Check.notNull(elements[i], "elements[", i, "] == null");

      element.acceptList(list);
    }

    return new Names(list.toUnmodifiableList());
  }

  protected final Value pct(double value) {
    return new Value.MethodDouble("pct", value);
  }

  protected final Value pct(int value) {
    return new Value.MethodInt("pct", value);
  }

  protected final Value px(int value) {
    return new Value.MethodInt("px", value);
  }

  protected final Value rem(double value) {
    return new Value.MethodDouble("rem", value);
  }

  protected final Value rem(int value) {
    return new Value.MethodInt("rem", value);
  }

  protected final SimpleName simpleName(String name) {
    Check.notNull(name, "name == null");

    return new SimpleName(name);
  }

  protected final Value vh(int value) {
    return new Value.MethodInt("vh", value);
  }

  protected final Value vw(int value) {
    return new Value.MethodInt("vw", value);
  }

  protected final Value zero() {
    return new Value.ExpressionName("$0");
  }

  private void compile() {
    definition();
  }

  private void write(JavaSink sink, ThisTemplate step) throws IOException {
    step.write(sink, this);
  }

}
