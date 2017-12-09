package IM.Util;

import java.util.HashMap;

public class CoreException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HashMap<Object, Object> Data = new HashMap<Object, Object>();

	public CoreException() {
		super();
	}

	public CoreException(String message) {
		super(message);
	}

	public CoreException(String message, Throwable inner) {
		super(message, inner);
	}
}
