package net.unesc.roma.managedbean.diagnostics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.unesc.roma.managedbean.likelihood.ratio.LikelihoodRatioPositivaMantelHaenszel.calcularLRPositivaAgrupada;
import static net.unesc.roma.managedbean.likelihood.ratio.LikelihoodRatioPositivaMantelHaenszel.calcularPesoTotalDaLRPositivaDeCadaEstudoIndividual;
import net.unesc.roma.managedbean.math.FormulasMatematicas;
import static net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird.calcularOddsRatioAgrupado;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import static net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel.calcularOddsRatioMantelHaenszel;

/**
 * Classe que contém os métodos realcionados ao calculo do <i>Odds Ratio</i>.
 * <br>
 * <br>
 * Todos os cálculos são realizados com escala FormulasMatematicas.SCALE_DEFAULT
 * = 20, apenas sendo arredondado no momento em que o valor é retornado pelo
 * método.
 *
 * @author Leandro de Oliveira Reolon
 */
public class OddsRatio {

    private static final Integer PRECISAO_INTERNA = 15;

    /**
     * Método utilizado para calular o <i>Odds Ratio</i> do Estudo.<br>
     * <br>
     * OR = <sup>a*d</sup>/<sub>b*c</sub><br>
     *
     * @param estudo estudo que será realizado o cálculo.
     * @param escala escala para arredondamento.
     * @param arredondamento tipo do arredondamento.
     * @return valor do <i>Odds Ratio</i> do estudo.
     */
    public static BigDecimal calcularOddsRatio(Estudo estudo, int escala, Arredondamento arredondamento) {

        Estudo estudoCalculo = checkEstudo(estudo);

        BigDecimal verdadeiros = estudoCalculo.getVerdadeiroPositivo().multiply(
                estudoCalculo.getVerdadeiroNegativo());

        BigDecimal falsos = estudoCalculo.getFalsoPositivo().multiply(
                estudoCalculo.getFalsoNegativo());

        BigDecimal oddsRatio = verdadeiros.divide(falsos, escala, arredondamento.getRoundingMode());

        return oddsRatio;

    }

    /**
     * Método utilizado para calular o log do <i>Odds Ratio</i> do Estudo.<br>
     * <br>
     * LogOR = ln(OR)<br>
     *
     * @param estudo estudo que será realizado o cálculo.
     * @param escala escala para arredondamento.
     * @param arredondamento tipo do arredondamento.
     * @return valor do log do <i>Odds Ratio</i> do estudo.
     */
    public static BigDecimal calcularLogOddsRatio(Estudo estudo, int escala, Arredondamento arredondamento) {

        BigDecimal oddsRatio = calcularOddsRatio(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal logOddsRatio = FormulasMatematicas.log(oddsRatio, escala, arredondamento.getRoundingMode());

        return logOddsRatio;
    }

    /**
     * Método utilizado para calular a variação do Log do <i>Odds Ratio</i> do
     * Estudo.<br>
     * <br>
     * Var<sub>LogOR</sub> = <sup>1</sup>/<sub>a</sub> +
     * <sup>1</sup>/<sub>b</sub> + <sup>1</sup>/<sub>c</sub> +
     * <sup>1</sup>/<sub>d</sub><br>
     *
     * @param estudo estudo que será realizado o cálculo.
     * @param escala escala para arredondamento.
     * @param arredondamento tipo do arredondamento.
     * @return valor do log do <i>Odds Ratio</i> do estudo.
     */
    public static BigDecimal calcularVariacao(Estudo estudo, int escala, Arredondamento arredondamento) {
        Estudo estudoCalculo = checkEstudo(estudo);

        BigDecimal vA = BigDecimal.ONE.divide(estudoCalculo.getVerdadeiroPositivo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vB = BigDecimal.ONE.divide(estudoCalculo.getFalsoPositivo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vC = BigDecimal.ONE.divide(estudoCalculo.getFalsoNegativo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());
        BigDecimal vD = BigDecimal.ONE.divide(estudoCalculo.getVerdadeiroNegativo(), FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal variancia = vA.add(vB.add(vC.add(vD)));
        return variancia.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularErroPadrao(Estudo estudo, int escala, Arredondamento arredondamento) {

        BigDecimal variancia = calcularVariacao(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadrao = FormulasMatematicas.sqrt(variancia, escala, arredondamento.getRoundingMode());
        return erroPadrao;
    }

    public static BigDecimal calcularIntervaloConfiancaSuperior(Estudo estudo, int escala, Arredondamento arredondamento, ZScore zScore) {
        checkEstudo(estudo);

        BigDecimal oddsRatio = calcularOddsRatio(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadrao = calcularErroPadrao(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal inerSuperior = zScore.getValue().multiply(
                erroPadrao);

        BigDecimal powConfiancaSuperiro = FormulasMatematicas.exp(inerSuperior, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal intervaloConfiancaSuperiro = oddsRatio.multiply(
                powConfiancaSuperiro);

        return intervaloConfiancaSuperiro.setScale(escala, arredondamento.getRoundingMode());
    }

    public static BigDecimal calcularIntervaloConfiancaInferior(Estudo estudo, int escala, Arredondamento arredondamento, ZScore zScore) {

        checkEstudo(estudo);

        BigDecimal oddsRatio = calcularOddsRatio(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal erroPadrao = calcularErroPadrao(estudo, FormulasMatematicas.SCALE_DEFAULT, arredondamento);

        BigDecimal inerInferior = FormulasMatematicas.LESS.multiply(zScore.getValue().multiply(
                erroPadrao));
        BigDecimal powConfiancaInferior = FormulasMatematicas.exp(inerInferior, FormulasMatematicas.SCALE_DEFAULT, arredondamento.getRoundingMode());

        BigDecimal intervaloConfiancaInferior = oddsRatio.multiply(
                powConfiancaInferior);

        return intervaloConfiancaInferior.setScale(escala, arredondamento.getRoundingMode());

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
        ArrendondadoCima("Arredondado para cima", "C", RoundingMode.HALF_DOWN);
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
