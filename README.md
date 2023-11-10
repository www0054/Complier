# Complier
![](https://github.com/www0054/Complier/blob/master/img/%E6%B5%81%E7%A8%8B.png)



## 词法分析

#### 词法程序输入文件
data/in

├── coding_map.csv # 码点文件

└── input_code.txt # 输入的代码

#### 词法程序输出文件

data/out 

├── old_symbol_table.txt # 符号表 

└── token.txt # 词法单元列表

#### 源代码

```
int result;
int a;
int b;
int c;
a = 8;
b = 5;
c = 3 - a;
result = a * b - ( 3 + b ) * ( c - a );
return result;
```

#### 词法单元列表

```
(int,)
(id,result)
(Semicolon,)
(int,)
(id,a)
(Semicolon,)
(int,)
(id,b)
(Semicolon,)
(int,)
(id,c)
(Semicolon,)
(id,a)
(=,)
(IntConst,8)
(Semicolon,)
(id,b)
(=,)
(IntConst,5)
(Semicolon,)
(id,c)
(=,)
(IntConst,3)
(-,)
(id,a)
(Semicolon,)
(id,result)
(=,)
(id,a)
(*,)
(id,b)
(-,)
((,)
(IntConst,3)
(+,)
(id,b)
(),)
(*,)
((,)
(id,c)
(-,)
(id,a)
(),)
(Semicolon,)
(return,)
(id,result)
(Semicolon,)
($,)
```
