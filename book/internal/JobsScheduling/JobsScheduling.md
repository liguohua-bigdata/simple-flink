
##一、程
###1.调度（Scheduling）
```
1.Flink集群一般有一个或多个TaskManager，每个TaskManager有一个或多个slot来区分不同的资源（当前是内存）
2.每个slot都可以运行整个pipeline，这些pipeline中的并行任务都可以并行的运行在各个slot之中
3.可通过SlotSharingGroup和CoLocationGroup来定义任务在共享任务槽的行为，可定义为自由共享，
  或是严格定义某些任务部署到同一个任务槽中。
```
![](images/Snip20161129_1.png) 
```
1.本例中有2个TaskManager，每个TaskManager划分了3个slot，一共6个slot。
2.本例是一个source-map-reduce的pipeline例子，source并行度为4，map并行度为4，reduce并行度为3.最大并行度为4.
3.图中可见TaskManager1使用2个slot,分别运行蓝，黄2个subtask的pipeline。
4.图中可见TaskManager2使用2个slot,分别运行红，橙2个subtask的pipeline。
```
###2.JobManager数据结构（JobManager Data Structures）
![](images/Snip20161129_2.png) 
![](images/Snip20161129_3.png) 
![](images/Snip20161129_5.png) 