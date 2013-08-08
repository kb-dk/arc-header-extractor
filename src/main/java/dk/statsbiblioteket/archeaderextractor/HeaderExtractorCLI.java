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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class HeaderExtractorCLI {

    /**
     * main class called from the CLI with two parameters:
     *
     * @param args contains two elements:
     *             0: a file name or a directory name. In the latter case all files ending with .arc will
     *             be included in the job.
     *             1: a full path to a directory where the output files will be placed.
     */
    public static void main(String[] args) {

        List<String> files = new ArrayList<String>();

        FilenameFilter arcFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".arc");
            }
        };

        try {
            File input = new File(args[0]);
            if (input.isFile()) {
                files.add(input.getAbsolutePath());
            } else if (input.isDirectory() && input.listFiles() != null) {
                for (File f : input.listFiles(arcFilter))
                    files.add(f.getAbsolutePath());
            } else {
                System.out.println("No files to handle.");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Unable to read input.");
            System.out.println(e);
            System.exit(1);
        }

        File outputDirectory = new File(args[1]);
        if (!outputDirectory.isDirectory()) {
            System.out.println("Output directory is not a directory!");
            System.exit(1);
        }

        Task headerExtractionTask = new Task();

        // call task with a list of files and the desired number of threads
        headerExtractionTask.command(files, 2, outputDirectory.getAbsolutePath());

        System.exit(0);
    }
}
