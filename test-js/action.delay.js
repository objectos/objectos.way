suite("Action::delay test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should execute the data-on-click action", function(done) {
    const clickMe = "click-me";

    const addClass = JSON.stringify([
      ["delay-0", 5, [["id-2", "subject", "toggle-class-0", "bar"]]]
    ]);

    make(`
		<div id='${clickMe}' data-on-click='${addClass}'>
		<div id='subject' class='foo'></div>
		</div>
		`);

    byId(clickMe).click();

    setTimeout(() => {
      const subject = byId("subject");

      const l = subject.classList;

      assert.equal(l.length, 2);
      assert.isTrue(l.contains("foo"));
      assert.isTrue(l.contains("bar"));

      done();
    }, 15);
  });

  test("it should execute the POST response action", function(done) {
    const clickMe = "click-me";

    make(`
    <form method='post' action='/test'>
    <button id='${clickMe}' type='submit'>Click Me</button>
    <div id='subject' class='foo'></div>
    </form>
    `);

    this.server.respondWith(
      "POST", "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["delay-0", 10, [["id-2", "subject", "toggle-class-0", "bar"]]]
      ])
    ]);

    byId(clickMe).click();

    this.server.respond();

    setTimeout(() => {
      const subject = byId("subject");

      const l = subject.classList;

      assert.equal(l.length, 2);
      assert.isTrue(l.contains("foo"));
      assert.isTrue(l.contains("bar"));

      done();
    }, 20);
  });

});