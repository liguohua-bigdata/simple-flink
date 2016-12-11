package code.book.batch.outputformat.java;

import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

import java.util.Date;

/**
 * Created by liguohua on 11/12/2016.
 */
public class MultipleTextOutputFormat002<K, V> extends MultipleTextOutputFormat<K, V> {
    /**
     * 此方法用于产生文件名称,这里将key_DateTime直接作为文件名称
     *
     * @param key   DataSet的key
     * @param value DataSet的value
     * @param name  DataSet的partition的id(从1开始)
     * @return file的name
     */
    @Override
    protected String generateFileNameForKeyValue(K key, V value, String name) {
        return key + "_" + new Date().getTime();
    }

    /**
     * 此方法用于产生文件内容中的key，这里文件内容中的key是就是DataSet的key
     *
     * @param key   DataSet的key
     * @param value DataSet的value
     * @return file的key
     */
    @Override
    protected K generateActualKey(K key, V value) {
        return key;
    }

    /**
     * 此方法用于产生文件内容中的value，这里文件内容中的value是就是DataSet的value
     *
     * @param key   DataSet的key
     * @param value DataSet的value
     * @return file的value
     */
    @Override
    protected V generateActualValue(K key, V value) {
        return value;
    }
}