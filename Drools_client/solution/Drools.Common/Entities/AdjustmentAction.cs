using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using GigaSpaces.Core.Metadata;

namespace BillBuddy.Common.Entities
{
    [Serializable]
    public class AdjustmentAction
    {
        public AdjustmentAction() {
            UpdateBalanceReason = 0;
            TheActionState = ActionState.NONE;
        }

        [SpaceProperty(AliasName = "updateBalanceReason")]
        public int? UpdateBalanceReason { get; set; }

        [SpaceProperty(AliasName = "amount")]
        public double? Amount { get; set; }

        [SpaceProperty(AliasName = "actionState")]
        public ActionState? TheActionState { get; set; }

        public override string ToString()
        {
            return "AdjustmentAction (updateBalanceReason=" + UpdateBalanceReason + ",Amount=" + Amount + ",TheActionState=" + TheActionState+")";
        }


    }
}
