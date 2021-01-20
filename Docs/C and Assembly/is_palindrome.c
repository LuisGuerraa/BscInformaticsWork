
int is_palindrome(unsigned long value){
	int counter = 0;
	int lsize = 63;
	
	while(counter<32){
		if(  ( ( value >> count) &1 ) !=  ( (value >> lsize) &1 ) ) return 0;
		size--;
		counter++;
		}
		return 1;
	}

	

	