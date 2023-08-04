package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class TypeSelectors {
  public static final TypeSelector sup = new TypeSelector(0, "sup");

  public static final TypeSelector style = new TypeSelector(1, "style");

  public static final TypeSelector footer = new TypeSelector(2, "footer");

  public static final TypeSelector tr = new TypeSelector(3, "tr");

  public static final TypeSelector form = new TypeSelector(4, "form");

  public static final TypeSelector label = new TypeSelector(5, "label");

  public static final TypeSelector figure = new TypeSelector(6, "figure");

  public static final TypeSelector button = new TypeSelector(7, "button");

  public static final TypeSelector thead = new TypeSelector(8, "thead");

  public static final TypeSelector details = new TypeSelector(9, "details");

  public static final TypeSelector hgroup = new TypeSelector(10, "hgroup");

  public static final TypeSelector sub = new TypeSelector(11, "sub");

  public static final TypeSelector h6 = new TypeSelector(12, "h6");

  public static final TypeSelector h5 = new TypeSelector(13, "h5");

  public static final TypeSelector nav = new TypeSelector(14, "nav");

  public static final TypeSelector h4 = new TypeSelector(15, "h4");

  public static final TypeSelector h3 = new TypeSelector(16, "h3");

  public static final TypeSelector h2 = new TypeSelector(17, "h2");

  public static final TypeSelector h1 = new TypeSelector(18, "h1");

  public static final TypeSelector fieldset = new TypeSelector(19, "fieldset");

  public static final TypeSelector html = new TypeSelector(20, "html");

  public static final TypeSelector link = new TypeSelector(21, "link");

  public static final TypeSelector body = new TypeSelector(22, "body");

  public static final TypeSelector select = new TypeSelector(23, "select");

  public static final TypeSelector th = new TypeSelector(24, "th");

  public static final TypeSelector td = new TypeSelector(25, "td");

  public static final TypeSelector meta = new TypeSelector(26, "meta");

  public static final TypeSelector div = new TypeSelector(27, "div");

  public static final TypeSelector ol = new TypeSelector(28, "ol");

  public static final TypeSelector code = new TypeSelector(29, "code");

  public static final TypeSelector abbr = new TypeSelector(30, "abbr");

  public static final TypeSelector small = new TypeSelector(31, "small");

  public static final TypeSelector clipPath = new TypeSelector(32, "clipPath");

  public static final TypeSelector blockquote = new TypeSelector(33, "blockquote");

  public static final TypeSelector article = new TypeSelector(34, "article");

  public static final TypeSelector menu = new TypeSelector(35, "menu");

  public static final TypeSelector em = new TypeSelector(36, "em");

  public static final TypeSelector path = new TypeSelector(37, "path");

  public static final TypeSelector input = new TypeSelector(38, "input");

  public static final TypeSelector pre = new TypeSelector(39, "pre");

  public static final TypeSelector section = new TypeSelector(40, "section");

  public static final TypeSelector span = new TypeSelector(41, "span");

  public static final TypeSelector head = new TypeSelector(42, "head");

  public static final TypeSelector defs = new TypeSelector(43, "defs");

  public static final TypeSelector dt = new TypeSelector(44, "dt");

  public static final TypeSelector strong = new TypeSelector(45, "strong");

  public static final TypeSelector img = new TypeSelector(46, "img");

  public static final TypeSelector dl = new TypeSelector(47, "dl");

  public static final TypeSelector dd = new TypeSelector(48, "dd");

  public static final TypeSelector samp = new TypeSelector(49, "samp");

  public static final TypeSelector optgroup = new TypeSelector(50, "optgroup");

  public static final TypeSelector hr = new TypeSelector(51, "hr");

  public static final TypeSelector header = new TypeSelector(52, "header");

  public static final TypeSelector progress = new TypeSelector(53, "progress");

  public static final TypeSelector legend = new TypeSelector(54, "legend");

  public static final TypeSelector tbody = new TypeSelector(55, "tbody");

  public static final TypeSelector kbd = new TypeSelector(56, "kbd");

  public static final TypeSelector p = new TypeSelector(57, "p");

  public static final TypeSelector script = new TypeSelector(58, "script");

  public static final TypeSelector template = new TypeSelector(59, "template");

  public static final TypeSelector main = new TypeSelector(60, "main");

  public static final TypeSelector table = new TypeSelector(61, "table");

  public static final TypeSelector svg = new TypeSelector(62, "svg");

  public static final TypeSelector g = new TypeSelector(63, "g");

  public static final TypeSelector textarea = new TypeSelector(64, "textarea");

  public static final TypeSelector b = new TypeSelector(65, "b");

  public static final TypeSelector summary = new TypeSelector(66, "summary");

  public static final TypeSelector a = new TypeSelector(67, "a");

  public static final TypeSelector li = new TypeSelector(68, "li");

  public static final TypeSelector option = new TypeSelector(69, "option");

  public static final TypeSelector title = new TypeSelector(70, "title");

  public static final TypeSelector br = new TypeSelector(71, "br");

  public static final TypeSelector ul = new TypeSelector(72, "ul");

  private static final TypeSelector[] ARRAY = {
    sup,
    style,
    footer,
    tr,
    form,
    label,
    figure,
    button,
    thead,
    details,
    hgroup,
    sub,
    h6,
    h5,
    nav,
    h4,
    h3,
    h2,
    h1,
    fieldset,
    html,
    link,
    body,
    select,
    th,
    td,
    meta,
    div,
    ol,
    code,
    abbr,
    small,
    clipPath,
    blockquote,
    article,
    menu,
    em,
    path,
    input,
    pre,
    section,
    span,
    head,
    defs,
    dt,
    strong,
    img,
    dl,
    dd,
    samp,
    optgroup,
    hr,
    header,
    progress,
    legend,
    tbody,
    kbd,
    p,
    script,
    template,
    main,
    table,
    svg,
    g,
    textarea,
    b,
    summary,
    a,
    li,
    option,
    title,
    br,
    ul
  };

  private static final UnmodifiableMap<String, TypeSelector> MAP = buildMap();

  private TypeSelectors() {}

  public static TypeSelector getByCode(int code) {
    return ARRAY[code];
  }

  public static TypeSelector getByName(String name) {
    return MAP.get(name);
  }

  private static UnmodifiableMap<String, TypeSelector> buildMap() {
    var m = new GrowableMap<String, TypeSelector>();
    m.put("sup", sup);
    m.put("style", style);
    m.put("footer", footer);
    m.put("tr", tr);
    m.put("form", form);
    m.put("label", label);
    m.put("figure", figure);
    m.put("button", button);
    m.put("thead", thead);
    m.put("details", details);
    m.put("hgroup", hgroup);
    m.put("sub", sub);
    m.put("h6", h6);
    m.put("h5", h5);
    m.put("nav", nav);
    m.put("h4", h4);
    m.put("h3", h3);
    m.put("h2", h2);
    m.put("h1", h1);
    m.put("fieldset", fieldset);
    m.put("html", html);
    m.put("link", link);
    m.put("body", body);
    m.put("select", select);
    m.put("th", th);
    m.put("td", td);
    m.put("meta", meta);
    m.put("div", div);
    m.put("ol", ol);
    m.put("code", code);
    m.put("abbr", abbr);
    m.put("small", small);
    m.put("clipPath", clipPath);
    m.put("blockquote", blockquote);
    m.put("article", article);
    m.put("menu", menu);
    m.put("em", em);
    m.put("path", path);
    m.put("input", input);
    m.put("pre", pre);
    m.put("section", section);
    m.put("span", span);
    m.put("head", head);
    m.put("defs", defs);
    m.put("dt", dt);
    m.put("strong", strong);
    m.put("img", img);
    m.put("dl", dl);
    m.put("dd", dd);
    m.put("samp", samp);
    m.put("optgroup", optgroup);
    m.put("hr", hr);
    m.put("header", header);
    m.put("progress", progress);
    m.put("legend", legend);
    m.put("tbody", tbody);
    m.put("kbd", kbd);
    m.put("p", p);
    m.put("script", script);
    m.put("template", template);
    m.put("main", main);
    m.put("table", table);
    m.put("svg", svg);
    m.put("g", g);
    m.put("textarea", textarea);
    m.put("b", b);
    m.put("summary", summary);
    m.put("a", a);
    m.put("li", li);
    m.put("option", option);
    m.put("title", title);
    m.put("br", br);
    m.put("ul", ul);
    return m.toUnmodifiableMap();
  }
}
