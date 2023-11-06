package dev.orewaee.version;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.*;

import dev.orewaee.Constants;

public class Updater {
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static void checkForUpdates() {
        Request request = new Request.Builder()
            .url("https://api.github.com/repos/orewaee/MentionsBot/releases/latest")
            .build();

        try {
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ResponseBody body = response.body();

            if (body == null) return;

            LatestRelease latestRelease = gson.fromJson(body.string(), LatestRelease.class);

            if (latestRelease.getTagName().equals(Constants.VERSION)) return;

            System.out.println("Your plugin version is " + Constants.VERSION + ". Install the latest version " + latestRelease.getTagName() + " by following the link " + latestRelease.getHtmlUrl());

            response.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
