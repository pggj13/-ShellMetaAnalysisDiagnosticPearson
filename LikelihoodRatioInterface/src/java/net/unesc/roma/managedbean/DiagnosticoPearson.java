/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean;

import java.util.ArrayList;
import java.util.List;
import net.unesc.roma.managedbean.odds.ratio.mantelHaenszel.EstudoMantelHaenszel;

/**
 *
 * @author ederm
 */
public class DiagnosticoPearson {
    
    public DiagnosticoPearson() {
        estudosValidos = new ArrayList<>();
    }
    private static List<EstudoMantelHaenszel> estudosValidos = new ArrayList<>();;

    public List<EstudoMantelHaenszel> getEstudosValidos() {
        return estudosValidos;
    }

    public void setEstudosValidos(List<EstudoMantelHaenszel> estudosValidos) {
        this.estudosValidos = estudosValidos;
    }
}
