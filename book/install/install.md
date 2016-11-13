####一、flink软件包的下载与解压   
1.从flink官网获得下载链接
1.1进入官网链接：  
http://flink.apache.org
![](images/Snip20161113_50.png) 
1.2进入下载页面：   
http://flink.apache.org/downloads.html
![](images/Snip20161113_50.png) 
1.3获取下载地址：
![](images/Snip20161113_54.png) 
选择相应的版本，下载地址为http://mirrors.tuna.tsinghua.edu.cn/apache/flink/flink-1.1.3/flink-1.1.3-bin-hadoop27-scala_2.10.tgz
2.下载并解压flink
2.1下载命令：
```
wget http://mirrors.tuna.tsinghua.edu.cn/apache/flink/flink-1.1.3/flink-1.1.3-bin-hadoop27-scala_2.10.tgz
```
得到flink-1.1.3-bin-hadoop27-scala_2.10.tgz软件压缩包

2.2解压命令：
```
tar -zxvf flink-1.1.3-bin-hadoop27-scala_2.10.tgz
```
解压得到flink-1.1.3文件夹，文件夹中的内容如下：
![](images/Snip20161113_55.png) 
3.分发flink及其环境变量  
3.1分发flink  
分发命令：  
```
scp -r /bigdata/software/flink-1.1.3  qingcheng12:/bigdata/software/
scp -r /bigdata/software/flink-1.1.3  qingcheng13:/bigdata/software/
```

3.2分发flink的环境变量  
配置FLINK_HOME环境变量  
```
1.编辑环境变量文件
执行命令：
    vim ~/.bashrc
编辑内容：
    在环境办理文件中加入如下内容
    export FLINK_HOME=/bigdata/software/flink-1.1.3
    export PATH=$FLINK_HOME/bin:$PATH
2.分发环境变量文件到其他机器
执行命令：
    scp ~/.bashrc  qingcheng12:~/.bashrc
    scp ~/.bashrc  qingcheng13:~/.bashrc
3.在每个机器上刷新环境变量
执行命令：
    source   ~/.bashrc
4.测试环境环境变量是否配置成功 
执行命令：
    $FLINK_HOME
执行效果：
    出现如下字样说明配置成功
   -bash: /bigdata/software/flink-1.1.3: Is a directory
```


####二、flink在standalone模式主节点下无HA的部署实战
1.部署规划：  
![](images/Snip20161113_56.png) 
