
#####1.print()方法    
执行程序：
```scale
val input: DataSet[String] = benv.fromElements("A", "B", "C", "D", "E", "F", "G", "H")
input.print()
```

程序解析：
```scale
//1.创建一个 DataSet其元素为String类型
val input: DataSet[String] = benv.fromElements("A", "B", "C", "D", "E", "F", "G", "H")

//2.将DataSet的内容打印出来
input.print()
```

shell中的执行效果：
![](images/Snip20161114_86.png) 

web ui中的执行效果：
![](images/Snip20161114_87.png) 

#####2.map()方法    
```
The Map transformation applies a user-defined map function on each element of a DataSet. 
It implements a one-to-one mapping, that is, exactly one element must be returned by the function.
```
执行程序：
```scale
val input: DataSet[Int] = benv.fromElements(23, 67, 18, 29, 32, 56, 4, 27)
val result=input.map(_*2)
result.print()
```
程序解析：
```scale
//1.创建一个DataSet其元素为Int类型
val input: DataSet[Int] = benv.fromElements(23, 67, 18, 29, 32, 56, 4, 27)

//2.将DataSet中的每个元素乘以2
val result=input.map(_*2)

//3.将DataSet中的每个元素输出出来
result.print()
```

shell中的执行效果：
![](images/Snip20161114_91.png) 
web ui中的执行效果：
![](images/Snip20161114_92.png)    
    
#####3.flatMap()方法    
```
The FlatMap transformation applies a user-defined flat-map function on each 
element of a DataSet. This variant of a map function can return arbitrary 
many result elements(including none) for each input element.
```
执行程序：
```scale
val input: DataSet[String] = benv.fromElements("zhangsan boy", "lisi girl")
val result=input.flatMap { _.split(" ") }
result.print()
```
程序解析：
```scale
//1.创建一个 DataSet其元素为String类型
val input: DataSet[String] = benv.fromElements("zhangsan boy", "lisi girl")

//2.将DataSet中的每个元素用空格切割成一组单词
val result=input.flatMap { _.split(" ") }

//3.将这组单词显示出来
result.print()
```
shell中的执行效果：
![](images/Snip20161114_88.png) 
web ui中的执行效果：
![](images/Snip20161114_89.png) 


#####4.mapPartition()方法    
```
MapPartition transforms a parallel partition in a single function call. The map-partition
function gets the partition as Iterable and can produce an arbitrary number of result values.
The number of elements in each partition depends on the degree-of-parallelism and previous 
operations.
```
执行程序：
```scale
val input: DataSet[String] = benv.fromElements("zhangsan boy", "lisi is a girl so sex")
val result=input.mapPartition{in => Some(in.size)}
result.print()
```
程序解析：
```scale
//1.创建一个 DataSet其元素为String类型
val input: DataSet[String] = benv.fromElements("zhangsan boy", "lisi is a girl so sex")

//2.??????????????
val result=input.mapPartition{in => Some(in.size)}

//3.将结果显示出来
result.print()
```
shell中的执行效果：
![](images/Snip20161114_95.png) 
web ui中的执行效果：
![](images/Snip20161114_96.png) 


---
#####5.filter()方法    
```
The Filter transformation applies a user-defined filter function on each element of 
a DataSet and retains only those elements for which the function returns true.
```
执行程序：
```scale
val input: DataSet[String] = benv.fromElements("zhangsan boy", "lisi is a girl so sex","wangwu boy")
val result=input.filter{_.contains("boy")}
result.print()
```
程序解析：
```scale
//1.创建一个 DataSet其元素为String类型
val input: DataSet[String] = benv.fromElements("zhangsan boy", "lisi is a girl so sex","wangwu boy")

//2.过滤出包含'boy'字样的元素
val result=input.filter{_.contains("boy")}

//3.将结果显示出来
result.print()
```
shell中的执行效果：
![](images/Snip20161114_97.png) 
web ui中的执行效果：
![](images/Snip20161114_99.png) 
