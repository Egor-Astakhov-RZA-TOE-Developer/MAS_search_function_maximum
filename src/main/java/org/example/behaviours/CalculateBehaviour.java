package org.example.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.FunctionAgent;
import org.example.StaticFunctions;

public class CalculateBehaviour extends Behaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchProtocol("calculate");
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null){
            System.out.println("\nРасчёт у агента -> " + myAgent.getLocalName());

            String[] msgString = msg.getContent().split(",");
            double x = Double.parseDouble(msgString[0]);
            double step = Double.parseDouble(msgString[1]);

            double[] res = func(x, step);

            // Формирование ответа с результатами расчёта для агента-инициатора
            ACLMessage resMsg = msg.createReply();
            resMsg.setContent(res[0] + "," + res[1] +"," + res[2] + "," + x + "," + step);
            resMsg.setProtocol("result");
            // Отправка сообщения
            myAgent.send(resMsg);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    private double[] func(double x, double step){
        double[] res = new double[3];
        if (myAgent.getLocalName().equals("func1")){
            res[0] = StaticFunctions.f1(x);
            res[1] = StaticFunctions.f1(x - step);
            res[2] = StaticFunctions.f1(x + step);
        }else if (myAgent.getLocalName().equals("func2")){
            res[0] = StaticFunctions.f2(x);
            res[1] = StaticFunctions.f2(x - step);
            res[2] = StaticFunctions.f2(x + step);
        } else{
            res[0] = StaticFunctions.f3(x);
            res[1] = StaticFunctions.f3(x - step);
            res[2] = StaticFunctions.f3(x + step);
        }

        return res;
    }
}
