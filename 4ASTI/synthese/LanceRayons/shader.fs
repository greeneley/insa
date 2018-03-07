
precision mediump float;


uniform vec3 uOriVector;
uniform int uTime;

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
int   NB_SOURCES = 0;
int   NB_SPHERES = 0;
float PI         = 3.14;

sphere t_sph[10];
source t_src[10];


/* 
	#################################
	#################################
			    CAMERA
	#################################
	################################# 
*/



/* 
	#################################
	#################################
			FONCTIONS PLAN
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
void shine_plan(ray r, plan p, source t_src[10])
{
	r.t = rayPlan(r, p);
	if(r.t >= 0.0)
	{
		// Init de la luminance resultante
		vec3 L0 = vec3(0.0, 0.0, 0.0);

		// Calcul de la luminance resultatne
		for(int i=0; i<1000; i++)
		{
			// Pour eviter a devoir changer la taille de la loop
			if(i>=NB_SOURCES)
			{
				break;
			}

			// Eclairage
			vec3   I        = r.O + r.t*r.V;
			vec3   N        = normalize(I - p.S);
			vec3   V        = normalize(t_src[i].pos - I);
			float cosThetaI = dot(N,V);
			
			// Brillance
			vec3 V0         = normalize(-r.V);
			vec3 H          = normalize(V0+V);
			float cosAlphaI = dot(H,N);

			// Calcul de la reflectance
			vec3 ks = vec3(0.1, 0.1, 0.1);
			vec3 kd = p.color;
			float n = 5.0;
			vec3 reflectance = (kd/PI) + ((n+2.0)/(2.0*PI))*ks*pow(cosAlphaI, n);

			// Addition d'une source de lumiere
			L0 = L0 + t_src[i].pow * reflectance * cosThetaI; 
		}
		
		// L0 est au final la somme de toutes les luminances	
		gl_FragColor = vec4(L0, 1.0);
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
void shine_sphere(ray r, sphere s)
{
	r.t = raySphere(r, s);
	if(r.t >= 0.0)
	{
		// Init de la luminance resultante
		vec3 LO = vec3(0.0, 0.0, 0.0);

		// Calcul de la luminance resultatne
		for(int i=0; i<100; i++)
		{
			// Pour eviter a devoir changer la taille de la loop
			if(i>=NB_SOURCES)
			{
				break;
			}

			// Eclairage
			vec3 I = r.O + r.t*r.V;

			// On verifie si il y a une ombre ou non
			for(int j=0; j<100; j++)
			{
				if(j >= NB_SPHERES)
				{
					break;
				}
				
				if(s.C != t_sph[j].C)
				{
					ray   r_sph = ray(I, t_src[i].pos, -1.0);
					float t_sph = raySphere(r_sph, t_sph[j]);
					if(t_sph >= 0.0)
					{
						LO = vec3(0.0, 0.0, 0.0);
					}
				}
				else
				{
					vec3   N        = normalize(I - s.C);
					vec3   V        = normalize(t_src[i].pos - I);
					float cosThetaI = dot(N,V);
					
					// Vecteurs brillance
					vec3 V0         = normalize(-r.V);
					vec3 H          = normalize(V0+V);
					float cosAlphaI = dot(H,N);

					// Calcul de la reflectance
					vec3 ks = vec3(0.1, 0.1, 0.1);
					vec3 kd = s.color;
					float n = 5.0;
					vec3 reflectance = (kd/PI) + ((n+2.0)/(2.0*PI))*ks*pow(cosAlphaI, n);

					// Addition d'une source de lumiere
					LO = LO + t_src[i].pow * reflectance * cosThetaI; 
				}

			}

		}
		
		// LO est au final la somme de toutes les luminances	
		gl_FragColor = vec4(LO, 1.0);
	}
}

// ==================================
void ombre_sphere(source t_src[10], sphere s, plan p)
{
	// TODO	
}


/* 
	##################################
	##################################
				SCENES
	##################################
	################################## 
*/
// ===================================
void scene_1(void) 
{
	// ===== Par defaut la scene est blanche
	gl_FragColor  = vec4(1.0, 1.0, 1.0, 1.0) ;

	// Init des structs
	ray    rayon  = ray(uOriVector, pixCenter, -1.0);

	// plan1 est legerement incline vers la droite et est en dessous de la vue
	plan   plan1  = plan(vec3(0.0, 0.0, -10.0), vec3(0.10, 0.0, 0.75), vec3(0.6, 0.3, 0.2));
	//plan   plan2  = plan(vec3(0.0, 0.0, 40.0), vec3(0.0, -1.0, 0.0), vec3(0.1, 0.1, 0.9));

	sphere sph0   = sphere(vec3(-20.0, 240.0+float(uTime), 0.0), vec3(0.8, 0.1, 0.1), 50.0);
	sphere sph1   = sphere(vec3(0.0, 200.0, 10.0), vec3(0.1, 0.1, 0.8), 10.0);
	t_sph[0]      = sph0;
	t_sph[1]      = sph1;

	NB_SPHERES = 2;

	// ===== Init des sources de lumiere
	t_src[0] = source(vec3(50.0, 100.0, 0.0), vec3(10.0, 10.0, 10.0));
	t_src[1] = source(vec3(-50.0, 100.0, 0.0), vec3(7.0, 7.0, 7.0));

	NB_SOURCES = 2;

	// ===== Dessin
	draw_plan(rayon, plan1);
	//draw_plan(rayon, plan2);

	for(int i=0; i<1000; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
		draw_sphere(rayon, t_sph[i]);
	}

	// ===== Eclairage
	//shine_plan(rayon, plan2, t_src);
	//shine_plan(rayon, plan1, t_src);

	for(int i=0; i<100; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
	shine_sphere(rayon, t_sph[i]);
	}

	// Ombre
	//ombre_sphere(t_src, sph1, plan1);
	//ombre_sphere(t_src, sph2, plan1);

}

