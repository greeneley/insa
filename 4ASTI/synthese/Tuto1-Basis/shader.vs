// attribute indique ici que la donnee provient d'un tableau (vertex data)
attribute vec2 vertexPosition;
void main(void) {
	// On a besoin de remplir les coordonnes manquantes : z et w
  gl_Position = vec4(vertexPosition, 0.0, 1.0);
}
