##一、配置yarn
###1.编辑yarn-site.xml
```
1.编辑命令
vim ${HADOOP_HOME}/etc/hadoop/yarn-site.xml

2.配置内容
<property>  
    <name>yarn.nodemanager.vmem-pmem-ratio</name>  
    <value>4</value>  
</property> 
<property>  
    <name>yarn.nodemanager.vmem-check-enabled</name>  
    <value>false</value>  
</property> 

3.配置说明
yarn.nodemanager.vmem-pmem-ratio是yarn内存与虚拟内存的比率默认2.1，适当调大。
yarn.nodemanager.vmem-check-enabled是yarn在启动程序时虚拟内存超标不要杀死程序。
```

###2.分发yarn的配置文件
```
scp  -r ${HADOOP_HOME}/etc/hadoop/yarn-site.xml  qingcheng12:${HADOOP_HOME}/etc/hadoop/
scp  -r ${HADOOP_HOME}/etc/hadoop/yarn-site.xml  qingcheng13:${HADOOP_HOME}/etc/hadoop/
```
###3.重启yarn
```
${HADOOP_HOME}/sbin/stop-yarn.sh
${HADOOP_HOME}/sbin/start-yarn.sh
```

##二、关闭flink-Standalone集群
###1.关闭flink-Standalone集群
```
${FLINK_HOME}/bin/stop-cluster.sh
```
###2.原因说明
```
1.因为yarn可以自动启动受yanr管理flink集群，所以应当将standalone集群关闭掉。
2.standalone集群都关闭掉了，因此再运行flink作业，就不能使用standalone集群的web界面进行监控了。
3.应当使用yarn的监控界面，在yarn中可以进入受yarn管理的flink-cluster-webUI进行监控。
```

##三、使用yarn-session创建flink集群
###1.说明
```
1.首先yarn-session会启动受yarn管理的flink集群。
2.这个flink集群可以运行一个又一个的程序。
3.程序运行结束后集群不结束，只用yarn-session关闭了，集群才会关闭。
```
###2.启动yarn-session
```
$FLINK_HOME/bin/yarn-session.sh -n 3 -s 3
```
###3.yarn-session在terminal下的启动效果
![](images/Snip20161127_69.png) 
###4.yarn-session在yarn-web下的启动效果
![](images/Snip20161127_70.png) 
###5.yarn-session代理出来的flink监控界面
![](images/Snip20161127_71.png) 
###6.运行程序
```
$FLINK_HOME/bin/flink run /bigdata/software/simple-flink.jar
```
###7.yarn-session代理出来的flink监控界面
![](images/Snip20161127_74.png) 
![](images/Snip20161127_75.png) 
###8.terminal下的运行效果
![](images/Snip20161127_72.png) 
![](images/Snip20161127_73.png) 

##四、使用flink-job创建flink集群
###1.说明
```
1.首先flink-client将一个job提交到yarn。
2.yarn为这个application启动一个临时flink集群
3.application运行结束后后,yarn关闭临时flink集群
```
###2.运程序
```
$FLINK_HOME/bin/flink run -m yarn-cluster -yn 3
/bigdata/software/simple-flink.jar
```
###3.yarn-web下的运行效果
![](images/Snip20161127_68.png) 
###4.terminal下的运行效果
![](images/Snip20161127_66.png) 
![](images/Snip20161127_67.png) 





