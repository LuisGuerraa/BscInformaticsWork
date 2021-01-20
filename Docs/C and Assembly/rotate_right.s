rotate_right:	
	push r12
	push r13
	push r14
	
	
	mov $0,r10 
	cmp %r10,%rsi
	jnz loop            ;verificar se size = 0
	pop r12
	pop r13
	pop r14
	ret 
loop:

	mov $1,%r13
	shl $63,%r13        ;criar a mascara para comparar o ultimo bit do indice de menor peso
	mov (%rdi),%r11     ;carregar o 1º e 2º indice do array em registos 
	mov $8(rdi),%r12
	anl %r11,%r13       ;comparar o ultimo bit dos bits de menor peso do array 
	shr $1,r11
	mov %r13,%r14       ;aux 
	mov $1,%r13         ; criar a mascara para comparar o ultimo bit do maior peso 
	shl $63,%r13
	anl %r12,%r13       ;comparar o ultimo bit dos bits de maior peso do array
	shr	$1,r12
	shl $63,%r14        ;somar o valor correto aos bits de maior peso do array */
	add %r14,%r12
	mov %r13,%r14       ;aux
	shl $63,%r14
	add %r14,%r11       ;somar os valores
	
	inc r10 
	cmp %r10,%rsi
	jnz loop
	
	pop r12
	pop r13
	pop r14
	ret
	

