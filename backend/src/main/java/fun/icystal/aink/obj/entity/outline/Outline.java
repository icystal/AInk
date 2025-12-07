package fun.icystal.aink.obj.entity.outline;

import lombok.Data;

import java.util.List;

@Data
public class Outline {

    private Sentence sentence;

    private Brief brief;

    private List<Profile> profiles;

    private Skeleton skeleton;

    private List<Origin> origins;

    private Synopsis synopsis;

    private List<Biography> biographies;

}
