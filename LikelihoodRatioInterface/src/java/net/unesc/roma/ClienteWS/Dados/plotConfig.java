package net.unesc.roma.ClienteWS.Dados;

import java.math.BigDecimal;

public class plotConfig {

    public String effectLabel;
    public int risk;
    public String mountNode;
    public int width;
    public int fontSize;
    public String fontFamily;
    public int vCentralBar;
    public int nTicks;
    public String url;
    public String type;
    public String obs;
    public int maxPositive;
    public BigDecimal dor;
    public BigDecimal dorInferior;
    public BigDecimal dorSuperior;
    public boolean basicPlot;

    public plotConfig(String effectLabel, int risk, String mountNode, int width, int fontSize, String fontFamily, int vCentralBar, int nTicks, String url, String type, String obs, int maxPositive, boolean basicPlot) {
        this.effectLabel = effectLabel;
        this.risk = risk;
        this.mountNode = mountNode;
        this.width = width;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.vCentralBar = vCentralBar;
        this.nTicks = nTicks;
        this.url = url;
        this.type = type;
        this.obs = obs;
        this.maxPositive = maxPositive;
        this.basicPlot = basicPlot;
    }

    public plotConfig(String effectLabel, int risk, String mountNode, int vCentralBar, int nTicks, String url, String type, String obs, boolean basicPlot) {
        this.effectLabel = effectLabel;
        this.risk = risk;
        this.mountNode = mountNode;
        this.vCentralBar = vCentralBar;
        this.nTicks = nTicks;
        this.url = url;
        this.type = type;
        this.obs = obs;
        this.basicPlot = basicPlot;
    }

    //Add curva roc
    public plotConfig(String type, String url, String mountNode,BigDecimal dor,BigDecimal dorInferior,BigDecimal dorSuperior) {

       this.mountNode = mountNode;
        this.url = url;
        this.dor = dor;
        this.dorInferior = dorInferior;
        this.dorSuperior = dorSuperior;
        this.type = type;
        //this.basicPlot = basicPlot;
    }

    public plotConfig() {
    }
}
