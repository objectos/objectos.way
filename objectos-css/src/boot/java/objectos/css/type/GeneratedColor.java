package objectos.css.type;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

abstract class GeneratedColor {
  public static final ColorName ActiveBorder = new ColorName(0, "ActiveBorder");

  public static final ColorName ActiveCaption = new ColorName(1, "ActiveCaption");

  public static final ColorName AppWorkspace = new ColorName(2, "AppWorkspace");

  public static final ColorName Background = new ColorName(3, "Background");

  public static final ColorName ButtonFace = new ColorName(4, "ButtonFace");

  public static final ColorName ButtonHighlight = new ColorName(5, "ButtonHighlight");

  public static final ColorName ButtonShadow = new ColorName(6, "ButtonShadow");

  public static final ColorName ButtonText = new ColorName(7, "ButtonText");

  public static final ColorName CaptionText = new ColorName(8, "CaptionText");

  public static final ColorName GrayText = new ColorName(9, "GrayText");

  public static final ColorName Highlight = new ColorName(10, "Highlight");

  public static final ColorName HighlightText = new ColorName(11, "HighlightText");

  public static final ColorName InactiveBorder = new ColorName(12, "InactiveBorder");

  public static final ColorName InactiveCaption = new ColorName(13, "InactiveCaption");

  public static final ColorName InactiveCaptionText = new ColorName(14, "InactiveCaptionText");

  public static final ColorName InfoBackground = new ColorName(15, "InfoBackground");

  public static final ColorName InfoText = new ColorName(16, "InfoText");

  public static final ColorName MenuText = new ColorName(17, "MenuText");

  public static final ColorName Scrollbar = new ColorName(18, "Scrollbar");

  public static final ColorName ThreeDDarkShadow = new ColorName(19, "ThreeDDarkShadow");

  public static final ColorName ThreeDFace = new ColorName(20, "ThreeDFace");

  public static final ColorName ThreeDHighlight = new ColorName(21, "ThreeDHighlight");

  public static final ColorName ThreeDLightShadow = new ColorName(22, "ThreeDLightShadow");

  public static final ColorName ThreeDShadow = new ColorName(23, "ThreeDShadow");

  public static final ColorName Window = new ColorName(24, "Window");

  public static final ColorName WindowFrame = new ColorName(25, "WindowFrame");

  public static final ColorName WindowText = new ColorName(26, "WindowText");

  public static final ColorName aliceblue = new ColorName(27, "aliceblue");

  public static final ColorName antiquewhite = new ColorName(28, "antiquewhite");

  public static final ColorName aqua = new ColorName(29, "aqua");

  public static final ColorName aquamarine = new ColorName(30, "aquamarine");

  public static final ColorName azure = new ColorName(31, "azure");

  public static final ColorName beige = new ColorName(32, "beige");

  public static final ColorName bisque = new ColorName(33, "bisque");

  public static final ColorName black = new ColorName(34, "black");

  public static final ColorName blanchedalmond = new ColorName(35, "blanchedalmond");

  public static final ColorName blue = new ColorName(36, "blue");

  public static final ColorName blueviolet = new ColorName(37, "blueviolet");

  public static final ColorName brown = new ColorName(38, "brown");

  public static final ColorName burlywood = new ColorName(39, "burlywood");

  public static final ColorName cadetblue = new ColorName(40, "cadetblue");

  public static final ColorName chartreuse = new ColorName(41, "chartreuse");

  public static final ColorName chocolate = new ColorName(42, "chocolate");

  public static final ColorName coral = new ColorName(43, "coral");

  public static final ColorName cornflowerblue = new ColorName(44, "cornflowerblue");

  public static final ColorName cornsilk = new ColorName(45, "cornsilk");

  public static final ColorName crimson = new ColorName(46, "crimson");

  public static final ColorName currentcolor = new ColorName(47, "currentcolor");

  public static final ColorName cyan = new ColorName(48, "cyan");

  public static final ColorName darkblue = new ColorName(49, "darkblue");

  public static final ColorName darkcyan = new ColorName(50, "darkcyan");

  public static final ColorName darkgoldenrod = new ColorName(51, "darkgoldenrod");

