package objectos.css.sheet;

import objectos.css.Css;
import objectos.css.function.RotateFunction;
import objectos.css.function.RotateXFunction;
import objectos.css.function.RotateYFunction;
import objectos.css.function.RotateZFunction;
import objectos.css.function.StandardFunctionName;
import objectos.css.keyword.AbsoluteKeyword;
import objectos.css.keyword.AliasKeyword;
import objectos.css.keyword.AllScrollKeyword;
import objectos.css.keyword.ArabicIndicKeyword;
import objectos.css.keyword.ArmenianKeyword;
import objectos.css.keyword.AutoKeyword;
import objectos.css.keyword.BaselineKeyword;
import objectos.css.keyword.BengaliKeyword;
import objectos.css.keyword.BlinkKeyword;
import objectos.css.keyword.BlockKeyword;
import objectos.css.keyword.BoldKeyword;
import objectos.css.keyword.BolderKeyword;
import objectos.css.keyword.BorderBoxKeyword;
import objectos.css.keyword.BothKeyword;
import objectos.css.keyword.BottomKeyword;
import objectos.css.keyword.BreakSpacesKeyword;
import objectos.css.keyword.ButtonBevelKeyword;
import objectos.css.keyword.ButtonKeyword;
import objectos.css.keyword.CambodianKeyword;
import objectos.css.keyword.CapitalizeKeyword;
import objectos.css.keyword.CaptionKeyword;
import objectos.css.keyword.CellKeyword;
import objectos.css.keyword.CenterKeyword;
import objectos.css.keyword.CheckboxKeyword;
import objectos.css.keyword.CircleKeyword;
import objectos.css.keyword.CjkDecimalKeyword;
import objectos.css.keyword.CjkEarthlyBranchKeyword;
import objectos.css.keyword.CjkHeavenlyStemKeyword;
import objectos.css.keyword.CjkIdeographicKeyword;
import objectos.css.keyword.ClipKeyword;
import objectos.css.keyword.CloseQuoteKeyword;
import objectos.css.keyword.ColResizeKeyword;
import objectos.css.keyword.CollapseKeyword;
import objectos.css.keyword.ColumnKeyword;
import objectos.css.keyword.ColumnReverseKeyword;
import objectos.css.keyword.ContainKeyword;
import objectos.css.keyword.ContentBoxKeyword;
import objectos.css.keyword.ContentKeyword;
import objectos.css.keyword.ContentsKeyword;
import objectos.css.keyword.ContextMenuKeyword;
import objectos.css.keyword.CopyKeyword;
import objectos.css.keyword.CoverKeyword;
import objectos.css.keyword.CrosshairKeyword;
import objectos.css.keyword.CursiveKeyword;
import objectos.css.keyword.DashedKeyword;
import objectos.css.keyword.DecimalKeyword;
import objectos.css.keyword.DecimalLeadingZeroKeyword;
import objectos.css.keyword.DefaultKeyword;
import objectos.css.keyword.DevanagariKeyword;
import objectos.css.keyword.DiscKeyword;
import objectos.css.keyword.DisclosureClosedKeyword;
import objectos.css.keyword.DisclosureOpenKeyword;
import objectos.css.keyword.DottedKeyword;
import objectos.css.keyword.DoubleKeyword;
import objectos.css.keyword.EResizeKeyword;
import objectos.css.keyword.EachLineKeyword;
import objectos.css.keyword.EndKeyword;
import objectos.css.keyword.EthiopicNumericKeyword;
import objectos.css.keyword.EwResizeKeyword;
import objectos.css.keyword.FantasyKeyword;
import objectos.css.keyword.FillKeyword;
import objectos.css.keyword.FirstKeyword;
import objectos.css.keyword.FixedKeyword;
import objectos.css.keyword.FlexEndKeyword;
import objectos.css.keyword.FlexKeyword;
import objectos.css.keyword.FlexStartKeyword;
import objectos.css.keyword.FlowKeyword;
import objectos.css.keyword.FlowRootKeyword;
import objectos.css.keyword.FromFontKeyword;
import objectos.css.keyword.FullSizeKanaKeyword;
import objectos.css.keyword.FullWidthKeyword;
import objectos.css.keyword.GeorgianKeyword;
import objectos.css.keyword.GrabKeyword;
import objectos.css.keyword.GrabbingKeyword;
import objectos.css.keyword.GrammarErrorKeyword;
import objectos.css.keyword.GridKeyword;
import objectos.css.keyword.GrooveKeyword;
import objectos.css.keyword.GujaratiKeyword;
import objectos.css.keyword.GurmukhiKeyword;
import objectos.css.keyword.HangingKeyword;
import objectos.css.keyword.HebrewKeyword;
import objectos.css.keyword.HelpKeyword;
import objectos.css.keyword.HiddenKeyword;
import objectos.css.keyword.HiraganaIrohaKeyword;
import objectos.css.keyword.HiraganaKeyword;
import objectos.css.keyword.HorizontalKeyword;
import objectos.css.keyword.IconKeyword;
import objectos.css.keyword.InheritKeyword;
import objectos.css.keyword.InitialKeyword;
import objectos.css.keyword.InlineBlockKeyword;
import objectos.css.keyword.InlineEndKeyword;
import objectos.css.keyword.InlineFlexKeyword;
import objectos.css.keyword.InlineGridKeyword;
import objectos.css.keyword.InlineKeyword;
import objectos.css.keyword.InlineStartKeyword;
import objectos.css.keyword.InlineTableKeyword;
import objectos.css.keyword.InsetKeyword;
import objectos.css.keyword.InsideKeyword;
import objectos.css.keyword.InvertKeyword;
import objectos.css.keyword.ItalicKeyword;
import objectos.css.keyword.JapaneseFormalKeyword;
import objectos.css.keyword.JapaneseInformalKeyword;
import objectos.css.keyword.JustifyKeyword;
import objectos.css.keyword.KannadaKeyword;
import objectos.css.keyword.KatakanaIrohaKeyword;
import objectos.css.keyword.KatakanaKeyword;
import objectos.css.keyword.Keywords;
import objectos.css.keyword.KhmerKeyword;
import objectos.css.keyword.KoreanHangulFormalKeyword;
import objectos.css.keyword.KoreanHanjaFormalKeyword;
import objectos.css.keyword.KoreanHanjaInformalKeyword;
import objectos.css.keyword.LaoKeyword;
import objectos.css.keyword.LargeKeyword;
import objectos.css.keyword.LargerKeyword;
import objectos.css.keyword.LastKeyword;
import objectos.css.keyword.LeftKeyword;
import objectos.css.keyword.LegacyKeyword;
import objectos.css.keyword.LighterKeyword;
import objectos.css.keyword.LineThroughKeyword;
import objectos.css.keyword.ListItemKeyword;
import objectos.css.keyword.ListboxKeyword;
import objectos.css.keyword.LocalKeyword;
import objectos.css.keyword.LowerAlphaKeyword;
import objectos.css.keyword.LowerArmenianKeyword;
import objectos.css.keyword.LowerGreekKeyword;
import objectos.css.keyword.LowerLatinKeyword;
import objectos.css.keyword.LowerRomanKeyword;
import objectos.css.keyword.LowercaseKeyword;
import objectos.css.keyword.MalayalamKeyword;
import objectos.css.keyword.MatchParentKeyword;
import objectos.css.keyword.MaxContentKeyword;
import objectos.css.keyword.MediumKeyword;
import objectos.css.keyword.MenuKeyword;
import objectos.css.keyword.MenulistButtonKeyword;
import objectos.css.keyword.MenulistKeyword;
import objectos.css.keyword.MessageBoxKeyword;
import objectos.css.keyword.MeterKeyword;
import objectos.css.keyword.MiddleKeyword;
import objectos.css.keyword.MinContentKeyword;
import objectos.css.keyword.MongolianKeyword;
import objectos.css.keyword.MonospaceKeyword;
import objectos.css.keyword.MoveKeyword;
import objectos.css.keyword.MozArabicIndicKeyword;
import objectos.css.keyword.MozBengaliKeyword;
import objectos.css.keyword.MozCjkEarthlyBranchKeyword;
import objectos.css.keyword.MozCjkHeavenlyStemKeyword;
import objectos.css.keyword.MozDevanagariKeyword;
import objectos.css.keyword.MozGujaratiKeyword;
import objectos.css.keyword.MozGurmukhiKeyword;
import objectos.css.keyword.MozKannadaKeyword;
import objectos.css.keyword.MozKhmerKeyword;
import objectos.css.keyword.MozLaoKeyword;
import objectos.css.keyword.MozMalayalamKeyword;
import objectos.css.keyword.MozMyanmarKeyword;
import objectos.css.keyword.MozOriyaKeyword;
import objectos.css.keyword.MozPersianKeyword;
import objectos.css.keyword.MozTamilKeyword;
import objectos.css.keyword.MozTeluguKeyword;
import objectos.css.keyword.MozThaiKeyword;
import objectos.css.keyword.MyanmarKeyword;
import objectos.css.keyword.NResizeKeyword;
import objectos.css.keyword.NeResizeKeyword;
import objectos.css.keyword.NeswResizeKeyword;
import objectos.css.keyword.NoCloseQuoteKeyword;
import objectos.css.keyword.NoDropKeyword;
import objectos.css.keyword.NoOpenQuoteKeyword;
import objectos.css.keyword.NoRepeatKeyword;
import objectos.css.keyword.NoneKeyword;
import objectos.css.keyword.NormalKeyword;
import objectos.css.keyword.NotAllowedKeyword;
import objectos.css.keyword.NowrapKeyword;
import objectos.css.keyword.NsResizeKeyword;
import objectos.css.keyword.NwResizeKeyword;
import objectos.css.keyword.NwseResizeKeyword;
import objectos.css.keyword.ObliqueKeyword;
import objectos.css.keyword.OpenQuoteKeyword;
import objectos.css.keyword.OriyaKeyword;
import objectos.css.keyword.OutsetKeyword;
import objectos.css.keyword.OutsideKeyword;
import objectos.css.keyword.OverlineKeyword;
import objectos.css.keyword.PaddingBoxKeyword;
import objectos.css.keyword.PersianKeyword;
import objectos.css.keyword.PointerKeyword;
import objectos.css.keyword.PreKeyword;
import objectos.css.keyword.PreLineKeyword;
import objectos.css.keyword.PreWrapKeyword;
import objectos.css.keyword.ProgressBarKeyword;
import objectos.css.keyword.ProgressKeyword;
import objectos.css.keyword.PushButtonKeyword;
import objectos.css.keyword.RadioKeyword;
import objectos.css.keyword.RelativeKeyword;
import objectos.css.keyword.RepeatKeyword;
import objectos.css.keyword.RepeatXKeyword;
import objectos.css.keyword.RepeatYKeyword;
import objectos.css.keyword.RidgeKeyword;
import objectos.css.keyword.RightKeyword;
import objectos.css.keyword.RoundKeyword;
import objectos.css.keyword.RowKeyword;
import objectos.css.keyword.RowResizeKeyword;
import objectos.css.keyword.RowReverseKeyword;
import objectos.css.keyword.RubyBaseContainerKeyword;
import objectos.css.keyword.RubyBaseKeyword;
import objectos.css.keyword.RubyKeyword;
import objectos.css.keyword.RubyTextContainerKeyword;
import objectos.css.keyword.RubyTextKeyword;
import objectos.css.keyword.RunInKeyword;
import objectos.css.keyword.SResizeKeyword;
import objectos.css.keyword.SafeKeyword;
import objectos.css.keyword.SansSerifKeyword;
import objectos.css.keyword.ScaleDownKeyword;
import objectos.css.keyword.ScrollKeyword;
import objectos.css.keyword.SeResizeKeyword;
import objectos.css.keyword.SearchfieldKeyword;
import objectos.css.keyword.SelfEndKeyword;
import objectos.css.keyword.SelfStartKeyword;
import objectos.css.keyword.SeparateKeyword;
import objectos.css.keyword.SerifKeyword;
import objectos.css.keyword.SimpChineseFormalKeyword;
import objectos.css.keyword.SimpChineseInformalKeyword;
import objectos.css.keyword.SliderHorizontalKeyword;
import objectos.css.keyword.SmallCapsKeyword;
import objectos.css.keyword.SmallCaptionKeyword;
import objectos.css.keyword.SmallKeyword;
import objectos.css.keyword.SmallerKeyword;
import objectos.css.keyword.SolidKeyword;
import objectos.css.keyword.SpaceAroundKeyword;
import objectos.css.keyword.SpaceBetweenKeyword;
import objectos.css.keyword.SpaceEvenlyKeyword;
import objectos.css.keyword.SpaceKeyword;
import objectos.css.keyword.SpellingErrorKeyword;
import objectos.css.keyword.SquareButtonKeyword;
import objectos.css.keyword.SquareKeyword;
import objectos.css.keyword.StartKeyword;
import objectos.css.keyword.StaticKeyword;
import objectos.css.keyword.StatusBarKeyword;
import objectos.css.keyword.StickyKeyword;
import objectos.css.keyword.StretchKeyword;
import objectos.css.keyword.SubKeyword;
import objectos.css.keyword.SuperKeyword;
import objectos.css.keyword.SwResizeKeyword;
import objectos.css.keyword.TableCaptionKeyword;
import objectos.css.keyword.TableCellKeyword;
import objectos.css.keyword.TableColumnGroupKeyword;
import objectos.css.keyword.TableColumnKeyword;
import objectos.css.keyword.TableFooterGroupKeyword;
import objectos.css.keyword.TableHeaderGroupKeyword;
import objectos.css.keyword.TableKeyword;
import objectos.css.keyword.TableRowGroupKeyword;
import objectos.css.keyword.TableRowKeyword;
import objectos.css.keyword.TamilKeyword;
import objectos.css.keyword.TeluguKeyword;
import objectos.css.keyword.TextBottomKeyword;
import objectos.css.keyword.TextKeyword;
import objectos.css.keyword.TextTopKeyword;
import objectos.css.keyword.TextareaKeyword;
import objectos.css.keyword.TextfieldKeyword;
import objectos.css.keyword.ThaiKeyword;
import objectos.css.keyword.ThickKeyword;
import objectos.css.keyword.ThinKeyword;
import objectos.css.keyword.TibetanKeyword;
import objectos.css.keyword.TopKeyword;
import objectos.css.keyword.TradChineseFormalKeyword;
import objectos.css.keyword.TradChineseInformalKeyword;
import objectos.css.keyword.UnderlineKeyword;
import objectos.css.keyword.UnsafeKeyword;
import objectos.css.keyword.UnsetKeyword;
import objectos.css.keyword.UpperAlphaKeyword;
import objectos.css.keyword.UpperArmenianKeyword;
import objectos.css.keyword.UpperLatinKeyword;
import objectos.css.keyword.UpperRomanKeyword;
import objectos.css.keyword.UppercaseKeyword;
import objectos.css.keyword.VerticalKeyword;
import objectos.css.keyword.VerticalTextKeyword;
import objectos.css.keyword.VisibleKeyword;
import objectos.css.keyword.WResizeKeyword;
import objectos.css.keyword.WaitKeyword;
import objectos.css.keyword.WavyKeyword;
import objectos.css.keyword.WrapKeyword;
import objectos.css.keyword.WrapReverseKeyword;
import objectos.css.keyword.XLargeKeyword;
import objectos.css.keyword.XSmallKeyword;
import objectos.css.keyword.XxLargeKeyword;
import objectos.css.keyword.XxSmallKeyword;
import objectos.css.keyword.XxxLargeKeyword;
import objectos.css.keyword.ZoomInKeyword;
import objectos.css.keyword.ZoomOutKeyword;
import objectos.css.property.StandardPropertyName;
import objectos.css.select.PseudoClassSelector;
import objectos.css.select.PseudoElementSelector;
import objectos.css.select.TypeSelector;
import objectos.css.type.AlignContentValue;
import objectos.css.type.AlignItemsValue;
import objectos.css.type.AlignSelfValue;
import objectos.css.type.AngleType;
import objectos.css.type.AngleUnit;
import objectos.css.type.AppearanceValue;
import objectos.css.type.BackgroundAttachmentValue;
import objectos.css.type.BackgroundImageValue;
import objectos.css.type.BackgroundPositionValue;
import objectos.css.type.BackgroundRepeatArity1Value;
import objectos.css.type.BackgroundRepeatArity2Value;
import objectos.css.type.BackgroundSizeArity1Value;
import objectos.css.type.BackgroundSizeArity2Value;
import objectos.css.type.BackgroundValue;
import objectos.css.type.BaselinePosition;
import objectos.css.type.BorderCollapseValue;
import objectos.css.type.BorderRadiusValue;
import objectos.css.type.BorderShorthandValue;
import objectos.css.type.BottomValue;
import objectos.css.type.BoxSizingValue;
import objectos.css.type.BoxValue;
import objectos.css.type.ClearValue;
import objectos.css.type.Color;
import objectos.css.type.ColorType;
import objectos.css.type.ContentPosition;
import objectos.css.type.ContentPositionOrLeftOrRight;
import objectos.css.type.ContentValue;
import objectos.css.type.CursorValue;
import objectos.css.type.DisplayArity1Value;
import objectos.css.type.DisplayArity2Value;
import objectos.css.type.FlexArity1Value;
import objectos.css.type.FlexBasisValue;
import objectos.css.type.FlexDirectionValue;
import objectos.css.type.FlexFlowValue;
import objectos.css.type.FlexWrapValue;
import objectos.css.type.FloatValue;
import objectos.css.type.FontFamilyValue;
import objectos.css.type.FontSizeValue;
import objectos.css.type.FontStyleValue;
import objectos.css.type.FontVariantCss21Value;
import objectos.css.type.FontWeightValue;
import objectos.css.type.GlobalKeyword;
import objectos.css.type.HeightOrWidthValue;
import objectos.css.type.JustifyContentValue;
import objectos.css.type.JustifyItemsValue;
import objectos.css.type.JustifyLegacyValue;
import objectos.css.type.JustifySelfValue;
import objectos.css.type.LeftValue;
import objectos.css.type.LengthOrPercentageValue;
import objectos.css.type.LengthType;
import objectos.css.type.LengthUnit;
import objectos.css.type.LineHeightValue;
import objectos.css.type.LineStyleValue;
import objectos.css.type.LineWidthValue;
import objectos.css.type.ListStyleImageValue;
import objectos.css.type.ListStylePositionValue;
import objectos.css.type.ListStyleTypeValue;
import objectos.css.type.ListStyleValue;
import objectos.css.type.MarginWidthValue;
import objectos.css.type.MaxHeightOrWidthValue;
import objectos.css.type.NumberValue;
import objectos.css.type.ObjectFitValue;
import objectos.css.type.OutlineColorValue;
import objectos.css.type.OutlineStyleValue;
import objectos.css.type.OutlineValue;
import objectos.css.type.OutlineWidthValue;
import objectos.css.type.OverflowPosition;
import objectos.css.type.OverflowValue;
import objectos.css.type.PercentageType;
import objectos.css.type.PositionValue;
import objectos.css.type.ResizeValue;
import objectos.css.type.RightValue;
import objectos.css.type.SelfPosition;
import objectos.css.type.SelfPositionOrLeftOrRight;
import objectos.css.type.SystemFontValue;
import objectos.css.type.TabSizeValue;
import objectos.css.type.TextAlignValue;
import objectos.css.type.TextDecorationLineKind;
import objectos.css.type.TextDecorationLineValue;
import objectos.css.type.TextDecorationStyleValue;
import objectos.css.type.TextDecorationThicknessValue;
import objectos.css.type.TextDecorationValue;
import objectos.css.type.TextIndentOptionalValue;
import objectos.css.type.TextIndentValue;
import objectos.css.type.TextSizeAdjustValue;
import objectos.css.type.TextTransformValue;
import objectos.css.type.TopValue;
import objectos.css.type.TransformValue;
import objectos.css.type.Value;
import objectos.css.type.VerticalAlignValue;
import objectos.css.type.WhiteSpaceValue;
import objectos.css.type.ZIndexValue;
import objectos.css.type.Zero;

