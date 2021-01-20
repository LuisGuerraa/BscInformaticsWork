	

globl is_palindrome

.text
	
# RDI = value
# RSI = size
# RDX = count
# RCX = operador shift - size / count
# R8 = value >> count
# R9 = value >> size

is_palindrome:
	xor 	%rdx, %rdx		
	mov 	$63, %esi  		
	mov		$1, %rax		
	jmp 	while_control		

afet:	
	dec		%rsi			# --size
	inc		%rdx			# ++count

while_loop:
	mov		%rdi, %r8		# r8 = value
	mov		%rdi, %r9		# r9 = value
	mov 	%dl, %cl		# cl = count
	shr 	%cl, %r8		# r8 = value >> count
	and		$1, %r8			# r8 = (value >> count) & 1
	mov 	%sil, %cl		# cl = size
	shr		%cl, %r9		# r9 = value >> size
	and		$1, %r9			# r9 = (value >> size) & 1
	
	cmp 	%r8, %r9		# r8 & r9
	jz		afet
	xor		%rax, %rax
	ret
	
while_control:
	cmp 	$32, %rdx		# count - 32
	jnz		while_loop
	ret
	
.end