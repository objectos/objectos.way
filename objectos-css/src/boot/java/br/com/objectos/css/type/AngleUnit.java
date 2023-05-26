package br.com.objectos.css.type;

import java.util.Locale;

public enum AngleUnit {
  DEG,

  GRAD,

  RAD,

  TURN;

  private static final AngleUnit[] ARRAY = AngleUnit.values();

  private final String name;

  private AngleUnit() {
    this.name = name().toLowerCase(Locale.US);
  }

  public static AngleUnit getByCode(int code) {
    return ARRAY[code];
  }

  public static int size() {
    return ARRAY.length;
  }

  public final int getCode() {
    return ordinal();
  }

  public final String getName() {
    return name;
  }
}
