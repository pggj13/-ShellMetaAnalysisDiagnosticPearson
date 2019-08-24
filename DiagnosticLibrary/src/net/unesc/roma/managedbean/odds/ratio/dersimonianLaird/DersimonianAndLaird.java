package net.unesc.roma.managedbean.odds.ratio.dersimonianLaird;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import net.unesc.roma.managedbean.diagnostics.Estudo;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import static net.unesc.roma.managedbean.diagnostics.OddsRatio.checkEstudo;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel;
import static net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel.calcularOddsRatioMantelHaenszel;

public class DersimonianAndLaird {
    private static final Integer PRECISAO_INTERNA = 15;
    
    public static BigDecimal calcularRaizDoLogOddsRatio(EstudoMantelHaenszel estudo, Integer precisao, OddsRatio.Arredondamento metodoArredondamento) {
        Estudo estudoCalculo = checkEstudo(estudo);
        
        BigDecimal verdPositivo = new BigDecimal("1").divide(estudo.getVerdadeiroPositivo(), precisao, metodoArredondamento.getRoundingMode());
        BigDecimal verdNegativo = new BigDecimal("1").divide(estudo.getVerdadeiroNegativo(), precisao, metodoArredondamento.getRoundingMode());
        BigDecimal falsoPositivo = new BigDecimal("1").divide(estudo.getFalsoPositivo(), precisao, metodoArredondamento.getRoundingMode());
        BigDecimal falsoNegativo = new BigDecimal("1").divide(estudo.getFalsoNegativo(), precisao, metodoArredondamento.getRoundingMode());
        BigDecimal resultado = FormulasMatematicas.sqrt(verdPositivo.add(verdNegativo).add(falsoPositivo).add(falsoNegativo), precisao,metodoArredondamento.getRoundingMode());
        return resultado;
    }

    public static BigDecimal calcularPesoGenerico(EstudoMantelHaenszel estudo, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal DistribuicaoLogOddsRatio = calcularRaizDoLogOddsRatio(estudo, PRECISAO_INTERNA, metodoArredondamento);
        DistribuicaoLogOddsRatio = DistribuicaoLogOddsRatio.multiply(DistribuicaoLogOddsRatio);
        BigDecimal peso = new BigDecimal("1").divide(DistribuicaoLogOddsRatio, precisao, metodoArredondamento.getRoundingMode());
        return peso;
    }

    public static BigDecimal calcularPesoGenericoAoQuadrado(EstudoMantelHaenszel estudo, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal peso = calcularPesoGenerico(estudo, PRECISAO_INTERNA, metodoArredondamento);
        peso = peso.multiply(peso).setScale(precisao, metodoArredondamento.getRoundingMode());
        return peso;
    }

    public static BigDecimal calcularTauAoQuadrado(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal resultado;
        BigDecimal quiQuadrado = MantelHaenszel.calcularChiSquared(estudos, 10, metodoArredondamento);
        BigDecimal grauDeLiberdade = DersimonianAndLaird.calcularGrauDeLiberdade(estudos);
        if (quiQuadrado.compareTo(grauDeLiberdade) == 1) {

            BigDecimal totalPeso = calcularTotalDoPesoGenerico(estudos, 10, metodoArredondamento);
            BigDecimal totalPesoAoQuadrado = calcularTotalDoPesoGenericoAoQuadrado(estudos, 10, metodoArredondamento);

            BigDecimal divisor = (totalPeso.subtract(totalPesoAoQuadrado.divide(totalPeso, 10, metodoArredondamento.getRoundingMode())));

            resultado = quiQuadrado.subtract(grauDeLiberdade).divide(divisor, PRECISAO_INTERNA, BigDecimal.ROUND_HALF_EVEN).setScale(precisao, metodoArredondamento.getRoundingMode());
        } else {
            resultado = new BigDecimal(BigInteger.ZERO);
        }
        return resultado;
    }

    public static BigDecimal calcularGrauDeLiberdade(List<EstudoMantelHaenszel> estudos) {
        Integer grauDeLiberdade = estudos.size() - 1;
        return new BigDecimal(grauDeLiberdade.toString());
    }

