package ac.grim.grimac.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

@CommandAlias("grim|grimac")
public class GrimVersion extends BaseCommand {

    @Subcommand("version")
    @CommandPermission("grim.version")
    public void onCommand(CommandSender sender) {
        checkForUpdatesAsync(sender);
    }

    private static long lastCheck;
    private static final AtomicReference<Component> updateMessage = new AtomicReference<>();

    public static void checkForUpdatesAsync(CommandSender sender) {
        String current = GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
        MessageUtil.sendMessage(sender, Component.text()
                .append(Component.text("Grim Version: ").color(NamedTextColor.GRAY))
                .append(Component.text(current).color(NamedTextColor.AQUA))
                .build());
        // use cached message if last check was less than 1 minute ago
        final long now = System.currentTimeMillis();
        if (now - lastCheck < 60000) {
            Component message = updateMessage.get();
            if (message != null) MessageUtil.sendMessage(sender, message);
            return;
        }
        lastCheck = now;
        FoliaScheduler.getAsyncScheduler().runNow(GrimAPI.INSTANCE.getPlugin(), (dummy) -> checkForUpdates(sender));
    }

    // Using UserAgent format recommended by https://docs.modrinth.com/api/
    private static void checkForUpdates(CommandSender sender) {
        String current = GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
        HttpURLConnection connection = null;
        try {
            // Create the URL object
            URL url = new URL("https://api.modrinth.com/v2/project/LJNGWSvH/version");

            // Open the connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "GrimAnticheat/Grim/" + GrimAPI.INSTANCE.getExternalAPI().getGrimVersion());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000); // Set timeout to 5 seconds
            connection.setReadTimeout(5000);    // Set read timeout to 5 seconds

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Component msg = updateMessage.get();
                if (msg == null) {
                    msg = Component.text()
                            .append(Component.text("Failed to check latest version.").color(NamedTextColor.RED))
                            .build();
                }
                MessageUtil.sendMessage(sender, msg);
                LogUtil.error("Failed to check latest GrimAC version. Response code: " + responseCode);
                return;
            }

            // Read the response body
            try (InputStream inputStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse the response JSON
                JsonObject object = JsonParser.parseString(response.toString()).getAsJsonArray().get(0).getAsJsonObject();
                String latest = object.get("version_number").getAsString();

                // Compare versions
                Status status = compareVersions(current, latest);
                Component msg = null;
                switch (status) {
                    case AHEAD:
                        msg = Component.text("You are using a development version of GrimAC").color(NamedTextColor.LIGHT_PURPLE);
                        break;
                    case UPDATED:
                        msg = Component.text("You are using the latest version of GrimAC").color(NamedTextColor.GREEN);
                        break;
                    case OUTDATED:
                        msg = Component.text()
                                .append(Component.text("New GrimAC version found!").color(NamedTextColor.AQUA))
                                .append(Component.text(" Version ").color(NamedTextColor.GRAY))
                                .append(Component.text(latest).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                                .append(Component.text(" is available to be downloaded here: ").color(NamedTextColor.GRAY))
                                .append(Component.text("https://modrinth.com/plugin/grimac").color(NamedTextColor.GRAY).decorate(TextDecoration.UNDERLINED)
                                        .clickEvent(ClickEvent.openUrl("https://modrinth.com/plugin/grimac")))
                                .build();
                        break;
                }
                updateMessage.set(msg);
                MessageUtil.sendMessage(sender, msg);
            }
        } catch (Exception e) {
            MessageUtil.sendMessage(sender, Component.text("Failed to check latest version.").color(NamedTextColor.RED));
            LogUtil.error("Failed to check latest GrimAC version.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private enum Status {
        AHEAD,
        UPDATED,
        OUTDATED
    }

    private static Status compareVersions(String local, String latest) {
        if (local.equals(latest)) return Status.UPDATED;
        String[] localParts = local.split("\\.");
        String[] latestParts = latest.split("\\.");
        int length = Math.max(localParts.length, latestParts.length);
        for (int i = 0; i < length; i++) {
            int localPart = i < localParts.length ? Integer.parseInt(localParts[i]) : 0;
            int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
            if (localPart < latestPart) {
                return Status.OUTDATED;
            } else if (localPart > latestPart) {
                return Status.AHEAD;
            }
        }
        return Status.UPDATED;
    }

}
