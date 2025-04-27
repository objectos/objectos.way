/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SequencedMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import objectos.way.Css.ThemeQueryEntry;
import objectos.way.Lang.InvalidClassFileException;

final class CssEngine implements Css.Engine, Consumer<String>, FileVisitor<Path> {

  record Notes(
      Note.Ref1<String> classNotFound,
      Note.Ref2<String, IOException> classIoError,
      Note.Ref2<String, Lang.InvalidClassFileException> classInvalid,
      Note.Ref1<String> classLoaded,
      Note.Ref1<String> classSkipped,

      Note.Ref2<Path, IOException> directoryDirError,
      Note.Ref2<Path, IOException> directoryFileError,

      Note.Ref2<Class<?>, Throwable> jarFileException,
      Note.Ref2<Class<?>, String> jarFileNull,

      Note.Long1 scanTime,
      Note.Long1 processTime,
      Note.Long1 totalTime,

      Note.Ref2<String, String> keyNotFound,
      Note.Ref3<String, String, Set<Css.Key>> matchNotFound,
      Note.Ref2<Css.Key, String> negativeNotSupported
  ) {

    static Notes get() {
      Class<?> s;
      s = CssEngine.class;

      return new Notes(
          Note.Ref1.create(s, "CNF", Note.WARN),
          Note.Ref2.create(s, "CIX", Note.WARN),
          Note.Ref2.create(s, "CFI", Note.WARN),
          Note.Ref1.create(s, "CLD", Note.TRACE),
          Note.Ref1.create(s, "CSK", Note.TRACE),

          Note.Ref2.create(s, "DDE", Note.WARN),
          Note.Ref2.create(s, "DFE", Note.WARN),

          Note.Ref2.create(s, "JER", Note.WARN),
          Note.Ref2.create(s, "JNV", Note.WARN),

          Note.Long1.create(s, "SCT", Note.INFO),
          Note.Long1.create(s, "PRT", Note.INFO),
          Note.Long1.create(s, "TOT", Note.INFO),

          Note.Ref2.create(s, "Css.Key not found", Note.DEBUG),
          Note.Ref3.create(s, "Match not found", Note.INFO),
          Note.Ref2.create(s, "Does not allow negative values", Note.WARN)
      );
    }

  }

  static final byte $SCAN = 1;
  static final byte $SCAN_CLASS = 2;
  static final byte $SCAN_CLASS_LOOP = 3;
  static final byte $SCAN_CLASS_NEXT = 4;
  static final byte $SCAN_DIRECTORY = 5;
  static final byte $SCAN_DIRECTORY_LOOP = 6;
  static final byte $SCAN_DIRECTORY_NEXT = 7;
  static final byte $SCAN_JAR = 8;
  static final byte $SCAN_JAR_LOOP = 9;
  static final byte $SCAN_JAR_NEXT = 10;

  static final byte $PROCESS = 11;
  static final byte $PROCESS_LOOP = 12;
  static final byte $PROCESS_NEXT = 13;

  static final byte $GENERATE = 14;
  static final byte $GENERATE_THEME = 15;
  static final byte $GENERATE_BASE = 16;
  static final byte $GENERATE_UTILITIES = 17;

  static final byte $OK = 18;

  private static final Notes NOTES = Notes.get();

  private final String base;

  private final Lang.ClassReader classReader;

  private Appendable css;

  private int entryIndex;

  private final Map<String, String> keywords;

  private long long0;

  private long long1;

  private final Notes notes = Notes.get();

  private final Note.Sink noteSink;

  private Object object0;

  private Object object1;

  private final Map<String, Css.Key> prefixes;

  private final StringBuilder sb = new StringBuilder();

  private final Iterable<? extends Class<?>> scanClasses;

  private final Iterable<? extends Path> scanDirectories;

  private final Iterable<? extends Class<?>> scanJars;

  private final Set<? extends Css.Layer> skipLayers;

  private byte state;

  private final Iterable<? extends Css.ThemeEntry> themeEntries;

  private final Iterable<? extends Map.Entry<String, List<Css.ThemeQueryEntry>>> themeQueryEntries;

  private final Map<String, Object> tokens = Util.createSequencedMap();

  private final SequencedMap<String, CssUtility> utilities = Util.createSequencedMap();

  private final Map<String, CssVariant> variants;

