package IM.Contract;

public enum MsgType {
	ECHO(0),
	HeartBeat(1),
	Login(2),
	ResponeCode(10000);

	private int _value;

	MsgType(int value) {
		this._value = value;
	}

	public int getVal() {
		return this._value;
	}

	public static MsgType valueOf(int value) {
		switch (value) {
		case 0: {
			return MsgType.ECHO;
		}
		case 1: {
			return MsgType.HeartBeat;
		}
		default: {
			return null;
		}
		}
	}
}
