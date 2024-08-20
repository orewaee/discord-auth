package dev.orewaee.discordauth.velocity.listeners;

import org.jetbrains.annotations.NotNull;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class PreLoginListener {
    private final Config config;
    private final AccountManager accountManager;

    private final JDA jda;

    private final static String NO_ACCOUNT = "minecraft-components.no-account";
    private final static String GUILD_MEMBER_ONLY = "discord.guild-member-only";
    private final static String GUILD_MEMBER_ONLY_MESSAGE = "minecraft-components.guild-member-only";
    private final static String WITH_ROLE_ONLY = "discord.with-role-only";
    private final static String WITH_ROLE_ONLY_MESSAGE = "minecraft-components.with-role-only";

    public PreLoginListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();

        this.jda = DiscordAuth.getInstance().getBot().getJda();
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String name = event.getUsername();

        Account account = accountManager.getByName(name);

        if (account == null) {
            String message = config
                .getString(NO_ACCOUNT, "You don't have an account")
                .replace("%name%", name);

            Component component = MiniMessage.miniMessage().deserialize(message);
            PreLoginComponentResult result = PreLoginComponentResult.denied(component);
            event.setResult(result);
            return;
        }

        String guildId = config.getString(GUILD_MEMBER_ONLY, "");
        if (!guildId.isEmpty()) {
            try {
                boolean isGuildMember = isGuildMember(account, guildId);

                if (!isGuildMember) {
                    String message = config
                        .getString(GUILD_MEMBER_ONLY_MESSAGE, "You are not a member of the guild")
                        .replace("%name%", name)
                        .replace("%discordid%", account.getDiscordId());

                    Component component = MiniMessage.miniMessage().deserialize(message);
                    PreLoginComponentResult result = PreLoginComponentResult.denied(component);
                    event.setResult(result);
                    return;
                }
            } catch (Exception ignored) {}
        }

        String roleId = config.getString(WITH_ROLE_ONLY, "");
        if (!roleId.isEmpty()) {
            try {
                boolean hasRole = hasRole(account, guildId, roleId);

                if (!hasRole) {
                    String message = config
                        .getString(WITH_ROLE_ONLY_MESSAGE, "You don't have a role")
                        .replace("%name%", name)
                        .replace("%discordid%", account.getDiscordId());

                    Component component = MiniMessage.miniMessage().deserialize(message);
                    PreLoginComponentResult result = PreLoginComponentResult.denied(component);
                    event.setResult(result);
                }
            } catch (Exception ignored) {}
        }
    }

    private boolean isGuildMember(@NotNull Account account, String guildId) throws NullPointerException {
        Guild guild = jda.getGuildById(guildId);

        if (guild == null) throw new NullPointerException("Guild not found");

        Member member = guild.getMemberById(account.getDiscordId());

        return member != null;
    }

    private boolean hasRole(@NotNull Account account, String guildId, String roleId) throws NullPointerException {
        Guild guild = jda.getGuildById(guildId);

        if (guild == null) throw new NullPointerException("Guild not found");

        Member member = guild.getMemberById(account.getDiscordId());

        if (member == null) throw new NullPointerException("Member not found");

        for (Role role : member.getRoles())
            if (role.getId().equals(roleId))
                return true;

        return false;
    }
}
