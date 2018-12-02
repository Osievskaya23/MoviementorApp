package com.example.a38066.brockenhopes;

import android.os.Environment;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FTPClientServer implements AutoCloseable {

    private FTPClient ftpClient = new FTPClient();
    private String remoteDirPath = "/photo";
    private String statusPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "status.txt";
    private String saveDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"";

    public FTPClientServer() throws Exception {
        ftpClient.connect("capt.ftp.tools");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        boolean login = ftpClient.login("capt_ftp", "yUslNSJB");
        if (!login) {
            throw new AutorizeException();
        }
    }

    public void sendFile(boolean flag) throws Exception {
        try (FileWriter fileWriter = new FileWriter(statusPath)) {
            fileWriter.write(Boolean.toString(flag));
            fileWriter.flush();
        }
        try(FileInputStream inputStream = new FileInputStream(statusPath)) {
            boolean isSend = ftpClient.storeFile("status.txt", inputStream);

            if (!isSend) {
                throw new SendPhotoException();
            }
        }
    }

    public void downloadDir() throws Exception {
        ftpClient.enterLocalPassiveMode();
        FTPUtil.downloadDirectory(ftpClient, remoteDirPath, "", saveDirPath);
    }

    @Override
    public void close() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

}
