#pragma once

typedef unsigned long int Huge;

struct Cles
{
	Huge e;
	Huge n;
	Huge d;
};
typedef struct Cles Cles;

Huge gcd (Huge a, Huge b);
Cles* keygen(Huge P, Huge Q);
Huge rsa_crypt(Cles* key, Huge M);
Huge rsa_decrypt(Cles* key, Huge C);
static Huge modexp(Huge a, Huge b, Huge n);
