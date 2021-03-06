package com.hongna.community.dao;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {
    public static void main(String[] args){
        BlockingQueue queue = new ArrayBlockingQueue(10);
        new Thread(new Producer(queue)).start();
        //一个生产者生产数据，三个消费者同时并发地消费数据
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}


class Producer implements Runnable{
    private BlockingQueue<Integer> queue;
    public Producer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }
    @Override
    public void run(){
        try{
            for(int i = 0; i < 100; i++){
                Thread.sleep(20);
                //不断地往队列中添加数据
                queue.put(i);
                System.out.print(Thread.currentThread().getName() + "生产:" + queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable{
    private BlockingQueue<Integer> queue;
    public Consumer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }
    //消费者的消费能力没有生产者的那么快,消费者消费的时间间隔是随机的
    @Override
    public void run(){
        try{
            while (true){

                Thread.sleep(new Random().nextInt(1000));
                queue.take();
                System.out.println(Thread.currentThread().getName()
                        + "消费:" + queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}