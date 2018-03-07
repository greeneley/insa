
precision mediump float;

varying vec3 pixCenter;


/* 
	#################################
	#################################
				STRUCTURES
	#################################
	#################################
*/

// ==================================
struct ray
{
	vec3 O; // origine du rayon
	vec3 V; // point de destination
	float t;
};

// ==================================
struct plan
{
	vec3 S; // quelconque appartenant au plan
	vec3 n; // vecteur normal au plan
	vec3 color;
};

// ==================================
struct sphere
{
	vec3  C;     // Centre du cercle
	vec3  color; 
	float r;     // Rayon
};

// ==================================
struct source
{
	vec3 pos; // Position de la source
	vec3 pow; // Puissance de la source

};


/* 
	#################################
	#################################
				GLOBAL
	#################################
	################################# 
*/
//// Scene 1
const int NB_SOURCES = 1;
const int NB_SPHERES = 2;
const int NB_PLANS   = 2;
/*/// Scene 2
const int NB_SOURCES = 1;
const int NB_SPHERES = 1;
const int NB_PLANS   = 4;
/*/


float PI             = 3.14;

// ==================================
sphere T_SPH[NB_SPHERES];
source T_SRC[NB_SOURCES];
plan   T_PLANS[NB_PLANS];


/* 
	#################################
	#################################
			FONCTIONS TIERS
	#################################
	################################# 
*/
// ==================================
float rayPlan(ray r, plan p)
{
	vec3 OS     = p.S - r.O; // Vecteur OS
	float denom = dot(p.n, r.V);
	if(denom != 0.0)
	{
		float t = (dot(OS, p.n) / denom);
		return t;
	}
	return -1.0;
}


