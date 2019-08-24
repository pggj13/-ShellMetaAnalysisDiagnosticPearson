package net.unesc.roma.managedbean.diagnostics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird;
import net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.EstudoDersimonianAndLaird;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel;

public class CriarOddsRatio {

    private Integer precisao;
    private String arredondamento;
    private String intervaloConfiaca;
    private List<EstudoMantelHaenszel> estudos;
    private List<EstudoMantelHaenszel> estudosCalculados;
    private BigDecimal oddsRatioMantelHaenszel;
    private BigDecimal SenMantelHaenszel;
    private BigDecimal SpecMantelHaenszel;
    private BigDecimal mhLimiteSuperior;
    private BigDecimal mhLimiteInferior;
    private BigDecimal mhLimiteSuperiorSen;
    private BigDecimal mhLimiteInferiorSen;
    private BigDecimal mhLimiteSuperiorSpec;
    private BigDecimal mhLimiteInferiorSpec;
    private BigDecimal chiSquared;
    private BigDecimal iSquared;

    @PostConstruct
    public void init() {
        arredondamento = "B";
        intervaloConfiaca = "95";
        precisao = 3;
        estudos = new ArrayList<>();
        estudos.add(new EstudoMantelHaenszel());
        estudosCalculados = new ArrayList<>();
    }

    public String getIntervaloConfiaca() {
        return intervaloConfiaca;
    }

    public void setIntervaloConfiaca(String intervaloConfiaca) {
        this.intervaloConfiaca = intervaloConfiaca;
    }

    public List<EstudoMantelHaenszel> getEstudos() {
        return estudos;
    }

    public void setEstudos(List<EstudoMantelHaenszel> estudos) {
        this.estudos = estudos;
    }

    public void exluirEstudo(EstudoMantelHaenszel estudo) {
        if (estudos != null) {
            estudos.remove(estudo);
        }
    }

    public void addEstudo() {

        estudos.add(new EstudoMantelHaenszel());
    }

    public void addEstudos() {

        estudos = new ArrayList<>();

        estudos.add(new EstudoMantelHaenszel("Bednarek et al., 1997", new BigDecimal("99"), new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("14")));
        estudos.add(new EstudoMantelHaenszel("Carey et al., 1996", new BigDecimal("82"), new BigDecimal("0.5"), new BigDecimal("23"), new BigDecimal("20")));
        estudos.add(new EstudoMantelHaenszel("He et al., 2004", new BigDecimal("47"), new BigDecimal("25"), new BigDecimal("5"), new BigDecimal("73")));
        estudos.add(new EstudoMantelHaenszel("Hiyama et al., 1996", new BigDecimal("130"), new BigDecimal("13"), new BigDecimal("10"), new BigDecimal("83")));

    }

