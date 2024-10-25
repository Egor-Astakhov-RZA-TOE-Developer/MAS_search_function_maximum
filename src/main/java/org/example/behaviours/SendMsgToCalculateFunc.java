package org.example.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SendMsgToCalculateFunc extends Behaviour {

    @Override
    public void action() {
        // Фильтрация и получение сообщения для назначения инициатора
        MessageTemplate mt = MessageTemplate.MatchProtocol("initiate");
        ACLMessage receiveMsg = myAgent.receive(mt);

        if(receiveMsg != null){
            System.out.println("Агент " + myAgent.getLocalName() + " получил команду на отправку агентам сообщений о расчёте от агента " + receiveMsg.getSender().getLocalName());
            // Формирование сообщения для расчёта значения функций агентов
            ACLMessage calcMsg = getAclMessageForSendCommand(receiveMsg);

            // Отправка команды на проведение рассчёта
           /* try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/

            myAgent.send(calcMsg);

            System.out.println("Агент " + myAgent.getLocalName() + " отправил всем агентам команду \"calculate\"");
        }else {
            block();
        }
    }

    private static ACLMessage getAclMessageForSendCommand(ACLMessage receiveMsg) {
        ACLMessage calcMsg = new ACLMessage(ACLMessage.PROPOSE);
        calcMsg.setContent(receiveMsg.getContent());
        calcMsg.setProtocol("calculate");

        AID aidAgentFirst, aidAgentSecond, aidAgentThird;
        aidAgentFirst = new AID("func1", AID.ISLOCALNAME);
        aidAgentSecond = new AID("func2", AID.ISLOCALNAME);
        aidAgentThird = new AID("func3", AID.ISLOCALNAME);

        calcMsg.addReceiver(aidAgentFirst);
        calcMsg.addReceiver(aidAgentSecond);
        calcMsg.addReceiver(aidAgentThird);
        return calcMsg;
    }

    @Override
    public boolean done() {
        return false;
    }

    private double[] getFuncResult(ACLMessage request){
        String stringValues = request.getContent();
        String[] split = stringValues.split(",");
        double[] result = new double[split.length];
        for (int i = 0; i < split.length; i++) {
            result[i] = Double.parseDouble(split[i]);
        }
        return result;
    }
}
