
.globl memove
.text

# RDI = dst
# RSI = src
# RDX = len

memove:
	jmp whilectrl
	
	
whilectrl:
	cmp 	$0, %rdx
	jbe     return

whileloop:
	cmp $8, %rdx
	jge long
	cmp $2, %rdx
	jge short
	
	mov %rsi, %rdi
	add $1, %rsi
	add $1, %rdi
	sub $1, %rdx
	jmp whilectrl
	
	
long:
	mov %rsi, %rdi
	add $8, %rsi
	add $8, %rdi
	sub $8, %rdx
	jmp whilectrl
	
short:
	mov %rsi, %rdi
	add $2, %rsi
	add $2, %rdi
	sub $2, %rdx
	jmp whilectrl


return:
	mov %rdi, %rax
	ret
	
  .end	
