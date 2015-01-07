using GigaSpaces.Core.Metadata;

namespace Drools.Common.Entities
{
    [SpaceClass(AliasName = "com.c123.demo.real.WagerFact")]
    public class WagerFact : BaseWithdrawFact 
    {
        public WagerFact()
        {
        }

        public WagerFact(BaseWithdrawFact originFact) :
            base(originFact)
        {
        }

        public override string ToString()
        {
            // return "WagerFact (Id=" + Id + ",NetworkId=" + NetworkId + ",CustomerId=" + CustomerId + ",ActualAmount=" + ActualAmount + ",State=" + State + ",Date=" + Date.ToString() + ",Action=" + Action + ")";
            return "WagerFact (Id=" + Id + ",NetworkId=" + NetworkID + ",CustomerId=" + CustomerID + ",ActualAmount=" + ActualAmount + ",State=" + FactState + ",Date=" + DateTime.ToString()
               + ",SkinID=" + SkinID + ",Offering=" + Offering + ",FundsType=" + FundsType + ",OperationSourceApplication=" + OperationSourceApplication + ",RequestCurrencyCode=" + RequestCurrencyCode
               + ",RoutingId=" + RoutingId + ",UpdateBalanceReason=" + UpdateBalanceReason+ ",RequestReference=" + RequestReference + ")";
        } 
    }
}
