/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/cbiffle/workspace/Pushr/src/org/mg8/pushr/droid/svc/IUploadCallback.aidl
 */
package org.mg8.pushr.droid.svc;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface IUploadCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mg8.pushr.droid.svc.IUploadCallback
{
private static final java.lang.String DESCRIPTOR = "org.mg8.pushr.droid.svc.IUploadCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IUploadCallback interface,
 * generating a proxy if needed.
 */
public static org.mg8.pushr.droid.svc.IUploadCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mg8.pushr.droid.svc.IUploadCallback))) {
return ((org.mg8.pushr.droid.svc.IUploadCallback)iin);
}
return new org.mg8.pushr.droid.svc.IUploadCallback.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_statusUpdate:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.statusUpdate(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mg8.pushr.droid.svc.IUploadCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void statusUpdate(java.lang.String file, int position, int end) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(file);
_data.writeInt(position);
_data.writeInt(end);
mRemote.transact(Stub.TRANSACTION_statusUpdate, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_statusUpdate = (IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void statusUpdate(java.lang.String file, int position, int end) throws android.os.RemoteException;
}