abstract class GeneratedStyleSheet {
  protected static final TypeSelector title = Css.title;

  protected static final TypeSelector option = Css.option;

  protected static final TypeSelector li = Css.li;

  protected static final TypeSelector a = Css.a;

  protected static final TypeSelector summary = Css.summary;

  protected static final TypeSelector b = Css.b;

  protected static final TypeSelector textarea = Css.textarea;

  protected static final TypeSelector g = Css.g;

  protected static final TypeSelector svg = Css.svg;

  protected static final TypeSelector table = Css.table;

  protected static final TypeSelector main = Css.main;

  protected static final TypeSelector template = Css.template;

  protected static final TypeSelector script = Css.script;

  protected static final TypeSelector p = Css.p;

  protected static final TypeSelector kbd = Css.kbd;

  protected static final TypeSelector tbody = Css.tbody;

  protected static final TypeSelector legend = Css.legend;

  protected static final TypeSelector progress = Css.progress;

  protected static final TypeSelector header = Css.header;

  protected static final TypeSelector hr = Css.hr;

  protected static final TypeSelector optgroup = Css.optgroup;

  protected static final TypeSelector samp = Css.samp;

  protected static final TypeSelector dd = Css.dd;

  protected static final TypeSelector dl = Css.dl;

  protected static final TypeSelector img = Css.img;

  protected static final TypeSelector strong = Css.strong;

  protected static final TypeSelector dt = Css.dt;

  protected static final TypeSelector defs = Css.defs;

  protected static final TypeSelector head = Css.head;

  protected static final TypeSelector span = Css.span;

  protected static final TypeSelector section = Css.section;

  protected static final TypeSelector pre = Css.pre;

  protected static final TypeSelector input = Css.input;

  protected static final TypeSelector path = Css.path;

  protected static final TypeSelector em = Css.em;

  protected static final TypeSelector menu = Css.menu;

  protected static final TypeSelector article = Css.article;

  protected static final TypeSelector blockquote = Css.blockquote;

  protected static final TypeSelector clipPath = Css.clipPath;

  protected static final TypeSelector small = Css.small;

  protected static final TypeSelector abbr = Css.abbr;

  protected static final TypeSelector code = Css.code;

  protected static final TypeSelector ol = Css.ol;

  protected static final TypeSelector div = Css.div;

  protected static final TypeSelector meta = Css.meta;

  protected static final TypeSelector td = Css.td;

  protected static final TypeSelector th = Css.th;

  protected static final TypeSelector select = Css.select;

  protected static final TypeSelector body = Css.body;

  protected static final TypeSelector link = Css.link;

  protected static final TypeSelector html = Css.html;

  protected static final TypeSelector fieldset = Css.fieldset;

  protected static final TypeSelector h1 = Css.h1;

  protected static final TypeSelector h2 = Css.h2;

  protected static final TypeSelector h3 = Css.h3;

  protected static final TypeSelector h4 = Css.h4;

  protected static final TypeSelector nav = Css.nav;

  protected static final TypeSelector h5 = Css.h5;

  protected static final TypeSelector h6 = Css.h6;

  protected static final TypeSelector sub = Css.sub;

  protected static final TypeSelector hgroup = Css.hgroup;

  protected static final TypeSelector details = Css.details;

  protected static final TypeSelector thead = Css.thead;

  protected static final TypeSelector button = Css.button;

  protected static final TypeSelector figure = Css.figure;

  protected static final TypeSelector label = Css.label;

  protected static final TypeSelector form = Css.form;

  protected static final TypeSelector tr = Css.tr;

  protected static final TypeSelector footer = Css.footer;

  protected static final TypeSelector style = Css.style;

  protected static final TypeSelector sup = Css.sup;

  protected static final TypeSelector ul = Css.ul;

  protected static final TypeSelector br = Css.br;

  protected static final PseudoClassSelector ACTIVE = Css.ACTIVE;

  protected static final PseudoClassSelector ANY_LINK = Css.ANY_LINK;

  protected static final PseudoClassSelector BLANK = Css.BLANK;

  protected static final PseudoClassSelector CHECKED = Css.CHECKED;

  protected static final PseudoClassSelector CURRENT = Css.CURRENT;

  protected static final PseudoClassSelector DEFAULT = Css.DEFAULT;

  protected static final PseudoClassSelector DEFINED = Css.DEFINED;

  protected static final PseudoClassSelector DISABLED = Css.DISABLED;

  protected static final PseudoClassSelector DROP = Css.DROP;

  protected static final PseudoClassSelector EMPTY = Css.EMPTY;

  protected static final PseudoClassSelector ENABLED = Css.ENABLED;

  protected static final PseudoClassSelector FIRST = Css.FIRST;

  protected static final PseudoClassSelector FIRST_CHILD = Css.FIRST_CHILD;

  protected static final PseudoClassSelector FIRST_OF_TYPE = Css.FIRST_OF_TYPE;

  protected static final PseudoClassSelector FULLSCREEN = Css.FULLSCREEN;

  protected static final PseudoClassSelector FUTURE = Css.FUTURE;

  protected static final PseudoClassSelector FOCUS = Css.FOCUS;

  protected static final PseudoClassSelector FOCUS_VISIBLE = Css.FOCUS_VISIBLE;

  protected static final PseudoClassSelector FOCUS_WITHIN = Css.FOCUS_WITHIN;

  protected static final PseudoClassSelector HOST = Css.HOST;

  protected static final PseudoClassSelector HOVER = Css.HOVER;

  protected static final PseudoClassSelector INDETERMINATE = Css.INDETERMINATE;

  protected static final PseudoClassSelector IN_RANGE = Css.IN_RANGE;

  protected static final PseudoClassSelector INVALID = Css.INVALID;

  protected static final PseudoClassSelector LAST_CHILD = Css.LAST_CHILD;

  protected static final PseudoClassSelector LAST_OF_TYPE = Css.LAST_OF_TYPE;

  protected static final PseudoClassSelector LEFT = Css.LEFT;

  protected static final PseudoClassSelector LINK = Css.LINK;

  protected static final PseudoClassSelector LOCAL_LINK = Css.LOCAL_LINK;

  protected static final PseudoClassSelector ONLY_CHILD = Css.ONLY_CHILD;

  protected static final PseudoClassSelector ONLY_OF_TYPE = Css.ONLY_OF_TYPE;

  protected static final PseudoClassSelector OPTIONAL = Css.OPTIONAL;

  protected static final PseudoClassSelector OUT_OF_RANGE = Css.OUT_OF_RANGE;

  protected static final PseudoClassSelector PAST = Css.PAST;

  protected static final PseudoClassSelector PLACEHOLDER_SHOWN = Css.PLACEHOLDER_SHOWN;

  protected static final PseudoClassSelector READ_ONLY = Css.READ_ONLY;

  protected static final PseudoClassSelector READ_WRITE = Css.READ_WRITE;

  protected static final PseudoClassSelector REQUIRED = Css.REQUIRED;

  protected static final PseudoClassSelector RIGHT = Css.RIGHT;

  protected static final PseudoClassSelector ROOT = Css.ROOT;

  protected static final PseudoClassSelector SCOPE = Css.SCOPE;

  protected static final PseudoClassSelector TARGET = Css.TARGET;

  protected static final PseudoClassSelector TARGET_WITHIN = Css.TARGET_WITHIN;

  protected static final PseudoClassSelector USER_INVALID = Css.USER_INVALID;

  protected static final PseudoClassSelector VALID = Css.VALID;

  protected static final PseudoClassSelector VISITED = Css.VISITED;

  protected static final PseudoClassSelector _MOZ_FOCUSRING = Css._MOZ_FOCUSRING;

  protected static final PseudoClassSelector _MOZ_UI_INVALID = Css._MOZ_UI_INVALID;

  protected static final PseudoElementSelector AFTER = Css.AFTER;

  protected static final PseudoElementSelector BACKDROP = Css.BACKDROP;

  protected static final PseudoElementSelector BEFORE = Css.BEFORE;

  protected static final PseudoElementSelector CUE = Css.CUE;

  protected static final PseudoElementSelector FIRST_LETTER = Css.FIRST_LETTER;

  protected static final PseudoElementSelector FIRST_LINE = Css.FIRST_LINE;

  protected static final PseudoElementSelector GRAMMAR_ERROR = Css.GRAMMAR_ERROR;

  protected static final PseudoElementSelector MARKER = Css.MARKER;

  protected static final PseudoElementSelector PLACEHOLDER = Css.PLACEHOLDER;

  protected static final PseudoElementSelector SELECTION = Css.SELECTION;

  protected static final PseudoElementSelector SPELLING_ERROR = Css.SPELLING_ERROR;

  protected static final PseudoElementSelector _MOZ_FOCUS_INNER = Css._MOZ_FOCUS_INNER;

  protected static final PseudoElementSelector _WEBKIT_INNER_SPIN_BUTTON = Css._WEBKIT_INNER_SPIN_BUTTON;

  protected static final PseudoElementSelector _WEBKIT_OUTER_SPIN_BUTTON = Css._WEBKIT_OUTER_SPIN_BUTTON;

  protected static final PseudoElementSelector _WEBKIT_SEARCH_DECORATION = Css._WEBKIT_SEARCH_DECORATION;

  protected static final PseudoElementSelector _WEBKIT_FILE_UPLOAD_BUTTON = Css._WEBKIT_FILE_UPLOAD_BUTTON;

  protected static final Color black = Color.black;

  protected static final Color silver = Color.silver;

  protected static final Color gray = Color.gray;

  protected static final Color white = Color.white;

  protected static final Color maroon = Color.maroon;

  protected static final Color red = Color.red;

  protected static final Color purple = Color.purple;

  protected static final Color fuchsia = Color.fuchsia;

  protected static final Color green = Color.green;

  protected static final Color lime = Color.lime;

  protected static final Color olive = Color.olive;

  protected static final Color yellow = Color.yellow;

  protected static final Color navy = Color.navy;

  protected static final Color blue = Color.blue;

  protected static final Color teal = Color.teal;

  protected static final Color aqua = Color.aqua;

  protected static final Color orange = Color.orange;

  protected static final Color aliceblue = Color.aliceblue;

  protected static final Color antiquewhite = Color.antiquewhite;

  protected static final Color aquamarine = Color.aquamarine;

  protected static final Color azure = Color.azure;

  protected static final Color beige = Color.beige;

  protected static final Color bisque = Color.bisque;

  protected static final Color blanchedalmond = Color.blanchedalmond;

  protected static final Color blueviolet = Color.blueviolet;

  protected static final Color brown = Color.brown;

  protected static final Color burlywood = Color.burlywood;

  protected static final Color cadetblue = Color.cadetblue;

  protected static final Color chartreuse = Color.chartreuse;

  protected static final Color chocolate = Color.chocolate;

  protected static final Color coral = Color.coral;

  protected static final Color cornflowerblue = Color.cornflowerblue;

  protected static final Color cornsilk = Color.cornsilk;

  protected static final Color crimson = Color.crimson;

  protected static final Color cyan = Color.cyan;

  protected static final Color darkblue = Color.darkblue;

  protected static final Color darkcyan = Color.darkcyan;

  protected static final Color darkgoldenrod = Color.darkgoldenrod;

