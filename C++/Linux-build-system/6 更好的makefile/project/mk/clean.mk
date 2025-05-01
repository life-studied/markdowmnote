.PHONY:cleanall
cleanall:cleanobj cleanlib cleanapp
ifeq ($(OS),Windows_NT)
cleanobj:
	-del $(MIDPATH)\*.o 
cleanlib:
	-del $(LIBPATH)\*.a
cleanapp:
	-del $(OUTPUTPATN)\$(EXE)
else
UNAME := $(shell uname -s)
ifeq ($(UNAME),Linux)
cleanobj:
	-rm -f (MIDPATH)/*.o
cleanlib:
	-rm -f $(LIBPATH)/*.a
cleanapp:
	-rm -f $(OUTPUTPATN)/$(EXE)
else
endif
endif