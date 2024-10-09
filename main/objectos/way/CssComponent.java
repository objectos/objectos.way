/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.TreeMap;
import objectos.way.Css.ClassNameFormat;
import objectos.way.Css.Context;
import objectos.way.Css.Indentation;
import objectos.way.Css.MediaQuery;
import objectos.way.Css.Repository;
import objectos.way.Css.Rule;

/**
 * A CSS component class.
 */
final class CssComponent implements Repository, Rule {

  private abstract class ThisContext extends Context {

    Map<List<Css.ClassNameFormat>, ClassNameContext> classNameFormats;

    Map<Css.MediaQuery, MediaQueryContext> mediaQueries;

    @Override
    public final void addComponent(CssComponent component) {
      for (Rule rule : component.rules.values()) {
        rule.accept(this);
      }
    }

    @Override
    public final Css.Context contextOf(Css.Modifier modifier) {
      ThisContext context;
      context = this;

      List<Css.MediaQuery> modifierQueries;
      modifierQueries = modifier.mediaQueries();

      if (!modifierQueries.isEmpty()) {
        if (mediaQueries == null) {
          mediaQueries = new TreeMap<>();
        }

        Iterator<Css.MediaQuery> iterator;
        iterator = modifierQueries.iterator();

        Css.MediaQuery first;
        first = iterator.next(); // safe as list is not empty

        MediaQueryContext queryContext;
        queryContext = mediaQueries.computeIfAbsent(first, MediaQueryContext::new);

        while (iterator.hasNext()) {
          queryContext = queryContext.nest(iterator.next());
        }

        context = queryContext;
      }

      List<Css.ClassNameFormat> formats;
      formats = modifier.classNameFormats();

      if (!formats.isEmpty()) {
        context = context.nest(formats);
      }

      return context;
    }

    final ThisContext nest(List<ClassNameFormat> formats) {
      if (classNameFormats == null) {
        classNameFormats = new LinkedHashMap<>(); // no sorting
      }

      return classNameFormats.computeIfAbsent(formats, ClassNameContext::new);
    }

    void writeClassName(StringBuilder out) {
      Css.writeClassName(out, className);
    }

    final void writeContents(StringBuilder out, Indentation indentation) {
      if (!rules.isEmpty()) {
        indentation.writeTo(out);

        writeClassName(out);
        out.append(' ');
        out.append('{');
        out.append(System.lineSeparator());

        Indentation blockIndentation;
        blockIndentation = indentation.increase();

        for (Rule rule : rules) {
          rule.writeProps(out, blockIndentation);
        }

        indentation.writeTo(out);

        out.append('}');

        out.append(System.lineSeparator());
      }

      if (classNameFormats != null) {
        for (Context child : classNameFormats.values()) {
          child.writeTo(out, indentation);
        }
      }

      if (mediaQueries != null) {
        for (Context child : mediaQueries.values()) {
          child.writeTo(out, indentation);
        }
      }
    }

  }

  private final class TopLevel extends ThisContext {

    @Override
    final void write(StringBuilder out, Indentation indentation) {
      if (!out.isEmpty()) {
        out.append(System.lineSeparator());
      }

      writeContents(out, indentation);
    }

  }

  private final class ClassNameContext extends ThisContext {

    private final List<Css.ClassNameFormat> formats;

    ClassNameContext(List<ClassNameFormat> formats) {
      this.formats = formats;
    }

    @Override
    final void write(StringBuilder out, Indentation indentation) {
      writeContents(out, indentation);
    }

    @Override
    final void writeClassName(StringBuilder out) {
      int startIndex;
      startIndex = out.length();

      Css.writeClassName(out, className);

      for (var format : formats) {
        format.writeClassName(out, startIndex);
      }
    }

  }

  private final class MediaQueryContext extends ThisContext {

    private final MediaQuery query;

    MediaQueryContext(MediaQuery query) {
      this.query = query;
    }

    public final MediaQueryContext nest(MediaQuery next) {
      return mediaQueries.computeIfAbsent(next, MediaQueryContext::new);
    }

    @Override
    final void write(StringBuilder out, Indentation indentation) {
      query.writeMediaQueryStart(out, indentation);

      Indentation blockIndentation;
      blockIndentation = indentation.increase();

      writeContents(out, blockIndentation);

      indentation.writeTo(out);

      out.append('}');

      out.append(System.lineSeparator());
    }

  }

  private final String className;

  private final SequencedMap<String, Rule> rules = Util.createSequencedMap();

  CssComponent(String className) {
    this.className = className;
  }

  @Override
  public final void accept(Css.Context gen) {
    gen.addComponent(this);
  }

  @Override
  public final int compareSameKind(Rule o) {
    CssComponent that;
    that = (CssComponent) o;

    return className.compareTo(that.className);
  }

  @Override
  public final int kind() {
    return 1;
  }

  @Override
  public final void writeTo(StringBuilder out, Indentation indentation) {
    Css.Context topLevel;
    topLevel = new TopLevel();

    for (Rule rule : rules.values()) {
      rule.accept(topLevel);
    }

    topLevel.writeTo(out, indentation);
  }

  @Override
  public final void writeProps(StringBuilder out, Indentation indentation) {
    for (var rule : rules.values()) {
      rule.writeProps(out, indentation);
    }
  }

  @Override
  public final void cycleCheck(String other) {
    if (className.equals(other)) {
      throw new IllegalArgumentException("Cycle detected @ component: " + className);
    }
  }

  @Override
  public final void consumeRule(String className, Rule existing) {
    rules.put(className, existing);
  }

  @Override
  public final void putRule(String className, Rule rule) {
    rules.put(className, rule);
  }

}