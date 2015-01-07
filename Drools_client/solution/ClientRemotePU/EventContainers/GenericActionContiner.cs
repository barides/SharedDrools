using System;

using GigaSpaces.XAP.Events;
using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events.Polling.Receive;
using Drools.Common.Entities;

namespace Drools.Client.RemotePU.EventContainers
{
    [PollingEventDriven]
    [TransactionalEvent(TransactionType = TransactionType.Distributed)]
    public class GenericActionContiner
    {

        [ReceiveHandler]
        public IReceiveOperationHandler<GenericAction> ReceiveHandler()
        {
            TakeReceiveOperationHandler<GenericAction> receiveHandler = new TakeReceiveOperationHandler<GenericAction>();
            receiveHandler.NonBlocking = true;
            receiveHandler.NonBlockingFactor = 1;
            return receiveHandler;
        }

        [EventTemplate]
        public GenericAction GetActions()
        {
            Console.Out.WriteLine("GenericActionContiner has registered .... " );
            GenericAction template = new GenericAction();
            template.State = FactState.RULE_PROCESSED;
            return template;
        }

        [DataEventHandler]
        public GenericAction ProcessAction(GenericAction action)
        {
            Console.Out.WriteLine("Recieved Action: " + action);
            action.State = FactState.REMOTE_PROCESSED;
            return action;
        }
    }
}
