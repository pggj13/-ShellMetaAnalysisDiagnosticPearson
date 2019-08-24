/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.unesc.roma.ClienteWS.ClienteWS;
import net.unesc.roma.ClienteWS.Dados.data;
import net.unesc.roma.ClienteWS.Dados.dataMetaD3;
import net.unesc.roma.ClienteWS.Dados.effect;
import net.unesc.roma.ClienteWS.Dados.plotConfig;
import net.unesc.roma.SenSpecy.SenSpecy;
import net.unesc.roma.managedbean.diagnostics.LikelihoodRatio;
import net.unesc.roma.managedbean.diagnostics.OddsRatio;
import net.unesc.roma.managedbean.likelihood.ratio.LikelihoodRatioNegativaMantelHaenszel;
import net.unesc.roma.managedbean.likelihood.ratio.LikelihoodRatioPositivaMantelHaenszel;
import net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird;
import static net.unesc.roma.managedbean.odds.ratio.dersimonianLaird.DersimonianAndLaird.calcularOddsRatioAgrupado;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel;
import static net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.MantelHaenszel.calcularOddsRatioMantelHaenszel;
import net.unesc.roma.managedbean.valide.ValidacaoDeEstudo;
import net.unesc.roma.receiverOperationCharacteristic.CurveSroc;

@Named(value = "diagnosticTest")
@ViewScoped
public class MantelHenszelMB implements Serializable {

    private Integer precisao;
    private Integer maxp;
    private String typeEfeito;
    private String arredondamento;
    private String intervaloConfiaca;
    private List<EstudoMantelHaenszel> estudosDigitado;
    private List<EstudoMantelHaenszel> estudosCalculados;
    private List<EstudoMantelHaenszel> estudosValidos;
    private BigDecimal oddsRatioMantelHaenszel;

    //EFEITO FIXO
    private BigDecimal aucEfeitoFixo;
    private BigDecimal erroPadraoAucEfeitoFixo;
    private BigDecimal intervaloConfiancaPositivoAucEfeitoFixo;
    private BigDecimal intervaloConfiancaNegativoAucEfeitoFixo;
    private BigDecimal indiceQEfeitoFixo;
    private BigDecimal erroPadraoIndiceQAucEfeitoFixo;
    private BigDecimal intervaloConfiancaNegativoIndiceQEfeitoFixo;
    private BigDecimal intervaloConfiancaPositivoIndiceQEfeitoFixo;//FIM

    //EFEITO RANDOMICO
    private BigDecimal aucEfeitoRandomico;
    private BigDecimal erroPadraoAucEfeitoRandomico;
    private BigDecimal intervaloConfiancaNegativoAucEfeitoRandomico;
    private BigDecimal intervaloConfiancaPositivoAucEfeitoRandomico;
    private BigDecimal indiceQEfeitoRandomico;
    private BigDecimal erroPadraoIndiceQAucEfeitoRandomico;
    private BigDecimal intervaloConfiancaNegativoIndiceQEfeitoRandomico;
    private BigDecimal intervaloConfiancaPositivoIndiceQEfeitoRandomico;
    //FIM

    private BigDecimal SenMantelHaenszel;
    private BigDecimal SpecMantelHaenszel;
    private BigDecimal mhLimiteSuperior;
    private BigDecimal mhLimiteInferior;
    private BigDecimal mhLimiteSuperiorSen;
    private BigDecimal mhLimiteInferiorSen;
    private BigDecimal mhLimiteSuperiorSpec;
    private BigDecimal mhLimiteInferiorSpec;
    private BigDecimal chiSquared;
    private BigDecimal iSquared;
    private BigDecimal tauQuadrado;
    private BigDecimal likelihoodRatioPositiva;
    private BigDecimal likelihoodRatioNegativa;
    private BigDecimal intervaloConfiancaSuperiorPositivo;
    private BigDecimal intervaloConfiancaInferiorPositivo;
    private BigDecimal intervaloConfiancaSuperiorNegativo;
    private BigDecimal intervaloConfiancaInferiorNegativo;
    private BigDecimal chiSquaredLikelihood;
    private BigDecimal iSquaredLikelihood;
    private BigDecimal erroPadraoLRPositiva;
    private BigDecimal erroPadraoLRNegativa;
    private String testeDiagnostico;
    private String visivelDersimonian;
    private String visivelSensibEspec;
    private String visivelMantelHenzel;
    private String visivelLikelihoodRatioPositivaNegativa;
    private String visivelBtnPlot;
    private String visivelPlot_1;
    private String visivelPlot_2;
    private int numeroDeEstudos;
    DiagnosticoPearson pearsonDiagnostico;
    private String imgPlot;
    private String imgPlot_2;
    private boolean isBasicPlot;
    private String imgPlotRoc;
    private String visivelPlotRoc;

    /**
     * Creates a new instance of DiagnosticOddsRatio
     */
    public MantelHenszelMB() {
    }

    @PostConstruct
    public void init() {
        arredondamento = "B";
        intervaloConfiaca = "95";
        typeEfeito = "EF";
        testeDiagnostico = "likelihoodRatioPositivaNegativa";
        numeroDeEstudos = 0;
        precisao = 4;
        maxp = 300;
        estudosDigitado = new ArrayList<>();
        estudosCalculados = new ArrayList<>();
        estudosValidos = new ArrayList<>();
        estudosDigitado.add(new EstudoMantelHaenszel());

        pearsonDiagnostico = new DiagnosticoPearson();
        setaVisualizacaoDeTabela(false, false, false, false);
        setaPlotVisible(false, false, false, false);
    }

    public String getTesteDiagnostico() {
        return testeDiagnostico;
    }

    public void setTesteDiagnostico(String testeDiagnostico) {
        this.testeDiagnostico = testeDiagnostico;
    }

    public String getVisivelDersimonian() {
        return visivelDersimonian;
    }

    public String getVisivelSensibEspec() {
        return visivelSensibEspec;
    }

    public String getVisivelMantelHenzel() {
        return visivelMantelHenzel;
    }

    public String getVisivelLikelihoodRatioPositivaNegativa() {
        return visivelLikelihoodRatioPositivaNegativa;
    }

    public String getVisivelBtnPlot() {
        return visivelBtnPlot;
    }

    public String getVisivelPlot_1() {
        return visivelPlot_1;
    }

    public String getVisivelPlot_2() {
        return visivelPlot_2;
    }

    public String getVisivelPlotRoc() {
        return visivelPlotRoc;
    }

    public void setaVisualizacaoDeTabela(boolean mantelHenzel, boolean sensibilidadeEspecificidade, boolean dersimonian, boolean likelihoodRatioPositivaNegativa) {
        setVisivelDersimonian(dersimonian);
        setVisivelSensibEspec(sensibilidadeEspecificidade);
        setVisivelMantelHenzel(mantelHenzel);
        setVisivelLikelihoodRatioPositivaNegativa(likelihoodRatioPositivaNegativa);
    }

    public void setaPlotVisible(boolean btnPlot, boolean plot_1, boolean plot_2, boolean plot_roc) {
        setVisivelBtnPlot(btnPlot);
        setVisivelPlot_1(plot_1);
        setVisivelPlot_2(plot_2);
        setVisivelPlotRoc(plot_roc);

    }