  protected static final Color darkgray = Color.darkgray;

  protected static final Color darkgreen = Color.darkgreen;

  protected static final Color darkgrey = Color.darkgrey;

  protected static final Color darkkhaki = Color.darkkhaki;

  protected static final Color darkmagenta = Color.darkmagenta;

  protected static final Color darkolivegreen = Color.darkolivegreen;

  protected static final Color darkorange = Color.darkorange;

  protected static final Color darkorchid = Color.darkorchid;

  protected static final Color darkred = Color.darkred;

  protected static final Color darksalmon = Color.darksalmon;

  protected static final Color darkseagreen = Color.darkseagreen;

  protected static final Color darkslateblue = Color.darkslateblue;

  protected static final Color darkslategray = Color.darkslategray;

  protected static final Color darkslategrey = Color.darkslategrey;

  protected static final Color darkturquoise = Color.darkturquoise;

  protected static final Color darkviolet = Color.darkviolet;

  protected static final Color deeppink = Color.deeppink;

  protected static final Color deepskyblue = Color.deepskyblue;

  protected static final Color dimgray = Color.dimgray;

  protected static final Color dimgrey = Color.dimgrey;

  protected static final Color dodgerblue = Color.dodgerblue;

  protected static final Color firebrick = Color.firebrick;

  protected static final Color floralwhite = Color.floralwhite;

  protected static final Color forestgreen = Color.forestgreen;

  protected static final Color gainsboro = Color.gainsboro;

  protected static final Color ghostwhite = Color.ghostwhite;

  protected static final Color gold = Color.gold;

  protected static final Color goldenrod = Color.goldenrod;

  protected static final Color greenyellow = Color.greenyellow;

  protected static final Color grey = Color.grey;

  protected static final Color honeydew = Color.honeydew;

  protected static final Color hotpink = Color.hotpink;

  protected static final Color indianred = Color.indianred;

  protected static final Color indigo = Color.indigo;

  protected static final Color ivory = Color.ivory;

  protected static final Color khaki = Color.khaki;

  protected static final Color lavender = Color.lavender;

  protected static final Color lavenderblush = Color.lavenderblush;

  protected static final Color lawngreen = Color.lawngreen;

  protected static final Color lemonchiffon = Color.lemonchiffon;

  protected static final Color lightblue = Color.lightblue;

  protected static final Color lightcoral = Color.lightcoral;

  protected static final Color lightcyan = Color.lightcyan;

  protected static final Color lightgoldenrodyellow = Color.lightgoldenrodyellow;

  protected static final Color lightgray = Color.lightgray;

  protected static final Color lightgreen = Color.lightgreen;

  protected static final Color lightgrey = Color.lightgrey;

  protected static final Color lightpink = Color.lightpink;

  protected static final Color lightsalmon = Color.lightsalmon;

  protected static final Color lightseagreen = Color.lightseagreen;

  protected static final Color lightskyblue = Color.lightskyblue;

  protected static final Color lightslategray = Color.lightslategray;

  protected static final Color lightslategrey = Color.lightslategrey;

  protected static final Color lightsteelblue = Color.lightsteelblue;

  protected static final Color lightyellow = Color.lightyellow;

  protected static final Color limegreen = Color.limegreen;

  protected static final Color linen = Color.linen;

  protected static final Color magenta = Color.magenta;

  protected static final Color mediumaquamarine = Color.mediumaquamarine;

  protected static final Color mediumblue = Color.mediumblue;

  protected static final Color mediumorchid = Color.mediumorchid;

  protected static final Color mediumpurple = Color.mediumpurple;

  protected static final Color mediumseagreen = Color.mediumseagreen;

  protected static final Color mediumslateblue = Color.mediumslateblue;

  protected static final Color mediumspringgreen = Color.mediumspringgreen;

  protected static final Color mediumturquoise = Color.mediumturquoise;

  protected static final Color mediumvioletred = Color.mediumvioletred;

  protected static final Color midnightblue = Color.midnightblue;

  protected static final Color mintcream = Color.mintcream;

  protected static final Color mistyrose = Color.mistyrose;

  protected static final Color moccasin = Color.moccasin;

  protected static final Color navajowhite = Color.navajowhite;

  protected static final Color oldlace = Color.oldlace;

  protected static final Color olivedrab = Color.olivedrab;

  protected static final Color orangered = Color.orangered;

  protected static final Color orchid = Color.orchid;

  protected static final Color palegoldenrod = Color.palegoldenrod;

  protected static final Color palegreen = Color.palegreen;

  protected static final Color paleturquoise = Color.paleturquoise;

  protected static final Color palevioletred = Color.palevioletred;

  protected static final Color papayawhip = Color.papayawhip;

  protected static final Color peachpuff = Color.peachpuff;

  protected static final Color peru = Color.peru;

  protected static final Color pink = Color.pink;

  protected static final Color plum = Color.plum;

  protected static final Color powderblue = Color.powderblue;

  protected static final Color rosybrown = Color.rosybrown;

  protected static final Color royalblue = Color.royalblue;

  protected static final Color saddlebrown = Color.saddlebrown;

  protected static final Color salmon = Color.salmon;

  protected static final Color sandybrown = Color.sandybrown;

  protected static final Color seagreen = Color.seagreen;

  protected static final Color seashell = Color.seashell;

  protected static final Color sienna = Color.sienna;

  protected static final Color skyblue = Color.skyblue;

  protected static final Color slateblue = Color.slateblue;

  protected static final Color slategray = Color.slategray;

  protected static final Color slategrey = Color.slategrey;

  protected static final Color snow = Color.snow;

  protected static final Color springgreen = Color.springgreen;

  protected static final Color steelblue = Color.steelblue;

  protected static final Color tan = Color.tan;

  protected static final Color thistle = Color.thistle;

  protected static final Color tomato = Color.tomato;

  protected static final Color turquoise = Color.turquoise;

  protected static final Color violet = Color.violet;

  protected static final Color wheat = Color.wheat;

  protected static final Color whitesmoke = Color.whitesmoke;

  protected static final Color yellowgreen = Color.yellowgreen;

  protected static final Color rebeccapurple = Color.rebeccapurple;

  protected static final Color currentcolor = Color.currentcolor;

  protected static final Color transparent = Color.transparent;

  protected static final Color ActiveBorder = Color.ActiveBorder;

  protected static final Color ActiveCaption = Color.ActiveCaption;

  protected static final Color AppWorkspace = Color.AppWorkspace;

  protected static final Color Background = Color.Background;

  protected static final Color ButtonFace = Color.ButtonFace;

  protected static final Color ButtonHighlight = Color.ButtonHighlight;

  protected static final Color ButtonShadow = Color.ButtonShadow;

  protected static final Color ButtonText = Color.ButtonText;

  protected static final Color CaptionText = Color.CaptionText;

  protected static final Color GrayText = Color.GrayText;

  protected static final Color Highlight = Color.Highlight;

  protected static final Color HighlightText = Color.HighlightText;

  protected static final Color InactiveBorder = Color.InactiveBorder;

  protected static final Color InactiveCaption = Color.InactiveCaption;

  protected static final Color InactiveCaptionText = Color.InactiveCaptionText;

  protected static final Color InfoBackground = Color.InfoBackground;

  protected static final Color InfoText = Color.InfoText;

  protected static final Color MenuText = Color.MenuText;

  protected static final Color Scrollbar = Color.Scrollbar;

  protected static final Color ThreeDDarkShadow = Color.ThreeDDarkShadow;

  protected static final Color ThreeDFace = Color.ThreeDFace;

  protected static final Color ThreeDHighlight = Color.ThreeDHighlight;

  protected static final Color ThreeDLightShadow = Color.ThreeDLightShadow;

  protected static final Color ThreeDShadow = Color.ThreeDShadow;

  protected static final Color Window = Color.Window;

  protected static final Color WindowFrame = Color.WindowFrame;

  protected static final Color WindowText = Color.WindowText;

  protected static final MozArabicIndicKeyword mozArabicIndic = Keywords.mozArabicIndic;

  protected static final MozBengaliKeyword mozBengali = Keywords.mozBengali;

  protected static final MozCjkEarthlyBranchKeyword mozCjkEarthlyBranch = Keywords.mozCjkEarthlyBranch;

  protected static final MozCjkHeavenlyStemKeyword mozCjkHeavenlyStem = Keywords.mozCjkHeavenlyStem;

  protected static final MozDevanagariKeyword mozDevanagari = Keywords.mozDevanagari;

  protected static final MozGujaratiKeyword mozGujarati = Keywords.mozGujarati;

  protected static final MozGurmukhiKeyword mozGurmukhi = Keywords.mozGurmukhi;

  protected static final MozKannadaKeyword mozKannada = Keywords.mozKannada;

  protected static final MozKhmerKeyword mozKhmer = Keywords.mozKhmer;

  protected static final MozLaoKeyword mozLao = Keywords.mozLao;

  protected static final MozMalayalamKeyword mozMalayalam = Keywords.mozMalayalam;

  protected static final MozMyanmarKeyword mozMyanmar = Keywords.mozMyanmar;

  protected static final MozOriyaKeyword mozOriya = Keywords.mozOriya;

  protected static final MozPersianKeyword mozPersian = Keywords.mozPersian;

  protected static final MozTamilKeyword mozTamil = Keywords.mozTamil;

  protected static final MozTeluguKeyword mozTelugu = Keywords.mozTelugu;

  protected static final MozThaiKeyword mozThai = Keywords.mozThai;

  protected static final AbsoluteKeyword absolute = Keywords.absolute;

  protected static final AliasKeyword alias = Keywords.alias;

  protected static final AllScrollKeyword allScroll = Keywords.allScroll;

  protected static final ArabicIndicKeyword arabicIndic = Keywords.arabicIndic;

  protected static final ArmenianKeyword armenian = Keywords.armenian;

  protected static final AutoKeyword auto = Keywords.auto;

  protected static final BaselineKeyword baseline = Keywords.baseline;

  protected static final BengaliKeyword bengali = Keywords.bengali;

  protected static final BlinkKeyword blink = Keywords.blink;

  protected static final BlockKeyword block = Keywords.block;

  protected static final BoldKeyword bold = Keywords.bold;

  protected static final BolderKeyword bolder = Keywords.bolder;

  protected static final BorderBoxKeyword borderBox = Keywords.borderBox;

  protected static final BothKeyword both = Keywords.both;

  protected static final BottomKeyword bottom = Keywords.bottom;

  protected static final BreakSpacesKeyword breakSpaces = Keywords.breakSpaces;

  protected static final ButtonKeyword buttonKw = Keywords.buttonKw;

  protected static final ButtonBevelKeyword buttonBevel = Keywords.buttonBevel;

  protected static final CambodianKeyword cambodian = Keywords.cambodian;

  protected static final CapitalizeKeyword capitalize = Keywords.capitalize;

  protected static final CaptionKeyword caption = Keywords.caption;

  protected static final CellKeyword cell = Keywords.cell;

  protected static final CenterKeyword center = Keywords.center;

  protected static final CheckboxKeyword checkbox = Keywords.checkbox;

  protected static final CircleKeyword circle = Keywords.circle;

  protected static final CjkDecimalKeyword cjkDecimal = Keywords.cjkDecimal;

  protected static final CjkEarthlyBranchKeyword cjkEarthlyBranch = Keywords.cjkEarthlyBranch;

  protected static final CjkHeavenlyStemKeyword cjkHeavenlyStem = Keywords.cjkHeavenlyStem;

  protected static final CjkIdeographicKeyword cjkIdeographic = Keywords.cjkIdeographic;

  protected static final ClipKeyword clip = Keywords.clip;

  protected static final CloseQuoteKeyword closeQuote = Keywords.closeQuote;

  protected static final ColResizeKeyword colResize = Keywords.colResize;

  protected static final CollapseKeyword collapse = Keywords.collapse;

  protected static final ColumnKeyword column = Keywords.column;

  protected static final ColumnReverseKeyword columnReverse = Keywords.columnReverse;

  protected static final ContainKeyword contain = Keywords.contain;

  protected static final ContentKeyword content = Keywords.content;

  protected static final ContentBoxKeyword contentBox = Keywords.contentBox;

  protected static final ContentsKeyword contents = Keywords.contents;

  protected static final ContextMenuKeyword contextMenu = Keywords.contextMenu;

  protected static final CopyKeyword copy = Keywords.copy;

  protected static final CoverKeyword cover = Keywords.cover;

  protected static final CrosshairKeyword crosshair = Keywords.crosshair;

  protected static final CursiveKeyword cursive = Keywords.cursive;

  protected static final DashedKeyword dashed = Keywords.dashed;

  protected static final DecimalKeyword decimal = Keywords.decimal;

  protected static final DecimalLeadingZeroKeyword decimalLeadingZero = Keywords.decimalLeadingZero;

  protected static final DefaultKeyword defaultKw = Keywords.defaultKw;

  protected static final DevanagariKeyword devanagari = Keywords.devanagari;

  protected static final DiscKeyword disc = Keywords.disc;

  protected static final DisclosureClosedKeyword disclosureClosed = Keywords.disclosureClosed;

  protected static final DisclosureOpenKeyword disclosureOpen = Keywords.disclosureOpen;

  protected static final DottedKeyword dotted = Keywords.dotted;

  protected static final DoubleKeyword doubleKw = Keywords.doubleKw;

  protected static final EResizeKeyword eResize = Keywords.eResize;

  protected static final EachLineKeyword eachLine = Keywords.eachLine;

  protected static final EndKeyword end = Keywords.end;

  protected static final EthiopicNumericKeyword ethiopicNumeric = Keywords.ethiopicNumeric;

  protected static final EwResizeKeyword ewResize = Keywords.ewResize;

  protected static final FantasyKeyword fantasy = Keywords.fantasy;

  protected static final FillKeyword fill = Keywords.fill;

  protected static final FirstKeyword first = Keywords.first;

  protected static final FixedKeyword fixed = Keywords.fixed;

  protected static final FlexKeyword flex = Keywords.flex;

  protected static final FlexEndKeyword flexEnd = Keywords.flexEnd;

  protected static final FlexStartKeyword flexStart = Keywords.flexStart;

  protected static final FlowKeyword flow = Keywords.flow;

  protected static final FlowRootKeyword flowRoot = Keywords.flowRoot;

  protected static final FromFontKeyword fromFont = Keywords.fromFont;

  protected static final FullSizeKanaKeyword fullSizeKana = Keywords.fullSizeKana;

  protected static final FullWidthKeyword fullWidth = Keywords.fullWidth;

  protected static final GeorgianKeyword georgian = Keywords.georgian;

  protected static final GrabKeyword grab = Keywords.grab;

  protected static final GrabbingKeyword grabbing = Keywords.grabbing;

  protected static final GrammarErrorKeyword grammarError = Keywords.grammarError;

  protected static final GridKeyword grid = Keywords.grid;

  protected static final GrooveKeyword groove = Keywords.groove;

  protected static final GujaratiKeyword gujarati = Keywords.gujarati;

  protected static final GurmukhiKeyword gurmukhi = Keywords.gurmukhi;

  protected static final HangingKeyword hanging = Keywords.hanging;

  protected static final HebrewKeyword hebrew = Keywords.hebrew;

  protected static final HelpKeyword help = Keywords.help;

  protected static final HiddenKeyword hidden = Keywords.hidden;

  protected static final HiraganaKeyword hiragana = Keywords.hiragana;

  protected static final HiraganaIrohaKeyword hiraganaIroha = Keywords.hiraganaIroha;

  protected static final HorizontalKeyword horizontal = Keywords.horizontal;

  protected static final IconKeyword icon = Keywords.icon;

