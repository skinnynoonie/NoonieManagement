package me.skinnynoonie.nooniemanagement.config.organizers;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.ConfigurableMessageOrganizerImpl;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class LocalConfigurableMessageOrganizerImpl implements ConfigurableMessageOrganizerImpl {

    private final Plugin plugin;
    private final File messagesDir;

    public LocalConfigurableMessageOrganizerImpl(Plugin plugin) {
        this.plugin = plugin;
        messagesDir = new File(plugin.getDataFolder(), "messages");
    }

    @Override
    public void initiate() throws Exception {
        messagesDir.mkdirs();
    }

    @Override
    public void register(Class<? extends ConfigurableMessage> configurableMessage) throws Exception {
        checkIfCorrectlyAnnotated(configurableMessage);
        File messageFile = new File(messagesDir, configurableMessage.getSimpleName()+".txt");
        printDefaultValueIfNonExistent(messageFile, configurableMessage);
        String content = Files.readString(messageFile.toPath());
        ConfigurableMessage.MESSAGE_VALUES.put(configurableMessage, content);
    }

    // In this case, this is purely for decorations.
    @Override
    public void reload(Class<? extends ConfigurableMessage> configurableMessage) throws Exception {
        register(configurableMessage);
    }

    private void checkIfCorrectlyAnnotated(Class<?> clazz) {
        if(!clazz.isAnnotationPresent(DefaultMessageConfigValue.class)) {
            throw new IllegalStateException("Configurable message MUST be annotated with '@DefaultMessageConfigValue'. Class with missing annotation: '"+clazz.getName()+"'.");
        }
    }

    private void printDefaultValueIfNonExistent(File messageFile, Class<?> configurableMessage) throws FileNotFoundException {
        if(!messageFile.exists()) {
            String defaultValue = getDefaultConfigMessageValue(configurableMessage);
            PrintWriter printWriter = new PrintWriter(messageFile);
            printWriter.print(defaultValue);
            printWriter.close();
        }
    }

    private String getDefaultConfigMessageValue(Class<?> configurableMessage) {
        DefaultMessageConfigValue defaultValueAnnotation = configurableMessage.getAnnotation(DefaultMessageConfigValue.class);
        return defaultValueAnnotation.defaultValue();
    }

}
