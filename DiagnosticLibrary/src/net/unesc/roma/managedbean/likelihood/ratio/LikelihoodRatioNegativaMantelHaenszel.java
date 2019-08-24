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
public class LikelihoodRatioNegativaMantelHaenszel {

    private static final Integer PRECISAO_INTERNA = 15;

    public static BigDecimal calcularPesoParaLRNegativa(Estudo estudo, int precisao, LikelihoodRatio.Arredondamento arredondamento) {
        EstudoMantelHaenszel estudoCalculado = (EstudoMantelHaenszel) LikelihoodRatio.checkEstudo(estudo);

        BigDecimal vnegativo = estudoCalculado.getVerdadeiroNegativo();
        BigDecimal verdadeiroPosFalsoNeg = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal multip = vnegativo.multiply(verdadeiroPosFalsoNeg);

        BigDecimal somaTodos = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo()).add(
                estudoCalculado.getFalsoNegativo()).add(estudoCalculado.getVerdadeiroNegativo());
        BigDecimal pesoLRNegativa = multip.divide(somaTodos, precisao, arredondamento.getRoundingMode());

        pesoLRNegativa = pesoLRNegativa.setScale(precisao, BigDecimal.ROUND_HALF_EVEN);
        return pesoLRNegativa;
    }

    public static BigDecimal calcularPesoTotalParaLRNegativa(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal somatorioLRNegativa = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            somatorioLRNegativa = somatorioLRNegativa.add(calcularPesoParaLRNegativa(estudo, PRECISAO_INTERNA, metodoArredondamento));
        }
        somatorioLRNegativa = somatorioLRNegativa.setScale(precisao, metodoArredondamento.getRoundingMode());
        return somatorioLRNegativa;
    }

    public static void calcularPercentualPesoLRNegativa(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal percentualPesoLRNegativa = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            LikelihoodRatio.checkEstudo(estudo);
            estudo.setPercentualPesoLRNegativa(calcularPesoParaLRNegativa(estudo, FormulasMatematicas.SCALE_DEFAULT, metodoArredondamento));
            percentualPesoLRNegativa = percentualPesoLRNegativa.add(estudo.getPercentualPesoLRNegativa());
        }
        for (EstudoMantelHaenszel estudo : estudos) {
            LikelihoodRatio.checkEstudo(estudo);
            BigDecimal percentualLRNegativa = estudo.getPercentualPesoLRNegativa().divide(percentualPesoLRNegativa, FormulasMatematicas.SCALE_DEFAULT, metodoArredondamento.getRoundingMode());
            percentualLRNegativa = FormulasMatematicas.HUNDRED.multiply(percentualLRNegativa);
            estudo.setPercentualPesoLRNegativa(percentualLRNegativa.setScale(2, metodoArredondamento.getRoundingMode()));
        }
    }

    public static BigDecimal calcularPesoLRNegativaDeCadaEstudoIndividual(List<EstudoMantelHaenszel> estudos, Estudo estudo, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal likelihood = LikelihoodRatio.calcularLikelihoodRatioNegativa(estudo, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal pesoParaLRNegativa = calcularPesoParaLRNegativa(estudo, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal pesoDeCadaEstudoInd = likelihood.multiply(pesoParaLRNegativa);

        pesoDeCadaEstudoInd = pesoDeCadaEstudoInd.setScale(precisao, metodoArredondamento.getRoundingMode());
        return pesoDeCadaEstudoInd;
    }

    public static BigDecimal calcularPesoTotalDaLRNegativaDeCadaEstudoIndividual(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal lrNegativaDeCadaEstudo = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            lrNegativaDeCadaEstudo = lrNegativaDeCadaEstudo.add(calcularPesoLRNegativaDeCadaEstudoIndividual(estudos, estudo, PRECISAO_INTERNA, metodoArredondamento));
        }
        lrNegativaDeCadaEstudo = lrNegativaDeCadaEstudo.setScale(precisao, metodoArredondamento.getRoundingMode());
        return lrNegativaDeCadaEstudo;
    }

    public static BigDecimal calcularLRNegativaAgrupada(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal pesoTotalDeCadaEstudo = calcularPesoTotalDaLRNegativaDeCadaEstudoIndividual(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal pesoTotalParaLRNegativa = calcularPesoTotalParaLRNegativa(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal lrNegativaAgrupada = pesoTotalDeCadaEstudo.divide(pesoTotalParaLRNegativa, precisao, metodoArredondamento.getRoundingMode());//.divide(pesoTotalParaLRPositiva, PRECISAO_INTERNA, metodoArredondamento.getRoundingMode() );

        lrNegativaAgrupada = lrNegativaAgrupada.setScale(precisao);
        return lrNegativaAgrupada;
    }

    public static BigDecimal calcularErroPadraoAgrupadoLRNegativa(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento) {
        BigDecimal somatorioDeP = LikelihoodRatio.calcularValorTotalDeP(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal somatorioDeUlinha = LikelihoodRatio.calcularValorTotalDeUlinha(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal somatorioDeVlinha = LikelihoodRatio.calcularValorTotalDeVlinha(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal valorDeUeV = somatorioDeUlinha.multiply(somatorioDeVlinha);
        BigDecimal divisorErroPadraoAgrupado = somatorioDeP.divide(valorDeUeV, precisao, metodoArredondamento.getRoundingMode());

        BigDecimal erroPadraoAgrupado = FormulasMatematicas.sqrt(divisorErroPadraoAgrupado, precisao, metodoArredondamento.getRoundingMode());
        erroPadraoAgrupado = erroPadraoAgrupado.setScale(precisao, BigDecimal.ROUND_HALF_EVEN);
        return erroPadraoAgrupado;
    }

    public static BigDecimal calcularIntervaloConfiancaSuperiorAgrupadoLRNegativa(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento, LikelihoodRatio.ZScore zScore) {
        BigDecimal valorZScore = zScore.getValue();
        BigDecimal lrNegativaAgrupada = calcularLRNegativaAgrupada(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal erroPadraoAgrupadoLRNegativa = calcularErroPadraoAgrupadoLRNegativa(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal exponencialICsuperior = FormulasMatematicas.exp(valorZScore.multiply(new BigDecimal("1").multiply(erroPadraoAgrupadoLRNegativa)), PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());

        BigDecimal resultadoSuperior = lrNegativaAgrupada.multiply(exponencialICsuperior).setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultadoSuperior;
    }

    public static BigDecimal calcularIntervaloConfiancaInferiorAgrupadoLRNegativa(List<EstudoMantelHaenszel> estudos, int precisao, LikelihoodRatio.Arredondamento metodoArredondamento, LikelihoodRatio.ZScore zScore) {
        BigDecimal valorZScore = zScore.getValue();
        BigDecimal lrNegativaAgrupada = calcularLRNegativaAgrupada(estudos, PRECISAO_INTERNA, metodoArredondamento);
        BigDecimal erroPadraoAgrupado = calcularErroPadraoAgrupadoLRNegativa(estudos, PRECISAO_INTERNA, metodoArredondamento);

        BigDecimal exponencialICinferior = FormulasMatematicas.exp(valorZScore.multiply(new BigDecimal("-1").multiply(erroPadraoAgrupado)), PRECISAO_INTERNA, metodoArredondamento.getRoundingMode());

        BigDecimal resultadoInferior = lrNegativaAgrupada.multiply(exponencialICinferior).setScale(precisao, metodoArredondamento.getRoundingMode());
        return resultadoInferior;
    }
}
