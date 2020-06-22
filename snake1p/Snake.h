#include <ncurses.h>
#include <stdlib.h>
#include <time.h>

typedef enum { UP, DOWN, LEFT, RIGHT } Direction;

typedef struct board {
	struct Snake *snake;
	int maxx;
	int maxy;
	struct Food *food;
} Board;

typedef struct Position {
	int x;
	int y;
} Position;

void Feed();
Position *Detect(struct Snake *snake);
typedef struct Snake {
	struct Node *tail;
	struct Node *head;	// head points to first Node
	Direction direction;
} Snake;

// verb
//

//verbs
Snake *Snake_init();

void Snake_move(Snake * snake, int p);

Direction Snake_turn(Snake * snake, int p);

void Snake_eat(struct Node *tail);

void Snake_del(Snake * snake);

typedef struct Node {
	Position *position;
	struct Node *next;
} Node;

Node *Node_init();

void Node_set(Node * p, int x, int y, struct Node *next);

void Node_del(Node * p);

void Node_print(Node * p);

typedef struct Food {
	Position *position;
} Food;

int Food_init(int x, int y);
void Food_del(Food * food);
