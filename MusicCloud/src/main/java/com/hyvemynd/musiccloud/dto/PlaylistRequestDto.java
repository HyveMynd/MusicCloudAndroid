package com.hyvemynd.musiccloud.dto;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class PlaylistRequestDto {
    public String Email;
    public String Name;
    public boolean IsTemporary;

    public PlaylistRequestDto() {
        this("","",false);
    }

    public PlaylistRequestDto(String email, String name, boolean isTemporary) {
        Email = email;
        Name = name;
        IsTemporary = isTemporary;
    }
}
