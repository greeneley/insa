
// =====================================================
var gl;
var shadersLoaded = 0;
var vertShaderTxt;
var fragShaderTxt;
var shaderProgram = null;
var vertexBuffer;
var pixCenterBuffer;


var mvMatrix = mat4.create();
var pMatrix = mat4.create();
var objMatrix = mat4.create();
mat4.identity(objMatrix);



// =====================================================
function webGLStart() {
	var canvas = document.getElementById("WebGL-test");
	canvas.onmousedown = handleMouseDown;
	document.onmouseup = handleMouseUp;
	document.onmousemove = handleMouseMove;

	initGL(canvas);
	initBuffers();
	loadShaders('shader');

	gl.clearColor(0.7, 0.7, 0.7, 1.0);

	tick();
}

// =====================================================
function initGL(canvas)
{
	try {
		gl = canvas.getContext("experimental-webgl");
		gl.viewportWidth = canvas.width;
		gl.viewportHeight = canvas.height;
		gl.viewport(0, 0, canvas.width, canvas.height);
	} catch (e) {}
	if (!gl) {
		console.log("Could not initialise WebGL");
	}
}

// =====================================================
function initBuffers() {
	vertices = [
							-1.0, -1.0, // bas gauche
							-1.0,  1.0, // haut gauche
							 1.0,  1.0, // haut droite
							 1.0, -1.0  // bas droite
							];

	vertexBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
	vertexBuffer.itemSize = 2;
	vertexBuffer.numItems = 4;


	tPx2 = 36.0 / gl.viewportWidth  / 2.0; // taille d'un demi-pixel
	tPy2 = 24.0 / gl.viewportHeight / 2.0; // taille d'un demi-pixel
	pixCenters = [
							-18.0+tPx2, 50.0, -12.0+tPy2, // bas gauche
							-18.0+tPx2, 50.0,  12.0-tPy2, // haut gauche
							 18.0-tPx2, 50.0,  12.0-tPy2, // haut droite
							 18.0-tPx2, 50.0, -12.0+tPy2  // bas droite
							];

	pixCenterBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, pixCenterBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(pixCenters), gl.STATIC_DRAW);
	pixCenterBuffer.itemSize = 3;
	pixCenterBuffer.numItems = 4;
}


// =====================================================
function loadShaders(shader) {
	loadShaderText(shader,'.vs');
	loadShaderText(shader,'.fs');
}

// =====================================================
function loadShaderText(filename,ext) {   // technique car lecture asynchrone...
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
			if(ext=='.vs') { vertShaderTxt = xhttp.responseText; shadersLoaded ++; }
			if(ext=='.fs') { fragShaderTxt = xhttp.responseText; shadersLoaded ++; }
			if(shadersLoaded==2) {
				initShaders(vertShaderTxt,fragShaderTxt);
				shadersLoaded=0;
			}
    }
  }
  xhttp.open("GET", filename+ext, true);
  xhttp.send();
}

// =====================================================
function initShaders(vShaderTxt,fShaderTxt) {

	vshader = gl.createShader(gl.VERTEX_SHADER);
	gl.shaderSource(vshader, vShaderTxt);
	gl.compileShader(vshader);
	if (!gl.getShaderParameter(vshader, gl.COMPILE_STATUS)) {
		console.log(gl.getShaderInfoLog(vshader));
		return null;
	}

	fshader = gl.createShader(gl.FRAGMENT_SHADER);
	gl.shaderSource(fshader, fShaderTxt);
	gl.compileShader(fshader);
	if (!gl.getShaderParameter(fshader, gl.COMPILE_STATUS)) {
		console.log(gl.getShaderInfoLog(fshader));
		return null;
	}

	shaderProgram = gl.createProgram();
	gl.attachShader(shaderProgram, vshader);
	gl.attachShader(shaderProgram, fshader);

	gl.linkProgram(shaderProgram);

	if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
		console.log("Could not initialise shaders");
	}

	gl.useProgram(shaderProgram);

	// On recupere les attributes dans un tableau
	shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
	// On indique ici que ces attributes peuvent etre recuperes par la CG dans un tableau
	gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);

	// On recupere les attributes dans un tableau
	shaderProgram.pixCenterAttribute = gl.getAttribLocation(shaderProgram, "aPixCenterPosition");
	// On indique ici que ces attributes peuvent etre recuperes par la CG dans un tableau
	gl.enableVertexAttribArray(shaderProgram.pixCenterAttribute);


	// Demande d'acces aux variables uPMatrix et uMVMatrix et on recoit un identifiant
	shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
	shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
}


// =====================================================
function setMatrixUniforms() {
	if(shaderProgram != null) {
		gl.uniformMatrix3fv(shaderProgram.pMatrixUniform, false, mat4.toMat3(pMatrix));
		gl.uniformMatrix3fv(shaderProgram.mvMatrixUniform, false, mat4.toMat3(mvMatrix));
	}
}

// =====================================================
function drawScene() {
  gl.clear(gl.COLOR_BUFFER_BIT);

  if(shaderProgram != null) {
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute,
                           vertexBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, pixCenterBuffer);
    gl.vertexAttribPointer(shaderProgram.pixCenterAttribute,
                           pixCenterBuffer.itemSize, gl.FLOAT, false, 0, 0);
    
    mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);
    mat4.identity(mvMatrix);
    mat4.translate(mvMatrix, [0.0, 0.0, -3.0]);
    mat4.multiply(mvMatrix, objMatrix);

    setMatrixUniforms();

    gl.drawArrays(gl.TRIANGLE_FAN, 0, vertexBuffer.numItems);
  }
}
