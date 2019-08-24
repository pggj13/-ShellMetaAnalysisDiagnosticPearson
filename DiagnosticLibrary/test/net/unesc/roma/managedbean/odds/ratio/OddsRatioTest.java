package net.unesc.roma.managedbean.odds.ratio;

import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import net.unesc.roma.managedbean.diagnostics.CriarOddsRatio;
import java.math.BigDecimal;
import net.unesc.roma.managedbean.builder.EstudosBD;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class OddsRatioTest {

    CriarOddsRatio oddsRatio;
    OddsRatio.Arredondamento metodoArredondamento;
    OddsRatio.ZScore zScore;

    public OddsRatioTest() {
    }

    @Before
    public void inicioDeCadaTeste() {
        this.oddsRatio = new CriarOddsRatio();
        this.oddsRatio.init();
        this.metodoArredondamento = OddsRatio.Arredondamento.get(this.oddsRatio.getArredondamento());
        this.zScore = OddsRatio.ZScore.get(this.oddsRatio.getIntervaloConfiaca());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOddsRatioSemEstudo() {
        this.oddsRatio.calcular();
    }

    @Test
    public void testConferirValorOddsRatio() {
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        BigDecimal valorOddsRatio = OddsRatio.calcularOddsRatio(primeiroEstudo(), precisao(), metodoArredondamento);

        assertThat(valorOddsRatio, equalTo(new BigDecimal("277.200")));
    }

    @Test
    public void testConferirValorDoIntervaloDeConfiancaInferior() {
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        EstudoMantelHaenszel estudo = primeiroEstudo();
        BigDecimal valorIntervaloConfianca = OddsRatio.calcularIntervaloConfiancaInferior(estudo, precisao(), metodoArredondamento,zScore);

        assertThat(valorIntervaloConfianca, equalTo(new BigDecimal("30.143")));
    }

    @Test
    public void testConferirValorDoIntervaloDeConfiancaSuperior() {
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        EstudoMantelHaenszel estudo = primeiroEstudo();
        BigDecimal valorIntervaloConfianca = OddsRatio.calcularIntervaloConfiancaSuperior(estudo, precisao(), metodoArredondamento,zScore);
        
        assertThat(valorIntervaloConfianca, equalTo(new BigDecimal("2549.136")));
    }
    
    @Test
    public void testConferirValorDoPesoEfeitoFixo() {
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        BigDecimal valorDoPeso = MantelHaenszel.calcularPeso(primeiroEstudo(), precisao(), metodoArredondamento);

       assertThat(valorDoPeso, equalTo(new BigDecimal("0.042")));
    }
    
    @Test
    public void testSomaOddsRatio() {
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());

        BigDecimal somaOddsRatio = MantelHaenszel.calcularOddsRatioMantelHaenszel(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(somaOddsRatio, equalTo(new BigDecimal("61.475")));
    }
   
    private int precisao(){
        return this.oddsRatio.getPrecisao();
    }
    
    private EstudoMantelHaenszel primeiroEstudo(){
        return this.oddsRatio.getEstudos().get(0);
    }
}
