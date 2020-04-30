package fr.vexia.api.sanctions;

import java.util.Date;
import java.util.UUID;

public class VexiaSanction {

    private final UUID id;
    private final UUID player;
    private final UUID moderator;
    private final SanctionType sanctionType;
    private final String reason;
    private final Date start;
    private Date end;
    private final boolean finished;

    public VexiaSanction(UUID id, UUID player, UUID moderator, SanctionType sanctionType, String reason, Date start, Date end, boolean finished) {
        this.id = id;
        this.player = player;
        this.moderator = moderator;
        this.sanctionType = sanctionType;
        this.reason = reason;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public VexiaSanction(UUID player, UUID moderator, SanctionType sanctionType, String reason, Date start, Date end) {
        this(UUID.randomUUID(), player, moderator, sanctionType, reason, start, end, false);
    }

    public UUID getID() {
        return id;
    }
    public UUID getPlayer() {
        return player;
    }

    public UUID getModerator() {
        return moderator;
    }

    public SanctionType getSanctionType() {
        return sanctionType;
    }

    public String getReason() {
        return reason;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}
