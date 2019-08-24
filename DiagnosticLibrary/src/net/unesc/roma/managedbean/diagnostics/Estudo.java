/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.diagnostics;

import java.math.BigDecimal;
import java.util.Objects;

public class Estudo implements Cloneable {

    private String nome;
    private BigDecimal verdadeiroPositivo;
    private BigDecimal verdadeiroNegativo;
    private BigDecimal falsoPositivo;
    private BigDecimal falsoNegativo;
    private BigDecimal oddsRatio;
    private BigDecimal Sen;
    private BigDecimal Specy;
    private BigDecimal logOddsRatio;
    private BigDecimal variancia;
    private BigDecimal erroPadrao;
    private BigDecimal intervaloConfiancaSuperior;
    private BigDecimal intervaloConfiancaInferior;
    private BigDecimal intervaloConfiancaSuperiorSens;
    private BigDecimal intervaloConfiancaInferiorSens;
    private BigDecimal intervaloConfiancaSuperiorSpec;
    private BigDecimal intervaloConfiancaInferiorSpec;
    private BigDecimal distribuicaoLogOddsRatioDersimonian;
    private BigDecimal positiveLikelihoodRatio;
    private BigDecimal negativeLikelihoodRatio;
    private BigDecimal logPositiveLikelihoodRatio;
    private BigDecimal logNegativeLikelihoodRatio;
    private BigDecimal varianciaLRPositiva;
    private BigDecimal varianciaLRNegativa;
    private BigDecimal erroPadraoLRPositiva;
    private BigDecimal erroPadraoLRNegativa;
    private BigDecimal intervaloConfiancaSuperiorLRPositiva;
    private BigDecimal intervaloConfiancaInferiorLRPositiva;
    private BigDecimal intervaloConfiancaSuperiorLRNegativa;
    private BigDecimal intervaloConfiancaInferiorLRNegativa;

    

    public Estudo() {
    }

