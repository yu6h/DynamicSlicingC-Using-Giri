#include<stdio.h>
#include<stdlib.h>
int main(int argc, char *argv[]){
    int n,a,i,s;
    n = atoi(argv[1]);
    a = atoi(argv[2]);
    i = 1;
    s = 1;
    if (a > 0)
        s = 0;
    while (i<=n){
        if(a>0)
            s+=2;
        else
            s*=2;
        i++;
    }
    printf("%d",s);
    return 0;
}