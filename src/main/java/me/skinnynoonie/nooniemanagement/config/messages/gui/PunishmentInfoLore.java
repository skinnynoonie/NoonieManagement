package me.skinnynoonie.nooniemanagement.config.messages.gui;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.config.messages.chat.ConsoleName;
import me.skinnynoonie.nooniemanagement.config.messages.chat.DefaultPunishmentReason;
import me.skinnynoonie.nooniemanagement.config.messages.chat.PermanentDuration;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

@DefaultMessageConfigValue(defaultValue =
        """
        <reset><aqua>{occurred}
        
        <reset><white>Target: <aqua>{target}
        <reset><white>Issuer: <aqua>{issuer}
        <reset><white>Reason: <aqua>{reason}
        <reset><white>Duration: <aqua>{duration}
        <reset><white>Active: <aqua>{active}
        
        <reset><white>Pardoner: <aqua>{pardoner}
        <reset><white>Pardon reason: <aqua>{pardonReason}
        """)
public record PunishmentInfoLore(Punishment punishment, HashMap<UUID, String> uuidToNameCache) implements ConfigurableMessage {

    @Override
    public String getFormatted() {

        String format = getUnformatted();
        format = format.replace("{occurred}", getTimeOccurred());
        format = format.replace("{target}", getDisplayTarget());
        format = format.replace("{issuer}", getDisplayIssuer());
        format = format.replace("{reason}", getDisplayReason());

        if(punishment.getPunishmentType().isDurational()) {
            format = format.replace("{duration}", getDisplayDuration());
            format = format.replace("{active}", getActivity());
        }else {
            format = stripLineWithKeyName(format, "duration");
            format = stripLineWithKeyName(format, "active");
        }

        if(punishment.isPardoned()) {
            format = format.replace("{pardoner}", getDisplayPardoner());
            format = format.replace("{pardonReason}", getPardonReason());
        } else {
            format = stripLineWithKeyName(format, "pardoner");
            format = stripLineWithKeyName(format, "pardonReason");
        }

        String removeRandomNewLinesRegex = "\\n*\\z";
        format = format.replaceAll(removeRandomNewLinesRegex, "");

        return format;
    }

    private String getTimeOccurred() {
        return new SimpleDateFormat("dd-MM-yyyy").format(punishment.getOccurredMillis());
    }

    private String getDisplayTarget() {
        return uuidToNameCache.get(punishment.getTarget().fetchUUID());
    }

    private String getDisplayIssuer() {
        return uuidToNameCache.getOrDefault(punishment.getIssuer().fetchUUID(), new ConsoleName().getAsString());
    }

    private String getDisplayReason() {
        return punishment.getReason() != null ? punishment.getReason() : new DefaultPunishmentReason().getAsString();
    }

    private String getDisplayDuration() {
        return punishment.getDurationMillis().getFormatted(new PermanentDuration().getAsString());
    }

    private String getActivity() {
        if(punishment.isActive()) {
            return "✔";
        } else {
            return "✖";
        }
    }

    private String getDisplayPardoner() {
        return punishment.getPardoner().fetchDisplayableName(new ConsoleName().getAsString());
    }

    private String getPardonReason() {
        return punishment.getPardonReason() != null ? punishment.getPardonReason() : new DefaultPunishmentReason().getAsString();
    }

    private String stripLineWithKeyName(String stringToStrip, String keyName) {
        Pattern stripKeyNameLinePattern = Pattern.compile("^.*\\{"+keyName+"}.*$\\n", Pattern.MULTILINE);
        return stripKeyNameLinePattern.matcher(stringToStrip).replaceAll("");
    }

}
