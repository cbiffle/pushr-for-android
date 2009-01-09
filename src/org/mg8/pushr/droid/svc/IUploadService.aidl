package org.mg8.pushr.droid.svc;

import org.mg8.pushr.droid.svc.IUploadCallback;
import android.net.Uri;

interface IUploadService {
	void registerCallback(IUploadCallback callback);
	void removeCallback(IUploadCallback callback);
}