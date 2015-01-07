using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    [SpaceClass(AliasName = "com.c123.demo.real.GenericAction")]
    [Serializable]
    public class GenericAction
    {
        public GenericAction()
        {
        }

        [SpaceProperty(AliasName = "updateBalanceReason")]
        public int? UpdateBalanceReason { get; set; }

        [SpaceProperty(AliasName = "amount")]
        public decimal? Amount { get; set; }

        [SpaceProperty(AliasName = "state")]
        public FactState? State { get; set; }

        [SpaceID(AutoGenerate = true)]
        [SpaceProperty(AliasName = "id")]
        public string Id { get; set; }

        [SpaceProperty(AliasName = "fact", StorageType = StorageType.Object)]
        public BaseFact Fact { get; set; }

        [SpaceProperty(AliasName = "actionType")]
        public ActionType? ActionType { get; set; }

        [SpaceProperty(AliasName = "processTime")]
        public long? ProcessTime { get; set; }


        public override string ToString()
        {
            return "GenericAction (Id=" + Id + ",UpdateBalanceReason=" + UpdateBalanceReason + ",Amount=" + Amount + ",State=" + State + ",Fact=" + Fact + ",ActionType=" + ActionType + ",ProcessTime=" + ProcessTime + ")";
        }
    }
}
