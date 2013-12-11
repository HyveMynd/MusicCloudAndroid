package com.hyvemynd.musiccloud.dto;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class PlaylistResponseDto {
    private int id;
    private boolean isTemporary;
    private String expirationDate;
    private String name;
    private int items;

    public PlaylistResponseDto() {
        this(0, false, "", "", 0);
    }

    public PlaylistResponseDto(int id, boolean isTemporary, String expirationDate, String name, int items) {
        this.id = id;
        this.isTemporary = isTemporary;
        this.expirationDate = expirationDate;
        this.name = name;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean isTemporary) {
        this.isTemporary = isTemporary;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
