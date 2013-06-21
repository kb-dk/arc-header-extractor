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


import java.io.OutputStream;
import java.io.PrintStream;

public class ProgressableOutput extends PrintStream {

	public ProgressableOutput(OutputStream out) {
		super(out);
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public void flush() {
		super.flush();
	}

	protected static char[] spacesArr = new char[256];

	static {
		for (int i=0; i<spacesArr.length; ++i) {
			spacesArr[i] = ' ';
		}
	}

	protected int lastProgressWidth = 0;

	protected boolean bNL = true;

	public synchronized void print_progress(String s) {
		if (!bNL) {
			super.println();
		} else {
			super.print("\r");
			if (lastProgressWidth > s.length()) {
				String spaces = new String(spacesArr, 0, lastProgressWidth);
				super.print(spaces);
				super.print("\r");
			}
		}
		super.print(s);
		lastProgressWidth = s.length();
	}

	@Override
	public synchronized void print(String s) {
		if (lastProgressWidth > 0) {
			String spaces = new String(spacesArr, 0, lastProgressWidth);
			super.print("\r");
			super.print(spaces);
			super.print("\r");
			lastProgressWidth = 0;
		}
		super.print(s);
		bNL = false;
	}

	@Override
	public synchronized void println(String x) {
		if (lastProgressWidth > 0) {
			String spaces = new String(spacesArr, 0, lastProgressWidth);
			super.print("\r");
			super.print(spaces);
			super.print("\r");
			lastProgressWidth = 0;
		}
		super.println(x);
		bNL = true;
	}

}
