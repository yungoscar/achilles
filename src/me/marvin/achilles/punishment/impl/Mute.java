package me.marvin.achilles.punishment.impl;

import me.marvin.achilles.Achilles;
import me.marvin.achilles.Variables;
import me.marvin.achilles.punishment.ExpirablePunishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Mute extends ExpirablePunishment {
    public Mute() {}

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
        Achilles.getConnection().update("INSERT INTO `" + Variables.Database.MUTE_TABLE_NAME + "` (" +
            "`server`, " +
            "`issuer`, " +
            "`target`, " +
            "`issueReason`, " +
            "`until`, " +
            "`issuedOn`, " +
            "`active`) VALUES (?, ?, ?, ?, ?, ?, ?);",
            (result) -> {}, server, issuer.toString(), target.toString(), issueReason, until, issuedOn, active);
    }

    @Override
    protected void liftPunishment() {
        Achilles.getConnection().update("UPDATE `" + Variables.Database.MUTE_TABLE_NAME + "` SET " +
            "active = ?, " +
            "liftedOn = ?, " +
            "liftReason = ?, " +
            "liftedBy = ? " +
            "WHERE target = ? " +
            "AND id = ?;",
            (result) -> {}, active, liftedOn, liftReason, liftedBy.toString(), target.toString(), id);
    }

    @Override
    public void expirePunishment() {
        if (!active || !isExpired()) return;
        Achilles.getConnection().update("UPDATE `" + Variables.Database.MUTE_TABLE_NAME + "` SET " +
            "active = ?, " +
            "WHERE target = ? " +
            "AND id = ?;",
            (result) -> {}, active, target.toString(), id);
    }

    @Override
    public void fromResultSet(ResultSet rs) throws SQLException {
        this.server = rs.getString("server");
        this.issuer = UUID.fromString(rs.getString("issuer"));
        this.target = UUID.fromString(rs.getString("target"));
        this.issueReason = rs.getString("issueReason");
        this.until = rs.getLong("until");
        this.issuedOn = rs.getLong("issuedOn");
        this.active = rs.getBoolean("active");
        this.id = rs.getInt("id");
        if (!this.active) {
            this.liftedBy = UUID.fromString(rs.getString("liftedBy"));
            this.liftReason = rs.getString("liftReason");
            this.liftedOn = rs.getLong("liftedOn");
        }
    }
}
