suite("Element::showModal test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("on-click should open the modal", function() {
    const onClick = JSON.stringify([
      ["id-2", "subject", "show-modal-0"]
    ]);

    make(`
	<div>
	<dialog id='subject'>Contents</dialog>
	<button id='a' data-on-click='${onClick}'>open</button>
	</div>
	`);

    byId('a').click();

    const subject = byId("subject");

    assert.isTrue(subject.open);
	
	subject.close();
  });

  test("on-load should open the modal", function() {
    make(`
    <div>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
	<div data-frame='x:foo'></div>
    </div>`);

    const onLoad = JSON.stringify([
      ["element-2", "show-modal-0"]
    ]);

    const page2 = makeElement(`
	<html>
	<div data-frame='x:bar'>
	<dialog id='subject' data-on-load='${onLoad}'></dialog>
	</div>
	</html>
    `);

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    const subject = byId("subject");

    assert.isTrue(subject.open);
	
	subject.close();
  });

});