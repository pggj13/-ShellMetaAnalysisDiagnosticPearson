package net.unesc.roma.managedbean.likelihood.ratio;

import java.math.BigDecimal;
import java.util.List;
import net.unesc.roma.managedbean.diagnostics.Estudo;
import net.unesc.roma.managedbean.diagnostics.LikelihoodRatio;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;

/**
 *
 * @author aminathamiguel
 */
public class LikelihoodRatioPositivaMantelHaenszel {

    private static final Integer PRECISAO_INTERNA = 15;

    public static BigDecimal calcularPesoParaLRPositiva(Estudo estudo, int precisao, LikelihoodRatio.Arredondamento arredondamento) {
        EstudoMantelHaenszel estudoCalculado = (EstudoMantelHaenszel) LikelihoodRatio.checkEstudo(estudo);

        BigDecimal fpositivo = estudoCalculado.getFalsoPositivo();
        BigDecimal verdadeiroPosFalsoNeg = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal multip = fpositivo.multiply(verdadeiroPosFalsoNeg);

        BigDecimal somaTodos = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo()).add(
                estudoCalculado.getFalsoNegativo()).add(estudoCalculado.getVerdadeiroNegativo());
        BigDecimal pesoLRPositiva = multip.divide(somaTodos, precisao, arredondamento.getRoundingMode());

        pesoLRPositiva = pesoLRPositiva.setScale(precisao, BigDecimal.ROUND_HALF_EVEN);
        return pesoLRPositiva;
    }

    public static BigDecimal calcularPesoTotalParaLRPositiva(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal somatorio = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            somatorio = somatorio.add(calcularPesoParaLRPositiva(estudo, PRECISAO_INTERNA, metodoArredondamento));
        }
        somatorio = somatorio.setScale(precisao, metodoArredondamento.getRoundingMode());
        return somatorio;
    }

    public static void calcularPercentualPesoLRPositiva(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal percentualPeso = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            LikelihoodRatio.checkEstudo(estudo);
            estudo.setPercentualPesoLRPositiva(calcularPesoParaLRPositiva(estudo, FormulasMatematicas.SCALE_DEFAULT, metodoArredondamento));
            percentualPeso = percentualPeso.add(estudo.getPercentualPesoLRPositiva());
        }
        for (EstudoMantelHaenszel estudo : estudos) {
            LikelihoodRatio.checkEstudo(estudo);
            BigDecimal percentual = estudo.getPercentualPesoLRPositiva().divide(percentualPeso, FormulasMatematicas.SCALE_DEFAULT, metodoArredondamento.getRoundingMode());
            percentual = FormulasMatematicas.HUNDRED.multiply(percentual);
            estudo.setPercentualPesoLRPositiva(percentual.setScale(2, metodoArredondamento.getRoundingMode()));
        }
    }

    public static BigDecimal calcularPesoLRPositivaDeCadaEstudoIndividual(List<EstudoMantelHaenszel> estudos, Estudo estudo, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal likelihood = LikelihoodRatio.calcularLikelihoodRatioPositiva(estudo, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal pesoParaLRPositiva = calcularPesoParaLRPositiva(estudo, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal pesoDeCadaEstudoIndividual = likelihood.multiply(pesoParaLRPositiva);

        pesoDeCadaEstudoIndividual = pesoDeCadaEstudoIndividual.setScale(precisao, metodoArredondamento.getRoundingMode());
        return pesoDeCadaEstudoIndividual;
    }

    public static BigDecimal calcularPesoTotalDaLRPositivaDeCadaEstudoIndividual(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal somatorio = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            somatorio = somatorio.add(calcularPesoLRPositivaDeCadaEstudoIndividual(estudos, estudo, PRECISAO_INTERNA, metodoArredondamento));
        }
        somatorio = somatorio.setScale(precisao, metodoArredondamento.getRoundingMode());
        return somatorio;
    }

    public static BigDecimal calcularLRPositivaAgrupada(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal pesoTotalDeCadaEstudo = calcularPesoTotalDaLRPositivaDeCadaEstudoIndividual(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal pesoTotalParaLRPositiva = calcularPesoTotalParaLRPositiva(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal lrPositivaAgrupada = pesoTotalDeCadaEstudo.divide(pesoTotalParaLRPositiva, precisao, metodoArredondamento.getRoundingMode());//.divide(pesoTotalParaLRPositiva, PRECISAO_INTERNA, metodoArredondamento.getRoundingMode() );

        lrPositivaAgrupada = lrPositivaAgrupada.setScale(precisao);
        return lrPositivaAgrupada;
    }

    public static BigDecimal calcularErroPadraoAgrupadoLRPositiva(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal somatorioDeP = LikelihoodRatio.calcularValorTotalDeP(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal somatorioDeU = LikelihoodRatio.calcularValorTotalDeU(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal somatorioDeV = LikelihoodRatio.calcularValorTotalDeV(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal valorDeUeV = somatorioDeU.multiply(somatorioDeV);
        BigDecimal divisorErroPadraoAgrupado = somatorioDeP.divide(valorDeUeV, precisao, metodoArredondamento.getRoundingMode());

        BigDecimal erroPadraoAgrupado = FormulasMatematicas.sqrt(divisorErroPadraoAgrupado, precisao, metodoArredondamento.getRoundingMode());
        erroPadraoAgrupado = erroPadraoAgrupado.setScale(precisao, BigDecimal.ROUND_HALF_EVEN);
        return erroPadraoAgrupado;
    }

    public static BigDecimal calcularIntervaloConfiancaSuperiorAgrupadoLRPositiva(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento, LikelihoodRatio.ZScore zScore) {
        BigDecimal valorZScore = zScore.getValue();
        BigDecimal lrAgrupada = calcularLRPositivaAgrupada(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal erroPadraoAgrupado = calcularErroPadraoAgrupadoLRPositiva(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal exponencial = FormulasMatematicas.exp(valorZScore.multiply(new BigDecimal("1").multiply(erroPadraoAgrupado)), PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());

        BigDecimal resultado = lrAgrupada.multiply(exponencial).setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultado;
    }

    public static BigDecimal calcularIntervaloConfiancaInferiorAgrupadoLRPositiva(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento, LikelihoodRatio.ZScore zScore) {
        BigDecimal valorZScore = zScore.getValue();
        BigDecimal lrAgrupada = calcularLRPositivaAgrupada(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal erroPadraoAgrupado = calcularErroPadraoAgrupadoLRPositiva(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal exponencial = FormulasMatematicas.exp(valorZScore.multiply(new BigDecimal("-1").multiply(erroPadraoAgrupado)), PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());

        BigDecimal resultado = lrAgrupada.multiply(exponencial).setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultado;
    }
}
