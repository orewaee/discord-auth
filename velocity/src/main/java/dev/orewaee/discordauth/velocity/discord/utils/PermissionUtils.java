package dev.orewaee.discordauth.velocity.discord.utils;

import dev.orewaee.discordauth.common.config.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class PermissionUtils {
    private final Config config;

    private final static String DISCORD_IDS = "discord.ids";

    public PermissionUtils(Config config) {
        this.config = config;
    }

    public boolean hasPermission(Member member) {
        List<String> ids = config.getList(DISCORD_IDS, List.of());

        String userId = member.getUser().getId();
        if (ids.contains(userId)) return true;

        for (Role role : member.getRoles()) {
            String roleId = role.getId();

            if (ids.contains(roleId))
                return true;
        }

        return false;
    }
}
