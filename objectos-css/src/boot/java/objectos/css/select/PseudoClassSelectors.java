package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class PseudoClassSelectors {
  public static final PseudoClassSelector ACTIVE = new PseudoClassSelector(0, "active");

  public static final PseudoClassSelector ANY_LINK = new PseudoClassSelector(1, "any-link");

  public static final PseudoClassSelector BLANK = new PseudoClassSelector(2, "blank");

  public static final PseudoClassSelector CHECKED = new PseudoClassSelector(3, "checked");

  public static final PseudoClassSelector CURRENT = new PseudoClassSelector(4, "current");

  public static final PseudoClassSelector DEFAULT = new PseudoClassSelector(5, "default");

  public static final PseudoClassSelector DEFINED = new PseudoClassSelector(6, "defined");

  public static final PseudoClassSelector DISABLED = new PseudoClassSelector(7, "disabled");

  public static final PseudoClassSelector DROP = new PseudoClassSelector(8, "drop");

  public static final PseudoClassSelector EMPTY = new PseudoClassSelector(9, "empty");

  public static final PseudoClassSelector ENABLED = new PseudoClassSelector(10, "enabled");

  public static final PseudoClassSelector FIRST = new PseudoClassSelector(11, "first");

  public static final PseudoClassSelector FIRST_CHILD = new PseudoClassSelector(12, "first-child");

  public static final PseudoClassSelector FIRST_OF_TYPE = new PseudoClassSelector(13, "first-of-type");

  public static final PseudoClassSelector FULLSCREEN = new PseudoClassSelector(14, "fullscreen");

  public static final PseudoClassSelector FUTURE = new PseudoClassSelector(15, "future");

  public static final PseudoClassSelector FOCUS = new PseudoClassSelector(16, "focus");

  public static final PseudoClassSelector FOCUS_VISIBLE = new PseudoClassSelector(17, "focus-visible");

  public static final PseudoClassSelector FOCUS_WITHIN = new PseudoClassSelector(18, "focus-within");

  public static final PseudoClassSelector HOST = new PseudoClassSelector(19, "host");

  public static final PseudoClassSelector HOVER = new PseudoClassSelector(20, "hover");

  public static final PseudoClassSelector INDETERMINATE = new PseudoClassSelector(21, "indeterminate");

  public static final PseudoClassSelector IN_RANGE = new PseudoClassSelector(22, "in-range");

  public static final PseudoClassSelector INVALID = new PseudoClassSelector(23, "invalid");

  public static final PseudoClassSelector LAST_CHILD = new PseudoClassSelector(24, "last-child");

  public static final PseudoClassSelector LAST_OF_TYPE = new PseudoClassSelector(25, "last-of-type");

  public static final PseudoClassSelector LEFT = new PseudoClassSelector(26, "left");

  public static final PseudoClassSelector LINK = new PseudoClassSelector(27, "link");

  public static final PseudoClassSelector LOCAL_LINK = new PseudoClassSelector(28, "local-link");

  public static final PseudoClassSelector ONLY_CHILD = new PseudoClassSelector(29, "only-child");

  public static final PseudoClassSelector ONLY_OF_TYPE = new PseudoClassSelector(30, "only-of-type");

  public static final PseudoClassSelector OPTIONAL = new PseudoClassSelector(31, "optional");

  public static final PseudoClassSelector OUT_OF_RANGE = new PseudoClassSelector(32, "out-of-range");

  public static final PseudoClassSelector PAST = new PseudoClassSelector(33, "past");

  public static final PseudoClassSelector PLACEHOLDER_SHOWN = new PseudoClassSelector(34, "placeholder-shown");

  public static final PseudoClassSelector READ_ONLY = new PseudoClassSelector(35, "read-only");

  public static final PseudoClassSelector READ_WRITE = new PseudoClassSelector(36, "read-write");

  public static final PseudoClassSelector REQUIRED = new PseudoClassSelector(37, "required");

  public static final PseudoClassSelector RIGHT = new PseudoClassSelector(38, "right");

  public static final PseudoClassSelector ROOT = new PseudoClassSelector(39, "root");

  public static final PseudoClassSelector SCOPE = new PseudoClassSelector(40, "scope");

  public static final PseudoClassSelector TARGET = new PseudoClassSelector(41, "target");

  public static final PseudoClassSelector TARGET_WITHIN = new PseudoClassSelector(42, "target-within");

  public static final PseudoClassSelector USER_INVALID = new PseudoClassSelector(43, "user-invalid");

  public static final PseudoClassSelector VALID = new PseudoClassSelector(44, "valid");

  public static final PseudoClassSelector VISITED = new PseudoClassSelector(45, "visited");

  public static final PseudoClassSelector _MOZ_FOCUSRING = new PseudoClassSelector(46, "-moz-focusring");

  public static final PseudoClassSelector _MOZ_UI_INVALID = new PseudoClassSelector(47, "-moz-ui-invalid");

  private static final PseudoClassSelector[] ARRAY = {
    ACTIVE,
    ANY_LINK,
    BLANK,
    CHECKED,
    CURRENT,
    DEFAULT,
    DEFINED,
    DISABLED,
    DROP,
    EMPTY,
    ENABLED,
    FIRST,
    FIRST_CHILD,
    FIRST_OF_TYPE,
    FULLSCREEN,
    FUTURE,
    FOCUS,
    FOCUS_VISIBLE,
    FOCUS_WITHIN,
    HOST,
    HOVER,
    INDETERMINATE,
    IN_RANGE,
    INVALID,
    LAST_CHILD,
    LAST_OF_TYPE,
    LEFT,
    LINK,
    LOCAL_LINK,
    ONLY_CHILD,
    ONLY_OF_TYPE,
    OPTIONAL,
    OUT_OF_RANGE,
    PAST,
    PLACEHOLDER_SHOWN,
    READ_ONLY,
    READ_WRITE,
    REQUIRED,
    RIGHT,
    ROOT,
    SCOPE,
    TARGET,
    TARGET_WITHIN,
    USER_INVALID,
    VALID,
    VISITED,
    _MOZ_FOCUSRING,
    _MOZ_UI_INVALID
  };

  private static final UnmodifiableMap<String, PseudoClassSelector> MAP = buildMap();

  private PseudoClassSelectors() {}

  public static PseudoClassSelector getByCode(int code) {
    return ARRAY[code];
  }

  public static PseudoClassSelector getByName(String name) {
    return MAP.get(name);
  }

  private static UnmodifiableMap<String, PseudoClassSelector> buildMap() {
    var m = new GrowableMap<String, PseudoClassSelector>();
    m.put("active", ACTIVE);
    m.put("any-link", ANY_LINK);
    m.put("blank", BLANK);
    m.put("checked", CHECKED);
    m.put("current", CURRENT);
    m.put("default", DEFAULT);
    m.put("defined", DEFINED);
    m.put("disabled", DISABLED);
    m.put("drop", DROP);
    m.put("empty", EMPTY);
    m.put("enabled", ENABLED);
    m.put("first", FIRST);
    m.put("first-child", FIRST_CHILD);
    m.put("first-of-type", FIRST_OF_TYPE);
    m.put("fullscreen", FULLSCREEN);
    m.put("future", FUTURE);
    m.put("focus", FOCUS);
    m.put("focus-visible", FOCUS_VISIBLE);
    m.put("focus-within", FOCUS_WITHIN);
    m.put("host", HOST);
    m.put("hover", HOVER);
    m.put("indeterminate", INDETERMINATE);
    m.put("in-range", IN_RANGE);
    m.put("invalid", INVALID);
    m.put("last-child", LAST_CHILD);
    m.put("last-of-type", LAST_OF_TYPE);
    m.put("left", LEFT);
    m.put("link", LINK);
    m.put("local-link", LOCAL_LINK);
    m.put("only-child", ONLY_CHILD);
    m.put("only-of-type", ONLY_OF_TYPE);
    m.put("optional", OPTIONAL);
    m.put("out-of-range", OUT_OF_RANGE);
    m.put("past", PAST);
    m.put("placeholder-shown", PLACEHOLDER_SHOWN);
    m.put("read-only", READ_ONLY);
    m.put("read-write", READ_WRITE);
    m.put("required", REQUIRED);
    m.put("right", RIGHT);
    m.put("root", ROOT);
    m.put("scope", SCOPE);
    m.put("target", TARGET);
    m.put("target-within", TARGET_WITHIN);
    m.put("user-invalid", USER_INVALID);
    m.put("valid", VALID);
    m.put("visited", VISITED);
    m.put("-moz-focusring", _MOZ_FOCUSRING);
    m.put("-moz-ui-invalid", _MOZ_UI_INVALID);
    return m.toUnmodifiableMap();
  }
}
