/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class VanillaResourceFinder {
    private final File objects;
    private final File indexFile;
    private final File output;

    private final ForkJoinPool pool = new ForkJoinPool(4);

    private final HashMap<String, String> hashes = new HashMap<>();

    private final List<Runnable> runnables = Collections.synchronizedList(new ArrayList<>());

    public VanillaResourceFinder(File objects, File indexFile, File output) {
        this.objects = objects;
        this.indexFile = indexFile;
        this.output = output;
    }

    public void loadHashes() throws IOException {
        hashes.clear();

        JsonElement element = new JsonParser().parse(new FileReader(indexFile));
        JsonObject object = element.getAsJsonObject();
        JsonObject objects = object.getAsJsonObject("objects");

        loadHashes(objects);
    }

    private void loadHashes(JsonObject objects) {
        for (Map.Entry<String, JsonElement> entry : objects.entrySet()) {
            String key = entry.getKey();
            String hash = loadHash(entry.getValue().getAsJsonObject());

            System.out.println(key + ": " + hash);

            hashes.put(entry.getKey(), loadHash(entry.getValue().getAsJsonObject()));
        }
    }

    private String loadHash(JsonObject object) {
        return object.getAsJsonPrimitive("hash").getAsString();
    }

    public void copyFiles() {
        output.mkdirs();

        int size = hashes.size();
        int counter = 0;

        for (Map.Entry<String, String> entry : hashes.entrySet()) {
            counter++;

            int finalCounter = counter;
            runnables.add(() -> {
                try {
                    copyFile(entry.getValue(), entry.getKey(), finalCounter, size);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            });
        }

        for (int i = 0; i < 8; i++) {
            WorkThread thr = new WorkThread(i);
            thr.start();
        }
    }

    private final ThreadLocal<byte[]> buffers = ThreadLocal.withInitial(() -> new byte[512]);

    private void copyFile(String hash, String path, int counter, int size) throws IOException {
        File hashFile = new File(objects, hash.substring(0, 2) + "/" + hash);
        File outFile = new File(output, path);

        outFile.getParentFile().mkdirs();

        System.out.printf("Copying file %d/%d: '%s' to '%s'...\n", counter, size, hashFile, outFile);

        FileInputStream fis = new FileInputStream(hashFile);
        FileOutputStream fos = new FileOutputStream(outFile);

        byte[] buf = buffers.get();

        int i;
        while ((i = fis.read(buf)) >= 0) {
            fos.write(buf, 0, i);
        }
    }

    public static void main(String[] args) throws IOException {
        VanillaResourceFinder instance = new VanillaResourceFinder(
            new File(".../objects"),
            new File(".../indexes/1.16.json"),
            new File(".../assets")
        );

        instance.loadHashes();
        instance.copyFiles();
    }

    private class WorkThread extends Thread {

        private final int index;

        private WorkThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            System.out.println("Starting worker thread " + index);
            while (!runnables.isEmpty()) {
                Runnable runnable = runnables.remove(0);
                runnable.run();
            }
            System.out.println("Worker thread " + index + " is done");
        }
    }
}
