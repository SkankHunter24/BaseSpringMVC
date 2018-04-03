package base;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * <p>
 * Description: 使用管理器，管理HTTP连接池 无效链接定期清理功能
 * </p>
 */
public class HttpClientConnectionMonitorThread extends Thread {

	private final HttpClientConnectionManager connManager;
	private volatile boolean shutdown;

	public HttpClientConnectionMonitorThread(HttpClientConnectionManager connManager) {
		super();
		this.setName("http-connection-monitor");
		this.setDaemon(true);
		this.connManager = connManager;
		this.start();
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(3000); // 等待3秒
					// 关闭过期的链接
					connManager.closeExpiredConnections();
					// 选择关闭 空闲XX分的链接
					connManager.closeIdleConnections(HttpClientFactory.CONN_CLEAN_TIME, TimeUnit.MINUTES);
				}
			}
		} catch (InterruptedException ex) {
		}
	}

	/**
	 * 方法描述: 停止 管理器 清理无效链接 (该方法当前暂时关闭)
	 */
	@Deprecated
	public void shutDownMonitor() {
		synchronized (this) {
			shutdown = true;
			notifyAll();
		}
	}

}