  CssEngine(CssEngineBuilder builder) {
    base = builder.base();

    classReader = builder.classReader();

    keywords = builder.keywords();

    noteSink = builder.noteSink();

    prefixes = builder.prefixes();

    scanClasses = builder.scanClasses();

    scanDirectories = builder.scanDirectories();

    scanJars = builder.scanJars();

    skipLayers = builder.skipLayers();

    themeEntries = builder.themeEntries();

    themeQueryEntries = builder.themeQueryEntries();

    variants = builder.variants();
  }

  static CssEngine create(Consumer<? super CssEngineBuilder> config) {
    final CssEngineBuilder builder;
    builder = new CssEngineBuilder();

    config.accept(builder);

    return builder.build();
  }

  static String generate(Consumer<? super CssEngineBuilder> config) {
    try {
      final CssEngine engine;
      engine = create(config);

      final StringBuilder out;
      out = new StringBuilder();

      engine.writeTo(out);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  @Override
  public final String contentType() {
    return "text/css; charset=utf-8";
  }

  @Override
  public final Charset charset() {
    return StandardCharsets.UTF_8;
  }

  @Override
  public final void writeTo(Appendable out) throws IOException {
    css = out;

    state = $SCAN;

    while (state < $OK) {
      state = execute(state);
    }

    css = null;
  }

  // ##################################################################
  // # BEGIN: State Machine
  // ##################################################################

  private byte execute(byte state) throws IOException {
    return switch (state) {
      case $SCAN -> executeScan();
      case $SCAN_CLASS -> executeScanClass();
      case $SCAN_CLASS_LOOP -> executeScanClassLoop();
      case $SCAN_CLASS_NEXT -> executeScanClassNext();
      case $SCAN_DIRECTORY -> executeScanDirectory();
      case $SCAN_DIRECTORY_LOOP -> executeScanDirectoryLoop();
      case $SCAN_DIRECTORY_NEXT -> executeScanDirectoryNext();
      case $SCAN_JAR -> executeScanJar();
      case $SCAN_JAR_LOOP -> executeScanJarLoop();
      case $SCAN_JAR_NEXT -> executeScanJarNext();

      case $PROCESS -> executeProcess();
      case $PROCESS_LOOP -> executeProcessLoop();
      case $PROCESS_NEXT -> executeProcessNext();

      case $GENERATE -> executeGenerate();
      case $GENERATE_THEME -> executeGenerateTheme();
      case $GENERATE_BASE -> executeGenerateBase();
      case $GENERATE_UTILITIES -> executeGenerateUtilities();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: State Machine
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan
  // ##################################################################

  private byte executeScan() {
    long0 = long1 = System.currentTimeMillis();

    object0 = object1 = null;

    sb.setLength(0);

    tokens.clear();

    return $SCAN_CLASS;
  }

  // ##################################################################
  // # BEGIN: Scan :: Class
  // ##################################################################

  private byte executeScanClass() {
    object0 = scanClasses.iterator();

    return $SCAN_CLASS_LOOP;
  }

  private byte executeScanClassLoop() {
    final Iterator<?> iterator;
    iterator = (Iterator<?>) object0;

    if (iterator.hasNext()) {
      object1 = iterator.next();

      return $SCAN_CLASS_NEXT;
    } else {
      return $SCAN_DIRECTORY;
    }
  }

  private byte executeScanClassNext() {
    final Class<?> next;
    next = (Class<?>) object1;

    final String binaryName;
    binaryName = next.getName();

    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    final ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    final InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      noteSink.send(NOTES.classNotFound, binaryName);

      return $SCAN_CLASS_LOOP;
    }

    final byte[] bytes;

    try (in) {
      final ByteArrayOutputStream out;
      out = new ByteArrayOutputStream();

      in.transferTo(out);

      bytes = out.toByteArray();
    } catch (IOException e) {
      noteSink.send(notes.classIoError, binaryName, e);

      return $SCAN_CLASS_LOOP;
    }

    try {
      classReader.init(bytes);

      classReader.visitStrings(this);

      noteSink.send(notes.classLoaded, binaryName);
    } catch (InvalidClassFileException e) {
      noteSink.send(notes.classInvalid, binaryName, e);
    }

    return $SCAN_CLASS_LOOP;
  }

  // ##################################################################
  // # END: Scan :: Class
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan :: Directory
  // ##################################################################

  private byte executeScanDirectory() {
    object0 = scanDirectories.iterator();

    return $SCAN_DIRECTORY_LOOP;
  }

  private byte executeScanDirectoryLoop() {
    final Iterator<?> iterator;
    iterator = (Iterator<?>) object0;

    if (iterator.hasNext()) {
      object1 = iterator.next();

      return $SCAN_DIRECTORY_NEXT;
    } else {
      return $SCAN_JAR;
    }
  }

  private byte executeScanDirectoryNext() {
    final Path directory;
    directory = (Path) object1;

    try {
      Files.walkFileTree(directory, this);
    } catch (IOException e) {
      noteSink.send(notes.directoryDirError, directory, e);
    }

    return $SCAN_DIRECTORY_LOOP;
  }

  @Override
  public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public final FileVisitResult postVisitDirectory(Path dir, IOException exc) {
    if (exc != null) {
      noteSink.send(notes.directoryDirError, dir, exc);
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
    final Path fileNamePath;
    fileNamePath = file.getFileName();

    final String fileName;
    fileName = fileNamePath.toString();

    if (!fileName.endsWith(".class")) {
      return FileVisitResult.CONTINUE;
    }

    try {
      final byte[] bytes;
      bytes = Files.readAllBytes(file);

      executeScanBytes(fileName, bytes);
    } catch (IOException e) {
      noteSink.send(notes.directoryFileError, file, e);
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public final FileVisitResult visitFileFailed(Path file, IOException exc) {
    return FileVisitResult.CONTINUE;
  }

  // ##################################################################
  // # END: Scan :: Directory
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan :: JAR
  // ##################################################################

  private byte executeScanJar() {
    object0 = scanJars.iterator();

    return $SCAN_JAR_LOOP;
  }

  private byte executeScanJarLoop() {
    final Iterator<?> iterator;
    iterator = (Iterator<?>) object0;

    if (iterator.hasNext()) {
      object1 = iterator.next();

      return $SCAN_JAR_NEXT;
    } else {
      noteTime(notes.scanTime, long1);

      return $PROCESS;
    }
  }

  private byte executeScanJarNext() {
    final Class<?> clazz;
    clazz = (Class<?>) object1;

    final ProtectionDomain domain;

    try {
      domain = clazz.getProtectionDomain();
    } catch (SecurityException e) {
      noteSink.send(notes.jarFileException, clazz, e);

      return $SCAN_JAR_LOOP;
    }

    final CodeSource source;
    source = domain.getCodeSource();

    if (source == null) {
      noteSink.send(notes.jarFileNull, clazz, "CodeSource");

      return $SCAN_JAR_LOOP;
    }

    final URL location;
    location = source.getLocation();

    if (location == null) {
      noteSink.send(notes.jarFileNull, clazz, "Location");

      return $SCAN_JAR_LOOP;
    }

    final File file;

    try {
      final URI uri;
      uri = location.toURI();

      file = new File(uri);
    } catch (URISyntaxException e) {
      noteSink.send(notes.jarFileException, clazz, e);

      return $SCAN_JAR_LOOP;
    }

    try (JarFile jar = new JarFile(file)) {
      final Enumeration<JarEntry> entries;
      entries = jar.entries();

      while (entries.hasMoreElements()) {
        final JarEntry entry;
        entry = entries.nextElement();

        final String entryName;
        entryName = entry.getName();

        if (!entryName.endsWith(".class")) {
          continue;
        }

        final long size;
        size = entry.getSize();

        final int intSize;
        intSize = Math.toIntExact(size);

        final byte[] bytes;
        bytes = new byte[intSize];

        final int bytesRead;

        try (InputStream in = jar.getInputStream(entry)) {
          bytesRead = in.read(bytes, 0, bytes.length);
        }

        if (bytesRead != bytes.length) {
          continue;
        }

        executeScanBytes(entryName, bytes);
      }
    } catch (ArithmeticException | IOException e) {
      noteSink.send(notes.jarFileException, clazz, e);
    }

    return $SCAN_JAR_LOOP;
  }

  // ##################################################################
  // # END: Scan :: JAR
  // ##################################################################

  private void executeScanBytes(String fileName, byte[] bytes) {
    try {

      classReader.init(bytes);

      if (classReader.annotatedWith(Css.Source.class)) {
        classReader.visitStrings(this);

        noteSink.send(notes.classLoaded, fileName);
      } else {
        noteSink.send(notes.classSkipped, fileName);
      }

    } catch (Lang.InvalidClassFileException e) {
      noteSink.send(notes.classInvalid, fileName, e);
    }
  }

  @Override
  public final void accept(String value) {
    enum Splitter {
      NORMAL,

      WORD,

      TOKEN;
    }

    Splitter parser;
    parser = Splitter.NORMAL;

    for (int idx = 0, len = value.length(); idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (Character.isWhitespace(c)) {
            parser = Splitter.NORMAL;
          }

          else {
            parser = Splitter.WORD;

            sb.setLength(0);

            sb.append(c);
          }
        }

        case WORD -> {
          if (Character.isWhitespace(c)) {
            parser = Splitter.NORMAL;
          }

          else if (c == ':') {
            parser = Splitter.TOKEN;

            sb.append(c);
          }

          else {
            parser = Splitter.WORD;

            sb.append(c);
          }
        }

        case TOKEN -> {
          if (Character.isWhitespace(c)) {
            parser = Splitter.NORMAL;

            consumeToken();
          }

          else {
            parser = Splitter.TOKEN;

            sb.append(c);
          }
        }
      }
    }

    if (parser == Splitter.TOKEN) {
      consumeToken();
    }
  }

  private void consumeToken() {
    final String token;
    token = sb.toString();

    tokens.put(token, Boolean.TRUE);
  }

  // ##################################################################
  // # END: Scan
  // ##################################################################

  // ##################################################################
  // # BEGIN: Process
  // ##################################################################

  // object0 = iterator
  // object1 = next
  // long1 = start time

  private byte executeProcess() {
    final Set<String> keys;
    keys = tokens.keySet();

    object0 = keys.iterator();

    long1 = System.currentTimeMillis();

    return $PROCESS_LOOP;
  }

  private byte executeProcessLoop() {
    final Iterator<?> iterator;
    iterator = (Iterator<?>) object0;

    if (iterator.hasNext()) {
      object1 = iterator.next();

      return $PROCESS_NEXT;
    } else {
      noteTime(notes.processTime, long1);

      return $GENERATE;
    }
  }

  private byte executeProcessNext() {
    final String className;
    className = (String) object1;

    // split className on the ':' character
    classNameSlugs.clear();

    int beginIndex;
    beginIndex = 0;

    int colon;
    colon = className.indexOf(':');

    while (colon >= 0) {
      String slug;
      slug = className.substring(beginIndex, colon);

      if (slug.isEmpty()) {
        // TODO log invalid className

        return $PROCESS_LOOP;
      }

      classNameSlugs.add(slug);

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    // last slug = propValue
    final String propValue;
    propValue = className.substring(beginIndex);

    // process variants
    variantsOfClassName.clear();

    variantsOfAtRule.clear();

    final int parts;
    parts = classNameSlugs.size();

    if (parts > 1) {

      for (int idx = 0, max = parts - 1; idx < max; idx++) {
        final String variantName;
        variantName = classNameSlugs.get(idx);

        final CssVariant variant;
        variant = variantByName(variantName);

        if (variant == null) {
          // TODO log unknown variant name

          return $PROCESS_LOOP;
        }

        switch (variant) {
          case CssVariant.OfAtRule ofAtRule -> variantsOfAtRule.add(ofAtRule);

          case CssVariant.OfClassName ofClassName -> variantsOfClassName.add(ofClassName);
        }
      }

    }

    final String propName;
    propName = classNameSlugs.get(parts - 1);

    final Css.Key key;
    key = prefixes.get(propName);

    if (key == null) {
      // TODO log unknown property name

      return $PROCESS_LOOP;
    }

    final String formatted;
    formatted = formatValue(propValue);

    final CssModifier modifier;
    modifier = createModifier();

    final CssProperties.Builder properties;
    properties = new CssProperties.Builder();

    properties.add(propName, formatted);

    final CssUtility utility;
    utility = new CssUtility(key, className, modifier, properties);

    utilities.put(className, utility);

    return $PROCESS_LOOP;
  }

  // ##################################################################
  // # END: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: Generate
  // ##################################################################

  private byte executeGenerate() {
    long1 = System.currentTimeMillis();

    return $GENERATE_THEME;
  }

  private byte executeGenerateTheme() throws IOException {
    if (!skipLayers.contains(Css.Layer.THEME)) {
      executeGenerateTheme0();
    }

    return $GENERATE_BASE;
  }

  private void executeGenerateTheme0() throws IOException {
    writeln("@layer theme {");

    indent(1);

    writeln(":root {");

    for (Css.ThemeEntry entry : themeEntries) {
      indent(2);

      write(entry.name());
      write(": ");
      write(entry.value());
      writeln(';');
    }

    indent(1);

    writeln('}');

    for (Map.Entry<String, List<ThemeQueryEntry>> queryEntry : themeQueryEntries) {
      indent(1);

      String query;
      query = queryEntry.getKey();

      write(query);
      writeln(" {");

      indent(2);

      writeln(":root {");

      for (ThemeQueryEntry entry : queryEntry.getValue()) {
        indent(3);

        write(entry.name());
        write(": ");
        write(entry.value());
        writeln(';');
      }

      indent(2);

      writeln('}');

      indent(1);

      writeln('}');
    }

    writeln('}');
  }

  private byte executeGenerateBase() throws IOException {
    if (!skipLayers.contains(Css.Layer.BASE)) {
      executeGenerateBase0();
    }

    return $GENERATE_UTILITIES;
  }

  private void executeGenerateBase0() throws IOException {
    enum Parser {
      NORMAL,

      SLASH,

      COMMENT,
      COMMENT_STAR,

      TEXT,

      UNKNOWN;
    }

    Parser parser;
    parser = Parser.NORMAL;

    writeln("@layer base {");

    boolean indent;
    indent = true;

    int level;
    level = 1;

    for (int idx = 0, len = base.length(); idx < len; idx++) {
      final char c;
      c = base.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.NORMAL;
          }

          else if (c == '/') {
            parser = Parser.SLASH;
          }

          else if (c == '{') {
            parser = Parser.NORMAL;

            indent = true;

            level++;

            writeln(c);
          }

          else if (c == '}') {
            parser = Parser.NORMAL;

            indent = true;

            level--;

            indent(level);

            writeln(c);
          }

          else {
            parser = Parser.TEXT;

            if (indent) {
              indent(level);

              indent = false;
            }

            write(c);
          }
        }

        case SLASH -> {
          if (c == '*') {
            parser = Parser.COMMENT;
          }

          else {
            parser = Parser.UNKNOWN;

            write('/', c);
          }
        }

        case COMMENT -> {
          if (c == '*') {
            parser = Parser.COMMENT_STAR;
          }
        }

        case COMMENT_STAR -> {
          if (c == '*') {
            parser = Parser.COMMENT_STAR;
          }

          else if (c == '/') {
            parser = Parser.NORMAL;
          }

          else {
            parser = Parser.COMMENT;
          }
        }

        case TEXT -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.NORMAL;

            write(' ');
          }

          else if (c == '{') {
            throw new UnsupportedOperationException("Implement me");
          }

          else if (c == ';') {
            parser = Parser.NORMAL;

            indent = true;

            writeln(c);
          }

          else {
            parser = Parser.TEXT;

            write(c);
          }
        }

        case UNKNOWN -> write(c);
      }
    }

    writeln('}');
  }

