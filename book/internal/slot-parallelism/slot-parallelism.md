##slot和parallelism
###1.slot是指taskmanager的并发执行能力
![](images/Snip20161127_77.png) 
```
taskmanager.numberOfTaskSlots:3
每一个taskmanager中的分配3个TaskSlot,3个taskmanager一共有9个TaskSlot
```

###2.parallelism是指taskmanager实际使用的并发能力
![](images/Snip20161127_78.png) 
```
parallelism.default:1 
运行程序默认的并行度为1，9个TaskSlot只用了1个，有8个空闲。增加并行度才能提高效率。
```

###3.parallelism是可配置、可指定的
![](images/Snip20161127_80.png) 
![](images/Snip20161127_81.png) 
```
1.可以通过修改$FLINK_HOME/conf/flink-conf.yaml文件的方式更改并行度。
2.可以通过设置$FLINK_HOME/bin/flink 的-p参数修改并行度
3.可以通过设置executionEnvironmentk的方法修改并行度
4.可以通过设置flink的编程API修改过并行度
5.这些并行度设置优先级从低到高排序，排序为api>env>p>file.
6.设置合适的并行度，能提高运算效率
```
###4.slot和parallelism总结
```
1.slot是静态的概念，是指taskmanager具有的并发执行能力
2.parallelism是动态的概念，是指程序运行时实际使用的并发能力
3.设置合适的parallelism能提高运算效率，太多了和太少了都不行
4.设置parallelism有多中方式，优先级为api>env>p>file
```
