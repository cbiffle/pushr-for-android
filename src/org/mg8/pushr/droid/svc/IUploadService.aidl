package org.mg8.pushr.droid.svc;

import org.mg8.pushr.droid.svc.IUploadCallback;
import android.net.Uri;

/*
 * RPC interface for the Upload Service.
 */
interface IUploadService {
	/*
	 * Indicates that 'callback' wants to receive progress notifications.
	 * Must be followed by a matching call to removeCallback.
	 */
	void registerCallback(IUploadCallback callback);

	/*
	 * Ends notifications to 'callback'.  Should follow a call to
	 * registerCallback.
	 */
	void removeCallback(IUploadCallback callback);
}