  private byte executeGenerateUtilities() throws IOException {
    if (!skipLayers.contains(Css.Layer.UTILITIES)) {
      executeGenerateUtilities0();
    }

    noteTime(notes.totalTime, long0);

    return $OK;
  }

  private static final class Context {

    final CssVariant.OfAtRule parent;

    Map<CssVariant.OfAtRule, Context> atRules;

    List<CssUtility> utilities = Util.createList();

    Context(CssVariant.OfAtRule parent) {
      this.parent = parent;
    }

    final void add(CssUtility utility) {
      utilities.add(utility);
    }

    final Context nest(CssVariant.OfAtRule next) {
      if (atRules == null) {
        atRules = new TreeMap<>();
      }

      return atRules.computeIfAbsent(next, Context::new);
    }

  }

  private void executeGenerateUtilities0() throws IOException {
    final Context topLevel;
    topLevel = new Context(null);

    for (CssUtility utility : utilities.values()) {
      Context ctx;
      ctx = topLevel;

      final List<CssVariant.OfAtRule> modifierAtRules;
      modifierAtRules = utility.atRules();

      if (!modifierAtRules.isEmpty()) {

        final Iterator<CssVariant.OfAtRule> iterator;
        iterator = modifierAtRules.iterator();

        while (iterator.hasNext()) {
          ctx = ctx.nest(iterator.next());
        }

      }

      ctx.add(utility);

    }

    writeln("@layer utilities {");

    writeContents(topLevel, 1);

    writeln('}');
  }

