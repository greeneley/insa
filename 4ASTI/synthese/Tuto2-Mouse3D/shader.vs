// attribute: indique qu'on recupere les donnees une a une dans un tableau de sommets
attribute vec3 aVertexPosition;

// uniform: donnees recuperees qu'une seule fois
uniform mat4 uMVMatrix; // Model View: rotation
uniform mat4 uPMatrix;  // Projection

void main(void) {
	gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1.0);
}
