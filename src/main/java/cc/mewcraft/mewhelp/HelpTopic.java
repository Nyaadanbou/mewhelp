package cc.mewcraft.mewhelp;

import cc.mewcraft.spatula.utils.ComponentUtils;
import net.kyori.adventure.text.Component;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public record HelpTopic(@NotNull String key, @NotNull List<String> messages) {
    public List<Component> components() {
        return ComponentUtils.asComponent(messages);
    }
}