  private void writeContents(Context ctx, int level) throws IOException {
    final List<CssUtility> list;
    list = ctx.utilities;

    list.sort(Comparator.naturalOrder());

    for (CssUtility utility : list) {
      writeUtility(utility, level);
    }

    final Map<CssVariant.OfAtRule, Context> atRules;
    atRules = ctx.atRules;

    if (atRules != null) {
      for (Context child : atRules.values()) {
        writeln();

        indent(level);

        final CssVariant.OfAtRule parent;
        parent = child.parent;

        write(parent.rule());

        writeln(" {");

        writeContents(child, level + 1);

        indent(level);

        writeln('}');
      }
    }
  }

  private void writeUtility(CssUtility u, int level) throws IOException {
    indent(level);

    sb.setLength(0);

    u.writeClassName(sb);

    write(sb);

    final CssProperties properties;
    properties = u.properties();

    switch (properties.size()) {
      case 0 -> write(" {}");

      case 1 -> {
        Entry<String, String> prop;
        prop = properties.get(0);

        writeBlockOne(prop);
      }

      case 2 -> {
        Entry<String, String> first;
        first = properties.get(0);

        Entry<String, String> second;
        second = properties.get(1);

        writeBlockTwo(first, second);
      }

      default -> writeBlockMany(level, properties);
    }

    writeln();
  }

