package IM.Connector;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
	private static ThreadPoolExecutor _threadPool;
	private static BlockingQueue<Runnable> _poolqueues;
	private static final int minPoolSize = 100;
	private static final int maxPoolSize = 2000;
	private static final int keepAliveTime = 30;

	static {
		_poolqueues = new LinkedBlockingQueue<Runnable>();
		_threadPool = new ThreadPoolExecutor(minPoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, _poolqueues);
	}

	public static void SetMaxPoolSize(int value) {
		if (value > 0) {
			_threadPool.setMaximumPoolSize(value);
		}
	}

	public static void SetCorePoolSize(int value) {
		if (value > 0) {
			_threadPool.setCorePoolSize(value);
		}
	}

	public static Boolean QueueUserWorkItem(Runnable run) throws InterruptedException {
		if (run == null) {
			return false;
		}
		// _poolqueues.put(run);
		_threadPool.execute(run);
		return true;
	}

	public static long GetTaskCount() {
		return _threadPool.getTaskCount();
	}

	public static long GetCompletedTaskCount() {
		return _threadPool.getCompletedTaskCount();
	}

	public static long GetLargestPoolSize() {
		return _threadPool.getLargestPoolSize();
	}

	public static long GetActiveCount() {
		return _threadPool.getActiveCount();
	}
}
