package com.self.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程执行任务测试
 */
public class ThreadTest {

    //单个线程处理数据的数量
    private static final Integer SIZE = 100;

    //静态线程池
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(10,20,0L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    //线程计数器
    private static CountDownLatch countDownLatch = null;

    /**
     * 批量处理全部数据
     * @param list 数据集
     */
    public void batchDealWithData(List<Integer> list){
        try{
            if(list == null || list.size() < SIZE){
                countDownLatch = new CountDownLatch(1);

                //提交任务
                executor.submit(new DataDealWith(list));
            }else{
                List<List<Integer>> listThread = groupList(list, SIZE);

                countDownLatch = new CountDownLatch(listThread.size());

                for (List<Integer> integerList : listThread) {
                    //提交任务
                    executor.submit(new DataDealWith(integerList));
                }
            }

            //等待所有线程执行完毕
            countDownLatch.await(15, TimeUnit.MINUTES);

            //关闭线程池
            executor.shutdown();

            System.out.println("所有线程执行结束，数据处理完毕！！！");
        }catch (Exception e){
            System.out.println("处理数据异常：" + e);
        }
    }

    /**
     * 数据分组
     * @param list 总数据集
     * @param groupSize 分组大小
     * @return 分组结果
     */
    public static List<List<Integer>> groupList(List<Integer> list, Integer groupSize){
        List<List<Integer>> totalList = new ArrayList<>();

        //分组数量
        int groupNum = list.size() % groupSize == 0 ? list.size() / groupSize : list.size() / groupSize + 1;

        for (int i = 0; i < groupNum; i++) {
            List<Integer> subList = new ArrayList<>();

            //分配数据
            for (int j = i * groupSize; j <= groupSize * (i + 1) - 1; j++) {
                if (j <= list.size() - 1) {
                    subList.add(list.get(j));
                }
            }

            totalList.add(subList);
        }

        return totalList;
    }

    /**
     * 线程处理内部类
     */
    class DataDealWith implements Runnable {
        //线程数据集
        List<Integer> list = new ArrayList<>();

        /**
         * 线程初始化方法
         * @param listIn 线程入参
         */
        public DataDealWith(List<Integer> listIn){
            try{
                if(listIn != null && listIn.size() > 0){
                    for (Integer inte : listIn) {
                        list.add(inte * inte);
                    }
                }
            }catch(Exception e){
                System.out.println("线程初始化数据异常：" + e);
            }
        }

        /**
         * 线程执行逻辑
         */
        @Override
        public void run(){
            try{
                System.out.println("线程" + Thread.currentThread().getName() + "开始执行");

                //线程关键逻辑
                for (Integer integer : list) {
                    System.out.println(integer);
                }

                System.out.println("线程" + Thread.currentThread().getName() + "结束执行");
            }catch (Exception e){
                System.out.println("线程" + Thread.currentThread().getName() + "处理数据异常");
            }finally {
                //线程执行结束，计数器减1
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        ThreadTest threadTest = new ThreadTest();
        threadTest.batchDealWithData(list);
    }

}
