
precision mediump float;


varying vec3 pixCenter;

// ========= STRUCT =========
struct ray
{
	vec3 O,V;
	float t;
};

struct sphere
{
	vec3 C;
	vec3 color;
	float r;
};

struct source
{
	vec3 pos;
	vec3 pow;

};

// ==================================
float raySphere(ray r, sphere s)
{
	vec3 OC = r.O - s.C;
	float A = dot(r.V, r.V);         // norme de V au carre
	float B = 2.0 * dot(OC, r.V);    // 2*(OC.V)
	float C = dot(OC, OC) - s.r*s.r; // norme(OC)*norme(OC) - r*r

	float delta = B*B - 4.0*A*C;

	if(delta >= 0.0)
	{
		float t1 = (-B - sqrt(delta)) / (2.0 * A);
		float t2 = (-B + sqrt(delta)) / (2.0 * A);

		if(t1 < 0.0 && t2 < 0.0)
		{
			return -1.0;
		}
		else if(t1 < 0.0)
		{
			return t2;
		}
		else if(t2 < 0.0)
		{
			return t1;
		}
		else
		{
			return min(t1,t2);
		}
	}
	return -1.0;
}


// ==================================
void draw_eclairage(ray r, sphere s, source src, int deja_eclaire)
{
	r.t = raySphere(r, s);
	if(r.t >= 0.0) // ne touche pas
	{
		float PI = 3.14;

		// Eclairage
		vec3   I       = r.O + r.t*r.V;
		vec3   N       = normalize(I - s.C);
		vec3   V       = normalize(src.pos - I);
		float cosTheta = dot(N,V);
		vec3 kd        = s.color;

		// Brillance
		vec3 V0        = normalize(-r.V);
		vec3 H         = normalize(V0+V);
		vec3 ks        = vec3(0.1, 0.1, 0.1);
		float n        = 10.0;
		float cosAlpha = dot(H,N);
		vec3 fr        = (kd/PI) + ((n+2.0)/(2.0*PI))*ks*pow(cosAlpha, n);

		// Luminance recue
		vec3 L0   = src.pow * fr * cosTheta; 

		if(deja_eclaire != 1)
		{
			gl_FragColor = vec4(L0, 1.0);
		}
		else
		{
			gl_FragColor = (vec4(L0, 1.0) + gl_FragColor) / 2.0;
		}
		
	}	
}


// ==================================
void main(void) 
{
	gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0) ;

	// Initialisation des structs
	ray    rayon  = ray(vec3(0.0, 0.0, 0.0), pixCenter, -1.0);
	sphere sph1   = sphere(vec3(-50.0, 200.0, 0.0), vec3(0.8, 0.1, 0.1), 25.0);
	sphere sph2   = sphere(vec3(20.0, 150.0, -20.0), vec3(0.1, 0.1, 0.8), 10.0);

	// Eclairage
	gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	source src   = source(vec3(0.0, 130.0, 150.0), vec3(10.0, 10.0, 10.0));
	draw_eclairage(rayon, sph1, src, 0);
	draw_eclairage(rayon, sph2, src, 0);
}
