
##一、流处理的基本概念
###1.基本数据流示例
![](images/example-input.png) 
```
1.用户上京东购物，会进行一系列的操作，比如（点击、浏览、搜索、购买、付款等），用户的操作可以被记录为用户操作数据流。
2.京东上的用户会同时有多个，每个用户的行为都是不一样的，他们之间没有说明必然的联系，他们的操作都是独立的，随机的。
```
###2.基本数据流分析之TumblingWindow
![](images/example-input-with-tumbling-windows.png) 
```
如果我们用原来的tumbling-window对stream进行窗口划分，也就是用统一的时间去划分window，
你会发现，用户的连续行为划分的不自然。因为有多个用户，你只用一共时间去划分，这种划分方法会
造成本来一连串的操作被划分到不同的window中去了
```

###3.基本数据流分析之SessionWindow
![](images/example-input-with-sessions.png) 
```
1.用户的行为有时是一连串的，形成的数据流也是一连串的
2.我们把每一串称为一个session，不同的用户的session划分结果是不一样的。
```

http:http://data-artisans.com/session-windowing-in-flink/