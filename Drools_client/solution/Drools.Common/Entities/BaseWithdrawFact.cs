using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    [Serializable]
    [SpaceClass(AliasName = "com.c123.demo.real.BaseWithdrawFact")]
    public class BaseWithdrawFact : BaseAccountOperationFact
    {
        public BaseWithdrawFact()
        {

        }

        public BaseWithdrawFact(BaseWithdrawFact originFact)
            : base(originFact)
        {
            FundsType = originFact.FundsType;
            Offering = originFact.Offering;
            OperationSourceApplication = originFact.OperationSourceApplication;
            RequestCurrencyCode = originFact.RequestCurrencyCode;
            UpdateBalanceReason = originFact.UpdateBalanceReason;
        }
        [SpaceProperty(AliasName = "offering")]
        public int? Offering { get; set; }
        [SpaceProperty(AliasName = "fundsType")]
        public FundsType? FundsType { get; set; }
        [SpaceProperty(AliasName = "updateBalanceReason")]
        public int? UpdateBalanceReason { get; set; }
        [SpaceProperty(AliasName = "operationSourceApplication")]
        public int? OperationSourceApplication { get; set; }
        [SpaceProperty(AliasName = "requestCurrencyCode")]
        public string RequestCurrencyCode { get; set; }

        // To verify that we will not duplicate facts, this is the unique definition of a fact.
        protected override object[] IdComponents
        {
            get
            {
                return new object[]
                {
                    this.NetworkID, this.RequestReference
                };
            }
        }
    }
}
