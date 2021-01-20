memset:
	push r12
	push r13
	push r14
	cmp $0,%rdx
	jz end
	mov $0,%r13
	mov %sil,%r12
loop_to_shift:
	
	shl $1,%rsi
	orl %r12,%rsi
	inc %r13          
	mov %r13,%r14          /* colocar o mesmo valor a cada byte do registo (8 bytes no total) */ 
	sub $8 , %r14
	jnz loop_to_shift 
	mov %rdi,%r12
	mov $0,%r11
	jmp multiplode8
	
	
multiplode8:
	mov $7,%r10 		   /* verificar se ptr e multiplo de 8 */
	anl %r12,%r10		   
	mov $0,%r11
	sub $7,%r10
	jz loop
	jmp ajustarByte/* caso nao seja realizar o ajuste  */
	
ajustarByte:
	mov %sil , (%rdi,%r11) /* alterar 1 byte de cada vez na memoria */
	inc %r11			   /* avançar para o byte seguinte */
	mov (%rdi,%r11), %rdi
	dec %rdx			   /* decrementar a len */
	cmp $0,%rdx 		   /* testar se chegou ao fim */
	jz end
	mov %rdi,%r12
	jmp multiplode8 /* voltar a testar se ja e multiplo de 8 */
loop:
	/* realizar o ajustamento 8 bytes em 8 bytes */
	mov %rsi , (%rdi,%r11)
	add $8,%r11
	sub $8,%rdx /* decrementar a len */
	jz end
	jmp loop

end:
	pop r12
	pop r13
	pop r14
	ret