  protected static final InheritKeyword inherit = Keywords.inherit;

  protected static final InitialKeyword initial = Keywords.initial;

  protected static final InlineKeyword inline = Keywords.inline;

  protected static final InlineBlockKeyword inlineBlock = Keywords.inlineBlock;

  protected static final InlineEndKeyword inlineEnd = Keywords.inlineEnd;

  protected static final InlineFlexKeyword inlineFlex = Keywords.inlineFlex;

  protected static final InlineGridKeyword inlineGrid = Keywords.inlineGrid;

  protected static final InlineStartKeyword inlineStart = Keywords.inlineStart;

  protected static final InlineTableKeyword inlineTable = Keywords.inlineTable;

  protected static final InsetKeyword inset = Keywords.inset;

  protected static final InsideKeyword inside = Keywords.inside;

  protected static final InvertKeyword invert = Keywords.invert;

  protected static final ItalicKeyword italic = Keywords.italic;

  protected static final JapaneseFormalKeyword japaneseFormal = Keywords.japaneseFormal;

  protected static final JapaneseInformalKeyword japaneseInformal = Keywords.japaneseInformal;

  protected static final JustifyKeyword justify = Keywords.justify;

  protected static final KannadaKeyword kannada = Keywords.kannada;

  protected static final KatakanaKeyword katakana = Keywords.katakana;

  protected static final KatakanaIrohaKeyword katakanaIroha = Keywords.katakanaIroha;

  protected static final KhmerKeyword khmer = Keywords.khmer;

  protected static final KoreanHangulFormalKeyword koreanHangulFormal = Keywords.koreanHangulFormal;

  protected static final KoreanHanjaFormalKeyword koreanHanjaFormal = Keywords.koreanHanjaFormal;

  protected static final KoreanHanjaInformalKeyword koreanHanjaInformal = Keywords.koreanHanjaInformal;

  protected static final LaoKeyword lao = Keywords.lao;

  protected static final LargeKeyword large = Keywords.large;

  protected static final LargerKeyword larger = Keywords.larger;

  protected static final LastKeyword last = Keywords.last;

  protected static final LeftKeyword left = Keywords.left;

  protected static final LegacyKeyword legacy = Keywords.legacy;

  protected static final LighterKeyword lighter = Keywords.lighter;

  protected static final LineThroughKeyword lineThrough = Keywords.lineThrough;

  protected static final ListItemKeyword listItem = Keywords.listItem;

  protected static final ListboxKeyword listbox = Keywords.listbox;

  protected static final LocalKeyword local = Keywords.local;

  protected static final LowerAlphaKeyword lowerAlpha = Keywords.lowerAlpha;

  protected static final LowerArmenianKeyword lowerArmenian = Keywords.lowerArmenian;

  protected static final LowerGreekKeyword lowerGreek = Keywords.lowerGreek;

  protected static final LowerLatinKeyword lowerLatin = Keywords.lowerLatin;

  protected static final LowerRomanKeyword lowerRoman = Keywords.lowerRoman;

  protected static final LowercaseKeyword lowercase = Keywords.lowercase;

  protected static final MalayalamKeyword malayalam = Keywords.malayalam;

  protected static final MatchParentKeyword matchParent = Keywords.matchParent;

  protected static final MaxContentKeyword maxContent = Keywords.maxContent;

  protected static final MediumKeyword medium = Keywords.medium;

  protected static final MenuKeyword menuKw = Keywords.menuKw;

  protected static final MenulistKeyword menulist = Keywords.menulist;

  protected static final MenulistButtonKeyword menulistButton = Keywords.menulistButton;

  protected static final MessageBoxKeyword messageBox = Keywords.messageBox;

  protected static final MeterKeyword meter = Keywords.meter;

  protected static final MiddleKeyword middle = Keywords.middle;

  protected static final MinContentKeyword minContent = Keywords.minContent;

  protected static final MongolianKeyword mongolian = Keywords.mongolian;

  protected static final MonospaceKeyword monospace = Keywords.monospace;

  protected static final MoveKeyword move = Keywords.move;

  protected static final MyanmarKeyword myanmar = Keywords.myanmar;

  protected static final NResizeKeyword nResize = Keywords.nResize;

  protected static final NeResizeKeyword neResize = Keywords.neResize;

  protected static final NeswResizeKeyword neswResize = Keywords.neswResize;

  protected static final NoCloseQuoteKeyword noCloseQuote = Keywords.noCloseQuote;

  protected static final NoDropKeyword noDrop = Keywords.noDrop;

  protected static final NoOpenQuoteKeyword noOpenQuote = Keywords.noOpenQuote;

  protected static final NoRepeatKeyword noRepeat = Keywords.noRepeat;

  protected static final NoneKeyword none = Keywords.none;

  protected static final NormalKeyword normal = Keywords.normal;

  protected static final NotAllowedKeyword notAllowed = Keywords.notAllowed;

  protected static final NowrapKeyword nowrap = Keywords.nowrap;

  protected static final NsResizeKeyword nsResize = Keywords.nsResize;

  protected static final NwResizeKeyword nwResize = Keywords.nwResize;

  protected static final NwseResizeKeyword nwseResize = Keywords.nwseResize;

  protected static final ObliqueKeyword oblique = Keywords.oblique;

  protected static final OpenQuoteKeyword openQuote = Keywords.openQuote;

  protected static final OriyaKeyword oriya = Keywords.oriya;

  protected static final OutsetKeyword outset = Keywords.outset;

  protected static final OutsideKeyword outside = Keywords.outside;

  protected static final OverlineKeyword overline = Keywords.overline;

  protected static final PaddingBoxKeyword paddingBox = Keywords.paddingBox;

  protected static final PersianKeyword persian = Keywords.persian;

  protected static final PointerKeyword pointer = Keywords.pointer;

  protected static final PreKeyword preKw = Keywords.preKw;

  protected static final PreLineKeyword preLine = Keywords.preLine;

  protected static final PreWrapKeyword preWrap = Keywords.preWrap;

  protected static final ProgressKeyword progressKw = Keywords.progressKw;

  protected static final ProgressBarKeyword progressBar = Keywords.progressBar;

  protected static final PushButtonKeyword pushButton = Keywords.pushButton;

  protected static final RadioKeyword radio = Keywords.radio;

  protected static final RelativeKeyword relative = Keywords.relative;

  protected static final RepeatKeyword repeat = Keywords.repeat;

  protected static final RepeatXKeyword repeatX = Keywords.repeatX;

  protected static final RepeatYKeyword repeatY = Keywords.repeatY;

  protected static final RidgeKeyword ridge = Keywords.ridge;

  protected static final RightKeyword right = Keywords.right;

  protected static final RoundKeyword round = Keywords.round;

  protected static final RowKeyword row = Keywords.row;

  protected static final RowResizeKeyword rowResize = Keywords.rowResize;

  protected static final RowReverseKeyword rowReverse = Keywords.rowReverse;

  protected static final RubyKeyword ruby = Keywords.ruby;

  protected static final RubyBaseKeyword rubyBase = Keywords.rubyBase;

  protected static final RubyBaseContainerKeyword rubyBaseContainer = Keywords.rubyBaseContainer;

  protected static final RubyTextKeyword rubyText = Keywords.rubyText;

  protected static final RubyTextContainerKeyword rubyTextContainer = Keywords.rubyTextContainer;

  protected static final RunInKeyword runIn = Keywords.runIn;

  protected static final SResizeKeyword sResize = Keywords.sResize;

  protected static final SafeKeyword safe = Keywords.safe;

  protected static final SansSerifKeyword sansSerif = Keywords.sansSerif;

  protected static final ScaleDownKeyword scaleDown = Keywords.scaleDown;

  protected static final ScrollKeyword scroll = Keywords.scroll;

  protected static final SeResizeKeyword seResize = Keywords.seResize;

  protected static final SearchfieldKeyword searchfield = Keywords.searchfield;

  protected static final SelfEndKeyword selfEnd = Keywords.selfEnd;

  protected static final SelfStartKeyword selfStart = Keywords.selfStart;

  protected static final SeparateKeyword separate = Keywords.separate;

  protected static final SerifKeyword serif = Keywords.serif;

  protected static final SimpChineseFormalKeyword simpChineseFormal = Keywords.simpChineseFormal;

  protected static final SimpChineseInformalKeyword simpChineseInformal = Keywords.simpChineseInformal;

  protected static final SliderHorizontalKeyword sliderHorizontal = Keywords.sliderHorizontal;

  protected static final SmallKeyword smallKw = Keywords.smallKw;

  protected static final SmallCapsKeyword smallCaps = Keywords.smallCaps;

  protected static final SmallCaptionKeyword smallCaption = Keywords.smallCaption;

  protected static final SmallerKeyword smaller = Keywords.smaller;

  protected static final SolidKeyword solid = Keywords.solid;

  protected static final SpaceKeyword space = Keywords.space;

  protected static final SpaceAroundKeyword spaceAround = Keywords.spaceAround;

  protected static final SpaceBetweenKeyword spaceBetween = Keywords.spaceBetween;

  protected static final SpaceEvenlyKeyword spaceEvenly = Keywords.spaceEvenly;

  protected static final SpellingErrorKeyword spellingError = Keywords.spellingError;

  protected static final SquareKeyword square = Keywords.square;

  protected static final SquareButtonKeyword squareButton = Keywords.squareButton;

  protected static final StartKeyword start = Keywords.start;

  protected static final StaticKeyword staticKw = Keywords.staticKw;

  protected static final StatusBarKeyword statusBar = Keywords.statusBar;

  protected static final StickyKeyword sticky = Keywords.sticky;

  protected static final StretchKeyword stretch = Keywords.stretch;

  protected static final SubKeyword subKw = Keywords.subKw;

  protected static final SuperKeyword superKw = Keywords.superKw;

  protected static final SwResizeKeyword swResize = Keywords.swResize;

  protected static final TableKeyword tableKw = Keywords.tableKw;

  protected static final TableCaptionKeyword tableCaption = Keywords.tableCaption;

  protected static final TableCellKeyword tableCell = Keywords.tableCell;

  protected static final TableColumnKeyword tableColumn = Keywords.tableColumn;

  protected static final TableColumnGroupKeyword tableColumnGroup = Keywords.tableColumnGroup;

  protected static final TableFooterGroupKeyword tableFooterGroup = Keywords.tableFooterGroup;

  protected static final TableHeaderGroupKeyword tableHeaderGroup = Keywords.tableHeaderGroup;

  protected static final TableRowKeyword tableRow = Keywords.tableRow;

  protected static final TableRowGroupKeyword tableRowGroup = Keywords.tableRowGroup;

  protected static final TamilKeyword tamil = Keywords.tamil;

  protected static final TeluguKeyword telugu = Keywords.telugu;

  protected static final TextKeyword text = Keywords.text;

  protected static final TextBottomKeyword textBottom = Keywords.textBottom;

  protected static final TextTopKeyword textTop = Keywords.textTop;

  protected static final TextareaKeyword textareaKw = Keywords.textareaKw;

  protected static final TextfieldKeyword textfield = Keywords.textfield;

  protected static final ThaiKeyword thai = Keywords.thai;

  protected static final ThickKeyword thick = Keywords.thick;

  protected static final ThinKeyword thin = Keywords.thin;

  protected static final TibetanKeyword tibetan = Keywords.tibetan;

  protected static final TopKeyword top = Keywords.top;

  protected static final TradChineseFormalKeyword tradChineseFormal = Keywords.tradChineseFormal;

  protected static final TradChineseInformalKeyword tradChineseInformal = Keywords.tradChineseInformal;

  protected static final UnderlineKeyword underline = Keywords.underline;

  protected static final UnsafeKeyword unsafe = Keywords.unsafe;

  protected static final UnsetKeyword unset = Keywords.unset;

  protected static final UpperAlphaKeyword upperAlpha = Keywords.upperAlpha;

  protected static final UpperArmenianKeyword upperArmenian = Keywords.upperArmenian;

  protected static final UpperLatinKeyword upperLatin = Keywords.upperLatin;

  protected static final UpperRomanKeyword upperRoman = Keywords.upperRoman;

  protected static final UppercaseKeyword uppercase = Keywords.uppercase;

  protected static final VerticalKeyword vertical = Keywords.vertical;

  protected static final VerticalTextKeyword verticalText = Keywords.verticalText;

  protected static final VisibleKeyword visible = Keywords.visible;

  protected static final WResizeKeyword wResize = Keywords.wResize;

  protected static final WaitKeyword wait = Keywords.wait;

  protected static final WavyKeyword wavy = Keywords.wavy;

  protected static final WrapKeyword wrap = Keywords.wrap;

  protected static final WrapReverseKeyword wrapReverse = Keywords.wrapReverse;

  protected static final XLargeKeyword xLarge = Keywords.xLarge;

  protected static final XSmallKeyword xSmall = Keywords.xSmall;

  protected static final XxLargeKeyword xxLarge = Keywords.xxLarge;

  protected static final XxSmallKeyword xxSmall = Keywords.xxSmall;

  protected static final XxxLargeKeyword xxxLarge = Keywords.xxxLarge;

  protected static final ZoomInKeyword zoomIn = Keywords.zoomIn;

  protected static final ZoomOutKeyword zoomOut = Keywords.zoomOut;

  GeneratedStyleSheet() {}

  protected final AngleType deg(double value) {
    return getAngle(AngleUnit.DEG, value);
  }

  protected final AngleType deg(int value) {
    return getAngle(AngleUnit.DEG, value);
  }

  protected final AngleType grad(double value) {
    return getAngle(AngleUnit.GRAD, value);
  }

  protected final AngleType grad(int value) {
    return getAngle(AngleUnit.GRAD, value);
  }

  protected final AngleType rad(double value) {
    return getAngle(AngleUnit.RAD, value);
  }

  protected final AngleType rad(int value) {
    return getAngle(AngleUnit.RAD, value);
  }

  protected final AngleType turn(double value) {
    return getAngle(AngleUnit.TURN, value);
  }

  protected final AngleType turn(int value) {
    return getAngle(AngleUnit.TURN, value);
  }

  protected final LengthType em(double value) {
    return getLength(LengthUnit.EM, value);
  }

  protected final LengthType em(int value) {
    return getLength(LengthUnit.EM, value);
  }

  protected final LengthType ex(double value) {
    return getLength(LengthUnit.EX, value);
  }

  protected final LengthType ex(int value) {
    return getLength(LengthUnit.EX, value);
  }

  protected final LengthType ch(double value) {
    return getLength(LengthUnit.CH, value);
  }

  protected final LengthType ch(int value) {
    return getLength(LengthUnit.CH, value);
  }

  protected final LengthType rem(double value) {
    return getLength(LengthUnit.REM, value);
  }

  protected final LengthType rem(int value) {
    return getLength(LengthUnit.REM, value);
  }

  protected final LengthType vw(double value) {
    return getLength(LengthUnit.VW, value);
  }

  protected final LengthType vw(int value) {
    return getLength(LengthUnit.VW, value);
  }

  protected final LengthType vh(double value) {
    return getLength(LengthUnit.VH, value);
  }

  protected final LengthType vh(int value) {
    return getLength(LengthUnit.VH, value);
  }

  protected final LengthType vmin(double value) {
    return getLength(LengthUnit.VMIN, value);
  }

  protected final LengthType vmin(int value) {
    return getLength(LengthUnit.VMIN, value);
  }

  protected final LengthType vmax(double value) {
    return getLength(LengthUnit.VMAX, value);
  }

  protected final LengthType vmax(int value) {
    return getLength(LengthUnit.VMAX, value);
  }

  protected final LengthType cm(double value) {
    return getLength(LengthUnit.CM, value);
  }

