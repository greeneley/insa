
precision mediump float;


varying vec3 pixCenter;

// ============= STRUCT =============
struct ray
{
	vec3 O; // origine du rayon
	vec3 V; // point de destination
	float t;
};

struct plan
{
	vec3 O; // quelconque appartenant au plan
	vec3 n; // vecteur normal au plan
};
struct sphere
{
	vec3  C;     // Centre du cercle
	vec3  color; 
	float r;     // Rayon
};

struct source
{
	vec3 pos; // Position de la source
	vec3 pow; // Puissance de la source

};

// ============= GLOBAL =============
int NB_SOURCES = 2;

/* 
	##################################
	##################################
				  PLAN
	##################################
	################################## 
*/

// ==================================
float rayPlan(ray r, plan p)
{
	//TODO
	return -1.0;
}


// ==================================
float draw_plan(ray r, plan p)
{
	//TODO
	return -1.0;
}

// ==================================
float shine_plan(ray r, plan p)
{
	//TODO
	return -1.0;
}

/* 
	##################################
	##################################
				 SPHERES
	##################################
	################################## 
*/

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
void draw_sphere(ray r, sphere s)
{
	r.t = raySphere(r, s);
	if(r.t >= 0.0)
	{
		gl_FragColor = vec4(s.color, 1.0);
	}
}

// ==================================
void shine_sphere(ray r, sphere s, source t_src[10])
{
	r.t = raySphere(r, s);
	if(r.t >= 0.0) // ne touche pas
	{
		// Init de la luminance resultante
		vec3 L0 = vec3(0.0, 0.0, 0.0);

		// Calcul de la luminance resultatne
		for(int i=0; i<1000; i++)
		{
			if(i>=NB_SOURCES)
			{
				break;
			}

			float PI = 3.14;

			// Eclairage
			vec3   I        = r.O + r.t*r.V;
			vec3   N        = normalize(I - s.C);
			vec3   V        = normalize(t_src[i].pos - I);
			float cosThetaI = dot(N,V);
			
			// Brillance
			vec3 V0         = normalize(-r.V);
			vec3 H          = normalize(V0+V);
			float cosAlphaI = dot(H,N);

			// Calcul de la reflectance
			vec3 ks = vec3(0.1, 0.1, 0.1);
			vec3 kd = s.color;
			float n = 5.0;
			vec3 reflectance = (kd/PI) + ((n+2.0)/(2.0*PI))*ks*pow(cosAlphaI, n);

			// Addition d'une source de lumiere
			L0 = L0 + t_src[i].pow * reflectance * cosThetaI; 
		}
		
		gl_FragColor = vec4(L0, 1.0);
	}
}

// ==================================
void main(void) 
{
	// Par defaut la scene est blanche
	gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0) ;

	// Initialisation des structs
	ray    rayon  = ray(vec3(0.0, 0.0, 0.0), pixCenter, -1.0);
	sphere sph1   = sphere(vec3(-50.0, 200.0, 0.0), vec3(0.8, 0.1, 0.1), 25.0);
	sphere sph2   = sphere(vec3(20.0, 150.0, -20.0), vec3(0.1, 0.1, 0.8), 10.0);

	// Sources de lumiere
	source t_src[10];
	t_src[0] = source(vec3(0.0, 130.0, 150.0), vec3(10.0, 10.0, 10.0));
	t_src[1] = source(vec3(30.0, 130.0, -150.0), vec3(10.0, 10.0, 10.0));

	// Dessin
	draw_sphere(rayon, sph1);
	draw_sphere(rayon, sph2);

	// Eclairage
	shine_sphere(rayon, sph1, t_src);
	shine_sphere(rayon, sph2, t_src);
}
