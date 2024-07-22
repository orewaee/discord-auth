package dev.orewaee.discordauth.velocity.discord.utils;

import dev.orewaee.discordauth.common.config.Config;
import dev.orewaee.discordauth.velocity.DiscordAuth;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Utils {
    protected void hasPermission(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
    }
}
