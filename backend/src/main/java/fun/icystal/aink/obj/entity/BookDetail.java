package fun.icystal.aink.obj.entity;

import fun.icystal.aink.obj.entity.outline.Brief;
import fun.icystal.aink.obj.entity.outline.Profile;
import fun.icystal.aink.obj.entity.outline.Sentence;
import lombok.Data;

import java.util.List;

@Data
public class BookDetail {

    private Sentence sentence;

    private Brief brief;

    private List<Profile> profiles;


}
