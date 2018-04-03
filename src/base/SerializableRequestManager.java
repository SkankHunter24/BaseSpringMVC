package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializableRequestManager {

	public static Object objLock = new Object();
	private static Thread managerThread = null;
	private static SerializableRequestThread serializableRequestThread = null;
	private static Logger log = LogManager.getLogger("SerializableRequestManager");

	public static void beginSerializableRequest(SerializableRequest serializableRequest) {
		serializableRequestThread = new SerializableRequestThread(serializableRequest);
		try {
			synchronized (objLock) {
				if (managerThread == null) {
					managerThread = new Thread(serializableRequestThread); //第一个请求 初始化一个管理线程
				} else {
					if (serializableRequestThread.isRunning()) {
						objLock.wait();   //后一个请求在管理线程结束前拿到了锁 等待
					}
					serializableRequestThread.setSerializableRequest(serializableRequest); 
				}
				//初始化管理线程参数 启动线程
				serializableRequestThread.setRunning(true);
				serializableRequestThread.setBeInterrupted(false);
				managerThread.start();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();// example, print stack trace
		}

	}

	public static void endSerializableRequest(){
		//终止管理线程 唤醒sleep
		if(serializableRequestThread.isRunning()){
			serializableRequestThread.setRunning(false);
			serializableRequestThread.setBeInterrupted(true);
			managerThread.interrupt();
		}
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		boolean b = exception instanceof RuntimeException;
		System.out.println(thread.getId());
		exception.printStackTrace();// example, print stack trace
	}
}
