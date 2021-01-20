	.globl binary_search
	.text

# RDI = key
# RSI = base
# RDX = nel
# RCX = width
# R8 = compar


binary_search:	
	push	%rbx		# rbx = low
	push	%r12		# r12 = high	
	push	%r13	 
	push	%r14	 
	push	%r15		 
	push	%rbp		
	
	mov		%rdi, %r13	# r13 = key
	mov		%rsi, %r14	# r14 = base
	mov		%rcx, %r15	# r15 = width
	mov		%r8, %rbp	# rbp = compar
	
	
	xor 	%rbx, %rbx	# low = 0
	mov		%rdx, %r12	# high = nel
	dec		%r12		# high = nel -1
while_control:
	cmp		%rbx, %r12	# high - low
	jae 	while_loop
	xor		%rax, %rax
	jmp		return					
	
	                    # midp = rax, mid = rdi
while_loop:
	mov		%r12, %rdi 	# rdi = mid = high
	add		%rbx, %rdi	# mid = high + low
	sar		$1, %rdi	# mid = (high + low) / 2
	mov		%rdi, %rax	# rax = midp = mid
	mul		%r15		# midp = mid * width(%r15) 
	add		%r14, %rax	# midp = base + mid * width
	
	push	%rax		# push midp
	push	%rdi		# push mid
	sub		$8, %rsp

	mov 	%rax, %rdi	# (midp, )
	mov		%r13, %rsi	# (midp, key)
	
	call	*%rbp		# compar(%rdi, %rsi)
	
	add		$8, %rsp
	pop 	%rdi		# %rdi = mid
	
	test	%eax, %eax	
	
	pop 	%rax		# pop midp
	
	jg		low_add
	jl		high_add
return:
	pop 	%rbp
	pop 	%r15
	pop 	%r14
	pop 	%r13
	pop 	%r12
	pop 	%rbx
	ret

low_add:
	mov		%rdi, %rbx	# low = mid
	inc		%rbx		# low = mid + 1
	jmp		while_control	
	
high_add:
	mov		%rdi, %r12	# high = mid
	dec		%r12		# high = mid - 1
	jmp 	while_control
	
.end

