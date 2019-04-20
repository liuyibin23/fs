package org.thingsboard.server.dao.asset;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@Slf4j
public class TestFutures {
    //线程池中线程个数
    private static final int POOL_SIZE = 50;
    //带有回调机制的线程池
    private static final ListeningExecutorService service = MoreExecutors
            .listeningDecorator(Executors.newFixedThreadPool(POOL_SIZE));

    @Test
    public void ListenableFutureTest1(){
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> explosion = service.submit(() -> "hello");
        Futures.addCallback(explosion, new FutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                log.info(s);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Test
    public void testListenableFuture() {
        final List<String> value = Collections
                .synchronizedList(new ArrayList<String>());
        try {
            List<ListenableFuture<String>> futures = new ArrayList<ListenableFuture<String>>();
            // 将实现了callable的任务放入到线程池中，得到一个带有回调机制的ListenableFuture实例，
            // 通过Futures.addCallback方法对得到的ListenableFuture实例进行监听，一旦得到结果就进入到onSuccess方法中，
            // 在onSuccess方法中将查询的结果存入到集合中
            for (int i = 0; i < 1; i++) {
                final int index = i;
                if (i == 9) {
                    Thread.sleep(500 * i);
                }
                ListenableFuture<String> sfuture = service
                        .submit(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                long time = System.currentTimeMillis();
                                log.info("Finishing sleeping task{}: {}", index, time);
                                return String.valueOf(time);
                            }
                        });
                sfuture.addListener(new Runnable() {
                    @Override
                    public void run() {
                        log.info("Listener be triggered for task{}.", index);
                    }
                }, service);

                Futures.addCallback(sfuture, new FutureCallback<String>() {
                    public void onSuccess(String result) {
                        log.info("Add result value into value list {}.", result);
                        value.add(result);
                    }

                    public void onFailure(Throwable t) {
                        log.info("Add result value into value list error.", t);
                        throw new RuntimeException(t);
                    }
                });
                // 将每一次查询得到的ListenableFuture放入到集合中
                futures.add(sfuture);
            }

            // 这里将集合中的若干ListenableFuture形成一个新的ListenableFuture
            // 目的是为了异步阻塞，直到所有的ListenableFuture都得到结果才继续当前线程
            // 这里的时间取的是所有任务中用时最长的一个
            ListenableFuture<List<String>> allAsList = Futures.allAsList(futures);
            allAsList.get();
            log.info("All sub-task are finished.");
        } catch (Exception ignored) {
        }
    }

    //同步处理链式任务
    @Test
    public void testListenableFutureTransform(){
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> task1 = service.submit(() -> "task1");
        //当task1执行完毕会回调执行Function的apply方法，如果有task1有异常抛出，则task2也抛出相同异常，不执行apply
        ListenableFuture<String> task2 = Futures.transform(task1,input -> input + " task2");
        ListenableFuture<String> task3 = Futures.transform(task2, input -> input + " task3");

        Futures.addCallback(task3, new FutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                log.info(s);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    //异步处理链式任务
    @Test
    public void testListenableFutureTransformAsync(){
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<List<String>> task1 = service.submit(()-> Arrays.asList("A","B","C"));
        ListenableFuture<List<String>>  task2 = Futures.transformAsync(task1,rs->{
            List<ListenableFuture<String>> futures = new ArrayList<>();
            rs.forEach(r->{
                ListenableFuture<String> future = service.submit(()-> r + " G");
                futures.add(future);
            });
            return Futures.successfulAsList(futures);
        });

        Futures.addCallback(task2, new FutureCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                strings.forEach(log::info);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

}
