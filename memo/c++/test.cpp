#include <iostream>

void f(int& a, int& b)
{
	a = 5;
	b = 6;
}


int main(int argc, char const *argv[])
{
	int nbA = 0;
	int nbB = 0;

	f(nbA, nbB);

	std::cout << nbA << std::endl << nbB << std::endl;
	return 0;
}