package IM.Contract;

public enum ResponseCode {
	/**
     * <pre>
     *一般性错误
     * </pre>
     *
     * <code>Error = 0;</code>
     */
    Error(0),
    /**
     * <pre>
     *正确1xxx
     * </pre>
     *
     * <code>OK = 1;</code>
     */
    OK(1),
    /**
     * <pre>
     *没登录
     * </pre>
     *
     * <code>NotLogin = 2;</code>
     */
    NotLogin(2),
    /**
     * <pre>
     *消息类型未定义
     * </pre>
     *
     * <code>NoMsgType = 3;</code>
     */
    NoMsgType(3),
    /**
     * <pre>
     *解析协议错误
     * </pre>
     *
     * <code>ProtocolError = 4;</code>
     */
    ProtocolError(4),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     *一般性错误
     * </pre>
     *
     * <code>Error = 0;</code>
     */
    public static final int Error_VALUE = 0;
    /**
     * <pre>
     *正确1xxx
     * </pre>
     *
     * <code>OK = 1;</code>
     */
    public static final int OK_VALUE = 1;
    /**
     * <pre>
     *没登录
     * </pre>
     *
     * <code>NotLogin = 2;</code>
     */
    public static final int NotLogin_VALUE = 2;
    /**
     * <pre>
     *消息类型未定义
     * </pre>
     *
     * <code>NoMsgType = 3;</code>
     */
    public static final int NoMsgType_VALUE = 3;
    /**
     * <pre>
     *解析协议错误
     * </pre>
     *
     * <code>ProtocolError = 4;</code>
     */
    public static final int ProtocolError_VALUE = 4;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static ResponseCode valueOf(int value) {
      return forNumber(value);
    }

    public static ResponseCode forNumber(int value) {
      switch (value) {
        case 0: return Error;
        case 1: return OK;
        case 2: return NotLogin;
        case 3: return NoMsgType;
        case 4: return ProtocolError;
        default: return null;
      }
    }


    private final int value;

    private ResponseCode(int value) {
      this.value = value;
    }

}
