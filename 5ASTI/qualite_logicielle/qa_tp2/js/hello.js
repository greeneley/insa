function hello(str) {
    if(str === undefined)        throw new Error("Variable is not set");
    if(str === null)             throw new Error("Input is null");
    if(typeof(str) !== "string") throw new Error("Input is not a string");

    return "Bonjour, "+str;
}