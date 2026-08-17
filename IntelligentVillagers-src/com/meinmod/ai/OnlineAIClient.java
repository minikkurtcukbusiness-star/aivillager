/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.ai;

import com.meinmod.ai.VillagerBrain;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OnlineAIClient {
    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2L)).build();

    private static String jsonEscape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String tryAsk(VillagerBrain brain, String cityName, String leaderName) {
        try {
            String payload = "{\"name\":\"" + OnlineAIClient.jsonEscape(brain.name) + "\",\"role\":\"" + OnlineAIClient.jsonEscape(brain.role) + "\",\"task\":\"" + OnlineAIClient.jsonEscape(brain.currentTask) + "\",\"city\":\"" + OnlineAIClient.jsonEscape(cityName) + "\",\"leader\":\"" + OnlineAIClient.jsonEscape(leaderName) + "\"}";
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://127.0.0.1:8080/decision")).timeout(Duration.ofSeconds(3L)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                return null;
            }
            String body = resp.body();
            int i = body.indexOf("\"reply\"");
            if (i < 0) {
                return null;
            }
            int q1 = body.indexOf(34, body.indexOf(58, i) + 1);
            int q2 = body.indexOf(34, q1 + 1);
            if (q1 < 0 || q2 < 0) {
                return null;
            }
            return body.substring(q1 + 1, q2).replace("\\n", "\n");
        }
        catch (Exception e) {
            return null;
        }
    }
}