  private void writeBlockOne(Map.Entry<String, String> property) throws IOException {
    blockStart();

    property(property);

    blockEnd();
  }

  private void writeBlockTwo(Map.Entry<String, String> prop1, Map.Entry<String, String> prop2) throws IOException {
    blockStart();

    property(prop1);

    nextProperty();

    property(prop2);

    blockEnd();
  }

  private void writeBlockMany(int level, CssProperties properties) throws IOException {
    blockStartMany();

    final int next;
    next = level + 1;

    for (Map.Entry<String, String> property : properties) {
      propertyMany(next, property);
    }

    blockEndMany(level);
  }

  private void blockStart() throws IOException {
    write(" { ");
  }

  private void blockStartMany() throws IOException {
    writeln(" {");
  }

  private void blockEnd() throws IOException {
    write(" }");
  }

  private void blockEndMany(int level) throws IOException {
    indent(level);

    write('}');
  }

  private void nextProperty() throws IOException {
    write("; ");
  }

  private void property(Map.Entry<String, String> property) throws IOException {
    final String name;
    name = property.getKey();

    write(name);

    write(": ");

    final String value;
    value = property.getValue();

    write(value);
  }

  private void propertyMany(int level, Entry<String, String> property) throws IOException {
    indent(level);

    property(property);

    writeln(';');
  }

