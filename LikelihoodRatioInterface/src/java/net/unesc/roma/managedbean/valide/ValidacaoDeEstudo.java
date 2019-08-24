/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.valide;

import java.util.Iterator;
import java.util.List;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ederm
 */
public class ValidacaoDeEstudo {

    public static List<EstudoMantelHaenszel> retornaEstudosValidados(List<EstudoMantelHaenszel> estudos) throws IllegalArgumentException {
        List<EstudoMantelHaenszel> retornoDeEstudos = new ArrayList<>();
        try {
            if (estudos == null || estudos.size() == 0) {
                throw new IllegalArgumentException("Não existe estudo para ser feito a pesquisa!");
            }
            retornoDeEstudos = acrescentaCincoDecimosEmEstudosZerados(estudos);
        } catch (ExceptionInInitializerError ex) {
            Logger.getLogger(ValidacaoDeEstudo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retornoDeEstudos;
    }

    private static List<EstudoMantelHaenszel> retiraEstudosComMaisDeUmEstudoComValoresZerados(List<EstudoMantelHaenszel> estudos) {
        List<EstudoMantelHaenszel> retornoDeEstudos = new ArrayList<>();
        
        for (EstudoMantelHaenszel estudo: estudos) {
            if (!possuiMaisDeUmEstudoComZero(estudo)) {
                retornoDeEstudos.add(estudo);
            }
        }
        return retornoDeEstudos;
    }

    private static boolean possuiMaisDeUmEstudoComZero(EstudoMantelHaenszel estudo) {
        int quantidadeComValorZerado = 0;
        if (isZero(estudo.getVerdadeiroNegativo())) {
            quantidadeComValorZerado++;
        }
        if (isZero(estudo.getVerdadeiroPositivo())) {
            quantidadeComValorZerado++;
        }
        if (isZero(estudo.getFalsoPositivo())) {
            quantidadeComValorZerado++;
        }
        if (isZero(estudo.getFalsoNegativo())) {
            quantidadeComValorZerado++;
        }

        if (quantidadeComValorZerado > 1) {
            return true;
        }
        return false;
    }

    private static List<EstudoMantelHaenszel> acrescentaCincoDecimosEmEstudosZerados(List<EstudoMantelHaenszel> estudos) {
        estudos = retiraEstudosComMaisDeUmEstudoComValoresZerados(estudos);
        List<EstudoMantelHaenszel> retornoDeEstudos = new ArrayList<>();
        
        for (EstudoMantelHaenszel estudo: estudos) {
            if (verificaSeUmPossuiValorZero(estudo)) {
                retornoDeEstudos.add(retornaEstudoComCincoDecimosAMais(estudo));
            } else {
                retornoDeEstudos.add(estudo);
            }
        }
        return retornoDeEstudos;
    }

    private static boolean isZero(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean verificaSeUmPossuiValorZero(EstudoMantelHaenszel estudoNext) {
        if (isZero(estudoNext.getFalsoPositivo()) || isZero(estudoNext.getFalsoNegativo())
                || isZero(estudoNext.getVerdadeiroPositivo()) || isZero(estudoNext.getVerdadeiroNegativo())) {
            return true;
        }
        return false;
    }

    private static EstudoMantelHaenszel retornaEstudoComCincoDecimosAMais(EstudoMantelHaenszel estudo) {
        estudo.setVerdadeiroNegativo(somaCincoDecimo(estudo.getVerdadeiroNegativo()));
        estudo.setVerdadeiroPositivo(somaCincoDecimo(estudo.getVerdadeiroPositivo()));
        estudo.setFalsoNegativo(somaCincoDecimo(estudo.getFalsoNegativo()));
        estudo.setFalsoPositivo(somaCincoDecimo(estudo.getFalsoPositivo()));
        return estudo;
    }

    private static BigDecimal somaCincoDecimo(BigDecimal valor) {
        return valor.add(new BigDecimal("0.5"));
    }
}
