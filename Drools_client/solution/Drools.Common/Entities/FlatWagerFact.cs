using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    [SpaceClass(AliasName = "com.c123.demo.model.facts.FlatWagerFact")]
    [Serializable]
    public class FlatWagerFact
    {

        public FlatWagerFact() { }

        [SpaceID(AutoGenerate = false)]
        [SpaceProperty(AliasName = "id")]
        public string Id { get; set; }
        
        [SpaceProperty(AliasName = "networkId")]
        public int? NetworkId { get; set; }

        [SpaceProperty(AliasName = "customerId")]
        public int? CustomerId { get; set; }

        [SpaceProperty(AliasName = "actualAmount")]
        public decimal ActualAmount { get; set; }

        [SpaceProperty(AliasName = "state")]
        public FactState State { get; set; }

        [SpaceProperty(AliasName = "correlationId")]
        public Guid CorrelationID { get; set; }

        [SpaceProperty(AliasName = "skinId")]
        public int? SkinId { get; set; }

        [SpaceProperty(AliasName = "date")]
        public DateTime? Date { get; set; }

        [SpaceProperty(AliasName = "offering")]
        public int? Offering { get; set; }

        [SpaceProperty(AliasName = "fundsType")]
        public FundsType FundsType { get; set; }

        [SpaceProperty(AliasName = "operationSourceApplication")]
        public int OperationSourceApplication { get; set; }

        [SpaceProperty(AliasName = "requestCurrencyCode")]
        public string RequestCurrencyCode { get; set; }

        [SpaceProperty(AliasName = "routingId")]
        [SpaceRouting]
        public int RoutingId { get; set; }

        [SpaceProperty(AliasName = "requestReference")]
        public Guid RequestReference { get; set; }

        public override string ToString()
        {
            // return "WagerFact (Id=" + Id + ",NetworkId=" + NetworkId + ",CustomerId=" + CustomerId + ",ActualAmount=" + ActualAmount + ",State=" + State + ",Date=" + Date.ToString() + ",Action=" + Action + ")";
            return "WagerFact (Id=" + Id + ",NetworkId=" + NetworkId + ",CustomerId=" + CustomerId + ",ActualAmount=" + ActualAmount + ",State=" + State + ",Date=" + Date.ToString()
               + ",Offering=" + Offering + ",FundsType=" + FundsType + ",OperationSourceApplication=" + OperationSourceApplication + ",RequestCurrencyCode=" + RequestCurrencyCode 
               + ",RoutingId=" + RoutingId + ")";
        } 
    }
}
