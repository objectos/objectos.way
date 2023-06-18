package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class TypeSelectors {
  public static final TypeSelector menu = new TypeSelector(0, "menu");

  public static final TypeSelector em = new TypeSelector(1, "em");

  public static final TypeSelector path = new TypeSelector(2, "path");

  public static final TypeSelector input = new TypeSelector(3, "input");

  public static final TypeSelector pre = new TypeSelector(4, "pre");

  public static final TypeSelector section = new TypeSelector(5, "section");

  public static final TypeSelector span = new TypeSelector(6, "span");

  public static final TypeSelector head = new TypeSelector(7, "head");

  public static final TypeSelector defs = new TypeSelector(8, "defs");

  public static final TypeSelector dt = new TypeSelector(9, "dt");

  public static final TypeSelector strong = new TypeSelector(10, "strong");

  public static final TypeSelector img = new TypeSelector(11, "img");

  public static final TypeSelector dl = new TypeSelector(12, "dl");

  public static final TypeSelector dd = new TypeSelector(13, "dd");

  public static final TypeSelector samp = new TypeSelector(14, "samp");

  public static final TypeSelector optgroup = new TypeSelector(15, "optgroup");

  public static final TypeSelector hr = new TypeSelector(16, "hr");

  public static final TypeSelector header = new TypeSelector(17, "header");

  public static final TypeSelector progress = new TypeSelector(18, "progress");

  public static final TypeSelector legend = new TypeSelector(19, "legend");

  public static final TypeSelector tbody = new TypeSelector(20, "tbody");

  public static final TypeSelector kbd = new TypeSelector(21, "kbd");

  public static final TypeSelector p = new TypeSelector(22, "p");

  public static final TypeSelector script = new TypeSelector(23, "script");

  public static final TypeSelector template = new TypeSelector(24, "template");

  public static final TypeSelector main = new TypeSelector(25, "main");

  public static final TypeSelector table = new TypeSelector(26, "table");

  public static final TypeSelector svg = new TypeSelector(27, "svg");

  public static final TypeSelector g = new TypeSelector(28, "g");

  public static final TypeSelector textarea = new TypeSelector(29, "textarea");

  public static final TypeSelector b = new TypeSelector(30, "b");

  public static final TypeSelector summary = new TypeSelector(31, "summary");

  public static final TypeSelector a = new TypeSelector(32, "a");

  public static final TypeSelector li = new TypeSelector(33, "li");

  public static final TypeSelector option = new TypeSelector(34, "option");

  public static final TypeSelector title = new TypeSelector(35, "title");

  public static final TypeSelector br = new TypeSelector(36, "br");

  public static final TypeSelector ul = new TypeSelector(37, "ul");

  public static final TypeSelector sup = new TypeSelector(38, "sup");

  public static final TypeSelector style = new TypeSelector(39, "style");

  public static final TypeSelector footer = new TypeSelector(40, "footer");

  public static final TypeSelector tr = new TypeSelector(41, "tr");

  public static final TypeSelector form = new TypeSelector(42, "form");

  public static final TypeSelector label = new TypeSelector(43, "label");

  public static final TypeSelector figure = new TypeSelector(44, "figure");

  public static final TypeSelector button = new TypeSelector(45, "button");

  public static final TypeSelector thead = new TypeSelector(46, "thead");

  public static final TypeSelector details = new TypeSelector(47, "details");

  public static final TypeSelector hgroup = new TypeSelector(48, "hgroup");

  public static final TypeSelector sub = new TypeSelector(49, "sub");

  public static final TypeSelector h6 = new TypeSelector(50, "h6");

  public static final TypeSelector h5 = new TypeSelector(51, "h5");

  public static final TypeSelector nav = new TypeSelector(52, "nav");

  public static final TypeSelector h4 = new TypeSelector(53, "h4");

  public static final TypeSelector h3 = new TypeSelector(54, "h3");

  public static final TypeSelector h2 = new TypeSelector(55, "h2");

  public static final TypeSelector h1 = new TypeSelector(56, "h1");

  public static final TypeSelector fieldset = new TypeSelector(57, "fieldset");

  public static final TypeSelector html = new TypeSelector(58, "html");

  public static final TypeSelector link = new TypeSelector(59, "link");

  public static final TypeSelector body = new TypeSelector(60, "body");

  public static final TypeSelector select = new TypeSelector(61, "select");

  public static final TypeSelector th = new TypeSelector(62, "th");

  public static final TypeSelector td = new TypeSelector(63, "td");

  public static final TypeSelector meta = new TypeSelector(64, "meta");

  public static final TypeSelector div = new TypeSelector(65, "div");

  public static final TypeSelector ol = new TypeSelector(66, "ol");

  public static final TypeSelector code = new TypeSelector(67, "code");

  public static final TypeSelector abbr = new TypeSelector(68, "abbr");

  public static final TypeSelector small = new TypeSelector(69, "small");

  public static final TypeSelector clipPath = new TypeSelector(70, "clipPath");

  public static final TypeSelector blockquote = new TypeSelector(71, "blockquote");

  public static final TypeSelector article = new TypeSelector(72, "article");

  private static final TypeSelector[] ARRAY = {
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
    article
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
    return m.toUnmodifiableMap();
  }
}