    public void setVisivelPlotRoc(boolean visivel) {

        if (visivel) {
            this.visivelPlotRoc = "inline-block";
        } else {
            this.visivelPlotRoc = "none";
        }
    }

    private void setVisivelDersimonian(boolean visivelDersimonian) {
        if (visivelDersimonian) {
            this.visivelDersimonian = "inline-block";
        } else {
            this.visivelDersimonian = "none";
        }
    }

    public void setVisivelSensibEspec(boolean visivel) {

        if (visivel) {
            this.visivelSensibEspec = "inline-block";
        } else {
            this.visivelSensibEspec = "none";
        }
    }

    public void setVisivelMantelHenzel(boolean visivel) {
        if (visivel) {
            this.visivelMantelHenzel = "inline-block";
        } else {
            this.visivelMantelHenzel = "none";
        }
    }

    public void setVisivelLikelihoodRatioPositivaNegativa(boolean likelihoodRatio) {
        if (likelihoodRatio) {
            this.visivelLikelihoodRatioPositivaNegativa = "inline-block";
        } else {
            this.visivelLikelihoodRatioPositivaNegativa = "none";
        }
    }

    public void setVisivelBtnPlot(boolean visivel) {
        if (visivel) {
            this.visivelBtnPlot = "inline-block";
        } else {
            this.visivelBtnPlot = "none";
        }
    }

    public void setVisivelPlot_1(boolean visivel) {
        if (visivel) {
            this.visivelPlot_1 = "inline-block";
        } else {
            this.visivelPlot_1 = "none";
        }
    }

    public void setVisivelPlot_2(boolean visivel) {
        if (visivel) {
            this.visivelPlot_2 = "inline-block";
        } else {
            this.visivelPlot_2 = "none";
        }
    }

    public String getIntervaloConfiaca() {
        return intervaloConfiaca;
    }

    public BigDecimal getTauQuadrado() {
        return tauQuadrado;
    }

    public int getNumeroDeEstudos() {
        return numeroDeEstudos;
    }

    public void setNumeroDeEstudos(int numeroDeEstudos) {
        this.numeroDeEstudos = numeroDeEstudos;
    }

    public void setTauQuadrado(BigDecimal tauQuadrado) {
        this.tauQuadrado = tauQuadrado;
    }

    public void setIntervaloConfiaca(String intervaloConfiaca) {
        this.intervaloConfiaca = intervaloConfiaca;
    }

    public List<EstudoMantelHaenszel> getEstudosDigitado() {
        return estudosDigitado;
    }

    public void setEstudosDigitado(List<EstudoMantelHaenszel> estudos) {
        this.estudosDigitado = estudos;
    }

    public void exluirEstudo(EstudoMantelHaenszel estudo) {
        if (estudosDigitado != null) {
            estudosDigitado.remove(estudo);
        }
    }

    public int quantidadeDeEstudosValidos() {
        if (pearsonDiagnostico.getEstudosValidos() != null) {
            return pearsonDiagnostico.getEstudosValidos().size();
        }
        return 0;
    }

    public void addEstudo() {
        estudosDigitado.add(new EstudoMantelHaenszel());
    }

