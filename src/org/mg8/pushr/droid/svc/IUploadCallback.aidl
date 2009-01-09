package org.mg8.pushr.droid.svc;

interface IUploadCallback {
	void statusUpdate(String file, int position, int end);
}