
// =====================================================
var gl;
var shadersLoaded = 0;
var vertShaderTxt;
var fragShaderTxt;
var shaderProgram;
var vertexBuffer;


// =====================================================
function webGLStart() {
	var canvas = document.getElementById("WebGL-test");
	initGL(canvas); // contexte OpenGL
	initBuffers();  // Init vertex
	loadShaders('shader');

	// Indique la couleur d'origine de la scene
	gl.clearColor(0.7, 0.7, 0.7, 1.0); 

	// Affichage de la scene
	// Probleme de synchro avec la lecture des fichiers
	//drawScene();
}

// =====================================================
function initGL(canvas)
{
	try {
		// gl est une var qui donne un lien entre la CG et le canva
		gl = canvas.getContext("experimental-webgl");

		// Ajout de variables a la volee
		gl.viewportWidth = canvas.width;
		gl.viewportHeight = canvas.height;

		// On dessine dans tous le canva (src) a (dst)
		gl.viewport(0, 0, canvas.width, canvas.height);
	} catch (e) {}
	if (!gl) {
		alert("Could not initialise WebGL");
	}
}


// =====================================================
function initBuffers() {
	vertexBuffer = gl.createBuffer();

	// On a qu'un seul buffer qui est lie a la fois
	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	vertices = [
							-0.3, -0.3,
							-0.3,  0.3,
							 0.3,  0.3,
							 0.3, -0.3
							];

	// On envoie les donnees a la CG
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

	// On a un tableau a une seule dimension donc on memorise
	vertexBuffer.itemSize = 2; // Le nombre de coordonnees
	vertexBuffer.numItems = 4; // Le nombre de points
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

	vshader = gl.createShader(gl.VERTEX_SHADER); // creation du shader
	gl.shaderSource(vshader, vShaderTxt);        // envoi du shader
	gl.compileShader(vshader); 
	if (!gl.getShaderParameter(vshader, gl.COMPILE_STATUS)) {
		alert(gl.getShaderInfoLog(vshader));
		return null;
	}

	fshader = gl.createShader(gl.FRAGMENT_SHADER);
	gl.shaderSource(fshader, fShaderTxt);
	gl.compileShader(fshader);
	if (!gl.getShaderParameter(fshader, gl.COMPILE_STATUS)) {
		alert(gl.getShaderInfoLog(fshader));
		return null;
	}

	shaderProgram = gl.createProgram();
	gl.attachShader(shaderProgram, vshader);
	gl.attachShader(shaderProgram, fshader);

	gl.linkProgram(shaderProgram);
	if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
		alert("Could not initialise shaders");
	}

	gl.useProgram(shaderProgram);

	shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "vertexPosition");
	gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);

	drawScene();
}



// =====================================================
function drawScene() {
	gl.clear(gl.COLOR_BUFFER_BIT);

	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute,
      vertexBuffer.itemSize, gl.FLOAT, false, 0, 0);

	//gl.drawArrays(gl.POINTS, 0, vertexBuffer.numItems);
	//gl.drawArrays(gl.LINES, 0, vertexBuffer.numItems);
	//gl.drawArrays(gl.LINE_LOOP, 0, vertexBuffer.numItems);
	//gl.drawArrays(gl.TRIANGLES, 0, vertexBuffer.numItems);
	gl.drawArrays(gl.TRIANGLE_FAN, 0, vertexBuffer.numItems);
	//gl.drawArrays(gl.TRIANGLE_STRIP, 0, vertexBuffer.numItems);
}