    public void calcular() {
        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);
        for (EstudoMantelHaenszel estudo : estudos) {
            estudo.setOddsRatio(OddsRatio.calcularOddsRatio(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperior(OddsRatio.calcularIntervaloConfiancaSuperior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferior(OddsRatio.calcularIntervaloConfiancaInferior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setPeso(MantelHaenszel.calcularPeso(estudo, precisao, metodoArredondamento));
           System.out.println(estudo.toString());
        }

        estudosCalculados = new ArrayList<>(estudos);
        setOddsRatioMantelHaenszel(MantelHaenszel.calcularOddsRatioMantelHaenszel(estudos, precisao, metodoArredondamento));
        setMhLimiteSuperior(MantelHaenszel.calcularIntervaloConfiancaSuperior(estudos, precisao, metodoArredondamento, zScore));
        setMhLimiteInferior(MantelHaenszel.calcularIntervaloConfiancaInferior(estudos, precisao, metodoArredondamento, zScore));
        setChiSquared(MantelHaenszel.calcularChiSquared(estudos, precisao, metodoArredondamento));
        setISquared(MantelHaenszel.calcularISquared(estudos, metodoArredondamento));
        MantelHaenszel.calcularPercentualPeso(estudos, metodoArredondamento);
    }

    public BigDecimal getSenMantelHaenszel() {
        return SenMantelHaenszel;
    }

    public void setSenMantelHaenszel(BigDecimal SenMantelHaenszel) {
        this.SenMantelHaenszel = SenMantelHaenszel;
    }

    public BigDecimal getSpecMantelHaenszel() {
        return SpecMantelHaenszel;
    }

    public void setSpecMantelHaenszel(BigDecimal SpecMantelHaenszel) {
        this.SpecMantelHaenszel = SpecMantelHaenszel;
    }

    public BigDecimal getOddsRatioMantelHaenszel() {
        return oddsRatioMantelHaenszel;
    }

    public void setOddsRatioMantelHaenszel(BigDecimal oddsRatioMantelHaenszel) {
        this.oddsRatioMantelHaenszel = oddsRatioMantelHaenszel;
    }

    public List<EstudoMantelHaenszel> getEstudosCalculados() {
        return estudosCalculados;
    }

    public void setEstudosCalculados(List<EstudoMantelHaenszel> estudosCalculados) {
        this.estudosCalculados = estudosCalculados;
    }

    public Integer getPrecisao() {
        return precisao;
    }

    public void setPrecisao(Integer precisao) {
        this.precisao = precisao;
    }

    public String getArredondamento() {
        return arredondamento;
    }

    public void setArredondamento(String arredondamento) {
        this.arredondamento = arredondamento;
    }

    public BigDecimal getMhLimiteSuperior() {
        return mhLimiteSuperior;
    }

    public void setMhLimiteSuperior(BigDecimal mhLimiteSuperior) {
        this.mhLimiteSuperior = mhLimiteSuperior;
    }

    public BigDecimal getMhLimiteInferior() {
        return mhLimiteInferior;
    }

    public void setMhLimiteInferior(BigDecimal mhLimiteInferior) {
        this.mhLimiteInferior = mhLimiteInferior;
    }

    public BigDecimal getChiSquared() {
        return chiSquared;
    }

    public void setChiSquared(BigDecimal chiSquared) {
        this.chiSquared = chiSquared;
    }

    public BigDecimal getISquared() {
        return iSquared;
    }

    public void setISquared(BigDecimal iSquared) {
        this.iSquared = iSquared;
    }

    public BigDecimal getMhLimiteSuperiorSen() {
        return mhLimiteSuperiorSen;
    }

    public void setMhLimiteSuperiorSen(BigDecimal mhLimiteSuperiorSen) {
        this.mhLimiteSuperiorSen = mhLimiteSuperiorSen;
    }

    public BigDecimal getMhLimiteInferiorSen() {
        return mhLimiteInferiorSen;
    }

    public void setMhLimiteInferiorSen(BigDecimal mhLimiteInferiorSen) {
        this.mhLimiteInferiorSen = mhLimiteInferiorSen;
    }

    public BigDecimal getMhLimiteSuperiorSpec() {
        return mhLimiteSuperiorSpec;
    }

    public void setMhLimiteSuperiorSpec(BigDecimal mhLimiteSuperiorSpec) {
        this.mhLimiteSuperiorSpec = mhLimiteSuperiorSpec;
    }

    public BigDecimal getMhLimiteInferiorSpec() {
        return mhLimiteInferiorSpec;
    }

    public void setMhLimiteInferiorSpec(BigDecimal mhLimiteInferiorSpec) {
        this.mhLimiteInferiorSpec = mhLimiteInferiorSpec;
    }

    public BigDecimal getiSquared() {
        return iSquared;
    }

    public void setiSquared(BigDecimal iSquared) {
        this.iSquared = iSquared;
    }

    public void calcularDersimmonianAndLaird() {
        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);
        for (EstudoMantelHaenszel estudo : estudos) {
            estudo.setOddsRatio(OddsRatio.calcularOddsRatio(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperior(OddsRatio.calcularIntervaloConfiancaSuperior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferior(OddsRatio.calcularIntervaloConfiancaInferior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setLogOddsRatio(DersimonianAndLaird.calcularRaizDoLogOddsRatio(estudo, precisao, metodoArredondamento));
        }

        estudosCalculados = new ArrayList<>(estudos);
        setOddsRatioMantelHaenszel(MantelHaenszel.calcularOddsRatioMantelHaenszel(estudos, precisao, metodoArredondamento));
        setMhLimiteSuperior(MantelHaenszel.calcularIntervaloConfiancaSuperior(estudos, precisao, metodoArredondamento, zScore));
        setMhLimiteInferior(MantelHaenszel.calcularIntervaloConfiancaInferior(estudos, precisao, metodoArredondamento, zScore));
        setChiSquared(MantelHaenszel.calcularChiSquared(estudos, precisao, metodoArredondamento));
        setISquared(MantelHaenszel.calcularISquared(estudos, metodoArredondamento));
        MantelHaenszel.calcularPercentualPeso(estudos, metodoArredondamento);
    }

    @Override
    public String toString() {
        return "CriarOddsRatio{" + "precisao=" + precisao + ", arredondamento=" + arredondamento + ", intervaloConfiaca=" + intervaloConfiaca + ", estudos=" + estudos + ", estudosCalculados=" + estudosCalculados + ", oddsRatioMantelHaenszel=" + oddsRatioMantelHaenszel + ", SenMantelHaenszel=" + SenMantelHaenszel + ", SpecMantelHaenszel=" + SpecMantelHaenszel + ", mhLimiteSuperior=" + mhLimiteSuperior + ", mhLimiteInferior=" + mhLimiteInferior + ", mhLimiteSuperiorSen=" + mhLimiteSuperiorSen + ", mhLimiteInferiorSen=" + mhLimiteInferiorSen + ", mhLimiteSuperiorSpec=" + mhLimiteSuperiorSpec + ", mhLimiteInferiorSpec=" + mhLimiteInferiorSpec + ", chiSquared=" + chiSquared + ", iSquared=" + iSquared + '}';
    }
}
