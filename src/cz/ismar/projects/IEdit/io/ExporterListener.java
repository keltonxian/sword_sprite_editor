package cz.ismar.projects.IEdit.io;


public interface ExporterListener
{
    public abstract void exportUpdated(int eventId, int percent);

    public static final int EVENT_STARTED = 0;
    public static final int EVENT_STOPED = 1;
    public static final int EVENT_ENDED = 2;
    public static final int EVENT_UPDATE = 3;
}