  protected final LengthType cm(int value) {
    return getLength(LengthUnit.CM, value);
  }

  protected final LengthType mm(double value) {
    return getLength(LengthUnit.MM, value);
  }

  protected final LengthType mm(int value) {
    return getLength(LengthUnit.MM, value);
  }

  protected final LengthType q(double value) {
    return getLength(LengthUnit.Q, value);
  }

  protected final LengthType q(int value) {
    return getLength(LengthUnit.Q, value);
  }

  protected final LengthType in(double value) {
    return getLength(LengthUnit.IN, value);
  }

  protected final LengthType in(int value) {
    return getLength(LengthUnit.IN, value);
  }

  protected final LengthType pt(double value) {
    return getLength(LengthUnit.PT, value);
  }

  protected final LengthType pt(int value) {
    return getLength(LengthUnit.PT, value);
  }

  protected final LengthType pc(double value) {
    return getLength(LengthUnit.PC, value);
  }

  protected final LengthType pc(int value) {
    return getLength(LengthUnit.PC, value);
  }

  protected final LengthType px(double value) {
    return getLength(LengthUnit.PX, value);
  }

  protected final LengthType px(int value) {
    return getLength(LengthUnit.PX, value);
  }

  protected final AlignContentDeclaration alignContent(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.ALIGN_CONTENT, value);
  }

  protected final AlignContentDeclaration alignContent(AlignContentValue value) {
    return addDeclaration(StandardPropertyName.ALIGN_CONTENT, value);
  }

  protected final AlignContentDeclaration alignContent(BaselinePosition firstOrLast, BaselineKeyword baseline) {
    return addDeclaration(StandardPropertyName.ALIGN_CONTENT, firstOrLast, baseline);
  }

  protected final AlignContentDeclaration alignContent(OverflowPosition safeOrUnsafe, ContentPosition position) {
    return addDeclaration(StandardPropertyName.ALIGN_CONTENT, safeOrUnsafe, position);
  }

  protected final AlignItemsDeclaration alignItems(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.ALIGN_ITEMS, value);
  }

  protected final AlignItemsDeclaration alignItems(AlignItemsValue value) {
    return addDeclaration(StandardPropertyName.ALIGN_ITEMS, value);
  }

  protected final AlignItemsDeclaration alignItems(BaselinePosition firstOrLast, BaselineKeyword baseline) {
    return addDeclaration(StandardPropertyName.ALIGN_ITEMS, firstOrLast, baseline);
  }

  protected final AlignItemsDeclaration alignItems(OverflowPosition safeOrUnsafe, SelfPosition position) {
    return addDeclaration(StandardPropertyName.ALIGN_ITEMS, safeOrUnsafe, position);
  }

  protected final AlignSelfDeclaration alignSelf(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.ALIGN_SELF, value);
  }

  protected final AlignSelfDeclaration alignSelf(AlignSelfValue value) {
    return addDeclaration(StandardPropertyName.ALIGN_SELF, value);
  }

  protected final AlignSelfDeclaration alignSelf(BaselinePosition firstOrLast, BaselineKeyword baseline) {
    return addDeclaration(StandardPropertyName.ALIGN_SELF, firstOrLast, baseline);
  }

  protected final AlignSelfDeclaration alignSelf(OverflowPosition safeOrUnsafe, SelfPosition position) {
    return addDeclaration(StandardPropertyName.ALIGN_SELF, safeOrUnsafe, position);
  }

  protected final AppearanceDeclaration appearance(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.APPEARANCE, value);
  }

  protected final AppearanceDeclaration appearance(AppearanceValue value) {
    return addDeclaration(StandardPropertyName.APPEARANCE, value);
  }

  protected final MozAppearanceDeclaration mozAppearance(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName._MOZ_APPEARANCE, value);
  }

  protected final MozAppearanceDeclaration mozAppearance(AppearanceValue value) {
    return addDeclaration(StandardPropertyName._MOZ_APPEARANCE, value);
  }

  protected final WebkitAppearanceDeclaration webkitAppearance(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName._WEBKIT_APPEARANCE, value);
  }

  protected final WebkitAppearanceDeclaration webkitAppearance(AppearanceValue value) {
    return addDeclaration(StandardPropertyName._WEBKIT_APPEARANCE, value);
  }

  protected final BackgroundAttachmentDeclaration backgroundAttachment(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_ATTACHMENT, value);
  }

  protected final BackgroundAttachmentDeclaration backgroundAttachment(BackgroundAttachmentValue value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_ATTACHMENT, value);
  }

  protected final BackgroundClipDeclaration backgroundClip(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_CLIP, value);
  }

  protected final BackgroundClipDeclaration backgroundClip(BoxValue value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_CLIP, value);
  }

  protected final BackgroundColorDeclaration backgroundColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_COLOR, value);
  }

  protected final BackgroundColorDeclaration backgroundColor(ColorType color) {
    return addDeclaration(StandardPropertyName.BACKGROUND_COLOR, color);
  }

  protected final BackgroundImageDeclaration backgroundImage(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_IMAGE, value);
  }

  protected final BackgroundImageDeclaration backgroundImage(BackgroundImageValue image) {
    return addDeclaration(StandardPropertyName.BACKGROUND_IMAGE, image);
  }

  protected final BackgroundOriginDeclaration backgroundOrigin(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_ORIGIN, value);
  }

  protected final BackgroundOriginDeclaration backgroundOrigin(BoxValue value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_ORIGIN, value);
  }

  protected final BackgroundPositionDeclaration backgroundPosition(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_POSITION, value);
  }

  protected final BackgroundPositionDeclaration backgroundPosition(BackgroundPositionValue value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_POSITION, value);
  }

  protected final BackgroundPositionDeclaration backgroundPosition(BackgroundPositionValue value1, BackgroundPositionValue value2) {
    return addDeclaration(StandardPropertyName.BACKGROUND_POSITION, value1, value2);
  }

  protected final BackgroundPositionDeclaration backgroundPosition(BackgroundPositionValue value1, BackgroundPositionValue value2, BackgroundPositionValue value3) {
    return addDeclaration(StandardPropertyName.BACKGROUND_POSITION, value1, value2, value3);
  }

  protected final BackgroundPositionDeclaration backgroundPosition(BackgroundPositionValue value1, BackgroundPositionValue value2, BackgroundPositionValue value3, BackgroundPositionValue value4) {
    return addDeclaration(StandardPropertyName.BACKGROUND_POSITION, value1, value2, value3, value4);
  }

  protected final BackgroundRepeatDeclaration backgroundRepeat(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_REPEAT, value);
  }

  protected final BackgroundRepeatDeclaration backgroundRepeat(BackgroundRepeatArity1Value value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_REPEAT, value);
  }

  protected final BackgroundRepeatDeclaration backgroundRepeat(BackgroundRepeatArity2Value value1, BackgroundRepeatArity2Value value2) {
    return addDeclaration(StandardPropertyName.BACKGROUND_REPEAT, value1, value2);
  }

  protected final BackgroundSizeDeclaration backgroundSize(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_SIZE, value);
  }

  protected final BackgroundSizeDeclaration backgroundSize(BackgroundSizeArity1Value value) {
    return addDeclaration(StandardPropertyName.BACKGROUND_SIZE, value);
  }

  protected final BackgroundSizeDeclaration backgroundSize(BackgroundSizeArity2Value value1, BackgroundSizeArity2Value value2) {
    return addDeclaration(StandardPropertyName.BACKGROUND_SIZE, value1, value2);
  }

  protected final BackgroundDeclaration background(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BACKGROUND, value);
  }

  protected final BackgroundDeclaration background(BackgroundValue value) {
    return addDeclaration(StandardPropertyName.BACKGROUND, value);
  }

  protected final BackgroundDeclaration background(BackgroundValue value1, BackgroundValue value2) {
    return addDeclaration(StandardPropertyName.BACKGROUND, value1, value2);
  }

  protected final BackgroundDeclaration background(BackgroundValue value1, BackgroundValue value2, BackgroundValue value3) {
    return addDeclaration(StandardPropertyName.BACKGROUND, value1, value2, value3);
  }

  protected final BackgroundDeclaration background(BackgroundValue value1, BackgroundValue value2, BackgroundValue value3, BackgroundValue value4) {
    return addDeclaration(StandardPropertyName.BACKGROUND, value1, value2, value3, value4);
  }

  protected final BackgroundDeclaration background(BackgroundValue value1, BackgroundValue value2, BackgroundValue value3, BackgroundValue value4, BackgroundValue value5) {
    return addDeclaration(StandardPropertyName.BACKGROUND, value1, value2, value3, value4, value5);
  }

  protected final BorderCollapseDeclaration borderCollapse(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_COLLAPSE, value);
  }

  protected final BorderCollapseDeclaration borderCollapse(BorderCollapseValue value) {
    return addDeclaration(StandardPropertyName.BORDER_COLLAPSE, value);
  }

  protected final BorderColorDeclaration borderColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_COLOR, value);
  }

  protected final BorderColorDeclaration borderColor(ColorType all) {
    return addDeclaration(StandardPropertyName.BORDER_COLOR, all);
  }

  protected final BorderColorDeclaration borderColor(ColorType vertical, ColorType horizontal) {
    return addDeclaration(StandardPropertyName.BORDER_COLOR, vertical, horizontal);
  }

  protected final BorderColorDeclaration borderColor(ColorType top, ColorType horizontal, ColorType bottom) {
    return addDeclaration(StandardPropertyName.BORDER_COLOR, top, horizontal, bottom);
  }

  protected final BorderColorDeclaration borderColor(ColorType top, ColorType right, ColorType bottom, ColorType left) {
    return addDeclaration(StandardPropertyName.BORDER_COLOR, top, right, bottom, left);
  }

  protected final BorderTopColorDeclaration borderTopColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_COLOR, value);
  }

  protected final BorderTopColorDeclaration borderTopColor(ColorType color) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_COLOR, color);
  }

  protected final BorderRightColorDeclaration borderRightColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT_COLOR, value);
  }

  protected final BorderRightColorDeclaration borderRightColor(ColorType color) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT_COLOR, color);
  }

  protected final BorderBottomColorDeclaration borderBottomColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_COLOR, value);
  }

  protected final BorderBottomColorDeclaration borderBottomColor(ColorType color) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_COLOR, color);
  }

  protected final BorderLeftColorDeclaration borderLeftColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT_COLOR, value);
  }

  protected final BorderLeftColorDeclaration borderLeftColor(ColorType color) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT_COLOR, color);
  }

  protected final BorderRadiusDeclaration borderRadius(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_RADIUS, value);
  }

  protected final BorderRadiusDeclaration borderRadius(BorderRadiusValue all) {
    return addDeclaration(StandardPropertyName.BORDER_RADIUS, all);
  }

  protected final BorderRadiusDeclaration borderRadius(BorderRadiusValue topLeftBottomRight, BorderRadiusValue topRightBottomLeft) {
    return addDeclaration(StandardPropertyName.BORDER_RADIUS, topLeftBottomRight, topRightBottomLeft);
  }

  protected final BorderRadiusDeclaration borderRadius(BorderRadiusValue topLeft, BorderRadiusValue topRightBottomLeft, BorderRadiusValue bottomRight) {
    return addDeclaration(StandardPropertyName.BORDER_RADIUS, topLeft, topRightBottomLeft, bottomRight);
  }

  protected final BorderRadiusDeclaration borderRadius(BorderRadiusValue topLeft, BorderRadiusValue topRight, BorderRadiusValue bottomRight, BorderRadiusValue bottomLeft) {
    return addDeclaration(StandardPropertyName.BORDER_RADIUS, topLeft, topRight, bottomRight, bottomLeft);
  }

  protected final BorderTopLeftRadiusDeclaration borderTopLeftRadius(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_LEFT_RADIUS, value);
  }

  protected final BorderTopLeftRadiusDeclaration borderTopLeftRadius(BorderRadiusValue value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_LEFT_RADIUS, value);
  }

  protected final BorderTopLeftRadiusDeclaration borderTopLeftRadius(BorderRadiusValue horizontal, BorderRadiusValue vertical) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_LEFT_RADIUS, horizontal, vertical);
  }

  protected final BorderTopRightRadiusDeclaration borderTopRightRadius(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_RIGHT_RADIUS, value);
  }

  protected final BorderTopRightRadiusDeclaration borderTopRightRadius(BorderRadiusValue value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_RIGHT_RADIUS, value);
  }

  protected final BorderTopRightRadiusDeclaration borderTopRightRadius(BorderRadiusValue horizontal, BorderRadiusValue vertical) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_RIGHT_RADIUS, horizontal, vertical);
  }

  protected final BorderBottomRightRadiusDeclaration borderBottomRightRadius(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_RIGHT_RADIUS, value);
  }

  protected final BorderBottomRightRadiusDeclaration borderBottomRightRadius(BorderRadiusValue value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_RIGHT_RADIUS, value);
  }

  protected final BorderBottomRightRadiusDeclaration borderBottomRightRadius(BorderRadiusValue horizontal, BorderRadiusValue vertical) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_RIGHT_RADIUS, horizontal, vertical);
  }

  protected final BorderBottomLeftRadiusDeclaration borderBottomLeftRadius(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_LEFT_RADIUS, value);
  }

  protected final BorderBottomLeftRadiusDeclaration borderBottomLeftRadius(BorderRadiusValue value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_LEFT_RADIUS, value);
  }

  protected final BorderBottomLeftRadiusDeclaration borderBottomLeftRadius(BorderRadiusValue horizontal, BorderRadiusValue vertical) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_LEFT_RADIUS, horizontal, vertical);
  }

  protected final BorderStyleDeclaration borderStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_STYLE, value);
  }

  protected final BorderStyleDeclaration borderStyle(LineStyleValue all) {
    return addDeclaration(StandardPropertyName.BORDER_STYLE, all);
  }

  protected final BorderStyleDeclaration borderStyle(LineStyleValue vertical, LineStyleValue horizontal) {
    return addDeclaration(StandardPropertyName.BORDER_STYLE, vertical, horizontal);
  }

  protected final BorderStyleDeclaration borderStyle(LineStyleValue top, LineStyleValue horizontal, LineStyleValue bottom) {
    return addDeclaration(StandardPropertyName.BORDER_STYLE, top, horizontal, bottom);
  }

  protected final BorderStyleDeclaration borderStyle(LineStyleValue top, LineStyleValue right, LineStyleValue bottom, LineStyleValue left) {
    return addDeclaration(StandardPropertyName.BORDER_STYLE, top, right, bottom, left);
  }

  protected final BorderTopStyleDeclaration borderTopStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_STYLE, value);
  }

  protected final BorderTopStyleDeclaration borderTopStyle(LineStyleValue style) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_STYLE, style);
  }

  protected final BorderRightStyleDeclaration borderRightStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT_STYLE, value);
  }

  protected final BorderRightStyleDeclaration borderRightStyle(LineStyleValue style) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT_STYLE, style);
  }

  protected final BorderBottomStyleDeclaration borderBottomStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_STYLE, value);
  }

  protected final BorderBottomStyleDeclaration borderBottomStyle(LineStyleValue style) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_STYLE, style);
  }

  protected final BorderLeftStyleDeclaration borderLeftStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT_STYLE, value);
  }

  protected final BorderLeftStyleDeclaration borderLeftStyle(LineStyleValue style) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT_STYLE, style);
  }

  protected final BorderWidthDeclaration borderWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_WIDTH, value);
  }

  protected final BorderWidthDeclaration borderWidth(LineWidthValue all) {
    return addDeclaration(StandardPropertyName.BORDER_WIDTH, all);
  }

  protected final BorderWidthDeclaration borderWidth(LineWidthValue vertical, LineWidthValue horizontal) {
    return addDeclaration(StandardPropertyName.BORDER_WIDTH, vertical, horizontal);
  }

  protected final BorderWidthDeclaration borderWidth(LineWidthValue top, LineWidthValue horizontal, LineWidthValue bottom) {
    return addDeclaration(StandardPropertyName.BORDER_WIDTH, top, horizontal, bottom);
  }

  protected final BorderWidthDeclaration borderWidth(LineWidthValue top, LineWidthValue right, LineWidthValue bottom, LineWidthValue left) {
    return addDeclaration(StandardPropertyName.BORDER_WIDTH, top, right, bottom, left);
  }

  protected final BorderTopWidthDeclaration borderTopWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_WIDTH, value);
  }

  protected final BorderTopWidthDeclaration borderTopWidth(LineWidthValue width) {
    return addDeclaration(StandardPropertyName.BORDER_TOP_WIDTH, width);
  }

  protected final BorderRightWidthDeclaration borderRightWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT_WIDTH, value);
  }

  protected final BorderRightWidthDeclaration borderRightWidth(LineWidthValue width) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT_WIDTH, width);
  }

  protected final BorderBottomWidthDeclaration borderBottomWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_WIDTH, value);
  }

  protected final BorderBottomWidthDeclaration borderBottomWidth(LineWidthValue width) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM_WIDTH, width);
  }

  protected final BorderLeftWidthDeclaration borderLeftWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT_WIDTH, value);
  }

  protected final BorderLeftWidthDeclaration borderLeftWidth(LineWidthValue width) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT_WIDTH, width);
  }

  protected final BorderDeclaration border(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER, value);
  }

  protected final BorderDeclaration border(BorderShorthandValue v1) {
    return addDeclaration(StandardPropertyName.BORDER, v1);
  }

  protected final BorderDeclaration border(BorderShorthandValue v1, BorderShorthandValue v2) {
    return addDeclaration(StandardPropertyName.BORDER, v1, v2);
  }

  protected final BorderDeclaration border(BorderShorthandValue v1, BorderShorthandValue v2, BorderShorthandValue v3) {
    return addDeclaration(StandardPropertyName.BORDER, v1, v2, v3);
  }

  protected final BorderTopDeclaration borderTop(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_TOP, value);
  }

  protected final BorderTopDeclaration borderTop(BorderShorthandValue v1) {
    return addDeclaration(StandardPropertyName.BORDER_TOP, v1);
  }

  protected final BorderTopDeclaration borderTop(BorderShorthandValue v1, BorderShorthandValue v2) {
    return addDeclaration(StandardPropertyName.BORDER_TOP, v1, v2);
  }

  protected final BorderTopDeclaration borderTop(BorderShorthandValue v1, BorderShorthandValue v2, BorderShorthandValue v3) {
    return addDeclaration(StandardPropertyName.BORDER_TOP, v1, v2, v3);
  }

  protected final BorderRightDeclaration borderRight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT, value);
  }

  protected final BorderRightDeclaration borderRight(BorderShorthandValue v1) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT, v1);
  }

  protected final BorderRightDeclaration borderRight(BorderShorthandValue v1, BorderShorthandValue v2) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT, v1, v2);
  }

  protected final BorderRightDeclaration borderRight(BorderShorthandValue v1, BorderShorthandValue v2, BorderShorthandValue v3) {
    return addDeclaration(StandardPropertyName.BORDER_RIGHT, v1, v2, v3);
  }

  protected final BorderBottomDeclaration borderBottom(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM, value);
  }

  protected final BorderBottomDeclaration borderBottom(BorderShorthandValue v1) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM, v1);
  }

  protected final BorderBottomDeclaration borderBottom(BorderShorthandValue v1, BorderShorthandValue v2) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM, v1, v2);
  }

  protected final BorderBottomDeclaration borderBottom(BorderShorthandValue v1, BorderShorthandValue v2, BorderShorthandValue v3) {
    return addDeclaration(StandardPropertyName.BORDER_BOTTOM, v1, v2, v3);
  }

  protected final BorderLeftDeclaration borderLeft(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT, value);
  }

  protected final BorderLeftDeclaration borderLeft(BorderShorthandValue v1) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT, v1);
  }

  protected final BorderLeftDeclaration borderLeft(BorderShorthandValue v1, BorderShorthandValue v2) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT, v1, v2);
  }

  protected final BorderLeftDeclaration borderLeft(BorderShorthandValue v1, BorderShorthandValue v2, BorderShorthandValue v3) {
    return addDeclaration(StandardPropertyName.BORDER_LEFT, v1, v2, v3);
  }

  protected final BottomDeclaration bottom(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BOTTOM, value);
  }

  protected final BottomDeclaration bottom(BottomValue value) {
    return addDeclaration(StandardPropertyName.BOTTOM, value);
  }

  protected final BoxShadowSingleDeclaration boxShadow(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, value);
  }

  protected final BoxShadowSingleDeclaration boxShadow(NoneKeyword none) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, none);
  }

  protected final BoxShadowSingleDeclaration boxShadow(LengthType offsetX, LengthType offsetY, ColorType color) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, offsetX, offsetY, color);
  }

  protected final BoxShadowSingleDeclaration boxShadow(LengthType offsetX, LengthType offsetY, LengthType blurRadius, ColorType color) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, offsetX, offsetY, blurRadius, color);
  }

  protected final BoxShadowSingleDeclaration boxShadow(InsetKeyword inset, LengthType offsetX, LengthType offsetY, ColorType color) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, inset, offsetX, offsetY, color);
  }

  protected final BoxShadowSingleDeclaration boxShadow(LengthType offsetX, LengthType offsetY, LengthType blurRadius, LengthType spreadRadius, ColorType color) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, offsetX, offsetY, blurRadius, spreadRadius, color);
  }

  protected final BoxShadowSingleDeclaration boxShadow(InsetKeyword inset, LengthType offsetX, LengthType offsetY, LengthType blurRadius, ColorType color) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, inset, offsetX, offsetY, blurRadius, color);
  }

  protected final BoxShadowSingleDeclaration boxShadow(InsetKeyword inset, LengthType offsetX, LengthType offsetY, LengthType blurRadius, LengthType spreadRadius, ColorType color) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, inset, offsetX, offsetY, blurRadius, spreadRadius, color);
  }

  protected final BoxShadowMultiDeclaration boxShadow(BoxShadowSingleDeclaration... declarations) {
    return addDeclaration(StandardPropertyName.BOX_SHADOW, declarations);
  }

  protected final BoxSizingDeclaration boxSizing(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.BOX_SIZING, value);
  }

  protected final BoxSizingDeclaration boxSizing(BoxSizingValue value) {
    return addDeclaration(StandardPropertyName.BOX_SIZING, value);
  }

  protected final ClearDeclaration clear(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.CLEAR, value);
  }

  protected final ClearDeclaration clear(ClearValue value) {
    return addDeclaration(StandardPropertyName.CLEAR, value);
  }

  protected final ColorDeclaration color(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.COLOR, value);
  }

  protected final ColorDeclaration color(ColorType color) {
    return addDeclaration(StandardPropertyName.COLOR, color);
  }

  protected final ContentDeclaration content(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.CONTENT, value);
  }

  protected final ContentDeclaration content(ContentValue value) {
    return addDeclaration(StandardPropertyName.CONTENT, value);
  }

  protected final ContentDeclaration content(String value) {
    return addDeclaration(StandardPropertyName.CONTENT, value);
  }

  protected final CursorDeclaration cursor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.CURSOR, value);
  }

  protected final CursorDeclaration cursor(CursorValue value) {
    return addDeclaration(StandardPropertyName.CURSOR, value);
  }

  protected final DisplayDeclaration display(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.DISPLAY, value);
  }

  protected final DisplayDeclaration display(DisplayArity1Value value) {
    return addDeclaration(StandardPropertyName.DISPLAY, value);
  }

  protected final DisplayDeclaration display(DisplayArity2Value value1, DisplayArity2Value value2) {
    return addDeclaration(StandardPropertyName.DISPLAY, value1, value2);
  }

  protected final FlexDeclaration flex(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX, value);
  }

  protected final FlexDeclaration flex(FlexArity1Value value) {
    return addDeclaration(StandardPropertyName.FLEX, value);
  }

  protected final FlexDeclaration flex(double grow) {
    return addDeclaration(StandardPropertyName.FLEX, grow);
  }

  protected final FlexDeclaration flex(int grow) {
    return addDeclaration(StandardPropertyName.FLEX, grow);
  }

  protected final FlexDeclaration flex(NumberValue grow, NumberValue shrink) {
    return addDeclaration(StandardPropertyName.FLEX, grow, shrink);
  }

  protected final FlexDeclaration flex(NumberValue grow, HeightOrWidthValue basis) {
    return addDeclaration(StandardPropertyName.FLEX, grow, basis);
  }

  protected final FlexDeclaration flex(NumberValue grow, NumberValue shrink, HeightOrWidthValue basis) {
    return addDeclaration(StandardPropertyName.FLEX, grow, shrink, basis);
  }

  protected final FlexBasisDeclaration flexBasis(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX_BASIS, value);
  }

  protected final FlexBasisDeclaration flexBasis(FlexBasisValue value) {
    return addDeclaration(StandardPropertyName.FLEX_BASIS, value);
  }

  protected final FlexDirectionDeclaration flexDirection(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX_DIRECTION, value);
  }

  protected final FlexDirectionDeclaration flexDirection(FlexDirectionValue value) {
    return addDeclaration(StandardPropertyName.FLEX_DIRECTION, value);
  }

  protected final FlexGrowDeclaration flexGrow(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX_GROW, value);
  }

  protected final FlexGrowDeclaration flexGrow(double value) {
    return addDeclaration(StandardPropertyName.FLEX_GROW, value);
  }

  protected final FlexGrowDeclaration flexGrow(int value) {
    return addDeclaration(StandardPropertyName.FLEX_GROW, value);
  }

  protected final FlexShrinkDeclaration flexShrink(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX_SHRINK, value);
  }

  protected final FlexShrinkDeclaration flexShrink(double value) {
    return addDeclaration(StandardPropertyName.FLEX_SHRINK, value);
  }

  protected final FlexShrinkDeclaration flexShrink(int value) {
    return addDeclaration(StandardPropertyName.FLEX_SHRINK, value);
  }

  protected final FlexWrapDeclaration flexWrap(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX_WRAP, value);
  }

  protected final FlexWrapDeclaration flexWrap(FlexWrapValue value) {
    return addDeclaration(StandardPropertyName.FLEX_WRAP, value);
  }

  protected final FlexFlowDeclaration flexFlow(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLEX_FLOW, value);
  }

  protected final FlexFlowDeclaration flexFlow(FlexFlowValue value) {
    return addDeclaration(StandardPropertyName.FLEX_FLOW, value);
  }

  protected final FloatDeclaration floatTo(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FLOAT, value);
  }

  protected final FloatDeclaration floatTo(FloatValue value) {
    return addDeclaration(StandardPropertyName.FLOAT, value);
  }

  protected final FontFamilySingleDeclaration fontFamily(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FONT_FAMILY, value);
  }

  protected final FontFamilySingleDeclaration fontFamily(FontFamilyValue family) {
    return addDeclaration(StandardPropertyName.FONT_FAMILY, family);
  }

  protected final FontFamilySingleDeclaration fontFamily(String family) {
    return addDeclaration(StandardPropertyName.FONT_FAMILY, family);
  }

  protected final FontFamilyMultiDeclaration fontFamily(FontFamilySingleDeclaration... declarations) {
    return addDeclaration(StandardPropertyName.FONT_FAMILY, declarations);
  }

  protected final FontDeclaration font(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FONT, value);
  }

  protected final FontDeclaration font(SystemFontValue value) {
    return addDeclaration(StandardPropertyName.FONT, value);
  }

  protected final FontDeclaration font(FontVariantCss21Value value) {
    return addDeclaration(StandardPropertyName.FONT, value);
  }

  protected final FontSizeDeclaration fontSize(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FONT_SIZE, value);
  }

  protected final FontSizeDeclaration fontSize(FontSizeValue size) {
    return addDeclaration(StandardPropertyName.FONT_SIZE, size);
  }

  protected final FontStyleDeclaration fontStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FONT_STYLE, value);
  }

  protected final FontStyleDeclaration fontStyle(FontStyleValue value) {
    return addDeclaration(StandardPropertyName.FONT_STYLE, value);
  }

  protected final FontWeightDeclaration fontWeight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.FONT_WEIGHT, value);
  }

  protected final FontWeightDeclaration fontWeight(FontWeightValue value) {
    return addDeclaration(StandardPropertyName.FONT_WEIGHT, value);
  }

  protected final FontWeightDeclaration fontWeight(int value) {
    return addDeclaration(StandardPropertyName.FONT_WEIGHT, value);
  }

  protected final HeightDeclaration height(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.HEIGHT, value);
  }

  protected final HeightDeclaration height(HeightOrWidthValue value) {
    return addDeclaration(StandardPropertyName.HEIGHT, value);
  }

  protected final WidthDeclaration width(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.WIDTH, value);
  }

  protected final WidthDeclaration width(HeightOrWidthValue value) {
    return addDeclaration(StandardPropertyName.WIDTH, value);
  }

  protected final JustifyContentDeclaration justifyContent(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_CONTENT, value);
  }

  protected final JustifyContentDeclaration justifyContent(JustifyContentValue value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_CONTENT, value);
  }

  protected final JustifyContentDeclaration justifyContent(OverflowPosition safeOrUnsafe, ContentPositionOrLeftOrRight position) {
    return addDeclaration(StandardPropertyName.JUSTIFY_CONTENT, safeOrUnsafe, position);
  }

  protected final JustifyItemsDeclaration justifyItems(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_ITEMS, value);
  }

  protected final JustifyItemsDeclaration justifyItems(JustifyItemsValue value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_ITEMS, value);
  }

  protected final JustifyItemsDeclaration justifyItems(BaselinePosition firstOrLast, BaselineKeyword baseline) {
    return addDeclaration(StandardPropertyName.JUSTIFY_ITEMS, firstOrLast, baseline);
  }

  protected final JustifyItemsDeclaration justifyItems(OverflowPosition safeOrUnsafe, SelfPosition position) {
    return addDeclaration(StandardPropertyName.JUSTIFY_ITEMS, safeOrUnsafe, position);
  }

  protected final JustifyItemsDeclaration justifyItems(LegacyKeyword legacy, JustifyLegacyValue value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_ITEMS, legacy, value);
  }

  protected final JustifySelfDeclaration justifySelf(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_SELF, value);
  }

  protected final JustifySelfDeclaration justifySelf(JustifySelfValue value) {
    return addDeclaration(StandardPropertyName.JUSTIFY_SELF, value);
  }

  protected final JustifySelfDeclaration justifySelf(BaselinePosition firstOrLast, BaselineKeyword baseline) {
    return addDeclaration(StandardPropertyName.JUSTIFY_SELF, firstOrLast, baseline);
  }

  protected final JustifySelfDeclaration justifySelf(OverflowPosition safeOrUnsafe, SelfPositionOrLeftOrRight position) {
    return addDeclaration(StandardPropertyName.JUSTIFY_SELF, safeOrUnsafe, position);
  }

  protected final LeftDeclaration left(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LEFT, value);
  }

  protected final LeftDeclaration left(LeftValue value) {
    return addDeclaration(StandardPropertyName.LEFT, value);
  }

  protected final LetterSpacingDeclaration letterSpacing(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LETTER_SPACING, value);
  }

  protected final LetterSpacingDeclaration letterSpacing(LineHeightValue value) {
    return addDeclaration(StandardPropertyName.LETTER_SPACING, value);
  }

  protected final LineHeightDeclaration lineHeight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LINE_HEIGHT, value);
  }

  protected final LineHeightDeclaration lineHeight(LineHeightValue value) {
    return addDeclaration(StandardPropertyName.LINE_HEIGHT, value);
  }

  protected final LineHeightDeclaration lineHeight(int value) {
    return addDeclaration(StandardPropertyName.LINE_HEIGHT, value);
  }

  protected final LineHeightDeclaration lineHeight(double value) {
    return addDeclaration(StandardPropertyName.LINE_HEIGHT, value);
  }

  protected final ListStyleImageDeclaration listStyleImage(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_IMAGE, value);
  }

  protected final ListStyleImageDeclaration listStyleImage(ListStyleImageValue value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_IMAGE, value);
  }

  protected final ListStylePositionDeclaration listStylePosition(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_POSITION, value);
  }

  protected final ListStylePositionDeclaration listStylePosition(ListStylePositionValue value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_POSITION, value);
  }

  protected final ListStyleTypeDeclaration listStyleType(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_TYPE, value);
  }

  protected final ListStyleTypeDeclaration listStyleType(ListStyleTypeValue value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_TYPE, value);
  }

  protected final ListStyleTypeDeclaration listStyleType(String value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE_TYPE, value);
  }

  protected final ListStyleDeclaration listStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE, value);
  }

  protected final ListStyleDeclaration listStyle(ListStyleValue value) {
    return addDeclaration(StandardPropertyName.LIST_STYLE, value);
  }

  protected final ListStyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2) {
    return addDeclaration(StandardPropertyName.LIST_STYLE, value1, value2);
  }

  protected final ListStyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2, ListStyleValue value3) {
    return addDeclaration(StandardPropertyName.LIST_STYLE, value1, value2, value3);
  }

  protected final MarginDeclaration margin(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MARGIN, value);
  }

  protected final MarginDeclaration margin(MarginWidthValue all) {
    return addDeclaration(StandardPropertyName.MARGIN, all);
  }

  protected final MarginDeclaration margin(MarginWidthValue vertical, MarginWidthValue horizontal) {
    return addDeclaration(StandardPropertyName.MARGIN, vertical, horizontal);
  }

  protected final MarginDeclaration margin(MarginWidthValue top, MarginWidthValue horizontal, MarginWidthValue bottom) {
    return addDeclaration(StandardPropertyName.MARGIN, top, horizontal, bottom);
  }

  protected final MarginDeclaration margin(MarginWidthValue top, MarginWidthValue right, MarginWidthValue bottom, MarginWidthValue left) {
    return addDeclaration(StandardPropertyName.MARGIN, top, right, bottom, left);
  }

  protected final MarginTopDeclaration marginTop(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MARGIN_TOP, value);
  }

  protected final MarginTopDeclaration marginTop(MarginWidthValue value) {
    return addDeclaration(StandardPropertyName.MARGIN_TOP, value);
  }

  protected final MarginRightDeclaration marginRight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MARGIN_RIGHT, value);
  }

  protected final MarginRightDeclaration marginRight(MarginWidthValue value) {
    return addDeclaration(StandardPropertyName.MARGIN_RIGHT, value);
  }

  protected final MarginBottomDeclaration marginBottom(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MARGIN_BOTTOM, value);
  }

  protected final MarginBottomDeclaration marginBottom(MarginWidthValue value) {
    return addDeclaration(StandardPropertyName.MARGIN_BOTTOM, value);
  }

  protected final MarginLeftDeclaration marginLeft(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MARGIN_LEFT, value);
  }

  protected final MarginLeftDeclaration marginLeft(MarginWidthValue value) {
    return addDeclaration(StandardPropertyName.MARGIN_LEFT, value);
  }

  protected final MaxHeightDeclaration maxHeight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MAX_HEIGHT, value);
  }

  protected final MaxHeightDeclaration maxHeight(MaxHeightOrWidthValue value) {
    return addDeclaration(StandardPropertyName.MAX_HEIGHT, value);
  }

  protected abstract MaxHeightDeclaration maxHeight(LengthType length);

  protected final MaxHeightDeclaration maxHeight(Zero zero) {
    return addDeclaration(StandardPropertyName.MAX_HEIGHT, 0);
  }

  protected final MaxWidthDeclaration maxWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MAX_WIDTH, value);
  }

  protected final MaxWidthDeclaration maxWidth(MaxHeightOrWidthValue value) {
    return addDeclaration(StandardPropertyName.MAX_WIDTH, value);
  }

  protected abstract MaxWidthDeclaration maxWidth(LengthType length);

  protected final MaxWidthDeclaration maxWidth(Zero zero) {
    return addDeclaration(StandardPropertyName.MAX_WIDTH, 0);
  }

  protected final MinHeightDeclaration minHeight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MIN_HEIGHT, value);
  }

  protected final MinHeightDeclaration minHeight(MaxHeightOrWidthValue value) {
    return addDeclaration(StandardPropertyName.MIN_HEIGHT, value);
  }

  protected abstract MinHeightDeclaration minHeight(LengthType length);

  protected final MinHeightDeclaration minHeight(Zero zero) {
    return addDeclaration(StandardPropertyName.MIN_HEIGHT, 0);
  }

  protected final MinWidthDeclaration minWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.MIN_WIDTH, value);
  }

  protected final MinWidthDeclaration minWidth(MaxHeightOrWidthValue value) {
    return addDeclaration(StandardPropertyName.MIN_WIDTH, value);
  }

  protected abstract MinWidthDeclaration minWidth(LengthType length);

  protected final MinWidthDeclaration minWidth(Zero zero) {
    return addDeclaration(StandardPropertyName.MIN_WIDTH, 0);
  }

  protected final ObjectFitDeclaration objectFit(ObjectFitValue value) {
    return addDeclaration(StandardPropertyName.OBJECT_FIT, value);
  }

  protected final ObjectFitDeclaration objectFit(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OBJECT_FIT, value);
  }

  protected final OpacityDeclaration opacity(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OPACITY, value);
  }

  protected final OpacityDeclaration opacity(PercentageType value) {
    return addDeclaration(StandardPropertyName.OPACITY, value);
  }

  protected final OpacityDeclaration opacity(int value) {
    return addDeclaration(StandardPropertyName.OPACITY, value);
  }

  protected final OpacityDeclaration opacity(double value) {
    return addDeclaration(StandardPropertyName.OPACITY, value);
  }

  protected final OutlineColorDeclaration outlineColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OUTLINE_COLOR, value);
  }

  protected final OutlineColorDeclaration outlineColor(OutlineColorValue value) {
    return addDeclaration(StandardPropertyName.OUTLINE_COLOR, value);
  }

  protected final OutlineOffsetDeclaration outlineOffset(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OUTLINE_OFFSET, value);
  }

  protected final OutlineOffsetDeclaration outlineOffset(LengthType offset) {
    return addDeclaration(StandardPropertyName.OUTLINE_OFFSET, offset);
  }

  protected final OutlineStyleDeclaration outlineStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OUTLINE_STYLE, value);
  }

  protected final OutlineStyleDeclaration outlineStyle(OutlineStyleValue value) {
    return addDeclaration(StandardPropertyName.OUTLINE_STYLE, value);
  }

  protected final OutlineWidthDeclaration outlineWidth(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OUTLINE_WIDTH, value);
  }

  protected final OutlineWidthDeclaration outlineWidth(OutlineWidthValue width) {
    return addDeclaration(StandardPropertyName.OUTLINE_WIDTH, width);
  }

  protected final OutlineDeclaration outline(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OUTLINE, value);
  }

  protected final OutlineDeclaration outline(OutlineValue value) {
    return addDeclaration(StandardPropertyName.OUTLINE, value);
  }

  protected final OutlineDeclaration outline(OutlineValue value1, OutlineValue value2) {
    return addDeclaration(StandardPropertyName.OUTLINE, value1, value2);
  }

  protected final OutlineDeclaration outline(OutlineValue value1, OutlineValue value2, OutlineValue value3) {
    return addDeclaration(StandardPropertyName.OUTLINE, value1, value2, value3);
  }

  protected final OverflowDeclaration overflow(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OVERFLOW, value);
  }

  protected final OverflowDeclaration overflow(OverflowValue xy) {
    return addDeclaration(StandardPropertyName.OVERFLOW, xy);
  }

  protected final OverflowDeclaration overflow(OverflowValue x, OverflowValue y) {
    return addDeclaration(StandardPropertyName.OVERFLOW, x, y);
  }

  protected final OverflowBlockDeclaration overflowBlock(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_BLOCK, value);
  }

  protected final OverflowBlockDeclaration overflowBlock(OverflowValue value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_BLOCK, value);
  }

  protected final OverflowInlineDeclaration overflowInline(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_INLINE, value);
  }

  protected final OverflowInlineDeclaration overflowInline(OverflowValue value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_INLINE, value);
  }

  protected final OverflowXDeclaration overflowX(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_X, value);
  }

  protected final OverflowXDeclaration overflowX(OverflowValue value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_X, value);
  }

  protected final OverflowYDeclaration overflowY(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_Y, value);
  }

  protected final OverflowYDeclaration overflowY(OverflowValue value) {
    return addDeclaration(StandardPropertyName.OVERFLOW_Y, value);
  }

  protected final PaddingDeclaration padding(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.PADDING, value);
  }

  protected final PaddingDeclaration padding(LengthOrPercentageValue all) {
    return addDeclaration(StandardPropertyName.PADDING, all);
  }

  protected final PaddingDeclaration padding(LengthOrPercentageValue vertical, LengthOrPercentageValue horizontal) {
    return addDeclaration(StandardPropertyName.PADDING, vertical, horizontal);
  }

  protected final PaddingDeclaration padding(LengthOrPercentageValue top, LengthOrPercentageValue horizontal, LengthOrPercentageValue bottom) {
    return addDeclaration(StandardPropertyName.PADDING, top, horizontal, bottom);
  }

  protected final PaddingDeclaration padding(LengthOrPercentageValue top, LengthOrPercentageValue right, LengthOrPercentageValue bottom, LengthOrPercentageValue left) {
    return addDeclaration(StandardPropertyName.PADDING, top, right, bottom, left);
  }

  protected final PaddingTopDeclaration paddingTop(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.PADDING_TOP, value);
  }

  protected final PaddingTopDeclaration paddingTop(LengthOrPercentageValue value) {
    return addDeclaration(StandardPropertyName.PADDING_TOP, value);
  }

  protected final PaddingRightDeclaration paddingRight(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.PADDING_RIGHT, value);
  }

  protected final PaddingRightDeclaration paddingRight(LengthOrPercentageValue value) {
    return addDeclaration(StandardPropertyName.PADDING_RIGHT, value);
  }

  protected final PaddingBottomDeclaration paddingBottom(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.PADDING_BOTTOM, value);
  }

  protected final PaddingBottomDeclaration paddingBottom(LengthOrPercentageValue value) {
    return addDeclaration(StandardPropertyName.PADDING_BOTTOM, value);
  }

  protected final PaddingLeftDeclaration paddingLeft(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.PADDING_LEFT, value);
  }

  protected final PaddingLeftDeclaration paddingLeft(LengthOrPercentageValue value) {
    return addDeclaration(StandardPropertyName.PADDING_LEFT, value);
  }

  protected final PositionDeclaration position(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.POSITION, value);
  }

  protected final PositionDeclaration position(PositionValue value) {
    return addDeclaration(StandardPropertyName.POSITION, value);
  }

  protected final ResizeDeclaration resize(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.RESIZE, value);
  }

  protected final ResizeDeclaration resize(ResizeValue value) {
    return addDeclaration(StandardPropertyName.RESIZE, value);
  }

  protected final RightDeclaration right(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.RIGHT, value);
  }

  protected final RightDeclaration right(RightValue value) {
    return addDeclaration(StandardPropertyName.RIGHT, value);
  }

  protected final TabSizeDeclaration tabSize(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TAB_SIZE, value);
  }

  protected final TabSizeDeclaration tabSize(TabSizeValue value) {
    return addDeclaration(StandardPropertyName.TAB_SIZE, value);
  }

  protected final TabSizeDeclaration tabSize(int value) {
    return addDeclaration(StandardPropertyName.TAB_SIZE, value);
  }

  protected final MozTabSizeDeclaration mozTabSize(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName._MOZ_TAB_SIZE, value);
  }

  protected final MozTabSizeDeclaration mozTabSize(TabSizeValue value) {
    return addDeclaration(StandardPropertyName._MOZ_TAB_SIZE, value);
  }

  protected final MozTabSizeDeclaration mozTabSize(int value) {
    return addDeclaration(StandardPropertyName._MOZ_TAB_SIZE, value);
  }

  protected final TextAlignDeclaration textAlign(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_ALIGN, value);
  }

  protected final TextAlignDeclaration textAlign(TextAlignValue value) {
    return addDeclaration(StandardPropertyName.TEXT_ALIGN, value);
  }

  protected final TextDecorationColorDeclaration textDecorationColor(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_COLOR, value);
  }

  protected final TextDecorationColorDeclaration textDecorationColor(ColorType color) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_COLOR, color);
  }

  protected final TextDecorationLineDeclaration textDecorationLine(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_LINE, value);
  }

  protected final TextDecorationLineDeclaration textDecorationLine(TextDecorationLineValue value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_LINE, value);
  }

  protected final TextDecorationLineDeclaration textDecorationLine(TextDecorationLineKind value1, TextDecorationLineKind value2) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_LINE, value1, value2);
  }

  protected final TextDecorationLineDeclaration textDecorationLine(TextDecorationLineKind value1, TextDecorationLineKind value2, TextDecorationLineKind value3) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_LINE, value1, value2, value3);
  }

  protected final TextDecorationStyleDeclaration textDecorationStyle(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_STYLE, value);
  }

  protected final TextDecorationStyleDeclaration textDecorationStyle(TextDecorationStyleValue style) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_STYLE, style);
  }

  protected final TextDecorationThicknessDeclaration textDecorationThickness(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_THICKNESS, value);
  }

  protected final TextDecorationThicknessDeclaration textDecorationThickness(TextDecorationThicknessValue value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION_THICKNESS, value);
  }

  protected final TextDecorationDeclaration textDecoration(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION, value);
  }

  protected final TextDecorationDeclaration textDecoration(TextDecorationValue value) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION, value);
  }

  protected final TextDecorationDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION, value1, value2);
  }

  protected final TextDecorationDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION, value1, value2, value3);
  }

  protected final TextDecorationDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3, TextDecorationValue value4) {
    return addDeclaration(StandardPropertyName.TEXT_DECORATION, value1, value2, value3, value4);
  }

  protected final TextIndentDeclaration textIndent(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_INDENT, value);
  }

  protected final TextIndentDeclaration textIndent(TextIndentValue value) {
    return addDeclaration(StandardPropertyName.TEXT_INDENT, value);
  }

  protected final TextIndentDeclaration textIndent(TextIndentValue value, TextIndentOptionalValue option) {
    return addDeclaration(StandardPropertyName.TEXT_INDENT, value, option);
  }

  protected final TextIndentDeclaration textIndent(TextIndentValue value, TextIndentOptionalValue option1, TextIndentOptionalValue option2) {
    return addDeclaration(StandardPropertyName.TEXT_INDENT, value, option1, option2);
  }

  protected final TextShadowDeclaration textShadow(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_SHADOW, value);
  }

  protected final TextShadowDeclaration textShadow(LengthType offsetX, LengthType offsetY) {
    return addDeclaration(StandardPropertyName.TEXT_SHADOW, offsetX, offsetY);
  }

  protected final TextShadowDeclaration textShadow(LengthType offsetX, LengthType offsetY, ColorType color) {
    return addDeclaration(StandardPropertyName.TEXT_SHADOW, offsetX, offsetY, color);
  }

  protected final TextShadowDeclaration textShadow(LengthType offsetX, LengthType offsetY, LengthType blurRadius) {
    return addDeclaration(StandardPropertyName.TEXT_SHADOW, offsetX, offsetY, blurRadius);
  }

  protected final TextShadowDeclaration textShadow(LengthType offsetX, LengthType offsetY, LengthType blurRadius, ColorType color) {
    return addDeclaration(StandardPropertyName.TEXT_SHADOW, offsetX, offsetY, blurRadius, color);
  }

  protected final TextSizeAdjustDeclaration textSizeAdjust(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_SIZE_ADJUST, value);
  }

  protected final TextSizeAdjustDeclaration textSizeAdjust(TextSizeAdjustValue value) {
    return addDeclaration(StandardPropertyName.TEXT_SIZE_ADJUST, value);
  }

  protected final WebkitTextSizeAdjustDeclaration webkitTextSizeAdjust(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName._WEBKIT_TEXT_SIZE_ADJUST, value);
  }

  protected final WebkitTextSizeAdjustDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    return addDeclaration(StandardPropertyName._WEBKIT_TEXT_SIZE_ADJUST, value);
  }

  protected final TextTransformDeclaration textTransform(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TEXT_TRANSFORM, value);
  }

  protected final TextTransformDeclaration textTransform(TextTransformValue value) {
    return addDeclaration(StandardPropertyName.TEXT_TRANSFORM, value);
  }

  protected final TopDeclaration top(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TOP, value);
  }

  protected final TopDeclaration top(TopValue value) {
    return addDeclaration(StandardPropertyName.TOP, value);
  }

  protected final RotateFunction rotate(AngleType angle) {
    return addFunction(StandardFunctionName.ROTATE, angle);
  }

  protected final RotateXFunction rotateX(AngleType angle) {
    return addFunction(StandardFunctionName.ROTATEX, angle);
  }

  protected final RotateYFunction rotateY(AngleType angle) {
    return addFunction(StandardFunctionName.ROTATEY, angle);
  }

  protected final RotateZFunction rotateZ(AngleType angle) {
    return addFunction(StandardFunctionName.ROTATEZ, angle);
  }

  protected final TransformDeclaration transform(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.TRANSFORM, value);
  }

  protected final TransformDeclaration transform(TransformValue value) {
    return addDeclaration(StandardPropertyName.TRANSFORM, value);
  }

  protected final VerticalAlignDeclaration verticalAlign(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.VERTICAL_ALIGN, value);
  }

  protected final VerticalAlignDeclaration verticalAlign(VerticalAlignValue value) {
    return addDeclaration(StandardPropertyName.VERTICAL_ALIGN, value);
  }

  protected final WhiteSpaceDeclaration whiteSpace(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.WHITE_SPACE, value);
  }

  protected final WhiteSpaceDeclaration whiteSpace(WhiteSpaceValue value) {
    return addDeclaration(StandardPropertyName.WHITE_SPACE, value);
  }

  protected final ZIndexDeclaration zIndex(GlobalKeyword value) {
    return addDeclaration(StandardPropertyName.Z_INDEX, value);
  }

  protected final ZIndexDeclaration zIndex(ZIndexValue value) {
    return addDeclaration(StandardPropertyName.Z_INDEX, value);
  }

  protected final ZIndexDeclaration zIndex(int value) {
    return addDeclaration(StandardPropertyName.Z_INDEX, value);
  }

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, int value);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, double value);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, MultiDeclarationElement... elements);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, String value);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, Value v1);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3, Value v4);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5);

  abstract AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5, Value v6);

  abstract AnyFunction addFunction(StandardFunctionName name, Value v1);

  abstract AngleType getAngle(AngleUnit unit, double value);

  abstract AngleType getAngle(AngleUnit unit, int value);

  abstract LengthType getLength(LengthUnit unit, double value);

  abstract LengthType getLength(LengthUnit unit, int value);

  protected interface AlignContentDeclaration extends Declaration {}

  protected interface AlignItemsDeclaration extends Declaration {}

  protected interface AlignSelfDeclaration extends Declaration {}

  protected interface AppearanceDeclaration extends Declaration {}

  protected interface MozAppearanceDeclaration extends Declaration {}

  protected interface WebkitAppearanceDeclaration extends Declaration {}

  protected interface BackgroundAttachmentDeclaration extends Declaration {}

  protected interface BackgroundClipDeclaration extends Declaration {}

  protected interface BackgroundColorDeclaration extends Declaration {}

  protected interface BackgroundImageDeclaration extends Declaration {}

  protected interface BackgroundOriginDeclaration extends Declaration {}

  protected interface BackgroundPositionDeclaration extends Declaration {}

  protected interface BackgroundRepeatDeclaration extends Declaration {}

  protected interface BackgroundSizeDeclaration extends Declaration {}

  protected interface BackgroundDeclaration extends Declaration {}

  protected interface BorderCollapseDeclaration extends Declaration {}

  protected interface BorderColorDeclaration extends Declaration {}

  protected interface BorderTopColorDeclaration extends Declaration {}

  protected interface BorderRightColorDeclaration extends Declaration {}

  protected interface BorderBottomColorDeclaration extends Declaration {}

  protected interface BorderLeftColorDeclaration extends Declaration {}

  protected interface BorderRadiusDeclaration extends Declaration {}

  protected interface BorderTopLeftRadiusDeclaration extends Declaration {}

  protected interface BorderTopRightRadiusDeclaration extends Declaration {}

  protected interface BorderBottomRightRadiusDeclaration extends Declaration {}

  protected interface BorderBottomLeftRadiusDeclaration extends Declaration {}

  protected interface BorderStyleDeclaration extends Declaration {}

  protected interface BorderTopStyleDeclaration extends Declaration {}

  protected interface BorderRightStyleDeclaration extends Declaration {}

  protected interface BorderBottomStyleDeclaration extends Declaration {}

  protected interface BorderLeftStyleDeclaration extends Declaration {}

  protected interface BorderWidthDeclaration extends Declaration {}

  protected interface BorderTopWidthDeclaration extends Declaration {}

  protected interface BorderRightWidthDeclaration extends Declaration {}

  protected interface BorderBottomWidthDeclaration extends Declaration {}

  protected interface BorderLeftWidthDeclaration extends Declaration {}

  protected interface BorderDeclaration extends Declaration {}

  protected interface BorderTopDeclaration extends Declaration {}

  protected interface BorderRightDeclaration extends Declaration {}

  protected interface BorderBottomDeclaration extends Declaration {}

  protected interface BorderLeftDeclaration extends Declaration {}

  protected interface BottomDeclaration extends Declaration {}

  protected interface BoxShadowMultiDeclaration extends Declaration {}

  protected interface BoxShadowSingleDeclaration extends MultiDeclarationElement {}

  protected interface BoxSizingDeclaration extends Declaration {}

  protected interface ClearDeclaration extends Declaration {}

  protected interface ColorDeclaration extends Declaration {}

  protected interface ContentDeclaration extends Declaration {}

  protected interface CursorDeclaration extends Declaration {}

  protected interface DisplayDeclaration extends Declaration {}

  protected interface FlexDeclaration extends Declaration {}

  protected interface FlexBasisDeclaration extends Declaration {}

  protected interface FlexDirectionDeclaration extends Declaration {}

  protected interface FlexGrowDeclaration extends Declaration {}

  protected interface FlexShrinkDeclaration extends Declaration {}

  protected interface FlexWrapDeclaration extends Declaration {}

  protected interface FlexFlowDeclaration extends Declaration {}

  protected interface FloatDeclaration extends Declaration {}

  protected interface FontFamilyMultiDeclaration extends Declaration {}

  protected interface FontFamilySingleDeclaration extends MultiDeclarationElement {}

  protected interface FontDeclaration extends Declaration {}

  protected interface FontSizeDeclaration extends Declaration {}

  protected interface FontStyleDeclaration extends Declaration {}

  protected interface FontWeightDeclaration extends Declaration {}

  protected interface HeightDeclaration extends Declaration {}

  protected interface WidthDeclaration extends Declaration {}

  protected interface JustifyContentDeclaration extends Declaration {}

  protected interface JustifyItemsDeclaration extends Declaration {}

  protected interface JustifySelfDeclaration extends Declaration {}

  protected interface LeftDeclaration extends Declaration {}

  protected interface LetterSpacingDeclaration extends Declaration {}

  protected interface LineHeightDeclaration extends Declaration {}

  protected interface ListStyleImageDeclaration extends Declaration {}

  protected interface ListStylePositionDeclaration extends Declaration {}

  protected interface ListStyleTypeDeclaration extends Declaration {}

  protected interface ListStyleDeclaration extends Declaration {}

  protected interface MarginDeclaration extends Declaration {}

  protected interface MarginTopDeclaration extends Declaration {}

  protected interface MarginRightDeclaration extends Declaration {}

  protected interface MarginBottomDeclaration extends Declaration {}

  protected interface MarginLeftDeclaration extends Declaration {}

  protected interface MaxHeightDeclaration extends Declaration {}

  protected interface MaxWidthDeclaration extends Declaration {}

  protected interface MinHeightDeclaration extends Declaration {}

  protected interface MinWidthDeclaration extends Declaration {}

  protected interface ObjectFitDeclaration extends Declaration {}

  protected interface OpacityDeclaration extends Declaration {}

  protected interface OutlineColorDeclaration extends Declaration {}

  protected interface OutlineOffsetDeclaration extends Declaration {}

  protected interface OutlineStyleDeclaration extends Declaration {}

  protected interface OutlineWidthDeclaration extends Declaration {}

  protected interface OutlineDeclaration extends Declaration {}

  protected interface OverflowDeclaration extends Declaration {}

  protected interface OverflowBlockDeclaration extends Declaration {}

  protected interface OverflowInlineDeclaration extends Declaration {}

  protected interface OverflowXDeclaration extends Declaration {}

  protected interface OverflowYDeclaration extends Declaration {}

  protected interface PaddingDeclaration extends Declaration {}

  protected interface PaddingTopDeclaration extends Declaration {}

  protected interface PaddingRightDeclaration extends Declaration {}

  protected interface PaddingBottomDeclaration extends Declaration {}

  protected interface PaddingLeftDeclaration extends Declaration {}

  protected interface PositionDeclaration extends Declaration {}

  protected interface ResizeDeclaration extends Declaration {}

  protected interface RightDeclaration extends Declaration {}

  protected interface TabSizeDeclaration extends Declaration {}

  protected interface MozTabSizeDeclaration extends Declaration {}

  protected interface TextAlignDeclaration extends Declaration {}

  protected interface TextDecorationColorDeclaration extends Declaration {}

  protected interface TextDecorationLineDeclaration extends Declaration {}

  protected interface TextDecorationStyleDeclaration extends Declaration {}

  protected interface TextDecorationThicknessDeclaration extends Declaration {}

  protected interface TextDecorationDeclaration extends Declaration {}

  protected interface TextIndentDeclaration extends Declaration {}

  protected interface TextShadowDeclaration extends Declaration {}

  protected interface TextSizeAdjustDeclaration extends Declaration {}

  protected interface WebkitTextSizeAdjustDeclaration extends Declaration {}

  protected interface TextTransformDeclaration extends Declaration {}

  protected interface TopDeclaration extends Declaration {}

  protected interface TransformDeclaration extends Declaration {}

  protected interface VerticalAlignDeclaration extends Declaration {}

  protected interface WhiteSpaceDeclaration extends Declaration {}

  protected interface ZIndexDeclaration extends Declaration {}

  interface AnyDeclaration extends AlignContentDeclaration, AlignItemsDeclaration, AlignSelfDeclaration, AppearanceDeclaration, MozAppearanceDeclaration, WebkitAppearanceDeclaration, BackgroundAttachmentDeclaration, BackgroundClipDeclaration, BackgroundColorDeclaration, BackgroundImageDeclaration, BackgroundOriginDeclaration, BackgroundPositionDeclaration, BackgroundRepeatDeclaration, BackgroundSizeDeclaration, BackgroundDeclaration, BorderCollapseDeclaration, BorderColorDeclaration, BorderTopColorDeclaration, BorderRightColorDeclaration, BorderBottomColorDeclaration, BorderLeftColorDeclaration, BorderRadiusDeclaration, BorderTopLeftRadiusDeclaration, BorderTopRightRadiusDeclaration, BorderBottomRightRadiusDeclaration, BorderBottomLeftRadiusDeclaration, BorderStyleDeclaration, BorderTopStyleDeclaration, BorderRightStyleDeclaration, BorderBottomStyleDeclaration, BorderLeftStyleDeclaration, BorderWidthDeclaration, BorderTopWidthDeclaration, BorderRightWidthDeclaration, BorderBottomWidthDeclaration, BorderLeftWidthDeclaration, BorderDeclaration, BorderTopDeclaration, BorderRightDeclaration, BorderBottomDeclaration, BorderLeftDeclaration, BottomDeclaration, BoxShadowMultiDeclaration, BoxShadowSingleDeclaration, BoxSizingDeclaration, ClearDeclaration, ColorDeclaration, ContentDeclaration, CursorDeclaration, DisplayDeclaration, FlexDeclaration, FlexBasisDeclaration, FlexDirectionDeclaration, FlexGrowDeclaration, FlexShrinkDeclaration, FlexWrapDeclaration, FlexFlowDeclaration, FloatDeclaration, FontFamilyMultiDeclaration, FontFamilySingleDeclaration, FontDeclaration, FontSizeDeclaration, FontStyleDeclaration, FontWeightDeclaration, HeightDeclaration, WidthDeclaration, JustifyContentDeclaration, JustifyItemsDeclaration, JustifySelfDeclaration, LeftDeclaration, LetterSpacingDeclaration, LineHeightDeclaration, ListStyleImageDeclaration, ListStylePositionDeclaration, ListStyleTypeDeclaration, ListStyleDeclaration, MarginDeclaration, MarginTopDeclaration, MarginRightDeclaration, MarginBottomDeclaration, MarginLeftDeclaration, MaxHeightDeclaration, MaxWidthDeclaration, MinHeightDeclaration, MinWidthDeclaration, ObjectFitDeclaration, OpacityDeclaration, OutlineColorDeclaration, OutlineOffsetDeclaration, OutlineStyleDeclaration, OutlineWidthDeclaration, OutlineDeclaration, OverflowDeclaration, OverflowBlockDeclaration, OverflowInlineDeclaration, OverflowXDeclaration, OverflowYDeclaration, PaddingDeclaration, PaddingTopDeclaration, PaddingRightDeclaration, PaddingBottomDeclaration, PaddingLeftDeclaration, PositionDeclaration, ResizeDeclaration, RightDeclaration, TabSizeDeclaration, MozTabSizeDeclaration, TextAlignDeclaration, TextDecorationColorDeclaration, TextDecorationLineDeclaration, TextDecorationStyleDeclaration, TextDecorationThicknessDeclaration, TextDecorationDeclaration, TextIndentDeclaration, TextShadowDeclaration, TextSizeAdjustDeclaration, WebkitTextSizeAdjustDeclaration, TextTransformDeclaration, TopDeclaration, TransformDeclaration, VerticalAlignDeclaration, WhiteSpaceDeclaration, ZIndexDeclaration {}

  interface AnyFunction extends RotateFunction, RotateXFunction, RotateYFunction, RotateZFunction {}
}
