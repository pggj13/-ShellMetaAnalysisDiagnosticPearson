/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.SenSpecy;

import java.math.BigDecimal;

/**
 *
 * @author Silvestre
 */
public class ClooperInterv {

    /**
     * This method computes the log of the gamma function.
     *
     * @param x a positive number
     * @return the log of the gamma function at x
     */
    public static double logGamma(double x) {
        double coef[] = {76.18009173, -86.50532033, 24.01409822, -1.231739516, 0.00120858003, -0.00000536382};
        double step = 2.50662827465, fpf = 5.5, t, tmp, ser, logGamma;
        t = x - 1;
        tmp = t + fpf;
        tmp = (t + 0.5) * Math.log(tmp) - tmp;
        ser = 1;
        for (int i = 1; i <= 6; i++) {
            t = t + 1;
            ser = ser + coef[i - 1] / t;
        }
        return tmp + Math.log(step * ser);
    }

    public static double incompleteBetai(
            double x,
            double alpha,
            double beta
    )
            throws IllegalArgumentException {
        double epsz;
        double a;
        double b;
        double c;
        double f;
        double fx;
        double apb;
        double zm;
        double alo;
        double ahi;
        double blo;
        double bhi;
        double bod;
        double bev;
        double zm1;
        double d1;
        double aev;
        double aod;
        double cPrec;
        double result;

        int nTries;
        int iter;
        int maxIter;

        boolean qSwap;
        boolean qDoit;
        boolean qConv;

        a = alpha;
        b = beta;
        qSwap = false;
        result = -1.0D;
        qDoit = true;
        maxIter = 200;
        /* Check arguments */
 /* Error if:       */
 /*    X <= 0       */
 /*    A <= 0       */
 /*    B <= 0       */
        if (x <= 0.0D) {
            throw new IllegalArgumentException("x <= 0.0");
        }

        if (a <= 0.0D) {
            throw new IllegalArgumentException("a <= 0.0");
        }

        if (b <= 0.0D) {
            throw new IllegalArgumentException("b <= 0.0");
        }

        result = 1.0D;
        /* If X >= 1, return 1.0 as prob */

        if (x >= 1.0D) {
            return result;
        }

        /* If x > a / ( a + b ) then swap */
 /* A, b para eval mais eficiente. */
        if (x > (a / (a + b))) {
            x = 1.0 - x;
            a = beta;
            b = alpha;
            qSwap = true;
        };

        /* Verifique se há valores extremos */
        if ((x == a) || (x == b)) {
        } else if (a == ((b * x) / (1.0 - x))) {
        } else if (Math.abs(a - (x * (a + b))) <= 100) {
        } else {
            c = Gamma.logGamma(a + b) + a * Math.log(x)
                    + b * Math.log(1.0 - x) - Gamma.logGamma(a)
                    - Gamma.logGamma(b) - Math.log(a - x * (a + b));

            if ((c < -36.0D) && qSwap) {
                return result;
            }

            result = 0.0D;

            if (c < -180.0D) {
                return result;
            }
        }
        /* Configurar expansão contínua fração Avaliação. */
        apb = a + b;
        zm = 0.0D;
        alo = 0.0D;
        bod = 1.0D;
        bev = 1.0D;
        bhi = 1.0D;
        blo = 1.0D;

        ahi
                = Math.exp(
                        Gamma.logGamma(apb) + a * Math.log(x)
                        + b * Math.log(1.0D - x) - Gamma.logGamma(a + 1.0D)
                        - Gamma.logGamma(b));

        f = ahi;
        iter = 0;
        /* Continuação circuito fração começa aqui.  Avaliação continua até no máximo 
   Iterações forem excedidos ou 
  Convergência alcançada. */
        qConv = false;

        do {
            fx = f;

            zm1 = zm;
            zm = zm + 1.0D;
            d1 = a + zm + zm1;
            aev = -(a + zm1) * (apb + zm1) * x / d1 / (d1 - 1.0D);
            aod = zm * (b - zm) * x / d1 / (d1 + 1.0D);
            alo = bev * ahi + aev * alo;
            blo = bev * bhi + aev * blo;
            ahi = bod * alo + aod * ahi;
            bhi = bod * blo + aod * bhi;

            if (Math.abs(bhi) < Double.MIN_VALUE) {
                bhi = 0.0D;
            }

            if (bhi != 0.0D) {
                f = ahi / bhi;
                qConv = (Math.abs((f - fx) / f) < 100);
            };
            iter++;
        } while ((iter <= maxIter) && (!qConv));

        /* Arrive here when convergence    */
 /* achieved, or maximum iterations */
 /* exceeded.                       */
        if (qSwap) {
            result = 1.0D - f;
        } else {
            result = f;
        }
        return result;
    }

