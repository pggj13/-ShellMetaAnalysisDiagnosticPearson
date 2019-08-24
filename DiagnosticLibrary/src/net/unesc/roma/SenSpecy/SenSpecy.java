/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.SenSpecy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import net.unesc.roma.managedbean.diagnostics.Estudo;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import static net.unesc.roma.managedbean.diagnostics.OddsRatio.checkEstudo;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;

/**
 *
 * @author Silvestre
 */
public class SenSpecy {

    /**
     * Método utilizado para calculo da sensibilidade  <i>Sensibilidade</i> do
     * Estudo.<br>
     * <br>
     * Sensi = <sup>TP</sup>/<sub>TP+FN</sub><br>
     *
     * @param estudo estudo que será realizado o cálculo.
     * @param escala escala para arredondamento.
     * @param arredondamento tipo do arredondamento.
     * @return valor do <i>Odds Ratio</i> do estudo.
     */
    public static BigDecimal CalcularSensibilidade(Estudo estudo, int escala, OddsRatio.Arredondamento arredondamento) {
        
        Estudo estudoCalculo = checkEstudo(estudo);
        System.out.println(estudoCalculo);
        BigDecimal sens = (estudoCalculo.getVerdadeiroPositivo());
        BigDecimal psoma = (estudoCalculo.getVerdadeiroPositivo()).add(estudoCalculo.getFalsoNegativo());
        BigDecimal sensdados = sens.divide(psoma, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return sensdados.setScale(escala, arredondamento.getRoundingMode());
    }

    /**
     * Método utilizado para calulo da Especificidade <i>Specificidade</i> do
     * Estudo.<br>
     * <br>
     * Spec = <sup>TN</sup>/<sub>TN+FP</sub><br>
     *
     * @param estudo estudo que será realizado o cálculo.
     * @param escala escala para arredondamento.
     * @param arredondamento tipo do arredondamento.
     * @return valor do <i>Odds Ratio</i> do estudo.
     */
    public static BigDecimal CalcularSpecificidade(Estudo estudo, int escala, OddsRatio.Arredondamento arredondamento) {
        
        Estudo estudoCalculo = checkEstudo(estudo);
        
        BigDecimal espec = (estudoCalculo.getVerdadeiroNegativo());
        BigDecimal espsoma = (estudoCalculo.getVerdadeiroNegativo()).add(estudoCalculo.getFalsoPositivo());
        
        BigDecimal especdados = espec.divide(espsoma, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return especdados.setScale(escala, arredondamento.getRoundingMode());
    }

    /**
     * Intervalo de Confiança <i> do Estudo.<br>
     *
     * @param estudo estudo que será realizado o cálculo.
     * @param escala escala para arredondamento.
     * @param arredondamento tipo do arredondamento.
     * @return valor do log do <i>Odds Ratio</i> do estudo.
     */
    public static BigDecimal calcularIntervaloConfiancaSuperiorSen(Estudo estudo, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        Estudo estudoCalculo = checkEstudo(estudo);
        
        BigDecimal Francao2 = ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(estudoCalculo.getVerdadeiroPositivo().add(BigDecimal.ONE)), BigDecimal.valueOf(2).multiply(estudoCalculo.getVerdadeiroPositivo().add(estudoCalculo.getFalsoNegativo().subtract(estudoCalculo.getVerdadeiroPositivo()))));
        BigDecimal IntervalSuperior = (estudoCalculo.getVerdadeiroPositivo().add(BigDecimal.ONE).multiply(Francao2)).divide(estudoCalculo.getVerdadeiroPositivo().add(estudoCalculo.getFalsoNegativo()).subtract(estudoCalculo.getVerdadeiroPositivo()).add(estudoCalculo.getVerdadeiroPositivo().add(BigDecimal.ONE).multiply(Francao2)), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return IntervalSuperior.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularIntervaloConfiancaInferiorSen(Estudo estudo, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        Estudo estudoCalculo = checkEstudo(estudo);
        
        BigDecimal denominador = estudoCalculo.getVerdadeiroPositivo().add((estudoCalculo.getVerdadeiroPositivo().add(estudoCalculo.getFalsoNegativo()).subtract(estudoCalculo.getVerdadeiroPositivo()).add(BigDecimal.ONE)).multiply(ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(((estudoCalculo.getVerdadeiroPositivo().add(estudoCalculo.getFalsoNegativo()).subtract(estudo.getVerdadeiroPositivo()).add(BigDecimal.ONE)))), BigDecimal.valueOf(2).multiply(estudoCalculo.getVerdadeiroPositivo()))));
        BigDecimal IntervalInferior = estudoCalculo.getVerdadeiroPositivo().divide(denominador, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return IntervalInferior.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularIntervaloConfiancaSuperiorSpec(Estudo estudo, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        Estudo estudoCalculo = checkEstudo(estudo);
        
        BigDecimal Francao4 = ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(estudoCalculo.getVerdadeiroNegativo().add(BigDecimal.ONE)), BigDecimal.valueOf(2).multiply(estudoCalculo.getVerdadeiroNegativo().add(estudoCalculo.getFalsoPositivo().subtract(estudoCalculo.getVerdadeiroNegativo()))));
        BigDecimal IntervalSpecSuperior = estudoCalculo.getVerdadeiroNegativo().add(BigDecimal.ONE).multiply(Francao4).divide(estudoCalculo.getVerdadeiroNegativo().add(estudoCalculo.getFalsoPositivo().subtract(estudoCalculo.getVerdadeiroNegativo()).add(estudoCalculo.getVerdadeiroNegativo().add(BigDecimal.ONE).multiply(Francao4))), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return IntervalSpecSuperior.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularIntervaloConfiancaInferiorSpecy(Estudo estudo, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        Estudo estudoCalculo = checkEstudo(estudo);
        
        BigDecimal Francao3 = ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(estudoCalculo.getFalsoPositivo().add((estudoCalculo.getVerdadeiroNegativo().subtract(estudoCalculo.getVerdadeiroNegativo()).add(BigDecimal.ONE)))), BigDecimal.valueOf(2).multiply(estudoCalculo.getVerdadeiroNegativo()));
        BigDecimal denominador = estudoCalculo.getVerdadeiroNegativo().add(estudoCalculo.getVerdadeiroNegativo().add(estudoCalculo.getFalsoPositivo()).subtract(estudoCalculo.getVerdadeiroNegativo()).add(BigDecimal.ONE).multiply(Francao3));
        BigDecimal IntervalSpecInferior = estudo.getVerdadeiroNegativo().divide(denominador, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return IntervalSpecInferior.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularSensAgrupada(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {
        BigDecimal SomaTP = BigDecimal.ZERO;
        BigDecimal SomaTPFN = BigDecimal.ZERO;
        
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            
            SomaTP = SomaTP.add(estudoCalculo.getVerdadeiroPositivo());
            
            SomaTPFN = SomaTPFN.add(estudoCalculo.getVerdadeiroPositivo().add(estudoCalculo.getFalsoNegativo()));
        }
        
        BigDecimal SomatorioSensibilidade = SomaTP.divide(SomaTPFN, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return SomatorioSensibilidade.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularSpecyAgrupada(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {
        BigDecimal SomaTN = BigDecimal.ZERO;
        BigDecimal SomaTNFP = BigDecimal.ZERO;
        
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            
            SomaTN = SomaTN.add(estudoCalculo.getVerdadeiroNegativo());
            //     System.out.println("GAGO"+SomaTN);
            SomaTNFP = SomaTNFP.add(estudoCalculo.getVerdadeiroNegativo().add(estudoCalculo.getFalsoPositivo()));
            //    System.out.println("GAGO2"+SomaTNFP);   
        }
        
        BigDecimal SomatorioEspecificidade = SomaTN.divide(SomaTNFP, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return SomatorioEspecificidade.setScale(escala, arredondamento.getRoundingMode());
    }

//Começa aqui o intervalo agrupado
    public static BigDecimal calcularIntervaloConfiancaAgrupadoInferiorSen(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        BigDecimal SomaFN = BigDecimal.ZERO;
        BigDecimal SomaTP = BigDecimal.ZERO;
        
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            
            SomaFN = SomaFN.add(estudoCalculo.getFalsoNegativo());
            
            SomaTP = SomaTP.add(estudoCalculo.getVerdadeiroPositivo());
        }
        BigDecimal IntervalodeConfianca = SomaTP.add((SomaTP.add(SomaFN).subtract(SomaTP).add(BigDecimal.ONE)).multiply(ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(((SomaTP.add(SomaFN).subtract(SomaTP).add(BigDecimal.ONE)))), BigDecimal.valueOf(2).multiply(SomaTP))));
        
        BigDecimal Interval = SomaTP.divide(IntervalodeConfianca, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return Interval.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularIntervaloConfiancaAgrupadoSuperiorSen(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        BigDecimal SomaFN = BigDecimal.ZERO;
        BigDecimal SomaTP = BigDecimal.ZERO;
        
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            
            SomaFN = SomaFN.add(estudoCalculo.getFalsoNegativo());
            
            SomaTP = SomaTP.add(estudoCalculo.getVerdadeiroPositivo());
        }
        
        BigDecimal IntervaloConfianca = ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(SomaTP.add(BigDecimal.ONE)), BigDecimal.valueOf(2).multiply(SomaTP.add(SomaFN.subtract(SomaTP))));
        
        BigDecimal Intervalo = (SomaTP.add(BigDecimal.ONE).multiply(IntervaloConfianca)).divide(SomaTP.add(SomaFN).subtract(SomaTP).add(SomaTP.add(BigDecimal.ONE).multiply(IntervaloConfianca)), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return Intervalo.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularIntervaloConfiancaAgrupadoInferiorSpecy(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        
        BigDecimal SomaTN = BigDecimal.ZERO;
        BigDecimal SomaFP = BigDecimal.ZERO;
        
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            
            SomaTN = SomaTN.add(estudoCalculo.getVerdadeiroNegativo());
            //  System.out.println("GAGO"+SomaFN);
            SomaFP = SomaFP.add(estudoCalculo.getFalsoPositivo());
            //  System.out.println("GAGO"+SomaFN) ;

        }
        BigDecimal Interval = ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(SomaFP.add((SomaTN.subtract(SomaTN).add(BigDecimal.ONE)))), BigDecimal.valueOf(2).multiply(SomaTN));
        BigDecimal termo1 = SomaTN.add(SomaTN.add(SomaFP).subtract(SomaTN).add(BigDecimal.ONE).multiply(Interval));
        // System.out.println("SPecificidade low " +estudo.getVerdadeiroNegativo().divide(Sp, 3, RoundingMode.HALF_UP));

        BigDecimal termo2 = SomaTN.divide(termo1, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return termo2.setScale(escala, arredondamento.getRoundingMode());
    }
    
    public static BigDecimal calcularIntervaloConfiancaAgrupadoSuperiorSpecy(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        BigDecimal SomaTN = BigDecimal.ZERO;
        BigDecimal SomaFP = BigDecimal.ZERO;
        
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            
            SomaTN = SomaTN.add(estudoCalculo.getVerdadeiroNegativo());
            
            SomaFP = SomaFP.add(estudoCalculo.getFalsoPositivo());
        }
        
        BigDecimal IntervaloConfianca = ClooperInterv.fInverse(new BigDecimal(0.05 / 2), new BigDecimal(2).multiply(SomaTN.add(BigDecimal.ONE)), BigDecimal.valueOf(2).multiply(SomaTN.add(SomaFP.subtract(SomaTN))));
        
        BigDecimal Intervalo = (SomaTN.add(BigDecimal.ONE).multiply(IntervaloConfianca)).divide(SomaTN.add(SomaFP).subtract(SomaTN).add(SomaTN.add(BigDecimal.ONE).multiply(IntervaloConfianca)), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        
        return Intervalo.setScale(escala, arredondamento.getRoundingMode());
    }  
}