    public static BigDecimal calcularTotalDoPesoGenerico(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal totalizador = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            totalizador = totalizador.add(calcularPesoGenerico(estudo, precisao, metodoArredondamento));
        }
        return totalizador;
    }

    public static BigDecimal calcularTotalDoPesoGenericoAoQuadrado(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal totalizador = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            totalizador = totalizador.add(calcularPesoGenericoAoQuadrado(estudo, PRECISAO_INTERNA, metodoArredondamento));
        }
        totalizador = totalizador.setScale(precisao, BigDecimal.ROUND_HALF_EVEN);
        return totalizador;
    }

    //Falta aqui
    public static BigDecimal calcularPesoParaOddsRatioDersimonian(List<EstudoMantelHaenszel> estudos, EstudoMantelHaenszel estudo, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal pesoGenerico;
        BigDecimal logaritmoNaturalOddsRatio;

        BigDecimal tauAoQuadrado = calcularTauAoQuadrado(estudos, 12, metodoArredondamento);
        pesoGenerico = calcularPesoGenerico(estudo, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal raizQuadradaLogOddsRatio = calcularRaizDoLogOddsRatio(estudo, PRECISAO_INTERNA, metodoArredondamento);
        logaritmoNaturalOddsRatio = OddsRatio.calcularLogOddsRatio(estudo, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal resultado = calcularPesoOriginalDersimonian(estudos, estudo, PRECISAO_INTERNA, metodoArredondamento)
                .multiply(logaritmoNaturalOddsRatio);

        resultado = resultado.setScale(precisao, BigDecimal.ROUND_HALF_EVEN);
        return resultado;
    }

    public static BigDecimal calcularPesoTotalParaOddsRatioDersimonian(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal totalizador = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            totalizador = totalizador.add(calcularPesoParaOddsRatioDersimonian(estudos, estudo, PRECISAO_INTERNA, metodoArredondamento));
        }
        totalizador = totalizador.setScale(precisao, metodoArredondamento.getRoundingMode());
        return totalizador;
    }

    //falta aqui
    public static BigDecimal calcularPesoOriginalDersimonian(List<EstudoMantelHaenszel> estudos, EstudoMantelHaenszel estudo, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal pesoGenerico;
        BigDecimal logaritmoNaturalOddsRatio;

        BigDecimal tauAoQuadrado = calcularTauAoQuadrado(estudos, 12, metodoArredondamento);
        pesoGenerico = calcularPesoGenerico(estudo, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal raizQuadradaLogOddsRatio = calcularRaizDoLogOddsRatio(estudo, PRECISAO_INTERNA, metodoArredondamento);
        logaritmoNaturalOddsRatio = OddsRatio.calcularLogOddsRatio(estudo, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal resultado = new BigDecimal("1")
                .divide(raizQuadradaLogOddsRatio
                        .multiply(raizQuadradaLogOddsRatio)
                        .add(tauAoQuadrado), PRECISAO_INTERNA, BigDecimal.ROUND_HALF_EVEN);

        resultado = resultado.setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultado;
    }

    public static BigDecimal calcularTotalPesoOriginalDersimonian(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal totalizador = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            totalizador = totalizador.add(calcularPesoOriginalDersimonian(estudos, estudo, precisao, metodoArredondamento));
        }
        totalizador = totalizador.setScale(precisao,metodoArredondamento.getRoundingMode());
        return totalizador;
    }

    //falta
    public static BigDecimal calcularPercentualPesoOriginalDersimonian(List<EstudoMantelHaenszel> estudos, EstudoMantelHaenszel primeiroEstudo, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal pesoDoEstudo = calcularPesoOriginalDersimonian(estudos, primeiroEstudo, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal totalPeso = calcularTotalPesoOriginalDersimonian(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal resultado = pesoDoEstudo.divide(totalPeso, PRECISAO_INTERNA, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal("100"));

        return resultado.setScale(precisao, metodoArredondamento.getRoundingMode());
    }

    public static BigDecimal calcularOddsRatioAgrupado(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento) {
        BigDecimal pesoTotalOddsRatio = calcularPesoTotalParaOddsRatioDersimonian(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal pesoTotalOriginal = calcularTotalPesoOriginalDersimonian(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal divisor = pesoTotalOddsRatio.divide(pesoTotalOriginal,PRECISAO_INTERNA,metodoArredondamento.getRoundingMode());
        BigDecimal resultado= FormulasMatematicas.exp(divisor,precisao,metodoArredondamento.getRoundingMode());
        
        return resultado;
    }

    public static BigDecimal calcularOddsRatioAgrupadoInferior(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento, OddsRatio.ZScore zScore) {
        BigDecimal valorZScore = zScore.getValue();
        BigDecimal oddsRatioAgrupado = calcularOddsRatioAgrupado(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal oddsRatio = calcularTotalPesoOriginalDersimonian(estudos, PRECISAO_INTERNA, metodoArredondamento);
        oddsRatio = FormulasMatematicas.sqrt(oddsRatio, PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());
        BigDecimal exponencial = FormulasMatematicas.exp(valorZScore.
                multiply(new BigDecimal("-1").
                        divide(oddsRatio,PRECISAO_INTERNA,metodoArredondamento.getRoundingMode())), 
                        PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());
        
        BigDecimal resultado = oddsRatioAgrupado.multiply(exponencial).setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultado;
    }

    public static BigDecimal calcularOddsRatioAgrupadoSuperior(List<EstudoMantelHaenszel> estudos, int precisao, OddsRatio.Arredondamento metodoArredondamento, OddsRatio.ZScore zScore) {
                BigDecimal valorZScore = zScore.getValue();
      // BigDecimal oddsRatioAgrupado = calcularOddsRatioAgrupado(estudos, PRECISAO_INTERNA, metodoArredondamento);
        
         BigDecimal oddsRatioAgrupado = calcularOddsRatioMantelHaenszel(estudos, FormulasMatematicas.SCALE_DEFAULT, metodoArredondamento);
        BigDecimal oddsRatio = calcularTotalPesoOriginalDersimonian(estudos, PRECISAO_INTERNA, metodoArredondamento);
        oddsRatio = FormulasMatematicas.sqrt(oddsRatio, PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());
        BigDecimal exponencial = FormulasMatematicas.exp(valorZScore.
                multiply(new BigDecimal("1").
                        divide(oddsRatio,PRECISAO_INTERNA,metodoArredondamento.getRoundingMode())), 
                        PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());
        
        BigDecimal resultado = oddsRatioAgrupado.multiply(exponencial).setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultado;
    }

}
