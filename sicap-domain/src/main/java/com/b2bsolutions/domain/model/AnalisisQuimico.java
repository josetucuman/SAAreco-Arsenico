package com.b2bsolutions.domain.model;

import java.time.Instant;

public final class AnalisisQuimico {
    private final double arsenicoMgL;
    private final double ph;
    private final double turbidezNTU;
    private final double temperaturaC;
    private final Instant fechaAnalisis;


    public AnalisisQuimico(double arsenicoMgL, double ph, double turbidezNTU, double temperaturaC, Instant fechaAnalisis) {
        this.arsenicoMgL = arsenicoMgL;
        this.ph = ph;
        this.turbidezNTU = turbidezNTU;
        this.temperaturaC = temperaturaC;
        this.fechaAnalisis = fechaAnalisis;
    }

    public double getArsenicoMgL() {
        return arsenicoMgL;
    }

    public double getPh() {
        return ph;
    }

    public double getTurbidezNTU() {
        return turbidezNTU;
    }

    public double getTemperaturaC() {
        return temperaturaC;
    }

    public Instant getFechaAnalisis() {
        return fechaAnalisis;
    }
}
