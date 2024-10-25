package org.example.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.example.StaticFunctions;

public class InitiateCalcsBehaviour extends OneShotBehaviour {
    private final double x = StaticFunctions.INITIAL_X;
    private final double step = StaticFunctions.STEP;
    @Override
    public void action() {
        System.out.println("Создан агент-инициатор " + myAgent.getLocalName());

        // Формируем сообщение для начальной инициации расчёта
        ACLMessage initiateMsg = new ACLMessage(ACLMessage.PROPOSE);
        initiateMsg.setProtocol("initiate");
        initiateMsg.setContent(x + "," + step);

        AID receiver = new AID("func1", false);
        initiateMsg.addReceiver(receiver);

        // Отправка сообщения для назначения инициатора
        myAgent.send(initiateMsg);

        System.out.println("Агент-инициатор " + myAgent.getLocalName() + " отправил команду на начало расчёта.");
    }
}
