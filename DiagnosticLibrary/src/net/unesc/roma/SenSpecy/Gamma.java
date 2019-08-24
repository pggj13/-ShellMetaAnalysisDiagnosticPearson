package net.unesc.roma.SenSpecy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Silvestre
 */
public class Gamma {
    
    public static final double MAXGAM = 171.624376956302725D;
    
    
    /** Machine epsilon.  Smallest double floating point number
	 *	such that (1 + MACHEPS) > 1 .
	 */

	public static final double MACHEPS = determineMachineEpsilon();
        
        
        	/* LN( Sqrt( 2 * PI ) ) */
        public static final double	LNSQRT2PI	=
		Math.log( Math.sqrt( 2.0D * Math.PI ) );

	

	/**	Calculate log of Gamma function.
	 *
	 *	@param	y		Gamma distribution parameter.
	 *
	 *	@return			Log gamma for specified parameter.
	 *					The value Double.POSITIVE_INFINITY is returned for
	 *					y <= 0.0 or when overflow would occur.
	 *
	 *	<p>
	 *	Minimax polynomial approximations are used over the
	 *	intervals [-inf,0], [0,.5], [.5,1.5], [1.5,4.0],
	 *	[4.0,12.0], [12.0,+inf].
	 *	</p>
	 *
	 *	<p>
	 *	See Hart et al, "Computer Approximations",
	 *	Wiley(1968), p. 130F, and also
	 *	Cody and Hillstrom, "Chebyshev approximations for
	 *	the natural logarithm of the Gamma function",
	 *	Mathematics of Computation, 21, April, 1967, P. 198F.
	 *	</p>
	 *
	 *	<p>
	 *	The minimax coefficients for y > 12 come from Hart et al.
	 *	The other coefficients come from unpublished work by
	 *	W. J. Cody and L. Stoltz at Argonne National Laboratory.
	 *	This java code is a fairly straightforward translation of
	 *	the freely available Fortran function ALGAMA by Cody and Stolz.
	 *	</p>
	 */

	public static double logGamma( double y )
	{
	    final double PNT68	= 0.6796875D;

		/*	Largest double value for which log(gamma(x)) can be represented.
		 */

		final double XBIG	= 2.5E305;

		/*	Approximate fourth root of XBIG.
		 */

		final double FRTBIG	= 2.25E76;

		/*	Numerator and denominator coefficients for rational minimax
	 	 *	approximation over (0.5,1.5).
		 */

		final double d1	=	-5.772156649015328605195174E-1;

    	final double[] p1 =
	    {
			4.945235359296727046734888E0,
			2.018112620856775083915565E2,
			2.290838373831346393026739E3,
			1.131967205903380828685045E4,
			2.855724635671635335736389E4,
			3.848496228443793359990269E4,
			2.637748787624195437963534E4,
			7.225813979700288197698961E3
    	};

	    final double[] q1 =
    	{
			6.748212550303777196073036E1,
			1.113332393857199323513008E3,
			7.738757056935398733233834E3,
			2.763987074403340708898585E4,
			5.499310206226157329794414E4,
			6.161122180066002127833352E4,
			3.635127591501940507276287E4,
			8.785536302431013170870835E3
    	};

		/*	Numerator and denominator coefficients for rational minimax
	 	 *	Approximation over (1.5,4.0).
	 	 */

		final double d2	=	4.227843350984671393993777E-1;

		final double[] p2 =
		{
			4.974607845568932035012064E0,
			5.424138599891070494101986E2,
			1.550693864978364947665077E4,
			1.847932904445632425417223E5,
			1.088204769468828767498470E6,
			3.338152967987029735917223E6,
			5.106661678927352456275255E6,
			3.074109054850539556250927E6
		};

		final double[] q2 =
		{
			1.830328399370592604055942E2,
			7.765049321445005871323047E3,
			1.331903827966074194402448E5,
			1.136705821321969608938755E6,
			5.267964117437946917577538E6,
			1.346701454311101692290052E7,
			1.782736530353274213975932E7,
			9.533095591844353613395747E6
		};

		/*	Numerator and denominator coefficients for rational minimax
 	 	 *	Approximation over (4.0,12.0).
 	 	 */

		final double d4	= 1.791759469228055000094023D;

		final double[] p4 =
		{
			1.474502166059939948905062E4,
			2.426813369486704502836312E6,
			1.214755574045093227939592E8,
			2.663432449630976949898078E9,
			2.940378956634553899906876E10,
			1.702665737765398868392998E11,
			4.926125793377430887588120E11,
			5.606251856223951465078242E11
		};

		final double[] q4 =
		{
			2.690530175870899333379843E3,
			6.393885654300092398984238E5,
			4.135599930241388052042842E7,
			1.120872109616147941376570E9,
			1.488613728678813811542398E10,
			1.016803586272438228077304E11,
			3.417476345507377132798597E11,
			4.463158187419713286462081E11
		};

		/*	Coefficients for minimax approximation over (12, INF).
 		 */

		final double[] c =
		{
			-1.910444077728E-03,
			8.4171387781295E-04,
			-5.952379913043012E-04,
			7.93650793500350248E-04,
			-2.777777777777681622553E-03,
			8.333333333333333331554247E-02,
			5.7083835261E-03
		};

		double	res;
		double	corr;
		double	ysq;
		double	xden;
		double	xnum;
		double	xm1;
		double	xm2;
		double	xm4;
								//	If argument is not a number,
								//	return that.

		if ( Double.isNaN( y ) )
		{
		    return y;
    	}

		if ( ( y > 0.0 ) && ( y <= XBIG ) )
		{
			if ( y <= MACHEPS )
			{
				res = -Math.log( y );
			}
			else if ( y <= 1.5D )
			{
								// MACHEPS < x <= 1.5

				if ( y < PNT68 )
				{
					corr	= -Math.log( y );
					xm1		= y;
				}
				else
				{
					corr	= 0.0D;
					xm1		= ( y - 0.5D ) - 0.5D;
				}

				if ( ( y <= 0.5D ) || ( y >= PNT68 ) )
				{
					xden	= 1.0D;
					xnum	= 0.0D;

					for ( int i = 0; i < 8; i++ )
					{
						xnum	= xnum * xm1 + p1[ i ];
						xden	= xden * xm1 + q1[ i ];
					}

					res	= corr + ( xm1 * ( d1 + xm1 * ( xnum / xden ) ) );
				}
				else
				{
					xm2		= ( y - 0.5D ) - 0.5D;
					xden	= 1.0D;
					xnum	= 0.0D;

					for ( int i = 0; i < 8; i++ )
					{
						xnum	= xnum * xm2 + p2[ i ];
						xden	= xden * xm2 + q2[ i ];
					}

					res	= corr + xm1 * ( d2 + xm2 * ( xnum / xden ) );
				}
			}
        	else if ( y <= 4.0D )
        	{
								// .5 .LT. X .LE. 4.0

				xm2		= y - 2.0D;
				xden	= 1.0D;
				xnum	= 0.0D;

				for ( int i = 0; i < 8; i++ )
				{
					xnum	= xnum * xm2 + p2[ i ];
					xden	= xden * xm2 + q2[ i ];
				}

				res	= xm2 * ( d2 + xm2 * ( xnum / xden ) );
			}
			else if ( y <= 12.0D )
			{
								//	4.0 < x < 12.0

				xm4		= y - 4.0D;
				xden	= -1.0D;
				xnum	= 0.0D;

				for ( int i = 0; i < 8; i++ )
				{
					xnum	= xnum * xm4 + p4[ i ];
					xden	= xden * xm4 + q4[ i ];
				}

				res	= d4 + xm4 * ( xnum / xden );
			}
			else
			{
								//	Evaluate for argument .GE. 12.0
				res	= 0.0D;

				if ( y <= FRTBIG )
				{
					res	= c[ 6 ];

					ysq	= y * y;

					for ( int i = 0; i < 6; i++ )
					{
						res	= res / ysq + c[ i ];
					}
				}

				res		= res / y;
				corr	= Math.log( y );

				res		= res + LNSQRT2PI - 0.5D * corr;
				res		= res + y * ( corr - 1.0D );
			}
		}
		else
		{
								// Return largest possible positive value
								// for bad arguments.

			res = Double.POSITIVE_INFINITY;
//			res = Double.MAX_VALUE;
		}

		return res;
	}

