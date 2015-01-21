using System;
using Drools.Common;
using Drools.Common.Entities;
using Drools.Common.Utils;
using GigaSpaces.Core;
using System.Threading;
using System.Collections;
using System.Collections.Generic;
using GigaSpaces.Core.Document;
using System.Diagnostics;

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
            // Create new stopwatch
            Stopwatch stopwatch = new Stopwatch();


            int numberOfCustomers = 1000;
            int eventPerSecond = 500;
            int eventPerCustomer = 24;
            int emulatedCustomerID = 1;
            int maxFacts = numberOfCustomers * eventPerCustomer;
            int delayBetweenEventBulks = 1000;
            // Load metadata
            // Customer
            Customer[] customerMetadat = new Customer[numberOfCustomers];
            Dictionary<int, Customer> customerDictionary = new Dictionary<int, Customer>();
            for (int customerCnt = 1; customerCnt <= numberOfCustomers; customerCnt++)
            {
                customerMetadat[customerCnt - 1] = GenerateCustomer(customerCnt);
                customerDictionary.Add(customerCnt, customerMetadat[customerCnt - 1]);
            }
            ILeaseContext<Customer>[] customersMetadataLease = spaceProxy.WriteMultiple<Customer>(customerMetadat);
            Console.Out.WriteLine("Customer metadata has been written to the space");
            Thread.Sleep(3000);

            GenericActionHandler handler = new GenericActionHandler();
            handler.Initialize(spaceProxy);
            
            int counter = 1;

            for (int eventsCnt=1; eventsCnt <= eventPerCustomer;eventsCnt++)
            {
                WagerFact[] bulkfacts = new WagerFact[eventPerSecond];
                // int writeMultiple = counter + numberOfCustomers;
                // Begin timing
                stopwatch.Restart();
                for (int eventCounter = 1; eventCounter <= eventPerSecond; eventCounter++)
                {
                    WagerFact wagerFact = new WagerFact();
                    wagerFact.ActualAmount = (decimal)DataGenerator.GenerateDoubleAmount;
                    wagerFact.CorrolationID = Guid.NewGuid();
                    if (emulatedCustomerID >= numberOfCustomers)
                    {
                        emulatedCustomerID = 1;
                    }
                    else
                    {
                        emulatedCustomerID++;
                    }
                    int customerId = emulatedCustomerID;
                    wagerFact.CustomerID = customerId;
                    wagerFact.DateTime = DateTime.Now;
                    // wagerFact.Id = i.ToString();
                    // wagerFact.NetworkID = DataGenerator.GenerateIntID;
                    wagerFact.NetworkID = customerDictionary[customerId].NetworkId;
                    wagerFact.SkinID = DataGenerator.GenerateSkinID;
                    wagerFact.FactState = FactState.NEW;
                    wagerFact.Offering = DataGenerator.GenerateIntID;
                    wagerFact.OperationSourceApplication = DataGenerator.GenerateIntID;
                    wagerFact.RequestCurrencyCode = "USD";
                    wagerFact.FundsType = DataGenerator.GenerateFundsType;
                    wagerFact.RoutingId = customerDictionary[customerId].NetworkId;
                    wagerFact.RequestReference = Guid.NewGuid();
                    wagerFact.UpdateBalanceReason = 0;
                    DocumentProperties addons = new DocumentProperties();
                    addons.Add("Class","Platinium");
                    addons.Add("Zone", 4);
                    wagerFact.DocumentProperties = addons;
                    // Console.Out.WriteLine("WRITING FACT: " + wagerFact.ToString());
                    bulkfacts[eventCounter-1] = wagerFact;
                    // arrayCounter++;
                    // customerIdMocker++;
                }
                // Stop timing
                stopwatch.Stop();
                Console.WriteLine("Time elapsed: {0}.{1} Total:{2}", stopwatch.Elapsed.Seconds, stopwatch.Elapsed.Milliseconds, eventsCnt*numberOfCustomers);
                ILeaseContext<WagerFact>[] allfacts = spaceProxy.WriteMultiple<WagerFact>(bulkfacts);
                // counter++;
                Thread.Sleep(delayBetweenEventBulks - stopwatch.Elapsed.Milliseconds);
            }


            int timer = 0;
            while (timer < 60)
            {
                Console.Out.WriteLine("Running timer before polling container is disposed for 10 sec, time passed " + timer + " sec");
                Thread.Sleep(1000);
                handler.IsActive();
                timer++;
            }

            handler.stop();
            /*
            GenericActionHandler handler = new GenericActionHandler();
            handler.Initialize(spaceProxy);

            int timer = 0;
            while (timer < 10)
            {
                Console.Out.WriteLine("Running timer before polling container is disposed for 10 sec, time passed " + timer + " sec");
                Thread.Sleep(1000);
                handler.IsActive();
                timer++;
            }

            handler.stop();
            */


            /*
            int cnt=0;
            while (cnt < 3) {
                Console.Out.WriteLine("try to find actions written in the space ..... attempt " + cnt);
                GenericAction ga = new GenericAction();
                ga.State = FactState.RULE_PROCESSED;
                GenericAction[] results = spaceProxy.ReadMultiple<GenericAction>(ga);
                if (results.Length > 0)
                {
                    foreach (GenericAction res in results)
                    {
                        Console.Out.WriteLine(res.ToString());
                    }
                }
                else
                {
                    Console.Out.WriteLine("no results");
                }
                cnt++;


                Thread.Sleep(2000);
            }
            */
            /*
            SqlQuery<WagerFact> query = new SqlQuery<WagerFact>("state = 2");
            WagerFact[] checkFacts = spaceProxy.ReadMultiple<WagerFact>(query);
            if (checkFacts.Length > 0)
            {
                Console.Out.WriteLine("I got results");
            }
            else
            {
                Console.Out.WriteLine("no results");
            }

            */
            Console.In.Read();

        }

        public static Customer GenerateCustomer(int id)
        {
            Customer output = new Customer();

            output.Id = id+1000000;
            output.Name = "Maor T" + id;
            output.Address = id + " test street testerville Israel";
            output.MobilePhone = "054-555666" + id;
            output.NetworkId = DataGenerator.GenerateIntNetworkID;
            Console.Out.WriteLine("Customer created " + output.ToString());
            return output;
        }
    }
}