  public static final ColorName darkgray = new ColorName(52, "darkgray");

  public static final ColorName darkgreen = new ColorName(53, "darkgreen");

  public static final ColorName darkgrey = new ColorName(54, "darkgrey");

  public static final ColorName darkkhaki = new ColorName(55, "darkkhaki");

  public static final ColorName darkmagenta = new ColorName(56, "darkmagenta");

  public static final ColorName darkolivegreen = new ColorName(57, "darkolivegreen");

  public static final ColorName darkorange = new ColorName(58, "darkorange");

  public static final ColorName darkorchid = new ColorName(59, "darkorchid");

  public static final ColorName darkred = new ColorName(60, "darkred");

  public static final ColorName darksalmon = new ColorName(61, "darksalmon");

  public static final ColorName darkseagreen = new ColorName(62, "darkseagreen");

  public static final ColorName darkslateblue = new ColorName(63, "darkslateblue");

  public static final ColorName darkslategray = new ColorName(64, "darkslategray");

  public static final ColorName darkslategrey = new ColorName(65, "darkslategrey");

  public static final ColorName darkturquoise = new ColorName(66, "darkturquoise");

  public static final ColorName darkviolet = new ColorName(67, "darkviolet");

  public static final ColorName deeppink = new ColorName(68, "deeppink");

  public static final ColorName deepskyblue = new ColorName(69, "deepskyblue");

  public static final ColorName dimgray = new ColorName(70, "dimgray");

  public static final ColorName dimgrey = new ColorName(71, "dimgrey");

  public static final ColorName dodgerblue = new ColorName(72, "dodgerblue");

  public static final ColorName firebrick = new ColorName(73, "firebrick");

  public static final ColorName floralwhite = new ColorName(74, "floralwhite");

  public static final ColorName forestgreen = new ColorName(75, "forestgreen");

  public static final ColorName fuchsia = new ColorName(76, "fuchsia");

  public static final ColorName gainsboro = new ColorName(77, "gainsboro");

  public static final ColorName ghostwhite = new ColorName(78, "ghostwhite");

  public static final ColorName gold = new ColorName(79, "gold");

  public static final ColorName goldenrod = new ColorName(80, "goldenrod");

  public static final ColorName gray = new ColorName(81, "gray");

  public static final ColorName green = new ColorName(82, "green");

  public static final ColorName greenyellow = new ColorName(83, "greenyellow");

  public static final ColorName grey = new ColorName(84, "grey");

  public static final ColorName honeydew = new ColorName(85, "honeydew");

  public static final ColorName hotpink = new ColorName(86, "hotpink");

  public static final ColorName indianred = new ColorName(87, "indianred");

  public static final ColorName indigo = new ColorName(88, "indigo");

  public static final ColorName ivory = new ColorName(89, "ivory");

  public static final ColorName khaki = new ColorName(90, "khaki");

  public static final ColorName lavender = new ColorName(91, "lavender");

  public static final ColorName lavenderblush = new ColorName(92, "lavenderblush");

  public static final ColorName lawngreen = new ColorName(93, "lawngreen");

  public static final ColorName lemonchiffon = new ColorName(94, "lemonchiffon");

  public static final ColorName lightblue = new ColorName(95, "lightblue");

  public static final ColorName lightcoral = new ColorName(96, "lightcoral");

  public static final ColorName lightcyan = new ColorName(97, "lightcyan");

  public static final ColorName lightgoldenrodyellow = new ColorName(98, "lightgoldenrodyellow");

  public static final ColorName lightgray = new ColorName(99, "lightgray");

  public static final ColorName lightgreen = new ColorName(100, "lightgreen");

  public static final ColorName lightgrey = new ColorName(101, "lightgrey");

  public static final ColorName lightpink = new ColorName(102, "lightpink");

  public static final ColorName lightsalmon = new ColorName(103, "lightsalmon");

  public static final ColorName lightseagreen = new ColorName(104, "lightseagreen");

  public static final ColorName lightskyblue = new ColorName(105, "lightskyblue");

  public static final ColorName lightslategray = new ColorName(106, "lightslategray");

  public static final ColorName lightslategrey = new ColorName(107, "lightslategrey");

