using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Drools.Common.Entities;
using GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer;
using System.Threading;
using GigaSpaces.Core;
using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events.Polling.Receive;
using GigaSpaces.XAP.Events;

namespace Drools.Client.StreamInput
{
    class GAPollContainer
    {
        private PollingEventListenerContainer<GenericAction> pollingEventListenerContainer;
        ISpaceProxy spaceProxy;

        public GAPollContainer(ISpaceProxy input ) {
            //spaceProxy = input;
            spaceProxy = GigaSpacesFactory.FindSpace("jini://*/*/space?groups=gigaspaces-10.0.1-XAPPremium-ga");
        }

        public void Start()
        {
            Console.Out.WriteLine("GAPollContainer:Start PollContainerStarter");
            pollingEventListenerContainer = new PollingEventListenerContainer<GenericAction>(spaceProxy);


            TakeReceiveOperationHandler<GenericAction> receiveHandler = new TakeReceiveOperationHandler<GenericAction>();
            receiveHandler.NonBlocking = true;
            receiveHandler.NonBlockingFactor = 10;
            pollingEventListenerContainer.ReceiveOperationHandler = receiveHandler;

            pollingEventListenerContainer.Template = new SqlQuery<GenericAction>("state=" + (int) Convert.ChangeType(FactState.RULE_PROCESSED, new FactState().GetTypeCode()));
            Console.Out.WriteLine("GAPollContainer:Start template " + pollingEventListenerContainer.Template);
            Console.Out.WriteLine("GAPollContainer:Start url " + pollingEventListenerContainer.SpaceProxy.Url);
            pollingEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<GenericAction, GenericAction>(GAEventHandler).WriteBackDataEventHandler;
            pollingEventListenerContainer.ContainerExceptionOccured += ExceptionHandler;

            pollingEventListenerContainer.Start();
        }

        public void Dispose()
        {
            Console.WriteLine("GAPollContainer dispose");
            pollingEventListenerContainer.Dispose();
        }

        public GenericAction GAEventHandler(IEventListenerContainer<GenericAction> sender, DataEventArgs<GenericAction> e)
        {
            GenericAction data = e.Data;
            Console.Out.WriteLine("Action : " + data.ToString());
            data.State = FactState.REMOTE_PROCESSED;
            return data;
        }

        public void ExceptionHandler(object sender, ContainerExceptionEventArgs e)
        {
            Console.WriteLine("Container Name: " + ((IEventListenerContainer<GenericAction>)sender).Name);
            Console.WriteLine(e.Exception.Message);
        }

        public void IsActive()
        {
            Console.WriteLine("GAPollContainer polling contianer state is: " + pollingEventListenerContainer.IsActive);
        }

    }
}
