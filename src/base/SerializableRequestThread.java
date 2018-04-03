package base;

public class SerializableRequestThread implements Runnable {

	private boolean isRunning = false;
	private boolean beInterrupted = false;
	private SerializableRequest serializableRequest = new SerializableRequest();

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isBeInterrupted() {
		return beInterrupted;
	}

	public void setBeInterrupted(boolean beInterrupted) {
		this.beInterrupted = beInterrupted;
	}

	public SerializableRequestThread(SerializableRequest serializableRequest) {
		this.serializableRequest = serializableRequest;
	}

	public SerializableRequest getSerializableRequest() {
		return serializableRequest;
	}

	public void setSerializableRequest(SerializableRequest serializableRequest) {
		this.serializableRequest = serializableRequest;
	}

	@Override
	public void run() throws RequestTimeOutException { 
		// TODO Auto-generated method stub
		synchronized (SerializableRequestManager.objLock) {
		}
	}

}
