VPATH = .\src
SRCPATH = .\src
INCPATH = .\include
MIDPATH = .\pkg
OUTPUTPATN = .\bin
LIBPATH = .\lib
LIBNAME = math
LIB = -L$(LIBPATH) -l$(LIBNAME)
INC = -I $(INCPATH)
OBJECTS = main.o add.o min.o
EXE = main.exe
CFLAGS := -O2 -g -std=c++11
TSFEXE = Transf.exe
$(EXE):main.o libmath.a
	g++ $(MIDPATH)\main.o $(LIB) $(CFLAGS) -o $(OUTPUTPATN)\$(EXE)
	echo exe build success!
libmath.a:add.o min.o
	ar -r $(LIBPATH)\libmath.a $(MIDPATH)\min.o $(MIDPATH)\add.o
$(OBJECTS):%.o:%.cpp
	g++ -c $(CFLAGS) $(INC) $< -o $(MIDPATH)\$@


include .\mk\clean.mk