  public static final ColorName lightsteelblue = new ColorName(108, "lightsteelblue");

  public static final ColorName lightyellow = new ColorName(109, "lightyellow");

  public static final ColorName lime = new ColorName(110, "lime");

  public static final ColorName limegreen = new ColorName(111, "limegreen");

  public static final ColorName linen = new ColorName(112, "linen");

  public static final ColorName magenta = new ColorName(113, "magenta");

  public static final ColorName maroon = new ColorName(114, "maroon");

  public static final ColorName mediumaquamarine = new ColorName(115, "mediumaquamarine");

  public static final ColorName mediumblue = new ColorName(116, "mediumblue");

  public static final ColorName mediumorchid = new ColorName(117, "mediumorchid");

  public static final ColorName mediumpurple = new ColorName(118, "mediumpurple");

  public static final ColorName mediumseagreen = new ColorName(119, "mediumseagreen");

  public static final ColorName mediumslateblue = new ColorName(120, "mediumslateblue");

  public static final ColorName mediumspringgreen = new ColorName(121, "mediumspringgreen");

  public static final ColorName mediumturquoise = new ColorName(122, "mediumturquoise");

  public static final ColorName mediumvioletred = new ColorName(123, "mediumvioletred");

  public static final ColorName midnightblue = new ColorName(124, "midnightblue");

  public static final ColorName mintcream = new ColorName(125, "mintcream");

  public static final ColorName mistyrose = new ColorName(126, "mistyrose");

  public static final ColorName moccasin = new ColorName(127, "moccasin");

  public static final ColorName navajowhite = new ColorName(128, "navajowhite");

  public static final ColorName navy = new ColorName(129, "navy");

  public static final ColorName oldlace = new ColorName(130, "oldlace");

  public static final ColorName olive = new ColorName(131, "olive");

  public static final ColorName olivedrab = new ColorName(132, "olivedrab");

  public static final ColorName orange = new ColorName(133, "orange");

  public static final ColorName orangered = new ColorName(134, "orangered");

  public static final ColorName orchid = new ColorName(135, "orchid");

  public static final ColorName palegoldenrod = new ColorName(136, "palegoldenrod");

  public static final ColorName palegreen = new ColorName(137, "palegreen");

  public static final ColorName paleturquoise = new ColorName(138, "paleturquoise");

  public static final ColorName palevioletred = new ColorName(139, "palevioletred");

  public static final ColorName papayawhip = new ColorName(140, "papayawhip");

  public static final ColorName peachpuff = new ColorName(141, "peachpuff");

  public static final ColorName peru = new ColorName(142, "peru");

  public static final ColorName pink = new ColorName(143, "pink");

  public static final ColorName plum = new ColorName(144, "plum");

  public static final ColorName powderblue = new ColorName(145, "powderblue");

  public static final ColorName purple = new ColorName(146, "purple");

  public static final ColorName rebeccapurple = new ColorName(147, "rebeccapurple");

  public static final ColorName red = new ColorName(148, "red");

  public static final ColorName rosybrown = new ColorName(149, "rosybrown");

  public static final ColorName royalblue = new ColorName(150, "royalblue");

  public static final ColorName saddlebrown = new ColorName(151, "saddlebrown");

  public static final ColorName salmon = new ColorName(152, "salmon");

  public static final ColorName sandybrown = new ColorName(153, "sandybrown");

  public static final ColorName seagreen = new ColorName(154, "seagreen");

  public static final ColorName seashell = new ColorName(155, "seashell");

  public static final ColorName sienna = new ColorName(156, "sienna");

  public static final ColorName silver = new ColorName(157, "silver");

  public static final ColorName skyblue = new ColorName(158, "skyblue");

  public static final ColorName slateblue = new ColorName(159, "slateblue");

  public static final ColorName slategray = new ColorName(160, "slategray");

  public static final ColorName slategrey = new ColorName(161, "slategrey");

  public static final ColorName snow = new ColorName(162, "snow");

  public static final ColorName springgreen = new ColorName(163, "springgreen");

  public static final ColorName steelblue = new ColorName(164, "steelblue");

  public static final ColorName tan = new ColorName(165, "tan");

