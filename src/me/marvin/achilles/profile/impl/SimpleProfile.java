package me.marvin.achilles.profile.impl;

import lombok.Getter;
import me.marvin.achilles.Achilles;
import me.marvin.achilles.profile.Profile;
import me.marvin.achilles.punishment.LiftablePunishment;
import me.marvin.achilles.punishment.Punishment;
import me.marvin.achilles.punishment.PunishmentHandlerData;
import me.marvin.achilles.utils.UUIDConverter;

import java.sql.SQLException;
import java.util.*;

@Getter
public class SimpleProfile extends Profile {
    private Map<Class<? extends Punishment>, List<Punishment>> cache;

    public SimpleProfile(UUID uuid) {
        super(uuid);
        this.cache = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends LiftablePunishment> Optional<T> getActive(Class<? extends T> type) {
        return (Optional<T>) cache.computeIfAbsent(type, (ignored) -> {
            PunishmentHandlerData data = Achilles.getHandlers().get(type);
            if (data == null) {
                throw new RuntimeException("found no handlers for punishment " + type.getSimpleName());
            }
            return new ArrayList<>(getPunishmentsFromTable(type, false));
        }).stream().map(punishment -> (LiftablePunishment) punishment).filter(LiftablePunishment::isActive).min(Comparator.comparingLong(Punishment::getId));
    }

    private List<Punishment> getPunishmentsFromTable(Class<? extends Punishment> type, boolean async) {
        PunishmentHandlerData data = Achilles.getHandlers().get(type);
        if (data == null) {
            throw new RuntimeException("found no handlers for punishment " + type.getSimpleName());
        }
        List<Punishment> list = new ArrayList<>();
        Achilles.getConnection().query(async, "SELECT * FROM `" + data.getTable() + "` WHERE `target` = ?", (result) -> {
            try {
                while (result.next()) {
                    Punishment punishment = data.getSupplier().get();
                    punishment.fromResultSet(result);
                    list.add(punishment);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, UUIDConverter.to(uuid));
        return list;
    }
}