// ==================================
float raySphere(ray r, sphere s)
{
	vec3 OC = r.O - s.C;
	float A = dot(r.V, r.V);         // norme de V au carre
	float B = 2.0 * dot(OC, r.V);    // 2*(OC.V)
	float C = dot(OC, OC) - (s.r*s.r); // norme(OC)*norme(OC) - r*r

	float delta = (B*B) - (4.0*A*C);

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
vec3 eclaire_ombre_point(ray r_src, vec3 pt_repere, source src_lum, vec3 kd)
{
	vec3  Li     = vec3(0.0, 0.0, 0.0);
	vec3  I      = r_src.O + r_src.t*r_src.V;
	ray   rayon  = ray(I, src_lum.pos, -1.0);
	bool  hidden = false;
	float t;

	for(int i=0; i<NB_SPHERES; i++)
	{
		t = raySphere(rayon, T_SPH[i]);
		if( (t >= 0.0) && (pt_repere != T_SPH[i].C))
		{
			hidden = true;
			break;
		}
	}

	if(!hidden)
	{
		// Eclairage
		vec3   N        = normalize(I - pt_repere);
		vec3   V        = normalize(src_lum.pos - I);
		float cosThetaI = dot(N,V);
		
		// Brillance
		vec3 V0         = normalize(-r_src.V);
		vec3 H          = normalize(V0+V);
		float cosAlphaI = dot(H,N);

		// Reflectance
		vec3 ks = vec3(0.1, 0.1, 0.1);
		float n = 5.0;
		vec3 reflectance = (kd/PI) + ((n+2.0)/(2.0*PI))*ks*pow(cosAlphaI, n);

		// Luminance issue de la source de lumiere
		Li = src_lum.pow * reflectance * cosThetaI; 
	}

	return Li;
}


/* 
	#################################
	#################################
			FONCTIONS PLAN
	#################################
	################################# 
*/
// ==================================
int draw_plan(ray r, plan p)
{
	r.t = rayPlan(r, p);
	if(r.t >= 0.0)
	{
		gl_FragColor = vec4(p.color, 1.0);
		return 1;
	}
	return -1;
}

// ==================================
void shine_plan(ray r, plan p)
{
	r.t = rayPlan(r, p);
	if(r.t >= 0.0)
	{
		// Init de la luminance resultante
		vec3 LO = vec3(0.0, 0.0, 0.0);

		// Calcul de la luminance resultante
		for(int i=0; i<NB_SOURCES; i++)
		{
			// Test d'ombre avec tous les objets
			LO += eclaire_ombre_point(r, p.S, T_SRC[i], p.color);
		}
		
		// LO est au final la somme de toutes les luminances	
		gl_FragColor = vec4(LO, 1.0);
	}
}


/* 
	##################################
	##################################
			FONCTIONS SPHERE
	##################################
	################################## 
*/

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
void shine_sphere(ray r, sphere s)
{
	r.t = raySphere(r, s);
	if(r.t >= 0.0)
	{
		// Init de la luminance resultante
		vec3 LO = vec3(0.0, 0.0, 0.0);

		// Calcul de la luminance resultante
		for(int i=0; i<NB_SOURCES; i++)
		{
			// Test d'ombre avec tous les objets
			LO += eclaire_ombre_point(r, s.C, T_SRC[i], s.color);
		}
		
		// LO est au final la somme de toutes les luminances	
		gl_FragColor = vec4(LO, 1.0);
	}
}

/* 
	##################################
	##################################
				SCENES
	##################################
	################################## 
*/
// ===================================
///
void scene_1(void) 
{
	// ===== Par defaut la scene est blanche
	gl_FragColor  = vec4(1.0, 1.0, 1.0, 1.0) ;

	// ===== Init des structs
	ray    rayon  = ray(vec3(0.0, 0.0, 0.0), pixCenter, -1.0);

	// Plans
	T_PLANS[0] = plan(vec3(0.0, 300, 0.0), vec3(0.0, -1.0, 0.0), vec3(0.3, 0.1, 0.8));
	T_PLANS[1] = plan(vec3(0.0, 0.0, -2.0), vec3(0.10, 0.0, 0.75), vec3(0.6, 0.3, 0.2));

	// Sources de lumieres
	//T_SRC[0] = source(vec3(40.0, 50.0, 0.0), vec3(5.0, 5.0, 5.0));
	T_SRC[0] = source(vec3(30.0, 80.0, 10.0), vec3(10.0, 10.0, 10.0));
	//T_SRC[0] = source(vec3(0.0, 180.0, 10.0),   vec3(5.0, 5.0, 5.0));

	// Spheres
	T_SPH[0]   = sphere(vec3(0.0, 80.0, 10.0), vec3(0.8, 0.1, 0.1), 5.0);
	T_SPH[1]   = sphere(vec3(10.0, 80.0, 10.0), vec3(0.1, 0.1, 0.8), 1.0);
	//T_SPH[2]   = sphere(lumiere, vec3(0.1, 0.1, 0.8), 1.0);

	// ===== Dessin
	for(int i=0; i<NB_PLANS; i++)
	{
		draw_plan(rayon, T_PLANS[i]);
	}

	for(int i=0; i<NB_SPHERES; i++)
	{
		draw_sphere(rayon, T_SPH[i]);
	}

	// ===== Eclairage
	for(int i=0; i<NB_PLANS; i++)
	{
		//shine_plan(rayon, T_PLANS[i]);
	}

	for(int i=0; i<NB_SPHERES; i++)
	{
		shine_sphere(rayon, T_SPH[i]);
	}
}

// ===================================
/*/// La scene represente une salle ou se situe une boule
void scene_2(void) 
{
	// ===== Par defaut la scene est blanche
	gl_FragColor  = vec4(1.0, 1.0, 1.0, 1.0) ;

	// ===== Init des structs
	ray    rayon  = ray(vec3(0.0, 0.0, 0.0), pixCenter, -1.0);

	// Plans
	T_PLANS[0] = plan(vec3(  0.0, 20.0,  0.0), vec3( 0.0, -1.0, 0.0), vec3(0.5, 0.5, 0.5));
	T_PLANS[1] = plan(vec3(  0.0, 20.0, -100.0), vec3( 0.0,  0.0, 1.0), vec3(0.8, 0.2, 0.2));
	T_PLANS[2] = plan(vec3( 50.0, 20.0,   0.0), vec3(-1.0,  0.25, 0.0), vec3(0.2, 0.8, 0.2));
	T_PLANS[3] = plan(vec3(-50.0, 20.0,   0.0), vec3( 1.0,  0.25, 0.0), vec3(0.2, 0.2, 0.8));

	// Sources de lumieres
	T_SRC[0] = source(vec3(10.0, 15.0, 15.0), vec3(10.0, 10.0, 10.0));

	// Spheres
	T_SPH[0] = sphere(vec3(0.0, 10.0, 0.0), vec3(0.8, 0.1, 0.1), 1.0);

	// ===== Dessin
	for(int i=0; i<NB_PLANS; i++)
	{
		//draw_plan(rayon, T_PLANS[i]);
	}

	for(int i=0; i<NB_SPHERES; i++)
	{
		//draw_sphere(rayon, T_SPH[i]);
	}

	// ===== Eclairage
	for(int i=0; i<NB_PLANS; i++)
	{
		shine_plan(rayon, T_PLANS[i]);
	}

	for(int i=0; i<NB_SPHERES; i++)
	{
		shine_sphere(rayon, T_SPH[i]);
	}
}

/*/// ==================================
void main(void)
{
	scene_1();
	//scene_2();
}

