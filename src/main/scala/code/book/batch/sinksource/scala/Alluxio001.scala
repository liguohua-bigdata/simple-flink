package code.book.batch.sinksource.scala

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

/*

1.the flink-conf.yaml has set the fs.hdfs.hadoopconf property set to the Hadoop configuration directory.


2.For Alluxio support add the following entry into the core-site.xml file:
<property>
  <name>fs.alluxio.impl</name>
  <value>alluxio.hadoop.FileSystem</value>
</property>


  */
object Alluxio001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //    val dataSetFromAlluxio: DataSet[String] = env.readTextFile("hdfs://qingcheng11:9000/input/flink/README.txt")
    val dataSetFromAlluxio: DataSet[String] = env.readTextFile("alluxio://qingcheng11:19998/input/flink/README.txt")
    dataSetFromAlluxio.print()

  }

}
