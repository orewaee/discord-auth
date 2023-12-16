package dev.orewaee.commands;

import java.util.Set;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.config.*;

public class ReloadBrigadierCommand {
    private static final Config config = TomlConfig.getInstance();

    private static final DiscordMessages discordMessages = TomlDiscordMessages.getInstance();
    private static final MinecraftMessages minecraftMessages = TomlMinecraftMessages.getInstance();

    private static final Set<String> types = Set.of("config", "messages", "all");

    public static BrigadierCommand createBrigadierCommand() {
        LiteralCommandNode<CommandSource> pepegaNode = LiteralArgumentBuilder
            .<CommandSource>literal("discordauth")
            .requires(source -> source.hasPermission("discordauth.reload"))
            .executes(context -> {
                context.getSource().sendMessage(
                    MiniMessage.miniMessage().deserialize(
                        minecraftMessages.invalidCommandSyntax()
                    )
                );

                return Command.SINGLE_SUCCESS;
            })
            .then(
                RequiredArgumentBuilder.<CommandSource, String>argument("subcommand", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("reload");

                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String subcommand = context.getArgument("subcommand", String.class);

                        if (!subcommand.equalsIgnoreCase("reload")) {
                            context.getSource().sendMessage(
                                Component.text("Invalid subcommand")
                            );

                            return Command.SINGLE_SUCCESS;
                        }

                        return Command.SINGLE_SUCCESS;
                    })
                    .then(
                        RequiredArgumentBuilder.<CommandSource, String>argument("type", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                types.forEach(builder::suggest);

                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                CommandSource source = context.getSource();
                                MiniMessage miniMessage = MiniMessage.miniMessage();

                                switch (context.getArgument("type", String.class).toLowerCase()) {
                                    case "config" -> {
                                        config.reload();

                                        source.sendMessage(
                                            miniMessage.deserialize(
                                                minecraftMessages.configReloaded()
                                            )
                                        );
                                    }

                                    case "messages" -> {
                                        discordMessages.reload();
                                        minecraftMessages.reload();

                                        source.sendMessage(
                                            miniMessage.deserialize(
                                                minecraftMessages.messagesReloaded()
                                            )
                                        );
                                    }

                                    case "all" -> {
                                        config.reload();

                                        discordMessages.reload();
                                        minecraftMessages.reload();

                                        source.sendMessage(
                                            miniMessage.deserialize(
                                                minecraftMessages.configReloaded()
                                            )
                                        );

                                        source.sendMessage(
                                            miniMessage.deserialize(
                                                minecraftMessages.messagesReloaded()
                                            )
                                        );
                                    }

                                    default -> source.sendMessage(
                                        miniMessage.deserialize(
                                            minecraftMessages.invalidCommandSyntax()
                                        )
                                    );
                                }

                                return Command.SINGLE_SUCCESS;
                            })
                    )
            )

            .build();

        return new BrigadierCommand(pepegaNode);
    }
}
