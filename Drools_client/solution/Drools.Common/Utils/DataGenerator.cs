using Drools.Common.Entities;
using System;

namespace Drools.Common.Utils
{
    public class DataGenerator
    {

        private static readonly Random randomizer = new Random();

        public static int GenerateIntID
        {
            get { return randomizer.Next(20)+1; }
        }

        public static int GenerateSkinID
        {
            get { return randomizer.Next(3) + 1; }
        }

        public static double GenerateDoubleAmount
        {
            get { return randomizer.NextDouble()*500; }
        }

        public static int GenerateIntCustomerID
        {
            get { return randomizer.Next(9) + 1; }
        }

        public static FundsType GenerateFundsType
        {
            

            get
            {
                double val = randomizer.NextDouble();
                if (val < 0.15)
                {
                    return FundsType.None;
                }
                if (val < 0.45)
                {
                    return FundsType.RealFunds;
                }
                if (val < 0.65)
                {
                    return FundsType.LoyaltyPoints;
                }
                if (val < 0.85)
                {
                    return FundsType.BonusFunds;
                }
                return FundsType.Bonus2Funds;

            }
            //return randomizer.NextDouble() * 500
        }
    }
}
