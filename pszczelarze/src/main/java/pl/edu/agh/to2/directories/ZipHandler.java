package pl.edu.agh.to2.directories;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipHandler {
    public boolean checkIfZip(File file){
        RandomAccessFile raf = null;
        long magincNumber = 0;

        try {
            raf = new RandomAccessFile(file, "r");
            magincNumber = raf.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return magincNumber == 0x504B0304;
    }

    public Map<String, List<String>> getImagesFromZip(File zipFile) throws IOException{
        Map<String, List<String>> imageMap = new HashMap<>();

        try (ZipFile zipped = new ZipFile(zipFile)) {
            String zipName = getZipName(zipFile);

            Enumeration<? extends ZipEntry> entries = zipped.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                if(validateEntry(entry, zipName)){
                    var entryPath = getZipName(zipFile) + getEntryPath(entry);
                    imageMap.computeIfAbsent(entryPath, k -> new ArrayList<>()).add(readEntryData(zipped, entry));
                }
            }
        }

        return imageMap;
    }

    private boolean validateEntry(ZipEntry entry, String zipName){
        return !entry.isDirectory()
                && (
                (entry.getName().startsWith(zipName)
                        || !entry.getName().contains("/"))
                        && isImage(entry.getName()));
    }

    private String getZipName(File zipFile){
        var zipName = zipFile.getName();
        zipName = zipName.substring(0, zipName.length()-4);

        return zipName;
    }

    private String getEntryPath(ZipEntry entry){
        var entryName = entry.getName();
        entryName = entryName.substring(0, entryName.lastIndexOf("/")+1);

        return entryName;
    }

    private String readEntryData(ZipFile zipFile, ZipEntry entry) throws IOException {
        try(InputStream inputStream = zipFile.getInputStream(entry)){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }
    }

    private boolean isImage(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".png");
    }

}
