/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.odds.ratio.mantelHaenszel;

import java.math.BigDecimal;
import java.util.Objects;
import net.unesc.roma.managedbean.diagnostics.Estudo;

/**
 *
 * @author R3oLoN
 */
public class EstudoMantelHaenszel extends Estudo {

    private BigDecimal peso;
    private BigDecimal percentualPeso;
    private BigDecimal pesoLRPositiva;
    private BigDecimal pesoLRNegativa;
    private BigDecimal percentualPesoLRPositiva;
    private BigDecimal percentualPesoLRNegativa;

    public EstudoMantelHaenszel(String nome, BigDecimal verdadeiroPositivo, BigDecimal falsoPositivo, BigDecimal falsoNegativo, BigDecimal verdadeiroNegativo  ) {
        super(nome, verdadeiroPositivo,  falsoPositivo, falsoNegativo,verdadeiroNegativo);
    }

    public EstudoMantelHaenszel() {
    }

    public BigDecimal getPercentualPeso() {
        return percentualPeso;
    }

    public void setPercentualPeso(BigDecimal percentualPeso) {
        this.percentualPeso = percentualPeso;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getPesoLRPositiva() {
        return pesoLRPositiva;
    }

    public void setPesoLRPositiva(BigDecimal pesoLRPositiva) {
        this.pesoLRPositiva = pesoLRPositiva;
    }

    public BigDecimal getPesoLRNegativa() {
        return pesoLRNegativa;
    }

    public void setPesoLRNegativa(BigDecimal pesoLRNegativa) {
        this.pesoLRNegativa = pesoLRNegativa;
    }

    public BigDecimal getPercentualPesoLRPositiva() {
        return percentualPesoLRPositiva;
    }

    public void setPercentualPesoLRPositiva(BigDecimal percentualPesoLRPositiva) {
        this.percentualPesoLRPositiva = percentualPesoLRPositiva;
    }

    public BigDecimal getPercentualPesoLRNegativa() {
        return percentualPesoLRNegativa;
    }

    public void setPercentualPesoLRNegativa(BigDecimal percentualPesoLRNegativa) {
        this.percentualPesoLRNegativa = percentualPesoLRNegativa;
    }
    
/*
    @Override
    public int hashCode() {

        int hash = 3;
        hash = 67 * hash + super.hashCode();
        hash = 67 * hash + Objects.hashCode(this.peso);
        hash = 67 * hash + Objects.hashCode(this.percentualPeso);
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
        final EstudoMantelHaenszel other = (EstudoMantelHaenszel) obj;

        if (!super.equals(obj)) {
            return false;
        }

        if (!Objects.equals(this.peso, other.peso)) {
            return false;
        }
        if (!Objects.equals(this.percentualPeso, other.percentualPeso)) {
            return false;
        }
        return true;
    }
    */

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.peso);
        hash = 17 * hash + Objects.hashCode(this.percentualPeso);
        hash = 17 * hash + Objects.hashCode(this.pesoLRPositiva);
        hash = 17 * hash + Objects.hashCode(this.pesoLRNegativa);
        hash = 17 * hash + Objects.hashCode(this.percentualPesoLRPositiva);
        hash = 17 * hash + Objects.hashCode(this.percentualPesoLRNegativa);
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
        final EstudoMantelHaenszel other = (EstudoMantelHaenszel) obj;
        if (!Objects.equals(this.peso, other.peso)) {
            return false;
        }
        if (!Objects.equals(this.percentualPeso, other.percentualPeso)) {
            return false;
        }
        if (!Objects.equals(this.pesoLRPositiva, other.pesoLRPositiva)) {
            return false;
        }
        if (!Objects.equals(this.pesoLRNegativa, other.pesoLRNegativa)) {
            return false;
        }
        if (!Objects.equals(this.percentualPesoLRPositiva, other.percentualPesoLRPositiva)) {
            return false;
        }
        if (!Objects.equals(this.percentualPesoLRNegativa, other.percentualPesoLRNegativa)) {
            return false;
        }
        return true;
    }
}
