#include "Snake.h"
#include <ncurses.h>
#include <time.h>
#include <stdlib.h>
#include <unistd.h>
//rules
//can't hit edge
//can't touch itself
//eating increases length
//create the game
void Feed(int counter)
{
	int maxx, maxy, x, y;
	int tf = 0;
	getmaxyx(stdscr, maxx, maxy);
	srand(time(NULL));
	x = (rand() % maxx);
	y = (rand() % maxy);
	if ((counter % 16) > 14) {
		while (tf == 0) {
			tf = Food_init(x, y);
			x = (rand() % maxx);
			y = (rand() % maxy);
		}
	}
}

Snake *Snake_init()
{
	Snake *snake = malloc(sizeof(Snake));

	snake->tail = Node_init();
	snake->head = Node_init();
	Node *n = Node_init();
	Node *r = Node_init();

	Node_set(snake->tail, 1, 3, NULL);
	Node_set(n, 2, 3, snake->tail);
	Node_set(r, 3, 3, n);
	Node_set(snake->head, 4, 3, r);

	snake->direction = RIGHT;
	return snake;
}

Direction Snake_turn(Snake * snake, int p)
{
	switch (p) {
	case 'w':
		if (snake->direction != DOWN) {
			snake->direction = UP;
		}
		break;
	case 'a':
		if (snake->direction != RIGHT) {
			snake->direction = LEFT;
		}
		break;
	case 's':
		if (snake->direction != UP) {
			snake->direction = DOWN;
		}
		break;
	case 'd':
		if (snake->direction != LEFT) {
			snake->direction = RIGHT;
		}
		break;
	default:
		//      mvprintw(5, 5, "-");
		break;
	}
	// mvprintw(20, 20, "p is %c and %d", p, snake->direction);
	return snake->direction;
}

void Snake_move(Snake * snake, int p)
{
	//move snake in current direction
	//turn if other key is pressed
	//cant go backwards
	Node *n = Node_init();
	snake->direction = Snake_turn(snake, p);	// set internal

	switch (snake->direction) {
	case UP:
		n->position->x = snake->head->position->x;
		n->position->y = --snake->head->position->y;
		break;
	case DOWN:
		n->position->x = snake->head->position->x;
		n->position->y = ++snake->head->position->y;
		break;
	case LEFT:
		n->position->x = --snake->head->position->x;
		n->position->y = snake->head->position->y;
		break;
	case RIGHT:
		n->position->x = ++snake->head->position->x;
		n->position->y = snake->head->position->y;
		break;
	}
	//change head
	n->next = snake->head;

	// Node_set(x,y,snake->head);
	snake->head = n;
	//mvprintw(snake->head->position->y, snake->head->x, "X");
	//delete tail
	Node *current;

	current = snake->head;

	while (current->next->next != NULL) {
		current = current->next;
	}
	// mvprintw(current->y, current->x, "y");
	// mvprintw(current->next->y, current->next->x, "X");
	move(current->next->position->y, current->next->position->x);
	printw(" ");
	free(current->next);
	snake->tail = current;
	snake->tail->next = NULL;
	return;
}

void Snake_del(Snake * snake)
{
	Node *temp;
	temp = snake->head;
	while (temp->next != NULL) {
		Node *current;
		current = temp;
		temp = temp->next;
		free(current);
	}
}

Node *Node_init()
{
	Node *p = malloc(sizeof(Node));
	p->position = malloc(sizeof(Position));
	return p;
}

void Node_del(Node * p)
{
	free(p);
}

void Node_print(Node * p)
{
	mvprintw(p->position->y, p->position->x, "X");
}

void Node_set(Node * p, int x, int y, Node * next)
{
	p->position->x = x;
	p->position->y = y;
	p->next = next;
}

int Food_init(int x, int y)
{
	char p;
	move(y, x);
	p = inch();
	if (p == 'X' || p == '@') {
		return 0;
	}
	printw("@");
	refresh();
	return 1;
}

void Food_del(Food * food)
{
	free(food);
}

int main()
{
	initscr();
	WINDOW *win;
	win = stdscr;
	box(win, 'X', 'X');
	wrefresh(win);
	int p, counter;
	Snake *snake = Snake_init();
	noecho();
	while (p != 27) {
		timeout(0);
		p = getch();
		Snake_move(snake, p);
		Node_print(snake->head);
		Feed(counter);
		//debug
		refresh();
		usleep(200000);
		++counter;
	}
	Snake_del(snake);
	delwin(win);
	endwin();
	return 0;
}