  // ##################################################################
  // # END: Generate
  // ##################################################################

  // ##################################################################
  // # BEGIN: Write
  // ##################################################################

  private void indent(int level) throws IOException {
    for (int i = 0, count = level * 2; i < count; i++) {
      css.append(' ');
    }
  }

  private void write(char c) throws IOException {
    css.append(c);
  }

  private void write(char c1, char c2) throws IOException {
    css.append(c1);
    css.append(c2);
  }

  private void write(CharSequence s) throws IOException {
    css.append(s);
  }

  private void writeln() throws IOException {
    css.append('\n');
  }

  private void writeln(char c) throws IOException {
    css.append(c);
    css.append('\n');
  }

  private void writeln(String s) throws IOException {
    css.append(s);
    css.append('\n');
  }

  // ##################################################################
  // # END: Write
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utils
  // ##################################################################

  private void noteTime(Note.Long1 note, long startTime) {
    final long time;
    time = System.currentTimeMillis() - startTime;

    noteSink.send(note, time);
  }

  // ##################################################################
  // # END: Utils
  // ##################################################################

  private final List<String> classNameSlugs = Util.createList();

  private final List<CssVariant.OfAtRule> variantsOfAtRule = Util.createList();

  private final List<CssVariant.OfClassName> variantsOfClassName = Util.createList();

  private CssVariant variantByName(String name) {
    CssVariant result;
    result = variants.get(name);

    if (result != null) {
      return result;
    }

    result = variantByNameOfAttribute(name);

    if (result != null) {
      return result;
    }

    result = variantByNameOfElement(name);

    if (result != null) {
      return result;
    }

    result = variantByPseudoClass(name);

    if (result != null) {
      return result;
    }

    return variantByNameOfGroup(name);
  }

  private CssVariant variantByNameOfAttribute(String name) {
    final int length;
    length = name.length();

    if (length == 0) {
      return null;
    }

    final char first;
    first = name.charAt(0);

    if (first != '[') {
      return null;
    }

    final char last;
    last = name.charAt(length - 1);

    if (last != ']') {
      return null;
    }

    return new CssVariant.Suffix(name);
  }

