
##一、流处理的基本概念
###1.全局窗口（Global Windows）
![](images/example-input.png) 
```
1.用户上京东购物，会进行一系列的操作，比如（点击、浏览、搜索、购买、付款等），用户的操作可以被记录为用户操作数据流。
2.京东上的用户会同时有多个，每个用户的操作都是独立的，随机的，因此用户之间的行为没有必然联系，没有统一规律。
```
###4.会话窗口（Session Windows）
![](images/example-input-with-sessions.png) 
```
1.用户的行为有时是一连串的，形成的数据流也是一连串的
2.我们把每一串称为一个session，不同的用户的session划分结果是不一样的。
3.我们把这种window称作SessionWindow
```
####4.2会话窗口定义
```  
// event-time session windows
input
    .keyBy(<key selector>)
    .window(EventTimeSessionWindows.withGap(Time.minutes(10)))
    .<windowed transformation>(<window function>)

// processing-time session windows
input
    .keyBy(<key selector>)
    .window(ProcessingTimeSessionWindows.withGap(Time.minutes(10)))
    .<windowed transformation>(<window function>)
```




##参考链接：
http:http://data-artisans.com/session-windowing-in-flink/