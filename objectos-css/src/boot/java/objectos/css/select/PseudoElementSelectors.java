package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class PseudoElementSelectors {
  public static final PseudoElementSelector AFTER = new PseudoElementSelector(0, "after");

  public static final PseudoElementSelector BACKDROP = new PseudoElementSelector(1, "backdrop");

  public static final PseudoElementSelector BEFORE = new PseudoElementSelector(2, "before");

  public static final PseudoElementSelector CUE = new PseudoElementSelector(3, "cue");

  public static final PseudoElementSelector FIRST_LETTER = new PseudoElementSelector(4, "first-letter");

  public static final PseudoElementSelector FIRST_LINE = new PseudoElementSelector(5, "first-line");

  public static final PseudoElementSelector GRAMMAR_ERROR = new PseudoElementSelector(6, "grammar-error");

  public static final PseudoElementSelector MARKER = new PseudoElementSelector(7, "marker");

  public static final PseudoElementSelector PLACEHOLDER = new PseudoElementSelector(8, "placeholder");

  public static final PseudoElementSelector SELECTION = new PseudoElementSelector(9, "selection");

  public static final PseudoElementSelector SPELLING_ERROR = new PseudoElementSelector(10, "spelling-error");

  public static final PseudoElementSelector _MOZ_FOCUS_INNER = new PseudoElementSelector(11, "-moz-focus-inner");

  public static final PseudoElementSelector _WEBKIT_INNER_SPIN_BUTTON = new PseudoElementSelector(12, "-webkit-inner-spin-button");

  public static final PseudoElementSelector _WEBKIT_OUTER_SPIN_BUTTON = new PseudoElementSelector(13, "-webkit-outer-spin-button");

  public static final PseudoElementSelector _WEBKIT_SEARCH_DECORATION = new PseudoElementSelector(14, "-webkit-search-decoration");

  public static final PseudoElementSelector _WEBKIT_FILE_UPLOAD_BUTTON = new PseudoElementSelector(15, "-webkit-file-upload-button");

  private static final PseudoElementSelector[] ARRAY = {
    AFTER,
    BACKDROP,
    BEFORE,
    CUE,
    FIRST_LETTER,
    FIRST_LINE,
    GRAMMAR_ERROR,
    MARKER,
    PLACEHOLDER,
    SELECTION,
    SPELLING_ERROR,
    _MOZ_FOCUS_INNER,
    _WEBKIT_INNER_SPIN_BUTTON,
    _WEBKIT_OUTER_SPIN_BUTTON,
    _WEBKIT_SEARCH_DECORATION,
    _WEBKIT_FILE_UPLOAD_BUTTON
  };

  private static final UnmodifiableMap<String, PseudoElementSelector> MAP = buildMap();

  private PseudoElementSelectors() {}

  public static PseudoElementSelector getByCode(int code) {
    return ARRAY[code];
  }

  public static PseudoElementSelector getByName(String name) {
    return MAP.get(name);
  }

  private static UnmodifiableMap<String, PseudoElementSelector> buildMap() {
    var m = new GrowableMap<String, PseudoElementSelector>();
    m.put("after", AFTER);
    m.put("backdrop", BACKDROP);
    m.put("before", BEFORE);
    m.put("cue", CUE);
    m.put("first-letter", FIRST_LETTER);
    m.put("first-line", FIRST_LINE);
    m.put("grammar-error", GRAMMAR_ERROR);
    m.put("marker", MARKER);
    m.put("placeholder", PLACEHOLDER);
    m.put("selection", SELECTION);
    m.put("spelling-error", SPELLING_ERROR);
    m.put("-moz-focus-inner", _MOZ_FOCUS_INNER);
    m.put("-webkit-inner-spin-button", _WEBKIT_INNER_SPIN_BUTTON);
    m.put("-webkit-outer-spin-button", _WEBKIT_OUTER_SPIN_BUTTON);
    m.put("-webkit-search-decoration", _WEBKIT_SEARCH_DECORATION);
    m.put("-webkit-file-upload-button", _WEBKIT_FILE_UPLOAD_BUTTON);
    return m.toUnmodifiableMap();
  }
}
