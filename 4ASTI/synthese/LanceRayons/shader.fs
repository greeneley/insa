
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
int   NB_SOURCES = 0;
float PI         = 3.14;

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
void shine_sphere(ray r, sphere s, source t_src[10])
{
	r.t = raySphere(r, s);
	if(r.t >= 0.0)
	{
		// Init de la luminance resultante
		vec3 L0 = vec3(0.0, 0.0, 0.0);

		// Calcul de la luminance resultatne
		for(int i=0; i<10; i++)
		{
			// Pour eviter a devoir changer la taille de la loop
			if(i>=NB_SOURCES)
			{
				break;
			}

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
		
		// L0 est au final la somme de toutes les luminances	
		gl_FragColor = vec4(L0, 1.0);
	}
}

// ==================================
void ombre_sphere(source t_src[10], sphere s, plan p)
{
	for(int i=0; i<10; i++)
	{
		if(i >= NB_SOURCES)
		{
			break;
		}

		ray r = ray(t_src[i].pos, pixCenter, -1.0);
		r.t   = rayPlan(r, p);
		if(r.t >= 0.0)
		{
			// Si le rayon passe par le cercle
			//gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
		}
	}
	
}



// ==================================
void main(void) 
{
	// Par defaut la scene est blanche
	gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0) ;

	// Init des structs
	ray    rayon  = ray(vec3(0.0, 0.0, 0.0), pixCenter, -1.0);

	sphere sph1   = sphere(vec3(-10.0, 40.0, 0.0), vec3(0.8, 0.1, 0.1), 5.0);
	sphere sph2   = sphere(vec3(10.0, 40.0, 0.0), vec3(0.1, 0.1, 0.8), 5.0);

	plan   plan1  = plan(vec3(0.0, 0.0, -10.0), vec3(0.25, 0.0, 1.0), vec3(0.6, 0.3, 0.2));
	//plan   plan2  = plan(vec3(0.0, 0.0, 40.0), vec3(0.0, -1.0, 0.0), vec3(0.1, 0.1, 0.9));

	// Init des sources de lumiere
	source t_src[10];
	t_src[0] = source(vec3(-20.0, 35.0, 20.0), vec3(7.0, 7.0, 7.0));
	t_src[1] = source(vec3(20.0, 200.0, 20.0), vec3(7.0, 7.0, 7.0));

	NB_SOURCES = 2;

	// Dessin
	draw_plan(rayon, plan1);
	//draw_plan(rayon, plan2);

	draw_sphere(rayon, sph1);
	draw_sphere(rayon, sph2);

	// Eclairage
	//shine_plan(rayon, plan2, t_src);
	//shine_plan(rayon, plan1, t_src);

	shine_sphere(rayon, sph1, t_src);
	shine_sphere(rayon, sph2, t_src);

	// Ombre
	//ombre_sphere(t_src, sph1, plan1);
	//ombre_sphere(t_src, sph2, plan1);

}