  private CssVariant variantByNameOfElement(String name) {
    if (HtmlElementName.hasName(name)) {
      final CssVariant descendant;
      descendant = new CssVariant.Suffix(" " + name);

      final CssVariant maybeExisting;
      maybeExisting = variants.put(name, descendant);

      if (maybeExisting != null) {
        // TODO log?
      }

      return descendant;
    }

    return null;
  }

  private CssVariant variantByPseudoClass(String name) {
    final int openIndex;
    openIndex = name.indexOf('(');

    if (openIndex < 0) {
      return null;
    }

    final int closeIndex;
    closeIndex = name.lastIndexOf(')');

    if (closeIndex < 0) {
      return null;
    }

    final String maybe;
    maybe = name.substring(0, openIndex);

    final boolean validName;
    validName = switch (maybe) {
      case "nth-child" -> true;

      default -> false;
    };

    if (!validName) {
      return null;
    }

    return new CssVariant.Suffix(":" + name);
  }

  private CssVariant variantByNameOfGroup(String name) {
    final int dash;
    dash = name.indexOf('-');

    if (dash < 0) {
      return null;
    }

    String maybeGroup;
    maybeGroup = name.substring(0, dash);

    if (!maybeGroup.equals("group")) {
      return null;
    }

    int suffixIndex;
    suffixIndex = dash + 1;

    if (suffixIndex >= name.length()) {
      return null;
    }

    String suffix;
    suffix = name.substring(suffixIndex);

    CssVariant groupVariant;
    groupVariant = variants.get(suffix);

    if (groupVariant != null) {
      return variantByNameOfGroup(name, groupVariant);
    }

    groupVariant = variantByNameOfAttribute(suffix);

    if (groupVariant != null) {
      return variantByNameOfGroup(name, groupVariant);
    }

    return null;
  }

  private CssVariant variantByNameOfGroup(String name, CssVariant groupVariant) {
    CssVariant generatedGroupVariant;
    generatedGroupVariant = groupVariant.generateGroup();

    if (generatedGroupVariant == null) {
      return null;
    }

    final CssVariant maybeExisting;
    maybeExisting = variants.put(name, generatedGroupVariant);

    if (maybeExisting != null) {
      // TODO log existing?
    }

    return generatedGroupVariant;
  }

  private CssModifier createModifier() {
    if (variantsOfAtRule.isEmpty() && variantsOfClassName.isEmpty()) {
      return CssModifier.EMPTY_MODIFIER;
    } else {
      return new CssModifier(
          Util.toUnmodifiableList(variantsOfAtRule),
          Util.toUnmodifiableList(variantsOfClassName)
      );
    }
  }

  @Lang.VisibleForTesting
  final String formatValue(String value) {
    sb.setLength(0);

    entryIndex = 0;

    int index = 0;

    final int length;
    length = value.length();

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (Ascii.isDigit(c)) {
        index = formatValueNumeric(value, index);
      }

      else if (Ascii.isLetter(c)) {
        index = formatValueKeyword(value, index);
      }

      else if (c == '-') {
        index = formatValueNegative(value, index);
      }

      else if (isWhitespace(c)) {
        index = formatValueWhitespace(value, index);
      }

      else {
        index++;
      }
    }

    if (sb.isEmpty()) {
      return value;
    }

    formatValueNormal(value, length);

