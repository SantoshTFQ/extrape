package com.techlabs.extrape.model;

public class ReelModel {
    private String id, caption, thumb, permalink;
    private boolean configured, automationEnabled;
    private int clicks, dms;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getThumb() { return thumb; }
    public void setThumb(String thumb) { this.thumb = thumb; }

    public String getPermalink() { return permalink; }
    public void setPermalink(String permalink) { this.permalink = permalink; }

    public boolean isConfigured() { return configured; }
    public void setConfigured(boolean configured) { this.configured = configured; }

    public boolean isAutomationEnabled() { return automationEnabled; }
    public void setAutomationEnabled(boolean automationEnabled) { this.automationEnabled = automationEnabled; }

    public int getClicks() { return clicks; }
    public void setClicks(int clicks) { this.clicks = clicks; }

    public int getDms() { return dms; }
    public void setDms(int dms) { this.dms = dms; }
}
