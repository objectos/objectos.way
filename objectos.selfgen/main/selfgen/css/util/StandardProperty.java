/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package selfgen.css.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.Code;
import objectos.lang.Check;

public final class StandardProperty extends PropertyClass {

  static record Constant(String name, String value) {}

  final String propertyName;

  final List<Constant> constants = new ArrayList<>();

  public StandardProperty(String simpleName, String propertyName) {
    super(simpleName);

    this.propertyName = propertyName;
  }

  public final void add(String name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    StandardProperty.Constant cte;
    cte = new Constant(name, value);

    constants.add(cte);
  }

  @Override
  final String generate(Code code) {
    return code."""
    public enum \{simpleName} implements \{STYLE_CLASS} {
    \{generatePropertyConstants()}
      private final String className = \{CLASS_SEQ_ID}.next();

      private final String value;

      private \{simpleName}(String value) {
        this.value = value;
      }

      /**
       * Returns the CSS class name.
       *
       * @return the CSS class name
       */
      @Override
      public final String className() {
        return className;
      }

      /**
       * Returns the CSS style rule represented by this utility class.
       *
       * @return the CSS style rule
       */
      @Override
      public final String toString() {
        return "." + className + " { \{propertyName}: " + value + " }";
      }

    }""";
  }

  private String generatePropertyConstants() {
    List<String> result;
    result = new ArrayList<>();

    for (var cte : constants) {
      result.add("  " + cte.name() + "(\"" + Code.escape(cte.value()) + "\")");
    }

    return result.stream().collect(Collectors.joining(",\n\n", "\n", ";\n"));
  }

}