/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/cbiffle/workspace/Pushr/src/org/mg8/pushr/droid/svc/IUploadService.aidl
 */
package org.mg8.pushr.droid.svc;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface IUploadService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mg8.pushr.droid.svc.IUploadService
{
private static final java.lang.String DESCRIPTOR = "org.mg8.pushr.droid.svc.IUploadService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IUploadService interface,
 * generating a proxy if needed.
 */
public static org.mg8.pushr.droid.svc.IUploadService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mg8.pushr.droid.svc.IUploadService))) {
return ((org.mg8.pushr.droid.svc.IUploadService)iin);
}
return new org.mg8.pushr.droid.svc.IUploadService.Stub.Proxy(obj);
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
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
org.mg8.pushr.droid.svc.IUploadCallback _arg0;
_arg0 = org.mg8.pushr.droid.svc.IUploadCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeCallback:
{
data.enforceInterface(DESCRIPTOR);
org.mg8.pushr.droid.svc.IUploadCallback _arg0;
_arg0 = org.mg8.pushr.droid.svc.IUploadCallback.Stub.asInterface(data.readStrongBinder());
this.removeCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mg8.pushr.droid.svc.IUploadService
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
public void registerCallback(org.mg8.pushr.droid.svc.IUploadCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void removeCallback(org.mg8.pushr.droid.svc.IUploadCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerCallback = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_removeCallback = (IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void registerCallback(org.mg8.pushr.droid.svc.IUploadCallback callback) throws android.os.RemoteException;
public void removeCallback(org.mg8.pushr.droid.svc.IUploadCallback callback) throws android.os.RemoteException;
}
