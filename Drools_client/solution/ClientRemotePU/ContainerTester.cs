using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Drools.Common.Entities;
using GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer;
using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events.Polling.Receive;
using GigaSpaces.XAP.Events;
using GigaSpaces.Core;

namespace ClientRemotePU
{
    [BasicProcessingUnitComponent(Name = "ContainerTester")]
    class ContainerTester
    {
        
        private PollingEventListenerContainer<GenericAction> pollingEventListenerContainer;


        [ContainerInitialized]
        public void Initialize(BasicProcessingUnitContainer container)
        {
        ISpaceProxy spaceProxy = container.GetSpaceProxy("space");
        pollingEventListenerContainer = new PollingEventListenerContainer<GenericAction>(spaceProxy);
        Console.Out.WriteLine("ContainerTester:Start url " + pollingEventListenerContainer.SpaceProxy.Url);

        TakeReceiveOperationHandler<GenericAction> receiveHandler = new TakeReceiveOperationHandler<GenericAction>();
        receiveHandler.NonBlocking = true;
        receiveHandler.NonBlockingFactor = 10;
        pollingEventListenerContainer.ReceiveOperationHandler = receiveHandler;

        GenericAction ga = new GenericAction();
        ga.State = FactState.RULE_PROCESSED;
        ga.Fact = new WagerFact();
        // Console.Out.WriteLine("ContainerTester:Start template " + ga.ToString());
        pollingEventListenerContainer.Template = ga;
        // pollingEventListenerContainer.Template = new SqlQuery<GenericAction>("state=2");
        Console.Out.WriteLine("ContainerTester:Start template " + pollingEventListenerContainer.Template);
        
        pollingEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<GenericAction, GenericAction>(GAEventHandler).WriteBackDataEventHandler;
        pollingEventListenerContainer.ContainerExceptionOccured += ExceptionHandler; 
        }

        public void Dispose()
        {
            pollingEventListenerContainer.Dispose();
        }

        public GenericAction GAEventHandler(IEventListenerContainer<GenericAction> sender, DataEventArgs<GenericAction> e)
        {
            GenericAction data = e.Data;
            Console.Out.WriteLine("ContainerTester Action : " + data.ToString());
            data.State = FactState.REMOTE_PROCESSED;
            return data;
        }

        public void ExceptionHandler(object sender, ContainerExceptionEventArgs e)
        {
            Console.WriteLine("Container Name: " + ((IEventListenerContainer<GenericAction>)sender).Name);
            Console.WriteLine(e.Exception.Message);
        }
        
    }
}
