# Math-Parser
 A java parser for math equations stored as a string. The parser was made for my ui library, and it was the first time, that I did something like that. Therefore, it may not be the cleanest code nor the fastest, but it works (with the equations I tested it with)

## Features
The math parser includes a list of operators and some math functions:
<h4>Operators:</h4>
<ul>
<li>+ - Addition</li>
<li>- - Subtraction</li>
<li>* - Multiplication</li>
<li>/ - Division</li>
<li>a ^ b - Raising a to the power of b</li>
<li>% - Modulo</li>
</ul>
<h4>Functions:</h4>
<ul>
<li>pow(a, b) - Raising a to the power of b</li>
<li>root(a, b) - Taking the b'th root of a</li>
<li>cos(a), sin(a), tan(a) - Trigonometric functions. a in radians</li>
<li>acos(a), asin(a), atan(a) - Inverse of the trigonometric functions. Result in radians</li>
<li>toRadians(a) - Convert a from degree to radians</li>
<li>toDegree(a) - Convert a from radians to degree</li>
<li>abs(a) - Returns the absolute value of a</li>
<li>clamp(a,b,c) - Clamps c between a and b</li>
</ul>

More math functions will be added in the future.

Additionally, the parser can simplify a math expression, once it is parsed. <br>
It also supports variables, which can be called everything except the preset function keywords listed above. The value of a variable can then be set using setVariable(name, value).
