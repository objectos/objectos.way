# AGENTS.md

Objectos Way: build web apps using only Java.

JPMS module `objectos.way`, target JDK 25 (`JAVA_RELEASE := 25`).

## Layout

- `main/` — production sources (module `objectos.way`). Public API under `objectos.*`; `objectos.internal` and the whole `objectox.*` tree are **internal/test-support code shipped in the jar** (the jar target excludes only `objectos/way/dev`).
- `test/` — TestNG tests.

## Projects

Objectos Way is divided in sub projects.

### Objectos HTML

Allows for declaring and generating HTML using pure Java.

The public, exported API is made of the following packages:

- `objectos.html`: defines the main API. Most users will only need to refer to this package.
- `objectos.html.format`: provides the API for formatting Objectos HTML documents
- `objectos.html.markup`: defines the interfaces that support the Objectos HTML declarative API
- `objectos.html.stream`: provides the interfaces that support the Objectos HTML streaming API

And the implementation classes are defined in the following packages:

- `objectox.html`
- `objectox.html.attr`
- `objectox.html.elem`
- `objectox.html.format`
- `objectox.html.markup`
- `objectox.html.stream`

## TDD

When writing tests, you may assume TDD is being used.

## Code style

### Class declarations

When adding methods to a class, keep them sorted. First, by access level:

- public
- default (package-private) access
- protected
- private

Then sorted alphabetically.

### Method invocations

Avoid having more than one method invocation in the same statement or sub-statement.

The following should be avoided:

```java
return append(s.subSequence(start, end));
```

The innermost method invocation should be declared as a local variable instead:

```java
final CharSequence sub;
sub = s.subSequence(start, end);

return append(sub);
```

Also, the use of method chaining should be minimized.

## Notes

- Not committed in this checkout is a git repo; do not rely on git workflow assumptions.
- Do not try to run the build.
- Do not try to compile classes.
- Do not try to run the tests.
- Just generate the code
