package tensors;

import functions.GeneralFunction;
import functions.endpoint.Variable;
import functions.unitary.trig.normal.Sin;

import static tensors.ArrayTensor.*;
import static tools.DefaultFunctions.*;
import static tools.DefaultFunctions.square;

@SuppressWarnings("NonAsciiCharacters")
public class DefaultSpaces {

    public static final Variable G = new Variable("G");
    public static final Variable M = new Variable("M");
    public static final Variable t = new Variable("t");
    public static final Variable x = new Variable("x");
    public static final Variable y = new Variable("y");
    public static final Variable z = new Variable("z");
    public static final Variable r = new Variable("r");
    public static final Variable r_s = new Variable("\\r_s");
    public static final Variable θ = new Variable("θ");
    public static final Variable φ = new Variable("φ");
    public static final Variable ψ = new Variable("ψ");

    public static final Space cartesian2d = Space.spaceFromDiagonal(new String[]{"x", "y"}, ONE, ONE);

    public static final Space cartesian3d = Space.spaceFromDiagonal(new String[]{"x", "y", "z"}, ONE, ONE, ONE);

    public static final Space polar = Space.spaceFromDiagonal(new String[]{"r", "θ"}, ONE, square(r));

    public static final Space s2 = Space.spaceFromDiagonal(new String[]{"θ", "φ"}, ONE, reciprocal(square(new Sin(θ))));




}
