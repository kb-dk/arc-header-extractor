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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.jwat.arc.ArcReaderFactory;
import org.jwat.common.ByteCountingPushBackInputStream;
import org.jwat.common.RandomAccessFileInputStream;
import org.jwat.gzip.GzipReader;

public final class FileIdent {

	public static final int FILEID_ERROR = -1;
	public static final int FILEID_UNKNOWN = 0;
	public static final int FILEID_GZIP = 1;
	public static final int FILEID_ARC = 2;
	public static final int FILEID_ARC_GZ = 4;

	public static int identFile(File file) {
		int fileId;
		String fname = file.getName().toLowerCase();
		if (fname.endsWith(".arc.gz")) {
			fileId = FILEID_ARC_GZ;
		} else if (fname.endsWith(".arc")) {
			fileId = FILEID_ARC;
		} else if (fname.endsWith(".gz")) {
			fileId = FILEID_GZIP;
		} else {
			fileId = identFileMagic(file);
		}
		return fileId;
	}

	public static int identFileMagic(File file) {
		int fileId = FILEID_UNKNOWN;
		byte[] magicBytes = new byte[16];
		int magicLength;
		RandomAccessFile raf = null;
		RandomAccessFileInputStream rafin;
		ByteCountingPushBackInputStream pbin = null;
		try {
			raf = new RandomAccessFile( file, "r" );
			rafin = new RandomAccessFileInputStream( raf );
			pbin = new ByteCountingPushBackInputStream(rafin, 16);
			magicLength = pbin.readFully(magicBytes);
			if (magicLength == 16) {
				if (GzipReader.isGzipped(pbin)) {
					fileId = FILEID_GZIP;
					// TODO check for compress arc or warc too
				} else if (ArcReaderFactory.isArcFile(pbin)) {
					fileId = FILEID_ARC;
				}
			}
		} catch (FileNotFoundException e) {
			fileId = FILEID_ERROR;
			System.out.println("Error reading: " + file.getPath());
		} catch (IOException e) {
			fileId = FILEID_ERROR;
			e.printStackTrace();
		}
		finally {
			if (pbin != null) {
				try {
					pbin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileId;
	}

}
