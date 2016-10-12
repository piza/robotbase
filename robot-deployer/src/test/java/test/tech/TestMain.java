package test.tech;

import java.util.concurrent.*;

/**
 * Created by Peter on 2016/10/12.
 */
public class TestMain {

    public static void main(String[] args) {

//        testCyclicBarrier();
//        testCountDownLatch();

    }

    private static void testCountDownLatch(){
        CountDownLatch countDownLatch=new CountDownLatch(7);
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i=0;i<10;i++){
            executor.submit(new CountDownLatchTask(countDownLatch,i));
        }
        executor.shutdown();

        System.out.println("all thread waiting for start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("start now:"+countDownLatch.getCount());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("man thread over");


    }

    public static class CountDownLatchTask implements Runnable{

        private CountDownLatch countDownLatch;
        private int id;
        public CountDownLatchTask(CountDownLatch countDownLatch,int id){
            this.countDownLatch=countDownLatch;
            this.id=id;
        }
        @Override
        public void run() {

            System.out.println("task "+this.id +" is ready!");
            try {
                Thread.sleep(1000+id*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.countDownLatch.countDown();
            System.out.println("task "+this.id +" end work:"+countDownLatch.getCount());



        }
    }

    private static void testCyclicBarrier(){
        CyclicBarrier cyclicBarrier=new CyclicBarrier(3);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new CyclicBarrierTask(cyclicBarrier,0));
        executor.submit(new CyclicBarrierTask(cyclicBarrier,1));
        executor.shutdown();

        System.out.println("all thread waiting for start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("start now!");
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("man thread over");


    }


    public static class CyclicBarrierTask implements Runnable{

        private CyclicBarrier cyclicBarrier;
        private int id;
        public CyclicBarrierTask(CyclicBarrier cyclicBarrier,int id){
            this.cyclicBarrier=cyclicBarrier;
            this.id=id;
        }
        @Override
        public void run() {

            System.out.println("task "+this.id +" is ready!");
            try {
                this.cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("task "+this.id +" start work!");



        }
    }

    private static void testFutureTask(){
        Task task = new Task();
//        ExecutorService executor = Executors.newCachedThreadPool();
//        Future<Integer> futureTask = executor.submit(task);
//        executor.shutdown();

        FutureTask<Integer> futureTask=new FutureTask<Integer>(task);
        new Thread(futureTask).start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务,等待结果");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        if(!futureTask.isDone()){
            System.out.println("等待超时,取消任务");
            futureTask.cancel(true);
        }else {

            try {
                System.out.println("task运行结果" + futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        System.out.println("所有任务执行完毕");
    }

    public static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程在进行计算");
            Thread.sleep(3000);
            int sum = 0;
            for(int i=0;i<100;i++)
                sum += i;
            return sum;
        }
    }
}
