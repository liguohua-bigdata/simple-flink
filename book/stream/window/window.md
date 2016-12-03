##一、

###1.time
![](images/Snip20161203_12.png) 
```
1.事件时间（Event Time）:事件在它的生产设备上发生的时间 
2.提取时间是（Ingestion time）:事件进入Flink的时间
3.处理时间（Processing Time）:执行对应Operation设备的系统时间
```



##一、

###1.window
![](images/window-stream.png) 
```
1.红绿灯路口会有汽车通过，一共会有多少汽车通过，无法计算。因为车流源源不断，计算没有边界。
2.统计每15秒钟通过红路灯的汽车数量，这个是可以计算的，因为有计算的边界。
3.第一个15秒为2辆，第二个15秒为3辆，第三个15秒为1辆。。。
```
###2.rolling window (滚动窗口-累积)
![](images/window-rolling-sum.png) 
```
每15秒统计一次，一共有多少汽车通过红路灯。新数据和原来数据一起统计。
```
###3.tumbling windows (翻转窗口-不重合)
![](images/window-tumbling-window.png) 
```
1.每分钟统计一次，一共有多少汽车通过红路灯。
2.第一分钟的为8辆，第二分钟为22辆，第三分钟为27辆。。。这样，1个小时内会有60个tumbling window。
```
###4.sliding windows （滑动窗口-重合)
![](images/window-sliding-window.png) 
```
1.每30秒统计一次,1分钟通过汽车数量
2.第一个1分钟通过8辆，第二个1分钟通过15辆，第三个1分钟通过22辆。。。
3.window出现了重合。这样，1个小时内会有120个window。
```



Window就是用来对一个无限的流设置一个有限的集合，在有界的数据集上进行操作的一种机制。window又可以分为基于时间（Time-based）的window以及基于数量（Count-based）的window。




http://www.cnblogs.com/lanyun0520/p/5745259.html
http://blog.csdn.net/lmalds/article/details/51604501
http://wuchong.me/blog/2016/05/25/flink-internals-window-mechanism/
http://flink.apache.org/news/2015/12/04/Introducing-windows.html
https://ci.apache.org/projects/flink/flink-docs-release-1.1/concepts/concepts.html#time
https://ci.apache.org/projects/flink/flink-docs-release-1.1/apis/streaming/event_time.html
https://ci.apache.org/projects/flink/flink-docs-release-1.2/dev/windows.html