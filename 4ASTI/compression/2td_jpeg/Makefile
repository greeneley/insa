SRC= jpeg.cpp jpeg.h noeud.cpp noeud.h zigzag.cpp rle.cpp huffman.cpp
CFLAGS= -Wall -Wpedantic -std=c++11 `pkg-config --cflags --libs opencv`

all:
	g++ $(CFLAGS) $(SRC) -o jpeg
