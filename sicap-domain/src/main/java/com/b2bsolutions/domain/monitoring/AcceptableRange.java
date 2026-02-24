package com.b2bsolutions.domain.monitoring;

public record AcceptableRange(
        double min,
        double max
) {

    public boolean isWithIn(double value){
        return  value >= min && value <= max;
    }
}
