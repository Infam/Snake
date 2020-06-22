CC=gcc
CFLAGS=-c -Wall
LDFLAGS=-lncurses
HEADER=$(wildcard *.h)
SRC=$(wildcard *.c)
OBJ=$(SRC:.c=.o)
EXEC=snake
DEP=$(OBJ:.o=.d)

all:$(SRC) $(EXEC)

$(EXEC): $(OBJ)
	$(CC) $(OBJ) $(LDFLAGS) -o $@

%.o: %.c
	@echo Compiling $<...
	$(CC) ${CFLAGS} $< -o $@

clean:
	rm ${OBJ} *.exe
