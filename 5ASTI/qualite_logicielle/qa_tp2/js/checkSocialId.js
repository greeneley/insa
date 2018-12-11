function checkSocialId(id)
{
    if(id === undefined)        throw new Error("Variable is not set");
    if(id === null)             throw new Error("Input is null");
    if(typeof(id) !== "number") throw new Error("Input is not a number");
    if(id < 0)                  throw new Error("Input must be positive");
    if(id < 100000000000000)    throw new Error("Input lacks digits (15 needed)");
    if(id > 999999999999999)    throw new Error("Input has too much digits(15 needed)");
    
    // Verification cle
    var controle = id % 100;
    var cle      = 97 - (Math.trunc(id / 100) % 97);
    return cle === controle;
}