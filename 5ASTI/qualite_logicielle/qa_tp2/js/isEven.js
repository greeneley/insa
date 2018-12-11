/**
 * Checks wether or not the input is a number.
 * @param {number} number 
 */
function isEven(number) {
    if(number === undefined)        throw new Error("Variable is not set");
    if(number === null)             throw new Error("Number is null");
    if(typeof(number) !== 'number') throw new Error("Input is not a number");
    
    return number%2 === 0;
}