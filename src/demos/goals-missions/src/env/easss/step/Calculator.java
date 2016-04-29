package easss.step;

import cartago.*;
import java.math.BigDecimal;

public class Calculator extends Artifact {

    void init() {
	}
    
    @OPERATION void computePi(int numDigits, OpFeedbackParam<String> res){
    	BigDecimal digits = CalcLib.computePiDigits(numDigits);
    	res.set(digits.toPlainString());
    }
}



