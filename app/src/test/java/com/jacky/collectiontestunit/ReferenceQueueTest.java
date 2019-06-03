package com.jacky.collectiontestunit;

import com.jacky.collectiontestunit.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ReferenceQueueTest {
    private ScheduledExecutorService scheduledExecutorService;
    private static ReferenceQueue<byte[]> rq = new ReferenceQueue<>();
    private static int _1M = 1024 * 1024;

    ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private int count = 0;

    @Before
    public void prepare() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                while (count < 10) {
                }
            }
        });
    }

    @After
    public void finish() {
        scheduledExecutorService.shutdown();
    }

    @Test
    public void test() {
        final Reference<?> poll = referenceQueue.poll();
        Utils.println(poll == null);        // true
    }


    /**
     * WeakReference & ReferenceQueue
     * 当一个对象被gc掉的时候通知用户线程，进行额外的处理时，就需要使用引用队列了。ReferenceQueue
     */
    @Test
    public void notify_gc_test() {
        Object value = new Object();
        Map<Object, Object> map = new HashMap<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int cnt = 0;
                    WeakReference<byte[]> k;
                    while ((k = (WeakReference) rq.remove()) != null) {
                        Utils.println((cnt++) + "回收了:" + k);
                    }
                } catch (InterruptedException e) {
                    System.out.println("thread interrupt" + e.getMessage());
                    //结束循环
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        for (int i = 0; i < 1000; i++) {
            byte[] bytes = new byte[_1M];
            WeakReference<byte[]> weakReference = new WeakReference<>(bytes, rq);
            map.put(weakReference, value);
        }
        Utils.println("map.size->" + map.size());

        Utils.suspend();
    }
}