    public static boolean bracketRoot(
            double bracket[],
            MonadicFunction function,
            int maxIter,
            double expansionFactor
    ) {
        // Verifique se há suporte inicial válida.

        if ((bracket == null) || (bracket.length < 2)
                || (bracket[0] == bracket[1])) {
            throw new IllegalArgumentException("initial bracket bad");
        }
        // Calcule a função na fase inicial
        // pontos de suporte esquerdo e direito.

        double fLeft = function.f(bracket[0]);
        double fRight = function.f(bracket[1]);

        for (int iter = 1; iter <= maxIter; iter++) {
            // Se a função esquerda e direita, valores são de sinal diferente, Um zero está entre colchetes entre eles.

            if ((fLeft * fRight) < 0.0) {
                return true;
            }

            // Determine which end of the bracket
            // to expand.
            if (Math.abs(fLeft) < Math.abs(fRight)) {
                bracket[0]
                        = expansionFactor * (bracket[0] - bracket[1]);

                fLeft = function.f(bracket[0]);
            } else {
                bracket[1]
                        = expansionFactor * (bracket[1] - bracket[0]);

                fRight = function.f(bracket[1]);
            }
        }
        return false;
    }

    public static BigDecimal incompleteBetaInverse(
            final double p,
            final double alpha,
            final double beta
    )
            throws IllegalArgumentException {
        /* Check validity of arguments */
        if (alpha <= 0.0D) {
            throw new IllegalArgumentException("alpha<=0");
        }

        if (beta <= 0.0D) {
            throw new IllegalArgumentException("beta<=0");
        }

        if ((p > 1.0D) || (p < 0.0D)) {
            throw new IllegalArgumentException("p < 0 or p > 1");
        }
        /* Check for P = 0 or 1        */

        if ((p == 0.0D) || (p == 1.0D)) {
            return BigDecimal.ZERO;
            /* this is bad */
        }
        double eps = Math.pow(10.0D, -2 * 6);
        int maxIter = 100;

        /* Create function for evaluating */
        /* zero root. */
        MonadicFunction function
                = new MonadicFunction() {
            @Override
            public double f(double x) {

                return p - incompleteBetai(x, alpha, beta);
            }
        };
        /* Definir o suporte inicial para o valor de raiz. */

        double[] bracket = new double[]{eps, 1.0D - eps};

        /* Certifique-se de suporte contém valor. */
        if (bracketRoot(bracket, function, maxIter, 1.6D)) {
            /* Use o método de Brent para procurar */
            /* root usando função beta CDF incompleto. */

            return BigDecimal.valueOf(Brent.brent(
                    bracket[0],
                    bracket[1],
                    eps,
                    maxIter,
                    function
            ));
        } else {
            throw new ArithmeticException("Unable to bracket value");
        }
    }

    public static BigDecimal fInverse(BigDecimal p, BigDecimal dfn, BigDecimal dfd)
            throws IllegalArgumentException {
        BigDecimal result = null;
        double result1;
        BigDecimal result2;

        if ((dfn.compareTo(BigDecimal.ZERO) > 0) && (dfd.compareTo(BigDecimal.ZERO) > 0)) {
            if ((p.compareTo(BigDecimal.ZERO) >= 0) && (p.compareTo(BigDecimal.ONE) <= 1)) {
                double prob = p.doubleValue();
                double numer = dfn.doubleValue();
                double den = dfd.doubleValue();
                result = (incompleteBetaInverse(1.0D - prob, numer / 2.0D, den / 2.0D));

                if ((result.compareTo(BigDecimal.ZERO) >= 0) && (result.compareTo(BigDecimal.ONE) < 1.)) {
                    result1 = result.doubleValue();

                    result = BigDecimal.valueOf(result1 * den / (numer * (1.0D - result1)));

                } else {
                    throw new ArithmeticException(
                            "inverse incomplete beta evaluation failed");
                }
            } else {
                throw new IllegalArgumentException("p < 0 or p > 1");
            }
        } else {
            //throw new IllegalArgumentException("dfn or dfd <= 0");
        }
        return result;
    }
}
