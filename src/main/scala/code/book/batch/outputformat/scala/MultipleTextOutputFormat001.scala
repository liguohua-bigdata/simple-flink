package code.book.batch.outputformat.scala

import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat

class MultipleTextOutputFormat001[K, V] extends MultipleTextOutputFormat[K, V] {
  /**
    * 此方法用于产生文件名称,这里将key_DateTime直接作为文件名称
    *
    * @param key   DataSet的key
    * @param value DataSet的value
    * @param name  DataSet的partition的id(从1开始)
    * @return file的name
    */
  override def generateFileNameForKeyValue(key: K, value: V, name: String): String = key.asInstanceOf[String]

  /**
    * 此方法用于产生文件内容中的key，这里文件内容中的key是就是DataSet的key
    *
    * @param key   DataSet的key
    * @param value DataSet的value
    * @return file的key
    */
  override def generateActualKey(key: K, value: V): K = NullWritable.get().asInstanceOf[K]

  /**
    * 此方法用于产生文件内容中的value，这里文件内容中的value是就是DataSet的value
    *
    * @param key   DataSet的key
    * @param value DataSet的value
    * @return file的value
    */
  override def generateActualValue(key: K, value: V): V = value.asInstanceOf[V]
}
