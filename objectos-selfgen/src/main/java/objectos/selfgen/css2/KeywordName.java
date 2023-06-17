/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.selfgen.css2;

import java.util.Objects;
import java.util.TreeMap;
import objectos.code.ClassTypeName;

public final class KeywordName implements Value {

  public final String fieldName;

  public final String keywordName;

  private final TreeMap<String, ClassTypeName> interfaces = new TreeMap<>();

  KeywordName(String fieldName, String keywordName) {
    this.fieldName = fieldName;
    this.keywordName = keywordName;
  }

  public static KeywordName of(String name) {
    Objects.requireNonNull(name, "name == null");

    var fieldName = name;

    return new KeywordName(fieldName, name);
  }

  @Override
  public final void addInterface(ClassTypeName className) {
    var key = className.simpleName();

    interfaces.put(key, className);
  }

  public final ClassTypeName fieldType() {
    return switch (interfaces.size()) {
      case 0 -> throw new IllegalStateException(
        """

        """
      );

      case 1 -> interfaces.firstEntry().getValue();

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

}