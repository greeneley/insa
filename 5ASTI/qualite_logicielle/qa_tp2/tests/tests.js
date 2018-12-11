QUnit.test( "hello test", function( assert ) {
    assert.ok( 1 == "1", "Passed!" );
});


QUnit.test("isEven", function(assert) {
    assert.expect(6);

    assert.throws(
        function() {isEven(undefined)},
        new Error("Variable is not set"),
        "undefined"
    );

    assert.throws(
        function() {isEven(null)},
        new Error("Number is null"),
        "null"
    );
    
    assert.throws(
        function() {isEven("foobar")},
        new Error("Input is not a number"),
        "Not a number"
    );

    assert.ok(isEven(0), "0 is even");
    assert.ok(isEven(2), "2 is even");
    assert.notOk(isEven(1), "1 is not even");

});

QUnit.test("hello", function(assert){
    assert.expect(4);

    assert.throws(
        function() {hello(undefined)},
        Error, "undefined"
    );

    assert.throws(
        function() {hello(null)},
        Error, "null"
    )

    assert.throws(
        function() {hello(56)},
        Error, "Not a string"
    );

    assert.equal("Bonjour, foobar", hello("foobar"), "Bonjour, foobar");
});

QUnit.test("checkSocialid", function(assert) {
    assert.expect(8);

    assert.throws(
        function() {checkSocialId(undefined)},
        Error, "undefined"
    );

    assert.throws(
        function() {checkSocialId(null)},
        Error, "undefined"
    );

    assert.throws(
        function() {checkSocialId("574345676765432")},
        Error, "Not a number"
    );

    assert.throws(
        function() {checkSocialId(-5676543)},
        Error, "Number < 0"
    );

    assert.throws(
        function() {checkSocialId(654334)},
        Error, "Not enough digits"
    );

    assert.throws(
        function() {checkSocialId(9999999999999990)},
        Error, "Too much digits"
    );

    assert.notOk(checkSocialId(100000000000000), "Numero invalide");
    assert.ok(checkSocialId(255081416802538), "Numero valide");
});
