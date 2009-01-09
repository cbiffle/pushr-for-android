package org.mg8.pushr.droid.svc;

/*
 * Callback interface used by IUploadService to notify on upload
 * progress.
 */
interface IUploadCallback {
	/*
	 * Indicates that the status of the upload service has changed.  This
	 * indicates one of two cases:
	 *
	 * - if 'file' is not null, it contains the name of the current file.
	 * - if 'file' is null, no file is currently being uploaded.  'position'
	 *   and 'end' should be ignored.
	 */
	void statusUpdate(String file, int position, int end);
}
