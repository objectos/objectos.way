package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class TypeSelectors {
  public static final TypeSelector kbd = new TypeSelector(0, "kbd");

  public static final TypeSelector tbody = new TypeSelector(1, "tbody");

  public static final TypeSelector legend = new TypeSelector(2, "legend");

  public static final TypeSelector progress = new TypeSelector(3, "progress");

  public static final TypeSelector header = new TypeSelector(4, "header");

  public static final TypeSelector hr = new TypeSelector(5, "hr");

  public static final TypeSelector optgroup = new TypeSelector(6, "optgroup");

  public static final TypeSelector samp = new TypeSelector(7, "samp");

  public static final TypeSelector dd = new TypeSelector(8, "dd");

  public static final TypeSelector dl = new TypeSelector(9, "dl");

  public static final TypeSelector img = new TypeSelector(10, "img");

  public static final TypeSelector strong = new TypeSelector(11, "strong");

  public static final TypeSelector dt = new TypeSelector(12, "dt");

  public static final TypeSelector defs = new TypeSelector(13, "defs");

  public static final TypeSelector head = new TypeSelector(14, "head");

  public static final TypeSelector span = new TypeSelector(15, "span");

  public static final TypeSelector section = new TypeSelector(16, "section");

  public static final TypeSelector pre = new TypeSelector(17, "pre");

  public static final TypeSelector input = new TypeSelector(18, "input");

  public static final TypeSelector path = new TypeSelector(19, "path");

  public static final TypeSelector em = new TypeSelector(20, "em");

  public static final TypeSelector menu = new TypeSelector(21, "menu");

  public static final TypeSelector article = new TypeSelector(22, "article");

  public static final TypeSelector blockquote = new TypeSelector(23, "blockquote");

  public static final TypeSelector clipPath = new TypeSelector(24, "clipPath");

  public static final TypeSelector small = new TypeSelector(25, "small");

  public static final TypeSelector abbr = new TypeSelector(26, "abbr");

  public static final TypeSelector code = new TypeSelector(27, "code");

  public static final TypeSelector ol = new TypeSelector(28, "ol");

  public static final TypeSelector div = new TypeSelector(29, "div");

  public static final TypeSelector meta = new TypeSelector(30, "meta");

  public static final TypeSelector td = new TypeSelector(31, "td");

  public static final TypeSelector th = new TypeSelector(32, "th");

  public static final TypeSelector select = new TypeSelector(33, "select");

  public static final TypeSelector body = new TypeSelector(34, "body");

  public static final TypeSelector link = new TypeSelector(35, "link");

  public static final TypeSelector html = new TypeSelector(36, "html");

  public static final TypeSelector fieldset = new TypeSelector(37, "fieldset");

  public static final TypeSelector h1 = new TypeSelector(38, "h1");

  public static final TypeSelector h2 = new TypeSelector(39, "h2");

  public static final TypeSelector h3 = new TypeSelector(40, "h3");

  public static final TypeSelector h4 = new TypeSelector(41, "h4");

  public static final TypeSelector nav = new TypeSelector(42, "nav");

  public static final TypeSelector h5 = new TypeSelector(43, "h5");

  public static final TypeSelector h6 = new TypeSelector(44, "h6");

  public static final TypeSelector sub = new TypeSelector(45, "sub");

  public static final TypeSelector hgroup = new TypeSelector(46, "hgroup");

  public static final TypeSelector details = new TypeSelector(47, "details");

  public static final TypeSelector thead = new TypeSelector(48, "thead");

  public static final TypeSelector button = new TypeSelector(49, "button");

  public static final TypeSelector figure = new TypeSelector(50, "figure");

  public static final TypeSelector label = new TypeSelector(51, "label");

  public static final TypeSelector form = new TypeSelector(52, "form");

  public static final TypeSelector tr = new TypeSelector(53, "tr");

  public static final TypeSelector footer = new TypeSelector(54, "footer");

  public static final TypeSelector style = new TypeSelector(55, "style");

  public static final TypeSelector sup = new TypeSelector(56, "sup");

  public static final TypeSelector ul = new TypeSelector(57, "ul");

  public static final TypeSelector br = new TypeSelector(58, "br");

  public static final TypeSelector title = new TypeSelector(59, "title");

  public static final TypeSelector option = new TypeSelector(60, "option");

  public static final TypeSelector li = new TypeSelector(61, "li");

  public static final TypeSelector a = new TypeSelector(62, "a");

  public static final TypeSelector summary = new TypeSelector(63, "summary");

  public static final TypeSelector b = new TypeSelector(64, "b");

  public static final TypeSelector textarea = new TypeSelector(65, "textarea");

  public static final TypeSelector g = new TypeSelector(66, "g");

  public static final TypeSelector svg = new TypeSelector(67, "svg");

  public static final TypeSelector table = new TypeSelector(68, "table");

  public static final TypeSelector main = new TypeSelector(69, "main");

  public static final TypeSelector template = new TypeSelector(70, "template");

  public static final TypeSelector script = new TypeSelector(71, "script");

  public static final TypeSelector p = new TypeSelector(72, "p");

  private static final TypeSelector[] ARRAY = {
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
    label,
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
    p
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
    return m.toUnmodifiableMap();
  }
}
