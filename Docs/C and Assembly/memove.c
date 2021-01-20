void * memmove(void* dst, void* src, size_t len){
  unsigned char* destc;
  unsigned char* sourcec;
	
  unsigned long* destl;
  unsigned long* sourcel;
  
  unsigned short* dests;
  unsigned short* sources;
  
  while(len > 0) {
	  
	if(len >= 8){
			destl = (unsigned long *) dst;
			sourcel = (unsigned long*) src;
			*destl = *sourcel;
			++destl;
			++sourcel;
			src=sourcel;
			dst=destl;
			len= len - 8;
		   
	 }
	else if(len >= 2){
			dests = (unsigned short *) dst;
			sources = (unsigned short*) src;
			*dests = *sources;
			++destl;
			++sourcel;
			src=sources;
			dst=dests;
			len= len - 2;
		
		
		}
	 
	else if(len >= 1){
			destc = (unsigned char *) dst;
			sourcec = (unsigned char*) src;
			*destc = *sourcec;
			++destc;
			++sourcec;
			src=sourcec;
			dst=destc;
			len= len - 1;
		
		}
		
	
	}  	
	return dst
	} 
