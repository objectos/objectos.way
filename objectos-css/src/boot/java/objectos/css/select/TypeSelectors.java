package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class TypeSelectors {
  public static final TypeSelector form = new TypeSelector(0, "form");

  public static final TypeSelector tr = new TypeSelector(1, "tr");

  public static final TypeSelector footer = new TypeSelector(2, "footer");

  public static final TypeSelector style = new TypeSelector(3, "style");

  public static final TypeSelector sup = new TypeSelector(4, "sup");

  public static final TypeSelector ul = new TypeSelector(5, "ul");

  public static final TypeSelector br = new TypeSelector(6, "br");

  public static final TypeSelector title = new TypeSelector(7, "title");

  public static final TypeSelector option = new TypeSelector(8, "option");

  public static final TypeSelector li = new TypeSelector(9, "li");

  public static final TypeSelector a = new TypeSelector(10, "a");

  public static final TypeSelector summary = new TypeSelector(11, "summary");

  public static final TypeSelector b = new TypeSelector(12, "b");

  public static final TypeSelector textarea = new TypeSelector(13, "textarea");

  public static final TypeSelector g = new TypeSelector(14, "g");

  public static final TypeSelector svg = new TypeSelector(15, "svg");

  public static final TypeSelector table = new TypeSelector(16, "table");

  public static final TypeSelector main = new TypeSelector(17, "main");

  public static final TypeSelector template = new TypeSelector(18, "template");

  public static final TypeSelector script = new TypeSelector(19, "script");

  public static final TypeSelector p = new TypeSelector(20, "p");

  public static final TypeSelector kbd = new TypeSelector(21, "kbd");

  public static final TypeSelector tbody = new TypeSelector(22, "tbody");

  public static final TypeSelector legend = new TypeSelector(23, "legend");

  public static final TypeSelector progress = new TypeSelector(24, "progress");

  public static final TypeSelector header = new TypeSelector(25, "header");

  public static final TypeSelector hr = new TypeSelector(26, "hr");

  public static final TypeSelector optgroup = new TypeSelector(27, "optgroup");

  public static final TypeSelector samp = new TypeSelector(28, "samp");

  public static final TypeSelector dd = new TypeSelector(29, "dd");

  public static final TypeSelector dl = new TypeSelector(30, "dl");

  public static final TypeSelector img = new TypeSelector(31, "img");

  public static final TypeSelector strong = new TypeSelector(32, "strong");

  public static final TypeSelector dt = new TypeSelector(33, "dt");

  public static final TypeSelector defs = new TypeSelector(34, "defs");

  public static final TypeSelector head = new TypeSelector(35, "head");

  public static final TypeSelector span = new TypeSelector(36, "span");

  public static final TypeSelector section = new TypeSelector(37, "section");

  public static final TypeSelector pre = new TypeSelector(38, "pre");

  public static final TypeSelector input = new TypeSelector(39, "input");

  public static final TypeSelector path = new TypeSelector(40, "path");

  public static final TypeSelector em = new TypeSelector(41, "em");

  public static final TypeSelector menu = new TypeSelector(42, "menu");

  public static final TypeSelector article = new TypeSelector(43, "article");

  public static final TypeSelector blockquote = new TypeSelector(44, "blockquote");

  public static final TypeSelector clipPath = new TypeSelector(45, "clipPath");

  public static final TypeSelector small = new TypeSelector(46, "small");

  public static final TypeSelector abbr = new TypeSelector(47, "abbr");

  public static final TypeSelector code = new TypeSelector(48, "code");

  public static final TypeSelector ol = new TypeSelector(49, "ol");

  public static final TypeSelector div = new TypeSelector(50, "div");

  public static final TypeSelector meta = new TypeSelector(51, "meta");

  public static final TypeSelector td = new TypeSelector(52, "td");

  public static final TypeSelector th = new TypeSelector(53, "th");

  public static final TypeSelector select = new TypeSelector(54, "select");

  public static final TypeSelector body = new TypeSelector(55, "body");

  public static final TypeSelector link = new TypeSelector(56, "link");

  public static final TypeSelector html = new TypeSelector(57, "html");

  public static final TypeSelector fieldset = new TypeSelector(58, "fieldset");

  public static final TypeSelector h1 = new TypeSelector(59, "h1");

  public static final TypeSelector h2 = new TypeSelector(60, "h2");

  public static final TypeSelector h3 = new TypeSelector(61, "h3");

  public static final TypeSelector h4 = new TypeSelector(62, "h4");

  public static final TypeSelector nav = new TypeSelector(63, "nav");

  public static final TypeSelector h5 = new TypeSelector(64, "h5");

  public static final TypeSelector h6 = new TypeSelector(65, "h6");

  public static final TypeSelector sub = new TypeSelector(66, "sub");

  public static final TypeSelector hgroup = new TypeSelector(67, "hgroup");

  public static final TypeSelector details = new TypeSelector(68, "details");

  public static final TypeSelector thead = new TypeSelector(69, "thead");

  public static final TypeSelector button = new TypeSelector(70, "button");

  public static final TypeSelector figure = new TypeSelector(71, "figure");

  public static final TypeSelector label = new TypeSelector(72, "label");

  private static final TypeSelector[] ARRAY = {
    form,
    tr,
    footer,
    style,
    sup,
    ul,
    br,
    title,
    option,
    li,
    a,
    summary,
    b,
    textarea,
    g,
    svg,
    table,
    main,
    template,
    script,
    p,
    kbd,
    tbody,
    legend,
    progress,
    header,
    hr,
    optgroup,
    samp,
    dd,
    dl,
    img,
    strong,
    dt,
    defs,
    head,
    span,
    section,
    pre,
    input,
    path,
    em,
    menu,
    article,
    blockquote,
    clipPath,
    small,
    abbr,
    code,
    ol,
    div,
    meta,
    td,
    th,
    select,
    body,
    link,
    html,
    fieldset,
    h1,
    h2,
    h3,
    h4,
    nav,
    h5,
    h6,
    sub,
    hgroup,
    details,
    thead,
    button,
    figure,
    label
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
    m.put("form", form);
    m.put("tr", tr);
    m.put("footer", footer);
    m.put("style", style);
    m.put("sup", sup);
    m.put("ul", ul);
    m.put("br", br);
    m.put("title", title);
    m.put("option", option);
    m.put("li", li);
    m.put("a", a);
    m.put("summary", summary);
    m.put("b", b);
    m.put("textarea", textarea);
    m.put("g", g);
    m.put("svg", svg);
    m.put("table", table);
    m.put("main", main);
    m.put("template", template);
    m.put("script", script);
    m.put("p", p);
    m.put("kbd", kbd);
    m.put("tbody", tbody);
    m.put("legend", legend);
    m.put("progress", progress);
    m.put("header", header);
    m.put("hr", hr);
    m.put("optgroup", optgroup);
    m.put("samp", samp);
    m.put("dd", dd);
    m.put("dl", dl);
    m.put("img", img);
    m.put("strong", strong);
    m.put("dt", dt);
    m.put("defs", defs);
    m.put("head", head);
    m.put("span", span);
    m.put("section", section);
    m.put("pre", pre);
    m.put("input", input);
    m.put("path", path);
    m.put("em", em);
    m.put("menu", menu);
    m.put("article", article);
    m.put("blockquote", blockquote);
    m.put("clipPath", clipPath);
    m.put("small", small);
    m.put("abbr", abbr);
    m.put("code", code);
    m.put("ol", ol);
    m.put("div", div);
    m.put("meta", meta);
    m.put("td", td);
    m.put("th", th);
    m.put("select", select);
    m.put("body", body);
    m.put("link", link);
    m.put("html", html);
    m.put("fieldset", fieldset);
    m.put("h1", h1);
    m.put("h2", h2);
    m.put("h3", h3);
    m.put("h4", h4);
    m.put("nav", nav);
    m.put("h5", h5);
    m.put("h6", h6);
    m.put("sub", sub);
    m.put("hgroup", hgroup);
    m.put("details", details);
    m.put("thead", thead);
    m.put("button", button);
    m.put("figure", figure);
    m.put("label", label);
    return m.toUnmodifiableMap();
  }
}
