/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;

/**
 *
 * @author ederm
 */
public class EstudosBD {

    private List<EstudoMantelHaenszel> estudos;

    public List<EstudoMantelHaenszel> addEstudos() {
        estudos = new ArrayList<>();

        estudos.add(new EstudoMantelHaenszel("Bednarek et al., 1997", new BigDecimal("99"), new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("14")));
        estudos.add(new EstudoMantelHaenszel("Carey et al., 1996", new BigDecimal("82"), new BigDecimal("0.5"), new BigDecimal("23"), new BigDecimal("20")));
        estudos.add(new EstudoMantelHaenszel("He et al., 2004", new BigDecimal("47"), new BigDecimal("25"), new BigDecimal("5"), new BigDecimal("73")));
        estudos.add(new EstudoMantelHaenszel("Hiyama et al., 1996", new BigDecimal("130"), new BigDecimal("13"), new BigDecimal("10"), new BigDecimal("83")));
        return estudos;
    }

    public List<EstudoMantelHaenszel> addUmEstudo() {
        estudos = new ArrayList<>();

        estudos.add(new EstudoMantelHaenszel("Bednarek et al., 1997", new BigDecimal("99"), new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("14")));
        return estudos;
    }

    public List<EstudoMantelHaenszel> addDoisEstudo() {
        estudos = new ArrayList<>();

        estudos.add(new EstudoMantelHaenszel("Bednarek et al., 1997", new BigDecimal("99"), new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("14")));
        estudos.add(new EstudoMantelHaenszel("Carey et al., 1996", new BigDecimal("82"), new BigDecimal("0.5"), new BigDecimal("23"), new BigDecimal("20")));
        return estudos;
    }
}
