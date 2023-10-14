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
package selfgen.css.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import objectos.code.Code;
import objectos.lang.Check;
import selfgen.css.util.Prefix.Breakpoint;
import selfgen.css.util.Prefix.Simple;

public abstract class CssUtilSelfGen {

  final Code code = Code.of();

  final List<Prefix> prefixList = new ArrayList<>();

  final List<PropertyClass> properties = new ArrayList<>();

  protected CssUtilSelfGen() {}

  public final void execute(String[] args) throws IOException {
    String srcDir;
    srcDir = args[0];

    Path directory;
    directory = Path.of(srcDir);

    compile();

    writeTo(new PropertyEnumStep(this), directory);

    //writeTo(new FrameworkClassStep(this), directory);

    //writeTo(new PrefixClassStep(this), directory);
  }

  protected final void add(PropertyClass p) {
    Check.notNull(p, "p == null");

    properties.add(p);
  }

  protected final Breakpoint breakpoint(String name, int length) {
    Breakpoint breakpoint;
    breakpoint = Prefix.ofBreakpoint(name, length);

    prefixList.add(breakpoint);

    return breakpoint;
  }

  protected final Value ch(int value) {
    return new Value.MethodInt("ch", value);
  }

  protected abstract void definition();

  protected final void generate(
      Prefix prefix, SimpleName simpleName, Methods methods, Names names) {
    //generate(SelectorKind.STANDARD, prefix, simpleName, methods, names);
  }

  protected final void generateAllButFirst(
      Prefix prefix, SimpleName simpleName, Methods methods, Names names) {
    //generate(SelectorKind.ALL_BUT_FIRST, prefix, simpleName, methods, names);
  }

  protected final void generateHover(
      Prefix prefix, SimpleName simpleName, Methods methods, Names names) {
    //generate(SelectorKind.HOVER, prefix, simpleName, methods, names);
  }

  protected final Value hex(String value) {
    return new Value.MethodString("hex", value);
  }

  protected final Value k(String fieldName) {
    Check.notNull(fieldName, "fieldName == null");

    return new Value.ExpressionName(fieldName);
  }

  protected final Value l(double value) {
    return new Value.LiteralDouble(value);
  }

  protected final Value l(int value) {
    return new Value.LiteralInt(value);
  }

  protected final Methods methods(String... names) {
    List<String> list;
    list = new ArrayList<String>();

    for (int i = 0; i < names.length; i++) {
      String name;
      name = Check.notNull(names[i], "names[", i, "] == null");

      list.add(name);
    }

    return new Methods(List.copyOf(list));
  }

  protected final NamedArguments name(String constantName, Value... values) {
    Check.notNull(constantName, "constantName == null");

    List<Value> list;
    list = new ArrayList<Value>();

    for (int i = 0; i < values.length; i++) {
      Value value;
      value = Check.notNull(values[i], "values[", i, "] == null");

      list.add(value);
    }

    return new NamedArguments(constantName, List.copyOf(list));
  }

  protected final Names names(NamesValue... elements) {
    List<NamedArguments> list;
    list = new ArrayList<NamedArguments>();

    for (int i = 0; i < elements.length; i++) {
      NamesValue element;
      element = Check.notNull(elements[i], "elements[", i, "] == null");

      element.acceptList(list);
    }

    return new Names(List.copyOf(list));
  }

  protected final Value pct(double value) {
    return new Value.MethodDouble("pct", value);
  }

  protected final Value pct(int value) {
    return new Value.MethodInt("pct", value);
  }

  protected final Simple prefix(String name) {
    Simple simple;
    simple = Prefix.ofSimple(name);

    prefixList.add(simple);

    return simple;
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

  private void writeTo(ThisTemplate template, Path directory) throws IOException {
    template.writeTo(directory);
  }

}
