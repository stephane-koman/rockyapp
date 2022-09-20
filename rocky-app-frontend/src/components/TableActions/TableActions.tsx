import React from "react";
import { ETableActionType } from "../../utils/enums/global.enum";
import { ITableActions } from "../../utils/interfaces/global.interface";
import TableActionsButton from "./actions/TableActionsButton";
import TableActionsDropdown from "./actions/TableActionsDropdown";

const TableActions = (props: ITableActions) => {
  return (
    <div className="ant-table-actions">
      {(!props.type || props.type === ETableActionType.BUTTON) && (
        <TableActionsButton {...props} />
      )}

      {props.type === ETableActionType.DROPDOWN && (
        <TableActionsDropdown {...props} />
      )}
    </div>
  );
};

export default TableActions;
