package code.book.table;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.table.BatchTableEnvironment;
import org.apache.flink.api.table.Table;
import org.apache.flink.api.table.TableEnvironment;
import org.apache.flink.api.table.Types;
import org.apache.flink.api.table.sinks.CsvTableSink;
import org.apache.flink.api.table.sinks.TableSink;
import org.apache.flink.api.table.sources.CsvTableSource;

/**
 * Created by liguohua on 11/12/2016.
 */
public class Test {
    public static void main(String[] args) {
        try {
            ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

            BatchTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(env);
            String path="/Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/book/table/teacher";
            CsvTableSource csvTableSource = new CsvTableSource(path, new String[] { "tname", "taddr" },
                    new TypeInformation<?>[] {  Types.STRING(), Types.STRING() });
            tableEnv.registerTableSource("teacher", csvTableSource);
            Table tab = tableEnv.scan("teacher");
            String patho="/Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/book/table/teacher";

            TableSink<?> sink = new CsvTableSink(patho, "|");
            tab .writeToSink(sink);
//            DataSet<Teacher> ds = tableEnv.toDataSet(tab, Teacher.class);
//
//            tableEnv.registerDataSet("user2", ds, "trans_id,part_dt,lstg_format_name,leaf_categ_id,lstg_site_id,slr_segment_cd,price,item_count,seller_id");
//
//            Table result = tableEnv.sql("SELECT * FROM teacher");
//
//            TableSink<?> sink = new CsvTableSink(args[1], "|");
//            // write the result Table to the TableSink
//            result.writeToSink(sink);
//
//            // execute the program
//            env.setParallelism(1);
            env.execute("Flink Sales SUM");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