void scene_SateliteOrbital(void) // Manque rotation orbitale  float(uTime) modulo 
{
	// ===== Par defaut la scene est noire
	gl_FragColor  = vec4(0.1, 0.1, 0.1, 1.0) ;

	// Init des structs
	ray    rayon  = ray(uOriVector, pixCenter, -1.0);


	sphere sph0   = sphere(vec3(-10.0, 300.0, 0.0), vec3(0.0, 0.0, 0.8), 40.0);
	sphere sph1   = sphere(vec3(50.0, 250.0, 10.0), vec3(0.88, 0.41, 0.0), 10.0);
	t_sph[0]      = sph0;
	t_sph[1]      = sph1;

	NB_SPHERES = 2;

	// ===== Init des sources de lumiere
	t_src[0] = source(vec3(200.0, 100.0, 0.0), vec3(10.0, 10.0, 10.0));
	//t_src[1] = source(vec3(-50.0, 100.0, 0.0), vec3(7.0, 7.0, 7.0));

	NB_SOURCES = 1;

	// ===== Dessin
	

	for(int i=0; i<1000; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
		draw_sphere(rayon, t_sph[i]);
	}

	for(int i=0; i<100; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
	shine_sphere(rayon, t_sph[i]);
	}
}
/*
void scene_Boite_Collier(void) // Rotation du collier horaire et changement des couleurs 
{
	// ===== Par defaut la scene est blancche
	gl_FragColor  = vec4(1.0, 1.0, 1.0, 1.0) ;

	// Init des structs
	ray    rayon  = ray(uOriVector, pixCenter, -1.0);

	// 5 plans pour la boite

	// 5 spheres pour le collier 
	 
	sphere sph0   = sphere(vec3(50.0, 250.0, 10.0), vec3(0.88, 0.41, 0.0), 10.0);
	sphere sph1   = sphere(vec3(50.0, 250.0, 10.0), vec3(0.88, 0.41, 0.0), 10.0);
	sphere sph2   = sphere(vec3(50.0, 250.0, 10.0), vec3(0.88, 0.41, 0.0), 10.0);
	sphere sph3   = sphere(vec3(50.0, 250.0, 10.0), vec3(0.88, 0.41, 0.0), 10.0);
	sphere sph4   = sphere(vec3(50.0, 250.0, 10.0), vec3(0.88, 0.41, 0.0), 10.0);
=	t_sph[0]      = sph0;
	t_sph[1]      = sph1;
	t_sph[2]      = sph2;
	t_sph[3]      = sph3;
	t_sph[4]      = sph4;

	NB_SPHERES = 5;

	// ===== Init des sources de lumiere
	t_src[0] = source(vec3(200.0, 100.0, 0.0), vec3(10.0, 10.0, 10.0));

	NB_SOURCES = 1;

	// ===== Dessin
	

	for(int i=0; i<1000; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
		draw_sphere(rayon, t_sph[i]);
	}

	for(int i=0; i<100; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
	shine_sphere(rayon, t_sph[i]);
	}
}
*/
void scene_Lueures(void) //  
{
	// ===== Par defaut la scene est noire
	gl_FragColor  = vec4(0.1, 0.1, 0.1, 1.0) ;

	// Init des structs
	ray    rayon  = ray(uOriVector, pixCenter, -1.0);

	// 1 plan pour le sol

	// 1 sphere  
	sphere sph0   = sphere(vec3(-10.0, 200.0, 0.0), vec3(0.8, 0.8, 0.8), 20.0);
	t_sph[0]      = sph0;
	NB_SPHERES = 1;

	// ===== Init des sources de lumiere
	t_src[0] = source(vec3(-200.0, 100.0, 0.0), vec3(10.0, 0.0, 0.0));
	t_src[1] = source(vec3(200.0, 100.0, 0.0), vec3(0.0, 0.0, 10.0));
	NB_SOURCES = 2;

	// ===== Dessin
	

	for(int i=0; i<1000; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
		draw_sphere(rayon, t_sph[i]);
	}

	for(int i=0; i<100; i++)
	{
		if(i >= NB_SPHERES)
		{
			break;
		}
	shine_sphere(rayon, t_sph[i]);
	}
}



// ==================================
void main(void)
{
	//scene_SateliteOrbital();
	//scene_Boite_Collier();
	scene_Lueures();
}

