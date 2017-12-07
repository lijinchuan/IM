package IM.Connector;

public enum ServerError {
   readLenError(1),
   dataTooLenError(2);
   
   private int _errorCode;
   ServerError(int value){
	   this._errorCode=value;
   }
   
   public int getErrorCode()
   {
	   return this._errorCode;
   }
}
