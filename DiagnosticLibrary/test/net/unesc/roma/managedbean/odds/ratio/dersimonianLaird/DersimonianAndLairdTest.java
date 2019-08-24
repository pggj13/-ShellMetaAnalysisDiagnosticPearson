/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.odds.ratio.dersimonianLaird;

import net.unesc.roma.managedbean.builder.EstudosBD;
import java.math.BigDecimal;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import net.unesc.roma.managedbean.diagnostics.CriarOddsRatio;

/**
 *
 * @author ederm
 */
public class DersimonianAndLairdTest {
    
    public DersimonianAndLairdTest() {
    }
    
    CriarOddsRatio oddsRatio;
    OddsRatio.Arredondamento metodoArredondamento;
    OddsRatio.ZScore zScore;

    @Before
    public void inicioDeCadaTeste() {
        this.oddsRatio = new CriarOddsRatio();
        this.oddsRatio.init();
        this.metodoArredondamento = OddsRatio.Arredondamento.get(this.oddsRatio.getArredondamento());
        this.zScore = OddsRatio.ZScore.get(this.oddsRatio.getIntervaloConfiaca());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOddsRatioSemEstudo() {
        this.oddsRatio.calcularDersimmonianAndLaird();
    }
    @Test
    public void testZScore() {
        assertThat(zScore.getValue(),equalTo(new BigDecimal("1.95996398454005")));
    }
    
    @Test
    public void testRaizLogaritmoOddsRatio(){
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        BigDecimal logOddsRatio = DersimonianAndLaird.calcularRaizDoLogOddsRatio(primeiroEstudo(), precisao(), metodoArredondamento);
        assertThat(logOddsRatio, equalTo(new BigDecimal("1.132")));
    }
    
    @Test
    public void testPesoGenericoPorEstudo(){
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        BigDecimal peso = DersimonianAndLaird.calcularPesoGenerico(primeiroEstudo(), precisao(), metodoArredondamento);
        
        assertThat(peso, equalTo(new BigDecimal("0.780")));
    }
    
    @Test
    public void testPesoGenericoPorEstudoAoQuadrado(){
        this.oddsRatio.setEstudos(new EstudosBD().addUmEstudo());
        BigDecimal peso = DersimonianAndLaird.calcularPesoGenericoAoQuadrado(primeiroEstudo(), precisao(), metodoArredondamento);
        
        assertThat(peso, equalTo(new BigDecimal("0.609")));
    }

    @Test
    public void testTotalDoPesoGenericoDeTodosEstudos(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularTotalDoPesoGenerico(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("9.977")));
    }
    
    @Test
    public void testTotalDoPesoGenericoAoQuadradoDeTodosEstudos(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularTotalDoPesoGenericoAoQuadrado(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("39.915")));
    }
     
    @Test
    public void testTauAoQuadrado(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularTauAoQuadrado(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("0.323")));
    }
    
    @Test
    public void testPesoParaOddsRatioDersimonian(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularPesoParaOddsRatioDersimonian(this.oddsRatio.getEstudos(),primeiroEstudo(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("3.506")));
    }
    
    @Test
    public void testPesoTotalParaOddsRatioDersimonian(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularPesoTotalParaOddsRatioDersimonian(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("19.596")));
    }
    
    @Test
    public void testPesoOriginalDersimonian(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularPesoOriginalDersimonian(this.oddsRatio.getEstudos(),primeiroEstudo(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("0.623")));
    }
    
    @Test
    public void testTotalPesoOriginalDersimonian(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularTotalPesoOriginalDersimonian(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("4.633")));
    }
    
    @Test
    public void testPercentualPesoOriginalDersimonian(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularPercentualPesoOriginalDersimonian(this.oddsRatio.getEstudos(),primeiroEstudo(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("13.454")));
    }
    
    @Test
    public void testOddsRatioAgrupado(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularOddsRatioAgrupado(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento);
        assertThat(peso, equalTo(new BigDecimal("68.693")));
    }
    
    @Test
    public void testOddsRatioAgrupadoInferior(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularOddsRatioAgrupadoInferior(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento,zScore);
        assertThat(peso, equalTo(new BigDecimal("27.634")));
    }
    
    @Test
    public void testOddsRatioAgrupadoSuperior(){
        this.oddsRatio.setEstudos(new EstudosBD().addEstudos());
        BigDecimal peso = DersimonianAndLaird.calcularOddsRatioAgrupadoSuperior(this.oddsRatio.getEstudos(), precisao(), metodoArredondamento,zScore);
        assertThat(peso, equalTo(new BigDecimal("170.754")));
    }

    private int precisao(){
        return this.oddsRatio.getPrecisao();
    }
    
    private EstudoMantelHaenszel primeiroEstudo(){
        return this.oddsRatio.getEstudos().get(0);
    }
}
