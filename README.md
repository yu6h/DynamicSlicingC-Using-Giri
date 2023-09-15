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


