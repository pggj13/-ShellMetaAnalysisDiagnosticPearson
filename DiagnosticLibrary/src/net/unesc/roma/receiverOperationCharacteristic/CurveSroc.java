/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.receiverOperationCharacteristic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import net.unesc.roma.managedbean.diagnostics.LikelihoodRatio;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import static net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird.calcularOddsRatioAgrupado;
import static net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird.calcularTotalPesoOriginalDersimonian;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import static net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel.calcularLogOddsRatioMantelHaenszel;
import static net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel.calcularOddsRatioMantelHaenszel;
import static net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel.calcularVariacao;

/**
 *
 * @author Paulo João
 */
public class CurveSroc {

    private static final Integer PRECISAO_INTERNA = 15;

    /*======= CALCULOS DE AUC DE EFEITO FIXO ===========*/
    public static BigDecimal calcularAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal dorEF = calcularOddsRatioMantelHaenszel(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal A1 = dorEF.pow(2).subtract(dorEF).divide(dorEF.pow(2).subtract(new BigDecimal("2").multiply(dorEF)).add(new BigDecimal("1")), escala, arredondamento.getRoundingMode());

        BigDecimal A2 = dorEF.pow(3).subtract(new BigDecimal("2").multiply(dorEF.pow(2))).add(dorEF).divide(dorEF.pow(4).subtract(new BigDecimal("4").multiply(dorEF.pow(3))).add(new BigDecimal("6").multiply(dorEF.pow(2))).subtract(new BigDecimal("4").multiply(dorEF)).add(new BigDecimal("1")), PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal LN = dorEF.subtract(new BigDecimal("1")).subtract(dorEF);
        if (LN.compareTo(new BigDecimal("0")) < 1) {
            LN = LN.multiply(new BigDecimal("-1"));
        }

        LN = FormulasMatematicas.log(LN, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal B1 = A1.add(A2.multiply(LN));

        BigDecimal B2 = A2.multiply(FormulasMatematicas.log(dorEF, PRECISAO_INTERNA, arredondamento.getRoundingMode()));

        BigDecimal resultado = B1.subtract(B2);

        return resultado.setScale(escala, arredondamento.getRoundingMode());
    }

    /* ======= ERRO PADRÃO DE AUC FIXO ========== */
    public static BigDecimal calcularErroPadraoAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal variacao = calcularVariacao(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal RaizVariacao = FormulasMatematicas.sqrt(variacao, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal dorEF = calcularOddsRatioMantelHaenszel(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal A1 = dorEF.divide(dorEF.subtract(new BigDecimal("1")).pow(3), PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal A2 = dorEF.add(new BigDecimal("1")).multiply(FormulasMatematicas.log(dorEF, PRECISAO_INTERNA, arredondamento.getRoundingMode())).subtract(new BigDecimal("2").multiply(dorEF.subtract(new BigDecimal("1"))));

        BigDecimal R1 = A1.multiply(A2);
        BigDecimal resultado = R1.multiply(RaizVariacao);
        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*=========== INDICE Q AUC EFEITO FIXO ===========*/
    public static BigDecimal calcularIndiceQEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal dorEF = calcularOddsRatioMantelHaenszel(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal resultado = FormulasMatematicas.sqrt(dorEF, PRECISAO_INTERNA, arredondamento.getRoundingMode()).divide(new BigDecimal("1").add(FormulasMatematicas.sqrt(dorEF, PRECISAO_INTERNA, arredondamento.getRoundingMode())), PRECISAO_INTERNA, arredondamento.getRoundingMode());
        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }
    /*==========INTERVALO DE CONFIANÇA NEGATIVO AUC EFEITO FIXO ===============*/

    public static BigDecimal calcularIntervaloConfiancaNegativoAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal seAuc = calcularErroPadraoAucEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal aucEF = calcularAucEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = aucEF.subtract(valorZScore.multiply(seAuc));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }
    /*==========INTERVALO DE CONFIANÇA POSITIVO AUC EFEITO FIXO ===============*/

    public static BigDecimal calcularIntervaloConfiancaPositivoAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal seAuc = calcularErroPadraoAucEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal aucEF = calcularAucEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = aucEF.add(valorZScore.multiply(seAuc));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*==== INTERVALO DE CONFIANÇA NEGATIVO INDICE Q AUC EFEITO FIXO====*/
    public static BigDecimal calcularIntervaloConfiancaNegativoIndiceQAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal erroPadraoQ = calcularErroPadraoIndiceQAucEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal indiceQ = calcularIndiceQEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = indiceQ.subtract(valorZScore.multiply(erroPadraoQ));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }
    /*==== INTERVALO DE CONFIANÇA POSITIVO INDICE Q AUC EFEITO FIXO====*/

    public static BigDecimal calcularIntervaloConfiancaPositivoIndiceQAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal erroPadraoQ = calcularErroPadraoIndiceQAucEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal indiceQ = calcularIndiceQEfeitoFixo(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = indiceQ.add(valorZScore.multiply(erroPadraoQ));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }
    /*======== ERRO PADRÃO DO INDICE Q  EFEITO FIXO ============*/

    public static BigDecimal calcularErroPadraoIndiceQAucEfeitoFixo(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal variacao = calcularVariacao(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal RaizVariacao = FormulasMatematicas.sqrt(variacao, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal dorEF = calcularOddsRatioMantelHaenszel(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal T1 = FormulasMatematicas.sqrt(dorEF, PRECISAO_INTERNA, arredondamento.getRoundingMode());
        BigDecimal T2 = new BigDecimal("2").multiply(new BigDecimal("1").add(FormulasMatematicas.sqrt(dorEF, PRECISAO_INTERNA, arredondamento.getRoundingMode())).pow(2));

        BigDecimal resultado = T1.divide(T2, escala, arredondamento.getRoundingMode()).multiply(RaizVariacao);
        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*
     ==========================================================================
     ===========  COMEÇOU AQUI TODOS OS CALCULOS DA AUC  ======================
     ===========             EFEITO RANDOMICO            ======================
     ==========================================================================
    
     */
    /*======= CALCULOS DE AUC DE EFEITO RANDOMICO ===========*/
    public static BigDecimal calcularAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal dorER = calcularOddsRatioAgrupado(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal A1 = dorER.pow(2).subtract(dorER).divide(dorER.pow(2).subtract(new BigDecimal("2").multiply(dorER)).add(new BigDecimal("1")), PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal A2 = dorER.pow(3).subtract(new BigDecimal("2").multiply(dorER.pow(2))).add(dorER).divide(dorER.pow(4).subtract(new BigDecimal("4").multiply(dorER.pow(3))).add(new BigDecimal("6").multiply(dorER.pow(2))).subtract(new BigDecimal("4").multiply(dorER)).add(new BigDecimal("1")), PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal LN = dorER.subtract(new BigDecimal("1")).subtract(dorER);
        if (LN.compareTo(new BigDecimal("0")) < 1) {
            LN = LN.multiply(new BigDecimal("-1"));
        }

        LN = FormulasMatematicas.log(LN, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal B1 = A1.add(A2.multiply(LN));

        BigDecimal B2 = A2.multiply(FormulasMatematicas.log(dorER, PRECISAO_INTERNA, arredondamento.getRoundingMode()));

        BigDecimal resultado = B1.subtract(B2);

        return resultado.setScale(escala, arredondamento.getRoundingMode());
    }

    /* ======= ERRO PADRÃO DE AUC RANDOMICO ========== */
    public static BigDecimal calcularErroPadraoAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal valorZScore = zScore.getValue();

        BigDecimal oddsRatio = calcularTotalPesoOriginalDersimonian(estudos, PRECISAO_INTERNA, arredondamento);
        oddsRatio = FormulasMatematicas.sqrt(oddsRatio, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        oddsRatio = new BigDecimal("1").divide(oddsRatio, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal dorER = calcularOddsRatioAgrupado(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal A1 = dorER.divide(dorER.subtract(new BigDecimal("1")).pow(3), PRECISAO_INTERNA, arredondamento.getRoundingMode());
        BigDecimal A2 = dorER.add(new BigDecimal("1")).multiply(FormulasMatematicas.log(dorER, PRECISAO_INTERNA, arredondamento.getRoundingMode())).subtract(new BigDecimal("2").multiply(dorER.subtract(new BigDecimal("1"))));

        BigDecimal R1 = A1.multiply(A2);
        BigDecimal resultado = R1.multiply(oddsRatio);

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*INTERVALO DE CONFIANÇA NEGATIVO DE EFEITO RANDOMICO*/
    public static BigDecimal calcularIntervaloConfiancaNegativoAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal seAuc = calcularErroPadraoAucEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento, zScore);
        BigDecimal aucER = calcularAucEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = aucER.subtract(valorZScore.multiply(seAuc));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*INTERVALO DE CONFIANÇA POSITIVO DE EFEITO RANDOMICO*/
    public static BigDecimal calcularIntervaloConfiancaPositivoAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal seAuc = calcularErroPadraoAucEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento, zScore);
        BigDecimal aucER = calcularAucEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = aucER.add(valorZScore.multiply(seAuc));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }
    /*=========== INDICE Q AUC EFEITO RANDOMICO ===========*/

    public static BigDecimal calcularIndiceQEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal dorER = calcularOddsRatioAgrupado(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal resultado = FormulasMatematicas.sqrt(dorER, PRECISAO_INTERNA, arredondamento.getRoundingMode()).divide(new BigDecimal("1").add(FormulasMatematicas.sqrt(dorER, PRECISAO_INTERNA, arredondamento.getRoundingMode())), PRECISAO_INTERNA, arredondamento.getRoundingMode());
        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*========= ERRO PADRÃO DO INDICE Q DE EFEITO RANDOMICO ===========*/
    public static BigDecimal calcularErroPadraoIndiceQAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento) {

        BigDecimal dorER = calcularOddsRatioAgrupado(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal oddsRatio = calcularTotalPesoOriginalDersimonian(estudos, PRECISAO_INTERNA, arredondamento);
        oddsRatio = FormulasMatematicas.sqrt(oddsRatio, PRECISAO_INTERNA, arredondamento.getRoundingMode());
        oddsRatio = new BigDecimal("1").divide(oddsRatio, PRECISAO_INTERNA, arredondamento.getRoundingMode());

        BigDecimal T1 = FormulasMatematicas.sqrt(dorER, PRECISAO_INTERNA, arredondamento.getRoundingMode());
        BigDecimal T2 = new BigDecimal("2").multiply(new BigDecimal("1").add(FormulasMatematicas.sqrt(dorER, PRECISAO_INTERNA, arredondamento.getRoundingMode())).pow(2));

        BigDecimal resultado = T1.divide(T2, escala, arredondamento.getRoundingMode()).multiply(oddsRatio);

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }

    /*==== INTERVALO DE CONFIANÇA NEGATIVO INDICE Q AUC EFEITO RANDOMICO====*/
    public static BigDecimal calcularIntervaloConfiancaNegativoIndiceQAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal erroPadraoQ = calcularErroPadraoIndiceQAucEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal indiceQ = calcularIndiceQEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = indiceQ.subtract(valorZScore.multiply(erroPadraoQ));

        return resultado.setScale(escala, arredondamento.getRoundingMode());

    }
    /*==== INTERVALO DE CONFIANÇA POSITIVO INDICE Q AUC EFEITO RANDOMICO====*/

    public static BigDecimal calcularIntervaloConfiancaPositivoIndiceQAucEfeitoRandomico(List<EstudoMantelHaenszel> estudos, int escala, OddsRatio.Arredondamento arredondamento, OddsRatio.ZScore zScore) {

        BigDecimal erroPadraoQ = calcularErroPadraoIndiceQAucEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento);
        BigDecimal indiceQ = calcularIndiceQEfeitoRandomico(estudos, PRECISAO_INTERNA, arredondamento);

        BigDecimal valorZScore = zScore.getValue();
        BigDecimal resultado = indiceQ.add(valorZScore.multiply(erroPadraoQ));

        return resultado.setScale(escala, arredondamento.getRoundingMode());
    }
}