	/** Cumulative probability density function for the incomplete gamma function.
	 *
	 *	@param	x		Gamma distribution value
	 *	@param	alpha	Shape parameter
	 *	@param	dPrec	Digits of precision desired (1 < dPrec < Constants.MAXPREC)
	 *	@param	maxIter	Maximum number of iterations allowed
	 *
	 *	@return			Cumulative probability density function value
	 *
	 *	@throws			IllegalArgumentException
	 *						if x < 0 or alpha <= 0
	 *
	 *	<p>
	 *	Either an infinite series summation or a continued fraction
	 *	approximation is used, depending upon the argument range.
	 *	See Bhattacharjee GP (1970) The incomplete gamma integral.
	 *	Applied Statistics, 19: 285-287 (AS32) .  The result is
	 *	accurate to about 14 decimal digits.
	 *  </p>
	 */

	

	/** Cumulative probability density function for the incomplete gamma function.
	 *
	 *	@param	x		Gamma distribution value
	 *	@param	alpha	Shape parameter
	 *
	 *	@return			Cumulative probability density function value
	 *
	 *	@throws			IllegalArgumentException
	 *						if x < 0 or alpha <= 0
	 *
	 *	<p>
	 *	Either an infinite series summation or a continued fraction
	 *	approximation is used, depending upon the argument range.
	 *	See Bhattacharjee GP (1970) The incomplete gamma integral.
	 *	Applied Statistics, 19: 285-287 (AS32) .  The result is
	 *	accurate to about 14 decimal digits.
	 *  </p>
	 */

	

	/**	Make class non-instantiable but inheritable.
	 */
        
        public static double determineMachineEpsilon()
    {
        double d1 = 1.3333333333333333D;
        double d3;
        double d4;

        for( d4 = 0.0D; d4 == 0.0D; d4 = Math.abs( d3 - 1.0D ) )
        {
            double d2 = d1 - 1.0D;
            d3 = d2 + d2 + d2;
        }

        return d4;
    }

	protected Gamma()
	{
	}
}

/*
 * <p>
 * Copyright &copy; 2004-2011 Northwestern University.
 * </p>
 * <p>
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA.
 * </p>
 */
    