    public void addEstudos() {

        estudosDigitado = new ArrayList<>();

        /*
         ESTUDO 1    */
         
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 1", new BigDecimal("99"), new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("14")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 2", new BigDecimal("82.5"), new BigDecimal("0.5"), new BigDecimal("23.5"), new BigDecimal("20.5")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 3", new BigDecimal("47"), new BigDecimal("25"), new BigDecimal("5"), new BigDecimal("73")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 4", new BigDecimal("130"), new BigDecimal("13"), new BigDecimal("10"), new BigDecimal("83")));
     
        /*
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 4", new BigDecimal("130"), new BigDecimal("13"), new BigDecimal("10"), new BigDecimal("83")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 6", new BigDecimal("24"), new BigDecimal("3"), new BigDecimal("16"), new BigDecimal("17")));*/

        /*
         ESTUDO 2
         */
        /*
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 1", new BigDecimal("35"), new BigDecimal("11"), new BigDecimal("6"), new BigDecimal("13")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 2", new BigDecimal("57"), new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("58")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 3", new BigDecimal("14"), new BigDecimal("5"), new BigDecimal("7"), new BigDecimal("71")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 4", new BigDecimal("37"), new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("57")));
         estudosDigitado.add(new EstudoMantelHaenszel("Estudo 5", new BigDecimal("22"), new BigDecimal("1"), new BigDecimal("30"), new BigDecimal("42")));
         */
        //ESTUDO 3
        /*estudosDigitado.add(new EstudoMantelHaenszel("Estudo 1", new BigDecimal("35"), new BigDecimal("11"), new BigDecimal("6"), new BigDecimal("13")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 2", new BigDecimal("57"), new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("58")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 3", new BigDecimal("14"), new BigDecimal("5"), new BigDecimal("7"), new BigDecimal("71")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 4", new BigDecimal("10.5"), new BigDecimal("0.5"), new BigDecimal("14.5"), new BigDecimal("44.5")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 5", new BigDecimal("37"), new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("57")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 6", new BigDecimal("22"), new BigDecimal("1"), new BigDecimal("30"), new BigDecimal("42")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 7", new BigDecimal("14"), new BigDecimal("10"), new BigDecimal("7"), new BigDecimal("107")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 8", new BigDecimal("34"), new BigDecimal("8"), new BigDecimal("8"), new BigDecimal("40")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 9", new BigDecimal("20.5"), new BigDecimal("0.5"), new BigDecimal("1.5"), new BigDecimal("6.5")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 10", new BigDecimal("44"), new BigDecimal("11"), new BigDecimal("72"), new BigDecimal("209")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 11", new BigDecimal("55"), new BigDecimal("6"), new BigDecimal("88"), new BigDecimal("118")));
        estudosDigitado.add(new EstudoMantelHaenszel("Estudo 12", new BigDecimal("64"), new BigDecimal("2"), new BigDecimal("14"), new BigDecimal("82")));
*/
    }

    public void calcular() {

        pearsonDiagnostico.setEstudosValidos(ValidacaoDeEstudo.retornaEstudosValidados(estudosDigitado));

        if ("mantelHaenszel".equals(this.testeDiagnostico)) {
            calcularMantelHaenszel();
        } else if ("dersimonianLaird".equals(this.testeDiagnostico)) {
            calcularDersimonianLaird();
        } else if ("sensibilidadeEspecificidade".equals(this.testeDiagnostico)) {
            calcularSensi();
            calcularSpec();

        } else if ("likelihoodRatioPositivaNegativa".equals(this.testeDiagnostico)) {
            calcularLikelihoodRatioPositiva();
            calcularLikelihoodRatioNegativa();
        }

        /*else if ("receiverOperatingCharacteristic".equals(this.testeDiagnostico)) {
         calcularSensi();
         calcularSpec();
            
         //calcularErroPadraoAucEfeitoFixo();
         //calcularErroPadraoAucEfeitoRandomico();

         }*/
    }

    public void exibeLista(List<EstudoMantelHaenszel> estudos) {
        for (Iterator<EstudoMantelHaenszel> it = estudos.iterator(); it.hasNext();) {
            EstudoMantelHaenszel estudo = it.next();
            System.out.println("ESTUDOS:" + estudo.getNome()
                    + "-VN:" + estudo.getVerdadeiroNegativo()
                    + "-VP:" + estudo.getVerdadeiroPositivo()
                    + "-FN:" + estudo.getFalsoNegativo()
                    + "-FP:" + estudo.getFalsoPositivo());
        }
    }

    public void calcularMantelHaenszel() {
        setaVisualizacaoDeTabela(true, false, false, false);
        setaPlotVisible(true, false, false, false);
        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);
        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {

            estudo.setOddsRatio(OddsRatio.calcularOddsRatio(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperior(OddsRatio.calcularIntervaloConfiancaSuperior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferior(OddsRatio.calcularIntervaloConfiancaInferior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setPeso(MantelHaenszel.calcularPeso(estudo, precisao, metodoArredondamento));
        }

        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());
        setOddsRatioMantelHaenszel(MantelHaenszel.calcularOddsRatioMantelHaenszel(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setMhLimiteSuperior(MantelHaenszel.calcularIntervaloConfiancaSuperior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setMhLimiteInferior(MantelHaenszel.calcularIntervaloConfiancaInferior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

        setChiSquared(MantelHaenszel.calcularChiSquared(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setISquared(MantelHaenszel.calcularISquared(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento));
        MantelHaenszel.calcularPercentualPeso(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento);

        // System.out.println(getOddsRatioMantelHaenszel());
    }

    public void calcularSensi() {
        setaVisualizacaoDeTabela(false, true, false, false);
        setaPlotVisible(true, false, false, false);
        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);

        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {

            estudo.setSen(SenSpecy.CalcularSensibilidade(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperiorSens(SenSpecy.calcularIntervaloConfiancaSuperiorSen(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferiorSens(SenSpecy.calcularIntervaloConfiancaInferiorSen(estudo, precisao, metodoArredondamento, zScore));
            //estudo.setPeso(DersimonianAndLaird.calcularPesoOriginalDersimonian(pearsonDiagnostico.getEstudosValidos(), estudo, precisao, metodoArredondamento));
            estudo.setPercentualPeso(DersimonianAndLaird.calcularPercentualPesoOriginalDersimonian(pearsonDiagnostico.getEstudosValidos(), estudo, precisao, metodoArredondamento));
        }

        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());
        setSenMantelHaenszel(SenSpecy.calcularSensAgrupada(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setMhLimiteSuperiorSen(SenSpecy.calcularIntervaloConfiancaAgrupadoSuperiorSen(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setMhLimiteInferiorSen(SenSpecy.calcularIntervaloConfiancaAgrupadoInferiorSen(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        //setChiSquared(MantelHaenszel.calcularChiSquared(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        //setISquared(MantelHaenszel.calcularISquared(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento));
        //setTauQuadrado(DersimonianAndLaird.calcularTauAoQuadrado(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        //setNumeroDeEstudos(quantidadeDeEstudosValidos());
        //MantelHaenszel.calcularPercentualPeso(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento);
    }

    public void calcularSpec() {

        setaVisualizacaoDeTabela(false, true, false, false);
        setaPlotVisible(true, false, false, false);
        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {

            //System.out.println(estudo);
            estudo.setSpecy(SenSpecy.CalcularSpecificidade(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperiorSpec(SenSpecy.calcularIntervaloConfiancaSuperiorSpec(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferiorSpec(SenSpecy.calcularIntervaloConfiancaInferiorSpecy(estudo, precisao, metodoArredondamento, zScore));
            //estudo.setPeso(DersimonianAndLaird.calcularPesoOriginalDersimonian(pearsonDiagnostico.getEstudosValidos(), estudo, precisao, metodoArredondamento));
            estudo.setPercentualPeso(DersimonianAndLaird.calcularPercentualPesoOriginalDersimonian(pearsonDiagnostico.getEstudosValidos(), estudo, precisao, metodoArredondamento));

        }

        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());
        setSpecMantelHaenszel(SenSpecy.calcularSpecyAgrupada(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setMhLimiteSuperiorSpec(SenSpecy.calcularIntervaloConfiancaAgrupadoSuperiorSpecy(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setMhLimiteInferiorSpec(SenSpecy.calcularIntervaloConfiancaAgrupadoInferiorSpecy(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        //setChiSquared(MantelHaenszel.calcularChiSquared(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        //setISquared(MantelHaenszel.calcularISquared(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento));
        //setTauQuadrado(DersimonianAndLaird.calcularTauAoQuadrado(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        //setNumeroDeEstudos(quantidadeDeEstudosValidos());
        //MantelHaenszel.calcularPercentualPeso(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento);

    }

    public void calcularDersimonianLaird() {
        setaVisualizacaoDeTabela(false, false, true, false);
        setaPlotVisible(true, false, false, false);
        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {

            estudo.setOddsRatio(OddsRatio.calcularOddsRatio(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperior(OddsRatio.calcularIntervaloConfiancaSuperior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferior(OddsRatio.calcularIntervaloConfiancaInferior(estudo, precisao, metodoArredondamento, zScore));
            estudo.setPeso(DersimonianAndLaird.calcularPesoOriginalDersimonian(pearsonDiagnostico.getEstudosValidos(), estudo, precisao, metodoArredondamento));
            estudo.setPercentualPeso(DersimonianAndLaird.calcularPercentualPesoOriginalDersimonian(pearsonDiagnostico.getEstudosValidos(), estudo, precisao, metodoArredondamento));
        }
        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());

        setOddsRatioMantelHaenszel(DersimonianAndLaird.calcularOddsRatioAgrupado(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setMhLimiteSuperior(DersimonianAndLaird.calcularOddsRatioAgrupadoSuperior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setMhLimiteInferior(DersimonianAndLaird.calcularOddsRatioAgrupadoInferior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

        setChiSquared(MantelHaenszel.calcularChiSquared(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setISquared(MantelHaenszel.calcularISquared(pearsonDiagnostico.getEstudosValidos(), metodoArredondamento));
        setTauQuadrado(DersimonianAndLaird.calcularTauAoQuadrado(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setNumeroDeEstudos(quantidadeDeEstudosValidos());

    }

    public void calcularLikelihoodRatioPositiva() {
        setaVisualizacaoDeTabela(false, false, false, true);
        setaPlotVisible(true, false, false, false);
        LikelihoodRatio.Arredondamento metodoArredondamento = LikelihoodRatio.Arredondamento.get(arredondamento);
        LikelihoodRatio.ZScore zScore = LikelihoodRatio.ZScore.get(intervaloConfiaca);

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {
            estudo.setPositiveLikelihoodRatio(LikelihoodRatio.calcularLikelihoodRatioPositiva(estudo, precisao, metodoArredondamento));
            estudo.setIntervaloConfiancaSuperiorLRPositiva(LikelihoodRatio.calcularIntervaloConfiancaSuperiorLRPositiva(estudo, precisao, metodoArredondamento, zScore));
            estudo.setIntervaloConfiancaInferiorLRPositiva(LikelihoodRatio.calcularIntervaloConfiancaInferiorLRPositiva(estudo, precisao, metodoArredondamento, zScore));
            estudo.setPesoLRPositiva(LikelihoodRatioPositivaMantelHaenszel.calcularPesoParaLRPositiva(estudo, precisao, metodoArredondamento));
        }
        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());
        setLikelihoodRatioPositiva(LikelihoodRatioPositivaMantelHaenszel.calcularLRPositivaAgrupada(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setIntervaloConfiancaSuperiorPositivo(LikelihoodRatioPositivaMantelHaenszel.calcularIntervaloConfiancaSuperiorAgrupadoLRPositiva(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIntervaloConfiancaInferiorPositivo(LikelihoodRatioPositivaMantelHaenszel.calcularIntervaloConfiancaInferiorAgrupadoLRPositiva(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setErroPadraoLRPositiva(LikelihoodRatioPositivaMantelHaenszel.calcularErroPadraoAgrupadoLRPositiva(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        LikelihoodRatioPositivaMantelHaenszel.calcularPercentualPesoLRPositiva(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento);
        setNumeroDeEstudos(quantidadeDeEstudosValidos());
    }

    public void calcularLikelihoodRatioNegativa() {
        setaVisualizacaoDeTabela(false, false, false, true);
        setaPlotVisible(true, false, false, false);
        LikelihoodRatio.Arredondamento metArredondamento = LikelihoodRatio.Arredondamento.get(arredondamento);
        LikelihoodRatio.ZScore zScore = LikelihoodRatio.ZScore.get(intervaloConfiaca);

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {
            estudo.setNegativeLikelihoodRatio(LikelihoodRatio.calcularLikelihoodRatioNegativa(estudo, precisao, metArredondamento));
            estudo.setIntervaloConfiancaSuperiorLRNegativa(LikelihoodRatio.calcularIntervaloConfiancaSuperiorLRNegativa(estudo, precisao, metArredondamento, zScore));
            estudo.setIntervaloConfiancaInferiorLRNegativa(LikelihoodRatio.calcularIntervaloConfiancaInferiorLRNegativa(estudo, precisao, metArredondamento, zScore));
            estudo.setPesoLRNegativa(LikelihoodRatioNegativaMantelHaenszel.calcularPesoParaLRNegativa(estudo, precisao, metArredondamento));
        }
        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());
        setLikelihoodRatioNegativa(LikelihoodRatioNegativaMantelHaenszel.calcularLRNegativaAgrupada(pearsonDiagnostico.getEstudosValidos(), precisao, metArredondamento));
        setIntervaloConfiancaSuperiorNegativo(LikelihoodRatioNegativaMantelHaenszel.calcularIntervaloConfiancaSuperiorAgrupadoLRNegativa(pearsonDiagnostico.getEstudosValidos(), precisao, metArredondamento, zScore));
        setIntervaloConfiancaInferiorNegativo(LikelihoodRatioNegativaMantelHaenszel.calcularIntervaloConfiancaInferiorAgrupadoLRNegativa(pearsonDiagnostico.getEstudosValidos(), precisao, metArredondamento, zScore));
        setErroPadraoLRNegativa(LikelihoodRatioNegativaMantelHaenszel.calcularErroPadraoAgrupadoLRNegativa(pearsonDiagnostico.getEstudosValidos(), precisao, metArredondamento));
        LikelihoodRatioNegativaMantelHaenszel.calcularPercentualPesoLRNegativa(pearsonDiagnostico.getEstudosValidos(), precisao, metArredondamento);
        setNumeroDeEstudos(quantidadeDeEstudosValidos());
    }

    //add
    public void calcularAucEfeitoFixo() {

        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);

        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());
        setOddsRatioMantelHaenszel(MantelHaenszel.calcularOddsRatioMantelHaenszel(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setMhLimiteSuperior(MantelHaenszel.calcularIntervaloConfiancaSuperior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setMhLimiteInferior(MantelHaenszel.calcularIntervaloConfiancaInferior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {
            estudo.setSpecy(SenSpecy.CalcularSpecificidade(estudo, precisao, metodoArredondamento));
            estudo.setSen(SenSpecy.CalcularSensibilidade(estudo, precisao, metodoArredondamento));
        }
        
        setAucEfeitoFixo(CurveSroc.calcularAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setErroPadraoAucEfeitoFixo(CurveSroc.calcularErroPadraoAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setIntervaloConfiancaNegativoAucEfeitoFixo(CurveSroc.calcularIntervaloConfiancaNegativoAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIntervaloConfiancaPositivoAucEfeitoFixo(CurveSroc.calcularIntervaloConfiancaPositivoAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

        setIndiceQEfeitoFixo(CurveSroc.calcularIndiceQEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setErroPadraoIndiceQAucEfeitoFixo(CurveSroc.calcularErroPadraoIndiceQAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setIntervaloConfiancaNegativoIndiceQEfeitoFixo(CurveSroc.calcularIntervaloConfiancaNegativoIndiceQAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIntervaloConfiancaPositivoIndiceQEfeitoFixo(CurveSroc.calcularIntervaloConfiancaPositivoIndiceQAucEfeitoFixo(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

        //System.out.println(getErroPadraoAucEfeitoFixo());
    }

    public void calcularAucEfeitoRandomico() {

        OddsRatio.Arredondamento metodoArredondamento = OddsRatio.Arredondamento.get(arredondamento);
        OddsRatio.ZScore zScore = OddsRatio.ZScore.get(intervaloConfiaca);

        estudosCalculados = new ArrayList<>(pearsonDiagnostico.getEstudosValidos());

        setOddsRatioMantelHaenszel(DersimonianAndLaird.calcularOddsRatioAgrupado(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setMhLimiteSuperior(DersimonianAndLaird.calcularOddsRatioAgrupadoSuperior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setMhLimiteInferior(DersimonianAndLaird.calcularOddsRatioAgrupadoInferior(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

        for (EstudoMantelHaenszel estudo : pearsonDiagnostico.getEstudosValidos()) {
            estudo.setSpecy(SenSpecy.CalcularSpecificidade(estudo, precisao, metodoArredondamento));
            estudo.setSen(SenSpecy.CalcularSensibilidade(estudo, precisao, metodoArredondamento));
        }
        setAucEfeitoRandomico(CurveSroc.calcularAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setErroPadraoAucEfeitoRandomico(CurveSroc.calcularErroPadraoAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIntervaloConfiancaNegativoAucEfeitoRandomico(CurveSroc.calcularIntervaloConfiancaNegativoAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIntervaloConfiancaPositivoAucEfeitoRandomico(CurveSroc.calcularIntervaloConfiancaPositivoAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIndiceQEfeitoRandomico(CurveSroc.calcularIndiceQEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setErroPadraoIndiceQAucEfeitoRandomico(CurveSroc.calcularErroPadraoIndiceQAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento));
        setIntervaloConfiancaNegativoIndiceQEfeitoRandomico(CurveSroc.calcularIntervaloConfiancaNegativoIndiceQAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));
        setIntervaloConfiancaPositivoIndiceQEfeitoRandomico(CurveSroc.calcularIntervaloConfiancaPositivoIndiceQAucEfeitoRandomico(pearsonDiagnostico.getEstudosValidos(), precisao, metodoArredondamento, zScore));

    }

    // MantelHaenzel
    public void DrawForestPlot() {

        switch (testeDiagnostico) {
            case "sensibilidadeEspecificidade":
                DrawForestPlot_sensibilidade();
                DrawForestPlot_especificidade();
                setaPlotVisible(false, true, true, false);
                break;

            case "dersimonianLaird":
                DrawForestPlot_oddsDersimonianLaird();
                setaPlotVisible(false, true, false, false);
                break;

            case "mantelHaenszel":
                DrawForestPlot_oddsRatioMantelHaenszel();
                setaPlotVisible(false, true, false, false);
                break;

            case "likelihoodRatioPositivaNegativa":
                DrawForestPlot_LikelihoodRatioP();
                DrawForestPlot_LikelihoodRatioN();
                setaPlotVisible(false, true, true, false);
                break;
        }
    }

    //Adicionado curva Roc
    public void DrawRoc() {

        switch (testeDiagnostico) {

            case "sensibilidadeEspecificidade":
            case "dersimonianLaird":
            case "mantelHaenszel":
            case "likelihoodRatioPositivaNegativa":

                if ("EF".equals(this.typeEfeito)) {
                    calcularAucEfeitoFixo();
                    DrawRocPlotEfeitoFixo();
                } else if ("ER".equals(this.typeEfeito)) {
                    calcularAucEfeitoRandomico();
                    DrawRocPlotEfeitoRandomico();
                }
                setaPlotVisible(false, false, false, true);
                break;
        }
    }

    public void DrawRocPlotEfeitoFixo() {
        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("fixo", "http://localhost:8080/", "#idforest", getOddsRatioMantelHaenszel(), getMhLimiteInferior(), getMhLimiteSuperior());
        
        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            lDados.datas[i] = new data(estudo.getSen(), estudo.getSpecy(), estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        /*dataCurveRoc dataCurve = new dataCurveRoc(
         aucEfeitoFixo,
         erroPadraoAucEfeitoFixo,
         indiceQEfeitoFixo,
         intervaloConfiancaNegativoIndiceQEfeitoFixo,
         erroPadraoIndiceQAucEfeitoFixo,
         aucEfeitoRandomico,
         erroPadraoAucEfeitoRandomico,
         indiceQEfeitoRandomico
         );*/
        // System.out.println(dataCurve);
        lDados.descEstudos = new String[8];
        lDados.descEstudos[0] = "Symmetric SROC";
        lDados.descEstudos[1] = "=== EFEITO FIXO ===";
        lDados.descEstudos[2] = "AUC = " + aucEfeitoFixo;
        lDados.descEstudos[3] = "IC = " + intervaloConfiancaNegativoAucEfeitoFixo + " - " + intervaloConfiancaPositivoAucEfeitoFixo;
        lDados.descEstudos[4] = "SE(AUC) = " + erroPadraoAucEfeitoFixo;
        lDados.descEstudos[5] = "Q* = " + indiceQEfeitoFixo;
        lDados.descEstudos[6] = "IC = " + intervaloConfiancaNegativoIndiceQEfeitoFixo + " - " + intervaloConfiancaPositivoIndiceQEfeitoFixo;
        lDados.descEstudos[7] = "SE(Q*) = " + erroPadraoIndiceQAucEfeitoFixo;

        //lDados.dadosCurva = dataCurve;
        this.setImgB64plotRoc(ClienteWS.toMetaRoc(lDados));
    }

    public void DrawRocPlotEfeitoRandomico() {
        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("randomico", "http://localhost:8080/", "#idforest", getOddsRatioMantelHaenszel(), getMhLimiteInferior(), getMhLimiteSuperior());

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            lDados.datas[i] = new data(estudo.getSen(), estudo.getSpecy(), estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }


        /*lDados.dadosCurva = new dataCurveRoc[3];  

         dataCurveRoc dataCurve = new dataCurveRoc(
         aucEfeitoFixo,
         erroPadraoAucEfeitoFixo
         );*/
        //lDados.dadosCurva = new data[3];
        lDados.descEstudos = new String[8];
        lDados.descEstudos[0] = "Symmetric SROC";
        lDados.descEstudos[1] = "=== EFEITO RANDOMICO ===";
        lDados.descEstudos[2] = "AUC = " + aucEfeitoRandomico;
        lDados.descEstudos[3] = "IC = " + intervaloConfiancaNegativoAucEfeitoRandomico + " - " + intervaloConfiancaPositivoAucEfeitoRandomico;
        lDados.descEstudos[4] = "SE(AUC) = " + erroPadraoAucEfeitoRandomico;
        lDados.descEstudos[5] = "Q* = " + indiceQEfeitoRandomico;
        lDados.descEstudos[6] = "IC = " + intervaloConfiancaNegativoIndiceQEfeitoRandomico + " - " + intervaloConfiancaPositivoIndiceQEfeitoRandomico;
        lDados.descEstudos[7] = "SE(Q*) = " + erroPadraoIndiceQAucEfeitoRandomico;

        // lDados.dadosCurva[i] = dataCurve;
        this.setImgB64plotRoc(ClienteWS.toMetaRoc(lDados));
    }

    // forest plots LikelihoodRatio Positiva 
    public String GetObsFormatada_LikelihoodRatioP() {

        String ret = "Erro Padrao (lnLR+): " + this.erroPadraoLRPositiva;
        return ret;
    }

    public void DrawForestPlot_LikelihoodRatioP() {

        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("Razao de Verossimilhanca Positiva (Efeito Fixo Mantel Haenszel)",
                Integer.parseInt(intervaloConfiaca),
                "#idforest", 780, 14, "Arial Bold", 1, 0, "http://localhost:8080/",
                "lrp", GetObsFormatada_LikelihoodRatioP(), maxp, isBasicPlot);

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            effect efeito = new effect(estudo.getPositiveLikelihoodRatio(), estudo.getIntervaloConfiancaInferiorLRPositiva(), estudo.getIntervaloConfiancaSuperiorLRPositiva());
            lDados.datas[i] = new data(estudo.getNome(), 0, efeito, estudo.getPercentualPesoLRPositiva(),
                    estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        effect efeitoMA = new effect(likelihoodRatioPositiva, intervaloConfiancaInferiorPositivo, intervaloConfiancaSuperiorPositivo);
        lDados.datas[i] = new data("Resultado:", 1, efeitoMA, BigDecimal.valueOf(0),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));

        this.setImgB64plot(ClienteWS.toMetaD3(lDados));
    }

    // forest plots odds LikelihoodRatio negativa  
    public String GetObsFormatada_LikelihoodRatioN() {

        String ret = "Erro Padrao (lnLR-): " + this.erroPadraoLRNegativa;

        return ret;
    }

    public void DrawForestPlot_LikelihoodRatioN() {

        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("Razao de Verossimilhanca Negativa (Efeito Fixo Mantel Haenszel)",
                Integer.parseInt(intervaloConfiaca),
                "#idforest", 780, 14, "Arial Bold", 1, 0, "http://localhost:8080/",
                "lrn", GetObsFormatada_LikelihoodRatioN(), maxp, isBasicPlot);

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            effect efeito = new effect(estudo.getNegativeLikelihoodRatio(), estudo.getIntervaloConfiancaInferiorLRNegativa(), estudo.getIntervaloConfiancaSuperiorLRNegativa());
            lDados.datas[i] = new data(estudo.getNome(), 0, efeito, estudo.getPercentualPesoLRPositiva(),
                    estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        effect efeitoMA = new effect(likelihoodRatioNegativa, intervaloConfiancaInferiorNegativo, intervaloConfiancaSuperiorNegativo);
        lDados.datas[i] = new data("Resultado:", 1, efeitoMA, BigDecimal.valueOf(0),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));

        this.setImgB64plot_2(ClienteWS.toMetaD3(lDados));
    }

    // forest plots odds DersimonianLaird  
    public String GetObsFormatada_oddsDersimonianLaird() {

        String ret = "Heterogeneidade qui-quadrado: " + this.chiSquared + " - ";
        ret += "I-quadrado(%): " + this.iSquared + " - ";
        ret += "Tau-quadrado(tau2): " + this.tauQuadrado;

        return ret;
    }

    public void DrawForestPlot_oddsDersimonianLaird() {

        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("Odds Ratio - Efeito fixo (Dersimonian Laird)",
                Integer.parseInt(intervaloConfiaca),
                "#idforest", 780, 14, "Arial Bold", 1, 0, "http://localhost:8080/",
                "odds", GetObsFormatada_oddsDersimonianLaird(), maxp, isBasicPlot);

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            effect efeito = new effect(estudo.getOddsRatio(), estudo.getIntervaloConfiancaInferior(), estudo.getIntervaloConfiancaSuperior());
            lDados.datas[i] = new data(estudo.getNome(), 0, efeito, estudo.getPercentualPeso(),
                    estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        effect efeitoMA = new effect(oddsRatioMantelHaenszel, mhLimiteInferior, mhLimiteSuperior);
        lDados.datas[i] = new data("Resultado:", 1, efeitoMA, BigDecimal.valueOf(0),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));

        this.setImgB64plot(ClienteWS.toMetaD3(lDados));
    }

    // forest plots odds Mantel 
    public String GetObsFormatada_oddsRatioMantelHaenszel() {

        String ret = "Heterogeneidade qui-quadrado: " + this.chiSquared + " - ";
        ret += "I-quadrado(%): " + this.iSquared;

        return ret;
    }

    public void DrawForestPlot_oddsRatioMantelHaenszel() {

        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("Odds Ratio - Efeito fixo (Mantel Haenszel)",
                Integer.parseInt(intervaloConfiaca),
                "#idforest", 780, 14, "Arial Bold", 1, 0, "http://localhost:8080/",
                "odds", GetObsFormatada_oddsRatioMantelHaenszel(), maxp, isBasicPlot);

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            effect efeito = new effect(estudo.getOddsRatio(), estudo.getIntervaloConfiancaInferior(), estudo.getIntervaloConfiancaSuperior());
            lDados.datas[i] = new data(estudo.getNome(), 0, efeito, estudo.getPercentualPeso(),
                    estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        effect efeitoMA = new effect(oddsRatioMantelHaenszel, mhLimiteInferior, mhLimiteSuperior);
        lDados.datas[i] = new data("Resultado:", 1, efeitoMA, BigDecimal.valueOf(0),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));

        this.setImgB64plot(ClienteWS.toMetaD3(lDados));
    }

    public String GetObsFormatada_sensibilidade() {

        return "";
        //String  ret = "Heterogeneidade qui-quadrado: " + this.chiSquared + " - ";
        //       ret += "I-quadrado(%): " + this.iSquared + " - ";
        //       ret += "Tau-quadrado(tau2): " + this.tauQuadrado;

        //return ret;
    }

    public void DrawForestPlot_sensibilidade() {

        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("Sensibilidade",
                Integer.parseInt(intervaloConfiaca),
                "#idforest", 780, 14, "Arial Bold", 100, 10, "http://localhost:8080/",
                "sen", GetObsFormatada_sensibilidade(), maxp, isBasicPlot);

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            effect efeito = new effect(estudo.getSen(), estudo.getIntervaloConfiancaInferiorSens(), estudo.getIntervaloConfiancaSuperiorSens());
            lDados.datas[i] = new data(estudo.getNome(), 0, efeito, estudo.getPercentualPeso(),
                    estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        effect efeitoMA = new effect(getSenMantelHaenszel(), mhLimiteInferiorSen, mhLimiteSuperiorSen);
        lDados.datas[i] = new data("Resultado:", 1, efeitoMA, BigDecimal.valueOf(0),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));

        this.setImgB64plot(ClienteWS.toMetaD3(lDados));
    }

    public String GetObsFormatada_especificidade() {

        return "";
        //String  ret = "Heterogeneidade qui-quadrado: " + this.chiSquared + " - ";
        //        ret += "I-quadrado(%): " + this.iSquared + " - ";
        //        ret += "Tau-quadrado(tau2): " + this.tauQuadrado;

        //return ret;
    }

    public void DrawForestPlot_especificidade() {

        int i = 0;
        dataMetaD3 lDados = new dataMetaD3();

        lDados.plotConfig = new plotConfig("Especificidade",
                Integer.parseInt(intervaloConfiaca),
                "#idforest", 780, 14, "Arial Bold", 100, 10, "http://localhost:8080/",
                "spec", GetObsFormatada_especificidade(), maxp, isBasicPlot);

        lDados.datas = new data[estudosCalculados.size() + 1];

        for (EstudoMantelHaenszel estudo : estudosCalculados) {

            effect efeito = new effect(estudo.getSpecy(), estudo.getIntervaloConfiancaInferiorSpec(), estudo.getIntervaloConfiancaSuperiorSpec());
            lDados.datas[i] = new data(estudo.getNome(), 0, efeito, estudo.getPercentualPeso(),
                    estudo.getVerdadeiroPositivo(), estudo.getFalsoPositivo(), estudo.getVerdadeiroNegativo(), estudo.getFalsoNegativo());
            i++;
        }

        effect efeitoMA = new effect(getSpecMantelHaenszel(), mhLimiteInferiorSpec, mhLimiteSuperiorSpec);
        lDados.datas[i] = new data("Resultado:", 1, efeitoMA, BigDecimal.valueOf(0),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));

        this.setImgB64plot_2(ClienteWS.toMetaD3(lDados));
    }

    public BigDecimal getLikelihoodRatioPositiva() {
        return likelihoodRatioPositiva;
    }

    public void setLikelihoodRatioPositiva(BigDecimal likelihoodRatioPositiva) {
        this.likelihoodRatioPositiva = likelihoodRatioPositiva;
    }

    public BigDecimal getLikelihoodRatioNegativa() {
        return likelihoodRatioNegativa;
    }

    public void setLikelihoodRatioNegativa(BigDecimal likelihoodRatioNegativa) {
        this.likelihoodRatioNegativa = likelihoodRatioNegativa;
    }

    public String getImgPlot_2() {
        return imgPlot_2;
    }

    public BigDecimal getIntervaloConfiancaSuperiorPositivo() {
        return intervaloConfiancaSuperiorPositivo;
    }

    public void setIntervaloConfiancaSuperiorPositivo(BigDecimal intervaloConfiancaSuperiorPositivo) {
        this.intervaloConfiancaSuperiorPositivo = intervaloConfiancaSuperiorPositivo;
    }

    public BigDecimal getIntervaloConfiancaInferiorPositivo() {
        return intervaloConfiancaInferiorPositivo;
    }

    public void setIntervaloConfiancaInferiorPositivo(BigDecimal intervaloConfiancaInferiorPositivo) {
        this.intervaloConfiancaInferiorPositivo = intervaloConfiancaInferiorPositivo;
    }

    public BigDecimal getIntervaloConfiancaSuperiorNegativo() {
        return intervaloConfiancaSuperiorNegativo;
    }

    public void setIntervaloConfiancaSuperiorNegativo(BigDecimal intervaloConfiancaSuperiorNegativo) {
        this.intervaloConfiancaSuperiorNegativo = intervaloConfiancaSuperiorNegativo;
    }

    public BigDecimal getIntervaloConfiancaInferiorNegativo() {
        return intervaloConfiancaInferiorNegativo;
    }

    public void setIntervaloConfiancaInferiorNegativo(BigDecimal intervaloConfiancaInferiorNegativo) {
        this.intervaloConfiancaInferiorNegativo = intervaloConfiancaInferiorNegativo;
    }

    public BigDecimal getChiSquaredLikelihood() {
        return chiSquaredLikelihood;
    }

    public void setChiSquaredLikelihood(BigDecimal chiSquaredLikelihood) {
        this.chiSquaredLikelihood = chiSquaredLikelihood;
    }

    public BigDecimal getISquaredLikelihood() {
        return iSquaredLikelihood;
    }

    public void setISquaredLikelihood(BigDecimal iSquaredLikelihood) {
        this.iSquaredLikelihood = iSquaredLikelihood;
    }

    public BigDecimal getErroPadraoLRPositiva() {
        return erroPadraoLRPositiva;
    }

    public void setErroPadraoLRPositiva(BigDecimal erroPadraoLRPositiva) {
        this.erroPadraoLRPositiva = erroPadraoLRPositiva;
    }

    public BigDecimal getErroPadraoLRNegativa() {
        return erroPadraoLRNegativa;
    }

    public void setErroPadraoLRNegativa(BigDecimal erroPadraoLRNegativa) {
        this.erroPadraoLRNegativa = erroPadraoLRNegativa;
    }

    public BigDecimal getSenMantelHaenszel() {
        return SenMantelHaenszel;
    }

    public void setSenMantelHaenszel(BigDecimal SenMantelHaenszel) {
        this.SenMantelHaenszel = SenMantelHaenszel;
    }

    public BigDecimal getSpecMantelHaenszel() {
        return SpecMantelHaenszel;
    }

    public void setSpecMantelHaenszel(BigDecimal SpecMantelHaenszel) {
        this.SpecMantelHaenszel = SpecMantelHaenszel;
    }

    public BigDecimal getOddsRatioMantelHaenszel() {
        return oddsRatioMantelHaenszel;
    }

    public void setOddsRatioMantelHaenszel(BigDecimal oddsRatioMantelHaenszel) {
        this.oddsRatioMantelHaenszel = oddsRatioMantelHaenszel;
    }

    //add
    /* GET E SET METODO EFEITOS FIXO*/
    public BigDecimal getAucEfeitoFixo() {
        return aucEfeitoFixo;
    }

    public void setAucEfeitoFixo(BigDecimal aucEfeitoFixo) {
        this.aucEfeitoFixo = aucEfeitoFixo;
    }

    public BigDecimal getIntervaloConfiancaNegativoAucEfeitoFixo() {
        return intervaloConfiancaNegativoAucEfeitoFixo;
    }

    public void setIntervaloConfiancaNegativoAucEfeitoFixo(BigDecimal intervaloConfiancaNegativoAucEfeitoFixo) {
        this.intervaloConfiancaNegativoAucEfeitoFixo = intervaloConfiancaNegativoAucEfeitoFixo;
    }

    public BigDecimal getIntervaloConfiancaPositivoAucEfeitoFixo() {
        return intervaloConfiancaPositivoAucEfeitoFixo;
    }

    public void setIntervaloConfiancaPositivoAucEfeitoFixo(BigDecimal intervaloConfiancaPositivoAucEfeitoFixo) {
        this.intervaloConfiancaPositivoAucEfeitoFixo = intervaloConfiancaPositivoAucEfeitoFixo;
    }

    public BigDecimal getErroPadraoAucEfeitoFixo() {
        return erroPadraoAucEfeitoFixo;
    }

    public void setErroPadraoAucEfeitoFixo(BigDecimal erroPadraoAucEfeitoFixo) {
        this.erroPadraoAucEfeitoFixo = erroPadraoAucEfeitoFixo;
    }

    public BigDecimal getIndiceQEfeitoFixo() {
        return indiceQEfeitoFixo;
    }

    public void setIndiceQEfeitoFixo(BigDecimal indiceQEfeitoFixo) {
        this.indiceQEfeitoFixo = indiceQEfeitoFixo;
    }

    public BigDecimal getIntervaloConfiancaNegativoIndiceQEfeitoFixo() {
        return intervaloConfiancaNegativoIndiceQEfeitoFixo;
    }

    public void setIntervaloConfiancaNegativoIndiceQEfeitoFixo(BigDecimal intervaloConfiancaNegativoIndiceQEfeitoFixo) {
        this.intervaloConfiancaNegativoIndiceQEfeitoFixo = intervaloConfiancaNegativoIndiceQEfeitoFixo;
    }

    public BigDecimal getIntervaloConfiancaPositivoIndiceQEfeitoFixo() {
        return intervaloConfiancaPositivoIndiceQEfeitoFixo;
    }

    public void setIntervaloConfiancaPositivoIndiceQEfeitoFixo(BigDecimal intervaloConfiancaPositivoIndiceQEfeitoFixo) {
        this.intervaloConfiancaPositivoIndiceQEfeitoFixo = intervaloConfiancaPositivoIndiceQEfeitoFixo;
    }

    public BigDecimal getErroPadraoIndiceQAucEfeitoFixo() {
        return erroPadraoIndiceQAucEfeitoFixo;
    }

    public void setErroPadraoIndiceQAucEfeitoFixo(BigDecimal erroPadraoIndiceQAucEfeitoFixo) {
        this.erroPadraoIndiceQAucEfeitoFixo = erroPadraoIndiceQAucEfeitoFixo;
    }

//FIM METODOS GET E SET METODOS EFEITOS FIXOS

    /*
     GET E SET METODO EFEITOS RANDOMICOS
     */
    public BigDecimal getAucEfeitoRandomico() {
        return aucEfeitoRandomico;
    }

    public void setAucEfeitoRandomico(BigDecimal aucEfeitoRandomico) {
        this.aucEfeitoRandomico = aucEfeitoRandomico;
    }

    public BigDecimal getErroPadraoAucEfeitoRandomico() {
        return erroPadraoAucEfeitoRandomico;
    }

    public void setErroPadraoAucEfeitoRandomico(BigDecimal erroPadraoAucEfeitoRandomico) {
        this.erroPadraoAucEfeitoRandomico = erroPadraoAucEfeitoRandomico;
    }

    public BigDecimal getIntervaloConfiancaNegativoAucEfeitoRandomico() {
        return intervaloConfiancaNegativoAucEfeitoRandomico;
    }

    public void setIntervaloConfiancaNegativoAucEfeitoRandomico(BigDecimal intervaloConfiancaNegativoAucEfeitoRandomico) {
        this.intervaloConfiancaNegativoAucEfeitoRandomico = intervaloConfiancaNegativoAucEfeitoRandomico;
    }

    public BigDecimal getIntervaloConfiancaPositivoAucEfeitoRandomico() {
        return intervaloConfiancaPositivoAucEfeitoRandomico;
    }

    public void setIntervaloConfiancaPositivoAucEfeitoRandomico(BigDecimal intervaloConfiancaPositivoAucEfeitoRandomico) {
        this.intervaloConfiancaPositivoAucEfeitoRandomico = intervaloConfiancaPositivoAucEfeitoRandomico;
    }

    public BigDecimal getIndiceQEfeitoRandomico() {
        return indiceQEfeitoRandomico;
    }

    public void setIndiceQEfeitoRandomico(BigDecimal indiceQEfeitoRandomico) {
        this.indiceQEfeitoRandomico = indiceQEfeitoRandomico;
    }

    public BigDecimal getErroPadraoIndiceQAucEfeitoRandomico() {
        return erroPadraoIndiceQAucEfeitoRandomico;
    }

    public void setErroPadraoIndiceQAucEfeitoRandomico(BigDecimal erroPadraoIndiceQAucEfeitoRandomico) {
        this.erroPadraoIndiceQAucEfeitoRandomico = erroPadraoIndiceQAucEfeitoRandomico;
    }

    public BigDecimal getIntervaloConfiancaNegativoIndiceQEfeitoRandomico() {
        return intervaloConfiancaNegativoIndiceQEfeitoRandomico;
    }

    public void setIntervaloConfiancaNegativoIndiceQEfeitoRandomico(BigDecimal intervaloConfiancaNegativoIndiceQEfeitoRandomico) {
        this.intervaloConfiancaNegativoIndiceQEfeitoRandomico = intervaloConfiancaNegativoIndiceQEfeitoRandomico;
    }

    public BigDecimal getIntervaloConfiancaPositivoIndiceQEfeitoRandomico() {
        return intervaloConfiancaPositivoIndiceQEfeitoRandomico;
    }

    public void setIntervaloConfiancaPositivoIndiceQEfeitoRandomico(BigDecimal intervaloConfiancaPositivoIndiceQEfeitoRandomico) {
        this.intervaloConfiancaPositivoIndiceQEfeitoRandomico = intervaloConfiancaPositivoIndiceQEfeitoRandomico;
    }
//FIM DOS METODOS GET E SET DO METODOS EFEITOS RANDOMICOS

    public List<EstudoMantelHaenszel> getEstudosCalculados() {
        return estudosCalculados;
    }

    public void setEstudosCalculados(List<EstudoMantelHaenszel> estudosCalculados) {
        this.estudosCalculados = estudosCalculados;
    }

    public void setMaxp(Integer maxp) {
        this.maxp = maxp;
    }

    public Integer getMaxp() {
        return maxp;
    }

    public Integer getPrecisao() {
        return precisao;
    }

    public void setPrecisao(Integer precisao) {
        this.precisao = precisao;
    }

    public String getArredondamento() {
        return arredondamento;
    }

    public void setArredondamento(String arredondamento) {
        this.arredondamento = arredondamento;
    }

    public String getTypeEfeito() {
        return typeEfeito;
    }

    public void setTypeEfeito(String typeEfeito) {
        this.typeEfeito = typeEfeito;
    }

    public BigDecimal getMhLimiteSuperior() {
        return mhLimiteSuperior;
    }

    public void setMhLimiteSuperior(BigDecimal mhLimiteSuperior) {
        this.mhLimiteSuperior = mhLimiteSuperior;
    }

    public BigDecimal getMhLimiteInferior() {
        return mhLimiteInferior;
    }

    public void setMhLimiteInferior(BigDecimal mhLimiteInferior) {
        this.mhLimiteInferior = mhLimiteInferior;
    }

    public BigDecimal getChiSquared() {
        return chiSquared;
    }

    public void setChiSquared(BigDecimal chiSquared) {
        this.chiSquared = chiSquared;
    }

    public BigDecimal getISquared() {
        return iSquared;
    }

    public void setISquared(BigDecimal iSquared) {
        this.iSquared = iSquared;
    }

    public BigDecimal getMhLimiteSuperiorSen() {
        return mhLimiteSuperiorSen;
    }

    public void setMhLimiteSuperiorSen(BigDecimal mhLimiteSuperiorSen) {
        this.mhLimiteSuperiorSen = mhLimiteSuperiorSen;
    }

    public BigDecimal getMhLimiteInferiorSen() {
        return mhLimiteInferiorSen;
    }

    public void setMhLimiteInferiorSen(BigDecimal mhLimiteInferiorSen) {
        this.mhLimiteInferiorSen = mhLimiteInferiorSen;
    }

    public BigDecimal getMhLimiteSuperiorSpec() {
        return mhLimiteSuperiorSpec;
    }

    public void setMhLimiteSuperiorSpec(BigDecimal mhLimiteSuperiorSpec) {
        this.mhLimiteSuperiorSpec = mhLimiteSuperiorSpec;
    }

    public BigDecimal getMhLimiteInferiorSpec() {
        return mhLimiteInferiorSpec;
    }

    public void setMhLimiteInferiorSpec(BigDecimal mhLimiteInferiorSpec) {
        this.mhLimiteInferiorSpec = mhLimiteInferiorSpec;
    }

    public BigDecimal getiSquared() {
        return iSquared;
    }

    public void setiSquared(BigDecimal iSquared) {
        this.iSquared = iSquared;
    }

    public void setImgB64plot(String imgB64plot) {

        byte[] decoded = Base64.getMimeDecoder().decode(imgB64plot);
        this.imgPlot = new String(decoded);
    }

    public void setImgB64plot_2(String imgB64plot) {

        byte[] decoded = Base64.getMimeDecoder().decode(imgB64plot);
        this.imgPlot_2 = new String(decoded);
    }

    //add
    public void setImgB64plotRoc(String imgB64plot) {

        byte[] decoded = Base64.getMimeDecoder().decode(imgB64plot);
        this.imgPlotRoc = new String(decoded);
    }

    public String getImgPlotRoc() {

        return imgPlotRoc;
    }

    public String getImgPlot() {
        return imgPlot;
    }

    public boolean isIsBasicPlot() {
        return isBasicPlot;
    }

    public void setIsBasicPlot(boolean isBasicPlot) {
        this.isBasicPlot = isBasicPlot;
    }
}
