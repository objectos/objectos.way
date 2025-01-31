suite("data-on-success test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("submit", function() {
    const clickMe = "click-me";

    const onSuccess = JSON.stringify([
      ["toggle-class-0", "subject", "remove-me"]
    ]);

    make(`
    <div data-frame='x:before'>
    <form action='/test' method='post' data-on-success='${onSuccess}'>
    <input id='${clickMe}' type='submit' value='click-me'>
    </form>
    </div>
    `);

    const page2 = makeElement(`
    <div data-frame='x:after'>
    <div id='subject' class='remove-me foo'></div>
    </div> 
    `);

    this.server.respondWith("POST", "/test", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const btn = byId(clickMe);

    btn.click();

    this.server.respond();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 1);
    assert.isTrue(l.contains("foo"));
    assert.isFalse(l.contains("remove-me"));
  });


});