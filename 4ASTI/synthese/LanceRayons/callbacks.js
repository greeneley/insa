

// =====================================================
// Mouse management
// =====================================================
var mouseDown = false;
var lastMouseX = null;
var lastMouseY = null;
var rotY = 0;
var rotX = 0;

var Keys = {
        up: false,
        down: false,
        back: false,
        go:false,
        left: false,
        right: false
    };



// =====================================================
window.requestAnimFrame = (function()
{
	return window.requestAnimationFrame ||
         window.webkitRequestAnimationFrame ||
         window.mozRequestAnimationFrame ||
         window.oRequestAnimationFrame ||
         window.msRequestAnimationFrame ||
         function(/* function FrameRequestCallback */ callback,
									/* DOMElement Element */ element)
         {
            window.setTimeout(callback, 1000/60);
         };
})();

// ==========================================
function tick() {
	requestAnimFrame(tick);

	sensib=0.5; // Sensiblité
	if (Keys.up) {
	    vec3.add(oriVector, [0.0,0.0,sensib], oriVector); // Haut
	}
	else if (Keys.down) { 
	    vec3.add(oriVector, [0.0,0.0,-sensib], oriVector); // Bas
	}

	if (Keys.go) {
	    vec3.add(oriVector, [0.0,sensib,0.0], oriVector); // Avancer
	}
	else if (Keys.back) { 
	    vec3.add(oriVector, [0.0,-sensib,0.0], oriVector); // Reculer
	}

	if (Keys.left) {
	    vec3.add(oriVector, [-sensib,0.0,0.0], oriVector); // Gauche
	}
	else if (Keys.right) {
	    vec3.add(oriVector, [sensib,0.0,0.0], oriVector);  // Droite
	}
	//  -gauche/+droite  -reculer/+avancer   -bas/+haut
	
	
	// Ne prend effet que si la scene a ete modifiee
	drawScene();
}

// =====================================================
function degToRad(degrees) {
	return degrees * Math.PI / 180;
}

// =====================================================
function handleMouseDown(event) {
	mouseDown = true;
	lastMouseX = event.clientX;
	lastMouseY = event.clientY;
}


// =====================================================
function handleMouseUp(event) {
	mouseDown = false;
}


// =====================================================
function handleMouseMove(event) {
	if (!mouseDown) {
		return;
	}
	var newX = event.clientX;
	var newY = event.clientY;

	var deltaX = newX - lastMouseX;
	var deltaY = newY - lastMouseY;

	// Calcul des nouveaux angles de rotation
	rotY += degToRad(deltaX / 2);
	rotX += degToRad(deltaY / 2);
	
	// Creation/MaJ de la matrice de rotation
	mat4.identity(objMatrix);
	mat4.rotate(objMatrix, rotY*0.1, [0, 0, -1]);
	mat4.rotate(objMatrix, rotX*0.1, [1, 0, 0]);
	

	lastMouseX = newX
	lastMouseY = newY;
}


window.onkeydown = function(e) { // Bouton Appuyé
    var kc = e.keyCode;
    e.preventDefault();

    if      (kc === 81) Keys.left = true; // Q -> Gauche
    else if (kc === 68) Keys.right = true; // D -> Droite

    else if (kc === 83) Keys.back = true; // Z -> Avancer
    else if (kc === 90) Keys.go = true; // S -> Reculer
    
    else if (kc === 32) Keys.up = true; // Espace -> Haut
    else if (kc === 17) Keys.down = true; // Ctrl -> Bas


};

window.onkeyup = function(e) { // Bouton Relaché
    var kc = e.keyCode;
    e.preventDefault();

    if      (kc === 81) Keys.left = false; // Q -> Gauche
    else if (kc === 68) Keys.right = false; // D -> Droite

    else if (kc === 83) Keys.back = false; // Z -> Avancer
    else if (kc === 90) Keys.go = false; // S -> Reculer
    
    else if (kc === 32) Keys.up = false; // Espace -> Haut
    else if (kc === 17) Keys.down = false; // Ctrl -> Bas
};