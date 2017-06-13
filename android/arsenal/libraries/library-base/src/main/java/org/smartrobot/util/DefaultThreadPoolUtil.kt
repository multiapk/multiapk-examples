package org.smartrobot.util

import java.util.concurrent.*

/**
 * @author michael.mao
 * *
 * @version V1.0
 * *
 * @title 数据中心之线程池
 * *
 * @date 2013-12-22 下午10:47:15
 */
class DefaultThreadPoolUtil {
    val TAG = "MThreadPool"
    var executorService: ExecutorService? = null
        private set
    /**
     * @return ArrayBlockingQueue<Runnable>
     * *
     * @title 获得有界阻塞队列
     * *
     * @author michael.mao
     * *
     * @date 2013-12-22 下午10:59:13
     * *
     * @version V1.0
    </Runnable> */
    var blockingQueue: BlockingQueue<Runnable>? = null
        private set

    /**
     * @title 单线程池(无界队列)SINGLE_THREADPOOL
     * *
     * @author michael.mao
     * *
     * @date 2013-12-23 上午12:14:33
     * *
     * @version V1.0
     */
    constructor() {
        blockingQueue = LinkedBlockingQueue<Runnable>() as BlockingQueue<Runnable>?
        executorService = ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, blockingQueue)
    }

    /**
     * @param corePoolSize    核心数量(5)
     * *
     * @param maximumPoolSize 最大数量(10)
     * *
     * @param keepAliveTime   线程(corePoolSize<which></which><=maximumPoolSize)维持活着的时间(60s)
     * *
     * @title 固定大小线程池(有界界队列)FIXED_THREADPOOL
     * *
     * @author michael.mao
     * *
     * @date 2013-12-22 下午10:47:58
     * *
     * @version V1.0
     */
    constructor(corePoolSize: Int, maximumPoolSize: Int, queueSize: Int, keepAliveTime: Long) {
        blockingQueue = ArrayBlockingQueue<Runnable>(queueSize, true)
        executorService = ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, blockingQueue)
    }

    /**
     * @param corePoolSize
     * *
     * @title isSyncQueue==true p v 各位1的生产者与消费者模式 isSyncQueue==false 无界线程池
     * *
     * @author michael.mao
     * *
     * @date 2014-1-5 上午2:51:01
     * *
     * @version V1.0
     */
    constructor(corePoolSize: Int, isSyncQueue: Boolean) {
        if (isSyncQueue) {
            blockingQueue = SynchronousQueue<Runnable>()
            executorService = ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, blockingQueue)
        } else {
            blockingQueue = LinkedBlockingQueue<Runnable>()
            executorService = ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, blockingQueue)
        }

    }

    /**
     * @param runnable void
     * *
     * @title 增加一个任务
     * *
     * @author michael.mao
     * *
     * @date 2013-12-22 下午10:59:34
     * *
     * @version V1.0
     */
    fun addCommand(runnable: Runnable) {
        executorService!!.execute(runnable)
    }
}
