####五、flink在standalone模式主节点下有HA的部署实战 
```
当Flink程序运行时，如果jobmanager崩溃，那么整个程序都会失败。为了防止jobmanager的单点故障，
借助于zookeeper的协调机制，可以实现jobmanager的HA配置—-1主（leader）多从（standby）。
这里的HA配置只涉及standalone模式，yarn模式暂不考虑。 
```
>注意：
>1.由于flink jobmanager的HA配置依赖 zookeeper，因此要先配置并启动zookeeper集群
>2.由于flink的HA模式下的state backend在要依赖hdfs，因此要先配置并启动Hadoop集群    
![](images/20161031105508805.png)    
       
