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
import org.jwat.gzip.GzipEntry;

import java.io.File;
import java.io.IOException;

public interface ArchiveParserCallback {

    public void apcFileId(File file, int fileId);

    public void apcGzipEntryStart(GzipEntry gzipEntry, long startOffset);

    public void apcArcRecordStart(ArcRecordBase arcRecord, long startOffset, boolean compressed, File outputDirectory) throws IOException;

    public void apcUpdateConsumed(long consumed);

    public void apcRuntimeError(Throwable t, long offset, long consumed);

    public void apcDone();

}