    public Estudo(String nome, BigDecimal verdadeiroPositivo, BigDecimal falsoPositivo, BigDecimal falsoNegativo, BigDecimal verdadeiroNegativo) {
        this.nome = nome;
        this.verdadeiroPositivo = verdadeiroPositivo;
        this.falsoPositivo = falsoPositivo;
        this.falsoNegativo = falsoNegativo;
        this.verdadeiroNegativo = verdadeiroNegativo;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getVerdadeiroPositivo() {
        return verdadeiroPositivo;
    }

    public void setVerdadeiroPositivo(BigDecimal verdadeiroPositivo) {
        this.verdadeiroPositivo = verdadeiroPositivo;
    }

    public BigDecimal getVerdadeiroNegativo() {
        return verdadeiroNegativo;
    }

    public void setVerdadeiroNegativo(BigDecimal verdadeiroNegativo) {
        this.verdadeiroNegativo = verdadeiroNegativo;
    }

    public BigDecimal getFalsoPositivo() {
        return falsoPositivo;
    }

    public void setFalsoPositivo(BigDecimal falsoPositivo) {
        this.falsoPositivo = falsoPositivo;
    }

    public BigDecimal getFalsoNegativo() {
        return falsoNegativo;
    }

    public void setFalsoNegativo(BigDecimal falsoNegativo) {
        this.falsoNegativo = falsoNegativo;
    }

    public BigDecimal getSen() {
        return Sen;
    }

    public void setSen(BigDecimal Sen) {
        this.Sen = Sen;
    }

    public BigDecimal getSpecy() {
        return Specy;
    }

    public void setSpecy(BigDecimal Specy) {
        this.Specy = Specy;
    }

    public BigDecimal getOddsRatio() {
        return oddsRatio;
    }

    public void setOddsRatio(BigDecimal oddsRatio) {
        this.oddsRatio = oddsRatio;
    }

    public BigDecimal getLogOddsRatio() {
        return logOddsRatio;
    }

    public void setLogOddsRatio(BigDecimal logOddsRatio) {
        this.logOddsRatio = logOddsRatio;
    }

    public BigDecimal getVariancia() {
        return variancia;
    }

    public void setVariancia(BigDecimal variancia) {
        this.variancia = variancia;
    }

    public BigDecimal getErroPadrao() {
        return erroPadrao;
    }

    public void setErroPadrao(BigDecimal erroPadrao) {
        this.erroPadrao = erroPadrao;
    }

    public BigDecimal getIntervaloConfiancaSuperior() {
        return intervaloConfiancaSuperior;
    }

    public void setIntervaloConfiancaSuperior(BigDecimal intervaloConfiancaSuperior) {
        this.intervaloConfiancaSuperior = intervaloConfiancaSuperior;
    }

    public BigDecimal getIntervaloConfiancaInferior() {
        return intervaloConfiancaInferior;
    }

    public void setIntervaloConfiancaInferior(BigDecimal intervaloConfiancaInferior) {
        this.intervaloConfiancaInferior = intervaloConfiancaInferior;
    }

    public BigDecimal getIntervaloConfiancaSuperiorSens() {
        return intervaloConfiancaSuperiorSens;
    }

    public void setIntervaloConfiancaSuperiorSens(BigDecimal intervaloConfiancaSuperiorSens) {
        this.intervaloConfiancaSuperiorSens = intervaloConfiancaSuperiorSens;
    }

    public BigDecimal getIntervaloConfiancaInferiorSens() {
        return intervaloConfiancaInferiorSens;
    }

    public void setIntervaloConfiancaInferiorSens(BigDecimal intervaloConfiancaInferiorSens) {
        this.intervaloConfiancaInferiorSens = intervaloConfiancaInferiorSens;
    }

    public BigDecimal getIntervaloConfiancaSuperiorSpec() {
        return intervaloConfiancaSuperiorSpec;
    }

    public void setIntervaloConfiancaSuperiorSpec(BigDecimal intervaloConfiancaSuperiorSpec) {
        this.intervaloConfiancaSuperiorSpec = intervaloConfiancaSuperiorSpec;
    }

    public BigDecimal getIntervaloConfiancaInferiorSpec() {
        return intervaloConfiancaInferiorSpec;
    }

    public void setIntervaloConfiancaInferiorSpec(BigDecimal intervaloConfiancaInferiorSpec) {
        this.intervaloConfiancaInferiorSpec = intervaloConfiancaInferiorSpec;
    }

    public BigDecimal getPositiveLikelihoodRatio() {
        return positiveLikelihoodRatio;
    }

    public void setPositiveLikelihoodRatio(BigDecimal positiveLikelihoodRatio) {
        this.positiveLikelihoodRatio = positiveLikelihoodRatio;
    }

    public BigDecimal getNegativeLikelihoodRatio() {
        return negativeLikelihoodRatio;
    }

    public void setNegativeLikelihoodRatio(BigDecimal negativeLikelihoodRatio) {
        this.negativeLikelihoodRatio = negativeLikelihoodRatio;
    }

    public BigDecimal getLogPositiveLikelihoodRatio() {
        return logPositiveLikelihoodRatio;
    }

    public void setLogPositiveLikelihoodRatio(BigDecimal logPositiveLikelihoodRatio) {
        this.logPositiveLikelihoodRatio = logPositiveLikelihoodRatio;
    }

    public BigDecimal getLogNegativeLikelihoodRatio() {
        return logNegativeLikelihoodRatio;
    }

    public void setLogNegativeLikelihoodRatio(BigDecimal logNegativeLikelihoodRatio) {
        this.logNegativeLikelihoodRatio = logNegativeLikelihoodRatio;
    }

    public BigDecimal getVarianciaLRPositiva() {
        return varianciaLRPositiva;
    }

    public void setVarianciaLRPositiva(BigDecimal varianciaLRPositiva) {
        this.varianciaLRPositiva = varianciaLRPositiva;
    }

    public BigDecimal getVarianciaLRNegativa() {
        return varianciaLRNegativa;
    }

    public void setVarianciaLRNegativa(BigDecimal varianciaLRNegativa) {
        this.varianciaLRNegativa = varianciaLRNegativa;
    }

    public BigDecimal getErroPadraoLRPositiva() {
        return erroPadraoLRPositiva;
    }

    public void setErroPadraoLRPositiva(BigDecimal erroPadraoLRPositiva) {
        this.erroPadraoLRPositiva = erroPadraoLRPositiva;
    }

    public BigDecimal getErroPadraoLRNegativa() {
        return erroPadraoLRNegativa;
    }

    public void setErroPadraoLRNegativa(BigDecimal erroPadraoLRNegativa) {
        this.erroPadraoLRNegativa = erroPadraoLRNegativa;
    }

    public BigDecimal getIntervaloConfiancaSuperiorLRPositiva() {
        return intervaloConfiancaSuperiorLRPositiva;
    }

    public void setIntervaloConfiancaSuperiorLRPositiva(BigDecimal intervaloConfiancaSuperiorLRPositiva) {
        this.intervaloConfiancaSuperiorLRPositiva = intervaloConfiancaSuperiorLRPositiva;
    }

    public BigDecimal getIntervaloConfiancaInferiorLRPositiva() {
        return intervaloConfiancaInferiorLRPositiva;
    }

    public void setIntervaloConfiancaInferiorLRPositiva(BigDecimal intervaloConfiancaInferiorLRPositiva) {
        this.intervaloConfiancaInferiorLRPositiva = intervaloConfiancaInferiorLRPositiva;
    }

    public BigDecimal getIntervaloConfiancaSuperiorLRNegativa() {
        return intervaloConfiancaSuperiorLRNegativa;
    }

    public void setIntervaloConfiancaSuperiorLRNegativa(BigDecimal intervaloConfiancaSuperiorLRNegativa) {
        this.intervaloConfiancaSuperiorLRNegativa = intervaloConfiancaSuperiorLRNegativa;
    }

    public BigDecimal getIntervaloConfiancaInferiorLRNegativa() {
        return intervaloConfiancaInferiorLRNegativa;
    }

    public void setIntervaloConfiancaInferiorLRNegativa(BigDecimal intervaloConfiancaInferiorLRNegativa) {
        this.intervaloConfiancaInferiorLRNegativa = intervaloConfiancaInferiorLRNegativa;
    }

    public BigDecimal getSomalTP() {
        return falsoNegativo.add(falsoPositivo.add(verdadeiroNegativo.add(verdadeiroPositivo)));
    }

    public BigDecimal getSomaTN() {
        return falsoNegativo.add(falsoPositivo.add(verdadeiroNegativo.add(verdadeiroPositivo)));
    }

    public BigDecimal getTotalTP() {
        return falsoPositivo.add(falsoPositivo);
    }

    public BigDecimal getTotalTN() {

        return verdadeiroNegativo.add(verdadeiroNegativo);
    }

    public BigDecimal getTotal() {
        return falsoNegativo.add(falsoPositivo.add(verdadeiroNegativo.add(verdadeiroPositivo)));
    }

    public BigDecimal getDistribuicaoLogOddsRatioDersimonian() {
        return distribuicaoLogOddsRatioDersimonian;
    }

    public void setDistribuicaoLogOddsRatioDersimonian(BigDecimal distribuicaoLogOddsRatioDersimonian) {
        this.distribuicaoLogOddsRatioDersimonian = distribuicaoLogOddsRatioDersimonian;
    }
    
 
    /*
     @Override
     public int hashCode() {
     int hash = 7;
     hash = 59 * hash + Objects.hashCode(this.nome);
     hash = 59 * hash + Objects.hashCode(this.verdadeiroPositivo);
     hash = 59 * hash + Objects.hashCode(this.verdadeiroNegativo);
     hash = 59 * hash + Objects.hashCode(this.falsoPositivo);
     hash = 59 * hash + Objects.hashCode(this.falsoNegativo);
     hash = 59 * hash + Objects.hashCode(this.oddsRatio);
     hash = 59 * hash + Objects.hashCode(this.Sen);
     hash = 59 * hash + Objects.hashCode(this.Specy);
     hash = 59 * hash + Objects.hashCode(this.logOddsRatio);
     hash = 59 * hash + Objects.hashCode(this.variancia);
     hash = 59 * hash + Objects.hashCode(this.erroPadrao);
     hash = 59 * hash + Objects.hashCode(this.intervaloConfiancaSuperior);
     hash = 59 * hash + Objects.hashCode(this.intervaloConfiancaInferior);
     hash = 59 * hash + Objects.hashCode(this.intervaloConfiancaSuperiorSens);
     hash = 59 * hash + Objects.hashCode(this.intervaloConfiancaInferiorSens);
     hash = 59 * hash + Objects.hashCode(this.intervaloConfiancaSuperiorSpec);
     hash = 59 * hash + Objects.hashCode(this.intervaloConfiancaInferiorSpec);
     return hash;
     }

     @Override
     public boolean equals(Object obj) {
     if (obj == null) {
     return false;
     }
     if (getClass() != obj.getClass()) {
     return false;
     }
     final Estudo other = (Estudo) obj;
     if (!Objects.equals(this.nome, other.nome)) {
     return false;
     }
     if (!Objects.equals(this.verdadeiroPositivo, other.verdadeiroPositivo)) {
     return false;
     }
     if (!Objects.equals(this.verdadeiroNegativo, other.verdadeiroNegativo)) {
     return false;
     }
     if (!Objects.equals(this.falsoPositivo, other.falsoPositivo)) {
     return false;
     }
     if (!Objects.equals(this.falsoNegativo, other.falsoNegativo)) {
     return false;
     }
     if (!Objects.equals(this.oddsRatio, other.oddsRatio)) {
     return false;
     }
     if (!Objects.equals(this.Sen, other.Sen)) {
     return false;

     }
     if (!Objects.equals(this.Specy, other.Specy)) {
     return false;
     }
     if (!Objects.equals(this.logOddsRatio, other.logOddsRatio)) {
     return false;
     }
     if (!Objects.equals(this.variancia, other.variancia)) {
     return false;
     }
     if (!Objects.equals(this.erroPadrao, other.erroPadrao)) {
     return false;
     }
     if (!Objects.equals(this.intervaloConfiancaSuperior, other.intervaloConfiancaSuperior)) {
     return false;
     }
     if (!Objects.equals(this.intervaloConfiancaInferior, other.intervaloConfiancaInferior)) {
     return false;
     }
     if (!Objects.equals(this.intervaloConfiancaSuperior, other.intervaloConfiancaSuperiorSens)) {
     return false;
     }
     if (!Objects.equals(this.intervaloConfiancaInferior, other.intervaloConfiancaInferiorSens)) {
     return false;
     }
     if (!Objects.equals(this.intervaloConfiancaSuperior, other.intervaloConfiancaSuperiorSpec)) {
     return false;
     }
     if (!Objects.equals(this.intervaloConfiancaInferior, other.intervaloConfiancaInferiorSpec)) {
     return false;
     }
     return true;
     }

     @Override
     public String toString() {
     return "Estudo{" + "nome=" + nome + ", verdadeiroPositivo=" + verdadeiroPositivo + ", verdadeiroNegativo=" + verdadeiroNegativo + ", falsoPositivo=" + falsoPositivo + ", falsoNegativo=" + falsoNegativo + ", oddsRatio=" + oddsRatio + ", Sen=" + Sen + ", Specy=" + Specy + ", logOddsRatio=" + logOddsRatio + ", variancia=" + variancia + ", erroPadrao=" + erroPadrao + ", intervaloConfiancaSuperior=" + intervaloConfiancaSuperior + ", intervaloConfiancaInferior=" + intervaloConfiancaInferior + ", intervaloConfiancaSuperiorSens=" + intervaloConfiancaSuperiorSens + ", intervaloConfiancaInferiorSens=" + intervaloConfiancaInferiorSens + ", intervaloConfiancaSuperiorSpec=" + intervaloConfiancaSuperiorSpec + ", intervaloConfiancaInferiorSpec=" + intervaloConfiancaInferiorSpec + '}';
     }
     */

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.nome);
        hash = 79 * hash + Objects.hashCode(this.verdadeiroPositivo);
        hash = 79 * hash + Objects.hashCode(this.verdadeiroNegativo);
        hash = 79 * hash + Objects.hashCode(this.falsoPositivo);
        hash = 79 * hash + Objects.hashCode(this.falsoNegativo);
        hash = 79 * hash + Objects.hashCode(this.oddsRatio);
        hash = 79 * hash + Objects.hashCode(this.Sen);
        hash = 79 * hash + Objects.hashCode(this.Specy);
        hash = 79 * hash + Objects.hashCode(this.logOddsRatio);
        hash = 79 * hash + Objects.hashCode(this.variancia);
        hash = 79 * hash + Objects.hashCode(this.erroPadrao);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaSuperior);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaInferior);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaSuperiorSens);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaInferiorSens);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaSuperiorSpec);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaInferiorSpec);
        hash = 79 * hash + Objects.hashCode(this.positiveLikelihoodRatio);
        hash = 79 * hash + Objects.hashCode(this.negativeLikelihoodRatio);
        hash = 79 * hash + Objects.hashCode(this.logPositiveLikelihoodRatio);
        hash = 79 * hash + Objects.hashCode(this.logNegativeLikelihoodRatio);
        hash = 79 * hash + Objects.hashCode(this.varianciaLRPositiva);
        hash = 79 * hash + Objects.hashCode(this.varianciaLRNegativa);
        hash = 79 * hash + Objects.hashCode(this.erroPadraoLRPositiva);
        hash = 79 * hash + Objects.hashCode(this.erroPadraoLRNegativa);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaSuperiorLRPositiva);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaInferiorLRPositiva);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaSuperiorLRNegativa);
        hash = 79 * hash + Objects.hashCode(this.intervaloConfiancaInferiorLRNegativa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estudo other = (Estudo) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.verdadeiroPositivo, other.verdadeiroPositivo)) {
            return false;
        }
        if (!Objects.equals(this.verdadeiroNegativo, other.verdadeiroNegativo)) {
            return false;
        }
        if (!Objects.equals(this.falsoPositivo, other.falsoPositivo)) {
            return false;
        }
        if (!Objects.equals(this.falsoNegativo, other.falsoNegativo)) {
            return false;
        }
        if (!Objects.equals(this.oddsRatio, other.oddsRatio)) {
            return false;
        }
        if (!Objects.equals(this.Sen, other.Sen)) {
            return false;
        }
        if (!Objects.equals(this.Specy, other.Specy)) {
            return false;
        }
        if (!Objects.equals(this.logOddsRatio, other.logOddsRatio)) {
            return false;
        }
        if (!Objects.equals(this.variancia, other.variancia)) {
            return false;
        }
        if (!Objects.equals(this.erroPadrao, other.erroPadrao)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaSuperior, other.intervaloConfiancaSuperior)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaInferior, other.intervaloConfiancaInferior)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaSuperiorSens, other.intervaloConfiancaSuperiorSens)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaInferiorSens, other.intervaloConfiancaInferiorSens)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaSuperiorSpec, other.intervaloConfiancaSuperiorSpec)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaInferiorSpec, other.intervaloConfiancaInferiorSpec)) {
            return false;
        }
        if (!Objects.equals(this.positiveLikelihoodRatio, other.positiveLikelihoodRatio)) {
            return false;
        }
        if (!Objects.equals(this.negativeLikelihoodRatio, other.negativeLikelihoodRatio)) {
            return false;
        }
        if (!Objects.equals(this.logPositiveLikelihoodRatio, other.logPositiveLikelihoodRatio)) {
            return false;
        }
        if (!Objects.equals(this.logNegativeLikelihoodRatio, other.logNegativeLikelihoodRatio)) {
            return false;
        }
        if (!Objects.equals(this.varianciaLRPositiva, other.varianciaLRPositiva)) {
            return false;
        }
        if (!Objects.equals(this.varianciaLRNegativa, other.varianciaLRNegativa)) {
            return false;
        }
        if (!Objects.equals(this.erroPadraoLRPositiva, other.erroPadraoLRPositiva)) {
            return false;
        }
        if (!Objects.equals(this.erroPadraoLRNegativa, other.erroPadraoLRNegativa)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaSuperiorLRPositiva, other.intervaloConfiancaSuperiorLRPositiva)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaInferiorLRPositiva, other.intervaloConfiancaInferiorLRPositiva)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaSuperiorLRNegativa, other.intervaloConfiancaSuperiorLRNegativa)) {
            return false;
        }
        if (!Objects.equals(this.intervaloConfiancaInferiorLRNegativa, other.intervaloConfiancaInferiorLRNegativa)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Estudo{" + "nome=" + nome + ", verdadeiroPositivo=" + verdadeiroPositivo + ", verdadeiroNegativo=" + verdadeiroNegativo + ", falsoPositivo=" + falsoPositivo + ", falsoNegativo=" + falsoNegativo + ", oddsRatio=" + oddsRatio + ", Sen=" + Sen + ", Specy=" + Specy + ", logOddsRatio=" + logOddsRatio + ", variancia=" + variancia + ", erroPadrao=" + erroPadrao + ", intervaloConfiancaSuperior=" + intervaloConfiancaSuperior + ", intervaloConfiancaInferior=" + intervaloConfiancaInferior + ", intervaloConfiancaSuperiorSens=" + intervaloConfiancaSuperiorSens + ", intervaloConfiancaInferiorSens=" + intervaloConfiancaInferiorSens + ", intervaloConfiancaSuperiorSpec=" + intervaloConfiancaSuperiorSpec + ", intervaloConfiancaInferiorSpec=" + intervaloConfiancaInferiorSpec + ", positiveLikelihoodRatio=" + positiveLikelihoodRatio + ", negativeLikelihoodRatio=" + negativeLikelihoodRatio + ", logPositiveLikelihoodRatio=" + logPositiveLikelihoodRatio + ", logNegativeLikelihoodRatio=" + logNegativeLikelihoodRatio + ", varianciaLRPositiva=" + varianciaLRPositiva + ", varianciaLRNegativa=" + varianciaLRNegativa + ", erroPadraoLRPositiva=" + erroPadraoLRPositiva + ", erroPadraoLRNegativa=" + erroPadraoLRNegativa + ", intervaloConfiancaSuperiorLRPositiva=" + intervaloConfiancaSuperiorLRPositiva + ", intervaloConfiancaInferiorLRPositiva=" + intervaloConfiancaInferiorLRPositiva + ", intervaloConfiancaSuperiorLRNegativa=" + intervaloConfiancaSuperiorLRNegativa + ", intervaloConfiancaInferiorLRNegativa=" + intervaloConfiancaInferiorLRNegativa + '}';
    }
}
