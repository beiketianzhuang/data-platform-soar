package com.bektz.dataplatformsoar.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Data
@ConfigurationProperties("soar-configs.skills")
@Component
public class SoarConfiguration {

    private  Map<String, Skill> skillsMap;

    public  Map<String, Skill> getSkillsMap() {
        return skillsMap;
    }

    @Data
    public static class Skill implements Serializable {
        private int type;
        private boolean isDefault;
    }
}
