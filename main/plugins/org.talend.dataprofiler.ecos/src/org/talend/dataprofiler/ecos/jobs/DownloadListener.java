// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.ecos.jobs;

/**
 * Listener for download events.
 */
public interface DownloadListener {

    /**
     * Notify before download start.
     * 
     * @param totalSize The size of file.
     */
    void downloadStart(int totalSize);

    /**
     * Notify when download progress. DOC hcw Comment method "downloadProgress".
     * 
     * @param downloader The source that trigger this event.
     * @param bytesDownloaded The number of bytes that have readed just now.
     */
    void downloadProgress(ComponentDownloader downloader, int bytesDownloaded);

    /**
     * Notify after download complete.
     */
    void downloadComplete();

}