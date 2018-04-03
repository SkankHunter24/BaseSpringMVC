package base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SerializableRequest {
	private long timeOut;
	private HttpServletRequest httpServletRequest;
	private HttpServletResponse httpServletResponse;

	public SerializableRequest(long timeOut, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

	}
}