  public static final ColorName teal = new ColorName(166, "teal");

  public static final ColorName thistle = new ColorName(167, "thistle");

  public static final ColorName tomato = new ColorName(168, "tomato");

  public static final ColorName transparent = new ColorName(169, "transparent");

  public static final ColorName turquoise = new ColorName(170, "turquoise");

  public static final ColorName violet = new ColorName(171, "violet");

  public static final ColorName wheat = new ColorName(172, "wheat");

  public static final ColorName white = new ColorName(173, "white");

  public static final ColorName whitesmoke = new ColorName(174, "whitesmoke");

  public static final ColorName yellow = new ColorName(175, "yellow");

  public static final ColorName yellowgreen = new ColorName(176, "yellowgreen");

  private static final ColorName[] ARRAY = {
    ActiveBorder,
    ActiveCaption,
    AppWorkspace,
    Background,
    ButtonFace,
    ButtonHighlight,
    ButtonShadow,
    ButtonText,
    CaptionText,
    GrayText,
    Highlight,
    HighlightText,
    InactiveBorder,
    InactiveCaption,
    InactiveCaptionText,
    InfoBackground,
    InfoText,
    MenuText,
    Scrollbar,
    ThreeDDarkShadow,
    ThreeDFace,
    ThreeDHighlight,
    ThreeDLightShadow,
    ThreeDShadow,
    Window,
    WindowFrame,
    WindowText,
    aliceblue,
    antiquewhite,
    aqua,
    aquamarine,
    azure,
    beige,
    bisque,
    black,
    blanchedalmond,
    blue,
    blueviolet,
    brown,
    burlywood,
    cadetblue,
    chartreuse,
    chocolate,
    coral,
    cornflowerblue,
    cornsilk,
    crimson,
    currentcolor,
    cyan,
    darkblue,
    darkcyan,
    darkgoldenrod,
    darkgray,
    darkgreen,
    darkgrey,
    darkkhaki,
    darkmagenta,
    darkolivegreen,
    darkorange,
    darkorchid,
    darkred,
    darksalmon,
    darkseagreen,
    darkslateblue,
    darkslategray,
    darkslategrey,
    darkturquoise,
    darkviolet,
    deeppink,
    deepskyblue,
    dimgray,
    dimgrey,
    dodgerblue,
    firebrick,
    floralwhite,
    forestgreen,
    fuchsia,
    gainsboro,
    ghostwhite,
    gold,
    goldenrod,
    gray,
    green,
    greenyellow,
    grey,
    honeydew,
    hotpink,
    indianred,
    indigo,
    ivory,
    khaki,
    lavender,
    lavenderblush,
    lawngreen,
    lemonchiffon,
    lightblue,
    lightcoral,
    lightcyan,
    lightgoldenrodyellow,
    lightgray,
    lightgreen,
    lightgrey,
    lightpink,
    lightsalmon,
    lightseagreen,
    lightskyblue,
    lightslategray,
    lightslategrey,
    lightsteelblue,
    lightyellow,
    lime,
    limegreen,
    linen,
    magenta,
    maroon,
    mediumaquamarine,
    mediumblue,
    mediumorchid,
    mediumpurple,
    mediumseagreen,
    mediumslateblue,
    mediumspringgreen,
    mediumturquoise,
    mediumvioletred,
    midnightblue,
    mintcream,
    mistyrose,
    moccasin,
    navajowhite,
    navy,
    oldlace,
    olive,
    olivedrab,
    orange,
    orangered,
    orchid,
    palegoldenrod,
    palegreen,
    paleturquoise,
    palevioletred,
    papayawhip,
    peachpuff,
    peru,
    pink,
    plum,
    powderblue,
    purple,
    rebeccapurple,
    red,
    rosybrown,
    royalblue,
    saddlebrown,
    salmon,
    sandybrown,
    seagreen,
    seashell,
    sienna,
    silver,
    skyblue,
    slateblue,
    slategray,
    slategrey,
    snow,
    springgreen,
    steelblue,
    tan,
    teal,
    thistle,
    tomato,
    transparent,
    turquoise,
    violet,
    wheat,
    white,
    whitesmoke,
    yellow,
    yellowgreen
  };

