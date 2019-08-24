package net.unesc.roma.managedbean.diagnostics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;

/**
 *
 * @author aminathamiguel
 */
public class LikelihoodRatio {

    public static BigDecimal calcularLikelihoodRatioPositiva(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal verdadeiroPositivo = estudoCalculado.getVerdadeiroPositivo();
        BigDecimal falsoNegPlusVerdadeiroPos = estudoCalculado.getFalsoNegativo().add(verdadeiroPositivo);
        BigDecimal verdadeiros = verdadeiroPositivo.divide(falsoNegPlusVerdadeiroPos, escala, arredondamento.getRoundingMode());

        BigDecimal verdadeiroNeg = estudoCalculado.getVerdadeiroNegativo();
        BigDecimal falsoPosPlusVerdadeiroNeg = estudoCalculado.getFalsoPositivo().add(verdadeiroNeg);
        BigDecimal falsos = estudoCalculado.getFalsoPositivo().divide(falsoPosPlusVerdadeiroNeg, escala, arredondamento.getRoundingMode());

        BigDecimal likelihoodRatioPositiva = verdadeiros.divide(falsos, escala, arredondamento.getRoundingMode());
        return likelihoodRatioPositiva;
    }

    public static BigDecimal calcularVeriacaoLRPositiva(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal verdadeiroPosPlusfalsoNeg = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal falsoPosPlusVerdadeiroNeg = estudoCalculado.getFalsoPositivo().add(estudoCalculado.getVerdadeiroNegativo());

        BigDecimal vA = BigDecimal.ONE.divide(estudoCalculado.getVerdadeiroPositivo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vB = BigDecimal.ONE.divide(estudoCalculado.getFalsoPositivo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vC = BigDecimal.ONE.divide(verdadeiroPosPlusfalsoNeg, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vD = BigDecimal.ONE.divide(falsoPosPlusVerdadeiroNeg, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal varianciaPositiva = vA.add(vB).subtract(vC).subtract(vD);
        return varianciaPositiva.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularErroPadraoLRPositiva(Estudo estudo, int escala, Arredondamento arredondamento) {
        BigDecimal variancia = calcularVeriacaoLRPositiva(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadraoPositivo = FormulasMatematicas.sqrt(variancia, escala, arredondamento.getRoundingMode());
        return erroPadraoPositivo;
    }

    public static BigDecimal calcularIntervaloConfiancaSuperiorLRPositiva(Estudo estudo, int escala, Arredondamento arredondamento, ZScore zScore) {
        checkEstudo(estudo);

        BigDecimal likelihoodRatioPositiva = calcularLikelihoodRatioPositiva(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadraoPositivo = calcularErroPadraoLRPositiva(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal intervaloSuperior = zScore.getValue().multiply(erroPadraoPositivo);

        BigDecimal expSuperiorPositivo = FormulasMatematicas.exp(intervaloSuperior, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal intervaloConfiancaSuperiorLRPositiva = likelihoodRatioPositiva.multiply(expSuperiorPositivo);
        return intervaloConfiancaSuperiorLRPositiva.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularIntervaloConfiancaInferiorLRPositiva(Estudo estudo, int escala, Arredondamento arredondamento, ZScore zScore) {
        checkEstudo(estudo);

        BigDecimal likelihoodRatioPositiva = calcularLikelihoodRatioPositiva(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadraoPositivo = calcularErroPadraoLRPositiva(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal intervaloInferior = FormulasMatematicas.LESS.multiply(zScore.getValue().multiply(erroPadraoPositivo));

        BigDecimal expInferiorPositivo = FormulasMatematicas.exp(intervaloInferior, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal intervaloConfiancaInferiorLRPositiva = likelihoodRatioPositiva.multiply(expInferiorPositivo);
        return intervaloConfiancaInferiorLRPositiva.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularLikelihoodRatioNegativa(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal falsoNegativo = estudoCalculado.getFalsoNegativo();
        BigDecimal verdadeiroPos = estudoCalculado.getVerdadeiroPositivo();
        BigDecimal verdadeiroPosPlusfalsoNeg = verdadeiroPos.add(falsoNegativo);
        BigDecimal verdadeiros = falsoNegativo.divide(verdadeiroPosPlusfalsoNeg, escala, arredondamento.getRoundingMode());

        BigDecimal verdadeiroNegativo = estudoCalculado.getVerdadeiroNegativo();
        BigDecimal falsoPos = estudoCalculado.getFalsoPositivo();
        BigDecimal falsoPosPlusVerdadeiroNeg = falsoPos.add(verdadeiroNegativo);
        BigDecimal falsos = verdadeiroNegativo.divide(falsoPosPlusVerdadeiroNeg, escala, arredondamento.getRoundingMode());

        BigDecimal likelihoodRatioNegativa = verdadeiros.divide(falsos, escala, arredondamento.getRoundingMode());
        return likelihoodRatioNegativa;
    }

    public static BigDecimal calcularVeriacaoLRNegativa(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal verdadeiroPosPlusfalsoNeg = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal falsoPosPlusVerdadeiroNeg = estudoCalculado.getFalsoPositivo().add(estudoCalculado.getVerdadeiroNegativo());

        BigDecimal vA = BigDecimal.ONE.divide(estudoCalculado.getFalsoNegativo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vB = BigDecimal.ONE.divide(estudoCalculado.getVerdadeiroNegativo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vC = BigDecimal.ONE.divide(verdadeiroPosPlusfalsoNeg, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vD = BigDecimal.ONE.divide(falsoPosPlusVerdadeiroNeg, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal vAvB = vA.add(vB);
        BigDecimal varianciaNegativa = vAvB.subtract(vC).subtract(vD);
        return varianciaNegativa.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularErroPadraoLRNegativa(Estudo estudo, int escala, Arredondamento arredondamento) {
        BigDecimal variancia = calcularVeriacaoLRNegativa(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadraoNegativo = FormulasMatematicas.sqrt(variancia, escala, arredondamento.getRoundingMode());
        return erroPadraoNegativo;
    }

    public static BigDecimal calcularIntervaloConfiancaSuperiorLRNegativa(Estudo estudo, int escala, Arredondamento arredondamento, ZScore zScore) {
        checkEstudo(estudo);

        BigDecimal likelihoodRatioNegativa = calcularLikelihoodRatioNegativa(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadraoNegativo = calcularErroPadraoLRNegativa(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal intervaloSuperiorNeg = zScore.getValue().multiply(erroPadraoNegativo);

        BigDecimal expSuperiorNegativo = FormulasMatematicas.exp(intervaloSuperiorNeg, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal intervaloConfiancaSuperiorLRNegativa = likelihoodRatioNegativa.multiply(expSuperiorNegativo);
        return intervaloConfiancaSuperiorLRNegativa.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularIntervaloConfiancaInferiorLRNegativa(Estudo estudo, int escala, Arredondamento arredondamento, ZScore zScore) {
        checkEstudo(estudo);

        BigDecimal likelihoodRatioNegativa = calcularLikelihoodRatioNegativa(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadraoPositivo = calcularErroPadraoLRNegativa(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal intervaloInferiorNeg = FormulasMatematicas.LESS.multiply(zScore.getValue().multiply(erroPadraoPositivo));

        BigDecimal expInferiorNegativo = FormulasMatematicas.exp(intervaloInferiorNeg, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal intervaloConfiancaInferiorLRNegativa = likelihoodRatioNegativa.multiply(expInferiorNegativo);
        return intervaloConfiancaInferiorLRNegativa.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularValorDeU(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal falsoPosPlusVerdadeiroNeg = estudoCalculado.getFalsoPositivo().add(estudoCalculado.getVerdadeiroNegativo());
        BigDecimal valoresDeU = estudoCalculado.getVerdadeiroPositivo().multiply(falsoPosPlusVerdadeiroNeg);

        BigDecimal somaU = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo().add(
                estudoCalculado.getFalsoNegativo().add(estudoCalculado.getVerdadeiroNegativo())));
        BigDecimal valorDeU = valoresDeU.divide(somaU, escala, arredondamento.getRoundingMode());
        return valorDeU;
    }

    public static BigDecimal calcularValorTotalDeU(List<EstudoMantelHaenszel> estudos, int escala, LikelihoodRatio.Arredondamento arredondamento) {
        BigDecimal valorTotalDeU = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            valorTotalDeU = valorTotalDeU.add(calcularValorDeU(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento));
        }
        return valorTotalDeU.setScale(escala, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal calcularValorDeUlinha(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal decimalUlinha = estudoCalculado.getFalsoPositivo();
        BigDecimal falsoPplusVerdadeiroN = decimalUlinha.add(estudoCalculado.getVerdadeiroNegativo());
        BigDecimal valorUlinha = decimalUlinha.multiply(falsoPplusVerdadeiroN);

        BigDecimal somaUlinha = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo().add(
                estudoCalculado.getFalsoNegativo().add(estudoCalculado.getVerdadeiroNegativo())));
        BigDecimal valorDeUlinha = valorUlinha.divide(somaUlinha, escala, arredondamento.getRoundingMode());
        return valorDeUlinha;
    }

    public static BigDecimal calcularValorTotalDeUlinha(List<EstudoMantelHaenszel> estudos, int escala, LikelihoodRatio.Arredondamento arredondamento) {
        BigDecimal valorTotalDeUlinha = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            valorTotalDeUlinha = valorTotalDeUlinha.add(calcularValorDeUlinha(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento));
        }
        return valorTotalDeUlinha.setScale(escala, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal calcularValorDeV(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal verdadeiroPosPlusFalsoNeg = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal valoresDeV = estudoCalculado.getFalsoNegativo().multiply(verdadeiroPosPlusFalsoNeg);

        BigDecimal somaV = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo().add(
                estudoCalculado.getFalsoNegativo().add(estudoCalculado.getVerdadeiroNegativo())));
        BigDecimal valorDeV = valoresDeV.divide(somaV, escala, arredondamento.getRoundingMode());
        return valorDeV;
    }

    public static BigDecimal calcularValorTotalDeV(List<EstudoMantelHaenszel> estudos, int escala, LikelihoodRatio.Arredondamento arredondamento) {
        BigDecimal somatorioValorTotalDeV = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            somatorioValorTotalDeV = somatorioValorTotalDeV.add(calcularValorDeV(estudo, escala, arredondamento));
        }
        return somatorioValorTotalDeV.setScale(escala, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal calcularValorDeVlinha(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal decimalVlinha = estudoCalculado.getVerdadeiroNegativo();
        BigDecimal verdadeiroPplusFalsoN = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal valoresVlinha = decimalVlinha.multiply(verdadeiroPplusFalsoN);

        BigDecimal somaVlinha = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo().add(
                estudoCalculado.getFalsoNegativo().add(estudoCalculado.getVerdadeiroNegativo())));
        BigDecimal valorDeVlinha = valoresVlinha.divide(somaVlinha, escala, arredondamento.getRoundingMode());
        return valorDeVlinha;
    }

    public static BigDecimal calcularValorTotalDeVlinha(List<EstudoMantelHaenszel> estudos, int escala, LikelihoodRatio.Arredondamento arredondamento) {
        BigDecimal valorTotalDeUlinha = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            valorTotalDeUlinha = valorTotalDeUlinha.add(calcularValorDeVlinha(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento));
        }
        return valorTotalDeUlinha.setScale(escala, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal calcularValorDeP(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculado = checkEstudo(estudo);

        BigDecimal valorA = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoNegativo());
        BigDecimal valorB = estudoCalculado.getFalsoPositivo().add(estudoCalculado.getVerdadeiroNegativo());
        BigDecimal valorC = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo());
        BigDecimal multipValor = valorA.multiply(valorB).multiply(valorC);

        BigDecimal valorD = estudoCalculado.getVerdadeiroPositivo().multiply(estudoCalculado.getFalsoPositivo());
        BigDecimal somaP = estudoCalculado.getVerdadeiroPositivo().add(estudoCalculado.getFalsoPositivo().add(
                estudoCalculado.getFalsoNegativo().add(estudoCalculado.getVerdadeiroNegativo())));
        BigDecimal multip = valorD.multiply(somaP);

        BigDecimal multipTotal = multipValor.subtract(multip);
        BigDecimal valorDeP = multipTotal.divide(somaP.pow(2), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        return valorDeP;
    }

    public static BigDecimal calcularValorTotalDeP(List<EstudoMantelHaenszel> estudos, int escala, LikelihoodRatio.Arredondamento arredondamento) {
        BigDecimal somatorioValorTotalDeP = BigDecimal.ZERO;
        for (EstudoMantelHaenszel estudo : estudos) {
            somatorioValorTotalDeP = somatorioValorTotalDeP.add(calcularValorDeP(estudo, escala, arredondamento));
        }
        return somatorioValorTotalDeP.setScale(escala, BigDecimal.ROUND_HALF_EVEN);
    }

    public static Estudo checkEstudo(Estudo estudo) throws IllegalArgumentException {
        Estudo copiaEstudo = null;
        try {
            if (estudo.getFalsoNegativo() == null || estudo.getFalsoPositivo() == null
                    || estudo.getVerdadeiroNegativo() == null || estudo.getVerdadeiroPositivo() == null) {
                throw new IllegalArgumentException("O estudo deve está todo preenchido!");
            }
            copiaEstudo = (Estudo) estudo.clone();
            if (BigDecimal.ZERO.equals(estudo.getFalsoNegativo())
                    || BigDecimal.ZERO.equals(estudo.getFalsoPositivo())
                    || BigDecimal.ZERO.equals(estudo.getFalsoNegativo())
                    || BigDecimal.ZERO.equals(estudo.getFalsoNegativo())) {

                copiaEstudo.setFalsoNegativo(copiaEstudo.getFalsoNegativo().add(FormulasMatematicas.HALF));
                copiaEstudo.setFalsoPositivo(copiaEstudo.getFalsoPositivo().add(FormulasMatematicas.HALF));
                copiaEstudo.setVerdadeiroNegativo(copiaEstudo.getVerdadeiroNegativo().add(FormulasMatematicas.HALF));
                copiaEstudo.setVerdadeiroPositivo(copiaEstudo.getVerdadeiroPositivo().add(FormulasMatematicas.HALF));
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(OddsRatio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copiaEstudo;
    }

    public enum Arredondamento {

        /**
         * Esse método de arredondamento irá ignorar as casas decimais após a
         * escala do arredondamento.<br>
         * <br>
         * Exemplo:<br>
         * Considerando uma escala de 2, o número 4,1254 será arredondado para
         * 4,12.
         */
        Truncado("Truncado", "T", RoundingMode.DOWN),
        ArrendondadoBaixo("Arredondado para baixo", "B", RoundingMode.HALF_DOWN),
        ArrendondadoCima("Arredondado para cima", "C", RoundingMode.HALF_UP);
        private final String descricao;
        private final String value;
        private final RoundingMode roundingMode;

        private Arredondamento(String descricao, String value, RoundingMode roundingMode) {
            this.descricao = descricao;
            this.value = value;
            this.roundingMode = roundingMode;
        }

        public String getDescricao() {
            return descricao;
        }

        public RoundingMode getRoundingMode() {
            return roundingMode;
        }

        public String getValue() {
            return value;
        }

        public static Arredondamento get(String value) {
            Objects.requireNonNull(value);
            for (Arredondamento arredondamento : Arredondamento.values()) {
                if (value.equals(arredondamento.getValue())) {
                    return arredondamento;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "Arredondamento{" + "descricao=" + descricao + ", value=" + value + ", roundingMode=" + roundingMode + '}';
        }
    }

    public enum ZScore {

        ic90(new BigDecimal("1.64485362695147"), "90%", "90"),
        ic95(new BigDecimal("1.95996398454005"), "95%", "95"),
        ic98(new BigDecimal("2.32634787404084"), "98%", "98"),
        ic99(new BigDecimal("2.57582930354890"), "99%", "99");

        private BigDecimal value;
        private String descricao;
        private String key;

        private ZScore(BigDecimal value, String descricao, String key) {
            this.value = value;
            this.descricao = descricao;
            this.key = key;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getKey() {
            return key;
        }

        public BigDecimal getValue() {
            return value;
        }

        public static ZScore get(String value) {
            Objects.requireNonNull(value);
            for (ZScore score : ZScore.values()) {
                if (value.equals(score.getKey())) {
                    return score;
                }
            }
            return null;
        }
    }

    private static BigDecimal raizQuadrada(BigDecimal valor) {

        return valor;
    }
}
