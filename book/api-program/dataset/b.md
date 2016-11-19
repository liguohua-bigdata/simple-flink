 
##crossWithTiny
```
暗示第二个输入较小的交叉。
拿第一个输入的每一个元素和第二个输入的每一个元素进行交叉操作。
```
执行程序：
```scale
//1.定义 case class
case class WC(val word: String, val count: Int) 

//2.定义DataSet[WC]
val words: DataSet[WC] = benv.fromElements(WC("LISI",6),WC("LISI",4),WC("WANGWU",3),WC("ZHAOLIU",7))

//3.使用自定义的reduce方法
val wordCounts = words.groupBy("word").reduce {
    (w1, w2) => new WC(w1.word, w1.count + w2.count)
}

//4.显示结果
wordCounts.collect
```
执行结果：
```
res75: Seq[WC] = Buffer(WC(LISI,10), WC(WANGWU,3), WC(ZHAOLIU,7))
```
web ui中的执行效果：
![](images/Snip20161119_11.png) 

