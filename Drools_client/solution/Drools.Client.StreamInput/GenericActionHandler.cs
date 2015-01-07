using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using GigaSpaces.Core;

namespace Drools.Client.StreamInput
{
    class GenericActionHandler
    {
        GAPollContainer handler;
        private Thread PollingThread;
        private bool _isRunning = true;
        

        public void Initialize(ISpaceProxy input)
        {
            Console.Out.WriteLine("GenericActionHandler:Initialize PollContainerStarter");

            handler = new GAPollContainer(input);

            PollingThread = new Thread(Track);

            PollingThread.Start();
        }

        private void Track()
        {
            Console.Out.WriteLine("GenericActionHandler:Track PollContainerStarter");
            handler.Start();
            while (_isRunning)
            {
                
            }
            handler.Dispose();
        }

        public void stop()
        {
            _isRunning = false;
        }

        public void IsActive()
        {
            handler.IsActive();
        }
    }
}
