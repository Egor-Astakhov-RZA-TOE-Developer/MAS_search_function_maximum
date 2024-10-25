package org.example.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.FunctionAgent;
import org.example.StaticFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ReceiveMsgToCalculateFunc extends Behaviour {
    private final int[] agentIndexes = new int[] {1, 2, 3};
    private int initiatorIndex = 2;
    private final double precision = StaticFunctions.PRECISION;
    private final double[] resultMass = new double[]{0, 0, 0, 0, StaticFunctions.STEP};
    private boolean isDone = false;

    private int countOfReceivedMessages = 0;
    private int countOfIterations = 0;

    @Override
    public void action() {

        MessageTemplate mt = MessageTemplate.MatchProtocol("result");
        ACLMessage msg = myAgent.receive(mt);

        if(msg != null){
            System.out.println("Агент-инициатор " + myAgent.getLocalName() +
                    " получил результаты расчёта от агента " + msg.getSender().getLocalName());
            String[] msgContentSplit = msg.getContent().split(",");
            List<Double> calculatedValues = Arrays.stream(msgContentSplit).map(Double::parseDouble).toList();
            resultMass[0] +=  calculatedValues.get(0);
            resultMass[1] +=  calculatedValues.get(1);
            resultMass[2] +=  calculatedValues.get(2);
            resultMass[3] = calculatedValues.get(3);
            resultMass[4] = calculatedValues.get(4);

            countOfReceivedMessages++;
        }
        else if (countOfReceivedMessages == 3){
            countOfIterations++;
            countOfReceivedMessages = 0;

            double fx = resultMass[0];
            double fLeftStep = resultMass[1];
            double fRightStep = resultMass[2];
            double x = resultMass[3];
            double step = resultMass[4];

            System.out.println("Получены все результаты расчёта");
            System.out.println("Промежуточный результат: x = " + x + ", step = " + step +
                    ", f(x) = " + fx + ", f(x-step) = " + fLeftStep + ", f(x+step) = " + fRightStep);

            double maxF = Math.max(fx, Math.max(fLeftStep, fRightStep));

            if(step < precision){

                System.out.println("Найдено решение (максимум функции): x = " + x + ", f(x) = " + fx);
                System.out.println("Общее число шагов поиска: " + countOfIterations);
                isDone = true;

            } else{
                if(maxF == fx){
                    step /= 2;
                    System.out.println("Уменьшен шаг поиска: step = " + step);
                } else if(maxF == fLeftStep){
                    x -= step;
                    System.out.println("Изменён x влево: x = " + x);
                }
                else {
                    x += step;
                    System.out.println("Изменён x вправо: x = " + x);
                }

                ACLMessage newXMessage = new ACLMessage(ACLMessage.PROPOSE);
                newXMessage.setContent(x + "," + step);
                System.out.println("Новая инициация итерации расчета агентом " + myAgent.getLocalName());
                newXMessage.setProtocol("initiate");

                if(initiatorIndex > 3) initiatorIndex = 1;

                AID receiver = new AID("func" + initiatorIndex++, AID.ISLOCALNAME);
                newXMessage.addReceiver(receiver);
                myAgent.send(newXMessage);

                resultMass[0] = 0;
                resultMass[1] = 0;
                resultMass[2] = 0;
                resultMass[3] = 0;
                resultMass[4] = StaticFunctions.STEP;
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }

}


