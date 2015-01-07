using System;
using Drools.Common;
using Drools.Common.Entities;
using Drools.Common.Utils;
using GigaSpaces.Core;
using System.Threading;
using System.Collections;
using System.Collections.Generic;
using GigaSpaces.Core.Document;

namespace Drools.Client.StreamInput
{
    class Program
    {
        static void Main(string[] args)
        {
            ISpaceProxy spaceProxy = null;
            try
            {
                spaceProxy = GigaSpacesFactory.FindSpace("jini://*/*/space?groups=gigaspaces-10.0.1-XAPPremium-ga");
            }
            catch (Exception ex)
            {
                Console.Out.WriteLine(ex.StackTrace);
                spaceProxy = GigaSpacesFactory.FindSpace("jini://*/*/space?groups=gigaspaces-10.0.1-XAPPremium-ga");
            }
        }
    }
}
