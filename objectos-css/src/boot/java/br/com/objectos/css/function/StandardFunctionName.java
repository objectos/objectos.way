package br.com.objectos.css.function;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public enum StandardFunctionName implements FunctionName {
  ROTATE("rotate", "rotate"),

  ROTATEX("rotateX", "rotateX"),

  ROTATEY("rotateY", "rotateY"),

  ROTATEZ("rotateZ", "rotateZ");

  private static final StandardFunctionName[] ARRAY = StandardFunctionName.values();

  private static final UnmodifiableMap<String, StandardFunctionName> MAP = buildMap();

  private final String javaName;

  private final String name;

  private StandardFunctionName(String javaName, String name) {
    this.javaName = javaName;
    this.name = name;
  }

  public static StandardFunctionName getByCode(int code) {
    return ARRAY[code];
  }

  public static StandardFunctionName getByName(String name) {
    return MAP.get(name);
  }

  private static UnmodifiableMap<String, StandardFunctionName> buildMap() {
    var m = new GrowableMap<String, StandardFunctionName>();
    m.put("rotate", ROTATE);
    m.put("rotateX", ROTATEX);
    m.put("rotateY", ROTATEY);
    m.put("rotateZ", ROTATEZ);
    return m.toUnmodifiableMap();
  }

  public static int size() {
    return ARRAY.length;
  }

  @Override
  public final int getCode() {
    return ordinal();
  }

  public final String getJavaName() {
    return javaName;
  }

  @Override
  public final String getName() {
    return name;
  }
}
