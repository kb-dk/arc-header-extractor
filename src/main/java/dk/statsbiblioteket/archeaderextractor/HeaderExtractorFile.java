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

import java.io.*;

public class HeaderExtractorFile implements ArchiveParserCallback {

    protected String fileName;

    protected int recordNr = 1;

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
                StringWriter headerString = new StringWriter();

                headerString.write("URL: " + arcRecord.getUrlStr() + "\n");
                headerString.write("IP:  " + arcRecord.getIpAddress() + "\n");

                headerString.write("ProtocolVersion: " + httpHeader.getProtocolVersion() + "\n");
                headerString.write("ProtocolStatusCode: " + httpHeader.getProtocolStatusCodeStr() + "\n");
                headerString.write("ProtocolContentType: " + httpHeader.getProtocolContentType() + "\n");
                headerString.write("TotalLength: " + httpHeader.getTotalLength() + "\n");

                for (HeaderLine hl : httpHeader.getHeaderList()) {
                    headerString.write(hl.name + ": " + hl.value + "\n");
                }

                headerString.write("Filename: " + fileName + "\n");
                headerString.write("Offset: " + arcRecord.getStartOffset() + "\n");

                FileWriter headerFile = new FileWriter(
                        outputDirectory.getAbsolutePath() + "/" + fileName
                                + "-"
                                + arcRecord.getStartOffset()
                                + "-"
                                + recordNr);

                headerFile.write(headerString.toString());
                headerFile.close();
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
