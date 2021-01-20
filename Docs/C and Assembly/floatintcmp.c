#include <stdio.h>
#include <limits.h>		

int floatintcmp (float val, int iv){
	int f = * (int*) & val;
	unsigned signal = f >> 31;
	unsigned exponent = (f >> 23) & 0xff;
	unsigned mantissa = f & 0x7fffff;
	int mask = 0;
	int shifter = exponent - 127; 
	//if (shifter == 31) return -1; 
	if ((shifter >= 32)&&(signal == 1)) return INT_MIN;
	if ((shifter >= 32)&&(signal == 0)) return INT_MAX;
	int result = (mantissa >> 1) | (1 << 22); 
	result = result >> (23 - 1 - shifter);
    if (result < iv) return -1;
	if (result > iv) return 1;
	if (result == iv) { //parte decimal
		mask = ((0x7fffff) >> shifter);
		if ((mantissa & mask) != 0) return 1;
		printf("%d",mask);
	};
	return 0;
}
int main(){
	printf("Valor: %d",floatintcmp(3.5, 3));
}