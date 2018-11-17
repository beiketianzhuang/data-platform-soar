package com.bektz.dataplatformsoar.constants;

import java.util.Objects;

public enum QueryStatus {
    
    SUCCESS("查询成功"), FAILURE("查询失败");
    public String value;
    private QueryStatus(String value) {
        this.value = value;
    }
    public Boolean isSuccess() {
        return Objects.equals(SUCCESS, this);
    }
}