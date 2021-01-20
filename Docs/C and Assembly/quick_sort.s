memswap:

	push	%rbp
	mov 	%rsp, %rbp
	push	%r13
	push	%r12
	push	%rbx
	sub		$8, %rsp
	
	mov		%rdi, %r13
	mov		%rsi, %r12
	mov		%rdx, %rbx
	lea		15(%rdx), %rax
	and		$-16, %rax
	sub		%rax, %rsp
	mov		%rdi, %rsi
	mov		%rsp, %rdi
	call	memcpy
	
	mov		%rbx, %rdx
	mov		%r12, %rsi
	mov		%r13, %rdi
	call	memcpy
	
	mov		%rbx, %rdx
	mov		%rsp, %rsi
	mov		%r12, %rdi
	call	memcpy
	
	lea		-24(%rbp), %rsp
	
	pop	%rbx
	pop	%r12
	pop	%r13
	pop	%rbp
	
	ret

quick_sort:
	

END:
	pop	%rbx
	pop	%rbp
	pop	%r12
	pop	%r13
	pop	%r14
	pop	%r15
	ret