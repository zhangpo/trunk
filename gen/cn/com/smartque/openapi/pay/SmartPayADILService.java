/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/chensen/Downloads/PayDemo/src/com/smartque/openapi/pay/SmartPayADILService.aidl
 */
package com.smartque.openapi.pay;
public interface SmartPayADILService extends android.os.IInterface
{
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements com.smartque.openapi.pay.SmartPayADILService
    {
        private static final java.lang.String DESCRIPTOR = "com.smartque.openapi.pay.SmartPayADILService";
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * * Cast an IBinder object into an com.smartque.openapi.pay.SmartPayADILService interface,
         * * generating a proxy if needed.
         * */
        public static com.smartque.openapi.pay.SmartPayADILService asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof com.smartque.openapi.pay.SmartPayADILService))) {
                return ((com.smartque.openapi.pay.SmartPayADILService)iin);
            }
            return new com.smartque.openapi.pay.SmartPayADILService.Stub.Proxy(obj);
        }
        @Override public android.os.IBinder asBinder()
{
return this;
}
        @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
        {
            switch (code)
           {
                case INTERFACE_TRANSACTION:
                {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_pay:
                {
                    data.enforceInterface(DESCRIPTOR);
                    double _arg0;
                    _arg0 = data.readDouble();
                    java.lang.String _result = this.pay(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
        private static class Proxy implements com.smartque.openapi.pay.SmartPayADILService
        {
            private android.os.IBinder mRemote;
            Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
            @Override public android.os.IBinder asBinder()
{
return mRemote;
}
            public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
            @Override public java.lang.String pay(double payMoney) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeDouble(payMoney);
                    mRemote.transact(Stub.TRANSACTION_pay, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_pay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }
    public java.lang.String pay(double payMoney) throws android.os.RemoteException;
}
