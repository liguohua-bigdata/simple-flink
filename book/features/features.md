##一、流处理
1.高吞吐，低延时  
有图有真相，有比较有差距。且看下图：
![](images/streaming_performance.png) 
```
1.flink的吞吐量大
2.flink的延时低
3.flink的配置少
```


2.支持Event-Time 和乱序-Event
![](images/out_of_order_stream.png) 
```
1.flink支持流处理
2.flink支持在Event-Time上的窗口处理
3.因为有Event-Time做保障，即使消息乱序或延时也能轻松应对。
```