  private static final UnmodifiableMap<String, ColorName> MAP = buildMap();

  public static ColorName getByCode(int code) {
    return ARRAY[code];
  }

  public static ColorName getByName(String name) {
    var c = MAP.get(name);
    if (c == null) {
      throw new IllegalArgumentException(name);
    }
    return c;
  }

  public static boolean isColor(String name) {
    return MAP.containsKey(name);
  }

  private static UnmodifiableMap<String, ColorName> buildMap() {
    var m = new GrowableMap<String, ColorName>();
    m.put("ActiveBorder", ActiveBorder);
    m.put("ActiveCaption", ActiveCaption);
    m.put("AppWorkspace", AppWorkspace);
    m.put("Background", Background);
    m.put("ButtonFace", ButtonFace);
    m.put("ButtonHighlight", ButtonHighlight);
    m.put("ButtonShadow", ButtonShadow);
    m.put("ButtonText", ButtonText);
    m.put("CaptionText", CaptionText);
    m.put("GrayText", GrayText);
    m.put("Highlight", Highlight);
    m.put("HighlightText", HighlightText);
    m.put("InactiveBorder", InactiveBorder);
    m.put("InactiveCaption", InactiveCaption);
    m.put("InactiveCaptionText", InactiveCaptionText);
    m.put("InfoBackground", InfoBackground);
    m.put("InfoText", InfoText);
    m.put("MenuText", MenuText);
    m.put("Scrollbar", Scrollbar);
    m.put("ThreeDDarkShadow", ThreeDDarkShadow);
    m.put("ThreeDFace", ThreeDFace);
    m.put("ThreeDHighlight", ThreeDHighlight);
    m.put("ThreeDLightShadow", ThreeDLightShadow);
    m.put("ThreeDShadow", ThreeDShadow);
    m.put("Window", Window);
    m.put("WindowFrame", WindowFrame);
    m.put("WindowText", WindowText);
    m.put("aliceblue", aliceblue);
    m.put("antiquewhite", antiquewhite);
    m.put("aqua", aqua);
    m.put("aquamarine", aquamarine);
    m.put("azure", azure);
    m.put("beige", beige);
    m.put("bisque", bisque);
    m.put("black", black);
    m.put("blanchedalmond", blanchedalmond);
    m.put("blue", blue);
    m.put("blueviolet", blueviolet);
    m.put("brown", brown);
    m.put("burlywood", burlywood);
    m.put("cadetblue", cadetblue);
    m.put("chartreuse", chartreuse);
    m.put("chocolate", chocolate);
    m.put("coral", coral);
    m.put("cornflowerblue", cornflowerblue);
    m.put("cornsilk", cornsilk);
    m.put("crimson", crimson);
    m.put("currentcolor", currentcolor);
    m.put("cyan", cyan);
    m.put("darkblue", darkblue);
    m.put("darkcyan", darkcyan);
    m.put("darkgoldenrod", darkgoldenrod);
    m.put("darkgray", darkgray);
    m.put("darkgreen", darkgreen);
    m.put("darkgrey", darkgrey);
    m.put("darkkhaki", darkkhaki);
    m.put("darkmagenta", darkmagenta);
    m.put("darkolivegreen", darkolivegreen);
    m.put("darkorange", darkorange);
    m.put("darkorchid", darkorchid);
    m.put("darkred", darkred);
    m.put("darksalmon", darksalmon);
    m.put("darkseagreen", darkseagreen);
    m.put("darkslateblue", darkslateblue);
    m.put("darkslategray", darkslategray);
    m.put("darkslategrey", darkslategrey);
    m.put("darkturquoise", darkturquoise);
    m.put("darkviolet", darkviolet);
    m.put("deeppink", deeppink);
    m.put("deepskyblue", deepskyblue);
    m.put("dimgray", dimgray);
    m.put("dimgrey", dimgrey);
    m.put("dodgerblue", dodgerblue);
    m.put("firebrick", firebrick);
    m.put("floralwhite", floralwhite);
    m.put("forestgreen", forestgreen);
    m.put("fuchsia", fuchsia);
    m.put("gainsboro", gainsboro);
    m.put("ghostwhite", ghostwhite);
    m.put("gold", gold);
    m.put("goldenrod", goldenrod);
    m.put("gray", gray);
    m.put("green", green);
    m.put("greenyellow", greenyellow);
    m.put("grey", grey);
    m.put("honeydew", honeydew);
    m.put("hotpink", hotpink);
    m.put("indianred", indianred);
    m.put("indigo", indigo);
    m.put("ivory", ivory);
    m.put("khaki", khaki);
    m.put("lavender", lavender);
    m.put("lavenderblush", lavenderblush);
    m.put("lawngreen", lawngreen);
    m.put("lemonchiffon", lemonchiffon);
    m.put("lightblue", lightblue);
    m.put("lightcoral", lightcoral);
    m.put("lightcyan", lightcyan);
    m.put("lightgoldenrodyellow", lightgoldenrodyellow);
    m.put("lightgray", lightgray);
    m.put("lightgreen", lightgreen);
    m.put("lightgrey", lightgrey);
    m.put("lightpink", lightpink);
    m.put("lightsalmon", lightsalmon);
    m.put("lightseagreen", lightseagreen);
    m.put("lightskyblue", lightskyblue);
    m.put("lightslategray", lightslategray);
    m.put("lightslategrey", lightslategrey);
    m.put("lightsteelblue", lightsteelblue);
    m.put("lightyellow", lightyellow);
    m.put("lime", lime);
    m.put("limegreen", limegreen);
    m.put("linen", linen);
    m.put("magenta", magenta);
    m.put("maroon", maroon);
    m.put("mediumaquamarine", mediumaquamarine);
    m.put("mediumblue", mediumblue);
    m.put("mediumorchid", mediumorchid);
    m.put("mediumpurple", mediumpurple);
    m.put("mediumseagreen", mediumseagreen);
    m.put("mediumslateblue", mediumslateblue);
    m.put("mediumspringgreen", mediumspringgreen);
    m.put("mediumturquoise", mediumturquoise);
    m.put("mediumvioletred", mediumvioletred);
    m.put("midnightblue", midnightblue);
    m.put("mintcream", mintcream);
    m.put("mistyrose", mistyrose);
    m.put("moccasin", moccasin);
    m.put("navajowhite", navajowhite);
    m.put("navy", navy);
    m.put("oldlace", oldlace);
    m.put("olive", olive);
    m.put("olivedrab", olivedrab);
    m.put("orange", orange);
    m.put("orangered", orangered);
    m.put("orchid", orchid);
    m.put("palegoldenrod", palegoldenrod);
    m.put("palegreen", palegreen);
    m.put("paleturquoise", paleturquoise);
    m.put("palevioletred", palevioletred);
    m.put("papayawhip", papayawhip);
    m.put("peachpuff", peachpuff);
    m.put("peru", peru);
    m.put("pink", pink);
    m.put("plum", plum);
    m.put("powderblue", powderblue);
    m.put("purple", purple);
    m.put("rebeccapurple", rebeccapurple);
    m.put("red", red);
    m.put("rosybrown", rosybrown);
    m.put("royalblue", royalblue);
    m.put("saddlebrown", saddlebrown);
    m.put("salmon", salmon);
    m.put("sandybrown", sandybrown);
    m.put("seagreen", seagreen);
    m.put("seashell", seashell);
    m.put("sienna", sienna);
    m.put("silver", silver);
    m.put("skyblue", skyblue);
    m.put("slateblue", slateblue);
    m.put("slategray", slategray);
    m.put("slategrey", slategrey);
    m.put("snow", snow);
    m.put("springgreen", springgreen);
    m.put("steelblue", steelblue);
    m.put("tan", tan);
    m.put("teal", teal);
    m.put("thistle", thistle);
    m.put("tomato", tomato);
    m.put("transparent", transparent);
    m.put("turquoise", turquoise);
    m.put("violet", violet);
    m.put("wheat", wheat);
    m.put("white", white);
    m.put("whitesmoke", whitesmoke);
    m.put("yellow", yellow);
    m.put("yellowgreen", yellowgreen);
    return m.toUnmodifiableMap();
  }
}
