package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

public class ReduceFunction001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<Integer> text = env.fromElements(1, 2, 3, 4, 5, 6,7);

        //2.对DataSet的元素进行合并，这里是计算累加和
        DataSet<Integer> text2 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                return intermediateResult + next;
            }
        });
        text2.print();

        //3.对DataSet的元素进行合并，这里是计算累乘积
        DataSet<Integer> text3 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                return intermediateResult * next;
            }
        });
        text3.print();

        //4.对DataSet的元素进行合并，逻辑可以写的很复杂
        DataSet<Integer> text4 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                if (intermediateResult % 2 == 0) {
                    return intermediateResult + next;
                } else {
                    return intermediateResult * next;
                }
            }
        });
        text4.print();

        //5.对DataSet的元素进行合并，可以看出intermediateResult是临时合并结果，next是下一个元素
        DataSet<Integer> text5 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                System.out.println("intermediateResult=" + intermediateResult + " ,next=" + next);
                return intermediateResult + next;
            }
        });
        text5.collect();


    }
}
