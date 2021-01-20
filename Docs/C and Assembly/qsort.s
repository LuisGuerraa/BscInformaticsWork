	.file	"qsort.c"
	.text
	.globl	int_cmp
	.type	int_cmp, @function
int_cmp:
.LFB62:
	.cfi_startproc
	movl	(%rdi), %edx
	movl	(%rsi), %eax
	cmpl	%eax, %edx
	jg	.L3
	jge	.L4
	movl	$-1, %eax
	ret
.L3:
	movl	$1, %eax
	ret
.L4:
	movl	$0, %eax
	ret
	.cfi_endproc
.LFE62:
	.size	int_cmp, .-int_cmp
	.globl	memswap
	.type	memswap, @function
memswap:
.LFB60:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	pushq	%r13
	pushq	%r12
	pushq	%rbx
	subq	$24, %rsp
	.cfi_offset 13, -24
	.cfi_offset 12, -32
	.cfi_offset 3, -40
	movq	%rdi, %r13
	movq	%rsi, %r12
	movq	%rdx, %rbx
	movq	%fs:40, %rax
	movq	%rax, -40(%rbp)
	xorl	%eax, %eax
	leaq	15(%rdx), %rax
	andq	$-16, %rax
	subq	%rax, %rsp
	movq	%rdi, %rsi
	movq	%rsp, %rdi
	call	memcpy
	movq	%rbx, %rdx
	movq	%r12, %rsi
	movq	%r13, %rdi
	call	memcpy
	movq	%rbx, %rdx
	movq	%rsp, %rsi
	movq	%r12, %rdi
	call	memcpy
	movq	-40(%rbp), %rax
	xorq	%fs:40, %rax
	je	.L6
	call	__stack_chk_fail
.L6:
	leaq	-24(%rbp), %rsp
	popq	%rbx
	popq	%r12
	popq	%r13
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE60:
	.size	memswap, .-memswap
	.globl	quick_sortC
	.type	quick_sortC, @function
quick_sortC:
.LFB61:
	.cfi_startproc
	pushq	%r15
	.cfi_def_cfa_offset 16
	.cfi_offset 15, -16
	pushq	%r14
	.cfi_def_cfa_offset 24
	.cfi_offset 14, -24
	pushq	%r13
	.cfi_def_cfa_offset 32
	.cfi_offset 13, -32
	pushq	%r12
	.cfi_def_cfa_offset 40
	.cfi_offset 12, -40
	pushq	%rbp
	.cfi_def_cfa_offset 48
	.cfi_offset 6, -48
	pushq	%rbx
	.cfi_def_cfa_offset 56
	.cfi_offset 3, -56
	subq	$8, %rsp
	.cfi_def_cfa_offset 64
	movq	%rdi, %r13
	movq	%rdx, %r12
	movq	%rcx, %r14
	subq	$1, %rsi
	imulq	%rdx, %rsi
	leaq	(%rdi,%rsi), %r15
	leaq	(%rdi,%rdx), %rbp
	movq	%r15, %rbx
	jmp	.L9
.L11:
	addq	%r12, %rbp
.L9:
	cmpq	%rbp, %rbx
	jb	.L12
	movq	%r13, %rsi
	movq	%rbp, %rdi
	call	*%r14
	testl	%eax, %eax
	jg	.L12
	jmp	.L11
.L14:
	subq	%r12, %rbx
.L12:
	cmpq	%rbp, %rbx
	jb	.L13
	movq	%r13, %rsi
	movq	%rbx, %rdi
	call	*%r14
	testl	%eax, %eax
	jns	.L14
.L13:
	cmpq	%rbp, %rbx
	jb	.L15
	movq	%r12, %rdx
	movq	%rbx, %rsi
	movq	%rbp, %rdi
	call	memswap
	jmp	.L9
.L15:
	movq	%r12, %rdx
	movq	%rbx, %rsi
	movq	%r13, %rdi
	call	memswap
	cmpq	%r13, %rbx
	jbe	.L17
	movq	%rbx, %rax
	subq	%r13, %rax
	movl	$0, %edx
	divq	%r12
	movq	%rax, %rsi
	movq	%r14, %rcx
	movq	%r12, %rdx
	movq	%r13, %rdi
	call	quick_sortC
.L17:
	cmpq	%r15, %rbx
	jnb	.L8
	movq	%r15, %rax
	subq	%rbx, %rax
	movl	$0, %edx
	divq	%r12
	movq	%rax, %rsi
	leaq	(%rbx,%r12), %rdi
	movq	%r14, %rcx
	movq	%r12, %rdx
	call	quick_sortC
.L8:
	addq	$8, %rsp
	.cfi_def_cfa_offset 56
	popq	%rbx
	.cfi_def_cfa_offset 48
	popq	%rbp
	.cfi_def_cfa_offset 40
	popq	%r12
	.cfi_def_cfa_offset 32
	popq	%r13
	.cfi_def_cfa_offset 24
	popq	%r14
	.cfi_def_cfa_offset 16
	popq	%r15
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE61:
	.size	quick_sortC, .-quick_sortC
	.globl	fill_with_random_data
	.type	fill_with_random_data, @function
fill_with_random_data:
.LFB63:
	.cfi_startproc
	pushq	%rbx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	call	clock
	movl	%eax, %edi
	call	srand
	movl	$0, %ebx
	jmp	.L21
.L23:
	call	rand
	movslq	%ebx, %rdx
	movl	%eax, data(,%rdx,4)
	call	rand
	movl	%eax, %ecx
	movl	$1374389535, %edx
	imull	%edx
	sarl	$5, %edx
	movl	%ecx, %eax
	sarl	$31, %eax
	subl	%eax, %edx
	imull	$100, %edx, %edx
	subl	%edx, %ecx
	cmpl	$50, %ecx
	jle	.L22
	movslq	%ebx, %rdx
	movl	data(,%rdx,4), %eax
	negl	%eax
	movl	%eax, data(,%rdx,4)
.L22:
	addl	$1, %ebx
.L21:
	cmpl	$99, %ebx
	jle	.L23
	popq	%rbx
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE63:
	.size	fill_with_random_data, .-fill_with_random_data
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC0:
	.string	"%d\n"
	.text
	.globl	main
	.type	main, @function
main:
.LFB64:
	.cfi_startproc
	pushq	%rbx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	movl	$0, %eax
	call	fill_with_random_data
	movl	$int_cmp, %ecx
	movl	$4, %edx
	movl	$100, %esi
	movl	$data, %edi
	call	quick_sortC
	movl	$0, %ebx
	jmp	.L26
.L27:
	movl	data(,%rbx,4), %edx
	movl	$.LC0, %esi
	movl	$1, %edi
	movl	$0, %eax
	call	__printf_chk
	addq	$1, %rbx
.L26:
	cmpq	$99, %rbx
	jbe	.L27
	movl	$10, %edi
	call	putchar
	movl	$0, %eax
	popq	%rbx
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE64:
	.size	main, .-main
	.comm	data,400,32
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.9) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
