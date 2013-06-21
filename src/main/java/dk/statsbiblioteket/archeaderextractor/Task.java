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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task {

    /**
     * Threads to use in thread pool.
     */
    public int threads = 1;

    /**
     * ThreadPool executor.
     */
    public ExecutorService executor;

    /**
     * Thread pool processing start time.
     */
    public long startCtm;
    public long stopCtm;

    public ProgressableOutput cout = new ProgressableOutput(System.out);

    public int queued = 0;

    public void command(List<String> filesList, Integer threads, String outputPath) {

        // Thread workers.
        this.threads = threads;

        threadpool_feeder_lifecycle(filesList, this, outputPath);

    }

    public void process(File file, File outputDirectory) {
        HeaderExtractorFile cdxFile = new HeaderExtractorFile();
        cdxFile.processFile(file, outputDirectory);

    }

    public void threadpool_feeder_lifecycle(List<String> filesList, Task task, String outputPath) {
        cout.println("Using " + threads + " thread(s).");

        executor = new ThreadPoolExecutor(threads, threads, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        startCtm = System.currentTimeMillis();
        cout.println("ThreadPool started at " + startCtm);

        try {
            fileProcessor(filesList, task, outputPath);
        } catch (Throwable t) {
            cout.println("Died unexpectedly!");
        } finally {
            cout.println("Queued " + queued + " file(s).");
            //System.out.println("Queued: " + queued + " - Processed: " + processed + ".");
            executor.shutdown();

            while (!executor.isTerminated()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            stopCtm = System.currentTimeMillis();
            cout.println("ThreadPool shut down at " + stopCtm);
            cout.println("Duration: " + (stopCtm - startCtm) / 1000.0 + " seconds");
        }
    }

    /*
     * Invoke the process task on each file
     */
    public void fileProcessor(List<String> files, Task task, String outputPath) {
        File outputDirectory = new File(outputPath);
        if (outputDirectory.isDirectory()) {
        for (String filename : files) {
            try {
                File file = new File(filename);
                if (file.exists() && file.isFile())
                    task.process(file, outputDirectory);

            } catch (Exception e) {
                cout.println("File does not exists -- " + filename);
            }
        }
        } else {
           cout.println(outputPath + " is not a directory. Aborting.");
        }
    }
}
