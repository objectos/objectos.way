suite("data-on-click test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("submit", function() {
    const clickId = "f";
    const onClick = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "a", "b"]
    ]);
    make(`
		<div id='${clickId}' data-on-click='${onClick}'>
		<div id='subject' class='a x'></div>
		</div>		
		`);

    byId(clickId).click();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 2);
    assert.isFalse(l.contains("a"));
    assert.isTrue(l.contains("b"));
    assert.isTrue(l.contains("x"));
  });

  test("it should prevent anchor default", function() {
    const clickId = "f";
    const onClick = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "a", "b"]
    ]);
    make(`
		<div data-frame='x:foo'>
		<a id='${clickId}' data-on-click='${onClick}' href='/foo'>Foo</a>
		<div id='subject' class='a x'></div>
		</div>
		`);
    const page2 = makeElement("<html><div data-frame='x:bar'>Bar</div></html>");

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    byId(clickId).click();

    this.server.respond();

    assert.notEqual(workArea().innerHTML, page2.outerHTML);

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 2);
    assert.isFalse(l.contains("a"));
    assert.isTrue(l.contains("b"));
    assert.isTrue(l.contains("x"));
  });

});