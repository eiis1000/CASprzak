package tensors3;

import functions.endpoint.Variable;
import functions.unitary.trig.normal.Sin;

import static tensors3.ArrayTensor.*;
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

    public static final Space cartesian2d = new Space(new String[]{"x", "y"},
            tensor(
            new Object[][]{
                    {ONE, ZERO},
                    {ZERO, ONE}
            },
            false, false
            ),
            tensor(
                    new Object[][]{
                            {ONE, ZERO},
                            {ZERO, ONE}
                    },
                    true, true
            )
    );

    public static final Space s2 = new Space(new String[]{"θ", "φ"},
            tensor(
                    new Object[][]{
                            {ONE, ZERO},
                            {ZERO, square(new Sin(θ))}
                    },
                    false, false
            ),
            tensor(
                    new Object[][]{
                            {ONE, ZERO},
                            {ZERO, reciprocal(square(new Sin(θ)))}
                    },
                    true, true
            )
    );




}
