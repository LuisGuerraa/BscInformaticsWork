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

Check_point *get_longer_working_day(Worker *worker);typedef struct personal_info {
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



.globl get_longer_working_day
	.text
	
# RDI = worker
# RSI = list
# RDX = max
# RCX = aux / list->exit_time
# R8 = list->entry_time
# RAX = infopointer

get_longer_working_day:
	xor 	%rdx, %rdx		
	xor 	%rax, %rax		
	mov		16(%rdi), %rsi	# list = worker->check_point_list
while_control:
	
	test	%rsi, %rsi		# list != 0
	jnz		while_loop
	
	ret
		
while_loop:
	mov		(%rsi), %r8		# r8 = list.entry_time
	mov		8(%rsi), %rcx	# rcx = list.exit_time
	sub		%r8, %rcx		# rcx = aux = exit_time - entry_time
	cmp		%rcx, %rdx		# max - aux
	jae		nextinfo		# max >= aux
	mov		%rcx, %rdx		# max = aux
	mov 	%rsi, %rax
	
nextinfo:
	
	mov		16(%rsi), %rsi
	jmp		while_control
	
	
	.end
	