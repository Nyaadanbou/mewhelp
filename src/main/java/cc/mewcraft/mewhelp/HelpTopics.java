package cc.mewcraft.mewhelp;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HelpTopics {

    private final MewHelpPlugin plugin;
    private final Map<String, HelpTopic> topicMap;

    public HelpTopics(MewHelpPlugin plugin) {
        this.plugin = plugin;
        this.topicMap = new HashMap<>();
        this.loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        Optional.ofNullable(plugin.getConfig().getConfigurationSection("help")).ifPresent(section -> {
            for (String key : section.getKeys(false)) {
                List<String> messages = section.getStringList(key);
                HelpTopic topic = new HelpTopic(key, messages);
                topicMap.put(key, topic);
            }
            plugin.getComponentLogger().info(
                    plugin.translations().of("msg_loaded_topics").replace("amount", topicMap.size()).component()
            );
        });
    }

    public @NonNull Collection<String> topics() {
        return topicMap.keySet();
    }

    public @Nullable HelpTopic topic(String key) {
        return topicMap.get(key);
    }

}
