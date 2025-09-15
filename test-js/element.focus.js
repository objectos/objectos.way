suite("Element::focus test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should focus the input (self)", function() {
    make(`
    <div>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
	<div data-frame='x:foo'></div>
    </div>`);

    const onLoad = JSON.stringify([
      ["element-2", "focus-0"]
    ]);

    const page2 = makeElement(`
	<html><div data-frame='x:bar'><input id='subject' type='text' data-on-load='${onLoad}'></div></html>
    `);

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    const subject = byId("subject");

	assert.isTrue(subject === document.activeElement);
  });

  test("it should focus the input (byId)", function() {
    const onClick = JSON.stringify([
      ["id-2", "subject", "focus-0"]
    ]);

    make(`
	<div>
	<input id='subject' type='text'>
	<button id='a' data-on-click='${onClick}'>focus</button>
	</div>
	`);

    const subject = byId("subject");
	
	assert.isFalse(subject === document.activeElement);
	
    byId('a').click();

	assert.isTrue(subject === document.activeElement);
  });

});