/*	Please see the license information at the end of this file. */
package net.unesc.roma.SenSpecy;

/** Interface for a monadic (single argument) function.
 */

public interface MonadicFunction
{
	/** Evaluates a monadic scalar function.
	 *
	 *	@param	x	Value at which function is to be evaluated.
	 *	@return		Result of evaluating function at x.
	 */

	public double f( double x );
}


