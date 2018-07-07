#include <stdio.h>

int main(void) {
  FILE *filePtr;

  filePtr = fopen("code.asm", "r");

  if (filePtr == NULL){
    printf("Could not find file!");
    fclose(filePtr);
  }
  else{
    char line[128];
    while(fgets(line, sizeof(line), filePtr))
    {
      
      printf("%c\n", line[0]);
    } 


    fclose(filePtr);
  }

  return 0;
}
