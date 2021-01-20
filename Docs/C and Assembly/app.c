#include <stdio.h>

typedef struct personal_info {
const char *name;
char genre;
int age;
} Personal_info;
typedef struct check_point {
long entry_time;
long exit_time;
struct check_point *next;
} Check_point;
typedef struct worker {
Personal_info identity;
Check_point *check_point_list;
} Worker;

Check_point *get_longer_working_day(Worker *worker);


int main(){
	char* p = "Constâncio";
	Check_point* infopointer ;
	
	Personal_info info = {Constâncio, 'M', 50};
	Personal_info info1 = {Maria, 'F', 22};
	
	Check_point check1;
	Check_point check2;
	
	check1.entry_time = 500;
	check1.exit_time = 750;
	check1.next = &cp2;
	
	check2.entry_time = 4600;
	check2.exit_time = 4700;
	check2.next = 0; //over
	

	Worker Constâncio = {info, &check1};
	
	infopointer = get_longer_working_day(&Constâncio);


	printf("Left Time : %ld\n", infopointer->exit_time);
	printf("Entry time : %ld\n", infopointer->entry_time);
	printf("intervalo de tempo = %ld\n", infopointer->exit_time - infopointer->entry_time);
	
	
	return 0;
}