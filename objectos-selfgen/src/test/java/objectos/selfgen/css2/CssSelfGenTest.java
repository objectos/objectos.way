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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

public class CssSelfGenTest {

  @Test
  public void propertyBorderColor() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        // global keywords
        var globalKeyword = def("GlobalKeyword",
          kw("inherit"), kw("initial"), kw("unset")
        );

        // color
        var color = def(
          "Color",
          kw("currentcolor"), kw("transparent")
        );

        // B
        pval("border-color", globalKeyword);
        pbox("border-color", color);
      }
    };

    var result = generate(spec);

    assertEquals(result.size(), 5);

    assertEquals(
      result.get("objectos/css/GeneratedCssTemplate.java"),
      """
      package objectos.css;

      import objectos.css.internal.NamedElement;
      import objectos.css.internal.Property;
      import objectos.css.internal.StyleDeclaration1;
      import objectos.css.internal.StyleDeclaration2;
      import objectos.css.internal.StyleDeclaration3;
      import objectos.css.internal.StyleDeclaration4;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.Color;
      import objectos.css.tmpl.GlobalKeyword;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final Color currentcolor = named("currentcolor");

        protected static final GlobalKeyword inherit = named("inherit");

        protected static final GlobalKeyword initial = named("initial");

        protected static final Color transparent = named("transparent");

        protected static final GlobalKeyword unset = named("unset");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final StyleDeclaration borderColor(GlobalKeyword value) {
          return new StyleDeclaration1(Property.BORDER_COLOR, value.self());
        }

        protected final StyleDeclaration borderColor(Color all) {
          return new StyleDeclaration1(Property.BORDER_COLOR, all.self());
        }

        protected final StyleDeclaration borderColor(Color vertical, Color horizontal) {
          return new StyleDeclaration2(Property.BORDER_COLOR, vertical.self(), horizontal.self());
        }

        protected final StyleDeclaration borderColor(Color top, Color horizontal, Color bottom) {
          return new StyleDeclaration3(Property.BORDER_COLOR, top.self(), horizontal.self(), bottom.self());
        }

        protected final StyleDeclaration borderColor(Color top, Color right, Color bottom, Color left) {
          return new StyleDeclaration4(Property.BORDER_COLOR, top.self(), right.self(), bottom.self(), left.self());
        }
      }
      """
    );

    assertEquals(
      result.get("objectos/css/internal/NamedElement.java"),
      """
      package objectos.css.internal;

      import objectos.css.om.Selector;
      import objectos.css.tmpl.Color;
      import objectos.css.tmpl.GlobalKeyword;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class NamedElement implements Selector,
          Color,
          GlobalKeyword {
        private final String name;

        public NamedElement(String name) {
          this.name = name;
        }

        @Override
        public final String toString() {
          return name;
        }
      }
      """
    );

    assertEquals(
      result.get("objectos/css/internal/Property.java"),
      """
      package objectos.css.internal;

      import objectos.css.om.PropertyName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum Property implements PropertyName {
        BORDER_COLOR("border-color");

        private final String propertyName;

        private Property(String propertyName) {
          this.propertyName = propertyName;
        }

        @Override
        public final String toString() {
          return propertyName;
        }
      }
      """
    );

    assertEquals(
      result.get("objectos/css/tmpl/Color.java"),
      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface Color extends PropertyValue permits NamedElement {}
      """
    );

    assertEquals(
      result.get("objectos/css/tmpl/GlobalKeyword.java"),
      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface GlobalKeyword extends PropertyValue permits NamedElement {}
      """
    );
  }

  @Test
  public void selectors01() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        selectors(
          // type selectors
          "a",
          "pre",

          // pseudo elements
          "::after", "::before"
        );
      }
    };

    var result = generate(spec);

    assertEquals(result.size(), 3);

    assertEquals(
      result.get("objectos/css/GeneratedCssTemplate.java"),

      """
      package objectos.css;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.Selector;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector __after = named("::after");

        protected static final Selector __before = named("::before");

        protected static final Selector a = named("a");

        protected static final Selector pre = named("pre");

        protected static final Selector any = named("*");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }
      }
      """
    );

    assertEquals(
      result.get("objectos/css/internal/Property.java"),
      """
      package objectos.css.internal;

      import objectos.css.om.PropertyName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum Property implements PropertyName {
        private final String propertyName;

        private Property(String propertyName) {
          this.propertyName = propertyName;
        }

        @Override
        public final String toString() {
          return propertyName;
        }
      }
      """
    );

    assertEquals(
      result.get("objectos/css/internal/NamedElement.java"),
      """
      package objectos.css.internal;

      import objectos.css.om.Selector;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class NamedElement implements Selector {
        private final String name;

        public NamedElement(String name) {
          this.name = name;
        }

        @Override
        public final String toString() {
          return name;
        }
      }
      """
    );
  }

  private Map<String, String> generate(CssSelfGen gen) throws IOException {
    var root = Files.createTempDirectory("objectos-selfgen-css-");

    try {
      gen.execute(new String[] {
          root.toString()
      });

      var result = new HashMap<String, String>();

      try (var walk = Files.walk(root)) {
        walk.filter(Files::isRegularFile)
            .forEach(file -> {
              try {
                var relative = root.relativize(file);

                var key = relative.toString();

                var value = Files.readString(file);

                result.put(key, value);
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            });
      }

      return result;
    } finally {
      rmdir(root);
    }
  }

  private void rmdir(Path root) throws IOException {
    var rm = new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
    };

    Files.walkFileTree(root, rm);
  }

}