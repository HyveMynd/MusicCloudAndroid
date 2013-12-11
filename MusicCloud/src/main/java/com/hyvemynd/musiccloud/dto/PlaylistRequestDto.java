package com.hyvemynd.musiccloud.dto;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class PlaylistRequestDto {
    private String email;
    private String name;
    private boolean isTemporary;

    public PlaylistRequestDto() {
        this("","",false);
    }

    public PlaylistRequestDto(String email, String name, boolean isTemporary) {
        this.email = email;
        this.name = name;
        this.isTemporary = isTemporary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean isTemporary) {
        this.isTemporary = isTemporary;
    }
}
