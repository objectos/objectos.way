package objectos.css.select;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public final class TypeSelectors {
  public static final TypeSelector tr = new TypeSelector(0, "tr");

  public static final TypeSelector form = new TypeSelector(1, "form");

  public static final TypeSelector label = new TypeSelector(2, "label");

  public static final TypeSelector figure = new TypeSelector(3, "figure");

  public static final TypeSelector button = new TypeSelector(4, "button");

  public static final TypeSelector thead = new TypeSelector(5, "thead");

  public static final TypeSelector details = new TypeSelector(6, "details");

  public static final TypeSelector hgroup = new TypeSelector(7, "hgroup");

  public static final TypeSelector sub = new TypeSelector(8, "sub");

  public static final TypeSelector h6 = new TypeSelector(9, "h6");

  public static final TypeSelector h5 = new TypeSelector(10, "h5");

  public static final TypeSelector nav = new TypeSelector(11, "nav");

  public static final TypeSelector h4 = new TypeSelector(12, "h4");

  public static final TypeSelector h3 = new TypeSelector(13, "h3");

  public static final TypeSelector h2 = new TypeSelector(14, "h2");

  public static final TypeSelector h1 = new TypeSelector(15, "h1");

  public static final TypeSelector fieldset = new TypeSelector(16, "fieldset");

  public static final TypeSelector html = new TypeSelector(17, "html");

  public static final TypeSelector link = new TypeSelector(18, "link");

  public static final TypeSelector body = new TypeSelector(19, "body");

  public static final TypeSelector select = new TypeSelector(20, "select");

  public static final TypeSelector th = new TypeSelector(21, "th");

  public static final TypeSelector td = new TypeSelector(22, "td");

  public static final TypeSelector meta = new TypeSelector(23, "meta");

  public static final TypeSelector div = new TypeSelector(24, "div");

  public static final TypeSelector ol = new TypeSelector(25, "ol");

  public static final TypeSelector code = new TypeSelector(26, "code");

  public static final TypeSelector abbr = new TypeSelector(27, "abbr");

  public static final TypeSelector small = new TypeSelector(28, "small");

  public static final TypeSelector clipPath = new TypeSelector(29, "clipPath");

  public static final TypeSelector blockquote = new TypeSelector(30, "blockquote");

  public static final TypeSelector article = new TypeSelector(31, "article");

  public static final TypeSelector menu = new TypeSelector(32, "menu");

  public static final TypeSelector em = new TypeSelector(33, "em");

  public static final TypeSelector path = new TypeSelector(34, "path");

  public static final TypeSelector input = new TypeSelector(35, "input");

  public static final TypeSelector pre = new TypeSelector(36, "pre");

  public static final TypeSelector section = new TypeSelector(37, "section");

  public static final TypeSelector span = new TypeSelector(38, "span");

  public static final TypeSelector head = new TypeSelector(39, "head");

  public static final TypeSelector defs = new TypeSelector(40, "defs");

  public static final TypeSelector dt = new TypeSelector(41, "dt");

  public static final TypeSelector strong = new TypeSelector(42, "strong");

  public static final TypeSelector img = new TypeSelector(43, "img");

  public static final TypeSelector dl = new TypeSelector(44, "dl");

  public static final TypeSelector dd = new TypeSelector(45, "dd");

  public static final TypeSelector samp = new TypeSelector(46, "samp");

  public static final TypeSelector optgroup = new TypeSelector(47, "optgroup");

  public static final TypeSelector hr = new TypeSelector(48, "hr");

  public static final TypeSelector header = new TypeSelector(49, "header");

  public static final TypeSelector progress = new TypeSelector(50, "progress");

  public static final TypeSelector legend = new TypeSelector(51, "legend");

  public static final TypeSelector tbody = new TypeSelector(52, "tbody");

  public static final TypeSelector kbd = new TypeSelector(53, "kbd");

  public static final TypeSelector p = new TypeSelector(54, "p");

  public static final TypeSelector script = new TypeSelector(55, "script");

  public static final TypeSelector template = new TypeSelector(56, "template");

  public static final TypeSelector main = new TypeSelector(57, "main");

  public static final TypeSelector table = new TypeSelector(58, "table");

  public static final TypeSelector svg = new TypeSelector(59, "svg");

  public static final TypeSelector g = new TypeSelector(60, "g");

  public static final TypeSelector textarea = new TypeSelector(61, "textarea");

  public static final TypeSelector b = new TypeSelector(62, "b");

  public static final TypeSelector summary = new TypeSelector(63, "summary");

  public static final TypeSelector a = new TypeSelector(64, "a");

  public static final TypeSelector li = new TypeSelector(65, "li");

  public static final TypeSelector option = new TypeSelector(66, "option");

  public static final TypeSelector title = new TypeSelector(67, "title");

  public static final TypeSelector br = new TypeSelector(68, "br");

  public static final TypeSelector ul = new TypeSelector(69, "ul");

  public static final TypeSelector sup = new TypeSelector(70, "sup");

  public static final TypeSelector style = new TypeSelector(71, "style");

  public static final TypeSelector footer = new TypeSelector(72, "footer");

  private static final TypeSelector[] ARRAY = {
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
    ul,
    sup,
    style,
    footer
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
    m.put("sup", sup);
    m.put("style", style);
    m.put("footer", footer);
    return m.toUnmodifiableMap();
  }
}
