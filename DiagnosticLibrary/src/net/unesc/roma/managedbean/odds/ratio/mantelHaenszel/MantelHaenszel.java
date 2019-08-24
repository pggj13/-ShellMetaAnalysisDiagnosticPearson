/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.odds.ratio.mantelHaenszel;

import java.math.BigDecimal;
import java.util.List;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import static net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird.calcularOddsRatioAgrupado;

/**
 *
 * @author R3oLoN
 */
public class MantelHaenszel {

    public static BigDecimal calcularPeso(EstudoMantelHaenszel estudo, int escala, OddsRatio.Arredondamento arredondamento) {

        EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);

        BigDecimal pFalsos = estudoCalculo.getFalsoNegativo().multiply(estudoCalculo.getFalsoPositivo());
        BigDecimal somaTodos = estudoCalculo.getFalsoNegativo().add(estudoCalculo.getFalsoPositivo());
        somaTodos = somaTodos.add(estudoCalculo.getVerdadeiroNegativo());
        somaTodos = somaTodos.add(estudoCalculo.getVerdadeiroPositivo());

        return pFalsos.divide(somaTodos, escala, arredondamento.getRoundingMode());
    }

    public static void calcularPercentualPeso(List<EstudoMantelHaenszel> estudos, OddsRatio.Arredondamento arredondamento) {
        BigDecimal totalPeso = BigDecimal.ZERO;

        for (EstudoMantelHaenszel estudo : estudos) {
            OddsRatio.checkEstudo(estudo);
            estudo.setPercentualPeso(calcularPeso(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento));
            totalPeso = totalPeso.add(estudo.getPercentualPeso());
        }
        for (EstudoMantelHaenszel estudo : estudos) {
            OddsRatio.checkEstudo(estudo);
            BigDecimal tempPercente = estudo.getPercentualPeso().divide(totalPeso, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
            tempPercente = FormulasMatematicas.HUNDRED.multiply(tempPercente);
            estudo.setPercentualPeso(tempPercente.setScale(2, arredondamento.getRoundingMode()));
        }
    }

    public static BigDecimal calcularOddsRatioMantelHaenszel(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {
        BigDecimal somaPesosOdds = BigDecimal.ZERO;
        BigDecimal somaPesos = BigDecimal.ZERO;

        for (EstudoMantelHaenszel estudo : estudos) {
            OddsRatio.checkEstudo(estudo);
            BigDecimal odds = OddsRatio.calcularOddsRatio(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

            BigDecimal peso = calcularPeso(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

            somaPesosOdds = somaPesosOdds.add(odds.multiply(peso));
            somaPesos = somaPesos.add(peso);
        }
        return somaPesosOdds.divide(somaPesos, escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularLogOddsRatioMantelHaenszel(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        return FormulasMatematicas.log(calcularOddsRatioMantelHaenszel(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento), escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularVariacao(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal R = BigDecimal.ZERO;
        BigDecimal S = BigDecimal.ZERO;
        BigDecimal E = BigDecimal.ZERO;
        BigDecimal F = BigDecimal.ZERO;
        BigDecimal G = BigDecimal.ZERO;
        BigDecimal H = BigDecimal.ZERO;

        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);

            R = R.add(estudoCalculo.getVerdadeiroNegativo().multiply(
                    estudoCalculo.getVerdadeiroPositivo()).divide(estudoCalculo.getTotal(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));
            S = S.add(estudoCalculo.getFalsoNegativo().multiply(
                    estudoCalculo.getFalsoPositivo()).divide(estudoCalculo.getTotal(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));
            BigDecimal quadradoTotal = estudoCalculo.getTotal().pow(2);

            E = E.add(estudoCalculo.getVerdadeiroNegativo().multiply(
                    estudoCalculo.getVerdadeiroPositivo().multiply(
                            estudoCalculo.getVerdadeiroNegativo().add(
                                    estudoCalculo.getVerdadeiroPositivo())))
                    .divide(quadradoTotal, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));
            F = F.add(estudoCalculo.getFalsoNegativo().multiply(
                    estudoCalculo.getFalsoPositivo().multiply(
                            estudoCalculo.getVerdadeiroNegativo().add(
                                    estudoCalculo.getVerdadeiroPositivo())))
                    .divide(quadradoTotal, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));
            G = G.add(estudoCalculo.getVerdadeiroNegativo().multiply(
                    estudoCalculo.getVerdadeiroPositivo().multiply(
                            estudoCalculo.getFalsoNegativo().add(
                                    estudoCalculo.getFalsoPositivo())))
                    .divide(quadradoTotal, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));
            H = H.add(estudoCalculo.getFalsoNegativo().multiply(
                    estudoCalculo.getFalsoPositivo().multiply(
                            estudoCalculo.getFalsoNegativo().add(
                                    estudoCalculo.getFalsoPositivo())))
                    .divide(quadradoTotal, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));

        }

        BigDecimal T1 = E.divide(R.pow(2), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal T2 = F.add(G).divide(R.multiply(S), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal T3 = H.divide(S.pow(2), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal varianca = T1.add(T2.add(T3)).divide(FormulasMatematicas.TWO, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        return varianca.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularIntervaloConfiancaSuperior(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
       
                /*BigDecimal oddsRatioAgrupado = calcularOddsRatioMantelHaenszel(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
                System.out.println(oddsRatioAgrupado);*/

        BigDecimal variacao = calcularVariacao(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
        BigDecimal LogORmh = calcularLogOddsRatioMantelHaenszel(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
        BigDecimal expoente = LogORmh.add(zScore.getValue().multiply(FormulasMatematicas.sqrt(variacao, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()))).setScale(FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        return FormulasMatematicas.exp(expoente, escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularIntervaloConfiancaInferior(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {
        BigDecimal variacao = calcularVariacao(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
        BigDecimal LogORmh = calcularLogOddsRatioMantelHaenszel(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
        BigDecimal expoente = LogORmh.subtract(zScore.getValue().multiply(FormulasMatematicas.sqrt(variacao, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()))).setScale(FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        return FormulasMatematicas.exp(expoente, escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularChiSquared(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {
        BigDecimal chiSquared = BigDecimal.ZERO;
        BigDecimal oddsRatioMh = calcularLogOddsRatioMantelHaenszel(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
        for (EstudoMantelHaenszel estudo : estudos) {
            EstudoMantelHaenszel estudoCalculo = (EstudoMantelHaenszel) OddsRatio.checkEstudo(estudo);
            BigDecimal variacao = OddsRatio.calcularVariacao(estudoCalculo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
            BigDecimal oddsRatio = OddsRatio.calcularLogOddsRatio(estudoCalculo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

            BigDecimal auxCalculo = oddsRatio.subtract(oddsRatioMh);
            auxCalculo = FormulasMatematicas.pow(auxCalculo, FormulasMatematicas.TWO, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
            auxCalculo = auxCalculo.multiply(BigDecimal.ONE.divide(variacao, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode()));

            chiSquared = chiSquared.add(auxCalculo);
        }
        return chiSquared.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularISquared(List<EstudoMantelHaenszel> estudos, OddsRatio.Arredondamento arredondamento) {
        BigDecimal chiSquared = calcularChiSquared(estudos, FormulasMatematicas.SCALE_DEFAULT, arredondamento);
        BigDecimal numEstudos = new BigDecimal(estudos.size()).subtract(BigDecimal.ONE);

        if (!(chiSquared.compareTo(numEstudos) >= 1)) {
            return BigDecimal.ZERO;
        }

        BigDecimal iSquared = chiSquared.subtract(numEstudos);
        iSquared = iSquared.divide(chiSquared, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        iSquared = iSquared.multiply(FormulasMatematicas.HUNDRED);

        return iSquared.setScale(2, arredondamento.getRoundingMode());
    }
}
