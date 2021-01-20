/*CheckPoints checkpoint = null;*/
/*for (int i = 0; i<worker_checkpoint_len;i++){*/
/*if(worker.checkpoint[i].entrytime>checkpoint.entrytime)*/
/*		checkpoint= worker.checkpoint[i];*/
/*	return checkpoint.exit_time - checkpoint.entry_time;*/
/*}*/




get_last_work_period :	
	mov 16(%rdi),%r12 /* len do array de checkpoints */
    test $0,%r12      /* testar se a len do array de checkpoints = 0*/
	jz END
	mov $0,%r10 /* variavel auxiliar para aceder a cada indice do array de CheckPoints*/
	mov 24(%rdi,%r10),%r11 /* aceder ao campo de array de checkpoints indice 0*/
	mov %r11,%r13 /* guardar o entry time*/
	
loop:
	add $16,%r10 /* avançar a variavel auxiliar para o proximo indice*/
	mov 24(%rdi,%r10),%r11 /* aceder ao campo de array de checkpoints indice que esta guardado em r10*/
	sub %r10,%r11
	jnc if_cond
	test $1,%r12 /* verificar se chegamos ao fim do array */
	jz END
	jmp loop
	
if_cond:
		 mov %r11,%r10
		 jmp loop
	
END:
	mov 8(%r11),%r11
	sub %r13,%r11
	mov %r11,%rax
	ret



 



