package net.unesc.roma.managedbean.odds.ratio.dersimonianLaird;

import java.math.BigDecimal;
import java.util.Objects;
import net.unesc.roma.managedbean.diagnostics.Estudo;

public class EstudoDersimonianAndLaird extends Estudo {

    private BigDecimal peso;
    private BigDecimal percentualPeso;
    public EstudoDersimonianAndLaird(String nome, BigDecimal verdadeiroPositivo, BigDecimal falsoPositivo, BigDecimal falsoNegativo, BigDecimal verdadeiroNegativo  ) {
        super(nome, verdadeiroPositivo,  falsoPositivo, falsoNegativo,verdadeiroNegativo);
    }

    public EstudoDersimonianAndLaird() {
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EstudoDersimonianAndLaird other = (EstudoDersimonianAndLaird) obj;
        if (!Objects.equals(this.peso, other.peso)) {
            return false;
        }
        if (!Objects.equals(this.percentualPeso, other.percentualPeso)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EstudoDersimonianAndLaird{" + "peso=" + peso + ", percentualPeso=" + percentualPeso + '}';
    }
    
}
