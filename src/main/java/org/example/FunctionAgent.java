package org.example;

import jade.core.Agent;
import org.example.behaviours.CalculateBehaviour;
import org.example.behaviours.InitiateCalcsBehaviour;
import org.example.behaviours.ReceiveMsgToCalculateFunc;
import org.example.behaviours.SendMsgToCalculateFunc;



public class FunctionAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();
        if(getLocalName().equals("func1")) this.addBehaviour(new InitiateCalcsBehaviour());
        this.addBehaviour(new SendMsgToCalculateFunc());
        this.addBehaviour(new CalculateBehaviour());
        this.addBehaviour(new ReceiveMsgToCalculateFunc());
    }
}

