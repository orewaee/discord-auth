package dev.orewaee.version;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.*;

import dev.orewaee.Constants;

public class Updater {
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static int compareVersions(String current, String target) {
        String[] currentArray = current.split("\\.");
        String[] targetArray = target.split("\\.");

        int currentLength = currentArray.length;
        int targetLength = targetArray.length;

        int length = Math.max(currentLength, targetLength);

        for (int i = 0; i < length; i++) {
            int currentTemporary = i < currentLength ? Integer.parseInt(currentArray[i]) : 0;
            int targetTemporary = i < targetLength ? Integer.parseInt(targetArray[i]) : 0;

            if (currentTemporary > targetTemporary) return 1;
            else if (currentTemporary < targetTemporary) return -1;
        }

        return 0;
    }

    public static void checkForUpdates() {
        Request request = new Request.Builder()
            .url("https://api.github.com/repos/orewaee/DiscordAuth/releases/latest")
            .build();

        try {
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ResponseBody body = response.body();

            if (body == null) return;

            LatestRelease latestRelease = gson.fromJson(body.string(), LatestRelease.class);

            if (compareVersions(Constants.VERSION, latestRelease.getTagName()) != -1) return;

            System.out.println("Your plugin version is " + Constants.VERSION + ". Install the latest version " + latestRelease.getTagName() + " by following the link " + latestRelease.getHtmlUrl());

            response.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
