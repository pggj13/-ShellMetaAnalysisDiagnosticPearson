/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.SenSpecy;

import java.math.BigDecimal;
import static net.unesc.roma.SenSpecy.ClooperInterv.bracketRoot;
import static net.unesc.roma.SenSpecy.Gamma.MACHEPS;

/**
 *
 * @author Silvestre
 */
public class Brent {

    public static double brent(
            double x0,
            double x1,
            double tol,
            int maxIter,
            MonadicFunction function
    )
            throws IllegalArgumentException {
        final double EPS = MACHEPS / 2.0D;

        int iter;

        double min1;
        double min2;
        double fc;
        double p;
        double q;
        double r;
        double s;
        double tol1;
        double xm;

        double a = x0;
        double b = x1;
        double c = x1;
        double d = 0;
        double e = 0;
  // Avaliar a função em cada extremidade 
// O intervalo pretendia colocar entre parênteses uma raiz.

        double fa = function.f(a);
        double fb = function.f(b);

     // Comprovar se existe uma raiz neste intervalo.
 // Para que isso seja verdade, os valores da função
 // Na extremidade esquerda e direita do intervalo
 // Deve ter sinais diferentes. Se os sinais
 // São os mesmos, tente expandir o intervalo
 // Geometricamente e ver se podemos encontrar uma
 // Novo intervalo de sucessão da raiz.
        if (((fa > 0.0) && (fb > 0.0))
                || ((fa < 0.0) && (fb < 0.0))) {
            double[] bracket = new double[]{a, b};

            if (!bracketRoot(bracket, function, maxIter, 1.6)) {
               // Desistir se não podemos encontrar um novo intervalo
              // Bracketing de uma raiz.

                throw new IllegalArgumentException(
                        "Não é possível expandir intervalo [x0, x1] para conter raiz.");
            } //Use novo intervalo de variação.
            else {
                a = bracket[0];
                b = bracket[1];
                fa = function.f(a);
                fb = function.f(b);
            }
        }

        fc = fb;
        //	começar a interação.

        for (iter = 1; iter <= maxIter; iter++) {
            if (((fb > 0.0) && (fc > 0.0))
                    || ((fb < 0.0) && (fc < 0.0))) {
                //	Mudar o nome a, b, c de modo a que se encontra na raiz [b, c] .
                c = a;
                fc = fa;
                e = d = b - a;
            }

            if (Math.abs(fc) < Math.abs(fb)) {
                //	Mudar o nome a, b, c de modo a que b é melhor raiz estimativa.
                a = b;
                b = c;
                c = a;
                fa = fb;
                fb = fc;
                fc = fa;
            }
            // Pós atualizada iteração informações.
            // Verificação de tolerância de convergência.
            tol1 = 2.0 * EPS * Math.abs(b) + 0.5 * tol;
            xm = 0.5 * (c - b);
            /*
			if ( ( Math.abs( xm ) <= tol1 ) || ( fb == 0.0 ) )
			{
				return b;
			}
             */

            if ((Math.abs(e) >= tol1)
                    && (Math.abs(fa) > Math.abs(fb))) {
              // Tente interpolação linear ou
              // Método quadrática inversa

                s = fb / fa;

             // Apenas a & b são distintos: executar  Interpolação linear (por exemplo, Método secante)
                if (a == c) {
                    p = 2.0 * xm * s;
                    q = 1.0 - s;
                } //	a, b, c são distribuição:
                //	usa o quadrado inverso
                else {
                    q = fa / fc;
                    r = fb / fc;
                    p = s * (2.0 * xm * q * (q - r) - (b - a) * (r - 1.0));
                    q = (q - 1.0) * (r - 1.0) * (s - 1.0);
                }
                //	Determinar se a próxima estivativa é aceitavel.

                if (p > 0.0) {
                    q = -q;
                }

                p = Math.abs(p);

                min1 = 3.0 * xm * q - Math.abs(tol1 * q);
                min2 = Math.abs(e * q);

                if ((2.0 * p) < (min1 < min2 ? min1 : min2)) {
                    //	Aceitar  interpolação linear ou
                    //	inverso do quadrado estimado.
                    e = d;
                    d = p / q;
                } else {
                    //	Rejeição de interpolação linear ou
                    //	inverso do quadrado estimado: usado da bisseção
                    d = xm;
                    e = d;
                }
            } else {
                //	Bounds decreasing too slowly: use bisection.
                d = xm;
                e = d;
            }
            //	Update a, the previous best root estimate.
            a = b;
            fa = fb;
            //	Update b, the latest best root estimate.

            if (Math.abs(d) > tol1) {
                b += d;
            } else {
                b += (xm > 0.0 ? tol1 : -tol1);
            }

            fb = function.f(b);
        }
        // Retorna última estimativa quando o número 
        // De iterações é excedido. este valor  Não vai ser tão precisos como solicitado.
        return b;
    }
}
