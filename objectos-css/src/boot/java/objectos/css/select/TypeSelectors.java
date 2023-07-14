package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class TypeSelectors {
  public static final TypeSelector textarea = new TypeSelector(0, "textarea");

  public static final TypeSelector b = new TypeSelector(1, "b");

  public static final TypeSelector summary = new TypeSelector(2, "summary");

  public static final TypeSelector a = new TypeSelector(3, "a");

  public static final TypeSelector li = new TypeSelector(4, "li");

  public static final TypeSelector option = new TypeSelector(5, "option");

  public static final TypeSelector title = new TypeSelector(6, "title");

  public static final TypeSelector br = new TypeSelector(7, "br");

  public static final TypeSelector ul = new TypeSelector(8, "ul");

  public static final TypeSelector sup = new TypeSelector(9, "sup");

  public static final TypeSelector style = new TypeSelector(10, "style");

  public static final TypeSelector footer = new TypeSelector(11, "footer");

  public static final TypeSelector tr = new TypeSelector(12, "tr");

  public static final TypeSelector form = new TypeSelector(13, "form");

  public static final TypeSelector label = new TypeSelector(14, "label");

  public static final TypeSelector figure = new TypeSelector(15, "figure");

  public static final TypeSelector button = new TypeSelector(16, "button");

  public static final TypeSelector thead = new TypeSelector(17, "thead");

  public static final TypeSelector details = new TypeSelector(18, "details");

  public static final TypeSelector hgroup = new TypeSelector(19, "hgroup");

  public static final TypeSelector sub = new TypeSelector(20, "sub");

  public static final TypeSelector h6 = new TypeSelector(21, "h6");

  public static final TypeSelector h5 = new TypeSelector(22, "h5");

  public static final TypeSelector nav = new TypeSelector(23, "nav");

  public static final TypeSelector h4 = new TypeSelector(24, "h4");

  public static final TypeSelector h3 = new TypeSelector(25, "h3");

  public static final TypeSelector h2 = new TypeSelector(26, "h2");

  public static final TypeSelector h1 = new TypeSelector(27, "h1");

  public static final TypeSelector fieldset = new TypeSelector(28, "fieldset");

  public static final TypeSelector html = new TypeSelector(29, "html");

  public static final TypeSelector link = new TypeSelector(30, "link");

  public static final TypeSelector body = new TypeSelector(31, "body");

  public static final TypeSelector select = new TypeSelector(32, "select");

  public static final TypeSelector th = new TypeSelector(33, "th");

  public static final TypeSelector td = new TypeSelector(34, "td");

  public static final TypeSelector meta = new TypeSelector(35, "meta");

  public static final TypeSelector div = new TypeSelector(36, "div");

  public static final TypeSelector ol = new TypeSelector(37, "ol");

  public static final TypeSelector code = new TypeSelector(38, "code");

  public static final TypeSelector abbr = new TypeSelector(39, "abbr");

  public static final TypeSelector small = new TypeSelector(40, "small");

  public static final TypeSelector clipPath = new TypeSelector(41, "clipPath");

  public static final TypeSelector blockquote = new TypeSelector(42, "blockquote");

  public static final TypeSelector article = new TypeSelector(43, "article");

  public static final TypeSelector menu = new TypeSelector(44, "menu");

  public static final TypeSelector em = new TypeSelector(45, "em");

  public static final TypeSelector path = new TypeSelector(46, "path");

  public static final TypeSelector input = new TypeSelector(47, "input");

  public static final TypeSelector pre = new TypeSelector(48, "pre");

  public static final TypeSelector section = new TypeSelector(49, "section");

  public static final TypeSelector span = new TypeSelector(50, "span");

  public static final TypeSelector head = new TypeSelector(51, "head");

  public static final TypeSelector defs = new TypeSelector(52, "defs");

  public static final TypeSelector dt = new TypeSelector(53, "dt");

  public static final TypeSelector strong = new TypeSelector(54, "strong");

  public static final TypeSelector img = new TypeSelector(55, "img");

  public static final TypeSelector dl = new TypeSelector(56, "dl");

  public static final TypeSelector dd = new TypeSelector(57, "dd");

  public static final TypeSelector samp = new TypeSelector(58, "samp");

  public static final TypeSelector optgroup = new TypeSelector(59, "optgroup");

  public static final TypeSelector hr = new TypeSelector(60, "hr");

  public static final TypeSelector header = new TypeSelector(61, "header");

  public static final TypeSelector progress = new TypeSelector(62, "progress");

  public static final TypeSelector legend = new TypeSelector(63, "legend");

  public static final TypeSelector tbody = new TypeSelector(64, "tbody");

  public static final TypeSelector kbd = new TypeSelector(65, "kbd");

  public static final TypeSelector p = new TypeSelector(66, "p");

  public static final TypeSelector script = new TypeSelector(67, "script");

  public static final TypeSelector template = new TypeSelector(68, "template");

  public static final TypeSelector main = new TypeSelector(69, "main");

  public static final TypeSelector table = new TypeSelector(70, "table");

  public static final TypeSelector svg = new TypeSelector(71, "svg");

  public static final TypeSelector g = new TypeSelector(72, "g");

  private static final TypeSelector[] ARRAY = {
    textarea,
    b,
    summary,
    a,
    li,
    option,
    title,
    br,
    ul,
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
    g
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
    m.put("textarea", textarea);
    m.put("b", b);
    m.put("summary", summary);
    m.put("a", a);
    m.put("li", li);
    m.put("option", option);
    m.put("title", title);
    m.put("br", br);
    m.put("ul", ul);
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
    return m.toUnmodifiableMap();
  }
}
