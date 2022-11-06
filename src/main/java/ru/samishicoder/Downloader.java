package ru.samishicoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Downloader {
    public static void DownloadAll () throws IOException { //1 to 100000
        var baseUrl = "http://1.ru.nbl.oasmobile.com/res/3.0/";

        InputStream stream = Downloader.class.getClassLoader().getResourceAsStream("allRes.json");
        StringReader reader = new StringReader(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        ObjectMapper mapper = new ObjectMapper();
        Images images = mapper.readValue(reader, Images.class);

        for (var i = 0; i < images.images.size(); i++) {
            var name = images.images.get(i).name;
            DownloadByURL(baseUrl + name);
            System.out.println(name);
        }
    }
    public static void DownloadByURL(String urlString) throws IOException {
        var url = new URL(urlString);
        var connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream();
            var fileName = urlString.substring(urlString.lastIndexOf("/"));

            var userDir = Paths.get(System.getProperty("user.dir"));
            File imagesFolder = new File(userDir + "/NarutoImages");
            if (!imagesFolder.exists()){
                imagesFolder.mkdirs();
            }
            Files.copy(inputStream, new File(imagesFolder + fileName).toPath());
        }
    }
}
