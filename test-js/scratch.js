suite("Scratch: learning JS from scratch", function() {

  "use strict";

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });


  test("Does spread operator throw errors?", function() {
    function subject(arg0, arg1, arg2) {
      assert.equal(arg0, "First");
      assert.equal(arg1, "Second");
      assert.equal(arg2, "Third");
    }

    const args = ["First", "Second", "Third", "Fourth"];

    subject(...args);
  });

});