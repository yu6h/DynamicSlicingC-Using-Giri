# DynamicSlicingC-Using-Giri

使用giri(https://github.com/liuml07/giri)做 dynamic backwards slicing

因giri不支援從標準輸入流(scanf、getchar、gets、fgets)取得輸入的輸入方式，僅支援命令列的輸入

本程式以Clean Architecture的方式，

將C語言程式碼插入自定義的函式且轉換輸入字串後，

轉換成符合命令列的輸入的形式，

再使用gcov工具找出該程式在執行時期的輸入有被覆蓋的輸出函式語句，

針對這些輸出函式語句，

使giri做dynamic backwards slicing，

得到結果後。

再還原成原始程式碼的行數，

得到動態切片的結果
