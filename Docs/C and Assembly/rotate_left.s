rotate_right:	
	push r12
	push r13
	push r14
	
	;realizar o teste para verificar se o size e 0, se sim acabar o programa
	mov $0,r10 
	cmp %r10,%rsi
	jnz loop
	pop r12
	pop r13
	pop r14
	ret 
loop:

	mov $1,%r13
	shl $63,%r13        ;criar a mascara para comparar o ultimo bit do indice de menor peso
	
	
	;carregar o 1º e 2º indice do array em registos */
	mov (%rdi),%r11 
	mov $8(rdi),%r12
	
	/* comparar o ultimo bit dos bits de menor peso do array */
	anl %r11,%r13
	/* realizar o shift */
	shr $1,r11
	/* registo auxiliar */
	mov %r13,%r14
	
	/* criar a mascara para comparar o ultimo bit do maior peso */
	mov $1,%r13
	shl $63,%r13
	
	/* comparar o ultimo bit dos bits de maior peso do array */
	anl %r12,%r13
	/*realizar o shift */
	shr	$1,r12
	
	/* somar o valor correto aos bits de maior peso do array */
	shl $63,%r14
	add %r14,%r12
	
	/* registo auxiliar */
	mov %r13,%r14
	
	/* somar o valor correto aos bits de menor peso do array */
	shl $63,%r14
	add %r14,%r11
	
	inc r10 
	cmp %r10,%rsi
	jnz loop
	
	pop r12
	pop r13
	pop r14
	ret
	