    return sb.toString();
  }

  private int formatValueKeyword(String value, int index) {
    final int length;
    length = value.length();

    // where the (possibly) keyword begins
    final int beginIndex;
    beginIndex = index;

    // consume initial char
    index++;

    enum State {

      KEYWORD,

      SLASH,

      OPACITY,

      INVALID;

    }

    State state;
    state = State.KEYWORD;

    int slashIndex;
    slashIndex = length;

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (isBoundary(c)) {
        break;
      }

      switch (state) {
        case KEYWORD -> {
          if (c == '/') {
            state = State.SLASH;

            slashIndex = index;
          } else {
            state = State.KEYWORD;
          }
        }

        case SLASH, OPACITY -> {
          if (Ascii.isDigit(c)) {
            state = State.OPACITY;
          } else {
            state = State.INVALID;
          }
        }

        case INVALID -> {
          state = State.INVALID;
        }
      }

      index++;
    }

    switch (state) {
      case KEYWORD -> {
        // extract keyword
        final String maybe;
        maybe = value.substring(beginIndex, index);

        // check for match
        final String keyword;
        keyword = keywords.get(maybe);

        if (keyword == null) {
          break;
        }

        formatValueNormal(value, beginIndex);

        sb.append(keyword);

        entryIndex = index;
      }

      case OPACITY -> {
        // extract keyword
        final String maybe;
        maybe = value.substring(beginIndex, slashIndex);

        // check for match
        final String keyword;
        keyword = keywords.get(maybe);

        if (keyword == null) {
          break;
        }

        formatValueNormal(value, beginIndex);

        final String opacity;
        opacity = value.substring(slashIndex + 1, index);

        sb.append("color-mix(in oklab, ");

        sb.append(keyword);

        sb.append(' ');

        sb.append(opacity);

        sb.append("%, transparent)");

        entryIndex = index;
      }

      case SLASH, INVALID -> {}
    }

    return index;
  }

  private void formatValueNormal(String value, int endIndex) {
    sb.append(value, entryIndex, endIndex);
  }

  private int formatValueNegative(String value, int index) {
    final int length;
    length = value.length();

    // where the number begins
    final int beginIndex;
    beginIndex = index;

    // consume '-'
    index++;

    if (index >= length) {
      // there're no more chars
      return index;
    }

    final char c;
    c = value.charAt(index);

    if (!Ascii.isDigit(c)) {
      return index;
    }

    return formatValueNumeric(value, beginIndex);
  }

  private enum FormatValueNumeric {

    INTEGER,

    DOT,

    DECIMAL;

  }

  private int formatValueNumeric(String value, int index) {
    final int length;
    length = value.length();

    // where the number begins
    final int beginIndex;
    beginIndex = index;

    // consume '-' or initial digit
    index++;

    // initial state
    FormatValueNumeric state;
    state = FormatValueNumeric.INTEGER;

    loop: while (index < length) {
      final char c;
      c = value.charAt(index);

      switch (state) {
        case INTEGER -> {
          if (Ascii.isDigit(c)) {
            state = FormatValueNumeric.INTEGER;
            index++;
          } else if (c == '.') {
            state = FormatValueNumeric.DOT;
            index++;
          } else {
            break loop;
          }
        }

        case DOT -> {
          if (Ascii.isDigit(c)) {
            state = FormatValueNumeric.DECIMAL;
            index++;
          } else {
            break loop;
          }
        }

        case DECIMAL -> {
          if (Ascii.isDigit(c)) {
            state = FormatValueNumeric.DECIMAL;
            index++;
          } else {
            break loop;
          }
        }
      }
    }

    // where the (maybe) unit begins
    final int unitIndex;
    unitIndex = index;

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (isBoundary(c)) {
        break;
      }

      index++;
    }

    if (state == FormatValueNumeric.DOT) {
      // invalid state
      return index;
    }

    // maybe rx value?

    final int unitLength;
    unitLength = index - unitIndex;

    if (unitLength != 2) {
      return index;
    }

    final char maybeR;
    maybeR = value.charAt(unitIndex);

    if (maybeR != 'r') {
      return index;
    }

    final char maybeX;
    maybeX = value.charAt(unitIndex + 1);

    if (maybeX != 'x') {
      return index;
    }

    // handle rx value

    // 1) emit normal value (if necessary)
    formatValueNormal(value, beginIndex);

    // 2) extract numeric value
    String number;
    number = value.substring(beginIndex, unitIndex);

    // 3) emit value
    sb.append("calc(");

    sb.append(number);

    sb.append(" / var(--rx) * 1rem)");

    // 4) update normal index
    entryIndex = index;

    return index;
  }

  private int formatValueWhitespace(String value, int index) {
    final int length;
    length = value.length();

    final int beginIndex;
    beginIndex = index;

    index++;

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (!isWhitespace(c)) {
        break;
      }

      index++;
    }

    formatValueNormal(value, beginIndex);

    sb.append(' ');

    entryIndex = index;

    return index;
  }

  private boolean isBoundary(char c) {
    return isSeparator(c) || isWhitespace(c);
  }

  private boolean isSeparator(char c) {
    return switch (c) {
      case ',', '(', ')' -> true;

      default -> false;
    };
  }

  private boolean isWhitespace(char c) {
    return c == '_';
  }

  // ##################################################################
  // # END: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: Test-only section
  // ##################################################################

  final void executeOne() throws IOException {
    state = execute(state);
  }

  final boolean shouldExecute(byte stop) {
    return state < stop;
  }

  final void state(byte value) {
    state = value;
  }

  final Set<String> tokens() {
    return tokens.keySet();
  }

  // ##################################################################
  // # END: Test-only section
  // ##################################################################

}