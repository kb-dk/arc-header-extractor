package dk.statsbiblioteket.archeaderextractor;

/*
 * #%L
 * ARC Header Extractor
 * %%
 * Copyright (C) 2013 State and University Library, Denmark
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.jwat.arc.ArcRecordBase;
import org.jwat.common.HeaderLine;
import org.jwat.common.HttpHeader;
import org.jwat.common.Payload;
import org.jwat.common.UriProfile;
import org.jwat.gzip.GzipEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HeaderExtractorFile implements ArchiveParserCallback {

    protected String fileName;

    protected int recordNr = 1;

    protected byte[] tmpBuf = new byte[8192];

    public HeaderExtractorFile() {
    }

    public void processFile(File file, File outputDirectory) {
        fileName = file.getName();
        ArchiveParser archiveParser = new ArchiveParser();
        archiveParser.uriProfile = UriProfile.RFC3986_ABS_16BIT_LAX;
        archiveParser.bBlockDigestEnabled = true;
        archiveParser.bPayloadDigestEnabled = true;
        long consumed = archiveParser.parse(file, outputDirectory, this);
    }

    @Override
    public void apcFileId(File file, int fileId) {
    }

    @Override
    public void apcGzipEntryStart(GzipEntry gzipEntry, long startOffset) {
    }

    @Override
    public void apcArcRecordStart(ArcRecordBase arcRecord, long startOffset, boolean compressed, File outputDirectory) throws IOException {
        Payload payload = arcRecord.getPayload();

        HttpHeader httpHeader = null;
        if (payload != null) {
            httpHeader = arcRecord.getHttpHeader();
            if (httpHeader != null) {
                FileWriter headerFile = new FileWriter(
                        outputDirectory.getAbsolutePath() + "/" + fileName
                                + "-"
                                + arcRecord.getStartOffset()
                                + "-"
                                + recordNr);

                PrintWriter hout = new PrintWriter(headerFile);

                hout.println("URL: " + arcRecord.getUrlStr());
                hout.println("IP:  " + arcRecord.getIpAddress());

                hout.println("ProtocolVersion: " + httpHeader.getProtocolVersion());
                hout.println("ProtocolStatusCode: " + httpHeader.getProtocolStatusCodeStr());
                hout.println("ProtocolContentType: " + httpHeader.getProtocolContentType());
                hout.println("TotalLength: " + httpHeader.getTotalLength());

                for (HeaderLine hl : httpHeader.getHeaderList()) {
                    hout.println(hl.name + ": " + hl.value);
                }

                hout.println("Filename: " + fileName);
                hout.println("Offset: " + arcRecord.getStartOffset());

                hout.close();
            }
        }
        if (httpHeader != null) {
            httpHeader.close();
        }
        if (payload != null) {
            payload.close();
        }
        arcRecord.close();
        ++recordNr;
    }

    @Override
    public void apcUpdateConsumed(long consumed) {
    }

    @Override
    public void apcRuntimeError(Throwable t, long offset, long consumed) {
    }

    @Override
    public void apcDone() {
    }

}
