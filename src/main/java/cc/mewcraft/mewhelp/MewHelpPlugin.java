package cc.mewcraft.mewhelp;

import cc.mewcraft.spatula.message.Translations;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.command.CommandSender;

public final class MewHelpPlugin extends ExtendedJavaPlugin {
    public static MewHelpPlugin INSTANCE;

    private HelpTopics config;
    private Translations translations;

    public static MewHelpPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    protected void enable() {
        INSTANCE = this;

        translations = new Translations(this, "languages");

        config = new HelpTopics(this);

        try {
            CommandRegistry commandRegistry = bind(new CommandRegistry(this));

            // Prepare command: /help <topic>
            commandRegistry.prepareCommand(commandRegistry
                    .commandBuilder("help")
                    .argument(HelpTopicArgument.of("topic"))
                    .handler(ctx -> {
                        HelpTopic topic = ctx.get("topic");
                        CommandSender sender = ctx.getSender();
                        topic.components().forEach(sender::sendMessage);
                    }).build()
            );

            // Prepare command: /help reload
            commandRegistry.prepareCommand(commandRegistry
                    .commandBuilder("help")
                    .literal("reload")
                    .permission("mewhelp.admin")
                    .handler(ctx -> {
                        onDisable();
                        onEnable();
                        translations().of("msg_reloaded_config")
                                .replace("plugin", getPluginMeta().getName())
                                .replace("author", getPluginMeta().getAuthors().get(0))
                                .send(ctx.getSender());
                    }).build()
            );

            // Register all commands
            commandRegistry.registerCommands();
        } catch (Exception e) {
            getSLF4JLogger().error("Failed to initialize command manager", e);
        }
    }

    public HelpTopics helpTopics() {
        return config;
    }

    public Translations translations() {
        return translations;
    }

}
