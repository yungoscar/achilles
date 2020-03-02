package me.marvin.achilles.punishment.impl;

import me.marvin.achilles.Achilles;
import me.marvin.achilles.Variables;
import me.marvin.achilles.punishment.ExpirablePunishment;
import me.marvin.achilles.punishment.Punishment;
import me.marvin.achilles.punishment.PunishmentHandler;
import me.marvin.achilles.utils.UUIDConverter;
import me.marvin.achilles.utils.sql.BatchContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Supplier;

import static me.marvin.achilles.Variables.Database.MUTE_TABLE_NAME;

public class Mute extends ExpirablePunishment {
    public Mute() {
        this.active = true;
        this.server = Variables.Database.SERVER_NAME;
        this.issuedOn = System.currentTimeMillis();
    }

    public Mute(UUID issuer, UUID target, long until, String reason) {
        this.issuer = issuer;
        this.target = target;
        this.until = until;
        this.issueReason = reason;
        this.active = true;
        this.server = Variables.Database.SERVER_NAME;
        this.issuedOn = System.currentTimeMillis();
    }

    @Override
    protected void issuePunishment() {
        Achilles.getConnection().update(PunishmentHandler.MUTE_HANDLER.getIssueQuery(),
            (result) -> {}, server, UUIDConverter.to(issuer), UUIDConverter.to(target), issueReason, until, issuedOn, active);
    }

    @Override
    protected void liftPunishment() {
        Achilles.getConnection().update(PunishmentHandler.MUTE_HANDLER.getLiftQuery(),
            (result) -> {}, createLiftBatch());
    }

    @Override
    public BatchContainer createLiftBatch() {
        this.liftedOn = System.currentTimeMillis();
        this.active = false;
        return new BatchContainer(active, liftedOn, liftReason, UUIDConverter.to(liftedBy), UUIDConverter.to(target), id);
    }

    @Override
    protected void expirePunishment() {
        Achilles.getConnection().update("UPDATE `" + Variables.Database.MUTE_TABLE_NAME + "` SET " +
            "active = ? " +
            "WHERE target = ? " +
            "AND id = ?;",
            (result) -> {}, active, UUIDConverter.to(target), id);
    }

    @Override
    public boolean isInstanceOf(Punishment punishment) {
        return punishment instanceof Mute;
    }

    public static class Handler implements PunishmentHandler<Mute> {
        @Override
        public String getTable() {
            return MUTE_TABLE_NAME;
        }

        @Override
        public void createTable() {
            Achilles.getConnection().update("CREATE TABLE IF NOT EXISTS `" + MUTE_TABLE_NAME + "` ("
                    + "`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,"
                    + "`server` varchar(200) NOT NULL,"
                    + "`issuer` binary(16) NOT NULL,"
                    + "`target` binary(16) NOT NULL,"
                    + "`issueReason` varchar(200) NOT NULL,"
                    + "`issuedOn` bigint NOT NULL,"
                    + "`until` bigint NOT NULL,"
                    + "`liftedBy` binary(16),"
                    + "`liftedOn` bigint,"
                    + "`liftReason` varchar(200),"
                    + "`active` tinyint(1) NOT NULL) DEFAULT CHARSET=utf8;",
                (result) -> {}
            );
        }

        @Override
        public Supplier<Mute> getSupplier() {
            return Mute::new;
        }

        @Override
        public String getIssueQuery() {
            return "INSERT INTO `" + MUTE_TABLE_NAME + "` (" +
                "`server`, " +
                "`issuer`, " +
                "`target`, " +
                "`issueReason`, " +
                "`until`, " +
                "`issuedOn`, " +
                "`active`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        }

        @Override
        public String getLiftQuery() {
            return "UPDATE `" + MUTE_TABLE_NAME + "` SET " +
                "active = ?, " +
                "liftedOn = ?, " +
                "liftReason = ?, " +
                "liftedBy = ? " +
                "WHERE target = ? " +
                "AND id = ?;";
        }
    }
}
