using System;

namespace Drools.Common.Entities
{
    [Flags]
    public enum FundsType
    {
        None=0, RealFunds=1, BonusFunds=2, LoyaltyPoints=4, Bonus2Funds=16
    }
}
