# DynamicSlicingC-Using-Giri

使用giri(https://github.com/liuml07/giri)做 dynamic backwards slicing

程式切片(Program Slicing)是一種將一個程式的不相關部分去除的技術，令剩餘部分 能仍能和原本的某一特定程式行為有一樣的表現。
而動態切片(Dynamic Slicing)是指在給定特定的輸入條件下，將程式執行過程中，確實對程式指定位置變數的值有影響的語句。

如以下程式碼為Giri提供的程式碼範例
```no-highlight
1. #include <math.h>
2. #include <stdio.h>
3. #include <stdlib.h>
4.
5. int main(int argc, char *argv[])
6. {
7.    int x, y, z;
8.
9.    x = atoi(argv[1]);
10.
11.   if (x < 0)
12.   {
13.      y = sqrt(x);
14.      z = pow(2, x);
15.    } else {
16.        if (x == 0)
17.        {
18.            y = sqrt(x * 2);
19.            z = pow(3, x);
20.        } else {
21.            y = sqrt(x * 3);
22.            z = pow(4, x);
23.        }
24.    }
25.
26.    printf("%d\n", y);
27.    printf("%d\n", z);
28.
29.    return z;
30.}
```
在給予x = 10的情況下，針對第27行做動態切片，會得到的行號為9, 22, 27。

因giri不支援從標準輸入流(scanf、getchar、gets、fgets)取得輸入的輸入方式，僅支援命令列的輸入

本程式以Clean Architecture的方式，

將C語言程式碼插入自定義的函式且轉換輸入字串後，

透過regular expression偵測程式碼須轉換的地方。

將整個程式碼轉換成符合命令列的輸入的形式，

再使用gcov工具得到程式覆蓋率後，

針對每一個被覆蓋的輸出函式語句，

令giri做dynamic backwards slicing，

得到全部的動態切片行號後。

再還原成原始程式碼的行數，

得到該程式在這次執行過程中輸出函式語句的動態切片的結果。

以下面這個程式碼舉例：

```no-highlight
1. #include <math.h>
2. #include <stdio.h>
3. #include <stdlib.h>
4.
5. int main()
6. {
7.    int x, y, z;
8.
9.    scanf("%d",&x);
10.
11.   if (x < 0)
12.   {
13.      y = sqrt(x);
14.      z = pow(2, x);
15.    } else {
16.        if (x == 0)
17.        {
18.            y = sqrt(x * 2);
19.            z = pow(3, x);
20.        } else {
21.            y = sqrt(x * 3);
22.            z = pow(4, x);
23.        }
24.    }
25.
26.    printf("%d\n", y);
27.    printf("%d\n", z);
28.
29.    return z;
30.}
```
經過自動轉換後，會轉變為如下的程式碼，並且輸入為1時，被覆蓋的輸出函式語句為第108,109行

會令giri對第108,109行做動態切片，再還原成原始程式碼對應行號，即可得到上面程式碼中第26,27行的動態切片結果。

```no-highlight
1 .#include <stdarg.h>
2 .#include <string.h>
3 .#include <stdio.h>
4 .#include <stdlib.h>
5 .char* INPUT110598067;
6 .int USED110598067;
7 .char* read110598067Fgets(char* str,int size){
8 .    char *format =  (char*)malloc(20 * sizeof(char));
9 .    char* temp;int i;size--;
10.    if(*INPUT110598067 == '\0')return NULL;
11.    size_t npos = strcspn(INPUT110598067, "\n");
12.    sprintf(format,"%%%d[^\\n]%%n",size);
13.    if(npos<size){
14.        temp =  (char*)malloc((npos+1) * sizeof(char));
15.        for(i=0;i<=npos;i++)temp[i] = INPUT110598067[i];
16.        temp[npos+1] ='\0';
17.        sscanf(temp,format,str,&USED110598067);
18.        INPUT110598067 = INPUT110598067 + npos +1;
19.        free(temp);
20.    }else{
21.        sscanf(INPUT110598067,format,str,&USED110598067);
22.        INPUT110598067 = INPUT110598067 + USED110598067;
23.    }
24.    free(format);
25.    return str;
26.}
27.char* read110598067Gets(char* str,const char* format, ...){
28.    char *ptr;
29.    if(*INPUT110598067 == '\n'){
30.        strcpy(str,"");
31.        INPUT110598067++;
32.        return str;
33.    }else if(*INPUT110598067 == '\0'){
34.        return NULL;
35.    }else{
36.    va_list args;
37.    va_start(args, format);
38.    vsscanf(INPUT110598067, format, args);
39.    va_end(args);
40.    INPUT110598067 = INPUT110598067 + USED110598067;
41.    return str;}
42.}
43.char read110598067Getchar(){return *INPUT110598067++;}
44.int read110598067Scanf(const char * format, ... ){
45.    va_list args;
46.    va_start(args, format);
47.    int n = vsscanf(INPUT110598067, format, args);
48.    va_end(args);
49.    INPUT110598067 = INPUT110598067 + USED110598067;
50.    return n;}
51.void convertSpecialCharacter110598067(char *str,char *old,char new){
52.    char *result;
53.    char *ptrResult;char *ptrOriginal;char *nextPtrOriginal;
54.    nextPtrOriginal = ptrOriginal = strstr(str, old);
55.    ptrResult = strstr(str,old);
56.    while(ptrOriginal!= NULL && nextPtrOriginal!=NULL){
57.        nextPtrOriginal  = strstr(ptrOriginal+ strlen(old), old);
58.        ptrOriginal = ptrOriginal + strlen(old);
59.        *ptrResult = new;
60.        while( *ptrResult++!='\0' && ptrOriginal!=nextPtrOriginal  ){
61.            *ptrResult = *ptrOriginal++;
62.        };
63.    }
64.}
65.void convertInputData110598067(char *inputData){
66.    convertSpecialCharacter110598067(inputData,"*BrnL31",'\n');
67.    convertSpecialCharacter110598067(inputData,"*NbsP13",' ');
68.    convertSpecialCharacter110598067(inputData,"*AsD38",'&');
69.    convertSpecialCharacter110598067(inputData,"*LtS60",'<');
70.    convertSpecialCharacter110598067(inputData,"*GtS62",'>');
71.    convertSpecialCharacter110598067(inputData,"*QuT878",'\"');
72.    convertSpecialCharacter110598067(inputData,"*SqT877",'\'');
73.    convertSpecialCharacter110598067(inputData,"*LpT203",'(');
74.    convertSpecialCharacter110598067(inputData,"*RpT301",')');
75.    convertSpecialCharacter110598067(inputData,"*EpT33",'!');
76.    convertSpecialCharacter110598067(inputData,"*PdS410",'#');
77.    convertSpecialCharacter110598067(inputData,"*DaS717",'$');
78.    convertSpecialCharacter110598067(inputData,"*AcA180",'`');
79.    convertSpecialCharacter110598067(inputData,"*HaF969",'|');
80.    convertSpecialCharacter110598067(inputData,"*BaS403",'\\');
81.}
82.#include <math.h>
83.#include <stdio.h>
84.#include <stdlib.h>
85.
86.int main(int argc, char *argv[])
87. {
88.INPUT110598067=argv[1];convertInputData110598067(INPUT110598067);
89.   int x, y, z;
90.
91.   read110598067Scanf("%d%n",&x,&USED110598067);
92.
93.   if (x < 0)
94.   {
95.      y = sqrt(x);
96.      z = pow(2, x);
97.    } else {
98.       if (x == 0)
99.        {
100.           y = sqrt(x * 2);
101.           z = pow(3, x);
102.        } else {
103.            y = sqrt(x * 3);
104.            z = pow(4, x);
105.       }
106.    }
107.
108.    printf("%d\n", y);
109.    printf("%d\n", z);
110.
111.   return z;
112.}
```
